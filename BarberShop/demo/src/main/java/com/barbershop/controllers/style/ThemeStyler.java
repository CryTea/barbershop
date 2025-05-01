package com.barbershop.controllers.style;

import javafx.scene.Scene;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThemeStyler {
    public static void applyColorsToScene(Scene scene) {
        try {
            String css = String.format(
                    ".root {\n" +
                            "    -fx-background-color: %s;\n" +
                            "    -fx-text-fill: %s;\n" +
                            "}\n" +
                            ".button {\n" +
                            "    -fx-background-color: %s;\n" +
                            "    -fx-text-fill: %s;\n" +
                            "}\n" +
                            ".label {\n" +
                            "    -fx-text-fill: %s;\n" +
                            "}\n" +
                            ".text-field {\n" +
                            "    -fx-background-color: %s;\n" +
                            "    -fx-text-fill: %s;\n" +
                            "}\n" +
                            ".table-view {\n" +
                            "    -fx-background-color: %s;\n" +
                            "}\n",
                    Colors.background, Colors.text,
                    Colors.primary, Colors.text,
                    Colors.text,
                    Colors.background2, Colors.text,
                    Colors.background2
            );

            // Создаём временный файл стилей
            Path tempFile = Files.createTempFile("dynamic-style", ".css");
            FileWriter writer = new FileWriter(tempFile.toFile());
            writer.write(css);
            writer.close();

            // Очищаем и применяем
            scene.getStylesheets().clear();
            scene.getStylesheets().add(tempFile.toUri().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
