package com.example.escapeyourbedroom;

import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;
import static com.example.escapeyourbedroom.Utilities.*;

@SuppressWarnings("ClassEscapesDefinedScope")
public class SafeScripts {
    public static List<ClickableSprite> safeNumpadButtons = new ArrayList<>();
    public static ImageView numpad = new ImageView("file:assets/safe_numpad.png");
    private static final String[] code = {""};
    public static void initializeSafe() {
        numpad.setVisible(false);

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

        root.getChildren().add(numpad);

        numpad.setOnMouseMoved(mouseEvent -> {
            nameTag.setPosToCursor(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            nameTag.show();
            if (code[0].isEmpty()) nameTag.setText("____");
        });

        // When cursor exits the numpad hide the nametag
        numpad.setOnMouseExited(mouseEvent -> nameTag.hide());

        // This has to be an array because javaFX shits itself when it sees a non-final variable inside a timeline, but you can still modify values in final arrays for some fucking reason?
        for (ClickableSprite safeNumpadButton : safeNumpadButtons) {
            safeNumpadButton.setOnlyZoomOnHoover();
            safeNumpadButton.setOnMouseClicked(mouseEvent -> {
                code[0] += safeNumpadButton.name;

                // Set text to code with underscores e.g. "12__" or "123_"
                nameTag.show();
                nameTag.setText(code[0] + "_".repeat(Math.max(0, 4 - code[0].length())));

                // If entered code is correct, set vault as unlocked
                if (code[0].equals("1943")) {
                    DatabaseHandler.changeProgression("safe");

                    for (ClickableSprite button : safeNumpadButtons) {
                        button.hide();
                    }
                    numpad.setVisible(false);
                    reRenderBackground();
                    exitButton.hide();

                    root.getChildren().remove(darkenBackground);
                    popoutMessage.showMessage("The safe unlocks!");
                }
                // If entered code is incorrect, reset it and notify the user about how they are an absolute imbecile
                else if (code[0].length() == 4){
                    popoutMessage.showMessage("Wrong code...", 1000);
                    code[0] = "";

                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(750));
                    pauseTransition.setOnFinished(event -> {
                        // If the user didn't start entering new code before the time runs out reset the name tag
                        if(code[0].isEmpty()) {
                            nameTag.setText("____");
                        }
                    });

                    pauseTransition.play();
                }
            });
            safeNumpadButton.setOnMouseMoved(mouseEvent -> {
                nameTag.setPosToCursor(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                nameTag.show();
                if (code[0].isEmpty()) nameTag.setText("____");
            });
        }
    }

    public static void safeShowNumpad() {
        setDarkenBackground();
        numpad.setVisible(true);
        numpad.toFront();
        code[0] = "";
        for (ClickableSprite safeNumpadButton : safeNumpadButtons) {
            safeNumpadButton.show();
        }
        exitButton();
    }
}
