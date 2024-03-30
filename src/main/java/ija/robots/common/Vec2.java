package ija.robots.common;

public final class Vec2 {
    private final double x;
    private final double y;

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x + other.x, y - other.y);
    }

    public Vec2 sub(double x, double y) {
        return new Vec2(this.x - x, this.y - y);
    }

    public Vec2 mul(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public static Vec2 unit(double angle) {
        return new Vec2(Math.cos(angle), Math.sin(angle));
    }

    public static Vec2 polar(double len, double angle) {
        return unit(angle).mul(len);
    }

    public Vec2 rotated(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return transformed(new Vec2(cos, -sin), new Vec2(sin, cos));
    }

    public double x() {
        return x;
    }

    public Vec2 x(double x) {
        return new Vec2(x, y);
    }

    public double y() {
        return y;
    }

    public Vec2 y(double y) {
        return new Vec2(x, y);
    }

    public double width() {
        return x;
    }

    public Vec2 width(double width) {
        return new Vec2(width, height());
    }

    public double height() {
        return this.y;
    }

    public Vec2 height(double height) {
        return new Vec2(width(), height);
    }

    public double len() {
        return Math.sqrt(sqLen());
    }

    public Vec2 len(double len) {
        double mul = len / this.len();
        return new Vec2(x * mul, y * mul);
    }

    public double sqLen() {
        return x * x + y * y;
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public Vec2 angle(double angle) {
        return Vec2.unit(angle).mul(len());
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInCircle(Vec2 center, double radius) {
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

    public boolean contains(double point) {
        return x < y
            ? x < point && y > point
            : y < point && x > point;
    }

    public boolean overlaps(Vec2 line) {
        return contains(line.x)
            || contains(line.y)
            || line.contains(x);
    }
}
