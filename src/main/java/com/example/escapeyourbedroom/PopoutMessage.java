package com.example.escapeyourbedroom;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;

import static com.example.escapeyourbedroom.EscapeRoomGame.root;

public class PopoutMessage {
    Font font;
    Text text = new Text();
    Rectangle box = new Rectangle();

    PopoutMessage() {
        // Set popout message's text and box properties
        File fontFile = new File("PixelFont.ttf");
        font = Font.loadFont(fontFile.toURI().toString(), 36);
        text.setFont(font);

        box.setFill(Color.WHITE);
        box.setStroke(Color.BLACK);
        box.setStrokeWidth(4);

        hide();

        text.setTranslateY(300);
        box.setTranslateY(300);

        root.getChildren().add(text);
        root.getChildren().add(box);
    }
    public void showMessage(String message) {
        // Show popout message
        show();
        text.setOpacity(1);
        box.setOpacity(1);
        setText(message);

        // Set it to start fading out after some time, depending on how long the message is
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(600 + (message.length() * 75)));

        Timeline fadeout = new Timeline(new KeyFrame(Duration.millis(1000/60.0), event -> {
            text.setOpacity(text.getOpacity() - 0.01);
            box.setOpacity(box.getOpacity() - 0.01);
        }));
        fadeout.setCycleCount(100);

        pauseTransition.setOnFinished(event -> fadeout.play());
        pauseTransition.play();
    }
    public void showMessage(String message, int durationMillis) {
        // Show popout message
        show();
        text.setOpacity(1);
        box.setOpacity(1);
        setText(message);

        // Set it to start fading out after 'durationMillis'
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(durationMillis));

        Timeline fadeout = new Timeline(new KeyFrame(Duration.millis(1000/60.0), event -> {
            text.setOpacity(text.getOpacity() - 0.01);
            box.setOpacity(box.getOpacity() - 0.01);
        }));
        fadeout.setCycleCount(100);

        pauseTransition.setOnFinished(event -> fadeout.play());
        pauseTransition.play();
    }
    void show() {
        box.setVisible(true);
        box.toFront();
        text.toFront();
    }
    void hide() {
        box.setVisible(false);
        box.toBack();
        text.toBack();
    }

    // Update nameTag text and it's box size
    void setText(String s) {
        text.setText(s);
        updateBoxSize();
    }

    // Set nameTag box size to be slightly bigger than the text
    void updateBoxSize() {
        box.setWidth(text.getLayoutBounds().getWidth() + 10);
        box.setHeight(text.getLayoutBounds().getHeight() + 10);
    }
}
