package ija.robots.actors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ija.robots.common.Rect;
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
}
