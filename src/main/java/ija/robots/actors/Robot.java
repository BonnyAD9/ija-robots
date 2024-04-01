package ija.robots.actors;

import ija.robots.common.Circle;
import ija.robots.common.Vec2;

public class Robot {
    private Circle hitbox;
    private Vec2 speed;
    public int id;

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

    public Vec2 speed(Vec2 speed) {
        return this.speed = speed;
    }

    public void moveTo(Vec2 pos) {
        hitbox = hitbox.pos(pos);
    }

    public Circle movedHitbox(double delta) {
        return hitbox.moveBy(speed.mul(delta));
    }

    public void lookAt(double angle) {
        speed = speed.angle(angle);
    }

    public void rotate(double angle) {
        speed = speed.rotated(angle);
    }

    @Override
    public String toString() {
        String s = "Robot"
            + id
            + " { position: "
            + hitbox.pos().toString()
            + ", direction: ";


        if (speed.y() > 0.5) {
            s += "d";
        } else if (speed.y() < -0.5) {
            s += "u";
        }

        if (speed.x() > 0.5) {
            s += "r";
        } else if (speed.x() < -0.5) {
            s += "l";
        }

        return s + " }";
    }
}
