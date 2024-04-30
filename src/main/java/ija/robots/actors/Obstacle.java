package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private static final double BORDER_THICKNESS = 6;
    private static final double ADJ = BORDER_THICKNESS / 2;

    private Rectangle shape;
    private Vec2 lastPos = new Vec2(0, 0);

    public Obstacle(Rect rect) {
        shape = new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
        shape.setFill(Color.web("#ff5555"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(BORDER_THICKNESS);
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
    }

    public Vec2 pos() {
        return apos().sub(ADJ, ADJ);
    }

    private Vec2 apos() {
        return new Vec2(shape.getX(), shape.getY());
    }

    public Rect hitbox() {
        return new Rect(
            pos(),
            shape.getWidth() + ADJ * 2,
            shape.getHeight() + ADJ * 2
        );
    }

    public Rect hitbox(Rect box) {
        shape.setWidth(box.width() - ADJ * 2);
        shape.setHeight(box.height() - ADJ * 2);
        moveTo(box.topLeft().add(ADJ, ADJ));
        return box;
    }

    private void moveBy(Vec2 vec) {
        moveTo(apos().add(vec));
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
