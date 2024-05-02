package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents robot that can move. It is the base class for all other robots.
 */
public class Robot extends SimObj {
    private static final double RADIUS = 25;
    private static final double BORDER_THICKNESS = 6;
    private static final double ADJ = BORDER_THICKNESS / 2;

    private Circle shape;
    private Circle eye;
    private Vec2 lastPos = new Vec2(0, 0);
    private boolean isDragging = false;

    private double speed;
    private double angle;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Crates new robot at the given position.
     * @param topLeft Position of the top left corner of the bounding box of
     * the robot
     * @param speed The movement speed of the robot. (pixels per second)
     * @param angle The direction that the robot is facing. (radians)
     */
    public Robot(Vec2 topLeft, double speed, double angle) {
        shape = new Circle(topLeft.x() + RADIUS, topLeft.y() + RADIUS, RADIUS);
        shape.setFill(Color.web("#cc55cc"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(BORDER_THICKNESS);
        shape.setOnMousePressed(e -> mousePress(e, shape));
        shape.setOnMouseDragged(e -> mouseDrag(e));
        shape.setOnMouseReleased(e -> mouseRelease(e, shape));
        shape.setOnMouseEntered(e -> mouseCurIfNotDrag(shape, Cursor.OPEN_HAND));
        shape.setOnMouseExited(e -> mouseCurIfNotDrag(shape, Cursor.NONE));
        shape.setOnMouseMoved(e -> mouseCurIfNotDrag(shape, Cursor.OPEN_HAND));

        eye = new Circle(3);
        eye.setFill(Color.WHITE);
        eye.setStrokeWidth(0);
        eye.setOnMousePressed(e -> mousePress(e, eye));
        eye.setOnMouseDragged(e -> mouseDrag(e));
        eye.setOnMouseReleased(e -> mouseRelease(e, eye));
        eye.setOnMouseEntered(e -> mouseCurIfNotDrag(eye, Cursor.OPEN_HAND));
        eye.setOnMouseExited(e -> mouseCurIfNotDrag(eye, Cursor.NONE));
        eye.setOnMouseMoved(e -> mouseCurIfNotDrag(eye, Cursor.OPEN_HAND));

        this.speed = speed;
        angle(angle);
    }

    /**
     * Gets the bounding box of the hitbox of the robot.
     * @return Bounding box of the hitbox of this.
     */
    public Rect hitbox() {
        return new Rect(
            pos(),
            RADIUS * 2 + ADJ * 2,
            RADIUS * 2 + ADJ * 2
        );
    }

    /**
     * Sets the bounding box of the hitbox of the robot. (Only position is
     * taken into acount)
     * @param rect New position of the robot.
     * @return The value passed in.
     */
    public Rect hitbox(Rect rect) {
        moveTo(rect.topLeft().add(ADJ, ADJ));
        return rect;
    }

    /**
     * Gets the speed of the robot.
     * @return Speed of the robot. (pixels per second)
     */
    public double speed() {
        return speed;
    }

    /**
     * Sets the speed of the robot.
     * @param speed Speed of the robot. (pixels per second)
     * @return The new speed.
     */
    public double speed(double speed) {
        return this.speed = speed;
    }

    public Vec2 center() {
        return new Vec2(shape.getCenterX(), shape.getCenterY());
    }

    /**
     * Sets the direction that the robot is facing.
     * @param angle The new direction. (radians)
     */
    public void angle(double angle) {
        this.angle = angle;
        var c = center().add(vecAngle().mul(RADIUS * 2 / 3.));
        eye.setCenterX(c.x());
        eye.setCenterY(c.y());
    }

    /**
     * Gets the direction that the robot is facing.
     * @return Direction that the robot is facing.
     */
    public double angle() {
        return angle;
    }

    /**
     * Gets the shape of the robot that can be drawn.
     * @return Shape of the robot that can be drawn.
     */
    public Circle getShape() {
        return shape;
    }

    public Circle getEye() {
        return eye;
    }

    /**
     * Gets the position of the top left corner of the robot.
     * @return Position of the top left corner of the robot hitbox.
     */
    public Vec2 pos() {
        return apos().sub(ADJ, ADJ);
    }

    /**
     * Gets the orientation of the robot as a unit vector.
     * @return Orientation of the robot as a unit vector.
     */
    public Vec2 vecAngle() {
        return Vec2.unit(angle);
    }

    /**
     * Gets the move vector of the robot.
     * @return The move vector of the robot that contains the angle and speed.
     */
    public Vec2 step() {
        return Vec2.polar(speed, angle);
    }

    public Vec2 orientationVec() {
        return new Vec2(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Move the robot.
     * @param delta Time ellapsed in seconds.
     */
    public void move(double delta, double distance) {
        moveBy(step().mul(delta));
    }

    /**
     * Checks whether the robod is currently dragged by the user.
     * @return true if robot is dragged, otherwise false
     */
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setSelected(boolean val) {
        if (val) {
            shape.setStroke(Color.web("#ffff55"));
        } else {
            shape.setStroke(Color.WHITE);
        }
        super.setSelected(val);
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
        angle(angle);
    }

    private Vec2 apos() {
        return new Vec2(
            shape.getCenterX() - RADIUS,
            shape.getCenterY() - RADIUS
        );
    }

    private void mousePress(MouseEvent event, Circle source) {
        shape.toFront();
        eye.toFront();
        setSelected(true);
        if (event.getButton() == MouseButton.PRIMARY) {
            isDragging = true;
            source.setCursor(Cursor.CLOSED_HAND);
            lastPos = new Vec2(event.getX(), event.getY());
        }
    }

    private void mouseRelease(MouseEvent event, Circle source) {
        source.setCursor(Cursor.OPEN_HAND);
        isDragging = false;
    }

    private void mouseDrag(MouseEvent event) {
        if (isDragging) {
            var newPos = new Vec2(event.getX(), event.getY());
            var delta = newPos.sub(lastPos);
            moveBy(delta);
            lastPos = newPos;
        }
    }

    private void mouseCurIfNotDrag(Circle source, Cursor cur) {
        if (!isDragging()) {
            source.setCursor(cur);
        }
    }
}
