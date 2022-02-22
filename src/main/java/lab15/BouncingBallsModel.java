package lab15;

import javafx.scene.paint.Color;

import java.util.Random;

public class BouncingBallsModel {
    static int size = 50;
    static int radius = BouncingBallsModel.size / 2;
    int x;
    int y;
    double vx;
    double vy;
    Color color;

    static BouncingBallsModel generate(double maxPositionX, double maxPositionY) {
        Random rand = new Random();
        float r = (float) (rand.nextFloat() / 2f + 0.5);
        float g = (float) (rand.nextFloat() / 2f + 0.5);
        float b = (float) (rand.nextFloat() / 2f + 0.5);

        BouncingBallsModel ball = new BouncingBallsModel();
        ball.x = rand.nextInt((int) maxPositionX);
        ball.y = rand.nextInt((int) maxPositionY);
        ball.vx = rand.nextDouble(20) - 10;
        ball.vy = rand.nextDouble(20) - 10;
        ball.color = Color.color(r, g, b);

        return ball;
    }
}
