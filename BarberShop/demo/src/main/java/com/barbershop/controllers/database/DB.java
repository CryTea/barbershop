package com.barbershop.controllers.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DB {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/barbershop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    private static Connection transactionConnection = null;
    public static Connection conn;



    /** CREATE a DATABASE.
     * @param dbName Database name
     */
    public static void createDB(String dbName) {
        // В PostgreSQL база данных обычно создается через отдельное соединение
        try (Connection connection = getConnection("jdbc:postgresql://localhost:5432/postgres", DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName.toLowerCase());
            System.out.println("Database created successfully.");
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
        }
    }


    public static void startTransaction() throws SQLException {
        if (transactionConnection != null) {
            throw new SQLException("Transaction already in progress");
        }
        transactionConnection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
        transactionConnection.setAutoCommit(false);
    }

    public static void commitTransaction() throws SQLException {
        if (transactionConnection != null) {
            transactionConnection.commit();
            transactionConnection.close();
            transactionConnection = null;
        }
    }

    public static void rollbackTransaction() {
        try {
            if (transactionConnection != null) {
                transactionConnection.rollback();
                transactionConnection.close();
                transactionConnection = null;
            }
        } catch (SQLException e) {
            System.out.println("Error rolling back transaction: " + e.getMessage());
        }
    }

    /** CREATE a TABLE in a database
     * @param dbName database name
     * @param tableName table name
     * @param attributes table attributes
     */
    public static void createTable(String dbName, String tableName, String attributes) {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName.toLowerCase() + " (" + attributes + ");";
        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
        }
    }

    /** INSERT a ROW in a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param values values to be inserted
     * @return last inserted id
     */
    public static int insertRow(String dbName, String tableName, String values) {
        // Изменено "RETURNING id" на "RETURNING client_id" для таблицы clients
        String returningColumn = tableName.equalsIgnoreCase("clients") ? "client_id" : "id";
        String query = "INSERT INTO " + tableName.toLowerCase() + " VALUES " + values + " RETURNING " + returningColumn + ";";

        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        }
    }

    /** UPDATE a ROW in a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param setClause new values
     * @param conditions where the values will be updated condition
     */
    public static void updateRow(String dbName, String tableName, String setClause, String conditions) {
        String query = "UPDATE " + tableName.toLowerCase() + " SET " + setClause + " WHERE " + conditions + ";";
        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
        }
    }

    /** DELETE a ROW from a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param conditions conditions to identify the row(s) to delete
     */
    public static void deleteRow(String dbName, String tableName, String conditions) {
        String query = "DELETE FROM " + tableName.toLowerCase() + " WHERE " + conditions + ";";
        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
        }
    }

    /** SELECT values from a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param attributes attibutes to be selected from the table
     * @param conditions where condition
     * @return List of values selected
     */
    public static List<List<String>> selectRow(String dbName, String tableName, String attributes, String conditions) {
        List<List<String>> resultList = new ArrayList<>();
        String query = "SELECT " + attributes + " FROM " + tableName.toLowerCase();
        if (!conditions.isEmpty()) {
            query += " " + conditions;
        }
        query += ";";

        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
        }
        return resultList;
    }

    public static int executeInsertWithReturnId(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to get generated ID");
    }

    public static ResultSet select(String query) {
        try {
            Connection conn = getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



}