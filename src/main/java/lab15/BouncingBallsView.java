package lab15;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BouncingBallsView extends Application {
    Group group = new Group();
    Pane root = new Pane();

    List<BouncingBallsModel> balls = new ArrayList<>();

    Button startBtn = new Button("Start");
    Button stopBtn = new Button("Stop");
    Button addBtn = new Button("+");
    Button removeBtn = new Button("-");

    public static void main(String[] args) {
        launch(args);
    }

    void updateView() {
        // usuń wszystkie węzły potomne grupy
        group.getChildren().clear();
        // wygeneruj nowe węzły na podstawie modelu
        for (BouncingBallsModel ball : balls) {
            Circle circle = new Circle(ball.x, ball.y, BouncingBallsModel.radius, ball.color);
            circle.setStroke(Color.BLACK);
            group.getChildren().add(circle);
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        root.getChildren().add(group);

        stopBtn.setDisable(true);
        HBox hBox = new HBox(startBtn, stopBtn, addBtn, removeBtn);
        hBox.setSpacing(10);
        hBox.setPadding(new javafx.geometry.Insets(10));
        root.getChildren().add(hBox);

        final Scene scene = new Scene(root, 800, 600, Color.BLACK);
        root.setOpacity(1);
        updateView();
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            final MovingAverage mave = new MovingAverage(300);
            long lastPaint = -1;
            double fps = 0;
            int counter = 0;

            void updateFps() {
                double diff = mave.getMean();
                diff /= 1e9;
                if (diff > 0) fps = 1 / diff;
            }

            void addFPS() {
                Text text = new Text();
                text.setFont(new Font(40));
                text.setFill(Color.WHITE);
                text.setText(String.format(Locale.US, "%.2f FPS", fps));
                text.setX(root.getWidth() - text.getLayoutBounds().getWidth() - 10);
                text.setY(50);
                group.getChildren().add(text);
            }

            @Override
            public void handle(long now) {
                if (lastPaint > 0) mave.add(now - lastPaint);
                lastPaint = now;
                counter++;
                updateView();
                if (counter % 50 == 0) updateFps();
                addFPS();

                // przesuń kulki
                for (BouncingBallsModel b : balls) {
                    b.x += b.vx;
                    b.y += b.vy;
                }

                // wykonaj odbicia od ściany
                for (BouncingBallsModel b : balls) {
                    if ((b.x + BouncingBallsModel.radius > root.getWidth() && b.vx > 0) ||
                            (b.x - BouncingBallsModel.radius < 0 && b.vx < 0)) {
                        b.vx *= -1;
                    }
                    if ((b.y + BouncingBallsModel.radius > root.getHeight() && b.vy > 0) ||
                            (b.y - BouncingBallsModel.radius < 0 && b.vy < 0)) {
                        b.vy *= -1;
                    }
                }

                // wykonaj odbicia od kulek
                for (int i = 0; i < balls.size(); i++) {
                    for (int j = i + 1; j < balls.size(); j++) {
                        BouncingBallsModel b1 = balls.get(i);
                        BouncingBallsModel b2 = balls.get(j);
                        double dx = b1.x - b2.x;
                        double dy = b1.y - b2.y;

                        if (Math.sqrt(dx * dx + dy * dy) < BouncingBallsModel.size) {
                            Vector x1 = new Vector(b1.x, b1.y);
                            Vector x2 = new Vector(b2.x, b2.y);
                            Vector v1 = new Vector(b1.vx, b1.vy);
                            Vector v2 = new Vector(b2.vx, b2.vy);
                            Vector vv1 = v1.subtract(x1.subtract(x2).multiply(v1.subtract(v2).dot(x1.subtract(x2)) /
                                    (x1.subtract(x2).length() * x1.subtract(x2).length())));
                            Vector vv2 = v2.subtract(x2.subtract(x1).multiply(v2.subtract(v1).dot(x2.subtract(x1)) /
                                    (x2.subtract(x1).length() * x2.subtract(x1).length())));
                            b1.vx = vv1.x;
                            b1.vy = vv1.y;
                            b2.vx = vv2.x;
                            b2.vy = vv2.y;
                        }
                    }
                }
            }
        };

        animationTimer.stop();

        startBtn.setOnAction(event -> {
            System.out.println("Start");
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            animationTimer.start();
        });

        stopBtn.setOnAction(event -> {
            System.out.println("Stop");
            startBtn.setDisable(false);
            stopBtn.setDisable(true);
            animationTimer.stop();
        });

        addBtn.setOnAction(event -> {
            System.out.println("Add");
            BouncingBallsModel ball = BouncingBallsModel.generate(root.getWidth(), root.getHeight());

            // Do not spawn new ball on existing ball!
            boolean collision = true;
            while (collision) {
                for (BouncingBallsModel b : balls) {
                    if (Math.abs(b.x - ball.x) <= BouncingBallsModel.size &&
                            Math.abs(b.y - ball.y) <= BouncingBallsModel.size) {
                        ball = BouncingBallsModel.generate(root.getWidth(), root.getHeight());
                        break;
                    }
                }
                collision = false;
            }

            balls.add(ball);
        });

        removeBtn.setOnAction(event -> {
            System.out.println("Remove");
            balls.remove(balls.size() - 1);
        });
    }
}
