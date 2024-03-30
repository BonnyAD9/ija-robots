package ija.robots.actors;

import ija.robots.common.Circle;
import ija.robots.common.Vec2;

public class Robot {
    private Circle hitbox;
    private Vec2 speed;

    public Robot(Circle hitbox, Vec2 speed) {
        this.hitbox = hitbox;
        this.speed = speed;
    }

    public Circle hitbox() {
        return hitbox;
    }

    public Vec2 speed() {
        return speed;
    }
}
