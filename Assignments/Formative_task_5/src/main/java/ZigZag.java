import swiftbot.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;

public class ZigZag {

    static SwiftBotAPI swiftBot;
    static Scanner scanner = new Scanner(System.in);
    
    // Linear calibration coefficient
    static final double K_COEFFICIENT = 0.355; 

    // --- FORWARD Rotational Calibration ---
    static final int FORWARD_LEFT_TURN_SPEED = 54; 
    static final int FORWARD_RIGHT_TURN_SPEED = 42; 
    static final int FORWARD_TURN_TIME_MS = 650; 

    // --- RETURN Rotational Calibration ---
    static final int RETURN_LEFT_TURN_SPEED = 42; 
    static final int RETURN_RIGHT_TURN_SPEED = 54; 
    static final int RETURN_TURN_TIME_MS = 650; 

    // Assignment Colours
    static final int[] GREEN = {0, 255, 0};
    static final int[] BLUE = {0, 0, 255};

    // --- Tracking Variables for Final Summary ---
    static int totalJourneys = 0;
    static double maxStraightLine = -1;
    static int maxLengthInput = 0;
    static int maxSectionsInput = 0;
    
    static double minStraightLine = Double.MAX_VALUE;
    static int minLengthInput = 0;
    static int minSectionsInput = 0;
    
    // Log file path
    static final String LOG_FILE_PATH = "zigzag_log.txt";

    public static void run_zigzag(String[] args) {
        try {
            System.out.println("Initializing SwiftBot...");
            swiftBot = SwiftBotAPI.INSTANCE;
            System.out.println("Initialization Complete.\n");

            System.out.println("-------------------------------------------------");
            System.out.println("Welcome to the SwiftBot Zigzag Task!");
            System.out.println("Format: Length-Sections (e.g., 20-6)");
            System.out.println("-------------------------------------------------");
            
            System.out.println("Press ENTER to start the camera...");
            scanner.nextLine(); 

            scanAndExecute();

        } catch (Exception e) {
            executeFailSafe("Initialization failed. Check your connection. Details: " + e.getMessage());
        }
    }

    public static void scanAndExecute() {
        boolean validScan = false;
        Random random = new Random();

        while (!validScan) {
            System.out.println("Scanning for QR code...");
            
            try {
                BufferedImage img = swiftBot.getQRImage();
                String decodedMessage = swiftBot.decodeQRImage(img);

                if (decodedMessage == null || decodedMessage.isEmpty()) {
                    System.out.println(">> No QR code found. Press ENTER to retry or type 'exit'.");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("exit")) {
                        printSummaryAndExit();
                    }
                    continue; 
                }

                System.out.println("SUCCESS: Decoded Message: " + decodedMessage);
                
                String[] parts = decodedMessage.split("-");
                
                if (parts.length == 2) {
                    try {
                        int length = Integer.parseInt(parts[0].trim());
                        int sections = Integer.parseInt(parts[1].trim());
                        
                        if (length >= 15 && length <= 85 && sections % 2 == 0 && sections <= 12) {
                            System.out.println("Input valid: " + length + "cm, " + sections + " sections.");
                            validScan = true;

                            int speedPercentage = random.nextInt(61) + 40; 
                            
                            double actualSpeed = speedPercentage * K_COEFFICIENT;
                            double timeInSeconds = (double) length / actualSpeed;
                            long timeInMillis = (long) (timeInSeconds * 1000);

                            System.out.println("-------------------------------------------------");
                            System.out.println("JOURNEY DETAILS:");
                            System.out.println("Selected Speed: " + speedPercentage + "%");
                            System.out.println("Calculated Time per Section: " + timeInMillis + " ms");
                            System.out.println("Total One-Way Journey Length: " + (length * sections) + " cm");
                            System.out.println("-------------------------------------------------");

                            executeZigzagMovement(sections, length, speedPercentage, (int) timeInMillis);
                            
                        } else {
                            System.out.println("ERROR: Length must be 15-85, Sections must be Even (max 12).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: QR code does not contain valid numbers.");
                    }
                } else {
                    System.out.println("ERROR: Invalid format. Use 'Length-Sections' (e.g. 20-6).");
                }

            } catch (Exception e) {
                System.out.println("Scanner error: " + e.getMessage());
            }
        }
    }

    public static void executeZigzagMovement(int sections, int length, int forwardSpeed, int forwardTimeMs) {
        try {
            System.out.println("Starting FORWARD movement sequence...");
            
            long startTime = System.currentTimeMillis();

            // --- FORWARD JOURNEY ---
            for (int i = 1; i <= sections; i++) {
                System.out.println("Executing section " + i + " of " + sections);
                
                if (i % 2 != 0) swiftBot.fillUnderlights(GREEN);
                else swiftBot.fillUnderlights(BLUE);
                
                swiftBot.move(forwardSpeed, forwardSpeed, forwardTimeMs);
                
                if (i < sections) {
                    swiftBot.disableUnderlights();
                    Thread.sleep(1000);
                    
                    if (i % 2 != 0) {
                        System.out.println("Turning Right...");
                        swiftBot.move(FORWARD_RIGHT_TURN_SPEED, -FORWARD_RIGHT_TURN_SPEED, FORWARD_TURN_TIME_MS);
                    } else {
                        System.out.println("Turning Left...");
                        swiftBot.move(-FORWARD_LEFT_TURN_SPEED, FORWARD_LEFT_TURN_SPEED, FORWARD_TURN_TIME_MS);
                    }
                }
            }
            
            long endTime = System.currentTimeMillis();
            double durationSeconds = (endTime - startTime) / 1000.0;
            
            System.out.println("Forward journey complete.");
            swiftBot.disableUnderlights();
            
            // --- ADDITIONAL FEATURE: TURBO RETURN MODE ---
            System.out.println("\n=================================================");
            System.out.println("SELECT RETURN MODE:");
            System.out.println("[Button A] - Standard Return (Same speed)");
            System.out.println("[Button B] - Turbo Return (Max speed)");
            System.out.println("=================================================");

            final boolean[] isTurbo = {false};
            final boolean[] inputReceived = {false};

            swiftBot.enableButton(Button.A, () -> {
                isTurbo[0] = false;
                inputReceived[0] = true;
            });
            
            swiftBot.enableButton(Button.B, () -> {
                isTurbo[0] = true;
                inputReceived[0] = true;
            });

            while (!inputReceived[0]) {
                Thread.sleep(100);
            }
            swiftBot.disableAllButtons();

            int returnForwardSpeed = forwardSpeed;
            int returnForwardTimeMs = forwardTimeMs;

            if (isTurbo[0]) {
                System.out.println("\n>> TURBO MODE ENGAGED!");
                returnForwardSpeed = 100; // Force maximum SwiftBot speed
                double actualTurboSpeed = returnForwardSpeed * K_COEFFICIENT;
                double turboTimeInSeconds = (double) length / actualTurboSpeed;
                returnForwardTimeMs = (int) (turboTimeInSeconds * 1000);
            } else {
                System.out.println("\n>> Standard Return Engaged.");
            }
            
            // --- 180 DEGREE TURN ---
            Thread.sleep(1000);
            System.out.println("Executing 180 U-Turn...");
            swiftBot.move(-FORWARD_LEFT_TURN_SPEED, FORWARD_LEFT_TURN_SPEED, FORWARD_TURN_TIME_MS);
            Thread.sleep(500);
            swiftBot.move(-FORWARD_LEFT_TURN_SPEED, FORWARD_LEFT_TURN_SPEED, FORWARD_TURN_TIME_MS);
            Thread.sleep(1000);
            
            System.out.println("Starting RETURN movement sequence...");

            // --- RETURN JOURNEY ---
            for (int i = sections; i >= 1; i--) {
                System.out.println("Retracing section " + i);
                
                if (i % 2 != 0) swiftBot.fillUnderlights(GREEN);
                else swiftBot.fillUnderlights(BLUE);
                
                // Uses the dynamically calculated speed and time
                swiftBot.move(returnForwardSpeed, returnForwardSpeed, returnForwardTimeMs);
                
                if (i > 1) {
                    swiftBot.disableUnderlights();
                    Thread.sleep(1000); 
                    
                    // INVERTED TURNS FOR RETURN JOURNEY
                    if ((i - 1) % 2 != 0) {
                        System.out.println("Turning Left (Reverse of original Right)...");
                        swiftBot.move(-RETURN_LEFT_TURN_SPEED, RETURN_LEFT_TURN_SPEED, RETURN_TURN_TIME_MS);
                    } else {
                        System.out.println("Turning Right (Reverse of original Left)...");
                        swiftBot.move(RETURN_RIGHT_TURN_SPEED, -RETURN_RIGHT_TURN_SPEED, RETURN_TURN_TIME_MS);
                    }
                }
            }
            
            System.out.println("Zigzag completely retraced. Returning to start position.");
            swiftBot.disableUnderlights();

            // --- UPDATE STATS AND WRITE LOG ---
            generateLogFileAndUpdateStats(length, sections, forwardSpeed, durationSeconds);
            
            // --- PROMPT USER FOR NEXT ACTION ---
            promptUserNextAction();

        } catch (Exception e) {
            executeFailSafe("Critical error during movement execution: " + e.getMessage());
        }
    }

    public static void generateLogFileAndUpdateStats(int length, int sections, int speed, double duration) {
        System.out.println("Generating log file data...");
        
        double straightLineDist = (sections / 2.0) * length * Math.sqrt(2);
        
        // Update Summary Tracking Variables
        totalJourneys++;
        if (straightLineDist > maxStraightLine) {
            maxStraightLine = straightLineDist;
            maxLengthInput = length;
            maxSectionsInput = sections;
        }
        if (straightLineDist < minStraightLine) {
            minStraightLine = straightLineDist;
            minLengthInput = length;
            minSectionsInput = sections;
        }
        
        try {
            // "true" enables append mode so multiple runs are logged together
            PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true));
            writer.println("--- SwiftBot Zigzag Task Log Entry ---");
            writer.println("Input Section Length: " + length + " cm");
            writer.println("Input Number of Sections: " + sections);
            writer.println("Generated Wheel Speed: " + speed + "%");
            writer.println("Total Path Length (Start to End): " + (length * sections) + " cm");
            writer.println("Duration (Start to End): " + String.format("%.2f", duration) + " seconds");
            writer.println("Straight Line Distance: " + String.format("%.2f", straightLineDist) + " cm");
            writer.println("--------------------------------------\n");
            writer.close();
            
            System.out.println("SUCCESS: Log entry added to '" + LOG_FILE_PATH + "'.");
        } catch (IOException e) {
            System.out.println("ERROR: Failed to write to log file: " + e.getMessage());
        }
    }

    public static void promptUserNextAction() {
        System.out.println("\n=================================================");
        System.out.println("JOURNEY COMPLETE. WAITING FOR INPUT:");
        System.out.println("[Button Y] - Scan another QR code");
        System.out.println("[Button X] - Terminate program and view summary");
        System.out.println("=================================================");

        final int[] nextAction = {0}; // 0 = Wait, 1 = Scan Again, 2 = Exit

        try {
            swiftBot.enableButton(Button.Y, () -> {
                nextAction[0] = 1;
            });
            
            swiftBot.enableButton(Button.X, () -> {
                nextAction[0] = 2;
            });
            
            // Loop until a button is pressed
            while (nextAction[0] == 0) {
                Thread.sleep(100);
            }
            
            swiftBot.disableAllButtons();
            
            if (nextAction[0] == 1) {
                System.out.println("\n--- Restarting Scanner ---");
                scanAndExecute(); 
            } else if (nextAction[0] == 2) {
                printSummaryAndExit(); 
            }

        } catch (Exception e) {
            executeFailSafe("Hardware error reading buttons: " + e.getMessage());
        }
    }

    public static void printSummaryAndExit() {
        System.out.println("\n=================================================");
        System.out.println("FINAL RUN SUMMARY");
        System.out.println("=================================================");
        System.out.println("Total Zigzag Journeys Completed: " + totalJourneys);
        
        if (totalJourneys > 0) {
            System.out.println("\n[LONGEST JOURNEY]");
            System.out.println("Inputs: Length = " + maxLengthInput + " cm, Sections = " + maxSectionsInput);
            System.out.println("Straight Line Distance: " + String.format("%.2f", maxStraightLine) + " cm");
            
            System.out.println("\n[SHORTEST JOURNEY]");
            System.out.println("Inputs: Length = " + minLengthInput + " cm, Sections = " + minSectionsInput);
            System.out.println("Straight Line Distance: " + String.format("%.2f", minStraightLine) + " cm");
        }
        
        File logFile = new File(LOG_FILE_PATH);
        System.out.println("\nLog file location: " + logFile.getAbsolutePath());
        System.out.println("Terminating program...");
        System.exit(0);
    }

    // --- FAIL-SAFE PROTOCOL ---
    public static void executeFailSafe(String errorMessage) {
        System.out.println("\n=================================================");
        System.out.println("CRITICAL RUNTIME FAILURE DETECTED");
        System.out.println("=================================================");
        System.out.println("Details: " + errorMessage);
        System.out.println("Initiating Fail-Safe Stop...");
        
        try {
            if (swiftBot != null) {
                swiftBot.stopMove();
                swiftBot.disableUnderlights();
                swiftBot.disableAllButtons();
            }
            System.out.println("Motors stopped. LEDs off. Program safely terminated.");
        } catch (Exception e) {
            System.out.println("Secondary hardware error during fail-safe shutdown.");
        }
        
        System.exit(1); 
    }
}