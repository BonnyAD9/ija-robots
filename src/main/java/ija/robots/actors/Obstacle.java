package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private Rectangle shape;
    private Vec2 lastPos = new Vec2(0, 0);

    public Obstacle(Rect rect) {
        shape = new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
        shape.setFill(Color.web("#ff5555"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(6);
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
    }

    public Vec2 pos() {
        return new Vec2(shape.getX(), shape.getY());
    }

    public Rect hitbox() {
        return new Rect(
            pos(),
            shape.getWidth(),
            shape.getHeight()
        );
    }

    public Rect hitbox(Rect box) {
        shape.setWidth(box.width());
        shape.setHeight(box.height());
        moveTo(box.topLeft());
        return box;
    }

    private void moveBy(Vec2 vec) {
        moveTo(pos().add(vec));
    }

    private void moveTo(Vec2 pos) {
        shape.setX(pos.x());
        shape.setY(pos.y());
    }

    public Rectangle getShape() {
        return shape;
    }

    private void mousePress(MouseEvent event) {
        lastPos = new Vec2(event.getX(), event.getY());
    }

    private void mouseDrag(MouseEvent event) {
        var newPos = new Vec2(event.getX(), event.getY());
        var delta = newPos.sub(lastPos);
        moveBy(delta);
        lastPos = newPos;
    }
}
