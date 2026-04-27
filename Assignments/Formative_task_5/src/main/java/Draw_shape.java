import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import swiftbot.Button;
import swiftbot.ButtonFunction;
import swiftbot.SwiftBotAPI;

public class Draw_shape {

	public static void run_draw_shape(String[] args) {
		SwiftBotAPI bot = SwiftBotAPI.INSTANCE;

		QRParser parser = new QRParser();
		MovementConfig config = new MovementConfig(10.0, 850, 30, 25);
		ShapeDrawer drawer = new ShapeDrawer(bot, config);
		LogService logService = new LogService();

		List<ShapeResult> history = new ArrayList<>();

		printBanner();

		bot.enableButton(Button.X, new ButtonFunction() {
			@Override
			public void run() {
				System.out.println("X button pressed. Exiting...");
				ExitHandler.requestExit();
			}
		});

		try {
			while (!ExitHandler.isExitRequested()) {
				System.out.println("\nShow a QR code to the camera...");
				System.out.println("Examples: S:16  |  T:16:30:24  |  S:16&T:16:30:24");

				BufferedImage qrImage = bot.getQRImage();

				if (qrImage == null) {
					System.out.println("Could not capture QR image. Try again.");
					Thread.sleep(1000);
					continue;
				}

				String qrText = bot.decodeQRImage(qrImage);

				if (qrText == null || qrText.trim().isEmpty()) {
					System.out.println("No valid QR code detected. Try again.");
					Thread.sleep(1000);
					continue;
				}

				System.out.println("QR detected: " + qrText);

				try {
					List<Shape> shapes = parser.parseShapes(qrText);

					for (int i = 0; i < shapes.size(); i++) {
						if (ExitHandler.isExitRequested()) break;

						Shape shape = shapes.get(i);

						System.out.println("Shape:  " + shape.getType());
						System.out.println("Size:   " + shape.getSizeText());
						System.out.println("Angles: " + shape.getAngleText());

						ShapeResult result = drawer.drawShape(shape);
						history.add(result);

						if (i < shapes.size() - 1) {
							System.out.println("Moving back 15 cm before next shape...");
							drawer.moveBack15Cm();
						}

						System.out.println("----------------------");
					}

				} catch (IllegalArgumentException e) {
					System.out.println("Invalid QR input: " + e.getMessage());
				}

				Thread.sleep(1000);
			}

			bot.disableAllButtons();

			System.out.println("\nWriting log file...");
			System.out.println("Saved to: " + logService.writeLog(history).getAbsolutePath());

		} catch (Exception e) {
			System.out.println("Fatal error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printBanner() {
		System.out.println("======================================");
		System.out.println("         SWIFTBOT DRAW SHAPE");
		System.out.println("======================================");
		System.out.println("Press X on the SwiftBot to quit.");
	}
}

// ---------------------------------------------------------------------------

abstract class Shape {
	private final String type;

	protected Shape(String type) {
		this.type = type;
	}

	public String getType() { return type; }

	public abstract void validate();
	public abstract double getArea();
	public abstract String getSizeText();
	public abstract String getAngleText();
}

// ---------------------------------------------------------------------------

class Square extends Shape {
	private final int side;

	public Square(int side) {
		super("Square");
		this.side = side;
	}

	public int getSide() { return side; }

	@Override
	public void validate() {
		InputValidator.validateSide(side);
	}

	@Override
	public double getArea() { return side * side; }

	@Override
	public String getSizeText() { return String.valueOf(side); }

	@Override
	public String getAngleText() { return "90, 90, 90, 90"; }
}

// ---------------------------------------------------------------------------

class Triangle extends Shape {
	private final int a;
	private final int b;
	private final int c;

	private double angleA;
	private double angleB;
	private double angleC;

	public Triangle(int a, int b, int c) {
		super("Triangle");
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public int getA() { return a; }
	public int getB() { return b; }
	public int getC() { return c; }

	@Override
	public void validate() {
		InputValidator.validateSide(a);
		InputValidator.validateSide(b);
		InputValidator.validateSide(c);

		if (a + b <= c || a + c <= b || b + c <= a) {
			throw new IllegalArgumentException(
					"Invalid triangle: sides " + a + ", " + b + ", " + c + " cannot form a triangle."
			);
		}

		calculateAngles();
	}

	private void calculateAngles() {
		angleA = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2.0 * b * c)));
		angleB = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2.0 * a * c)));
		angleC = 180.0 - angleA - angleB;
	}

	public double getAngleA() { return round2(angleA); }
	public double getAngleB() { return round2(angleB); }
	public double getAngleC() { return round2(angleC); }

	@Override
	public double getArea() {
		double s = (a + b + c) / 2.0;
		return Math.sqrt(s * (s - a) * (s - b) * (s - c));
	}

	@Override
	public String getSizeText() { return a + ", " + b + ", " + c; }

	@Override
	public String getAngleText() { return getAngleA() + ", " + getAngleB() + ", " + getAngleC(); }

	private double round2(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}

// ---------------------------------------------------------------------------

class InputValidator {
	private InputValidator() {}

	public static void validateSide(int side) {
		if (side < 15 || side > 85) {
			throw new IllegalArgumentException(
					"Side length " + side + " is invalid. It must be between 15 and 85 cm."
			);
		}
	}
}

// ---------------------------------------------------------------------------

class MovementConfig {
	private final double cmPerSecond;
	private final long turn90Ms;
	private final int drawSpeed;
	private final int turnSpeed;

	public MovementConfig(double cmPerSecond, long turn90Ms, int drawSpeed, int turnSpeed) {
		this.cmPerSecond = cmPerSecond;
		this.turn90Ms = turn90Ms;
		this.drawSpeed = drawSpeed;
		this.turnSpeed = turnSpeed;
	}

	public long distanceToMs(double distanceCm) {
		return Math.round((distanceCm / cmPerSecond) * 1000.0);
	}

	public long angleToTurnMs(double angleDegrees) {
		return Math.round((angleDegrees / 90.0) * turn90Ms);
	}

	public int getDrawSpeed() { return drawSpeed; }
	public int getTurnSpeed() { return turnSpeed; }
}

// ---------------------------------------------------------------------------

class ShapeResult {
	private final String type;
	private final String sizeText;
	private final String angleText;
	private final long timeMs;
	private final double area;

	public ShapeResult(String type, String sizeText, String angleText, long timeMs, double area) {
		this.type = type;
		this.sizeText = sizeText;
		this.angleText = angleText;
		this.timeMs = timeMs;
		this.area = area;
	}

	public String getType()      { return type; }
	public String getSizeText()  { return sizeText; }
	public String getAngleText() { return angleText; }
	public long getTimeMs()      { return timeMs; }
	public double getArea()      { return area; }
}

// ---------------------------------------------------------------------------

class ShapeDrawer {
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

		long duration = System.currentTimeMillis() - start;

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

			turnByAngle(90);
		}
	}

	private void drawTriangle(Triangle triangle) throws InterruptedException {
		long aMs = config.distanceToMs(triangle.getA());
		long bMs = config.distanceToMs(triangle.getB());
		long cMs = config.distanceToMs(triangle.getC());

		// Side A
		bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) aMs);
		Thread.sleep(aMs);
		bot.stopMove();

		// Exterior turn at vertex B
		turnByAngle(180.0 - triangle.getAngleB());

		// Side B
		bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) bMs);
		Thread.sleep(bMs);
		bot.stopMove();

		// Exterior turn at vertex C
		turnByAngle(180.0 - triangle.getAngleC());

		// Side C
		bot.move(config.getDrawSpeed(), config.getDrawSpeed(), (int) cMs);
		Thread.sleep(cMs);
		bot.stopMove();

		// Final turn at vertex A to restore original heading
		turnByAngle(180.0 - triangle.getAngleA());
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

// ---------------------------------------------------------------------------

class QRParser {

	public List<Shape> parseShapes(String qrText) {
		if (qrText == null || qrText.trim().isEmpty()) {
			throw new IllegalArgumentException("QR input cannot be empty.");
		}

		String[] commands = qrText.trim().split("&");

		if (commands.length > 5) {
			throw new IllegalArgumentException("Only up to 5 shapes are allowed in one QR code.");
		}

		List<Shape> shapes = new ArrayList<>();

		for (String command : commands) {
			String[] parts = command.trim().split(":");
			String code = parts[0].trim().toUpperCase();

			try {
				switch (code) {
					case "S":
						if (parts.length != 2) {
							throw new IllegalArgumentException("Square format must be S:length");
						}
						int side = Integer.parseInt(parts[1].trim());
						Square square = new Square(side);
						square.validate();
						shapes.add(square);
						break;

					case "T":
						if (parts.length != 4) {
							throw new IllegalArgumentException("Triangle format must be T:a:b:c");
						}
						int a = Integer.parseInt(parts[1].trim());
						int b = Integer.parseInt(parts[2].trim());
						int c = Integer.parseInt(parts[3].trim());
						Triangle triangle = new Triangle(a, b, c);
						triangle.validate();
						shapes.add(triangle);
						break;

					default:
						throw new IllegalArgumentException("Unknown shape type: " + code);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("All side lengths must be whole numbers.");
			}
		}

		return shapes;
	}
}

// ---------------------------------------------------------------------------

class LogService {

	public File writeLog(List<ShapeResult> history) throws IOException {
		File file = new File("draw_shape_log.txt");

		try (FileWriter writer = new FileWriter(file)) {
			writer.write("=== Draw Shape Log ===\n\n");

			if (history.isEmpty()) {
				writer.write("No shapes were drawn.\n");
				return file;
			}

			writer.write("Shapes drawn in order:\n");
			for (ShapeResult result : history) {
				if ("Triangle".equals(result.getType())) {
					writer.write(result.getType() + ": " + result.getSizeText()
							+ " (angles: " + result.getAngleText()
							+ "; time: " + result.getTimeMs() + " ms)\n");
				} else {
					writer.write(result.getType() + ": " + result.getSizeText()
							+ " (time: " + result.getTimeMs() + " ms)\n");
				}
			}

			ShapeResult largest = findLargest(history);
			writer.write("\nLargest shape by area:\n");
			writer.write(largest.getType() + ": " + largest.getSizeText() + "\n");

			String mostFrequent = findMostFrequent(history);
			int count = countType(history, mostFrequent);
			writer.write("\nMost frequent shape:\n");
			writer.write(mostFrequent + ": " + count + " times\n");

			writer.write("\nAverage time:\n");
			writer.write(averageTime(history) + " ms\n");
		}

		return file;
	}

	private ShapeResult findLargest(List<ShapeResult> history) {
		ShapeResult largest = history.get(0);
		for (ShapeResult result : history) {
			if (result.getArea() > largest.getArea()) largest = result;
		}
		return largest;
	}

	private String findMostFrequent(List<ShapeResult> history) {
		return countType(history, "Square") >= countType(history, "Triangle") ? "Square" : "Triangle";
	}

	private int countType(List<ShapeResult> history, String type) {
		int count = 0;
		for (ShapeResult result : history) {
			if (type.equals(result.getType())) count++;
		}
		return count;
	}

	private long averageTime(List<ShapeResult> history) {
		long total = 0;
		for (ShapeResult result : history) total += result.getTimeMs();
		return total / history.size();
	}
}

// ---------------------------------------------------------------------------

class ExitHandler {
	private static volatile boolean exitRequested = false;

	public static void requestExit()        { exitRequested = true; }
	public static boolean isExitRequested() { return exitRequested; }
}