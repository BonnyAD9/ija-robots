package ija.robots.actors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ija.robots.SimHandler;
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

    private SimHandler<SimObj> onSelect = null;
    private SimObj selected = null;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new room with the given bounds.
     * @param bounds Area in the room that the robots move in.
     */
    public Room(Rect bounds) {
        view = new Pane();
        view.setPrefWidth(bounds.width());
        view.setPrefHeight(bounds.height());
        view.setStyle("-fx-background-color: #222222");
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
        robot.onSelect(o -> select(o));
        robots.add(robot);
        view.getChildren().add(robot.getShape());
        view.getChildren().add(robot.getEye());
    }

    /**
     * Adds obstacle to the room.
     * @param obstacle Obstacle to add to the room.
     */
    public void add(Obstacle obstacle) {
        obstacle.onSelect(o -> select(o));
        obstacles.add(obstacle);
        view.getChildren().add(obstacle.getShape());
    }

    /**
     * Removes the given robot/obstacle from the room.
     * @param obj Robot/Obstacle to remove.
     */
    public void remove(SimObj obj) {
        if (obj == selected) {
            select(null);
        }

        if (obj instanceof Robot r) {
            view.getChildren().remove(r.getShape());
            view.getChildren().remove(r.getEye());
            robots.remove(r);
        } else if (obj instanceof Obstacle o) {
            view.getChildren().remove(o.getShape());
            obstacles.remove(o);
        }
    }

    /**
     * Gets the view of the room that can be drawn.
     * @return Drawable view of the room.
     */
    public Pane getGraphics() {
        return view;
    }

    /**
     * Resizes the room.
     * @param bounds The new bounds of the room.
     */
    public void resize(Rect bounds) {
        this.bounds = bounds;
        view.setPrefWidth(bounds.width());
        view.setPrefHeight(bounds.height());
    }

    /**
     * Checks whether the simulation is running.
     * @return true if the simulation is running, otherwise false.
     */
    public boolean isRunning() {
        return timer != null;
    }

    /**
     * Sets the onSelect event that is triggered when object is selected.
     * @param val The event handler,
     */
    public void setOnSelect(SimHandler<SimObj> val) {
        onSelect = val;
    }

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

    private void tick(double delta) {
        moveRobots(delta);

        // collisions of robots with the border of the room
        for (var r : robots) {
            if (!r.isDragging()) {
                borderCollision(r);
            }
        }
    }

    private void moveRobots(double delta) {
        for (var r : robots) {
            if (!r.isDragging()) {
                r.move(delta, obstacleDistance(r));
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

        for (int i = 0; i < robots.size(); ++i) {
            if (robots.get(i).isDragging()) {
                continue;
            }
            for (int j = i + 1; j < robots.size(); ++j) {
                if (!robots.get(j).isDragging()) {
                    robotCollision(robots.get(i), robots.get(j));
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
            if (r.horizontal().contains(c.right())) {
                rob.hitbox(c.right(r.left()));
                return;
            }
            // right edge of the obstacle
            if (r.horizontal().contains(c.left())) {
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

    private void robotCollision(Robot r1, Robot r2) {
        var c1 = r1.hitbox();
        var c2 = r2.hitbox();
        var dir = c2.topLeft().sub(c1.topLeft());
        var cw = (c1.width() + c2.width()) / 2;
        var dirLen = dir.len();
        var over = cw - dirLen;

        if (over <= 0) {
            // No collision
            return;
        }

        dir = dirLen == 0 ? new Vec2(0, 0) : dir.mul(over / (2 * dirLen));
        r1.hitbox(c1.moveBy(dir.negate()));
        r2.hitbox(c2.moveBy(dir));
    }

    private void select(SimObj obj) {
        if (selected != null && selected != obj) {
            var evt = selected.onSelect();
            selected.onSelect(null);
            selected.setSelected(false);
            selected.onSelect(evt);
        }

        selected = obj;
        if (onSelect != null) {
            onSelect.invoke(selected);
        }
    }

    private double obstacleDistance(Robot rob) {
        var r = rob.hitbox();
        var c = (r.topLeft().add(r.botRight())).div(2.0);
        var d = rob.vecAngle();

        double res = rectDistance(
            c, d, new Rect(0, 0, bounds.width(), bounds.height())
        );

        for (var o : obstacles) {
            if (o.isDragging()) {
                res = Math.min(res, rectDistance(c, d, o.hitbox()));
            }
        }

        return Math.max(
            0.,
            Math.min(res - r.width() / 2, Double.POSITIVE_INFINITY)
        );
    }

    private double rectDistance(Vec2 p, Vec2 d, Rect r) {
        return Math.min(
            Math.min(
                segmentDistance(p, d, r.topLeft(), r.topRight()),
                segmentDistance(p, d, r.topRight(), r.botRight())
            ),
            Math.min(
                segmentDistance(p, d, r.botLeft(), r.botRight()),
                segmentDistance(p, d, r.topLeft(), r.botLeft())
            )
        );
    }

    private double segmentDistance(Vec2 p, Vec2 d, Vec2 a, Vec2 b) {
        var is = line_intersection(p, d, a, a.sub(b));
        if (Double.isNaN(is.x())
            || Double.isNaN(is.y())
            || (!inRange(is.x(), a.x(), b.x()) && !inRange(is.y(), a.y(), b.y()))
        ) {
            return Double.POSITIVE_INFINITY;
        }
        var v = is.sub(p);
        if (d.dot(v) < 0) {
            return Double.POSITIVE_INFINITY;
        }

        return Math.sqrt(v.x() * v.x() + v.y() * v.y());
    }

    private Vec2 line_intersection(Vec2 p1, Vec2 d1, Vec2 p2, Vec2 d2) {
        var u = p2.sub(p1).cross(d1) / d1.cross(d2);
        return p2.add(d2.mul(u));
    }

    private boolean inRange(double val, double start, double end) {
        return val > start && val < end;
    }
}
