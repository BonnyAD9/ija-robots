package ija.robots.actors;

import ija.robots.common.Rect;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private Rect hitbox;

    public Obstacle(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public Rect hitbox() {
        return hitbox;
    }

    public Rectangle getGraphics() {
        return hitbox.getGraphics();
    }
}
