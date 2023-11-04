package com.example.escapeyourbedroom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;

public class SpriteEvents {
    // Background darken
    public static Rectangle darkenBackground = new Rectangle(WIDTH, HEIGHT, Color.rgb(0, 0, 0, 0.5));
    // Safe things
    public static List<ClickableSprite> safeNumpadButtons = new ArrayList<>();
    public static ImageView keypad = new ImageView("file:assets/safe_numpad.png");
    public static boolean isSafeUnlocked;
    // Inventory things
    static List<ClickableSprite> items = new ArrayList<>();
    public static ImageView inventory = new ImageView("file:assets/inventory.png");
    // Door things
    public static ImageView lock = new ImageView("file:assets/lock1_closeup.png");
    public static int locksOpen = 0;

    public static void initialize() {
        darkenBackground.setVisible(false);

        // I've split initialization into each sprite, so it's less cluttered
        initializeSafe();
        initializeInventory();
        initializeLock();
    }

    // --- SAFE METHODS ---
    private static void initializeSafe() {
        keypad.setVisible(false);

        int temp = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1 ; j++) {
                temp += 1;
                String imagePath = "file:assets/safe_numpad_" + temp + ".png";
                safeNumpadButtons.add(new ClickableSprite(imagePath, String.valueOf(temp), (7 + 20) * 8 * j, (7 + 20) * 8 * i));
                safeNumpadButtons.get(safeNumpadButtons.size() - 1).hide();

            }
        }

        root.getChildren().add(keypad);

        // This has to be an array because javaFX shits itself when it sees a non-final variable inside a timeline, but you can still modify values in final arrays for some fucking reason?
        final String[] code = {""};

        for (ClickableSprite safeNumpadButton : safeNumpadButtons) {
            safeNumpadButton.setOnlyZoomOnHoover();
            safeNumpadButton.setOnMouseClicked(mouseEvent -> {
                code[0] += safeNumpadButton.name;

                if (code[0].equals("1943")) {
                    DatabaseHandler.changeProgression("safe");

                    for (ClickableSprite button : safeNumpadButtons) {
                        button.hide();
                    }
                    keypad.setVisible(false);
                    nextBackground();
                    prevBackground();
                    exitButton.hide();
                    root.getChildren().remove(darkenBackground);
                    popoutMessage.showMessage("The safe unlocks!");
                }
                else if (code[0].length() == 4){
                    popoutMessage.showMessage("Wrong code...", 1);
                    code[0] = "";
                }
            });
        }
    }

    public static void safeShowNumpad() {
        setDarkenBackground();
        keypad.setVisible(true);
        keypad.toFront();
        for (ClickableSprite safeNumpadButton : safeNumpadButtons) {
            safeNumpadButton.show();
        }
        exitButton();
    }

    // --- INVENTORY METHODS ---
    private static void initializeInventory() {
        inventory.setVisible(false);
        refreshInventory();
        root.getChildren().add(inventory);
    }

    private static void refreshInventory() {
        List<String> itemsPaths = DatabaseHandler.getAllItems();
        int currentItem = 0;
        items.clear();
        for (double i = -0.5; i <= 0.5; i++) {
            for (double j = -1.5; j <= 1.5; j++) {
                if (currentItem >= itemsPaths.size()) break;
                String currentItemName = itemsPaths.get(currentItem);
                items.add(new ClickableSprite(iconizeName(currentItemName),
                        filenameToLabel(currentItemName), (11 + 25) * 8 * j, ((7+25) * 8 * i)+48));
                        // Y adjusted to the difference of top and bottom margins of inventory background
                currentItem += 1;
            }
        }
    }

    public static void renderInventory() {
        refreshInventory();
        setDarkenBackground();
        inventory.setVisible(true);
        inventory.toFront();
        for (int i = 0; i < items.size(); i++) {
                ClickableSprite item = items.get(i);
                item.setOnlyLabelOnHoover();
                item.show();
        }
        exitButton();
    }

    // --- DOOR METHODS ---
    private static void initializeLock() {
        lock.setVisible(false);
        refreshLock();
        root.getChildren().add(lock);
    }

    private static void refreshLock() {
        locksOpen = DatabaseHandler.checkLocksOpen();
        lock.setImage(new Image("file:assets/lock" + (locksOpen+1) + "_closeup.png"));
    }

    public static void renderLock(ClickableSprite door) {
        setDarkenBackground();
        refreshLock();
        lock.setVisible(true);
        lock.toFront();
        keyHole(door);
        exitButton();
    }

    public static void keyHole(ClickableSprite door) {
        ClickableSprite keyhole = new ClickableSprite("file:assets/keyhole.png", "Open using Key " + (locksOpen+1), 0, 132);
        keyhole.show();
        keyhole.setHighlightOnHover();
        keyhole.setOnMouseClicked(event -> {
            if (DatabaseHandler.isItemPickedUp("key_" + (locksOpen+1) + ".png")) {
                locksOpen++;
                DatabaseHandler.removeItemFromInventory("key_" + locksOpen + ".png");
                DatabaseHandler.changeProgression("lock_" + locksOpen);
                door.setImage(new Image("file:assets/door_" + locksOpen + ".png"));
                nextBackground();
                prevBackground();
                updateSpritesVisibility(currentScene, true);
                keyhole.hide();
                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("Lock opened!");
            }
            else {
                nextBackground();
                prevBackground();
                updateSpritesVisibility(currentScene, true);
                keyhole.hide();
                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("You need a right key for that!");
            }
        });
    }

    // Needed for exiting numpad / eq view
    public static void exitButton() {
        exitButton = new ClickableSprite("file:assets/exit_button.png", "Go back", 796, -360);
        exitButton.show();
        exitButton.setHighlightOnHover();
        exitButton.setOnMouseClicked(event -> {
            // Hide keypad and render the background once again
            nextBackground();
            prevBackground();
            updateSpritesVisibility(currentScene, true);
            exitButton.hide();
            root.getChildren().remove(darkenBackground);
        });
    }

    // Hides all buttons and darkens the background
    public static void setDarkenBackground() {
        rightArrow.hide();
        leftArrow.hide();
        backpack.hide();
        darkenBackground.setVisible(true);
        darkenBackground.toFront();
        root.getChildren().add(darkenBackground);
    }

    //Get _icon version of a filename
    public static String iconizeName(String original) {
        StringBuilder iconized = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            if (original.charAt(i) == '.') {
                iconized.append("_icon.");
            }
            else iconized.append(original.charAt(i));
        }
        return iconized.toString();
    }

    // Used for inventory items' nameTags to avoid adding another column to the database
    public static String filenameToLabel(String fileName) {
        String temp = fileName;
        temp = temp.replace("file:assets/", "");
        temp = temp.replace(".png", "");
        temp = temp.replace("_"," ");
        String result = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        return result;
    }
}
