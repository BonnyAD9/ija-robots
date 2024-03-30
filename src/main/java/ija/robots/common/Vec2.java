package ija.robots.common;

public final class Vec2 {
    private float x;
    private float y;

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x + other.x, y - other.y);
    }

    public Vec2 mul(float scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public float dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public static Vec2 unit(float angle) {
        return new Vec2((float)Math.cos(angle), (float)Math.sin(angle));
    }

    public Vec2 rotated(float angle) {
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        return transformed(new Vec2(cos, -sin), new Vec2(sin, cos));
    }

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

    public Vec2 withLen(float len) {
        float mul = len / this.len();
        return new Vec2(x * mul, y * mul);
    }

    public float sqLen() {
        return x * x + y * y;
    }

    public float angle() {
        return (float)Math.atan2(y, x);
    }

    public Vec2 withAngle(float angle) {
        return Vec2.unit(angle).mul(len());
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
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

    public Vec2 transformed(Vec2 row1, Vec2 row2) {
        return new Vec2(dot(row1), dot(row2));
    }
}
