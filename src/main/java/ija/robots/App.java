package ija.robots;

import java.util.Random;
import java.util.Scanner;

import ija.robots.actors.Obstacle;
import ija.robots.actors.Robot;
import ija.robots.actors.Room;
import ija.robots.common.Circle;
import ija.robots.common.Rect;
import ija.robots.common.Vec2;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        //    -6-5-4-3-2-10 1 2 3 4 5
        // -6 ########################
        // -5 ##                RR  ##
        // -4 ##                    ##
        // -3 ##      OOOOOOOO      ##
        // -2 ##            OO      ##
        // -1 ##            OO      ##
        //  0 ##    RR      OO      ##
        //  1 ##            OO      ##
        //  2 ##                    ##
        //  3 ##          RR        ##
        //  4 ##                    ##
        //  5 ########################

        Room room = new Room(new Rect(-5, -5, 10, 10));
        System.out.println(room.add(new Obstacle(new Rect(-2, -3, 4, 1))));
        System.out.println(room.add(new Obstacle(new Rect(1, -2, 1, 4))));

        Robot[] robots = {
            new Robot(new Circle(3.5, -4.5, 0.4), Vec2.unit(0)),
            new Robot(new Circle(-2.5, 0.5, 0.4), Vec2.unit(0)),
            new Robot(new Circle(0.5, 3.5, 0.4), Vec2.unit(0)),
        };

        for (int i = 0; i < robots.length; ++i) {
            robots[i].id = i;
            System.out.println(room.add(robots[i]));
        }

        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        while (true) {
            for (Robot r : robots) {
                r.rotate(Math.PI * rand.nextInt(-1, 2) / 2);
                System.out.println(r);
            }

            System.out.print(room);
            System.out.print("Press q to exit: ");

            if (sc.nextLine().trim().equals("q")) {
                break;
            }
            
            System.out.println();

            room.tick(1);
        }

        sc.close();
    }
}
