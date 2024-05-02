package ija.robots;

import ija.robots.actors.SimObj;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ReditMenu {
    private SimObj obj;

    private HBox pane;

    private HBox all;
    private HBox robot;

    private HBox speedPane;
    private TextField speedField;

    private SimHandler<SimObj> onRemove = null;

    public ReditMenu() {
        var all = allPane();
        var robot = robotPane();
        HBox.setHgrow(all, Priority.NEVER);
        HBox.setHgrow(robot, Priority.ALWAYS);
        pane = new HBox(robot, all);
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
        speedField.setEditable(true);
    }

    public void setOnRemove(SimHandler<SimObj> val) {
        onRemove = val;
    }

    private HBox allPane() {
        var deselect = deselectBtn();
        var remove = removeBtn();
        all = new HBox(5, deselect, remove);
        all.setPadding(new Insets(0, 5, 0, 0));
        all.setAlignment(Pos.CENTER_RIGHT);
        return all;
    }

    private Button deselectBtn() {
        var deselect = new Button("deselect");
        deselect.setOnMouseClicked(e -> {
            if (obj != null) {
                obj.setSelected(false);
            }
        });
        return deselect;
    }

    private Button removeBtn() {
        var remove = new Button("remove");
        remove.setOnMouseClicked(e -> {
            if (onRemove != null) {
                onRemove.invoke(obj);
            }
        });
        return remove;
    }

    private HBox robotPane() {
        robot = new HBox(5, speed());
        robot.setPadding(new Insets(0, 0, 0, 5));
        robot.setAlignment(Pos.CENTER_LEFT);
        return robot;
    }

    private HBox speed() {
        speedPane = new HBox(5, new Label("speed:"), speedField());
        speedPane.setAlignment(Pos.CENTER_LEFT);
        return speedPane;
    }

    private TextField speedField() {
        speedField = new TextField();
        speedField.setPrefWidth(60);
        return speedField;
    }
}
