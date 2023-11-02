package com.example.escapeyourbedroom;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;
import static com.example.escapeyourbedroom.EscapeRoomGame.HEIGHT;

public class NameTag  {
    Font font;
    Text text = new Text();
    Rectangle box = new Rectangle();

    NameTag() {
        // Set the nameTag box properties and hide it
        box.setFill(Color.WHITE);
        box.setStroke(Color.BLACK);
        box.setStrokeWidth(4);
        box.toBack();

        // Set the nameTag text properties
        File fontFile = new File("PixelFont.ttf");
        font = Font.loadFont(fontFile.toURI().toString(), 36);

        text.setFont(font);

        hide();

        // Add them to root
        root.getChildren().add(text);
        root.getChildren().add(box);
    }
    void setText(String s) {
        // Update nameTag text and it's box size
        text.setText(s);
        updateBoxSize();
    }
    void updateBoxSize() {
        // Set nameTag box size to be slightly bigger than the text
        box.setWidth(text.getLayoutBounds().getWidth() + 10);
        box.setHeight(text.getLayoutBounds().getHeight() + 10);
    }
    void setPosToCursor(double mouseX, double mouseY) {
        // The nameTag has to be a couple pixels away from the cursor, otherwise it overrides sprite hovering
        if (mouseX + box.getWidth() + 10 < WIDTH) {
            // If the nameTag fits in the screen, show it to the right of the cursor
            text.setTranslateX(mouseX - (WIDTH / 2.0) + box.getWidth() / 2 + 15);
            box.setTranslateX(text.getTranslateX());
        } else {
            // If the nameTag goes offscreen, set it to show to the left of the cursor instead
            text.setTranslateX(mouseX - (WIDTH / 2.0) - box.getWidth() / 2 - 15);
            box.setTranslateX(text.getTranslateX());
        }
        // Set the Y position of the nameTag
        text.setTranslateY(mouseY - (HEIGHT / 2.0) - box.getHeight() / 2 + 10);
        box.setTranslateY(text.getTranslateY() - 5);
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
}
