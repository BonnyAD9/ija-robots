package ija.robots;

import ija.robots.actors.AutoRobot;
import ija.robots.actors.ControlRobot;
import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import ija.robots.load.Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
    private Room room;
    private HBox simMenu;
    private Menu menu;
    private ReditMenu reditMenu;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * The man entry point of the application.
     * @param args Command line arguments for the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Platform.runLater(() -> {
            final int WIDTH = 900;
            final int HEIGHT = 600;

            simMenu = simMenu(stage);
            reditMenu = new ReditMenu();
            room = new Room(new Rect(0, 0, WIDTH, viewHeight(HEIGHT)));

            room.add(new Obstacle(new Rect(100, 200, 60, 60)));
            room.add(new ControlRobot(new Vec2(200, 100)));
            room.add(new Robot(new Vec2(201, 200), 0, 0));
            room.add(new AutoRobot(new Vec2(300, 100)));

            menu = new Menu(room, new Rect(0, 0, WIDTH, viewHeight(HEIGHT)));
            var menuButton = new Button("menu");
            menuButton.setOnMouseClicked(e -> menu.setVisible(true));

            room.setOnSelect(e -> reditMenu.select(e));
            reditMenu.setOnRemove(e -> room.remove(e));
            reditMenu.setOnChangeRobot((o, n) -> room.changeRobot(o, n));
            stage.setOnCloseRequest(e -> room.run(false));

            // Robot[] robots = {
            //     new Robot(new Circle(3.5, -4.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(-2.5, 0.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(0.5, 3.5, 0.4), Vec2.unit(0)),
            // };

            var stack = new StackPane();
            StackPane.setMargin(menuButton, new Insets(5));
            stack.setAlignment(Pos.TOP_LEFT);
            stack
                .getChildren()
                .addAll(room.getGraphics(), menuButton, menu.getGraphics());

            var root = new FlowPane(
                Orientation.VERTICAL,
                reditMenu.getNode(),
                stack,
                simMenu
            );

            Scene scene = new Scene(root, WIDTH, HEIGHT);

            ChangeListener<Number> resizeListener =
                (observable, oldValue, newValue) -> {
                    System.out.println(newValue + " == " + stage.getWidth());
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

    //=======================================================================//
    //                               PRIVATE                                 //
    //=======================================================================//

    private HBox simMenu(Stage stage) {
        var path = new TextField();

        var save = new Button("save");
        save.setOnMouseClicked(e -> room.save(stage, path.getText()));

        var load = new Button("load");
        load.setOnMouseClicked(e -> {
            var loader = new Loader(path.getText());
            loader.load(stage, room);
        });

        var but = new Button("pause");
        but.setPrefWidth(60);
        but.setOnMouseClicked(e -> {
            var run = !room.isRunning();
            but.setText(run ? "pause" : "play");
            room.run(run);
        });

        var res = new HBox(5, path, save, load, but);
        HBox.setHgrow(path, Priority.ALWAYS);
        res.setAlignment(Pos.CENTER_RIGHT);
        res.setPadding(new Insets(5));
        res.setPrefHeight(40);
        return res;
    }

    private double viewHeight(double height) {
        return height - simMenu.getHeight() - reditMenu.getHeight();
    }
}
