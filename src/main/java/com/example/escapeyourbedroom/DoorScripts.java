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

    private static void refreshLock() {
        locksOpen = DatabaseHandler.checkLocksOpen();
        lock.setImage(new Image("file:assets/lock" + (locksOpen+1) + "_closeup.png"));
    }

    public static void renderLock(ClickableSprite door) {
        setDarkenBackground();
        refreshLock();

        lock.setVisible(true);
        lock.toFront();

        keyHole(door);
        exitButton();
    }

    public static void keyHole(ClickableSprite door) {
        ClickableSprite keyhole = new ClickableSprite("file:assets/keyhole.png", "Open using Key " + (locksOpen+1), 0, 132);
        keyhole.show();
        keyhole.setHighlightOnHover();

        keyhole.setOnMouseClicked(event -> {
            if (DatabaseHandler.isItemPickedUp("key_" + (locksOpen+1) + ".png")) {
                locksOpen++;

                if (locksOpen == 3) {
                    renderVictoryScreen();
                    return;
                }

                DatabaseHandler.removeItemFromInventory("key_" + locksOpen + ".png");
                DatabaseHandler.changeProgression("lock_" + locksOpen);

                door.setImage(new Image("file:assets/door_" + locksOpen + ".png"));
                reRenderBackground();
                updateSpritesVisibility(currentScene, true);

                keyhole.hide();

                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("Lock opened!");
            }
            else {
                reRenderBackground();
                updateSpritesVisibility(currentScene, true);

                keyhole.hide();

                root.getChildren().remove(darkenBackground);
                popoutMessage.showMessage("None of your keys seems to fit...");
            }
        });
    }
}
