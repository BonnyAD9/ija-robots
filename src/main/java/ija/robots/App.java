package ija.robots;

import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
    private Room room;
    private FlowPane simMenu;
    private Menu menu;
    private ReditMenu reditMenu;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Platform.runLater(() -> {
            simMenu = simMenu();
            reditMenu = new ReditMenu();
            room = new Room(new Rect(0, 0, 800, viewHeight(600)));

            room.add(new Obstacle(new Rect(100, 200, 60, 60)));
            room.add(new Robot(new Vec2(200, 100), 20, Math.PI / 2));
            room.add(new Robot(new Vec2(201, 200), 0, 0));

            menu = new Menu(room, new Rect(0, 0, 800, 600 - 40));
            var menuButton = new Button("menu");
            menuButton.setOnMouseClicked(e -> menu.setVisible(true));

            room.setOnSelect(e -> reditMenu.select(e));
            reditMenu.setOnRemove(e -> room.remove(e));
            stage.setOnCloseRequest(e -> room.run(false));

            // Robot[] robots = {
            //     new Robot(new Circle(3.5, -4.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(-2.5, 0.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(0.5, 3.5, 0.4), Vec2.unit(0)),
            // };

            var root = new FlowPane(Orientation.VERTICAL, reditMenu.getNode(), room.getGraphics(), simMenu);

            var stack = new StackPane();
            StackPane.setMargin(menuButton, new Insets(5));
            StackPane.setMargin(
                menu.getGraphics(),
                new Insets(0, 0, 40, 0)
            );
            stack.setAlignment(Pos.TOP_LEFT);
            stack.getChildren().addAll(root, menuButton, menu.getGraphics());

            Scene scene = new Scene(stack, 800, 600);

            ChangeListener<Number> resizeListener =
                (observable, oldValue, newValue) -> {
                    var rsize = new Vec2(
                        scene.getWidth(),
                        viewHeight(scene.getHeight())
                    );
                    room.resize(new Rect(0, 0, rsize.width(), rsize.height()));
                    menu.resize(new Vec2(rsize.width(), rsize.height()));
                };
            stage.widthProperty().addListener(resizeListener);
            stage.heightProperty().addListener(resizeListener);

            stage.setScene(scene);
            stage.setTitle("IJA robots");
            stage.show();
        });
    }

    private FlowPane simMenu() {
        var but = new Button("pause");
        but.setPrefWidth(60);
        but.setOnMouseClicked(e -> {
            var run = !room.isRunning();
            but.setText(run ? "pause" : "play");
            room.run(run);
        });
        var res = new FlowPane(5, 5, but);
        res.setPadding(new Insets(5));
        res.setAlignment(Pos.CENTER_RIGHT);
        res.setPrefHeight(40);
        return res;
    }

    private double viewHeight(double height) {
        return height - simMenu.getHeight() - reditMenu.getHeight();
    }
}
