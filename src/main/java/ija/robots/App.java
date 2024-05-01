package ija.robots;

import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Platform.runLater(() -> {
            Room room = new Room(new Rect(0, 0, 800, 600));
            room.add(new Obstacle(new Rect(100, 200, 60, 60)));
            room.add(new Robot(new Vec2(200, 100), 20, Math.PI / 2));
            room.add(new Robot(new Vec2(201, 200), 0, 0));

            stage.setOnCloseRequest(e -> room.run(false));

            // Robot[] robots = {
            //     new Robot(new Circle(3.5, -4.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(-2.5, 0.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(0.5, 3.5, 0.4), Vec2.unit(0)),
            // };

            var root = new FlowPane(Orientation.VERTICAL, room.getGraphics());

            Scene scene = new Scene(root, 800, 600);

            ChangeListener<Number> resizeListener =
                (observable, oldValue, newValue) -> {
                    room.resize(new Rect(0, 0, scene.getWidth(), scene.getHeight()));
                };
            stage.widthProperty().addListener(resizeListener);
            stage.heightProperty().addListener(resizeListener);
            //scene.setFill(Color.web("#222222"));

            stage.setScene(scene);
            stage.setTitle("IJA robots");
            stage.show();
        });
    }
}
