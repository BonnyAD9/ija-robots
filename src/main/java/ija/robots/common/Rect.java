package ija.robots.common;

public final class Rect {
    private final Vec2 pos;
    private final Vec2 size;

    public Rect(Vec2 pos, Vec2 size) {
        this.pos = pos;
        this.size = size;
    }

    public Rect(Vec2 pos, double width, double height) {
        this(pos, new Vec2(width, height));
    }

    public Rect(double x, double y, Vec2 size) {
        this(new Vec2(x, y), size);
    }

    public Rect(double x, double y, double width, double height) {
        this(new Vec2(x, y), new Vec2(width, height));
    }

    public Vec2 pos() {
        return pos;
    }

    public Rect pos(Vec2 pos) {
        return new Rect(pos, size);
    }

    public Rect pos(double x, double y) {
        return new Rect(x, y, size);
    }

    public Vec2 size() {
        return size;
    }

    public Rect size(Vec2 size) {
        return new Rect(pos, size);
    }

    public Rect size(double width, double height) {
        return new Rect(pos, width, height);
    }

    public double x() {
        return this.pos.x();
    }

    public Rect x(double x) {
        return new Rect(pos.x(x), size);
    }

    public double y() {
        return this.pos.y();
    }

    public Rect y(double y) {
        return new Rect(pos.y(y), size);
    }

    public double width() {
        return this.size.width();
    }

    public Rect width(double width) {
        return new Rect(pos, size.width(width));
    }

    public double height() {
        return this.size.height();
    }

    public Rect height(double height) {
        return new Rect(pos, size.height(height));
    }

    public boolean contains(Vec2 point) {
        return point.isInRectangle(pos, size);
    }
}
