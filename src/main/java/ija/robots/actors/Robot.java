package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Robot {
    private static final double RADIUS = 25;
    private static final double BORDER_THICKNESS = 6;
    private static final double ADJ = BORDER_THICKNESS / 2;

    private Circle shape;
    private Vec2 lastPos = new Vec2(0, 0);
    private boolean isDragging;

    private double speed;
    private double angle;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    public Robot(Vec2 topLeft, double speed, double angle) {
        shape = new Circle(topLeft.x() + RADIUS, topLeft.y() + RADIUS, RADIUS);
        shape.setFill(Color.web("#cc55cc"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(BORDER_THICKNESS);
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
        shape.setOnMouseReleased(e -> mouseRelease(e));
        shape.setOnMouseEntered(e -> shape.setCursor(Cursor.OPEN_HAND));
        shape.setOnMouseExited(e -> shape.setCursor(Cursor.DEFAULT));
        shape.setOnMouseMoved(e -> shape.setCursor(Cursor.OPEN_HAND));
        this.speed = speed;
        this.angle = angle;
    }

    public Rect hitbox() {
        return new Rect(
            pos(),
            RADIUS * 2 + ADJ * 2,
            RADIUS * 2 + ADJ * 2
        );
    }

    public Rect hitbox(Rect rect) {
        moveTo(rect.topLeft().add(ADJ, ADJ));
        return rect;
    }

    public double speed() {
        return speed;
    }

    public double speed(double speed) {
        return this.speed = speed;
    }

    public void angle(double angle) {
        this.angle = angle;
    }

    public Circle getShape() {
        return shape;
    }

    public Vec2 pos() {
        return apos().sub(ADJ, ADJ);
    }

    public Vec2 vecAngle() {
        return Vec2.unit(angle);
    }

    public Vec2 step() {
        return Vec2.polar(speed, angle);
    }

    public void move(double delta) {
        moveBy(step().mul(delta));
    }

    public boolean isDragging() {
        return isDragging;
    }

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

    private void moveBy(Vec2 vec) {
        moveTo(apos().add(vec));
    }

    private void moveTo(Vec2 pos) {
        shape.setCenterX(pos.x() + RADIUS);
        shape.setCenterY(pos.y() + RADIUS);
    }

    private Vec2 apos() {
        return new Vec2(
            shape.getCenterX() - RADIUS,
            shape.getCenterY() - RADIUS
        );
    }

    private void mousePress(MouseEvent event) {
        isDragging = true;
        shape.setCursor(Cursor.CLOSED_HAND);
        lastPos = new Vec2(event.getX(), event.getY());
    }

    private void mouseRelease(MouseEvent event) {
        shape.setCursor(Cursor.OPEN_HAND);
        isDragging = false;
    }

    private void mouseDrag(MouseEvent event) {
        var newPos = new Vec2(event.getX(), event.getY());
        var delta = newPos.sub(lastPos);
        moveBy(delta);
        lastPos = newPos;
    }
}
