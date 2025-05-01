package com.barbeshop.controllers;

import com.barbershop.controllers.database.DB;
import com.barbershop.models.UserSession;
import com.barbershop.views.Menu;
import com.barbershop.views.admin.AdminMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    void onLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            ResultSet rs = DB.select("SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
            if (rs.next()) {
                String role = rs.getString("role");
                String fullName = rs.getString("full_name");
                UserSession.set(username, role, fullName);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();

                if (role.equals("admin")) {
                    AdminMenu.open();
                } else {
                    Menu.open();
                }
            } else {
                errorLabel.setText("Неверный логин или пароль");
            }
        } catch (SQLException e) {
            errorLabel.setText("Ошибка подключения к БД");
            e.printStackTrace();
        }
    }
}
