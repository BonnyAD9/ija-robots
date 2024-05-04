package ija.robots;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

import ija.robots.actors.AutoRobot;
import ija.robots.actors.ControlRobot;
import ija.robots.actors.Robot;
import ija.robots.actors.SimObj;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private class RobotType {
        static final int DUMMY = 0;
        static final int AUTO = 1;
        static final int CONTROL = 2;
    }

    private SimObj obj;

    private HBox pane;


    private HBox all;
    private HBox robot;

    private ComboBox<String> rtype;
    private TextField speed;
    private TextField angle;

    private HBox crobot;
    private TextField rspeed;

    private HBox arobot;
    private TextField edist;
    private TextField rdist;

    private Logger log = Logger.getLogger("ReditMenu");

    private Consumer<SimObj> onRemove = null;
    private BiConsumer<Robot, Robot> onChangeRobot = null;

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

        log.info("Craete redit menu.");
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
            log.info("Selecting the same object: " + obj);
            return;
        }
        log.info("new selection: " + obj);

        if (this.obj instanceof Robot r) {
            r.setOnAngleChange(null);
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

        r.setOnAngleChange(a -> {
            if (!angle.isFocused()) {
                angle.setText(
                    String.format("%.2f", reduceDegrees(-a / Math.PI * 180))
                );
            }
        });

        robot.setVisible(true);
        crobot.setVisible(false);
        arobot.setVisible(false);

        rtype.getSelectionModel().select(getRobotType());
        speed.setText(String.format("%.2f", r.speed()));
        angle.setText(
            String.format("%.2f", reduceDegrees(-r.angle() / Math.PI * 180))
        );

        if (r instanceof ControlRobot cr) {
            crobot.setVisible(true);

            rspeed.setText(String.format("%.2f", cr.rspeed() / Math.PI * 180));
        } else if (r instanceof AutoRobot ar) {
            crobot.setVisible(true);
            arobot.setVisible(true);

            rspeed.setText(String.format("%.2f", ar.rspeed() / Math.PI * 180));
            edist.setText(String.format("%.2f", ar.edist()));
            rdist.setText(String.format("%.2f", -ar.erot() / Math.PI * 180));
        }
    }

    /**
     * Sets the onRemove event handler that is invoked when the object should
     * be removed.
     * @param val Object that should be removed.
     */
    public void setOnRemove(Consumer<SimObj> val) {
        onRemove = val;
    }

    /**
     * Sets the event handler for the onChangeRobot event that triggers when
     * one robot should be replaced by another.
     * @param val The event handler.
     */
    public void setOnChangeRobot(BiConsumer<Robot, Robot> val) {
        onChangeRobot = val;
    }

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

    private HBox allPane() {
        all = new HBox(5, deselectBtn(), removeBtn());
        all.setPadding(new Insets(0, 5, 0, 0));
        all.setAlignment(Pos.CENTER_RIGHT);
        return all;
    }

    private Button deselectBtn() {
        var deselect = new Button("deselect");
        deselect.setOnMouseClicked(e -> {
            log.info("Deselecting: " + obj);
            if (obj != null) {
                obj.setSelected(false);
            }
        });
        return deselect;
    }

    private Button removeBtn() {
        var remove = new Button("remove");
        remove.setOnMouseClicked(e -> {
            log.info("Removing: " + obj);
            if (onRemove != null) {
                onRemove.accept(obj);
            }
        });
        return remove;
    }

    private HBox robotPane() {
        robot = new HBox(
            5,
            rtype(),
            new Label("speed:"),
            speed(),
            new Label("angle:"),
            angle(),
            crobot(),
            arobot()
        );
        robot.setPadding(new Insets(0, 0, 0, 5));
        robot.setAlignment(Pos.CENTER_LEFT);
        return robot;
    }

    private ComboBox<String> rtype() {
        rtype = new ComboBox<>();
        rtype.getItems().addAll("Dummy", "Auto", "Control");
        rtype.getSelectionModel().selectedIndexProperty().addListener(i -> {
            var idx = rtype.getSelectionModel().getSelectedIndex();
            if (idx == getRobotType() || onChangeRobot == null || !(obj instanceof Robot r)) {
                log.warning("Cannot change robot type");
                return;
            }
            switch (idx) {
                case RobotType.DUMMY:
                    log.info("Changing robot to Dummy: " + r);
                    onChangeRobot.accept(r, new Robot(r));
                    break;
                case RobotType.AUTO:
                    log.info("Changing robot to Auto: " + r);
                    onChangeRobot.accept(r, new AutoRobot(r));
                    break;
                case RobotType.CONTROL:
                    log.info("Changing robot to Control: " + r);
                    onChangeRobot.accept(r, new ControlRobot(r));
                    break;
            }
        });
        return rtype;
    }

    private TextField speed() {
        return speed = makeNumField(
            0,
            Double.MAX_VALUE,
            (s, r) -> {
                log.info("Changing robot speed to '" + s + "'': " + r);
                r.speed(s);
            },
            Robot.class
        );
    }

    private TextField angle() {
        return angle = makeNumField(
            -360,
            360,
            (a, r) -> {
                log.info("Changing robot angle to '" + a + "'': " + r);
                r.angle(-a / 180 * Math.PI);
            },
            Robot.class
        );
    }

    private HBox crobot() {
        crobot = new HBox(5, new Label("r. speed: "), rspeed());
        crobot.setAlignment(Pos.CENTER_LEFT);
        return crobot;
    }

    private TextField rspeed() {
        return rspeed = makeNumField(
            0,
            Double.MAX_VALUE,
            (rs, r) -> {
                log.info(
                    "Changing robot rotation speed to '" + rs + "'': " + r
                );
                rs = rs / 180 * Math.PI;
                if (r instanceof ControlRobot cr) {
                    cr.rspeed(rs);
                } else if (r instanceof AutoRobot ar) {
                    ar.rspeed(rs);
                }
            },
            Robot.class
        );
    }

    private HBox arobot() {
        arobot = new HBox(
            5,
            new Label("d. dist:"),
            edist(),
            new Label("r. dist:"),
            rdist()
        );
        arobot.setAlignment(Pos.CENTER_LEFT);
        return arobot;
    }

    private TextField edist() {
        return edist = makeNumField(
            0,
            Double.MAX_VALUE,
            (ed, r) -> {
                log.info(
                    "Changing robot elide distance to '" + ed + "'': " + r
                );
                r.edist(ed);
            },
            AutoRobot.class
        );
    }

    private TextField rdist() {
        return rdist = makeNumField(
            -360,
            360,
            (rd, r) -> {
                log.info(
                    "Changing robot elide rotation to '" + rd + "'': " + r
                );
                r.erot(-rd / 180 * Math.PI);
            },
            AutoRobot.class
        );
    }

    private static TextFormatter<?> numberFormatter(double min, double max) {
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

    private int getRobotType() {
        if (obj instanceof AutoRobot) {
            return RobotType.AUTO;
        }
        if (obj instanceof ControlRobot) {
            return RobotType.CONTROL;
        }
        return RobotType.DUMMY;
    }

    private static double reduceDegrees(double angle) {
        var sign = angle > 0 ? 1 : -1;
        angle = Math.abs(angle);
        var rnum = (int)angle % 360;
        angle = sign * (rnum + (angle - (int)angle));
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }
        return angle;
    }

    private <T extends SimObj> TextField makeNumField(
        double min,
        double max,
        BiConsumer<Double, T> onSet,
        Class<T> clazz
    ) {
        var fld = new TextField();
        fld.setPrefWidth(60);
        fld.setTextFormatter(numberFormatter(min, max));
        setNumber(fld, onSet, clazz);
        return fld;
    }
}
