import swiftbot.*;
import java.awt.image.BufferedImage;

public class Traffic_lights {
	private final SwiftBotAPI api = SwiftBotAPI.INSTANCE;
	private volatile boolean started = false;
	private volatile boolean stopRequested = false;
	
	public static void run_traffic_lights(String[] args) {
		new Traffic_lights().run();
	}
	private void run() {
		setupButtons();
		System.out.println("==================================");
		System.out.println(" SwiftBot Traffic Light Program");
		System.out.println("==================================");
		System.out.println("Press button A on the SwiftBot to start.");
		System.out.println("Press button X on the SwiftBot to stop.");
		System.out.println();
		while (!started) {
			sleep(100);
		}
		int initialSpeed = 30;
		System.out.println("Started.");
		System.out.println("Initial speed: " + initialSpeed);
		setLights(255, 255, 0); // yellow
		forward(initialSpeed); // move immediately
		while (!stopRequested) {
			double distance = getDistanceSafe();
			// If ultrasound works and object is close, check colour
			if (distance > 0 && distance <= 30) {
				stop();
				String colour = detectColour();
				System.out.printf("Detected: %s at %.2f cm%n", colour, distance);
				if (colour.equals("RED")) {
					handleRed(initialSpeed);
				} else if (colour.equals("GREEN")) {
					handleGreen(initialSpeed);
				} else if (colour.equals("BLUE")) {
					handleBlue(initialSpeed);
				} else {
					System.out.println("Unknown colour, continuing forward.");
					setLights(255, 255, 0);
					forward(initialSpeed);
				}
			} else {
				// Nothing close, keep moving
				setLights(255, 255, 0);
				forward(initialSpeed);
			}
			sleep(200);
		}
		stop();
		api.disableUnderlights();
		api.disableAllButtons();
		System.out.println("Program stopped.");
	}
	private void setupButtons() {
		api.enableButton(Button.A, new ButtonFunction() {
			@Override
			public void run() {
				started = true;
				System.out.println("A pressed.");
			}
		});
		api.enableButton(Button.X, new ButtonFunction() {
			@Override
			public void run() {
				stopRequested = true;
				System.out.println("X pressed.");
			}
		});
	}
	private void handleRed(int speed) {
		System.out.println("RED - STOP");
		setLights(255, 0, 0);
		stop();
		sleep(1000);
		if (!stopRequested) {
			setLights(255, 255, 0);
			forward(speed);
		}
	}
	private void handleGreen(int speed) {
		System.out.println("GREEN - GO");
		setLights(0, 255, 0);
		forward(speed);
		sleep(2000); // pass within 2 seconds
		stop();
		sleep(1000);
		if (!stopRequested) {
			setLights(255, 255, 0);
			forward(speed);
		}
	}
	private void handleBlue(int speed) {
		System.out.println("BLUE - AVOID");
		stop();
		sleep(1000);
		blinkBlue(3, 250);
		// turn left
		api.startMove(-40, 40);
		sleep(550);
		stop();
		// move forward slowly
		forward(Math.max(15, speed / 2));
		sleep(1000);
		stop();
		sleep(1000);
		// move back
		backward(Math.max(15, speed / 2));
		sleep(1000);
		stop();
		// turn right to original direction
		api.startMove(40, -40);
		sleep(550);
		stop();
		if (!stopRequested) {
			setLights(255, 255, 0);
			forward(speed);
		}
	}
	private String detectColour() {
		try {
			BufferedImage img = api.takeStill(ImageSize.SQUARE_144x144);
			long r = 0, g = 0, b = 0;
			int count = 0;
			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {
					int rgb = img.getRGB(x, y);
					r += (rgb >> 16) & 0xFF;
					g += (rgb >> 8) & 0xFF;
					b += rgb & 0xFF;
					count++;
				}
			}
			if (count == 0) {
				return "UNKNOWN";
			}
			int avgR = (int) (r / count);
			int avgG = (int) (g / count);
			int avgB = (int) (b / count);
			System.out.println("Average RGB: " + avgR + ", " + avgG + ", " + avgB);
			// small threshold so weak noise does not trigger colours
			if (avgR > avgG + 15 && avgR > avgB + 15) return "RED";
			if (avgG > avgR + 15 && avgG > avgB + 15) return "GREEN";
			if (avgB > avgR + 15 && avgB > avgG + 15) return "BLUE";
			return "UNKNOWN";
		} catch (Exception e) {
			System.out.println("Camera failed: " + e.getMessage());
			return "UNKNOWN";
		}
	}
	private void setLights(int r, int g, int b) {
		try {
			api.fillUnderlights(new int[]{r, g, b});
		} catch (Exception e) {
			System.out.println("Lights failed: " + e.getMessage());
		}
	}
	private void blinkBlue(int times, int delayMs) {
		for (int i = 0; i < times; i++) {
			setLights(0, 0, 255);
			sleep(delayMs);
			api.disableUnderlights();
			sleep(delayMs);
		}
	}
	private void forward(int speed) {
		try {
			api.startMove(speed, speed);
		} catch (Exception e) {
			System.out.println("Forward move failed: " + e.getMessage());
		}
	}
	private void backward(int speed) {
		try {
			api.startMove(-speed, -speed);
		} catch (Exception e) {
			System.out.println("Backward move failed: " + e.getMessage());
		}
	}
	private void stop() {
		try {
			api.stopMove();
		} catch (Exception e) {
			System.out.println("Stop failed: " + e.getMessage());
		}
	}
	// Safe ultrasound: if sensor fails, return a large distance so robot keeps going
	private double getDistanceSafe() {
		try {
			return api.useUltrasound();
		} catch (Exception e) {
			System.out.println("Ultrasound failed: " + e.getMessage());
			return 100.0;
		}
	}
	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}