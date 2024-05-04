package ija.robots.load;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import ija.robots.actors.AutoRobot;
import ija.robots.actors.ControlRobot;
import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Class for loading saved room from file
 */
public class Loader {
    private String filename;
    private Token cur;
    private Logger log = Logger.getLogger("Loader");

    /**
     * Creates new loader
     * @param filename file to read from
     */
    public Loader(String filename) {
        this.filename = filename;
    }

    /**
     * Loads room from the file
     * @param stage window
     * @param room room to load obstacles and robots into
     */
    public void load(Stage stage, Room room) {
        var obstacles = new ArrayList<Obstacle>();
        var robots = new ArrayList<Robot>();

        try {
            var scan = new Scanner(new File(filename));
            var lexer = new LoaderLex(scan);

            cur = lexer.next();
            while (cur != Token.Eof) {
                if (cur != Token.Ident)
                    throw new Exception("Unexpected token in file");

                switch (lexer.getString()) {
                    case "room":
                        cur = lexer.next();
                        if (cur != Token.Number)
                            throw new Exception("Room expects size");
                        var size = readSize(lexer);
                        stage.setWidth(size.width());
                        stage.setHeight(size.height() + 80);

                        cur = lexer.next();
                        break;
                    case "obstacle":
                        obstacles.add(loadObstacle(lexer));
                        break;
                    case "robot":
                        robots.add(loadRobot(lexer));
                        break;
                    case "auto_robot":
                        robots.add(load_auto_robot(lexer));
                        break;
                    case "control_robot":
                        robots.add(load_control_robot(lexer));
                        break;
                    default:
                        throw new Exception(String.format(
                            "Unexpected identifier '%s'",
                            lexer.getString()
                        ));
                }
            }
            scan.close();
        } catch (Exception e) {
            log.severe("Failed to load room: " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.show();
            return;
        }

        room.clear(obstacles, robots);
    }

    private Obstacle loadObstacle(LoaderLex lexer) throws Exception {
        Vec2 pos = new Vec2(0, 0), size = new Vec2(0, 0);
        boolean spos = false, ssize = false;

        while (!spos || !ssize) {
            cur = lexer.next();
            if (cur == Token.Number && !ssize) {
                size = readSize(lexer);
                ssize = true;
            } else if (cur == Token.PosStart && !spos) {
                pos = readPos(lexer);
                spos = true;
            } else {
                throw new Exception("Unexpected obstacle attribute");
            }
        }
        cur = lexer.next();

        var rect = new Rect(pos, size);
        var obst = new Obstacle(rect);
        obst.hitbox(rect);
        return obst;
    }

    private Robot loadRobot(LoaderLex lexer) throws Exception {
        double speed = 0, angle = -90;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            cur = lexer.next();
            if (cur == Token.PosStart && !spos) {
                pos = readPos(lexer);
                spos = true;
            } else if (cur == Token.OptStart && !sopt) {
                cur = lexer.next();
                while (cur != Token.OptEnd) {
                    if (cur != Token.Ident)
                        throw new Exception("Identifier expected");

                    var ident = lexer.getString();
                    cur = lexer.next();
                    switch (ident) {
                        case "speed":
                            speed = readNum(lexer, "Speed expects number");
                            break;
                        case "angle":
                            angle = readNum(lexer, "Angle expects number");
                            break;
                        default:
                            throw new Exception("Unexpected robot attribute");
                    }

                    cur = lexer.next();
                    if (cur == Token.Colon) {
                        cur = lexer.next();
                    } else if (cur != Token.OptEnd) {
                        throw new Exception("Missing attribute separator");
                    }
                }
            } else {
                sopt = true;
            }
        }

        var rob = new Robot(pos, speed, -angle * Math.PI / 180);
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private AutoRobot load_auto_robot(LoaderLex lexer) throws Exception {
        double speed = 0, angle = -90, el = 20, el_r = Math.PI / Math.E;
        double r = Math.PI / 4;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            cur = lexer.next();
            if (cur == Token.PosStart && !spos) {
                pos = readPos(lexer);
                spos = true;
            } else if (cur == Token.OptStart && !sopt) {
                cur = lexer.next();
                while (cur != Token.OptEnd) {
                    if (cur != Token.Ident)
                        throw new Exception("Identifier expected");

                    var ident = lexer.getString();
                    cur = lexer.next();
                    switch (ident) {
                        case "speed":
                            speed = readNum(lexer, "Speed expects number");
                            break;
                        case "angle":
                            angle = readNum(lexer, "Angle expects number");
                            break;
                        case "rotation_speed":
                            r = readNum(lexer, "Rot. speed expects number");
                            break;
                        case "elide_distance":
                            el = readNum(lexer, "Elide dist. expects number");
                            break;
                        case "elide_rotation":
                            el_r = readNum(lexer, "Elide rot. expects number");
                            break;
                        default:
                            throw new Exception(
                                "Unexpected auto_robot attribute"
                            );
                    }

                    cur = lexer.next();
                    if (cur == Token.Colon) {
                        cur = lexer.next();
                    } else if (cur != Token.OptEnd) {
                        throw new Exception("Missing attribute separator");
                    }
                }
            } else {
                sopt = true;
            }
        }

        var rad = Math.PI / 180;
        var rob = new AutoRobot(
            pos, speed, -angle * rad, el, el_r * rad, r * rad
        );
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private ControlRobot load_control_robot(LoaderLex lexer) throws Exception {
        double speed = 0, angle = -90, r = Math.PI / 4;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            cur = lexer.next();
            if (cur == Token.PosStart && !spos) {
                pos = readPos(lexer);
                spos = true;
            } else if (cur == Token.OptStart && !sopt) {
                cur = lexer.next();
                while (cur != Token.OptEnd) {
                    if (cur != Token.Ident)
                        throw new Exception("Identifier expected");

                    var ident = lexer.getString();
                    cur = lexer.next();
                    switch (ident) {
                        case "speed":
                            speed = readNum(lexer, "Speed expects number");
                            break;
                        case "angle":
                            angle = readNum(lexer, "Angle expects number");
                            break;
                        case "rotation_speed":
                            r = readNum(lexer, "Rot. speed expects number");
                            break;
                        default:
                            throw new Exception(
                                "Unexpected control_robot attribute"
                            );
                    }

                    cur = lexer.next();
                    if (cur == Token.Colon) {
                        cur = lexer.next();
                    } else if (cur != Token.OptEnd) {
                        throw new Exception("Missing attribute separator");
                    }
                }
            } else {
                sopt = true;
            }
        }

        var rad = Math.PI / 180;
        var rob = new ControlRobot(pos, speed, -angle * rad, r * rad);
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private Vec2 readSize(LoaderLex lexer) throws Exception {
        double x = lexer.getNum();

        cur = lexer.next();
        if (cur != Token.X)
            throw new Exception("Size missing separator");

        cur = lexer.next();
        double y = readNum(lexer, "Size missing height");

        return new Vec2(x, y);
    }

    private Vec2 readPos(LoaderLex lexer) throws Exception {
        cur = lexer.next();
        double x = readNum(lexer, "Position missing x coordinate");

        cur = lexer.next();
        if (cur != Token.Colon)
            throw new Exception("Position missing coordinates separator");

        cur = lexer.next();
        double y = readNum(lexer, "Position missing y coordinate");

        cur = lexer.next();
        if (cur != Token.PosEnd)
            throw new Exception("Unclosed position");

        return new Vec2(x, y);
    }

    private double readNum(LoaderLex lexer, String msg) throws Exception {
        if (cur != Token.Number)
            throw new Exception(msg);

        return lexer.getNum();
    }
}
