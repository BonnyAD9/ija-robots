package ija.robots.common;

/**
 * Represents vector, position, size or range.
 */
public final class Vec2 {
    private final double x;
    private final double y;

    /**
     * Creates a new unit vector.
     * @param angle Angle of the vector.
     * @return New vector with length 1 and the given angle.
     */
    public static Vec2 unit(double angle) {
        return new Vec2(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Craetes new vector from polar coordinates.
     * @param len Length of the vector.
     * @param angle Modulus (angle) of the vector.
     * @return New vector.
     */
    public static Vec2 polar(double len, double angle) {
        return unit(angle).mul(len);
    }

    /**
     * Craetes new vector from the x and y components.
     * @param x X component.
     * @param y Y component.
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the two vectors.
     * @param other The other vector.
     * @return Sum of the two vectors.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    /**
     * Adds to the components of the vector.
     * @param x Addition to the x component.
     * @param y Addition to the y component.
     * @return New vector with the new values.
     */
    public Vec2 add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    /**
     * Subtracts the two vectors.
     * @param other The other vector.
     * @return Difference of the two vectors.
     */
    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    /**
     * Subtract to the components of the vector.
     * @param x Difference to the x component.
     * @param y Difference to the y component.
     * @return New vector with the new values.
     */
    public Vec2 sub(double x, double y) {
        return new Vec2(this.x - x, this.y - y);
    }

    /**
     * Multiplies the components by a scalar value.
     * @param scalar Value to multiply by.
     * @return Vector with the new values.
     */
    public Vec2 mul(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    /**
     * Divides the components by a scalar value.
     * @param scalar Value to divide by.
     * @return Vector with the new values.
     */
    public Vec2 div(double scalar) {
        return new Vec2(x / scalar, y / scalar);
    }

    /**
     * Calculates the dot product of the two vectors.
     * @param other The other vector.
     * @return Dot product of the two vectors.
     */
    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Calculates the cross product of the two vectors
     * @param other The other vector
     * @return Cross product of the two vectors
     */
    public double cross(Vec2 other) {
        return x * other.y - y * other.x;
    }

    /**
     * Creates new vector by rotating this.
     * @param angle Angle to rotate by.
     * @return A new vector.
     */
    public Vec2 rotated(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return transformed(new Vec2(cos, -sin), new Vec2(sin, cos));
    }

    /**
     * Gets the x coordinate of the vector.
     * @return X coordinate of the vector.
     */
    public double x() {
        return x;
    }

    /**
     * Sets the x value of the vector.
     * @param x The new value.
     * @return New vector with the new x value.
     */
    public Vec2 x(double x) {
        return new Vec2(x, y);
    }

    /**
     * Gets the y coordinate of the vector.
     * @return Y coordinate of the vector.
     */
    public double y() {
        return y;
    }

    /**
     * Sets the y value of the vector.
     * @param y The new value.
     * @return New vector with the new y value.
     */
    public Vec2 y(double y) {
        return new Vec2(x, y);
    }

    /**
     * Gets the width when this is size.
     * @return The width.
     */
    public double width() {
        return x;
    }

    /**
     * Sets the width when this is a size.
     * @param width The new wdith.
     * @return New size with the new components.
     */
    public Vec2 width(double width) {
        return new Vec2(width, height());
    }

    /**
     * Gets the height when this is size.
     * @return The height.
     */
    public double height() {
        return this.y;
    }

    /**
     * Sets the height when this is a size.
     * @param height The new height.
     * @return New size with the new components.
     */
    public Vec2 height(double height) {
        return new Vec2(width(), height);
    }

    /**
     * Gets the length (absolute value) of the vector.
     * @return
     */
    public double len() {
        return Math.sqrt(sqLen());
    }

    /**
     * Changes the length of the vector.
     * @param len The new length of the vector.
     * @return New vector with the new length.
     */
    public Vec2 len(double len) {
        double mul = len / this.len();
        return new Vec2(x * mul, y * mul);
    }

    /**
     * Gets the length of the vector squared.
     * @return Length of the vector squared.
     */
    public double sqLen() {
        return x * x + y * y;
    }

    /**
     * Gets the modulus of the vector.
     * @return Modulus of the vector.
     */
    public double angle() {
        return Math.atan2(y, x);
    }

    /**
     * Sets the angle of the vector.
     * @param angle The new angle.
     * @return New vector with the new angle.
     */
    public Vec2 angle(double angle) {
        return Vec2.unit(angle).mul(len());
    }

    /**
     * Checks whether the point is in the circle.
     * @param center Center of the circle.
     * @param radius Radius of the circle.
     * @return true if the point is in the circle, otherwise false.
     */
    public boolean isInCircle(Vec2 center, double radius) {
        return sub(center).sqLen() < radius * radius;
    }

    /**
     * Checks if the point is in the rectangle.
     * @param pos Position of the rectangle.
     * @param size Size of the rectangle.
     * @return true if the point is in the rectangle, otherwise false.
     */
    public boolean isInRectangle(Vec2 pos, Vec2 size) {
        return x >= pos.x
            && y >= pos.y
            && x <= pos.x + size.width()
            && y <= pos.y + size.height();
    }

    /**
     * Transforms the point with a matrix.
     * @param row1 First row of the matrix.
     * @param row2 Second row of the matrix.
     * @return New transformed vector.
     */
    public Vec2 transformed(Vec2 row1, Vec2 row2) {
        return new Vec2(dot(row1), dot(row2));
    }

    /**
     * Checks whether a value is contained in this range.
     * @param point Value to check.
     * @return true if the value is in the range, otherwise false.
     */
    public boolean contains(double point) {
        return x < point && y > point;
    }

    /**
     * Checks whether this range of values overlaps with the other.
     * @param line Other range of values.
     * @return true if the ranges overlap otherwise false.
     */
    public boolean overlaps(Vec2 line) {
        return contains(line.x)
            || contains(line.y)
            || line.contains(x)
            || equals(line);
    }

    /**
     * Negates the components of the vector.
     * @return New vector with negated components.
     */
    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Vec2 v && v.x == x && v.y == y;
    }
}
