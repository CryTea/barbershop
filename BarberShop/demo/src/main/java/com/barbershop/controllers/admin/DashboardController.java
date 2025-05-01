package com.barbershop.controllers.admin;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;

    @FXML private Label currentUserLabel;
    @FXML private Label managerCountLabel;
    @FXML private Label totalIncomeLabel;
    @FXML private Label clientCountLabel;
    @FXML private Label eventCountLabel;

    @FXML
    public void initialize() {
        loadStats();
        loadBarChart();
        loadPieChart();
    }

    private void loadStats() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            Statement stmt = conn.createStatement();

            ResultSet user = stmt.executeQuery("SELECT full_name, role FROM users WHERE username = 'admin'");
            if (user.next()) {
                currentUserLabel.setText(user.getString("full_name") + " (" + user.getString("role") + ")");
            }

            ResultSet res1 = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'manager'");
            if (res1.next()) managerCountLabel.setText(res1.getInt(1) + "");

            ResultSet res2 = stmt.executeQuery("SELECT SUM(total) FROM invoice");
            if (res2.next()) totalIncomeLabel.setText(String.format("%.2f ₽", res2.getDouble(1)));

            ResultSet res3 = stmt.executeQuery("SELECT COUNT(*) FROM client");
            if (res3.next()) clientCountLabel.setText(res3.getInt(1) + "");

            ResultSet res4 = stmt.executeQuery("SELECT COUNT(*) FROM event");
            if (res4.next()) eventCountLabel.setText(res4.getInt(1) + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Посещения");

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT TO_CHAR(date_time, 'Day') AS weekday, COUNT(*) " +
                            "FROM event " +
                            "GROUP BY weekday " +
                            "ORDER BY MIN(date_time)");

            while (rs.next()) {
                String weekday = rs.getString(1).strip();
                int count = rs.getInt(2);
                series.getData().add(new XYChart.Data<>(weekday, count));
            }

            barChart.getData().clear();
            barChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPieChart() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            Statement stmt = conn.createStatement();

            // Получить общий доход
            ResultSet totalResult = stmt.executeQuery(
                    "SELECT SUM(i.total) " +
                            "FROM service s JOIN event e ON s.service_id = e.service_id " +
                            "JOIN invoice i ON i.invoice_id = e.invoice_id");

            double total = 0;
            if (totalResult.next()) {
                total = totalResult.getDouble(1);
            }

            // Получить доход по категориям
            ResultSet rs = stmt.executeQuery(
                    "SELECT s.name, SUM(i.total) " +
                            "FROM service s JOIN event e ON s.service_id = e.service_id " +
                            "JOIN invoice i ON i.invoice_id = e.invoice_id " +
                            "GROUP BY s.name");

            pieChart.getData().clear();

            while (rs.next()) {
                String serviceName = rs.getString(1);
                double income = rs.getDouble(2);

                double percentage = total > 0 ? (income / total) * 100 : 0;
                String label = String.format("%s (%.1f%%)", serviceName, percentage);

                PieChart.Data slice = new PieChart.Data(label, income);
                pieChart.getData().add(slice);

                // Анимация
                slice.getNode().setScaleX(0);
                slice.getNode().setScaleY(0);

                ScaleTransition st = new ScaleTransition(Duration.millis(800), slice.getNode());
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(1);
                st.setToY(1);
                st.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
