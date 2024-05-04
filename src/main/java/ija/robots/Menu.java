/**
 * @file
 * @authors Martin Slezák (xsleza26), Jakub Antonín Štigler (xstigl00)
 * @brief Menu for adding robots to the room.
 */

package ija.robots;

import java.util.logging.Logger;

import ija.robots.actors.AutoRobot;
import ija.robots.actors.Obstacle;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Button representing obstacle in the menu
 */
class ObstacleButton {
    Menu menu;
    Room room;
    Rectangle shape;
    Vec2 pos, lastPos;

    private Logger log = Logger.getLogger("Menu");

    /**
     * Creates new menu obstacle button
     * @param room room to add obstacle into
     * @param pos position of the button
     */
    public ObstacleButton(Menu menu, Room room, Vec2 pos) {
        this.menu = menu;
        this.room = room;
        this.pos = pos;
        lastPos = new Vec2(0, 0);

        createShape();
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseReleased(e -> mouseRelease(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
        shape.setOnMouseEntered(e -> shape.setCursor(Cursor.OPEN_HAND));
        shape.setOnMouseExited(e -> shape.setCursor(Cursor.DEFAULT));
    }

    /**
     * Creates new menu obstacle ghost (stays in place all the time)
     * @param pos position of the ghost
     */
    public ObstacleButton(Vec2 pos) {
        this.pos = pos;
        lastPos = new Vec2(0, 0);
        createShape();
    }

    /**
     * Gets menu obstacle button shape
     * @return shape of the button
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * Sets position of the obstacle button
     * @param pos new position
     */
    public void setPos(Vec2 pos) {
        this.pos = pos;
        shape.setX(pos.x());
        shape.setY(pos.y());
    }

    private void createShape() {
        shape = new Rectangle(pos.x(), pos.y(), 60, 60);
        shape.setFill(Color.web("#ff5555"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(6);
    }

    private void mousePress(MouseEvent event) {
        shape.setStroke(Color.web("#ffff55"));
        shape.setCursor(Cursor.CLOSED_HAND);
        lastPos = new Vec2(event.getX(), event.getY());
    }

    private void mouseRelease(MouseEvent event) {
        shape.setStroke(Color.WHITE);
        log.info("Adding new obstacle.");
        var obs = new Obstacle(new Rect(shape.getX(), shape.getY(), 60, 60));
        room.add(obs);
        obs.setSelected(true);
        menu.setVisible(false);
        shape.setX(pos.x());
        shape.setY(pos.y());
        shape.setCursor(Cursor.DEFAULT);
    }

    private void mouseDrag(MouseEvent event) {
        var newPos = new Vec2(event.getX(), event.getY());
        var delta = newPos.sub(lastPos);
        lastPos = newPos;

        shape.setX(shape.getX() + delta.x());
        shape.setY(shape.getY() + delta.y());
    }
}

/**
 * Class representing menu robot button
 */
class RobotButton {
    Menu menu;
    Room room;
    Circle shape;
    Circle eye;
    Vec2 pos, lastPos;

    private Logger log = Logger.getLogger("Menu");

    /**
     * Creates new menu robot button
     * @param room room to add robot into
     * @param pos position of the button
     */
    public RobotButton(Menu menu, Room room, Vec2 pos) {
        this.menu = menu;
        this.room = room;
        this.pos = new Vec2(pos.x() + 25, pos.y() + 25);
        lastPos = new Vec2(0, 0);

        createShape();
        shape.setOnMousePressed(e -> mousePress(e));
        shape.setOnMouseReleased(e -> mouseRelease(e));
        shape.setOnMouseDragged(e -> mouseDrag(e));
        shape.setOnMouseEntered(e -> shape.setCursor(Cursor.OPEN_HAND));
        shape.setOnMouseExited(e -> shape.setCursor(Cursor.DEFAULT));

        createEye();
        eyePos();
        eye.setOnMousePressed(e -> mousePress(e));
        eye.setOnMouseReleased(e -> mouseRelease(e));
        eye.setOnMouseDragged(e -> mouseDrag(e));
        eye.setOnMouseEntered(e -> eye.setCursor(Cursor.OPEN_HAND));
        eye.setOnMouseExited(e -> eye.setCursor(Cursor.DEFAULT));
    }

    /**
     * Creates new menu robot ghost (stays in place all the time)
     * @param pos position of the ghost
     */
    public RobotButton(Vec2 pos) {
        this.pos = new Vec2(pos.x() + 25, pos.y() + 25);
        lastPos = new Vec2(0, 0);
        createShape();
        createEye();
        eyePos();
    }

    /**
     * Gets menu robot button shape
     * @return shape of the button
     */
    public Circle getShape() {
        return shape;
    }

    /**
     * Gets menu robot eye
     * @return eye of the robot button
     */
    public Circle getEye() {
        return eye;
    }

    /**
     * Sets position of the robot button
     * @param pos new position
     */
    public void setPos(Vec2 pos) {
        this.pos = new Vec2(pos.x() + 25, pos.y() + 25);
        shape.setCenterX(this.pos.x());
        shape.setCenterY(this.pos.y());
        eyePos();
    }

    private void createShape() {
        shape = new Circle(this.pos.x(), this.pos.y(), 25);
        shape.setFill(Color.web("#5555cc"));
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(6);
    }

    private void createEye() {
        eye = new Circle(3);
        eye.setFill(Color.WHITE);
        eye.setStrokeWidth(0);
    }

    private void mousePress(MouseEvent event) {
        shape.setStroke(Color.web("#ffff55"));
        shape.setCursor(Cursor.CLOSED_HAND);
        eye.setCursor(Cursor.CLOSED_HAND);
        lastPos = new Vec2(event.getX(), event.getY());
    }

    private void mouseRelease(MouseEvent event) {
        log.info("Adding new robot.");
        shape.setStroke(Color.WHITE);
        var loc = new Vec2(shape.getCenterX() - 25, shape.getCenterY() - 25);
        var rob = new AutoRobot(
            loc, 20, Math.PI / 2, 20, Math.PI / Math.E, Math.PI / 4
        );
        room.add(rob);
        rob.setSelected(true);
        menu.setVisible(false);
        shape.setCenterX(pos.x());
        shape.setCenterY(pos.y());
        shape.setCursor(Cursor.DEFAULT);
        eye.setCursor(Cursor.DEFAULT);
        eyePos();
    }

    private void mouseDrag(MouseEvent event) {
        var newPos = new Vec2(event.getX(), event.getY());
        var delta = newPos.sub(lastPos);

        var cpos = new Vec2(
            shape.getCenterX() + delta.x(),
            shape.getCenterY() + delta.y()
        );
        shape.setCenterX(cpos.x());
        shape.setCenterY(cpos.y());
        eyePos();
        lastPos = newPos;
    }

    private void eyePos() {
        eye.setCenterX(shape.getCenterX());
        eye.setCenterY(shape.getCenterY() + 15);
    }
}

public class Menu {
    StackPane menu;
    Pane pane;
    ObstacleButton obstBtn;
    ObstacleButton obstGhost;
    RobotButton robBtn;
    RobotButton robGhost;

    public Menu(Room room, Rect rect) {
        var btn = new Button("close");
        btn.setOnMouseClicked(e -> menu.setVisible(false));

        var bg = new Region();
        bg.setMaxWidth(100);
        bg.setStyle("-fx-background-color: #404040");

        pane = new Pane();
        var paneMid = (rect.height() - 40) / 2;

        obstGhost = new ObstacleButton(new Vec2(20, paneMid - 30));
        obstBtn = new ObstacleButton(this, room, new Vec2(20, paneMid - 30));
        robGhost = new RobotButton(new Vec2(25, paneMid + 50));
        robBtn = new RobotButton(this, room, new Vec2(25, paneMid + 50));
        pane.getChildren().addAll(
            obstGhost.getShape(),
            obstBtn.getShape(),
            robGhost.getShape(),
            robGhost.getEye(),
            robBtn.getShape(),
            robBtn.getEye()
        );
        pane.setMaxHeight(rect.height());

        menu = new StackPane(bg, pane, btn);
        StackPane.setAlignment(bg, Pos.CENTER_LEFT);
        StackPane.setMargin(btn, new Insets(5));
        menu.setOnMousePressed(e -> {
            if (e.getX() > 100)
                setVisible(false);
        });
        menu.setAlignment(Pos.TOP_LEFT);
        menu.setVisible(false);
    }

    public StackPane getGraphics() {
        return menu;
    }

    public void resize(Vec2 bounds) {
        var paneMid = (bounds.height() - 40) / 2;
        obstBtn.setPos(new Vec2(20, paneMid - 30));
        obstGhost.setPos(new Vec2(20, paneMid - 30));
        robBtn.setPos(new Vec2(25, paneMid + 50));
        robGhost.setPos(new Vec2(25, paneMid + 50));
        pane.setMaxHeight(bounds.height());
    }

    public void setVisible(boolean vis) {
        menu.setVisible(vis);
    }
}
