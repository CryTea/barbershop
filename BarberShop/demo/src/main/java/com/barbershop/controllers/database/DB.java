package com.barbershop.controllers.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** @author Niema Alaoui Mdaghri
 * This class contains only static public methods for database managment, it can be called from anywhere!
 */
public class DB {
    /** CREATE a DATABASE.
     * @param dbName Database name
     */
    public static void createDB(String dbName){
        //create the .db file
        String jdbcUrl = "jdbc:sqlite:"+dbName.toUpperCase()+".db";
        try {
            //connect to the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            //print connection successful
            if (connection != null) {
                System.out.println("Database created successfully.");
                connection.close();
            }
        } catch (SQLException e) {
            //print connectino error
            System.out.println("SQLite ERROR: " + e.getMessage());
        }
    }

    /** CREATE a TABLE in a database
     * @param dbName database name
     * @param tableName table name
     * @param attributes table attributes
     */
    public static void createTable(String dbName, String tableName, String attributes){
        //create the query
        String query = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase() + " (" + attributes + ");";
        try {
            //get the database path
            String jdbcUrl = "jdbc:sqlite:"+dbName.toUpperCase()+".db";
            //create connection with the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            //create a statement that will wait for the query
            Statement statement = connection.createStatement();
            //execute the query
            statement.execute(query);
            //print query successful
            //System.out.println("Table "+tableName.toUpperCase()+" created successfully!");
            //close connection
            connection.close();
        } catch (SQLException e) {
            //print query error
            System.out.println("SQLite ERROR: " + e.getMessage());
        }
    }

    /** INSERT a ROW in a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param values values to be inserted
     * @return last inserted id
     */
    public static int insertRow(String dbName, String tableName, String values) {
        String query = "INSERT INTO " + tableName.toUpperCase() + " VALUES " + values + ";";
        int id = 0;
        try {
            // get the database path
            String jdbcUrl = "jdbc:sqlite:" + dbName.toUpperCase() + ".db";
            // create connection with the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            // create a statement that will wait for the query
            Statement statement = connection.createStatement();
            // execute the query
            statement.executeUpdate(query);
            // retrieve the last inserted row ID
            ResultSet rs = statement.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                id = rs.getInt(1);
            }
            // print query successful
            //System.out.println("Row inserted in table "+tableName.toUpperCase()+" successfully into table!");
            // close connection
            connection.close();
            return id;
        } catch (SQLException e) {
            // print query error
            System.out.println("SQLite ERROR: " + e.getMessage());
            return -1;
        }
    }

    /** UPDATE a ROW in a specific table i a database.
     * @param dbName database name
     * @param tableName table name
     * @param setClause new values
     * @param conditions where the values will be updated condition
     */
    public static void updateRow(String dbName, String tableName, String setClause, String conditions) {
        String query = "UPDATE " + tableName.toUpperCase() + " SET " + setClause + " WHERE " + conditions + ";";
        try {
            // Get the database path
            String jdbcUrl = "jdbc:sqlite:"+dbName.toUpperCase()+".db";
            // Create connection with the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            // Create a statement that will wait for the query
            Statement statement = connection.createStatement();
            // Execute the update query
            //int rowsAffected = 
            statement.executeUpdate(query);
            // Print query successful
            //System.out.println(rowsAffected + " row(s) updated in table " + tableName.toUpperCase() + " successfully!");
            // Close connection
            connection.close();
        } catch (SQLException e) {
            // Print query error
            System.out.println("SQLite ERROR: " + e.getMessage());
        }
    }

    /** DELETE a ROW from a specific table in a database.
     * @param dbName database name
     * @param tableName table name
     * @param conditions conditions to identify the row(s) to delete
     */
    public static void deleteRow(String dbName, String tableName, String conditions) {
        String query = "DELETE FROM " + tableName.toUpperCase() + " WHERE " + conditions + ";";
        try {
            // Get the database path
            String jdbcUrl = "jdbc:sqlite:"+dbName.toUpperCase()+".db";
            // Create connection with the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            // Enable foreign key constraints
            connection.createStatement().execute("PRAGMA foreign_keys=off;");
            // Create a statement that will wait for the query
            Statement statement = connection.createStatement();
            // Execute the delete query
            statement.executeUpdate(query);
            // Print query successful
            //System.out.println("Row(s) deleted from table " + tableName.toUpperCase() + " successfully!");
            // Close connection
            connection.close();
        } catch (SQLException e) {
            // Print query error
            System.out.println("SQLite ERROR: " + e.getMessage());
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
        String query = "SELECT " + attributes + " FROM " + tableName.toUpperCase() + " " + conditions + ";";
        try {
            //get the database path
            String jdbcUrl = "jdbc:sqlite:"+dbName.toUpperCase()+".db";
            //create connection with the database
            Connection connection = DriverManager.getConnection(jdbcUrl);
            //create a statement that will wait for the query
            Statement statement = connection.createStatement();
            //execute the query and store the result in a ResultSet
            ResultSet resultSet = statement.executeQuery(query);
            //retrieves the metadata for the result set
            //instance of ResultSetMetaData associated with the current ResultSet
            // use its methods to access information such as the number of columns, the names of the columns, the types of the columns, and other properties of the result set.
            ResultSetMetaData metaData = resultSet.getMetaData();
            //get the number of column
            int columnCount = metaData.getColumnCount();
            //Display & store the result of the query
            //System.out.println("\nRows selected from table "+tableName.toUpperCase()+" successfully!");
            while (resultSet.next()) {
                //Store each row individually
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                //add the row to the final list
                resultList.add(row);
                //Display the result of the query
                //System.out.println(Colors.BLACK+row);
            }
            //close connection
            connection.close();
        } catch (SQLException e) {
            //print query error
            System.out.println("SQLite ERROR: " + e.getMessage());
        }
        //return the query result
        return resultList;
    }
}
