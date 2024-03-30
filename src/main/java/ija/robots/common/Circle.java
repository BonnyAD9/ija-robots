package ija.robots.common;

public final class Circle {
    private final Vec2 pos;
    private final double radius;

    public Circle(Vec2 pos, double radius) {
        this.pos = pos;
        this.radius = radius;
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

    public boolean contains(Vec2 point) {
        return point.isInCircle(pos, radius);
    }
}
