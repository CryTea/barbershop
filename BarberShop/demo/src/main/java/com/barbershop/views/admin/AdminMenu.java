package com.barbershop.views.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminMenu {
    public static void open() {
        try {
            FXMLLoader loader = new FXMLLoader(AdminMenu.class.getResource("/com/barbershop/AdminMenu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Barbershop - Admin Panel");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
