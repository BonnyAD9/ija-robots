package ija.robots.actors;

import java.util.ArrayList;

import ija.robots.common.Rect;
import javafx.scene.layout.Pane;

public class Room {
    private Pane view;

    private Rect bounds;
    private ArrayList<Robot> robots = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    public Room(Rect bounds) {
        view = new Pane();
        this.bounds = bounds;
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
