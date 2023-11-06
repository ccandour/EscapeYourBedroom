package com.example.escapeyourbedroom;

import javafx.scene.image.ImageView;

import static com.example.escapeyourbedroom.Utilities.exitButton;

@SuppressWarnings("ClassEscapesDefinedScope")
public class BedScripts {

    public static ClickableSprite bedZoomed = new ClickableSprite("file:assets/bed_zoom.png", "Move Pillow", 0 ,0);
    public static ImageView bedZoomedNoPillow = new ClickableSprite("file:assets/bed_zoom_zoom_wroooom.png", "", 0, 0);

    static void initializeBed() {
        bedZoomed.setOnMouseClicked(mouseEvent -> {
            zoomZoomToBed();
            Utilities.renderChildSprite("key_2.png", -150, -50);
        });
    }

    static void zoomZoomToBed() {
        bedZoomedNoPillow.toFront();
        exitButton();
    }

    public static void zoomToBed() {
        bedZoomed.show();
        exitButton();
    }
}
