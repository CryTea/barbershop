package com.barbershop.views.admin;

import com.barbershop.controllers.style.Colors;
import com.barbershop.controllers.style.HoverController;
import com.barbershop.models.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMenuController {

    private String page_selected = "Dashboard";

    @FXML private Pane contentPane;
    @FXML private Button dashboardButton;
    @FXML private Button manageUsersButton;
    @FXML private Button themeButton;
    @FXML private Button exitButton;
    @FXML private Pane menuBar;

    @FXML
    public void initialize() {
        if (!Colors.isDark) {
            themeButton.setText("🌙");
            themeButton.setTooltip(new Tooltip("Switch to Dark Mode."));
            themeButton.getTooltip().setFont(new Font(13));
        } else {
            themeButton.setText("☀");
            themeButton.setTooltip(new Tooltip("Switch to Light Mode."));
            themeButton.getTooltip().setFont(new Font(13));
        }

        menuBar.setStyle("-fx-background-color: " + Colors.primary + ";");

        HoverController.addSideMenuHoverEffect(themeButton);
        HoverController.addSideMenuHoverEffect(exitButton);
        HoverController.addSideMenuHoverEffect(dashboardButton);
        HoverController.addSideMenuHoverEffect(manageUsersButton);

        switchPage();
    }

    @FXML
    void handleDashboardButton(ActionEvent event) {
        page_selected = "Dashboard";
        switchPage();
    }

    @FXML
    void handleUsersButton(ActionEvent event) {
        page_selected = "Managers"; // Менеджеры
        switchPage();
    }

    @FXML
    void handleThemeButton(ActionEvent event) {
        Colors.setTheme(!Colors.isDark);

        if (!Colors.isDark) {
            themeButton.setText("🌙");
            themeButton.setTooltip(new Tooltip("Switch to Dark Mode."));
        } else {
            themeButton.setText("☀");
            themeButton.setTooltip(new Tooltip("Switch to Light Mode."));
        }

        menuBar.setStyle("-fx-background-color: " + Colors.primary + ";");

        Scene scene = themeButton.getScene();
        Colors.applyColorsToScene(scene);
    }



    @FXML
    void handleExitButton(ActionEvent event) {
        try {
            // Закрыть текущее окно
            Stage currentStage = (Stage) exitButton.getScene().getWindow();
            currentStage.close();

            // Очистить сессию
            UserSession.clear();

            // Открыть окно авторизации
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barbershop/Login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Авторизация");
            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void switchPage() {
        String fxmlFile;
        switch (page_selected) {
            case "Managers":
                fxmlFile = "Managers.fxml";
                break;
            case "Dashboard":
            default:
                fxmlFile = "Dashboard.fxml";
                break;
        }

        try {
            Parent content = FXMLLoader.load(getClass().getResource("/com/barbershop/" + fxmlFile));
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
