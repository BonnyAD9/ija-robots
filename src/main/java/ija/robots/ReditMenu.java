package ija.robots;

import ija.robots.actors.SimObj;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class ReditMenu {
    private SimObj obj;

    private BorderPane pane;
    private FlowPane all;
    private FlowPane robots;

    public ReditMenu() {
        var deselect = new Button("deselect");
        deselect.setOnMouseClicked(e -> {
            if (obj != null) {
                obj.setSelected(false);
            }
        });

        all = new FlowPane(deselect);
        all.setAlignment(Pos.CENTER_RIGHT);

        robots = new FlowPane();
        robots.setAlignment(Pos.CENTER_LEFT);

        pane = new BorderPane();
        pane.setLeft(robots);
        pane.setRight(all);
        pane.setPrefHeight(40);
        pane.setVisible(false);
        pane.setPadding(new Insets(5));
    }

    public Node getNode() {
        return pane;
    }

    public double getHeight() {
        return pane.getHeight();
    }

    public void select(SimObj obj) {
        if (obj == null) {
            pane.setVisible(false);
            return;
        }

        pane.setVisible(true);
        all.setVisible(true);
    }
}
