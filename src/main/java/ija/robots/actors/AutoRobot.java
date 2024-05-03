package ija.robots.actors;

import ija.robots.common.Vec2;
import javafx.scene.paint.Color;

/**
 * Robot with basic AI.
 */
public class AutoRobot extends Robot {
    private double edist;
    private double erot;
    private double rspeed;

    private double sspeed;
    private double rotRem = 0;

    /**
     * Creates new robot with basic AI.
     * @param topLeft Top left corner of the robot.
     * @param speed Speed of the robot. (pixels per second)
     * @param angle Angle the robot is facing. (radians)
     * @param edist Elide distance. (pixels)
     * @param erot Elide rotataion. (radians)
     * @param rspeed Rotation speed. (radians per second)
     */
    public AutoRobot(
        Vec2 topLeft,
        double speed,
        double angle,
        double edist,
        double erot,
        double rspeed
    ) {
        super(topLeft, speed, angle);
        this.edist = edist;
        this.erot = erot;
        this.rspeed = rspeed;
        sspeed = speed;

        var shape = getShape();
        shape.setFill(Color.web("#5555cc"));
    }

    /**
     * Creates new robot with basic AI.
     * @param topLeft Position of the top left corner of the robot.
     */
    public AutoRobot(Vec2 topLeft) {
        this(topLeft, 20, Math.PI / 2, 20, Math.PI / Math.E, Math.PI / 4);
    }

    /**
     * Creates new robot with simple AI and takes the parameters from other
     * robtot;
     * @param r Robot to take the parameters from.
     */
    public AutoRobot(Robot r) {
        super(r);
        edist = 20;
        erot = Math.PI / Math.E;
        rspeed = Math.PI / 4;
        sspeed = r.speed();
        var shape = getShape();
        shape.setFill(Color.web("#5555cc"));

        if (r instanceof AutoRobot ar) {
            edist = ar.edist();
            erot = ar.erot();
            rspeed = ar.rspeed();
        } else if (r instanceof ControlRobot cr) {
            rspeed = cr.rspeed();
        }
    }

    @Override
    public double speed() {
        if (rotRem != 0) {
            return sspeed;
        }
        return super.speed();
    }

    @Override
    public double speed(double speed) {
        if (rotRem != 0) {
            return sspeed = speed;
        }
        return super.speed(speed);
    }

    /**
     * Gets the elide distance.
     * @return The elide distance. (pixels)
     */
    public double edist() {
        return edist;
    }

    /**
     * Sets the elide distance.
     * @param edist New elide distance (pixels).
     * @return The new elide distance.
     */
    public double edist(double edist) {
        return this.edist = edist;
    }

    /**
     * Gets the elide rotation.
     * @return Elide rotation. (radians)
     */
    public double erot() {
        return erot;
    }

    /**
     * Sets the elide rotation.
     * @param erot New elide rotation. (radians)
     * @return The new elide rotation.
     */
    public double erot(double erot) {
        return this.erot = erot;
    }

    /**
     * Gets the rotation speed.
     * @return Rotation speed. (radians per second)
     */
    public double rspeed() {
        return rspeed;
    }

    /**
     * Sets the rotation speed.
     * @param rspeed New rotation speed (radians per second)
     * @return The new rotation speed.
     */
    public double rspeed(double rspeed) {
        return this.rspeed = rspeed;
    }

    @Override
    public void move(double delta, double distance) {
        if (rotRem == 0 && distance <= edist) {
            rotRem = erot;
            sspeed = super.speed();
            super.speed(0);
        }

        if (rotRem != 0) {
            var ang = rspeed * delta;
            ang = rotRem < 0 ? - ang : ang;
            if (Math.abs(rotRem) < Math.abs(ang)) {
                ang = rotRem;
                rotRem = 0;
                super.speed(sspeed);
            } else {
                rotRem -= ang;
            }

            angle(angle() + ang);
        }

        super.move(delta, distance);
    }

    @Override
    public String toString() {
        return String.format(
            "auto_robot: [%f, %f] { speed: %f, angle: %f, rotation_speed: " +
            "%f, elide_dist: %f, elide_rot: %f }",
            hitbox().x(),
            hitbox().y(),
            sspeed,
            -angle() / Math.PI * 180,
            rspeed / Math.PI * 180,
            edist,
            erot / Math.PI * 180
        );
    }
}
