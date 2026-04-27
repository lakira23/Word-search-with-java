import swiftbot.*;
import java.util.*;


public class Main {
	
	// keeps track of which hint positions were already used
	static Set<Integer> usedHints = new HashSet<>();

    public static SwiftBotAPI swiftBot;
    
    public static void main(String[] args) throws InterruptedException {
    	
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