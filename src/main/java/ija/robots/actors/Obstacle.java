package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Immovable obstacle represented by a rectangle.
 */
public class Obstacle extends SimObj {
    /**
     * Dragging state of the obstacle.
     */
    private class State {
        static final int NONE = 0x0;
        static final int DRAGGING = 0x1;
        static final int RESIZE_HORIZONTAL = 0x02;
        static final int RESIZE_VERTICAL = 0x04;
        static final int RESIZE_LEFT = 0x8;
        static final int RESIZE_TOP = 0x10;
    }

    private static final double BORDER_THICKNESS = 6;
    private static final double ADJ = BORDER_THICKNESS / 2;

    private Rectangle shape;
    private Vec2 lastPos = new Vec2(0, 0);
    private int state = State.NONE;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new obstacle with the given size.
     * @param rect Size and position of the obstacle.
     */
    public Obstacle(Rect rect) {
        shape = new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
        shape.setFill(Color.web("#ff5555"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(BORDER_THICKNESS);
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseReleased(e -> mouseRelease(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
        shape.setOnMouseEntered(e -> hover(e));
        shape.setOnMouseMoved(e -> hover(e));
        shape.setOnMouseExited(e -> shape.setCursor(Cursor.DEFAULT));
    }

    /**
     * Gets the top left corner of the hitbox of the obstacle.
     * @return Top left corner of the obstacle hitbox.
     */
    public Vec2 pos() {
        return apos().sub(ADJ, ADJ);
    }

    /**
     * Gets the hitbox of the obstacle.
     * @return Hitbox of the obstacle.
     */
    public Rect hitbox() {
        return new Rect(
            pos(),
            shape.getWidth() + ADJ * 2,
            shape.getHeight() + ADJ * 2
        );
    }

    /**
     * Sets the hitbox of the obstacle.
     * @param box The new hitbox.
     * @return The new hitbox.
     */
    public Rect hitbox(Rect box) {
        shape.setWidth(box.width() - ADJ * 2);
        shape.setHeight(box.height() - ADJ * 2);
        moveTo(box.topLeft().add(ADJ, ADJ));
        return box;
    }

    /**
     * Gets the shape of the obstacle that can be drawn.
     * @return Shape of the obstacle to be drawn.
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * Checks whether the obstacle is currently dragged by the user.
     * @return true if obstacle is dragged by the user, otherwise false.
     */
    public boolean isDragging() {
        return state == State.DRAGGING;
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

    private Vec2 apos() {
        return new Vec2(shape.getX(), shape.getY());
    }

    private void moveBy(Vec2 vec) {
        moveTo(apos().add(vec));
    }

    private void moveTo(Vec2 pos) {
        shape.setX(pos.x());
        shape.setY(pos.y());
    }

    private void mousePress(MouseEvent event) {
        shape.toFront();
        setSelected(true);
        if (event.getButton() == MouseButton.PRIMARY) {
            if (state == State.NONE) {
                shape.setCursor(Cursor.CLOSED_HAND);
                state = State.DRAGGING;
            }
        }
    }

    private void mouseRelease(MouseEvent event) {
        state = State.NONE;
    }

    private void mouseDrag(MouseEvent event) {
        var newPos = new Vec2(event.getX(), event.getY());
        var delta = newPos.sub(lastPos);
        lastPos = newPos;

        if (state == State.DRAGGING) {
            moveBy(delta);
            return;
        }

        if (hasState(State.RESIZE_HORIZONTAL)) {
            var w = shape.getWidth();
            if (hasState(State.RESIZE_LEFT)) {
                shape.setX(shape.getX() + delta.x());
                w -= delta.x();
            } else {
                w += delta.x();
            }
            if (w < 0) {
                shape.setX(shape.getX() + w);
                w = -w;
                state ^= State.RESIZE_LEFT;
            }
            shape.setWidth(w);
        }

        if (hasState(State.RESIZE_VERTICAL)) {
            var h = shape.getHeight();
            if (hasState(State.RESIZE_TOP)) {
                shape.setY(shape.getY() + delta.y());
                h -= delta.y();
            } else {
                h += delta.y();
            }
            if (h < 0) {
                shape.setY(shape.getY() + h);
                h = -h;
                state ^= State.RESIZE_TOP;
            }
            shape.setHeight(h);
        }
    }

    private void hover(MouseEvent event) {
        lastPos = new Vec2(event.getX(), event.getY());
        var relPos = lastPos.sub(apos());

        state = State.NONE;

        if (relPos.x() < ADJ) {
            state |= State.RESIZE_HORIZONTAL | State.RESIZE_LEFT;
        } else if (shape.getWidth() - relPos.x() <= ADJ) {
            state |= State.RESIZE_HORIZONTAL;
        }

        if (relPos.y() < ADJ) {
            state |= State.RESIZE_VERTICAL | State.RESIZE_TOP;
        } else if (shape.getHeight() - relPos.y() <= ADJ) {
            state |= State.RESIZE_VERTICAL;
        }

        switch (state) {
            case State.RESIZE_HORIZONTAL
                | State.RESIZE_VERTICAL
                | State.RESIZE_LEFT
                | State.RESIZE_TOP:
            case State.RESIZE_HORIZONTAL | State.RESIZE_VERTICAL:
                shape.setCursor(Cursor.NW_RESIZE);
                break;
            case State.RESIZE_HORIZONTAL
                | State.RESIZE_VERTICAL
                | State.RESIZE_TOP:
            case State.RESIZE_HORIZONTAL
                | State.RESIZE_VERTICAL
                | State.RESIZE_LEFT:
                shape.setCursor(Cursor.NE_RESIZE);
                break;
            case State.RESIZE_HORIZONTAL:
            case State.RESIZE_HORIZONTAL | State.RESIZE_LEFT:
                shape.setCursor(Cursor.E_RESIZE);
                break;
            case State.RESIZE_VERTICAL:
            case State.RESIZE_VERTICAL | State.RESIZE_TOP:
                shape.setCursor(Cursor.N_RESIZE);
                break;
            default:
                shape.setCursor(Cursor.OPEN_HAND);
                break;
        }
    }

    private boolean hasState(int flag) {
        return (state & flag) == flag;
    }
}
