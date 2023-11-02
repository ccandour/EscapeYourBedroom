package com.example.escapeyourbedroom;

import java.sql.*;

public class DatabaseHandler {
    static Connection connection;
    public static void main( String args[] ) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            connection = c;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened inventory database successfully");
        selectAll();
        System.out.println(addItemToInventory("key5.png"));
    }

    // That's something we'll use in rendering items in inventory
    public static void selectAll() {
        String sql = "SELECT field_id, filename FROM inventory";

        try (
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)){

            // loop through the result set
            while (result.next()) {
                System.out.println(result.getInt("field_id") +  "\t" +
                        result.getString("filename"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to check if we already picked up an item, we provide an itemName, like "key1.png"
    public static boolean isItemPickedUp(String itemName) {
        String sql = "SELECT filename FROM inventory";

        try (
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql)){

            // Loop through the item names in inventory, checking if any matches
            while (result.next()) {
                String filename = result.getString("filename");
                if (!(filename == null) && filename.equals(itemName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Method to add an item to the inventory, we provide an itemName, like "key1.png"
    public static boolean addItemToInventory(String itemName) {
        // Check if we didn't already pick up that item for good measure
        if (isItemPickedUp(itemName)) return false;

        // Add our item to the first available slot
        String sql = String.format("UPDATE inventory " +
                "SET filename = '%s' " +
                "WHERE field_id = (SELECT field_id FROM inventory WHERE filename IS NULL ORDER BY field_id LIMIT 1)", itemName);

        try {
                Statement statement = connection.createStatement();
                statement.execute(sql);
                return true;

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
