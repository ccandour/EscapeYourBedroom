package com.example.escapeyourbedroom;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;
@SuppressWarnings("ClassEscapesDefinedScope")
public class Utilities {
    public static Rectangle darkenBackground = new Rectangle(WIDTH, HEIGHT, Color.rgb(0, 0, 0, 0.5));
    static Rectangle bg;
    public static Rectangle white = new Rectangle(WIDTH, HEIGHT, Color.rgb(255, 255, 255, 1));
    static ImageView victory1 = new ImageView("file:assets/victory1.png");
    static ImageView victory2 = new ImageView("file:assets/victory2.png");
    public static PopoutMessage popoutMessage;
    public static ClickableSprite exitButton;
    public static NameTag nameTag;

    public static void initialize() {
        darkenBackground.setVisible(false);
    }

    public static void nextBackground() {
        // Single line for cycling through the number of backgrounds (instead of doing some weird ifs)
        int nextScene = (currentScene + 1) % sceneBackgrounds.size();

        // Hide all sprites from the current background
        updateSpritesVisibility(currentScene, false);

        // Set the background to the next scene's background (also hides previous scene sprites so cool)
        sceneBackgrounds.get(nextScene).toFront();

        // Make all sprites from the next background visible
        updateSpritesVisibility(nextScene, true);

        // Update currentBackground
        currentScene = nextScene;
    }

    public static List<ClickableSprite> getSpritesFromScene(int scene) {
        return sceneSprites.get(scene);
    }


    public static void prevBackground() {
        // Single line for cycling through the number of backgrounds (instead of doing some weird ifs)
        int prevScene = (currentScene - 1 + sceneBackgrounds.size()) % sceneBackgrounds.size();

        // Hide all sprites from the current background
        updateSpritesVisibility(currentScene, false);

        // Set the background to the previous scene's background (also hides previous sprites so cool)
        sceneBackgrounds.get(prevScene).toFront();

        // Make all sprites from the next background visible
        updateSpritesVisibility(prevScene, true);

        // Update currentBackground
        currentScene = prevScene;
    }

    public static void updateSpritesVisibility(int scene, boolean isVisible) {
        // Update the visibility of the arrows
        if (isVisible) {
            rightArrow.show();
            leftArrow.show();
            backpack.show();
        }
        else {
            rightArrow.hide();
            leftArrow.hide();
            backpack.hide();
        }
        // Update the visibility of the sprites
        List<ClickableSprite> sprites = getSpritesFromScene(scene);
        for (ClickableSprite sprite : sprites) {
            if (isVisible) {
                sprite.show();
            }
            else {
                sprite.hide();
            }
        }
    }

    public static void reRenderBackground() {
        // A very professional way to zoom out
        nextBackground();
        prevBackground();
    }

    // Needed for exiting numpad / eq view
    public static void exitButton() {
        exitButton = new ClickableSprite("file:assets/exit_button.png", "Go back", 796, -360);
        exitButton.show();
        exitButton.setHighlightAndLabelOnHover();
        exitButton.setOnMouseClicked(event -> {
            // Hide keypad and render the background once again
            reRenderBackground();
            updateSpritesVisibility(currentScene, true);
            exitButton.hide();
            popoutMessage.hide();
            root.getChildren().remove(darkenBackground);
        });
    }

    public static void exitButton(ClickableSprite sprite) {
        exitButton = new ClickableSprite("file:assets/exit_button.png", "Go back", 796, -360);
        exitButton.show();
        exitButton.setHighlightAndLabelOnHover();
        exitButton.setOnMouseClicked(event -> {
            sprite.zoomOut();
            if (Utilities.bg != null) {
                root.getChildren().remove(Utilities.bg);
            }
        });
    }

    // Hides all buttons and darkens the background
    public static void darkenBackground() {
        rightArrow.hide();
        leftArrow.hide();
        backpack.hide();
        darkenBackground.setVisible(true);
        darkenBackground.toFront();
        root.getChildren().add(darkenBackground);
    }

    public static void renderBackground(String hex) {
        bg = new Rectangle(WIDTH, HEIGHT, Color.web(hex));
        root.getChildren().add(bg);
        bg.setVisible(true);
        bg.toFront();
    }

    public static void renderVictoryScreen() {
        Timeline victory = new Timeline();
        Timeline victoryFlashing = new Timeline();

        // Background fade-in
        KeyValue transparent = new KeyValue(white.opacityProperty(), 0.0);
        KeyValue opaque = new KeyValue(white.opacityProperty(), 1.0);

        KeyFrame start = new KeyFrame(Duration.ZERO, transparent);
        KeyFrame end = new KeyFrame(Duration.millis(2000), opaque);

        // Flashing text
        KeyValue vic1opq = new KeyValue(victory1.opacityProperty(), 1.0);
        KeyValue vic1tns = new KeyValue(victory1.opacityProperty(), 0.0);
        KeyValue vic2opq = new KeyValue(victory2.opacityProperty(), 1.0);
        KeyValue vic2tns = new KeyValue(victory2.opacityProperty(), 0.0);


        KeyFrame vic1trans = new KeyFrame(Duration.millis(1000), vic1opq);
        KeyFrame vic1opaque = new KeyFrame(Duration.millis(2000), vic1tns);
        KeyFrame vic2trans = new KeyFrame(Duration.millis(1000), vic2tns);
        KeyFrame vic2opaque = new KeyFrame(Duration.millis(2000), vic2opq);


        victory.getKeyFrames().addAll(start, end);
        victory.setCycleCount(1);

        victoryFlashing.getKeyFrames().addAll(vic1trans, vic1opaque, vic2trans, vic2opaque);
        victoryFlashing.setCycleCount(5);

        victory.setOnFinished(event -> {
            root.getChildren().add(victory1);
            victory1.setVisible(true);
            victory1.toFront();
            root.getChildren().add(victory2);
            victory2.setVisible(true);
            victory2.toFront();
            victoryFlashing.play();
        });

        victoryFlashing.setOnFinished(event -> {
            DatabaseHandler.resetDB();
            System.exit(1943);
        });

        root.getChildren().add(white);
        white.setVisible(true);
        white.toFront();
        victory.play();

    }

    //Get _icon version of a filename
    public static String iconizeName(String original) {
        return original.replace(".", "_icon.");
    }

    //Get _zoom version of a filename
    public static String zoomifyName(String original) {
        return original.replace(".", "_zoom.");
    }

    // Extract item name from file name (e.g. "file:assets/key_1.png" -> key1)
    // Used for inventory items' nameTags to avoid adding another column to the database
    public static String filenameToLabel(String fileName) {
        String temp = fileName;

        temp = temp.replace("file:assets/", "");
        temp = temp.replace(".png", "");
        temp = temp.replace("_"," ");

        return temp.substring(0, 1).toUpperCase() + temp.substring(1);
    }

    public static void renderChildSprite(String filename, int X, int Y) {
        String imagePath = "file:assets/" + filename;

        if (DatabaseHandler.isItemPickedUp(filename)) return;

        if (filename.startsWith("key")) {
            String removedDotPng = filename.replace(".png", "");
            String lock = removedDotPng.replace("key", "lock");
            if (DatabaseHandler.checkProgression(lock)) return;
        }

        childSprite = new ClickableSprite(imagePath, "Pick up", X, Y);
        childSprite.show();
        childSprite.setHighlightAndLabelOnHover();

        childSprite.setOnMouseClicked(event -> {
            DatabaseHandler.addItemToInventory(filename);
            childSprite.hide();
        });
    }
}