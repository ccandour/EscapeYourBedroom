package com.example.escapeyourbedroom;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;
import static com.example.escapeyourbedroom.Utilities.*;

@SuppressWarnings("ClassEscapesDefinedScope")
public class SafeScripts {
    public static List<ClickableSprite> safeNumpadButtons = new ArrayList<>();
    public static ImageView keypad = new ImageView("file:assets/safe_numpad.png");

    public static void initializeSafe() {
        keypad.setVisible(false);

        int temp = 0;

        // Add numpad buttons
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

                // If entered code is correct, set vault as unlocked
                if (code[0].equals("1943")) {
                    DatabaseHandler.changeProgression("safe");

                    for (ClickableSprite button : safeNumpadButtons) {
                        button.hide();
                    }
                    keypad.setVisible(false);
                    reRenderBackground();
                    exitButton.hide();

                    root.getChildren().remove(darkenBackground);
                    popoutMessage.showMessage("The safe unlocks!");
                }
                // If entered code is incorrect, reset it and notify the user about how they are an absolute imbecile
                else if (code[0].length() == 4){
                    popoutMessage.showMessage("Wrong code...", 1000);
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
}
