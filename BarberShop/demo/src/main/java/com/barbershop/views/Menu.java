package com.barbershop.views;

import java.io.IOException;

import com.barbershop.App;
import com.barbershop.controllers.style.Colors;
import com.barbershop.controllers.style.HoverController;
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

public class Menu{
    String page_selected = "Calendar";

    @FXML private Pane contentPane;
    private void loadContent(String fxmlFileName) {
        try {
            // Load the content from the specified FXML file
            Pane newContent = FXMLLoader.load(getClass().getResource("/com/barbershop/" + fxmlFileName));
            
            // Replace the content in the pane with the new content
            contentPane.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void open() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Menu.class.getResource("/com/barbershop/Menu.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Barbershop - Manager Panel");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button agendaButton;
    @FXML
    private void handleAgendaButtonClick(ActionEvent event) {
        page_selected = "Calendar";
        switchPage();
    }

    @FXML
    private Button clientsButton;
    @FXML
    private void handleClientsButtonClick(ActionEvent event) {
        page_selected = "Clients";
        switchPage();
    }

    @FXML
    private Button stockButton;
    @FXML
    private void handleStockButtonClick(ActionEvent event) {
        page_selected = "Products";
        switchPage();
    }

    @FXML
    private Button servicesButton;
    @FXML
    private void handleServicesButtonClick(ActionEvent event) {
        page_selected = "Services";
        switchPage();
    }

    @FXML
    private Button exitButton;
    @FXML
    private void handleExitButtonClick(ActionEvent event) {
        // Закрыть текущее окно
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

        // Открыть окно авторизации
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/barbershop/Login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Авторизация");
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML 
    private Button themeButton;
    @FXML
    private void handleThemeButtonClick(ActionEvent event) {
        Colors.setTheme(!Colors.isDark);

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); // Закрыть старую тему
        Menu.open();   // Открыть заново, с сохранением UserSession
    }



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
        menuBar.setStyle("-fx-background-color: "+Colors.primary+";");
        HoverController.addSideMenuHoverEffect(themeButton);
        switchPage();
    }

    private void switchPage(){
        switch (page_selected) {
            case "Calendar":
            loadContent("Calendar.fxml");
            HoverController.noSideMenuHoverEffect(agendaButton);
            HoverController.addSideMenuHoverEffect(stockButton);
            HoverController.addSideMenuHoverEffect(servicesButton);
            HoverController.addSideMenuHoverEffect(exitButton);
            HoverController.addSideMenuHoverEffect(clientsButton);
            break;
            case "Clients":
            loadContent("Clients.fxml");
            HoverController.noSideMenuHoverEffect(clientsButton);
            HoverController.addSideMenuHoverEffect(agendaButton);
            HoverController.addSideMenuHoverEffect(stockButton);
            HoverController.addSideMenuHoverEffect(servicesButton);
            HoverController.addSideMenuHoverEffect(exitButton);
            break;
            case "Products":
            loadContent("Products.fxml");
            HoverController.noSideMenuHoverEffect(stockButton);
            HoverController.addSideMenuHoverEffect(agendaButton);
            HoverController.addSideMenuHoverEffect(servicesButton);
            HoverController.addSideMenuHoverEffect(exitButton);
            HoverController.addSideMenuHoverEffect(clientsButton);
            break;
            case "Services":
            loadContent("Services.fxml");
            HoverController.noSideMenuHoverEffect(servicesButton);
            HoverController.addSideMenuHoverEffect(agendaButton);
            HoverController.addSideMenuHoverEffect(stockButton);
            HoverController.addSideMenuHoverEffect(exitButton);
            HoverController.addSideMenuHoverEffect(clientsButton);
            break;
            case "Exit":
            HoverController.addSideMenuHoverEffect(agendaButton);
            HoverController.addSideMenuHoverEffect(stockButton);
            HoverController.addSideMenuHoverEffect(servicesButton);
            HoverController.addSideMenuHoverEffect(exitButton);
            HoverController.addSideMenuHoverEffect(clientsButton);
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            break;
        
            default:
                page_selected = "Calendar";
            break;
        }
    }
    
}
