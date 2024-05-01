package ija.robots;

import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
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
            room.add(new Robot(new Vec2(200, 100), 20, 0));

            stage.setOnCloseRequest(e -> room.run(false));

            // Robot[] robots = {
            //     new Robot(new Circle(3.5, -4.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(-2.5, 0.5, 0.4), Vec2.unit(0)),
            //     new Robot(new Circle(0.5, 3.5, 0.4), Vec2.unit(0)),
            // };

            Scene scene = new Scene(room.getGraphics(), 800, 600);
            scene.setFill(Color.web("#222222"));

            stage.setScene(scene);
            stage.setTitle("IJA robots");
            stage.show();
        });
    }
}
