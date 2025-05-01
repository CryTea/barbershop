package com.barbershop.models;

public class UserSession {
    private static String username;
    private static String role;
    private static String fullName;

    public static void set(String user, String r, String name) {
        username = user;
        role = r;
        fullName = name;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void clear() {
        username = null;
        role = null;
        fullName = null;
    }
}
