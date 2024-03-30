package ija.robots.actors;

import ija.robots.common.Rect;

public class Obstacle {
    private Rect hitbox;

    public Obstacle(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public Rect hitbox() {
        return hitbox;
    }
}
