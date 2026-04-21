import swiftbot.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Detect_Object {

    static SwiftBotAPI swiftBot;

    // ── ANSI colours ─────────────────────────────────────────────────────────────
    static final String RESET  = "\u001B[0m";
    static final String CYAN   = "\u001B[36m";
    static final String YELLOW = "\u001B[33m";
    static final String GREEN  = "\u001B[32m";
    static final String RED    = "\u001B[31m";
    static final String BOLD   = "\u001B[1m";

    // ── Constants ─────────────────────────────────────────────────────────────────
    private static final double BUFFER_ZONE_CM    = 30.0;
    private static final double BUFFER_TOLERANCE  = 5.0;   // ±5 cm = "at the gap"
    private static final double SCARED_TRIGGER_CM = 50.0;
    private static final int    WANDER_SPEED      = 70;
    private static final int    MOVE_SPEED        = 70;
    private static final int    FLEE_MS           = 3000;

    private static final String IMAGE_DIR = "/data/home/pi/object_images/";
    private static final String LOG_FILE  = "/data/home/pi/swiftbot_log.txt";

    // ── LED colour arrays ─────────────────────────────────────────────────────────
    private static final int[] BLUE        = {0,   0,   255};
    private static final int[] GREEN_LIGHT = {0,   255, 0  };
    private static final int[] RED_LIGHT   = {255, 0,   0  };
    private static final int[] OFF         = {0,   0,   0  };

    // ── Runtime state ─────────────────────────────────────────────────────────────
    private static String  selectedMode         = "";
    private static String  activeMode           = "";
    private static int     objectEncounters     = 0;
    private static int     curiousEncounters    = 0;  // counts encounters in curious loop
    private static long    startTime            = 0;
    private static long    modeStartTime        = 0;
    private static long    encounterWindowStart = 0;
    private static int     encountersInWindow   = 0;
    private static boolean limitPromptShown     = false;
    private static int     wanderDirection      = 1; // +1 = slight right, -1 = slight left

    private static final Map<String, long[]>       modeStats  = new LinkedHashMap<>();
    private static final Map<String, List<String>> modeImages = new LinkedHashMap<>();
    private static final AtomicBoolean             running    = new AtomicBoolean(true);
    private static final Scanner                   scanner    = new Scanner(System.in);

    // ════════════════════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ════════════════════════════════════════════════════════════════════════════
    public static void run_detect_object(String[] args) {
        try {
            swiftBot = SwiftBotAPI.INSTANCE;
            new File(IMAGE_DIR).mkdirs();
        } catch (Exception e) {
            System.out.println(RED + BOLD + "ERROR: SwiftBot hardware not available!" + RESET);
            System.exit(1);
        }

        printHeader();

        try {
            selectedMode = scanModeQR();
            if (selectedMode.isEmpty()) {
                System.out.println(RED + "No valid mode scanned. Exiting." + RESET);
                System.exit(1);
            }

            activeMode = resolveMode(selectedMode);

            System.out.println(GREEN + BOLD + "Mode: " + selectedMode
                    + (selectedMode.equals("Dubious SwiftBot") ? " -> " + activeMode : "")
                    + RESET + "\n");

            setupStopButton();

            startTime            = System.currentTimeMillis();
            modeStartTime        = startTime;
            encounterWindowStart = startTime;
            initModeTracking(activeMode);

            runBehaviorLoop();

        } catch (Exception e) {
            System.out.println(RED + "Fatal error: " + e.getMessage() + RESET);
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // HEADER
    // ════════════════════════════════════════════════════════════════════════════
    private static void printHeader() {
        System.out.println(CYAN + BOLD
                + "*******************************************************************\n"
                + "*               SWIFTBOT OBJECT DETECTOR  - Task 10               *\n"
                + "*******************************************************************\n"
                + RESET);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // MODE RESOLUTION  (Dubious picks randomly once at startup / mode change)
    // ════════════════════════════════════════════════════════════════════════════
    private static String resolveMode(String selected) {
        if (selected.equals("Dubious SwiftBot")) {
            String resolved = new Random().nextBoolean() ? "Curious SwiftBot" : "Scaredy SwiftBot";
            System.out.println(CYAN + "Dubious mode resolved to: " + resolved + RESET);
            return resolved;
        }
        return selected;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // QR SCANNING  — retries until a valid mode name is scanned
    // ════════════════════════════════════════════════════════════════════════════
    private static String scanModeQR() {
        System.out.println(YELLOW + "Please show a QR code to select a mode:" + RESET);
        System.out.println("  - Curious SwiftBot");
        System.out.println("  - Scaredy SwiftBot");
        System.out.println("  - Dubious SwiftBot\n");

        final String[] VALID = {"Curious SwiftBot", "Scaredy SwiftBot", "Dubious SwiftBot"};

        while (running.get()) {
            try {
                BufferedImage img     = swiftBot.getQRImage();
                String        decoded = swiftBot.decodeQRImage(img);

                if (decoded != null && !decoded.isBlank()) {
                    String trimmed = decoded.trim();
                    for (String mode : VALID) {
                        if (trimmed.equals(mode)) {
                            System.out.println();
                            return mode;
                        }
                    }
                    System.out.println(RED + "\nUnrecognised mode: \"" + trimmed + "\""
                            + "\nPlease use one of the three mode names exactly as shown above."
                            + RESET);
                }
            } catch (Exception ignored) {}

            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            System.out.print(".");
        }
        return "";
    }

    // ════════════════════════════════════════════════════════════════════════════
    // X BUTTON
    // ════════════════════════════════════════════════════════════════════════════
    private static void setupStopButton() {
        try {
            swiftBot.enableButton(Button.X, () -> {
                System.out.println("\nX button pressed - stopping SwiftBot...");
                running.set(false);
            });
            System.out.println(GREEN + "X button configured to stop the program." + RESET + "\n");
        } catch (Exception e) {
            System.out.println(YELLOW + "Warning: could not configure X button." + RESET);
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // MAIN BEHAVIOUR LOOP
    //
    // Active move-then-scan cycle:
    //   1. Drive one step (~15 cm).
    //   2. Stop and scan with ultrasound.
    //   3. Object found -> handle encounter, then resume.
    //   4. After NO_OBJECT_STEPS consecutive steps with no object ->
    //      wait 1 s, flip direction.
    // ════════════════════════════════════════════════════════════════════════════
    private static final int WANDER_STEP_MS  = 800; // ~15 cm at speed 70
    private static final int NO_OBJECT_STEPS = 6;   // ~5 s worth of steps

    private static final int MAX_CURIOUS_ENCOUNTERS = 5;

    private static void runBehaviorLoop() throws InterruptedException {
        setUnderlights(BLUE);

        if (activeMode.equals("Curious SwiftBot")) {
            runCuriousLoop();
        } else {
            runScaredyLoop();
        }
    }

    // ── Curious outer loop: wander → encounter → turn → repeat up to 5 times ──
    // ── Curious outer loop: runs 5 encounters, prompts C/T, repeats until T ────
    private static void runCuriousLoop() throws InterruptedException {
        while (running.get()) {
            curiousEncounters = 0;
            System.out.println("Curious mode started. Will seek up to " + MAX_CURIOUS_ENCOUNTERS + " objects.");

            while (running.get() && curiousEncounters < MAX_CURIOUS_ENCOUNTERS) {

                System.out.println("--- Wandering for object "
                        + (curiousEncounters + 1) + " of " + MAX_CURIOUS_ENCOUNTERS + " ---");

                boolean found = wanderUntilObject();
                if (!found) break;

                double dist = readDistance();
                handleObjectEncounter(dist);

                if (curiousEncounters < MAX_CURIOUS_ENCOUNTERS && running.get()) {
                    System.out.println("Encounter " + curiousEncounters + " done. Turning to find next object...");
                    setUnderlights(BLUE);
                }
            }

            // All 5 encounters done — prompt C/T, then loop again if C was chosen
            if (running.get() && curiousEncounters >= MAX_CURIOUS_ENCOUNTERS) {
                stopWheels();
                setUnderlights(OFF);
                System.out.println(GREEN + BOLD + "Completed all " + MAX_CURIOUS_ENCOUNTERS + " curious encounters!" + RESET);
                promptChangeModeOrTerminate();
                // If mode changed to Scaredy inside promptChangeModeOrTerminate,
                // break so we don't restart the curious loop
                if (!activeMode.equals("Curious SwiftBot")) break;
            } else {
                break; // X button pressed or running stopped
            }
        }
    }

    // ── Wander in steps until an object is detected, return true if found ───────
    private static boolean wanderUntilObject() throws InterruptedException {
        int stepsSinceObject = 0;
        setUnderlights(BLUE);

        while (running.get()) {
            double dist = readDistance();

            // Object detected — stop immediately before taking another step
            if (dist > 0 && dist < 100) {
                stopWheels();
                System.out.println("Object spotted at " + fmt(dist) + " cm - stopping wander.");
                return true;
            }

            // No object — take one wander step
            stepsSinceObject++;
            System.out.println("Wandering... step " + stepsSinceObject
                    + " (no object yet).");

            if (stepsSinceObject >= NO_OBJECT_STEPS) {
                System.out.println("No object for ~5 s - waiting 1 s then changing direction.");
                Thread.sleep(1000);
                turnAndWander();
                stepsSinceObject = 0;
            }

            if (!running.get()) break;

            int bias  = WANDER_SPEED / 5;
            int left  = WANDER_SPEED + (wanderDirection * bias);
            int right = WANDER_SPEED - (wanderDirection * bias);
            swiftBot.move(left, right, WANDER_STEP_MS);
            Thread.sleep(WANDER_STEP_MS + 50);
            stopWheels();
            Thread.sleep(200);
        }
        return false;
    }

    // ── Scaredy outer loop ────────────────────────────────────────────────────────
    // Moves in short steps (300ms each), scans between every step.
    // Stops the moment anything is within 50cm.
    private static final int SCAREDY_STEP_MS = 300;

    private static void runScaredyLoop() throws InterruptedException {
        long noObjectSince = System.currentTimeMillis();

        while (running.get()) {

            // ── Scan BEFORE every step ────────────────────────────────────────────
            double dist = readDistance();

            if (dist > 0 && dist <= SCARED_TRIGGER_CM) {
                stopWheels();
                System.out.println("Object within 50 cm at " + fmt(dist) + " cm - triggering scaredy!");
                handleObjectEncounter(dist);
                setUnderlights(BLUE);
                // Reset the no-object timer after each encounter so 5s starts fresh
                noObjectSince = System.currentTimeMillis();
                continue;
            }

            // Check 5 second no-object timer
            if (System.currentTimeMillis() - noObjectSince >= 5000) {
                stopWheels();
                System.out.println("No object for 5 s - waiting 1 s then changing direction.");
                Thread.sleep(1000);
                wanderDirection *= -1;
                noObjectSince = System.currentTimeMillis();
                continue;
            }

            // No object and timer not expired — take one short step
            if (!running.get()) break;
            int bias  = WANDER_SPEED / 5;
            int left  = WANDER_SPEED + (wanderDirection * bias);
            int right = WANDER_SPEED - (wanderDirection * bias);
            swiftBot.move(left, right, SCAREDY_STEP_MS);
            Thread.sleep(SCAREDY_STEP_MS + 50);
            stopWheels();
            Thread.sleep(150);
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ENCOUNTER DISPATCHER
    // ════════════════════════════════════════════════════════════════════════════
    private static void handleObjectEncounter(double initialDist) throws InterruptedException {
        objectEncounters++;
        recordEncounterInWindow();
        System.out.println("\nObject detected at " + fmt(initialDist)
                + " cm  [encounter #" + objectEncounters + "]");
        stopWheels();

        if (activeMode.equals("Curious SwiftBot")) {
            curiousBehavior();
        } else {
            scaredBehavior();
        }

        checkEncounterLimit();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // TURN AND WANDER  — called when Curious mode is done with an object.
    // Waits 1 s (already waited by caller), performs a slight turn in the
    // opposite wander direction, then lets the main loop resume wandering.
    // ════════════════════════════════════════════════════════════════════════════
    private static void turnAndWander() throws InterruptedException {
        System.out.println("Turning to new direction and resuming wander...");

        // Flip the wander bias direction for next wander steps
        wanderDirection *= -1;

        // Execute a physical turn so the robot actually faces a new direction.
        // One wheel forward, one back = turn on the spot.
        // ~400 ms at MOVE_SPEED gives roughly a 30-45 degree turn.
        int turnLeft  = MOVE_SPEED * wanderDirection;   // one wheel drives
        int turnRight = -MOVE_SPEED * wanderDirection;  // other reverses
        swiftBot.move(turnLeft, turnRight, 400);
        Thread.sleep(480);
        stopWheels();
        Thread.sleep(200);

        setUnderlights(BLUE);
        System.out.println("Turned. Resuming wander for new object.");
    }

    // ════════════════════════════════════════════════════════════════════════════
    // CURIOUS BEHAVIOUR
    //
    // Brief says:
    //   - Green lights when approaching/retreating.
    //   - Maintain 30 cm buffer.
    //   - Object beyond buffer  → green lights, move toward it, stop at gap, lights off.
    //   - Object inside buffer  → green lights, back away to gap, stop, lights off.
    //   - Object at gap already → blink green, stay stationary.
    //   - Once movement complete → take image and save.
    //   - Wait 5 s → check if object moved → if yes, re-adjust.
    //   - If object not moved for 5 s → wait 1 s → wander in different direction.
    //   - If no object for 5 s → wait 1 s → wander in different direction.
    // ════════════════════════════════════════════════════════════════════════════
    private static void curiousBehavior() throws InterruptedException {
        System.out.println("--- Curious SwiftBot ---");

        double dist = readDistance();
        if (dist <= 0 || dist >= 100) {
            System.out.println("Object lost before approach - returning to wander.");
            return;
        }

        while (running.get()) {

            dist = readDistance();

            // ── No object visible: start 5-second no-object timer ────────────────
            if (dist <= 0 || dist >= 200) {
                System.out.println("Object not visible - waiting 5 s...");
                long noObjectStart = System.currentTimeMillis();
                boolean objectReappeared = false;
                while (System.currentTimeMillis() - noObjectStart < 5000) {
                    Thread.sleep(500);
                    double check = readDistance();
                    if (check > 0 && check < 100) {
                        objectReappeared = true;
                        dist = check;
                        break;
                    }
                }
                if (!objectReappeared) {
                    System.out.println("No object for 5 s - waiting 1 s then turning to new direction.");
                    curiousEncounters++;
                    Thread.sleep(1000);
                    turnAndWander();
                    break;
                }
                // Object reappeared - fall through to handle it below
            }

            double error = dist - BUFFER_ZONE_CM;

            if (Math.abs(error) <= BUFFER_TOLERANCE) {
                // ── At required gap — blink green, stay stationary ───────────────
                System.out.println("At required gap (" + fmt(dist) + " cm) - blinking green, staying stationary.");
                blinkUnderlights(GREEN_LIGHT, 4);
                setUnderlights(OFF);

            } else if (error > 0) {
                // ── Object beyond buffer — green lights, move forward to gap ─────
                System.out.println("Object beyond buffer (" + fmt(dist) + " cm) - turning green, moving toward object.");
                setUnderlights(GREEN_LIGHT);
                moveToBuffer();
                stopWheels();
                setUnderlights(OFF);
                System.out.println("Reached required gap - lights off.");

            } else {
                // ── Object inside buffer — green lights, back away to gap ─────────
                System.out.println("Object inside buffer (" + fmt(dist) + " cm) - turning green, moving backward.");
                setUnderlights(GREEN_LIGHT);
                moveToBuffer();
                stopWheels();
                setUnderlights(OFF);
                System.out.println("Reached required gap - lights off.");
            }

            // ── Take image once movement is complete ─────────────────────────────
            String imgPath = takeObjectImage("curious_" + objectEncounters + "_");
            if (imgPath != null) recordImage(activeMode, imgPath);

            // ── Record current distance at the gap as the reference ───────────────
            // After adjustment the bot should be ~30 cm away; record actual reading
            double distAfterAdjust = readDistance();
            if (distAfterAdjust <= 0 || distAfterAdjust >= 200) {
                distAfterAdjust = BUFFER_ZONE_CM; // fallback
            }

            // ── Wait 5 s then check if object has moved ───────────────────────────
            System.out.println("Waiting 5 s to check if object moves...");
            Thread.sleep(5000);

            double newDist = readDistance();

            if (newDist <= 0 || newDist >= 200) {
                // No object visible after wait
                System.out.println("Object no longer visible - waiting 1 s then turning to new direction.");
                curiousEncounters++;
                Thread.sleep(1000);
                turnAndWander();
                break;
            }

            double movement = Math.abs(newDist - distAfterAdjust);
            if (movement <= BUFFER_TOLERANCE) {
                // Object has not moved — wait 1 s, turn and wander in a different direction
                System.out.println("Object has not moved (still at " + fmt(newDist)
                        + " cm) - waiting 1 s then turning to new direction.");
                curiousEncounters++;
                Thread.sleep(1000);
                turnAndWander();
                break;
            }

            // ── Object moved — loop back and re-adjust ────────────────────────────
            System.out.println("Object moved to " + fmt(newDist) + " cm - re-adjusting...");
            dist = newDist;
        }

        setUnderlights(BLUE);
    }

    // ── Drive forward or backward in steps until within ±2 cm of 30 cm ──────────
    // Uses longer steps at MOVE_SPEED so the robot covers meaningful distance
    // each iteration. Re-reads sensor between each step to stay accurate.
    private static void moveToBuffer() throws InterruptedException {
        while (running.get()) {
            double d = readDistance();

            if (d <= 0 || d >= 200) {
                System.out.println("Sensor invalid during approach - stopping.");
                stopWheels();
                break;
            }

            double error = d - BUFFER_ZONE_CM;

            if (Math.abs(error) <= BUFFER_TOLERANCE) {
                stopWheels();
                System.out.println("Buffer reached at " + fmt(d) + " cm - stopping.");
                break;
            }

            // Step duration scales with distance to target.
            // Small steps (100-400ms) so it never overshoots the 30cm buffer.
            int stepMs = (int) Math.min(400, Math.max(100, Math.abs(error) * 8));

            if (error > 0) {
                // Object farther than 30 cm — move forward slowly
                swiftBot.move(MOVE_SPEED, MOVE_SPEED, stepMs);
            } else {
                // Object closer than 30 cm — back away
                swiftBot.move(-MOVE_SPEED, -MOVE_SPEED, stepMs);
            }

            Thread.sleep(stepMs + 80);
            stopWheels();
            Thread.sleep(300); // pause for sensor to stabilise
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // SCAREDY BEHAVIOUR
    //
    // Brief says:
    //   - Object within 50 cm → take image and save → blink underlights →
    //     back up → turn in opposite direction → move away for 3 s.
    //   - Red lights while fleeing, blue when done.
    // ════════════════════════════════════════════════════════════════════════════
    private static void scaredBehavior() throws InterruptedException {
        System.out.println("--- Scaredy SwiftBot ---");

        // 1. Take image and save
        String imgPath = takeObjectImage("scaredy_" + objectEncounters + "_");
        if (imgPath != null) recordImage(activeMode, imgPath);

        // 2. Blink underlights (red)
        blinkUnderlights(RED_LIGHT, 4);

        // 3. Red lights on, back up, turn, flee 3 s
        setUnderlights(RED_LIGHT);
        System.out.println("Object within " + SCARED_TRIGGER_CM + " cm - fleeing!");

        // Back up
        swiftBot.move(-MOVE_SPEED, -MOVE_SPEED, 500);
        Thread.sleep(600);
        stopWheels();
        Thread.sleep(200);

        // Turn ~180 degrees (longer duration to ensure full turn away from object)
        swiftBot.move(-MOVE_SPEED, MOVE_SPEED, 1000);
        Thread.sleep(1100);
        stopWheels();
        Thread.sleep(300);

        // Flee forward for exactly 3 seconds
        System.out.println("Fleeing forward for 3 s...");
        swiftBot.move(MOVE_SPEED, MOVE_SPEED, FLEE_MS);
        Thread.sleep(FLEE_MS + 100);
        stopWheels();

        setUnderlights(BLUE);
    }



    // ════════════════════════════════════════════════════════════════════════════
    // PROMPT: CHANGE MODE OR TERMINATE
    // Called after curious loop completes AND after encounter limit is hit.
    // ════════════════════════════════════════════════════════════════════════════
    private static void promptChangeModeOrTerminate() throws InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("  C  -  Change mode (scan a new QR code)");
        System.out.println("  T  -  Terminate program");
        System.out.println("------------------------------------------");

        String choice = promptChoice("Enter choice (C/T): ", "C", "T");

        if (choice.equals("T")) {
            System.out.println("Terminating...");
            running.set(false);

        } else {
            finaliseCurrentModeStats();

            System.out.println("Scan the QR code for the new mode...");
            String newSelected = scanModeQR();

            if (newSelected.isEmpty()) {
                System.out.println(RED + "No valid mode scanned. Terminating." + RESET);
                running.set(false);
                return;
            }

            selectedMode         = newSelected;
            activeMode           = resolveMode(selectedMode);
            objectEncounters     = 0;
            curiousEncounters    = 0;
            modeStartTime        = System.currentTimeMillis();
            encounterWindowStart = System.currentTimeMillis();
            encountersInWindow   = 0;
            limitPromptShown     = false;

            System.out.println(GREEN + "Mode changed to: " + activeMode + RESET);
            initModeTracking(activeMode);
            setUnderlights(BLUE);

            // Restart the correct behaviour loop for the new mode
            if (activeMode.equals("Curious SwiftBot")) {
                runCuriousLoop();
            } else {
                runScaredyLoop();
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ENCOUNTER LIMIT  — > 3 objects in < 5 minutes → prompt user
    // ════════════════════════════════════════════════════════════════════════════
    private static void recordEncounterInWindow() {
        long now = System.currentTimeMillis();
        if (now - encounterWindowStart > 5 * 60 * 1000L) {
            encounterWindowStart = now;
            encountersInWindow   = 0;
            limitPromptShown     = false;
        }
        encountersInWindow++;
    }

    private static void checkEncounterLimit() throws InterruptedException {
        boolean within5min = (System.currentTimeMillis() - encounterWindowStart) < 5 * 60 * 1000L;

        if (encountersInWindow > 3 && within5min && !limitPromptShown) {
            limitPromptShown = true;
            stopWheels();
            setUnderlights(OFF);
            System.out.println(YELLOW + BOLD
                    + "\nWARNING: More than 3 objects encountered in under 5 minutes!" + RESET);
            promptChangeModeOrTerminate();
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════════════════

    private static void stopWheels() {
        try { swiftBot.move(0, 0, 1); } catch (Exception ignored) {}
    }

    private static double readDistance() {
        try { return swiftBot.useUltrasound(); } catch (Exception e) { return -1; }
    }

    private static void setUnderlights(int[] color) {
        try { swiftBot.fillUnderlights(color); } catch (Exception ignored) {}
    }

    private static void blinkUnderlights(int[] color, int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            setUnderlights(color);
            Thread.sleep(200);
            setUnderlights(OFF);
            Thread.sleep(200);
        }
    }

    private static String takeObjectImage(String prefix) {
        try {
            String ts       = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = IMAGE_DIR + prefix + ts + ".png";
            BufferedImage img = swiftBot.takeStill(ImageSize.SQUARE_720x720);
            if (img != null) {
                ImageIO.write(img, "png", new File(filename));
                System.out.println("Image saved: " + filename);
                return filename;
            }
        } catch (Exception e) {
            System.out.println(YELLOW + "Warning: could not save image." + RESET);
        }
        return null;
    }

    private static String promptChoice(String prompt, String... accepted) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim().toUpperCase();
                for (String a : accepted) {
                    if (input.equals(a.toUpperCase())) return a;
                }
            } catch (Exception ignored) {}
            System.out.println(RED + "Invalid input. Please enter: "
                    + String.join(" or ", accepted) + RESET);
        }
    }

    private static String fmt(double d) { return String.format("%.1f", d); }

    // ════════════════════════════════════════════════════════════════════════════
    // MODE TRACKING
    // ════════════════════════════════════════════════════════════════════════════
    private static void initModeTracking(String mode) {
        modeStats.putIfAbsent(mode,  new long[]{0, 0});
        modeImages.putIfAbsent(mode, new ArrayList<>());
    }

    private static void recordImage(String mode, String path) {
        modeImages.computeIfAbsent(mode, k -> new ArrayList<>()).add(path);
    }

    private static void finaliseCurrentModeStats() {
        long elapsed = System.currentTimeMillis() - modeStartTime;
        long[] stats = modeStats.computeIfAbsent(activeMode, k -> new long[]{0, 0});
        stats[0] += elapsed;
        stats[1]  = objectEncounters;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // SHUTDOWN & LOG FILE
    // ════════════════════════════════════════════════════════════════════════════
    private static void shutdown() {
        System.out.println("\nShutting down SwiftBot...");
        try {
            stopWheels();
            Thread.sleep(300);
            setUnderlights(OFF);
            Thread.sleep(100);
            setUnderlights(OFF);
            swiftBot.disableAllButtons();
        } catch (Exception ignored) {}

        finaliseCurrentModeStats();
        writeLogFile();

        System.out.println(GREEN + "\nLog saved to : " + LOG_FILE + RESET);
        System.out.println("Images in    : " + IMAGE_DIR);
    }

    private static void writeLogFile() {
        try (PrintWriter w = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String now      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            long   totalSec = (System.currentTimeMillis() - startTime) / 1000;

            w.println("\n====================================================");
            w.println("  SwiftBot Session Log");
            w.println("====================================================");
            w.println("Session start   : "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime)));
            w.println("Session end     : " + now);
            w.println("Total duration  : " + totalSec + " s");
            w.println("Selected mode   : " + selectedMode);
            w.println("Active mode     : " + activeMode);
            w.println("Total encounters: " + objectEncounters);
            w.println();

            for (Map.Entry<String, long[]> entry : modeStats.entrySet()) {
                String       mode  = entry.getKey();
                long[]       stats = entry.getValue();
                List<String> imgs  = modeImages.getOrDefault(mode, Collections.emptyList());

                w.println("  Mode          : " + mode);
                w.println("    Duration    : " + (stats[0] / 1000) + " s");
                w.println("    Encounters  : " + stats[1]);
                w.println("    Images (" + imgs.size() + "):");
                for (String img : imgs) w.println("      - " + img);
                w.println();
            }
            w.println("====================================================");

        } catch (IOException e) {
            System.out.println(RED + "Failed to write log file: " + e.getMessage() + RESET);
        }
    }
}
