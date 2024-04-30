package ija.robots.actors;

import ija.robots.common.Rect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private Rectangle shape;

    public Obstacle(Rect rect) {
        shape = new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
        shape.setFill(Color.web("#ff5555"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(6);
    }

    public Rect hitbox() {
        return new Rect(
            shape.getX(),
            shape.getY(),
            shape.getWidth(),
            shape.getHeight()
        );
    }

    public Rect hitbox(Rect box) {
        shape.setX(box.x());
        shape.setY(box.y());
        shape.setWidth(box.width());
        shape.setHeight(box.height());
        return box;
    }

    public Rectangle getShape() {
        return shape;
    }
}
