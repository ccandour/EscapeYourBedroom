package com.example.escapeyourbedroom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EscapeRoomGame extends Application {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static StackPane root;
    private static final List<ImageView> sceneBackgrounds = new ArrayList<>();
    public static int currentScene = 0;
    public static ClickableSprite rightArrow;
    public static ClickableSprite leftArrow;
    public static NameTag nameTag;
    public static PopoutMessage popoutMessage;
    public static List<ClickableSprite> safeNumpadButtons = new ArrayList<>();
    // A list of lists of all sprites in a given scene (not that complicated once you go insane)
    // I decided to make it this way so when you want to get all the sprites from the current scene you can just sceneSprites.get(currentBackground)
    public static List<List<ClickableSprite>> sceneSprites = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Escape Room Game");
        root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);

        SpriteEvents.initialize();

        // Load the backgrounds
        sceneBackgrounds.add(new ImageView(new Image("file:Background1.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:Background2.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:Background3.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:Background4.png")));

        for (int i = 0; i < sceneBackgrounds.size(); i++) {
            // Add the backgrounds to root
            root.getChildren().add(sceneBackgrounds.get(i));

            // Initialize the lists in the list of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists of the lists sprites
            sceneSprites.add(new ArrayList<>());
        }



        // Set the visible background to the first scene
        sceneBackgrounds.get(0).toFront();

        // Initialize the name tag
        nameTag = new NameTag();
        popoutMessage = new PopoutMessage();


        // Add sprites and their properties
        // TODO: Possibly move this to a separate class because it will clutter Main
        ClickableSprite drawer = new ClickableSprite("file:drawer.png", "Drawer", -600 , 250);
        drawer.setOnMouseClicked(event -> drawer.zoomHandler());
        drawer.setHighlightOnHover();
        drawer.setParentScene(2);
        drawer.setZoomedImage("file:drawer.png");

        ClickableSprite box = new ClickableSprite("file:box.png", "Box", 600 , 250);
        box.setOnMouseClicked(event -> box.zoomHandler());
        box.setHighlightOnHover();
        box.setParentScene(1);
        box.setZoomedImage("file:box_zoom.png");

        ClickableSprite painting = new ClickableSprite("file:painting.png", "Painting", 400, -200);
        painting.setOnMouseClicked(mouseEvent -> painting.zoomHandler());
        painting.setHighlightOnHover();
        painting.setParentScene(0);
        painting.setZoomedImage("file:painting_zoom.png");

        ClickableSprite safe = new ClickableSprite("file:safe.png", "Safe", 500, 250);
        safe.setHighlightOnHover();
        safe.setParentScene(2);
        safe.setZoomedImage("file:safe_zoom.png");
        safe.setOnMouseClicked(mouseEvent -> {
            if (SpriteEvents.isSafeUnlocked) safe.zoomHandler();
            else SpriteEvents.safeShowNumpad();
        });

        rightArrow = new ClickableSprite("file:Right_Arrow.png", "Go right", 900, 0);
        rightArrow.setHighlightOnHover();
        rightArrow.setOnMouseClicked(mouseEvent -> nextBackground());

        leftArrow = new ClickableSprite("file:Left_Arrow.png", "Go left", -900, 0);
        leftArrow.setHighlightOnHover();
        leftArrow.setOnMouseClicked(mouseEvent -> prevBackground());



        primaryStage.show();
    }

    public static void nextBackground() {
        // Single line for cycling through the number of backgrounds (instead of doing some weird ifs)
        int nextScene = (currentScene + 1) % sceneBackgrounds.size();

        // Set the background to the next scene's background (also hides previous scene sprites so cool)
        sceneBackgrounds.get(nextScene).toFront();

        // Make all sprites from the next background visible
        updateSpritesVisibility(nextScene, true);

        // Update currentBackground
        currentScene = nextScene;
    }
    public static void prevBackground() {
        // Single line for cycling through the number of backgrounds (instead of doing some weird ifs)
        int prevScene = (currentScene - 1 + sceneBackgrounds.size()) % sceneBackgrounds.size();

        // Set the background to the previous scene's background (also hides previous sprites so cool)
        sceneBackgrounds.get(prevScene).toFront();

        // Make all sprites from the next background visible
        updateSpritesVisibility(prevScene, true);

        // Update currentBackground
        currentScene = prevScene;
    }
    public static List<ClickableSprite> getSpritesFromScene(int scene) {
        return sceneSprites.get(scene);
    }
    public static void updateSpritesVisibility(int scene, boolean isVisible) {
        // Update the visibility of the arrows
        if (isVisible) {
            rightArrow.show();
            leftArrow.show();
        }
        else {
            rightArrow.hide();
            leftArrow.hide();
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
}
