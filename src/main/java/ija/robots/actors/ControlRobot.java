package ija.robots.actors;

import ija.robots.common.Vec2;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ControlRobot extends Robot {
    private double sspeed = 20;
    private double rotSpeed = Math.PI / 4;
    private double curSpeed = 0;
    private double curRotSpeed = 0;

    //=======================================================================//
    //                                PUBLIC                                 //
    //=======================================================================//

    /**
     * Creates new controlles robot
     * @param topLeft position of the robot
     */
    public ControlRobot(Vec2 topLeft) {
        super(topLeft, 20, Math.PI / 2);
    }

    /**
     * Creates new controlled robot
     * @param topLeft position of the robot
     * @param speed speed of the robot
     * @param angle orientation of the robot
     */
    public ControlRobot(Vec2 topLeft, double speed, double angle, double rot) {
        super(topLeft, speed, angle);
        sspeed = speed;
        rotSpeed = rot;
    }

    /**
     * Creates a new controlled robot and takes the parameters from existing
     * robot.
     * @param r Robot to take the parameters from.
     */
    public ControlRobot(Robot r) {
        super(r);
        rotSpeed = Math.PI / 4;
        sspeed = r.speed();
        if (r instanceof AutoRobot ar) {
            rotSpeed = ar.rspeed();
        } else if (r instanceof ControlRobot cr) {
            rotSpeed = cr.rspeed();
        }
    }

    /**
     * Creates a new controlled robot and takes the parameters from existing
     * robot.
     * @param r Robot to take the parameters from.
     */
    public ControlRobot(Robot r) {
        super(r);
        rotSpeed = Math.PI / 4;
        sspeed = r.speed();
        if (r instanceof AutoRobot ar) {
            rotSpeed = ar.rspeed();
        } else if (r instanceof ControlRobot cr) {
            rotSpeed = cr.rspeed();
        }
    }

    @Override
    public void move(double delta, double distance) {
        if (distance == 0) {
            sspeed = super.speed();
            super.speed(0);
        } else {
            super.speed(curSpeed);
        }

        if (curRotSpeed != 0) {
            var ang = curRotSpeed * delta;
            angle(super.angle() + ang);
        }

        super.move(delta, distance);
    }

    @Override
    public Circle getShape() {
        var shape = super.getShape();
        shape.setFill(Color.web("#55cc55"));
        return shape;
    }

    @Override
    public double speed() {
        if (curSpeed == 0)
            return sspeed;
        return super.speed();
    }

    @Override
    public double speed(double speed) {
        if (curSpeed == 0)
            return sspeed = speed;
        return super.speed(speed);
    }

    /**
     * Gets rotation speed of the robot
     * @return rotation speed
     */
    public double rspeed() {
        return rotSpeed;
    }

    /**
     * Sets rotation speed of the robot
     * @param speed new rotation speed
     */
    public void rspeed(double speed) {
        rotSpeed = speed;
    }

    /**
     * Handles forward movement
     * @param start true when should start moving, else false
     */
    public void forward(boolean start) {
        curSpeed = start ? sspeed : 0;
    }

    /**
     * Handles left movement
     * @param start true when should start moving, else false
     */
    public void left(boolean start) {
        if (start) {
            curRotSpeed = Math.max(curRotSpeed - rotSpeed, -rotSpeed);
        } else {
            curRotSpeed = Math.min(curRotSpeed + rotSpeed, 0.);
        }
    }

    /**
     * Handles right movement
     * @param start true when should start moving, else false
     */
    public void right(boolean start) {
        if (start) {
            curRotSpeed = Math.min(rotSpeed + curRotSpeed, rotSpeed);
        } else {
            curRotSpeed = Math.max(curRotSpeed - rotSpeed, 0.);
        }
    }

    @Override
    public String toString() {
        return String.format(
            "control_robot: [%f, %f] { speed: %f, angle: %f, " +
            "rotation_speed: %f }",
            hitbox().x(),
            hitbox().y(),
            sspeed,
            angle(),
            rotSpeed
        );
    }
}
