import swiftbot.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


class SwiftBotFeedback {

    // reference to the robot
    private SwiftBotAPI swiftBot;

    public SwiftBotFeedback(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    // reacts to feedback using colours
    // green = correct position, yellow = correct colour wrong place, red = no match
    public void react(String feedback) throws InterruptedException {

        if (feedback.contains("+")) {
            swiftBot.fillUnderlights(new int[]{0,255,0}); // correct positions
        } else if (feedback.contains("-")) {
            swiftBot.fillUnderlights(new int[]{255,255,0}); // correct colour wrong place
        } else {
            swiftBot.fillUnderlights(new int[]{255,0,0}); // no matches
        }

        Thread.sleep(800); // keep light on briefly
        swiftBot.disableUnderlights();
    }

    // called when player wins
    public void win() throws InterruptedException {
        swiftBot.fillUnderlights(new int[]{0,255,0}); // green
        swiftBot.move(100,-100,1000); // small spin movement
    }

    // called when player loses
    public void lose() throws InterruptedException {
        swiftBot.fillUnderlights(new int[]{255,0,0}); // red
        swiftBot.move(-50,-50,1000); // move backwards
    }
    
    // shows a single colour using underlights
    public void showColour(char c) throws InterruptedException {

        int[] colour;

        // map character to RGB value
        switch (c) {
            case 'R': colour = new int[]{255, 0, 0}; break;
            case 'G': colour = new int[]{0, 255, 0}; break;
            case 'B': colour = new int[]{0, 0, 255}; break;
            case 'Y': colour = new int[]{255, 255, 0}; break;
            case 'O': colour = new int[]{255, 165, 0}; break;
            case 'P': colour = new int[]{255, 105, 180}; break;
            default:  colour = new int[]{255, 255, 255}; // fallback
        }

        swiftBot.fillUnderlights(colour);
        Thread.sleep(400);
        swiftBot.disableUnderlights();
    }
    
    // replays the full guessed sequence user entered using underlights
    public void showSequence(List<Character> guess) throws InterruptedException {

        Thread.sleep(500); // small delay before playback

        for (char c : guess) {
            showColour(c);
            Thread.sleep(200); // spacing between colours
        }
    }
}

class ScoreManager {

    // keeps track of scores for both sides
    private int playerScore = 0;
    private int computerScore = 0;

    // called when player wins a game
    public void playerWins() {
        playerScore++;
    }

    // called when computer wins (player loses)
    public void computerWins() {
        computerScore++;
    }

    // returns score in simple format like "1-0"
    public String getScore() {
        return playerScore + "-" + computerScore;
    }
}

class LoggerService {

    // file where all game logs are stored
    private String filePath = "game_log.txt";

    //writes a single line to the log file
    public void log(String message) {

        try (FileWriter fw = new FileWriter(filePath, true)) {

            //append message to file
            fw.write(message + "\n");

        } catch (IOException e) {

            // error message if handling fails
            System.out.println("Logging error!");
        }
    }

    // returns file path to be shown to user
    public String getFilePath() {
        return filePath;
    }
}

class GameConfig {
	// ended up choosing to use direct variables in the final implementation, this is just design idea
    // stores settings for a game session
    private int codeLength;
    private int maxAttempts;

    // constructor to initialise config values
    public GameConfig(int codeLength, int maxAttempts) {
        this.codeLength = codeLength;
        this.maxAttempts = maxAttempts;
    }

    // returns how many colours are in the code
    public int getCodeLength() {
        return codeLength;
    }

    // returns how many attempts the player has
    public int getMaxAttempts() {
        return maxAttempts;
    }
}

class FeedbackService {

    // returns the symbol feedback like "++-" 
    // + = correct colour in correct position
    // - = correct colour but wrong position
    public static String getFeedback(List<Character> code, List<Character> guess) {

        int plus = 0;
        int minus = 0;

        // copies so original list dont need to be modified
        List<Character> codeCopy = new ArrayList<>(code);
        List<Character> guessCopy = new ArrayList<>(guess);

        // first pass - check correct positions
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                plus++;

                // mark as used so it wont be counted again
                codeCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // second pass - correct colour wrong position
        for (int i = 0; i < guessCopy.size(); i++) {

            // skip already matched values
            if (guessCopy.get(i) != null && codeCopy.contains(guessCopy.get(i))) {
                minus++;

                // remove matched value to avoid double counting
                codeCopy.set(codeCopy.indexOf(guessCopy.get(i)), null);
            }
        }

        // return feedback with all + first then -
        return "+".repeat(plus) + "-".repeat(minus);
    }
    
    // returns detailed feedback as numbers instead of symbols
    // [0] = correct position, [1] = correct colour wrong position
    public static int[] getDetailedFeedback(List<Character> code, List<Character> guess) {

        int plus = 0;
        int minus = 0;

        // same logic as above but returns numbers instead
        List<Character> codeCopy = new ArrayList<>(code);
        List<Character> guessCopy = new ArrayList<>(guess);

        // correct position
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                plus++;
                codeCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // correct colour wrong position
        for (int i = 0; i < guessCopy.size(); i++) {
            if (guessCopy.get(i) != null && codeCopy.contains(guessCopy.get(i))) {
                minus++;
                codeCopy.set(codeCopy.indexOf(guessCopy.get(i)), null);
            }
        }

        return new int[]{plus, minus};
    }
}

class ColourDetector {

	// takes an image and determines the main colour between red, green, blue, yellow, orange, pink
	public char detectColour(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();

		long totalR = 0, totalG = 0, totalB = 0;
		int count = 0;

		// sample pixels across the image (skips some for speed)
		for (int x = 0; x < width; x += 10) {
			for (int y = 0; y < height; y += 10) {

				Color c = new Color(image.getRGB(x, y));

				totalR += c.getRed();
				totalG += c.getGreen();
				totalB += c.getBlue();

				count++;
			}
		}

		//calculate average RGB values
		int avgR = (int)(totalR / count);
		int avgG = (int)(totalG / count);
		int avgB = (int)(totalB / count);

		// print values for debugging / testing
		System.out.println("RGB: " + avgR + ", " + avgG + ", " + avgB);

		return mapToColour(avgR, avgG, avgB);
	}

	//maps RGB values to one of the game colours
	private char mapToColour(int r, int g, int b) {

		// ignore very dark values (likely no card detected)
		if (r + g + b < 100) return '?';

		// red and orange detection
		if (r > 180 && b < 100) {

			double ratio = (double) g / r;

			// if green is close to red it returns orange
			if (ratio > 0.65) return 'O';

			return 'R';
		}

		// green and yellow detection
		if (g > b) {

			// if red and green are close, treat as yellow
			if (Math.abs(r - g) < 50 && r > 150 && g > 150) return 'Y';

			return 'G';
		}

		// pink detection (red + blue)
		if (r > 180 && b > 120) return 'P';

		// blue detection
		if (b > r && b > g) return 'B';

		return '?'; // fall back if unclear
	}
}

class CodeGenerator {

	// available colours for the game
	private static final char[] COLOURS = {'R','G','B','Y','O','P'};

	// generates a random code with no repeating colours
	public static List<Character> generateCode(int length) {

		List<Character> list = new ArrayList<>();

		// add all possible colours into a list
		for (char c : COLOURS) {
			list.add(c);
		}

		// randomise order of list
		Collections.shuffle(list);

		// take only the required number of colours
		return list.subList(0, length);
	}
}

class CameraHandler {

	// reference to the SwiftBot API
	private SwiftBotAPI swiftBot;

	// constructor to pass in the robot instance
	public CameraHandler(SwiftBotAPI swiftBot) {
		this.swiftBot = swiftBot;
	}

	// captures an image using the robot camera
	public BufferedImage captureImage() {
		try {
			System.out.println("Capturing image...");

			// take a square image (smaller size for speed)
			BufferedImage img = swiftBot.takeStill(ImageSize.SQUARE_240x240);

			// basic check to make sure an image was captured
			if (img == null) {
				System.out.println("Image is NULL!");
			} else {
				System.out.println("Image captured successfully");
			}

			return img;

		} catch (Exception e) {
			// handles camera errors (e.g. camera disabled or not working, as mine was for a short while)
			System.out.println("Camera error:");
			e.printStackTrace();
			return null;
		}
	}
}

public class Master_mind {

	// keeps track of which hint positions were already used
	static Set<Integer> usedHints = new HashSet<>();

	public static SwiftBotAPI swiftBot;

	public static void run_master_mind(String[] args) throws InterruptedException {

		int gameNumber = 1; // tracks how many games played

		try {
			swiftBot = SwiftBotAPI.INSTANCE; // initialises robot
		} catch (Exception e) {
			System.out.println("SwiftBot failed to initialise!");
			return;
		}

		// setup main components
		Scanner scanner = new Scanner(System.in);
		CameraHandler cam = new CameraHandler(swiftBot);
		ColourDetector detector = new ColourDetector();
		ScoreManager score = new ScoreManager();

		SwiftBotFeedback bot = new SwiftBotFeedback(swiftBot); // handles lights and movement
		LoggerService logger = new LoggerService(); // logs everything to file

		int codeLength;
		int maxAttempts;

		// choose between default or custom mode using buttons
		String mode = selectMode();

		if (mode.equals("DEFAULT")) {
			codeLength = 4;
			maxAttempts = 6;
		} else {

			// custom setup with validation
			do {
				System.out.println("Enter code length (3-6): ");
				codeLength = scanner.nextInt();
			} while (codeLength < 3 || codeLength > 6);

			do {
				System.out.println("Enter number of attempts: ");
				maxAttempts = scanner.nextInt();
			} while (maxAttempts <= 0);

			scanner.nextLine(); // clear leftover input
		}

		// main game loop (keeps running until exit)
		while (true) {

			List<Character> code = CodeGenerator.generateCode(codeLength);
			System.out.println("New game started!");

			// log game setup
			logger.log("\nGame " + gameNumber);
			logger.log("Mode: "+ mode);
			logger.log("Code Length: "+ codeLength);
			logger.log("Max Attempts: " + maxAttempts);
			logger.log("Code: " + code);

			boolean won = false;

			usedHints.clear(); // reset hints every game
			int hintsLeft = 1;

			// attempt loop
			for (int attempt = 1; attempt <= maxAttempts; attempt++) {

				System.out.println("\nAttempt " + attempt + "/" + maxAttempts);

				System.out.println("Press H for a hint or ENTER to continue:");
				String input = scanner.nextLine().toUpperCase();

				// hint system
				if (input.equals("H")) {
					if (hintsLeft > 0) {
						giveHint(code);
						hintsLeft--;
						System.out.println("Hints remaining: " + hintsLeft);
					} else {
						System.out.println("No hints left!");
					}

					attempt--;   // dont count hint as attempt
					continue; 
				}

				// scan colours using camera
				List<Character> guess = scanGuess(cam, detector, codeLength, scanner, bot);

				// replay the guess using lights
				bot.showSequence(guess);
				Thread.sleep(500);

				// get feedback
				String feedback = FeedbackService.getFeedback(code, guess);
				int[] detail = FeedbackService.getDetailedFeedback(code, guess);

				System.out.println("Feedback: " + feedback);
				System.out.println(detail[0] + " correct position");
				System.out.println(detail[1] + " correct colour wrong position");

				// logs attempt info
				logger.log("\nAttempt " + attempt);
				logger.log("Guess: " + guess);
				logger.log("Feedback: " + (feedback.isEmpty() ? "(none)" : feedback));
				logger.log("Guesses left: " + (maxAttempts - attempt));

				// robot reacts (lights + movement)
				bot.react(feedback);

				// check win
				if (feedback.equals("+".repeat(codeLength))) {
					System.out.println("You WIN!");
					score.playerWins();
					bot.win();
					won = true;

					logger.log("Result: WIN");
					logger.log("Score: " + score.getScore());
					logger.log("--------------------------------");

					gameNumber++;
					break;
				}
			}

			// if player didnt win
			if (!won) {
				System.out.println("You LOST! Code was: " + code);
				score.computerWins();
				bot.lose();

				logger.log("Result: LOSS");
				logger.log("Score: " + score.getScore());
				logger.log("--------------------------------");

				gameNumber++;
			}

			// replay or exit using buttons
			String choice = waitForReplayChoice();
			swiftBot.disableUnderlights();

			if (choice.equals("X")) {
				System.out.println("Exiting game... ");
				System.out.println("Log saved at: " + logger.getFilePath());
				System.exit(0); // force exit
			}
		}
	}

	// gives a random correct position as hint
	public static void giveHint(List<Character> code) {

		Random rand = new Random();
		int index;

		do {
			index = rand.nextInt(code.size());
		} while (usedHints.contains(index));

		usedHints.add(index);

		System.out.println("HINT: Position " + (index + 1) + " is " + code.get(index));
	}

	// scans colours one by one using camera
	public static List<Character> scanGuess(CameraHandler cam, ColourDetector detector, int length, Scanner scanner, SwiftBotFeedback bot) throws InterruptedException {

		List<Character> guess = new ArrayList<>();

		for (int i = 1; i <= length; i++) {
			System.out.println("Place card " + i + " and press ENTER...");
			scanner.nextLine();

			var image = cam.captureImage();

			if (image != null) {
				char colour = detector.detectColour(image);

				if (colour == '?') {
					System.out.println("Colour unclear, try again!");
					i--;
					continue;
				}

				System.out.println("Detected: " + colour);
				guess.add(colour);

				// show detected colour on robot
				bot.showColour(colour);
			} else {
				System.out.println("Capture failed, try again.");
				i--;
			}
		}

		return guess;
	}

	// handles mode selection using buttons
	public static String selectMode() throws InterruptedException {

		final String[] mode = {""};

		System.out.println("Press A for Default Mode or B for Custom Mode");

		swiftBot.disableButtonLights();
		swiftBot.setButtonLight(Button.A, true);
		swiftBot.setButtonLight(Button.Y, true);
		swiftBot.fillButtonLights();

		// invalid button feedback
		swiftBot.enableButton(Button.X, () -> {
			System.out.println("Invalid button! Press A or B.");
		});

		swiftBot.enableButton(Button.Y, () -> {
			System.out.println("Invalid button! Press A or B.");
		});

		// valid buttons
		swiftBot.enableButton(Button.A, () -> {
			System.out.println("Default Mode Selected");
			mode[0] = "DEFAULT";
		});

		swiftBot.enableButton(Button.B, () -> {
			System.out.println("Custom Mode Selected");
			mode[0] = "CUSTOM";
		});

		// wait for selection
		while (mode[0].equals("")) {
			Thread.sleep(100);
		}

		swiftBot.disableButtonLights();
		swiftBot.disableAllButtons();

		return mode[0];
	}

	// handles replay choice (Y/X)
	public static String waitForReplayChoice() throws InterruptedException {

		final String[] choice = {""};

		System.out.println("Press Y to play again or X to quit");

		swiftBot.disableButtonLights();
		swiftBot.setButtonLight(Button.X, true);
		swiftBot.setButtonLight(Button.B, true);
		swiftBot.fillButtonLights();

		// invalid buttons
		swiftBot.enableButton(Button.A, () -> {
			System.out.println("Invalid button! Press Y or X.");
		});

		swiftBot.enableButton(Button.B, () -> {
			System.out.println("Invalid button! Press Y or X.");
		});

		// valid buttons
		swiftBot.enableButton(Button.Y, () -> {
			swiftBot.disableButtonLights();
			System.out.println("Play Again selected");
			choice[0] = "Y";
		});

		swiftBot.enableButton(Button.X, () -> {
			swiftBot.disableButtonLights();
			System.out.println("Exit selected");
			choice[0] = "X";
		});

		// wait for input
		while (choice[0].equals("")) {
			Thread.sleep(100);
		}

		swiftBot.disableAllButtons();

		return choice[0];
	}
}