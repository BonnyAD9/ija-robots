package ija.robots.actors;

import java.util.ArrayList;

import ija.robots.common.Circle;
import ija.robots.common.IHitbox;
import ija.robots.common.Rect;

public class Room {
    private Rect bounds;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

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
            || colidesObstacle(hitbox, except)
            || colidesRobot(hitbox, except);
    }

    public boolean colidesObstacle(IHitbox hitbox) {
        return colidesObstacle(hitbox, null);
    }

    public boolean colidesObstacle(IHitbox hitbox, Object except) {
        return obstacles.stream().anyMatch(
            o -> o != except && hitbox.overlaps(o.hitbox())
        );
    }

    public boolean colidesRobot(IHitbox hitbox) {
        return colidesRobot(hitbox, null);
    }

    public Robot getColidingRobot(IHitbox hitbox) {
        for (Robot r : robots) {
            if (hitbox.overlaps(r.hitbox())) {
                return r;
            }
        }
        return null;
    }

    public boolean colidesRobot(IHitbox hitbox, Object except) {
        return robots.stream().anyMatch(
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int width = (int)bounds.width();
        sb.append("##");
        for (int i = 0; i < width; ++i) {
            sb.append("##");
        }
        sb.append("##\n");

        String line = sb.toString();

        int height = (int)bounds.height();
        for (int y = 0; y < height; ++y) {
            sb.append("##");
            for (int x = 0; x < width; ++x) {
                Rect box = new Rect(bounds.pos().add(x + 0.25, y + 0.25), 0.5, 0.5);
                if (colidesObstacle(box)) {
                    sb.append("OO");
                } else if (getColidingRobot(box) instanceof Robot r) {
                    sb.append("R" + r.id);
                } else {
                    sb.append("  ");
                }
            }
            sb.append("##\n");
        }

        sb.append(line);

        return sb.toString();
    }
}
