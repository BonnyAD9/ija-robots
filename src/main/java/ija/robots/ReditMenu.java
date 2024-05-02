package ija.robots;

import ija.robots.actors.SimObj;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class ReditMenu {
    private SimObj obj;

    private StackPane pane;
    private FlowPane all;
    private FlowPane robots;

    private SimHandler<SimObj> onRemove = null;

    public ReditMenu() {
        var deselect = new Button("deselect");
        deselect.setOnMouseClicked(e -> {
            if (obj != null) {
                obj.setSelected(false);
            }
        });

        var remove = new Button("remove");
        remove.setOnMouseClicked(e -> {
            if (onRemove != null) {
                onRemove.invoke(obj);
            }
        });

        all = new FlowPane(5, 5, deselect, remove);
        all.setPadding(new Insets(0, 5, 0, 0));
        all.setAlignment(Pos.CENTER_RIGHT);

        robots = new FlowPane();
        robots.setAlignment(Pos.CENTER_LEFT);

        pane = new StackPane(robots, all);
        pane.setPrefHeight(40);
        pane.setVisible(false);
    }

    public Node getNode() {
        return pane;
    }

    public double getHeight() {
        return pane.getHeight();
    }

    public void select(SimObj obj) {
        if (this.obj == obj) {
            return;
        }
        this.obj = obj;

        if (obj == null) {
            pane.setVisible(false);
            return;
        }

        pane.setVisible(true);
        all.setVisible(true);
    }

    public void setOnRemove(SimHandler<SimObj> val) {
        onRemove = val;
    }
}
