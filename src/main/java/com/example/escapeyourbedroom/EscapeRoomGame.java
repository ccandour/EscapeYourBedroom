package com.example.escapeyourbedroom;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.EscapeRoomGame.exitButton;

public class EscapeRoomGame extends Application {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static StackPane root;
    private static final List<ImageView> sceneBackgrounds = new ArrayList<>();
    public static int currentScene = 0;
    public static ClickableSprite rightArrow;
    public static ClickableSprite leftArrow;
    public static ClickableSprite exitButton;
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
        sceneBackgrounds.add(new ImageView(new Image("file:assets/Background1.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:assets/Background2.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:assets/Background3.png")));
        sceneBackgrounds.add(new ImageView(new Image("file:assets/Background4.png")));

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
        ClickableSprite drawer = new ClickableSprite("file:assets/drawer.png", "Drawer", -600 , 250);
        drawer.setOnMouseClicked(event -> {
            if (!drawer.isZoomed) {
                drawer.zoomInto();
                exitButton(drawer);
            }
        });
        drawer.setHighlightOnHover();
        drawer.setParentScene(2);
        drawer.setZoomedImage("file:assets/drawer.png");

        ClickableSprite box = new ClickableSprite("file:assets/box.png", "Box", 600 , 250);
        box.setOnMouseClicked(event -> {
            if (!box.isZoomed) {
                box.zoomInto();
                exitButton(box);
            }
        });
        box.setHighlightOnHover();
        box.setParentScene(1);
        box.setZoomedImage("file:assets/box_zoom.png");

        ClickableSprite painting = new ClickableSprite("file:assets/painting.png", "Painting", 400, -200);
        painting.setOnMouseClicked(event -> {
            if (!painting.isZoomed) {
                painting.zoomInto();
                exitButton(painting);
            }
        });
        painting.setHighlightOnHover();
        painting.setParentScene(0);
        painting.setZoomedImage("file:assets/painting_zoom.png");

        ClickableSprite safe = new ClickableSprite("file:assets/safe.png", "Safe", 500, 250);
        safe.setHighlightOnHover();
        safe.setParentScene(2);
        safe.setZoomedImage("file:assets/safe_zoom.png");
        safe.setOnMouseClicked(mouseEvent -> {
            if (SpriteEvents.isSafeUnlocked) {
                if (!safe.isZoomed) {
                    safe.zoomInto();
                    exitButton(safe);
                }
            }
            else SpriteEvents.safeShowNumpad();
        });

        rightArrow = new ClickableSprite("file:assets/Right_Arrow.png", "Go right", 900, 0);
        rightArrow.setHighlightOnHover();
        rightArrow.setOnMouseClicked(mouseEvent -> nextBackground());

        leftArrow = new ClickableSprite("file:assets/Left_Arrow.png", "Go left", -900, 0);
        leftArrow.setHighlightOnHover();
        leftArrow.setOnMouseClicked(mouseEvent -> prevBackground());

//        Timeline temp = new Timeline(new KeyFrame(Duration.millis(500), event -> {
//            System.out.println(box.isZoomed);
//        }));
//        temp.setCycleCount(Animation.INDEFINITE);
//        temp.play();

        primaryStage.show();
    }

    public static void exitButton(ClickableSprite sprite) {
        exitButton = new ClickableSprite("file:assets/exit_button.png", "Go back", 796, -360);
        exitButton.show();
        exitButton.setHighlightOnHover();
        exitButton.setOnMouseClicked(event -> sprite.zoomOut());
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
