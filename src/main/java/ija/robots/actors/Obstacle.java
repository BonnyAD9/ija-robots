package ija.robots.actors;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle {
    private static final double BORDER_THICKNESS = 6;
    private static final double ADJ = BORDER_THICKNESS / 2;

    private class State {
        static final int NONE = 0x0;
        static final int DRAGGING = 0x1;
        static final int RESIZE_HORIZONTAL = 0x02;
        static final int RESIZE_VERTICAL = 0x04;
        static final int RESIZE_LEFT = 0x8;
        static final int RESIZE_TOP = 0x10;
    }

    private Rectangle shape;
    private Vec2 lastPos = new Vec2(0, 0);
    private int state = State.NONE;

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

        if (state == State.NONE) {
            state = State.DRAGGING;
        }
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

    private boolean hasState(int flag) {
        return (state & flag) == flag;
    }
}
