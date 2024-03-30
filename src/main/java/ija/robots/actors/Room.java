package ija.robots.actors;

import java.util.ArrayList;

import ija.robots.common.IHitbox;
import ija.robots.common.Rect;

public class Room {
    private Rect bounds;
    private ArrayList<Robot> robots;
    private ArrayList<Obstacle> obstacles;

    public Room(Rect bounds) {
        this.bounds = bounds;
    }

    public Rect bounds() {
        return bounds;
    }

    public boolean add(Robot robot) {
        if (colides(robot.hitbox())) {
            return false;
        }
        robots.add(robot);
        return true;
    }

    public boolean add(Obstacle obstacle) {
        if (colides(obstacle.hitbox())) {
            return false;
        }
        obstacles.add(obstacle);
        return true;
    }

    public boolean colides(IHitbox hitbox) {
        return !bounds.contains(hitbox)
            || obstacles.stream().anyMatch(o -> hitbox.overlaps(o.hitbox()))
            || robots.stream().anyMatch(r -> hitbox.overlaps(r.hitbox()));
    }
}
