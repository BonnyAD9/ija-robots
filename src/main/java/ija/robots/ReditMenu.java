package ija.robots;

import java.util.function.BiConsumer;

import ija.robots.actors.Robot;
import ija.robots.actors.SimObj;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Menu for editing robot/obstacle parameters.
 */
public class ReditMenu {
    private SimObj obj;

    private HBox pane;

    private HBox all;
    private HBox robot;

    private HBox speedPane;
    private TextField speedField;

    private HBox anglePane;
    private TextField angleField;

    private SimHandler<SimObj> onRemove = null;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new menu for editing robot/obstacle parameters.
     */
    public ReditMenu() {
        var all = allPane();
        var robot = robotPane();
        HBox.setHgrow(all, Priority.NEVER);
        HBox.setHgrow(robot, Priority.ALWAYS);
        pane = new HBox(robot, all);
        pane.setPrefHeight(40);
        pane.setVisible(false);
    }

    /**
     * Gets the menu node.
     * @return The menu node.
     */
    public Node getNode() {
        return pane;
    }

    /**
     * Gets the height of the menu node.
     * @return Height of the menu node.
     */
    public double getHeight() {
        return pane.getHeight();
    }

    /**
     * Chooses object which parameters will be edited.
     * @param obj Object to edit.
     */
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

        if (!(obj instanceof Robot r)) {
            robot.setVisible(false);
            return;
        }

        robot.setVisible(true);

        speedField.setText(String.format("%.2f", r.speed()));
        angleField.setText(String.format("%.2f", r.angle()));
    }

    /**
     * Sets the onRemove event handler that is invoked when the object should
     * be removed.
     * @param val Object that should be removed.
     */
    public void setOnRemove(SimHandler<SimObj> val) {
        onRemove = val;
    }

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

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
        robot = new HBox(5, speed(), angle());
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
        speedField.setTextFormatter(numberFormatter(0, Double.MAX_VALUE));
        setNumber(speedField, (s, r) -> r.speed(s), Robot.class);
        return speedField;
    }

    private HBox angle() {
        anglePane = new HBox(5, new Label("angle:"), angleField());
        anglePane.setAlignment(Pos.CENTER_LEFT);
        return anglePane;
    }

    private TextField angleField() {
        angleField = new TextField();
        angleField.setPrefWidth(60);
        angleField.setTextFormatter(numberFormatter(-360, 360));
        setNumber(angleField, (a, r) -> r.angle(a), Robot.class);
        return angleField;
    }

    private TextFormatter<?> numberFormatter(double min, double max) {
        return new TextFormatter<>(c -> {
            var txt = c.getControlNewText();
            if (txt.isEmpty() || min < 0 && txt.equals("-")) {
                return c;
            }
            try {
                var n = Double.parseDouble(txt);
                if (n >= min && n <= max) {
                    return c;
                }
            } catch (NumberFormatException ex) {}
            c.setText("");
            c.setRange(c.getRangeStart(), c.getRangeStart());
            return c;
        });
    }

    private <T extends SimObj> void setNumber(
        TextField field,
        BiConsumer<Double, T> setter,
        Class<T> clazz
    ) {
        field.setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) {
                return;
            }
            e.consume();
            try {
                setter.accept(
                    Double.parseDouble(field.getText()), clazz.cast(obj)
                );
            } catch (Exception ex) {}
        });
    }
}
