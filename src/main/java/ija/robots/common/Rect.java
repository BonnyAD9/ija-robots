/**
 * @file
 * @authors Martin Slezák (xsleza26), Jakub Antonín Štigler (xstigl00)
 * @brief Rectangle.
 */

package ija.robots.common;

/**
 * Represents a rectangle.
 */
public final class Rect {
    private final Vec2 pos;
    private final Vec2 size;

    /**
     * Creates new rectangle from its position and size.
     * @param pos Position of the rectangle.
     * @param size Size of the rectangle.
     */
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

    /**
     * Craetes rectangle from its position and size.
     * @param pos Position of the rectangle.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     */
    public Rect(Vec2 pos, double width, double height) {
        this(pos, new Vec2(width, height));
    }

    /**
     * Craetes rectangle from its position and size.
     * @param x X coordinate of the rectangle.
     * @param y Y coordinate of the rectangle.
     * @param size Size of the rectangle.
     */
    public Rect(double x, double y, Vec2 size) {
        this(new Vec2(x, y), size);
    }

    /**
     * Craetes rectangle from its position and size.
     * @param x X coordinate of the rectangle.
     * @param y Y coordinate of the rectangle.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     */
    public Rect(double x, double y, double width, double height) {
        this(new Vec2(x, y), new Vec2(width, height));
    }

    /**
     * Gets the position of the rectangle.
     * @return Position of the rectangle.
     */
    public Vec2 pos() {
        return pos;
    }

    /**
     * Moves the rectangle.
     * @param pos Position to move to.
     * @return New rectangle with the position.
     */
    public Rect pos(Vec2 pos) {
        return new Rect(pos, size);
    }

    /**
     * Moves the rectangle.
     * @param x X coordinate of the rectangle.
     * @param y Y coordinate of the rectangle.
     * @return New rectangle with the position.
     */
    public Rect pos(double x, double y) {
        return new Rect(x, y, size);
    }

    /**
     * Gets the size of the rectangle.
     * @return Size of the rectangle.
     */
    public Vec2 size() {
        return size;
    }

    /**
     * Resizes the rectangle.
     * @param size New size.
     * @return New rectangle with the new size.
     */
    public Rect size(Vec2 size) {
        return new Rect(pos, size);
    }

    /**
     * Resizes the rectangle.
     * @param width New width.
     * @param height New height.
     * @return New rectangle with the new size.
     */
    public Rect size(double width, double height) {
        return new Rect(pos, width, height);
    }

    /**
     * Gets the x coordinate of the rectangle.
     * @return The x coordinate of the rectangle.
     */
    public double x() {
        return this.pos.x();
    }

    /**
     * Moves the x coordinate of the rectangle.
     * @param x New x coordinate.
     * @return New rectangle with the new position.
     */
    public Rect x(double x) {
        return new Rect(pos.x(x), size);
    }

    /**
     * Gets the y coordinate of the rectangle.
     * @return The y coordinate of the rectangle.
     */
    public double y() {
        return this.pos.y();
    }

    /**
     * Moves the y coordinate of the rectangle.
     * @param y The new y coordinate.
     * @return New rectangle with the new position.
     */
    public Rect y(double y) {
        return new Rect(pos.y(y), size);
    }

    /**
     * Gets the width of the rectangle.
     * @return
     */
    public double width() {
        return this.size.width();
    }

    /**
     * Resizes the width of the rectangle.
     * @param width New width of the rectangle.
     * @return New rectangle with the new width.
     */
    public Rect width(double width) {
        return new Rect(pos, size.width(width));
    }

    /**
     * Gets the height of the rectangle.
     * @return Height of the rectangle.
     */
    public double height() {
        return this.size.height();
    }

    /**
     * Resizes the rectangle.
     * @param height New height of the rectangle.
     * @return New rectangle with the new height.
     */
    public Rect height(double height) {
        return new Rect(pos, size.height(height));
    }

    /**
     * Checks whether the given point is in the rectangle.
     * @param point Point to check.
     * @return true if the point is in the rectangle, otherwise false.
     */
    public boolean contains(Vec2 point) {
        return point.isInRectangle(pos, size);
    }

    /**
     * Checks if the given rectangle is contained by this rectangle.
     * @param rect The contained rectangle.
     * @return true if the rectangle is contained by this rectangle, otherwise
     * false.
     */
    public boolean contains(Rect rect) {
        return contains(rect.topLeft()) && contains(rect.botRight());
    }

    /**
     * Gets the position of the top left corner of the rectangle.
     * @return Position of the top left corner of the rectangle.
     */
    public Vec2 topLeft() {
        return pos;
    }

    /**
     * Gets the position of the top right corner of the rectangle.
     * @return Position of the top right corner of the rectangle.
     */
    public Vec2 topRight() {
        return pos.add(width(), 0);
    }

    /**
     * Gets the bottom left corner of the rectangle.
     * @return Position of the bottom left corner of the rectangle.
     */
    public Vec2 botLeft() {
        return pos.add(0, height());
    }

    /**
     * Gets the bottom right corner of the rectangle.
     * @return Position of the bottom right corner of the rectangle.
     */
    public Vec2 botRight() {
        return pos.add(size);
    }

    /**
     * Checks whether the two rectangles overlap.
     * @param rect The other rectangle.
     * @return true if this rectangle overlaps with the other rectangle.
     * Otherwise false.
     */
    public boolean overlaps(Rect rect) {
        return horizontal().overlaps(rect.horizontal())
            && vertical().overlaps(rect.vertical());
    }

    /**
     * Moves the rectangle by the given amount.
     * @param vec Amount to move by.
     * @return New rectangle with the new position.
     */
    public Rect moveBy(Vec2 vec) {
        return new Rect(pos.add(vec), size);
    }

    /**
     * Moves the rectangle by the given amount.
     * @param x X change of the position.
     * @param y Y change of the position.
     * @return New rectangle with the new position.
     */
    public Rect moveBy(double x, double y) {
        return new Rect(pos.add(x, y), size);
    }

    /**
     * Gets the x coordinate of the left side of the rectangle.
     * @return X coordinate of the left side of the rectangle.
     */
    public double left() {
        return pos.x();
    }

    /**
     * Moves the rectangle.
     * @param l The new position f the left side of the rectangle.
     * @return New rectangle with the new position.
     */
    public Rect left(double l) {
        return pos(pos.x(l));
    }

    /**
     * Gets the x coordinate of the right side of the rectangle.
     * @return X coordinate of the right side of the rectangle.
     */
    public double right() {
        return pos.x() + size.width();
    }

    /**
     * Moves the rectangle.
     * @param r The new position f the left side of the rectangle.
     * @return New rectangle with the new position.
     */
    public Rect right(double r) {
        return pos(pos.x(r - size.width()));
    }

    /**
     * Gets the y coordinate of the top side of the rectangle.
     * @return Y coordinate of the top side of the rectangle.
     */
    public double top() {
        return pos.y();
    }

    /**
     * Moves the rectangle.
     * @param t The new position f the top side of the rectangle.
     * @return New rectangle with the new position.
     */
    public Rect top(double t) {
        return pos(pos.y(t));
    }

    /**
     * Gets the y coordinate of the bottom side of the rectangle.
     * @return Y coordinate of the bottom side of the rectangle.
     */
    public double bottom() {
        return pos.y() + size.height();
    }

    /**
     * Moves the rectangle.
     * @param b The new position f the bottom side of the rectangle.
     * @return New rectangle with the new position.
     */
    public Rect bottom(double b) {
        return pos(pos.y(b - size.height()));
    }

    /**
     * Gets the horizontal range of values of the rectangle.
     * @return Vec2(left, right)
     */
    public Vec2 horizontal() {
        return new Vec2(left(), right());
    }

    /**
     * Gets the vertical range of values of the rectangle.
     * @return Vec2(top, bottom)
     */
    public Vec2 vertical() {
        return new Vec2(top(), bottom());
    }

    @Override
    public String toString() {
        return "Rect{" + pos + ", " + size + "}";
    }
}
