package ija.robots.actors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ija.robots.common.Rect;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class Room {
    private Pane view;
    private Timer timer;

    private Rect bounds;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    public Room(Rect bounds) {
        view = new Pane();
        this.bounds = bounds;
        timer = null;
        run(true);
    }

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

    private void tick(double delta) {
        for (var r : robots) {
            if (!r.isDragging()) {
                r.move(delta);
            }
        }
    }

    public Rect bounds() {
        return bounds;
    }

    public void add(Robot robot) {
        robots.add(robot);
        view.getChildren().add(robot.getShape());
    }

    public void add(Obstacle obstacle) {
        obstacles.add(obstacle);
        view.getChildren().add(obstacle.getShape());
    }

    public Pane getGraphics() {
        return view;
    }
}
