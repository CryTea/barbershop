package com.barbershop.controllers.admin;

import com.barbershop.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EditManagerController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public static User selectedUser;

    @FXML
    public void initialize() {
        if (selectedUser != null) {
            fullNameField.setText(selectedUser.getFullName());
            usernameField.setText(selectedUser.getUsername());
        }
    }

    @FXML
    private void handleSave() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty()) {
            showAlert("ФИО и логин обязательны для заполнения.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/barbershop", "postgres", "1234")) {
            String sql;
            PreparedStatement stmt;
            if (!password.isEmpty()) {
                sql = "UPDATE users SET full_name = ?, username = ?, password = ? WHERE user_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fullName);
                stmt.setString(2, username);
                stmt.setString(3, password);
                stmt.setInt(4, selectedUser.getUserId());
            } else {
                sql = "UPDATE users SET full_name = ?, username = ? WHERE user_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fullName);
                stmt.setString(2, username);
                stmt.setInt(3, selectedUser.getUserId());
            }
            stmt.executeUpdate();
            close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка при сохранении изменений.");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}