package ija.robots.common;

public class Vec2 {
    private float x;
    private float y;

    public float x() {
        return x;
    }

    public float x(float x) {
        return this.x = x;
    }

    public float y() {
        return y;
    }

    public float y(float y) {
        return this.y = y;
    }

    public float width() {
        return x;
    }

    public float width(float width) {
        return this.x = width;
    }

    public float height() {
        return this.y;
    }

    public float height(float height) {
        return this.y = height;
    }

    public float len() {
        return (float)Math.sqrt(sqLen());
    }

    public float sqLen() {
        return x * x + y * y;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x + other.x, y - other.y);
    }

    public boolean isInCircle(Vec2 center, float radius) {
        return sub(center).sqLen() < radius * radius;
    }

    public boolean isInRectangle(Vec2 pos, Vec2 size) {
        return x > pos.x
            && y > pos.y
            && x < pos.x + size.width()
            && y < pos.y + size.height();
    }
}
