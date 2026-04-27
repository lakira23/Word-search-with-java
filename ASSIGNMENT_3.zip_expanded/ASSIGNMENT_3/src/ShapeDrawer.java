import swiftbot.SwiftBotAPI;

public class ShapeDrawer {
    private final SwiftBotAPI bot;
    private final MovementConfig config;

    public ShapeDrawer(SwiftBotAPI bot, MovementConfig config) {
        this.bot = bot;
        this.config = config;
    }

    public ShapeResult drawShape(Shape shape) throws InterruptedException {
        long start = System.currentTimeMillis();

        System.out.println("Drawing: " + shape.getType() + " [" + shape.getSizeText() + "]");

        if (shape instanceof Square) {
            drawSquare((Square) shape);
        } else if (shape instanceof Triangle) {
            drawTriangle((Triangle) shape);
        }

        blinkGreen();

        long end = System.currentTimeMillis();
        long duration = end - start;

        return new ShapeResult(
                shape.getType(),
                shape.getSizeText(),
                shape.getAngleText(),
                duration,
                shape.getArea()
        );
    }

    public void moveBack15Cm() throws InterruptedException {
        long ms = config.distanceToMs(15);
        bot.move(-config.getDrawSpeed(), -config.getDrawSpeed(), (int) ms);
        Thread.sleep(ms);
        bot.stopMove();
    }

    private void drawSquare(Square square) throws InterruptedException {
        long sideMs = config.distanceToMs(square.getSide());

        for (int i = 0; i < 4; i++) {
            bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) sideMs);
            Thread.sleep(sideMs);
            bot.stopMove();

            long turnMs = config.angleToTurnMs(90);
            bot.move(config.getTurnSpeed(), -config.getTurnSpeed(), (int) turnMs);
            Thread.sleep(turnMs);
            bot.stopMove();
        }
    }

    private void drawTriangle(Triangle triangle) throws InterruptedException {
        long aMs = config.distanceToMs(triangle.getA());
        long bMs = config.distanceToMs(triangle.getB());
        long cMs = config.distanceToMs(triangle.getC());

        bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) aMs);
        Thread.sleep(aMs);
        bot.stopMove();

        turnByAngle(180.0 - triangle.getAngleB());

        bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) bMs);
        Thread.sleep(bMs);
        bot.stopMove();

        turnByAngle(180.0 - triangle.getAngleC());

        bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) cMs);
        Thread.sleep(cMs);
        bot.stopMove();
    }

    private void turnByAngle(double angle) throws InterruptedException {
        long turnMs = config.angleToTurnMs(angle);
        bot.move(config.getTurnSpeed(), -config.getTurnSpeed(), (int) turnMs);
        Thread.sleep(turnMs);
        bot.stopMove();
    }

    private void blinkGreen() throws InterruptedException {
        int[] green = {0, 255, 0};

        for (int i = 0; i < 3; i++) {
            bot.fillUnderlights(green);
            Thread.sleep(250);
            bot.disableUnderlights();
            Thread.sleep(250);
        }
    }
}
