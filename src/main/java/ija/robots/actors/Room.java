package ija.robots.actors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

/**
 * Room with robots and obstacles.
 */
public class Room {
    private Pane view;
    private Timer timer;

    private Rect bounds;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new room with the given bounds.
     * @param bounds Area in the room that the robots move in.
     */
    public Room(Rect bounds) {
        view = new Pane();
        this.bounds = bounds;
        timer = null;
        run(true);
    }

    /**
     * Play/pause the simulation.
     * @param play when true simulation is played, otherwise simulation is
     * paused.
     */
    public void run(boolean play) {
        if (play && timer == null) {
            timer = new Timer();
            timer.schedule(
                new TimerTask() {
                    public void run() {
                        Platform.runLater(() -> tick(10. / 1000.));
                    }
                },
                0,
                10
            );
        } else if (!play && timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Gets the bounds of the room.
     * @return Bounds of the room.
     */
    public Rect bounds() {
        return bounds;
    }

    /**
     * Adds robot to the room.
     * @param robot robot to add to the room.
     */
    public void add(Robot robot) {
        robots.add(robot);
        view.getChildren().add(robot.getShape());
    }

    /**
     * Adds obstacle to the room.
     * @param obstacle Obstacle to add to the room.
     */
    public void add(Obstacle obstacle) {
        obstacles.add(obstacle);
        view.getChildren().add(obstacle.getShape());
    }

    /**
     * Gets the view of the room that can be drawn.
     * @return Drawable view of the room.
     */
    public Pane getGraphics() {
        return view;
    }

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

    private void tick(double delta) {
        moveRobots(delta);

        for (var r : robots) {
            if (!r.isDragging()) {
                borderCollision(r);
            }
        }
    }

    private void moveRobots(double delta) {
        for (var r : robots) {
            if (!r.isDragging()) {
                r.move(delta);
            }
        }

        for (var o : obstacles) {
            if (o.isDragging()) {
                continue;
            }
            for (var r : robots) {
                if (!r.isDragging()) {
                    obstacleCollision(r, o);
                }
            }
        }
    }

    private void borderCollision(Robot rob) {
        var bounds = bounds();
        var r = rob.hitbox();

        if (r.left() < bounds.left()) {
            r = r.left(bounds.left());
        } else if (r.right() > bounds.right()) {
            r = r.right(bounds.right());
        }

        if (r.top() < bounds.top()) {
            r = r.top(bounds.top());
        } else if (r.bottom() > bounds.bottom()) {
            r = r.bottom(bounds.bottom());
        }

        rob.hitbox(r);
    }

    private void obstacleCollision(Robot rob, Obstacle obs) {
        // circle (robot)
        var c = rob.hitbox();
        // rectangle (obstacle)
        var r = obs.hitbox();

        // check edge overlap
        var center = c.topLeft().add(c.botRight()).div(2);
        var cx = center.x();
        var cy = center.y();
        // horizontal edge
        if (r.horizontal().contains(cx)) {
            // top edge of the obstacle
            if (r.vertical().contains(c.bottom())) {
                rob.hitbox(c.bottom(r.top()));
                return;
            }
            // bottom edge of obstacle
            if (r.vertical().contains(c.top())) {
                rob.hitbox(c.top(r.bottom()));
                return;
            }
            // no overlap
            return;
        }
        // vertical edge
        if (r.vertical().contains(cy)) {
            // left edge of the obstacle
            if (r.horizontal().contains(r.right())) {
                rob.hitbox(c.right(r.left()));
                return;
            }
            // right edge of the obstacle
            if (r.horizontal().contains(r.left())) {
                rob.hitbox(c.left(r.right()));
                return;
            }
            // no overlap
            return;
        }

        // check corner overlap
        var radius = c.width() / 2;
        if (r.topLeft().isInCircle(center, radius)) {
            cornerCollision(rob, r.topLeft());
        } else if (r.topRight().isInCircle(center, radius)) {
            cornerCollision(rob, r.topRight());
        } else if (r.botRight().isInCircle(center, radius)) {
            cornerCollision(rob, r.botRight());
        } else if (r.botLeft().isInCircle(center, radius)) {
            cornerCollision(rob, r.botLeft());
        }
    }

    private void cornerCollision(Robot rob, Vec2 p) {
        var box = rob.hitbox();
        var c = box.topLeft().add(box.botRight()).div(2);
        var r = box.width() / 2;

        var mv = p.sub(c);
        mv = mv.sub(mv.mul(r / mv.len()));

        rob.hitbox(box.moveBy(mv));
    }
}
