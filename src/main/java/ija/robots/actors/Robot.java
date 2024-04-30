package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Robot {
    private static final double RADIUS = 30;
    private Circle shape;

    double speed;
    double angle;

    public Robot(Vec2 topLeft, double speed, double angle) {
        shape = new Circle(topLeft.x() + RADIUS, topLeft.y() + RADIUS, RADIUS);
        shape.setFill(Color.web("#cc55cc"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(6);
        this.speed = speed;
        this.angle = angle;
    }

    public Rect hitbox() {
        return new Rect(
            shape.getCenterX() - RADIUS,
            shape.getCenterY() - RADIUS,
            RADIUS * 2,
            RADIUS * 2
        );
    }

    public Rect hitbox(Rect rect) {
        moveTo(rect.topLeft());
        return rect;
    }

    public double speed() {
        return speed;
    }

    public double speed(double speed) {
        return this.speed = speed;
    }

    public void moveTo(Vec2 pos) {
        shape.setCenterX(pos.x() - RADIUS);
        shape.setCenterY(pos.y() - RADIUS);
    }

    public void angle(double angle) {
        this.angle = angle;
    }

    public Circle getShape() {
        return shape;
    }
}
