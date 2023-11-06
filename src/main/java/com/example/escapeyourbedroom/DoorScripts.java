package com.example.escapeyourbedroom;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.escapeyourbedroom.EscapeRoomGame.*;
import static com.example.escapeyourbedroom.Utilities.*;

@SuppressWarnings("ClassEscapesDefinedScope")
public class DoorScripts {
    public static ImageView lock = new ImageView("file:assets/lock1_closeup.png");
    public static int locksOpen;

    public static void initializeLock() {
        lock.setVisible(false);
        refreshLock();
        root.getChildren().add(lock);
    }

    // Refresh the lock image based on the number of locks open
    private static void refreshLock() {
        locksOpen = DatabaseHandler.checkLocksOpen();
        lock.setImage(new Image("file:assets/lock" + (locksOpen+1) + "_closeup.png"));
    }

    // Render the lock for when user interacts with the door
    public static void renderLock(ClickableSprite door) {
        darkenBackground();
        refreshLock();

        lock.setVisible(true);
        lock.toFront();

        keyHole(door);
        exitButton();
    }

    // Method to handle the interaction with a keyhole
    public static void keyHole(ClickableSprite door) {
        ClickableSprite keyhole = new ClickableSprite("file:assets/keyhole.png", "Open using Key " + (locksOpen+1), 0, 132);
        keyhole.show();
        keyhole.setHighlightAndLabelOnHover();

        keyhole.setOnMouseClicked(event -> {
            if (DatabaseHandler.isItemPickedUp("key_" + (locksOpen+1) + ".png")) {
                locksOpen++;

                // Render the victory screen if all locks are open
                if (locksOpen == 3) {
                    renderVictoryScreen();
                    return;
                }

                // Remove the key from inventory and save user's progress
                DatabaseHandler.removeItemFromInventory("key_" + locksOpen + ".png");
                DatabaseHandler.changeProgression("lock_" + locksOpen);

                // Update the door image and exit out of the door interaction screen
                door.setImage(new Image("file:assets/door_" + locksOpen + ".png"));
                reRenderBackground();
                updateSpritesVisibility(currentScene, true);

                keyhole.hide();

                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("Lock opened!");
            }
            else {
                // Exit out of the door interaction screen
                reRenderBackground();
                updateSpritesVisibility(currentScene, true);

                keyhole.hide();

                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("None of your keys seems to fit...");
            }
        });
    }
}
