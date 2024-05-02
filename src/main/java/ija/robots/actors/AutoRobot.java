package ija.robots.actors;

import ija.robots.common.Vec2;
import javafx.scene.paint.Color;

public class AutoRobot extends Robot {
    private double edist;
    private double erot;
    private double rspeed;

    private double sspeed;
    private double rotRem = 0;

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

    public AutoRobot(Vec2 topLeft) {
        this(topLeft, 20, Math.PI / 2, 20, Math.PI / Math.E, Math.PI / 2);
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

    public double edist() {
        return edist;
    }

    public double edist(double edist) {
        return this.edist = edist;
    }

    public double erot() {
        return erot;
    }

    public double erot(double erot) {
        return this.erot = erot;
    }

    public double rspeed() {
        return rspeed;
    }

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
            ang -= rotRem < 0 ? - ang : ang;
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
}
