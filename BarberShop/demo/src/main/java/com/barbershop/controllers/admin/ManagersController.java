// ManagersController.java
package com.barbershop.controllers.admin;

import com.barbershop.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class ManagersController {

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TextField searchField;
    @FXML private Label adminsCountLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label lastAdminLoginLabel;
    @FXML private Label currentTimeLabel;

    private ObservableList<User> allUsers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fullNameColumn.setCellValueFactory(cell -> cell.getValue().fullNameProperty());
        usernameColumn.setCellValueFactory(cell -> cell.getValue().usernameProperty());

        loadManagers();
        loadStats();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTable(newVal));

        if (currentTimeLabel != null) {
            currentTimeLabel.setText(java.time.LocalDateTime.now().toString());
        }
    }

    private void loadManagers() {
        allUsers.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE role = 'manager'");
            while (rs.next()) {
                allUsers.add(new User(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("username")));
            }
            tableView.setItems(allUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        filterTable(searchField.getText());
    }

    private void loadStats() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'admin'");
            if (rs1.next() && adminsCountLabel != null) adminsCountLabel.setText(rs1.getInt(1) + "");

            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs2.next() && totalUsersLabel != null) totalUsersLabel.setText(rs2.getInt(1) + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            tableView.setItems(allUsers);
            return;
        }
        ObservableList<User> filtered = FXCollections.observableArrayList();
        for (User u : allUsers) {
            if (u.getFullName().toLowerCase().contains(keyword.toLowerCase()) || u.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(u);
            }
        }
        tableView.setItems(filtered);
    }

    @FXML
    private void handleAddManager() {
        if (openModal("/com/barbershop/AddManagerModal.fxml")) {
            loadManagers();
        }
    }

    @FXML
    private void handleEditManager() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        EditManagerController.selectedUser = selected;
        if (openModal("/com/barbershop/EditManagerModal.fxml")) {
            loadManagers();
        }
    }

    private boolean openModal(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Manager");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleDelete() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Выберите менеджера для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Удалить выбранного менеджера?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {

                PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
                stmt.setInt(1, selected.getUserId());
                stmt.executeUpdate();

                loadManagers();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка при удалении.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
