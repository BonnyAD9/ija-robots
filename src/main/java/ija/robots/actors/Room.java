/**
 * @file
 * @authors Martin Slezák (xsleza26), Jakub Antonín Štigler (xstigl00)
 * @brief Room that can contain robots and obstacles and simulate their
 * movement.
 */

package ija.robots.actors;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Logger;

import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Room with robots and obstacles.
 */
public class Room {
    private Pane view;
    private Timer timer;

    private Rect bounds;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    private Consumer<SimObj> onSelect = null;
    private SimObj selected = null;

    private Logger log = Logger.getLogger("Room");

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new room with the given bounds.
     * @param bounds Area in the room that the robots move in.
     */
    public Room(Rect bounds) {
        log.info("Creating new room.");
        view = new Pane();
        view.setPrefWidth(bounds.width());
        view.setPrefHeight(bounds.height());
        view.setStyle("-fx-background-color: #222222");
        this.bounds = bounds;
        timer = null;
        run(true);

        view.setFocusTraversable(true);
        view.setOnMousePressed(e -> view.requestFocus());
        view.setOnKeyPressed(e -> keyListener(e, true));
        view.setOnKeyReleased(e -> keyListener(e, false));
    }

    /**
     * Play/pause the simulation.
     * @param play when true simulation is played, otherwise simulation is
     * paused.
     */
    public void run(boolean play) {
        if (play && timer == null) {
            log.info("Playing the simulation.");
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
            log.info("Pausing the simulation.");
            timer.cancel();
            timer = null;
        } else {
            log.info("Simulation was already playing/paused");
        }
    }

    /**
     * Saves room to the file
     * @param stage window
     * @param filename name of the file to save into
     */
    public void save(Stage stage, String filename) {
        log.info("Saving the room to file '" + filename + "'");
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(
                "room: " + stage.getWidth() + "x" +
                (stage.getHeight() - 80) + "\n"
            );
            for (var obst : obstacles) {
                writer.write(obst + "\n");
            }
            for (var rob : robots) {
                writer.write(rob + "\n");
            }
            writer.close();
        } catch (IOException e) {
            log.severe("Failed to save room: " + e.getMessage());
            Alert alert = new Alert(
                AlertType.ERROR,
                e.getMessage()
            );
            alert.show();
            return;
        }

        Alert alert = new Alert(
            AlertType.CONFIRMATION,
            "The room was successfully saved"
        );
        alert.show();
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
        log.info("Adding robot: " + robot);
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
        log.info("Adding obstacle: " + obstacle);
        obstacle.onSelect(o -> select(o));
        obstacles.add(obstacle);
        view.getChildren().add(obstacle.getShape());
    }

    /**
     * Removes the given robot/obstacle from the room.
     * @param obj Robot/Obstacle to remove.
     */
    public void remove(SimObj obj) {
        log.info("Removing object: " + obj);

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
    public void setOnSelect(Consumer<SimObj> val) {
        onSelect = val;
    }

    /**
     * Replaces one robot with another.
     * @param o Robot to replace.
     * @param n New robot to replace with.
     */
    public void changeRobot(Robot o, Robot n) {
        log.info("Changing robot:");
        log.info("    from: " + o);
        log.info("    to:   " + n);
        var select = (SimObj)o == selected;
        remove(o);
        add(n);
        if (select) {
            n.setSelected(true);
        }
    }

    /**
     * Clears current items from room and adds new
     * @param obstacles new obstacles
     * @param robots new robots
     */
    public void clear(ArrayList<Obstacle> obstacles, ArrayList<Robot> robots) {
        log.info(
            "Replacing "
                + this.robots.size()
                + " robots and "
                + this.obstacles.size()
                + " obstacles for "
                + robots.size()
                + " robots and "
                + obstacles.size()
                + " obstacles."
        );

        for (var obst : this.obstacles) {
            view.getChildren().remove(obst.getShape());
        }
        for (var rob : this.robots) {
            view.getChildren().remove(rob.getShape());
            view.getChildren().remove(rob.getEye());
        }

        this.obstacles.clear();
        this.robots.clear();

        for (var obst : obstacles) {
            add(obst);
        }
        for (var rob : robots) {
            add(rob);
        }
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

    private void keyListener(KeyEvent event, boolean start) {
        if (selected != null && event.getCode() == KeyCode.DELETE) {
            remove(selected);
            return;
        }

        if (!isRunning() || !(selected instanceof ControlRobot))
            return;

        var rob = (ControlRobot)selected;
        switch (event.getCode()) {
            case UP:
                rob.forward(start);
                break;
            case RIGHT:
                rob.right(start);
                break;
            case LEFT:
               rob.left(start);
                break;
            default:
                return;
        }
        event.consume();
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
        log.info("Selecting new object: " + obj);
        if (selected != null && selected != obj) {
            log.info("Deselecting old object: " + selected);
            var evt = selected.onSelect();
            selected.onSelect(null);
            selected.setSelected(false);
            selected.onSelect(evt);
        }

        selected = obj;
        if (onSelect != null) {
            onSelect.accept(selected);
        }
    }

    private double obstacleDistance(Robot rob) {
        var r = rob.hitbox();
        var c = r.topLeft().add(r.botRight()).div(2.0);
        var d = rob.vecAngle();

        double res = rectDistance(
            c, d, new Rect(0, 0, bounds.width(), bounds.height())
        );

        for (var o : obstacles) {
            if (!o.isDragging()) {
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
        var is = lineIntersection(p, d, a, a.sub(b));
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

    private Vec2 lineIntersection(Vec2 p1, Vec2 d1, Vec2 p2, Vec2 d2) {
        var u = p2.sub(p1).cross(d1) / d1.cross(d2);
        return p2.add(d2.mul(u));
    }

    private boolean inRange(double val, double start, double end) {
        return val > start && val < end;
    }
}
