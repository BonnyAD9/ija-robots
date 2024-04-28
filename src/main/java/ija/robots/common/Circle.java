package ija.robots.common;

import javafx.scene.paint.Color;

public final class Circle implements IHitbox {
    private final Vec2 pos;
    private final double radius;

    public Circle(Vec2 pos, double radius) {
        this.pos = pos;
        this.radius = Math.abs(radius);
    }

    public Circle(double x, double y, double radius) {
        this(new Vec2(x, y), radius);
    }

    public Vec2 pos() {
        return pos;
    }

    public Circle pos(Vec2 pos) {
        return new Circle(pos, radius);
    }

    public Circle pos(double x, double y) {
        return new Circle(x, y, radius);
    }

    public double radius() {
        return radius;
    }

    public Circle radius(double radius) {
        return new Circle(pos, radius);
    }

    public double x() {
        return pos.x();
    }

    public Circle x(double x) {
        return new Circle(pos.x(x), radius);
    }

    public double y() {
        return pos.y();
    }

    public Circle y(double y) {
        return new Circle(pos.y(y), radius);
    }

    @Override
    public boolean contains(Vec2 point) {
        return point.isInCircle(pos, radius);
    }

    @Override
    public Rect boundingBox() {
        double diameter = radius * 2;
        return new Rect(pos.sub(radius, radius), diameter, diameter);
    }

    public Circle moveBy(Vec2 vec) {
        return new Circle(pos.add(vec), radius);
    }

    public Circle moveBy(double x, double y) {
        return new Circle(pos.add(x, y), radius);
    }

    @Override
    public boolean overlaps(Rect rect) {
        Rect bound = boundingBox();
        return rect.overlaps(bound) && (
            (rect.xRange().contains(x()) && rect.yRange().contains(y()))
            || contains(rect.topLeft())
            || contains(rect.topRight())
            || contains(rect.botLeft())
            || contains(rect.botRight())
        );
    }

    @Override
    public boolean overlaps(Circle other) {
        return pos.sub(other.pos()).len() < radius + other.radius;
    }

    public javafx.scene.shape.Circle getGraphics() {
        javafx.scene.shape.Circle circle =
            new javafx.scene.shape.Circle(x(), y(), radius());
        circle.setFill(Color.web("#55cc55"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(6);
        return circle;
    }
}
