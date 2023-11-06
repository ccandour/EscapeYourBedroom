package com.example.escapeyourbedroom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.escapeyourbedroom.Utilities.*;
import static com.example.escapeyourbedroom.EscapeRoomGame.*;

public class InventoryScripts {
    static List<ClickableSprite> items = new ArrayList<>();
    public static ImageView inventory = new ImageView("file:assets/inventory.png");

    public static void initializeInventory() {
        inventory.setVisible(false);
        refreshInventory();
        root.getChildren().add(inventory);
    }

    // Refresh inventory items
    private static void refreshInventory() {
        List<String> itemsPaths = DatabaseHandler.getAllItems();
        int currentItem = 0;
        items.clear();

        for (double i = -0.5; i <= 0.5; i++) {
            for (double j = -1.5; j <= 1.5; j++) {
                if (currentItem >= itemsPaths.size()) break;

                String currentItemName = itemsPaths.get(currentItem);

                // Y adjusted to the difference of top and bottom margins of inventory background
                items.add(new ClickableSprite(
                        iconizeName(currentItemName),
                        filenameToLabel(currentItemName),
                        (11 + 25) * 8 * j,
                        ((7+25) * 8 * i)+48
                ));

                currentItem += 1;
            }
        }
    }

    public static void renderInventory() {
        refreshInventory();
        darkenBackground();

        inventory.setVisible(true);
        inventory.toFront();

        for (ClickableSprite item : items) {
            if (item.name.startsWith("Key")) {
                item.setOnlyLabelOnHover();
            } else {
                item.setHighlightAndLabelOnHover();

                item.setOnMouseClicked(event -> {
                    darkenBackground.toFront();
                    exitButton.hide();

                    System.out.println(item.getImage().getUrl());

                    String zoomName = item.getImage().getUrl().replace("_icon", "_zoom");
                    ImageView zoomedNote = new ImageView(new Image(zoomName));

                    zoomedNote.setVisible(true);
                    zoomedNote.toFront();

                    root.getChildren().add(zoomedNote);

                    itemZoomExitButton();
                });
            }
            item.show();
        }
        exitButton();
    }

    // Create an exit button when zooming into an item
    public static void itemZoomExitButton() {
        exitButton = new ClickableSprite("file:assets/exit_button.png", "Go back", 796, -360);
        exitButton.show();
        exitButton.setHighlightAndLabelOnHover();

        exitButton.setOnMouseClicked(event -> {
            // Hide keypad and render the background once again
            reRenderBackground();
            updateSpritesVisibility(currentScene, true);
            exitButton.hide();

            root.getChildren().remove(darkenBackground);

            renderInventory();
        });
    }

}
