package ija.robots.common;

public interface IHitbox {
    public boolean overlaps(Circle circle);
    public boolean overlaps(Rect rect);
    public boolean contains(Vec2 point);
    public Rect boundingBox();
}
