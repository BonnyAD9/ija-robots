package ija.robots.common;

public class Circle {
    private Vec2 pos;
    private float radius;

    public Circle(Vec2 pos, float radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public Circle(float x, float y, float radius) {
        this(new Vec2(x, y), radius);
    }

    public Vec2 pos() {
        return pos;
    }

    public Vec2 pos(Vec2 pos) {
        return this.pos = pos;
    }

    public float radius() {
        return radius;
    }

    public float radius(float radius) {
        return this.radius = radius;
    }

    public float x() {
        return pos.x();
    }

    public float x(float x) {
        return pos.x(x);
    }

    public float y() {
        return pos.y();
    }

    public float y(float y) {
        return pos.y(y);
    }

    public boolean contains(Vec2 point) {
        return point.isInCircle(pos, radius);
    }
}
