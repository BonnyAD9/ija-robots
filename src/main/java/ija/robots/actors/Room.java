package ija.robots.actors;

import java.util.ArrayList;

import ija.robots.common.Circle;
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
        return colides(hitbox, null);
    }

    public boolean colides(IHitbox hitbox, Object except) {
        return !bounds.contains(hitbox)
            || obstacles.stream().anyMatch(
                o -> o != except && hitbox.overlaps(o.hitbox())
            ) || robots.stream().anyMatch(
                r -> r != except && hitbox.overlaps(r.hitbox())
            );
    }

    public void tick(double delta) {
        // TODO: smarter moving, check collisions after all move
        for (Robot r : robots) {
            moveRobot(r, delta);
        }
    }

    private void moveRobot(Robot robot, double delta) {
        // TODO: better moving, move where it is possible on collision
        Circle newHitbox = robot.movedHitbox(delta);
        if (!colides(newHitbox, robot)) {
            robot.moveTo(newHitbox.pos());
        }
    }
}
