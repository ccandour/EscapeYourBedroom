package com.example.escapeyourbedroom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    static Connection connection;
    public static void connectToDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:inventory.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    // That's something we use in rendering items in inventory
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        String sql = "SELECT field_id, filename FROM inventory";

        try (
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql)){

            // loop through the result set
            while (result.next()) {
                if (result.getString("filename") == null) continue;
                String imagePath = "file:assets/" + result.getString("filename");
                items.add(imagePath);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    // Check if an item is already picked up
    public static boolean isItemPickedUp(String itemName) {
        String sql = "SELECT filename FROM inventory";

        try (
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql)){

            // Loop through the item names in inventory, checking if any matches
            while (result.next()) {
                String filename = result.getString("filename");

                // If the item is already in inventory return true
                if (!(filename == null) && filename.equals(itemName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void addItemToInventory(String itemName) {
        // Check if we didn't already pick up that item for good measure
        if (isItemPickedUp(itemName)) return;

        // Add our item to the first available slot
        String sql = String.format("UPDATE inventory " +
                "SET filename = '%s' " +
                // this is quite the mouthful
                "WHERE field_id = (SELECT field_id FROM inventory WHERE filename IS NULL ORDER BY field_id LIMIT 1)", itemName);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeItemFromInventory(String itemName) {
        String sql = String.format("UPDATE inventory " +
                "SET filename = NULL WHERE filename = '%s'", itemName);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Check at what progression point the user is
    public static boolean checkProgression(String progressionPoint) {
        String sql = "SELECT name, value FROM progression";

        try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)){

            // Loop through the item names in inventory, checking if any matches
            while (result.next()) {
                String name = result.getString("name");
                int value = result.getInt("value");
                if (!(name == null) && name.equals(progressionPoint) && value == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Update progression point
    public static void changeProgression(String progressionPoint) {
        String sql = String.format("UPDATE progression " +
                "SET value = 1 WHERE name = '%s'", progressionPoint);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static int checkLocksOpen() {
        int locksOpen = 0;
        String sql = "SELECT name, value FROM progression";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                String name = result.getString("name");
                int value = result.getInt("value");

                // If the currently checked item is a lock, check which one. If it's "higher tier" than last used one, update progression
                if (name.startsWith("lock")) {
                    // 6th char because this is the char that determines lock number in e.g. "lock_2" (I think, IDK this is Peter's code)
                    int sixthChar = Integer.parseInt(String.valueOf(name.charAt(5)));

                    if (name.startsWith("lock") && value == 1 && sixthChar > locksOpen) {
                        locksOpen = sixthChar;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return locksOpen;
    }

    // Reset the database, essentially reset the gamestate
    public static void resetDB() {
        String inventory = "UPDATE inventory " +
                "SET filename = NULL";

        String progression = "UPDATE progression " +
                "SET value = 0";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(inventory);
            statement.executeUpdate(progression);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}