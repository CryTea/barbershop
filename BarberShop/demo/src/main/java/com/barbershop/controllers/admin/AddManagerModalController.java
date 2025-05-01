package com.barbershop.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddManagerModalController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    @FXML
    public void initialize() {
        addButton.setOnAction(e -> handleAdd());
        cancelButton.setOnAction(e -> ((Stage) cancelButton.getScene().getWindow()).close());
    }

    private void handleAdd() {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Все поля должны быть заполнены");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (full_name, username, password, role) VALUES (?, ?, ?, 'manager')");
            stmt.setString(1, fullName);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();

            ((Stage) addButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось добавить менеджера");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
