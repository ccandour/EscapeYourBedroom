package com.example.escapeyourbedroom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;

class ClickableSprite extends ImageView {
    boolean isZoomed = false;
    String name;
    double x;
    double y;
    Image normalImage;
    Image zoomedImage;
    ClickableSprite(String imagePath, String name, double spriteX, double spriteY) {
        // Set the displayed image
        normalImage = new Image(imagePath);
        setImage(normalImage);

        this.name = name;
        x = spriteX;
        y = spriteY;

        // setTranslate moves the image from [0,0], which is the middle of the screen. Apparently it's the only way to move the image because the other two don't work? Classic javaFX shenanigans
        setTranslateX(x);
        setTranslateY(y);

        // Automatically try to assign zoomed image
        zoomedImage = new Image(Utilities.zoomifyName(imagePath));

        root.getChildren().add(this);
    }
    void hide() {
        setVisible(false);
    }
    void show() {
        setVisible(true);
        toFront();
    }
    void setHighlightOnHover() {
        setOnMouseEntered(event -> {
            if (!isZoomed) {
                // When mouse enters a sprite, show its name on the name tag and highlight it
                Utilities.nameTag.setText(name);
                setScaleX(this.getScaleX() * 1.1);
                setScaleY(this.getScaleY() * 1.1);
                Utilities.nameTag.show();
            }
        });
        setOnMouseExited(event -> {
            if (!isZoomed) {
                // When the mouse exits the sprite, hide the name tag and stop highlighting it
                Utilities.nameTag.hide();
                setScaleX(1);
                setScaleY(1);
            }
        });
        setOnMouseMoved(mouseEvent -> {
            // If the sprite its zoomed in, make the name tag follow the cursor; otherwise hide it
            if (!isZoomed) Utilities.nameTag.setPosToCursor(mouseEvent.getSceneX(),  mouseEvent.getSceneY());
            else Utilities.nameTag.hide();
        });
    }

    // For inventory
    void setOnlyLabelOnHoover() {
        setOnMouseEntered(event -> {
            if (!isZoomed) {
                // When mouse enters a sprite, show its name on the name tag and highlight it
                Utilities.nameTag.setText(name);
                Utilities.nameTag.show();
            }
        });
        setOnMouseExited(event -> {
            if (!isZoomed) {
                // When the mouse exits the sprite, hide the name tag and stop highlighting it
                Utilities.nameTag.hide();
            }
        });
        setOnMouseMoved(mouseEvent -> {
            // If the sprite its zoomed in, make the name tag follow the cursor; otherwise hide it
            if (!isZoomed) Utilities.nameTag.setPosToCursor(mouseEvent.getSceneX(),  mouseEvent.getSceneY());
            else Utilities.nameTag.hide();
        });
    }

    // For safe numpad
    void setOnlyZoomOnHoover() {
        setOnMouseEntered(event -> {
            if (!isZoomed) {
                // When mouse enters a sprite, zoom it 1.1 times
                setScaleX(this.getScaleX() * 1.1);
                setScaleY(this.getScaleY() * 1.1);
            }
        });
        setOnMouseExited(event -> {
            if (!isZoomed) {
                // When the mouse exits the sprite, revert scale back to 1
                setScaleX(1);
                setScaleY(1);
            }
        });
    }

    void setParentScene(int sceneIndex) {
        // Add sprite to its appropriate list and hide it if it shouldn't be displayed
        sceneSprites.get(sceneIndex).add(this);
        if (sceneIndex != currentScene) hide();
    }

    void zoomInto() {
        // Revert scale back to the un-zoomed one so that the sprite isn't growing when zooming and un-zooming
        setScaleX(1);
        setScaleY(1);

        // Hide all sprites on the current scene
        Utilities.updateSpritesVisibility(currentScene, false);

        // Renders the zoomed-in item
        setImage(zoomedImage);
        setTranslateX(0);
        setTranslateY(0);
        show();
        isZoomed = true;
    }
    void zoomOut() {
        // Show all sprites on the current scene
        Utilities.updateSpritesVisibility(currentScene, true);
        setImage(this.normalImage);
        setTranslateX(x);
        setTranslateY(y);

        // Hide the exit button on zoom-out
        Utilities.exitButton.hide();

        if (childSprite != null) childSprite.hide();
        isZoomed = false;

    }
}