package com.barbershop.controllers.style;

import javafx.scene.Scene;

public class Colors {
    public static boolean isDark = false;
    public static String primary = "#56097a"; 
    public static String secondary = "#34054a"; 
    public static String forground = "#c052f2"; 
    public static String background = "#242424";
    public static String background2 = "#1a1a1a";  
    public static String background3 = "BLACK";  
    public static String text = "WHITE";  
    public static String text2 = "WHITESMOKE"; 

    public static void setTheme(boolean is_Dark){
        isDark = is_Dark;
        if (is_Dark) {
            primary = "#56097a"; 
            secondary = "#34054a"; 
            forground = "#c052f2"; 

            background = "#242424";
            background2 = "#1a1a1a";  
            background3 = "BLACK";  

            text = "WHITE";  
            text2 = "WHITE"; 
        } else {
            primary = "LIGHTSEAGREEN"; 
            secondary = "#148c86";

            forground = "WHITESMOKE";

            background = "WHITESMOKE";
            background2 = "#f0f0f0";  
            background3 = "GREY"; 

            text = "BLACK";  
            text2 = "WHITE"; 
        }
    }

    public static void applyColorsToScene(Scene scene) {
        String style =
                ".root { -fx-background-color: " + background + "; -fx-text-fill: " + text + "; }\n" +
                        ".button { -fx-background-color: " + primary + "; -fx-text-fill: " + text2 + "; }\n" +
                        ".table-view { -fx-background-color: " + background2 + "; }\n" +
                        ".label { -fx-text-fill: " + text + "; }\n" +
                        ".text-field { -fx-background-color: " + background3 + "; -fx-text-fill: " + text2 + "; }";

        scene.getRoot().setStyle(style);
    }


}
