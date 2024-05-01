package ija.robots.common;

public final class Rect {
    private final Vec2 pos;
    private final Vec2 size;

    public Rect(Vec2 pos, Vec2 size) {
        if (size.width() < 0) {
            pos = pos.add(size.width(), 0);
            size = size.width(-size.width());
        }
        if (size.height() < 0) {
            pos = pos.add(0, size.height());
            size = size.height(-size.height());
        }
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

    public boolean contains(Rect rect) {
        return contains(rect.topLeft()) && contains(rect.botRight());
    }

    public boolean contains(Circle circle) {
        return contains(circle.boundingBox());
    }

    public Vec2 xRange() {
        return new Vec2(x(), x() + width());
    }

    public Vec2 yRange() {
        return new Vec2(y(), y() + height());
    }

    public Vec2 topLeft() {
        return pos;
    }

    public Vec2 topRight() {
        return pos.add(width(), 0);
    }

    public Vec2 botLeft() {
        return pos.add(0, height());
    }

    public Vec2 botRight() {
        return pos.add(size);
    }

    public boolean overlaps(Rect rect) {
        return xRange().overlaps(rect.xRange())
            && yRange().overlaps(rect.yRange());
    }

    public boolean overlaps(Circle circle) {
        return circle.overlaps(this);
    }

    public Rect moveBy(Vec2 vec) {
        return new Rect(pos.add(vec), size);
    }

    public Rect moveBy(double x, double y) {
        return new Rect(pos.add(x, y), size);
    }

    public Rect boundingBox() {
        return this;
    }

    public double left() {
        return pos.x();
    }

    public Rect left(double l) {
        return pos(pos.x(l));
    }

    public double right() {
        return pos.x() + size.width();
    }

    public Rect right(double r) {
        return pos(pos.x(r - size.width()));
    }

    public double top() {
        return pos.y();
    }

    public Rect top(double t) {
        return pos(pos.y(t));
    }

    public double bottom() {
        return pos.y() + size.height();
    }

    public Rect bottom(double b) {
        return pos(pos.y(b - size.height()));
    }

    public Vec2 horizontal() {
        return new Vec2(left(), right());
    }

    public Vec2 vertical() {
        return new Vec2(top(), bottom());
    }

    @Override
    public String toString() {
        return "Rect{" + pos + ", " + size + "}";
    }
}
