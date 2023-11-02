package com.example.escapeyourbedroom;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;

public class SpriteEvents {
    public static List<ClickableSprite> safeNumpadButtons = new ArrayList<>();
    public static ImageView keypad = new ImageView("file:assets/safe_numpad.png");
    public static Rectangle darkenBackground = new Rectangle(WIDTH, HEIGHT, Color.rgb(0, 0, 0, 0.5));
    public static boolean isSafeUnlocked;
    public static void initialize() {
        darkenBackground.setVisible(false);

        // I've split initialization into each sprite, so it's less cluttered
        initializeSafe();
    }
    public static void safeShowNumpad() {
        rightArrow.hide();
        leftArrow.hide();
        darkenBackground.setVisible(true);
        darkenBackground.toFront();
        keypad.setVisible(true);
        keypad.toFront();
        for (ClickableSprite safeNumpadButton : safeNumpadButtons) {
            safeNumpadButton.show();
        }
        exitButton();
    }
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
            safeNumpadButton.setOnMouseClicked(mouseEvent -> {
                code[0] += safeNumpadButton.name;

                if (code[0].equals("1943")) {
                    isSafeUnlocked = true;

                    for (ClickableSprite button : safeNumpadButtons) {
                        button.hide();
                    }
                    keypad.setVisible(false);
                    popoutMessage.showMessage("The safe unlocks!");
                }
                else if (code[0].length() == 4){
                    popoutMessage.showMessage("Wrong code...", 1);
                    code[0] = "";
                }
            });
        }
    }
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
        });
    }
}
