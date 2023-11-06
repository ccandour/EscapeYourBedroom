package com.example.escapeyourbedroom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("ClassEscapesDefinedScope")
public class EscapeRoomGame extends Application {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static StackPane root;
    public static final List<ImageView> sceneBackgrounds = new ArrayList<>();
    public static int currentScene = 0;
    public static ClickableSprite rightArrow;
    public static ClickableSprite leftArrow;
    public static ClickableSprite backpack;
    public static ClickableSprite childSprite;

    // A list of lists of all sprites in a given scene (not that complicated once you go insane)
    public static List<List<ClickableSprite>> sceneSprites = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseHandler.connectToDatabase();
        primaryStage.setTitle("Why are you looking at this?");
        root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);

        Utilities.initialize();

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

        // Initialize the name tag and popout message
        Utilities.nameTag = new NameTag();
        Utilities.popoutMessage = new PopoutMessage();

        InventoryScripts.initializeInventory();

        // Add sprites and their properties
        ClickableSprite drawer = new ClickableSprite("file:assets/drawer.png", "Drawer", -600 , 250);
        drawer.setOnMouseClicked(event -> {
            if (!drawer.isZoomed) {
                drawer.zoomIn();
                Utilities.renderChildSprite("note_1.png", -200, -70);
                Utilities.exitButton(drawer);
            }
        });
        drawer.setHighlightAndLabelOnHover();
        drawer.setParentScene(2);

        ClickableSprite box = new ClickableSprite("file:assets/box.png", "Box", -600 , 250);
        box.setOnMouseClicked(event -> {
            if (!box.isZoomed) {
                box.zoomIn();
                Utilities.exitButton(box);
                Utilities.renderChildSprite("key_1.png", -95, -50);
            }
        });
        box.setHighlightAndLabelOnHover();
        box.setParentScene(1);

        ClickableSprite painting = new ClickableSprite("file:assets/painting.png", "Painting", 400, -200);
        painting.setHighlightAndLabelOnHover();
        painting.setParentScene(0);
        painting.setOnMouseClicked(event -> {
            if (!painting.isZoomed) {
                Utilities.renderBackground("d7bc5f");
                Utilities.renderChildSprite("note_2.png", 0, -276);
                painting.zoomIn();
                Utilities.exitButton(painting);
            }
        });

        ClickableSprite safe = new ClickableSprite("file:assets/safe.png", "Safe", 500, 250);
        safe.setHighlightAndLabelOnHover();
        safe.setParentScene(2);
        safe.setOnMouseClicked(mouseEvent -> {
            if (DatabaseHandler.checkProgression("safe")) {
                if (!safe.isZoomed) {
                    safe.zoomIn();
                    Utilities.exitButton(safe);
                    Utilities.renderChildSprite("key_3.png", 150, 350);
                }
            }
            else SafeScripts.safeShowNumpad();
        });
        SafeScripts.initializeSafe();

        ClickableSprite bed = new ClickableSprite("file:assets/bed.png", "Bed", 500, 300);
        bed.setHighlightAndLabelOnHover();
        bed.setParentScene(1);
        bed.setOnMouseClicked(mouseEvent -> BedScripts.zoomToBed());
        BedScripts.initializeBed();

        DoorScripts.initializeLock();
        ClickableSprite door = new ClickableSprite("file:assets/door_" + (DoorScripts.locksOpen) + ".png", "Door [LOCKED]", 250, -8);
        door.setHighlightAndLabelOnHover();
        door.setParentScene(3);
        door.setOnMouseClicked(mouseEvent -> DoorScripts.renderLock(door));


        // Add the navigation buttons
        rightArrow = new ClickableSprite("file:assets/right_arrow.png", "Go right", 900, 0);
        rightArrow.setHighlightAndLabelOnHover();
        rightArrow.setOnMouseClicked(mouseEvent -> Utilities.nextBackground());

        leftArrow = new ClickableSprite("file:assets/left_arrow.png", "Go left", -900, 0);
        leftArrow.setHighlightAndLabelOnHover();
        leftArrow.setOnMouseClicked(mouseEvent -> Utilities.prevBackground());

        backpack = new ClickableSprite("file:assets/backpack.png", "Open inventory", 750, -450);
        backpack.setHighlightAndLabelOnHover();
        backpack.setOnMouseClicked(mouseEvent -> InventoryScripts.renderInventory());

        Utilities.reRenderBackground();

        primaryStage.show();
    }
}