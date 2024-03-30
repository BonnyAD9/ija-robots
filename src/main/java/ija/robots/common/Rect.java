package ija.robots.common;

public class Rect {
    private Vec2 pos;
    private Vec2 size;

    public Rect(Vec2 pos, Vec2 size) {
        this.pos = pos;
        this.size = size;
    }

    public Rect(Vec2 pos, float width, float height) {
        this(pos, new Vec2(width, height));
    }

    public Rect(float x, float y, Vec2 size) {
        this(new Vec2(x, y), size);
    }

    public Rect(float x, float y, float width, float height) {
        this(new Vec2(x, y), new Vec2(width, height));
    }

    public Vec2 pos() {
        return pos;
    }

    public Vec2 pos(Vec2 pos) {
        return this.pos = pos;
    }

    public Vec2 size() {
        return size;
    }

    public Vec2 size(Vec2 size) {
        return this.size = size;
    }

    public float x() {
        return this.pos.x();
    }

    public float x(float x) {
        return this.pos.x(x);
    }

    public float y() {
        return this.pos.y();
    }

    public float y(float y) {
        return this.pos.y(y);
    }

    public float width() {
        return this.size.width();
    }

    public float width(float width) {
        return this.size.width(width);
    }

    public float height() {
        return this.size.height();
    }

    public float height(float height) {
        return this.size.height(height);
    }

    public boolean contains(Vec2 point) {
        return point.isInRectangle(pos, size);
    }
}
