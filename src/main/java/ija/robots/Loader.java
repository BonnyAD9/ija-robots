package ija.robots;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import ija.robots.actors.AutoRobot;
import ija.robots.actors.ControlRobot;
import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Loader {
    private String filename;
    private char cur;

    public Loader(String filename) {
        this.filename = filename;
        cur = '\0';
    }

    public void load(Room room) {
        var obstacles = new ArrayList<Obstacle>();
        var robots = new ArrayList<Robot>();

        try {
            var scan = new Scanner(new File(filename));
            scan.useDelimiter("");

            var ident = read_ident(scan);
            while (ident != "") {
                switch (ident) {
                    case "room":
                        var size = read_size(scan);
                        break;
                    case "obstacle":
                        obstacles.add(load_obstacle(scan));
                        break;
                    case "robot":
                        robots.add(load_robot(scan));
                        break;
                    case "auto_robot":
                        robots.add(load_auto_robot(scan));
                        break;
                    case "control_robot":
                        robots.add(load_control_robot(scan));
                        break;
                    default:
                        throw new Exception("Unexpected identifier: " + ident);
                }
                ident = read_ident(scan);
            }

            scan.close();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.show();
            return;
        }

        System.out.println("Success");
    }

    Obstacle load_obstacle(Scanner scan) throws Exception {
        Vec2 pos = new Vec2(0, 0), size = new Vec2(0, 0);
        boolean spos = false, ssize = false;

        while (!spos || !ssize) {
            if (scan.hasNextDouble() && !ssize) {
                size = read_size(scan);
                ssize = true;
            } else if (read_next(scan) && cur == '[' && !spos) {
                pos = read_pos(scan);
                spos = true;
            } else {
                throw new Exception("Unexpected obstacle attribute");
            }
        }

        var rect = new Rect(pos, size);
        var obst = new Obstacle(rect);
        obst.hitbox(rect);
        return obst;
    }

    private Robot load_robot(Scanner scan) throws Exception {
        double speed = 0, angle = -90;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            if (!read_next(scan)) {
                if (spos)
                    break;
                throw new Exception("Robot requires position");
            }

            if (cur == '[') {
                pos = read_pos(scan);
                spos = true;
            } else if (cur == '{') {
                while (true) {
                    var ident = read_ident(scan);
                    if (ident == "speed") {
                        speed = read_double(scan, "Speed expects number");
                    } else if (ident == "angle") {
                        angle = read_double(scan, "Angle expects number");
                    } else {
                        throw new Exception(
                            "Unexpected controller robot attribute: "
                            + ident
                        );
                    }

                    if (!read_next(scan))
                        throw new Exception("Unexpected file end");
                    if (cur == '}')
                        break;
                    if (cur == ',')
                        continue;

                    throw new Exception("Unexpected character");
                }
            } else {
                // file.seekg((int)file.tellg() - 1);
                sopt = true;
            }
        }

        angle = -angle * Math.PI / 180.0;
        var rob = new Robot(pos, angle, speed);
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private AutoRobot load_auto_robot(Scanner scan) throws Exception {
        double speed = 0, angle = -90, el = 20, el_r = Math.PI / Math.E;
        double r = Math.PI / 4;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            if (!read_next(scan)) {
                if (spos)
                    break;
                throw new Exception("Robot requires position");
            }

            if (cur == '[') {
                pos = read_pos(scan);
                spos = true;
            } else if (cur == '{') {
                while (true) {
                    var ident = read_ident(scan);
                    if (ident == "speed") {
                        speed = read_double(scan, "Speed expects number");
                    } else if (ident == "angle") {
                        angle = read_double(scan, "Angle expects number");
                    } else if (ident == "rotation_speed") {
                        r = read_double(
                            scan,
                            "Rotation speed expects number"
                        );
                    } else if (ident == "elide_distance") {
                        el = read_double(scan, "Elide dist. expects number");
                    } else if (ident == "elide_rotation") {
                        el_r = read_double(scan, "Elide rot. expects number");
                    } else {
                        throw new Exception(
                            "Unexpected controller robot attribute: "
                            + ident
                        );
                    }

                    if (!read_next(scan))
                        throw new Exception("Unexpected file end");
                    if (cur == '}')
                        break;
                    if (cur == ',')
                        continue;

                    throw new Exception("Unexpected character");
                }
            } else {
                // file.seekg((int)file.tellg() - 1);
                sopt = true;
            }
        }

        angle = -angle * Math.PI / 180.0;
        var rob = new AutoRobot(pos, angle, speed, el, el_r, r);
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private ControlRobot load_control_robot(Scanner scan) throws Exception {
        double speed = 0, angle = -90, r = Math.PI / 4;
        Vec2 pos = new Vec2(0, 0);
        boolean spos = false, sopt = false;

        while (!spos || !sopt) {
            if (!read_next(scan)) {
                if (spos)
                    break;
                throw new Exception("Robot requires position");
            }

            if (cur == '[') {
                pos = read_pos(scan);
                spos = true;
            } else if (cur == '{') {
                while (true) {
                    var ident = read_ident(scan);
                    if (ident == "speed") {
                        speed = read_double(scan, "Speed expects number");
                    } else if (ident == "angle") {
                        angle = read_double(scan, "Angle expects number");
                    } else if (ident == "rotation_speed") {
                        r = read_double(
                            scan,
                            "Rotation speed expects number"
                        );
                    } else {
                        throw new Exception(
                            "Unexpected controller robot attribute: "
                            + ident
                        );
                    }

                    if (!read_next(scan))
                        throw new Exception("Unexpected file end");
                    if (cur == '}')
                        break;
                    if (cur == ',')
                        continue;

                    throw new Exception("Unexpected character");
                }
            } else {
                // file.seekg((int)file.tellg() - 1);
                sopt = true;
            }
        }

        angle = -angle * Math.PI / 180.0;
        var rob = new ControlRobot(pos, speed, angle, r);
        rob.hitbox(new Rect(pos.x(), pos.y(), 0, 0));
        return rob;
    }

    private String read_ident(Scanner scan) throws Exception {
        if (!read_next(scan) || !Character.isAlphabetic(cur))
            throw new Exception("Expected identifier");

        String res = "" + cur;
        while (scan.hasNext()) {
            cur = scan.next().charAt(0);
            if (!Character.isAlphabetic(cur) && cur != '_')
                break;
            res += cur;
        }

        if (cur == ':' || res == "")
            return res;

        if (!read_next(scan) || cur != ':')
            throw new Exception("Identifier must be followed by ':'");

        return res;
    }

    private Vec2 read_size(Scanner scan) throws Exception {
        double w, h;

        w = read_double(scan, "Size missing width");

        if (!read_next(scan) || cur != 'x') {
            System.out.println(w);
            System.out.println(cur);
            throw new Exception("Invalid character in size");
        }

        h = read_double(scan, "Size missing height");

        return new Vec2(w, h);
    }

    private Vec2 read_pos(Scanner scan) throws Exception {
        double x, y;

        x = read_double(scan, "Position missing x coordinate");

        if (!read_next(scan) || cur != ',')
            throw new Exception("Invalid character in position");

        y = read_double(scan, "Position missing y coordinate");

        if (!read_next(scan) || cur != ']')
            throw new Exception("Unclosed position");

        return new Vec2(x, y);
    }

    private boolean read_next(Scanner scan) throws Exception {
        if (!scan.hasNext())
            return false;

        cur = scan.next().charAt(0);
        if (!Character.isWhitespace(cur))
            return true;

        return read_next(scan);
    }

    private double read_double(Scanner scan, String msg) throws Exception {
        while (!scan.hasNextDouble()) {
            if (!scan.hasNext())
                throw new Exception(msg);

            cur = scan.next().charAt(0);
            if (!Character.isWhitespace(cur))
                throw new Exception(msg);
        }

        return scan.nextDouble();
    }
}
