// -- IMPORTS  -- //
import swiftbot.*;
import swiftbot.SwiftBotAPI;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//====================================================================
// MAIN SYSTEM CLASS:
// Acts as central controller 
// Handles Main menu and brings other classes together to run smoothly 
//====================================================================
public class SPY_BOT {
	static SwiftBotAPI swiftBot;
	static Scanner scanner = new Scanner(System.in);
    // Colour codes for CLI
	public static final String RESET = "\u001B[0m";
	public static final String CYAN = "\u001B[36m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";
	public static final String WHITE = "\u001B[37m";
	public static final String BOLD = "\u001B[1m";
	public static final String RED = "\u001B[31m";

	public static void run_spyBot(String[] args) {
//		Using try-catch block here so that if robot turns off or
//		fails to connect, the program exits safely with a friendly error message
		try {
			swiftBot = SwiftBotAPI.INSTANCE;
			displayWelcomeScreen();
			showMainMenu();

		} catch(Exception e) {
			System.out.println("ERROR: Could not connect to SwiftBot." + RESET);
			e.printStackTrace();
			System.exit(5);
		}

	}
	
//	--- STARTUP SCREENS ---
	public static void displayWelcomeScreen() {

		String art = CYAN +


				"       \n" 
				+ "                                    _____                    _____                _____                    _____                   _______               _____          \r\n"
				+ "         /\\    \\                  /\\    \\              |\\    \\                  /\\    \\                 /::\\    \\             /\\    \\         \r\n"
				+ "        /::\\    \\                /::\\    \\             |:\\____\\                /::\\    \\               /::::\\    \\           /::\\    \\        \r\n"
				+ "       /::::\\    \\              /::::\\    \\            |::|   |               /::::\\    \\             /::::::\\    \\          \\:::\\    \\       \r\n"
				+ "      /::::::\\    \\            /::::::\\    \\           |::|   |              /::::::\\    \\           /::::::::\\    \\          \\:::\\    \\      \r\n"
				+ "     /:::/\\:::\\    \\          /:::/\\:::\\    \\          |::|   |             /:::/\\:::\\    \\         /:::/~~\\:::\\    \\          \\:::\\    \\     \r\n"
				+ "    /:::/__\\:::\\    \\        /:::/__\\:::\\    \\         |::|   |            /:::/__\\:::\\    \\       /:::/    \\:::\\    \\          \\:::\\    \\    \r\n"
				+ "    \\:::\\   \\:::\\    \\      /::::\\   \\:::\\    \\        |::|   |           /::::\\   \\:::\\    \\     /:::/    / \\:::\\    \\         /::::\\    \\   \r\n"
				+ "  ___\\:::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\       |::|___|______    /::::::\\   \\:::\\    \\   /:::/____/   \\:::\\____\\       /::::::\\    \\  \r\n"
				+ " /\\   \\:::\\   \\:::\\    \\  /:::/\\:::\\   \\:::\\____\\      /::::::::\\    \\  /:::/\\:::\\   \\:::\\ ___\\ |:::|    |     |:::|    |     /:::/\\:::\\    \\ \r\n"
				+ "/::\\   \\:::\\   \\:::\\____\\/:::/  \\:::\\   \\:::|    |    /::::::::::\\____\\/:::/__\\:::\\   \\:::|    ||:::|____|     |:::|    |    /:::/  \\:::\\____\\\r\n"
				+ "\\:::\\   \\:::\\   \\::/    /\\::/    \\:::\\  /:::|____|   /:::/~~~~/~~      \\:::\\   \\:::\\  /:::|____| \\:::\\    \\   /:::/    /    /:::/    \\::/    /\r\n"
				+ " \\:::\\   \\:::\\   \\/____/  \\/_____/\\:::\\/:::/    /   /:::/    /          \\:::\\   \\:::\\/:::/    /   \\:::\\    \\ /:::/    /    /:::/    / \\/____/ \r\n"
				+ "  \\:::\\   \\:::\\    \\               \\::::::/    /   /:::/    /            \\:::\\   \\::::::/    /     \\:::\\    /:::/    /    /:::/    /          \r\n"
				+ "   \\:::\\   \\:::\\____\\               \\::::/    /   /:::/    /              \\:::\\   \\::::/    /       \\:::\\__/:::/    /    /:::/    /           \r\n"
				+ "    \\:::\\  /:::/    /                \\::/____/    \\::/    /                \\:::\\  /:::/    /         \\::::::::/    /     \\::/    /            \r\n"
				+ "     \\:::\\/:::/    /                  ~~           \\/____/                  \\:::\\/:::/    /           \\::::::/    /       \\/____/             \r\n"
				+ "      \\::::::/    /                                                          \\::::::/    /             \\::::/    /                            \r\n"
				+ "       \\::::/    /                                                            \\::::/    /               \\::/____/                             \r\n"
				+ "        \\::/    /                                                              \\::/____/                 ~~                                   \r\n"
				+ "         \\/____/                                                                ~~                                                            \r\n"
				+ "                                                                                                                                                                                  \n" +
				"                                                               \n" + RESET;

		System.out.println(art);
		System.out.println("----------------------------------");
		System.out.println("System status: " + GREEN + "INITIALISING..." + RESET + "\n");
	}

//       --- MAIN MENU & MISSION LOGIC ---
	public static void showMainMenu() {

		System.out.println(YELLOW + ""
				+ "     \n"
				+ "                                                                                                                                                                                                                          \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "MMMMMMMM               MMMMMMMM               AAA               IIIIIIIIIINNNNNNNN        NNNNNNNN     MMMMMMMM               MMMMMMMMEEEEEEEEEEEEEEEEEEEEEENNNNNNNN        NNNNNNNNUUUUUUUU     UUUUUUUU\r\n"
				+ "M:::::::M             M:::::::M              A:::A              I::::::::IN:::::::N       N::::::N     M:::::::M             M:::::::ME::::::::::::::::::::EN:::::::N       N::::::NU::::::U     U::::::U\r\n"
				+ "M::::::::M           M::::::::M             A:::::A             I::::::::IN::::::::N      N::::::N     M::::::::M           M::::::::ME::::::::::::::::::::EN::::::::N      N::::::NU::::::U     U::::::U\r\n"
				+ "M:::::::::M         M:::::::::M            A:::::::A            II::::::IIN:::::::::N     N::::::N     M:::::::::M         M:::::::::MEE::::::EEEEEEEEE::::EN:::::::::N     N::::::NUU:::::U     U:::::UU\r\n"
				+ "M::::::::::M       M::::::::::M           A:::::::::A             I::::I  N::::::::::N    N::::::N     M::::::::::M       M::::::::::M  E:::::E       EEEEEEN::::::::::N    N::::::N U:::::U     U:::::U \r\n"
				+ "M:::::::::::M     M:::::::::::M          A:::::A:::::A            I::::I  N:::::::::::N   N::::::N     M:::::::::::M     M:::::::::::M  E:::::E             N:::::::::::N   N::::::N U:::::D     D:::::U \r\n"
				+ "M:::::::M::::M   M::::M:::::::M         A:::::A A:::::A           I::::I  N:::::::N::::N  N::::::N     M:::::::M::::M   M::::M:::::::M  E::::::EEEEEEEEEE   N:::::::N::::N  N::::::N U:::::D     D:::::U \r\n"
				+ "M::::::M M::::M M::::M M::::::M        A:::::A   A:::::A          I::::I  N::::::N N::::N N::::::N     M::::::M M::::M M::::M M::::::M  E:::::::::::::::E   N::::::N N::::N N::::::N U:::::D     D:::::U \r\n"
				+ "M::::::M  M::::M::::M  M::::::M       A:::::A     A:::::A         I::::I  N::::::N  N::::N:::::::N     M::::::M  M::::M::::M  M::::::M  E:::::::::::::::E   N::::::N  N::::N:::::::N U:::::D     D:::::U \r\n"
				+ "M::::::M   M:::::::M   M::::::M      A:::::AAAAAAAAA:::::A        I::::I  N::::::N   N:::::::::::N     M::::::M   M:::::::M   M::::::M  E::::::EEEEEEEEEE   N::::::N   N:::::::::::N U:::::D     D:::::U \r\n"
				+ "M::::::M    M:::::M    M::::::M     A:::::::::::::::::::::A       I::::I  N::::::N    N::::::::::N     M::::::M    M:::::M    M::::::M  E:::::E             N::::::N    N::::::::::N U:::::D     D:::::U \r\n"
				+ "M::::::M     MMMMM     M::::::M    A:::::AAAAAAAAAAAAA:::::A      I::::I  N::::::N     N:::::::::N     M::::::M     MMMMM     M::::::M  E:::::E       EEEEEEN::::::N     N:::::::::N U::::::U   U::::::U \r\n"
				+ "M::::::M               M::::::M   A:::::A             A:::::A   II::::::IIN::::::N      N::::::::N     M::::::M               M::::::MEE::::::EEEEEEEE:::::EN::::::N      N::::::::N U:::::::UUU:::::::U \r\n"
				+ "M::::::M               M::::::M  A:::::A               A:::::A  I::::::::IN::::::N       N:::::::N     M::::::M               M::::::ME::::::::::::::::::::EN::::::N       N:::::::N  UU:::::::::::::UU  \r\n"
				+ "M::::::M               M::::::M A:::::A                 A:::::A I::::::::IN::::::N        N::::::N     M::::::M               M::::::ME::::::::::::::::::::EN::::::N        N::::::N    UU:::::::::UU    \r\n"
				+ "MMMMMMMM               MMMMMMMMAAAAAAA                   AAAAAAAIIIIIIIIIINNNNNNNN         NNNNNNN     MMMMMMMM               MMMMMMMMEEEEEEEEEEEEEEEEEEEEEENNNNNNNN         NNNNNNN      UUUUUUUUU      \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                         \r\n"
				+ "                                                                                                                                                                                                                      \n"
				+ "   \n"
				+ "     " + RESET);
		System.out.println("[1] Send Secure Message");
		System.out.println("[2] View Logs");
		System.out.println("[3] Exit");
		System.out.println("\nSelect an option: ");

		String choice = scanner.nextLine();

		switch(choice) {
		case "1":
			System.out.println(CYAN + "PLEASE AUTHENTICATE TO CONTINUE." + RESET);
			
			// Created an object of the Authentication class to handle the camera
			Authentication auth = new Authentication(swiftBot); 
			String fullSenderQR = auth.authenticateSender();

			// If the camera times out or fails, returns to menu safely
			if (fullSenderQR == null) {
				System.out.println(RED + "\n[!] AUTHENTICATION FAILED. Mission Aborted." + RESET);
				showMainMenu();
				break;
			}

			// Cleans up the QR code text. replaceAll() removes any accidental hidden
			// spaces that the QR generator might have added, so it doesn't break the math later
			String[] senderParts = fullSenderQR.split(":");
			String senderID = senderParts[0].trim();
			String startLocation = (senderParts.length > 1) ? senderParts[1].replaceAll("\\s+", "").toUpperCase() : "A";


			System.out.println(GREEN + "\nAUTHENTICATION SUCCESSFUL! Welcome, Agent " + senderID + RESET);
			System.out.println("Starting message protocol...");
			
			// Create the translator object to handle button inputs and Morse decoding
			MorseTranslator translator  = new MorseTranslator(swiftBot, scanner);

			String rawMorse = translator.recordInputProcess();

			if (rawMorse != null) {
				String decodedMessage = translator.decodeAndPrint(rawMorse);

				if (decodedMessage != null) {
					System.out.println(GREEN + "Message validated. Ready for Navigation..." + RESET);

					// Dynamic priority scaling:
					// Adjusts robot speed and LED blink based on the length of the message
					int moveSpeed = 50;
					int ledDelay = 800;
					String priority  = "LOW";

					if (decodedMessage.length() < 10) {
						moveSpeed = 100;
						ledDelay = 400;
						priority = "HIGH";
					}
					
					String destination = translator.extractDestination(decodedMessage).trim().toUpperCase();

					System.out.println("Priority: " + priority + " (Move Speed: " + moveSpeed + "%, LED Delay: "  + ledDelay + "ms)");

					// start driving sequence
					navigateTrack(moveSpeed, startLocation, destination);



					System.out.println(CYAN + "\n--- RECEIVER AUTHENTICATION ---" + RESET);
					System.out.println(">>> SENDER MUST AUTHENTICATE RECEIVER.");
					String receiverID = auth.authenticateReceiver();

					if (receiverID != null) {
						String[] receiverParts = receiverID.split(":");
						String receiverLoc = (receiverParts.length > 1) ? receiverParts[1].replaceAll("\\s+", "").toUpperCase() : "UNKNOWN";

						
						// Security check:
						// Make sure the person receiving the message is actually at the correct destination
						if(receiverLoc.equals(destination)) {
							System.out.println(GREEN + "Receiver Authenticated: " + receiverID + RESET);
							translator.deliverMessageLEDs(rawMorse, ledDelay);
							Logger.logMessage(senderID, receiverID, decodedMessage, destination, priority, "SUCCESS");

							System.out.println(GREEN + "Protocol complete. Returning to menu..." + RESET);
							showMainMenu();
						} else {
							// If user scans a QR code for the wrong safe house,blocks the message and logs as  unauthorised - redirects back to main menu
							System.out.println(RED + "[!] SECURITY ALERT: Agent at location " + receiverLoc + " is not authorised for this message! Mission Aborted." + RESET);
							Logger.logMessage(senderID, receiverID, decodedMessage, destination, priority, "FAILED (UNAUTHORISED)");
							showMainMenu();
						}


					} else {
						System.out.println(RED + "Receiver Authentication Failed. Mission Aborted." + RESET);
						Logger.logMessage(senderID, "UNKNOWN", decodedMessage, destination, priority, "FAILED");
						showMainMenu();
					}

				} else {
					showMainMenu(); // returns if Morse code was invalid
				}

			} else {

				showMainMenu(); // returns if no input detected
			}

			break;

		case "2":
			System.out.println("Opening logs...");
			Logger.displayLogs();
			showMainMenu();
			break;

		case "3":
			System.out.println(RED + "Closing connection." + RESET);
			System.exit(0);
			break;

		default:
			System.out.println(RED + "Invalid input. Please Try again." + RESET);
			showMainMenu();


		}

	}
	
	// --- NAVIGATION LOGIC ---
	// This method handles all  the complex driving maths so main menu stays clean and simple
	public static void navigateTrack(int speed, String start, String dest) {
		System.out.println(CYAN + "\n>>> Commencing navigation from " + start + " to " + dest + "..." + RESET);

		// Calculate the shortest path around the triangle to avoid unnecessary driving
		int legsToTravel = 0;
		if (start.equals(dest)) {
			legsToTravel = 0; 
		}  else if ((start.equals("A") && dest.equals("B")) ||
				(start.equals("B") && dest.equals("C")) ||
				(start.equals("C") && dest.equals("A"))) {
			legsToTravel = 1;
		} else if ((start.equals("A") && dest.equals("C")) ||
				(start.equals("B") && dest.equals("A")) ||
				(start.equals("C") && dest.equals("B"))) {
			legsToTravel  = 2;
		} else {
			legsToTravel = 3; // Fallback
		}



		try {
			if (legsToTravel == 0) {
				System.out.println(YELLOW + "Agent is already at the destination safehouse. No movement required." + RESET);
			} else {
				for (int i = 1; i <= legsToTravel; i++) {
					System.out.println("Leg " + i + "/" + legsToTravel + ": Driving forward...");
					swiftBot.move(speed, speed, 1000);
					Thread.sleep(500);


					// Only want the swiftBot to turn if it has another leg left to travel
					if (i < legsToTravel) {
						System.out.println("Leg " + i + "/" + legsToTravel + ": Turning...");
						swiftBot.move(speed, -speed, 800);
						Thread.sleep(500);
					}
				}

			}
			System.out.println(GREEN + ">>> Arrived at Destination.\n" + RESET);
		} catch (Exception e) {
			System.out.println(RED + "Navigation Hardware Error: " + e.getMessage() + RESET);
		}
	}

}


//======================================================================================
//MORSE TRANSLATOR CLASS:
//Separated to keep the code organised.
// It is entirely responsible for handling button inputs, dictionaries and LED outputs.
//======================================================================================
class MorseTranslator {
	// These variables are private (encapsulated) so they can't be accidentally 
	// altered by other parts of the program.
	private SwiftBotAPI bot;
	private Scanner scanner;
	private HashMap<String, String> dictionary;

	public  MorseTranslator(SwiftBotAPI bot, Scanner scanner) {
		this.bot = bot;
		this.scanner = scanner;
		setupDictionary();  // Auto prepares the dictionary when its created
	}

	public String recordInputProcess() { // Makes UI user friendly and easy to navigate
		System.out.println("\nUse SwiftBot Buttons to enter Morse code: ");
		System.out.println("[X] Dot (.), [Y] Dash (-), [A] End of Character ( ), [B] End of Word (/)");
		System.out.println("TYPE '0' IN THIS CONSOLE AND PRESS ENTER TO FINISH RECORDING");

		String rawMorse = recordInput();

		if (rawMorse.isEmpty() || rawMorse.isBlank()) {
			System.out.println(SPY_BOT.RED + "\n[!] No input detected." + SPY_BOT.RESET);
			return null;
		}
		return rawMorse;

	}

	public String decodeAndPrint(String rawMorse) {
		String decodedText = decodeMorse(rawMorse);

		if (decodedText == null) {
			System.out.println(SPY_BOT.RED + "\n\n[ERROR] Invalid Morse sequence detected. Input rejected." + SPY_BOT.RESET);
			return null;
		}

		String destination = extractDestination(decodedText);

		System.out.println(SPY_BOT.GREEN + "\n--- TRANSLATION SUCCESSFUL ---" + SPY_BOT.RESET);
		System.out.println("Raw Morse: " + rawMorse);
		System.out.println("Decoded Message: " + decodedText);
		System.out.println("Target Destination: " + destination );

		return decodedText;
	}

	public void deliverMessageLEDs(String rawMorse, int ledDelay) {
		System.out.println(">>> Transmitting Message via UnderLights...");


		int[] colorWhite = { 255, 255, 255};
		int[] colorBlue = { 0, 0, 255};
		int[] colorAmber = { 255, 191, 0};
		int[] colorRed = { 255, 0, 0};
		int[] colorGreen = { 0, 255, 0};

		try {

			for (char c : rawMorse.toCharArray()) {
				if (c == '.') {
					System.out.println("FLASHING WHITE (Dot)");
					bot.fillUnderlights(colorWhite);
				} else if (c == '-') {
					System.out.println("FLASHING BLUE (Dash)");
					bot.fillUnderlights(colorBlue);
				} else if (c == ' ') {
					System.out.println("FLASHING AMBER (End of character)");
					bot.fillUnderlights(colorAmber);
				} else if (c == '/') {
					System.out.println("FLASHING RED (End of Word)");
					bot.fillUnderlights(colorRed);
				}

				Thread.sleep(ledDelay);

				bot.disableUnderlights();
				Thread.sleep(ledDelay / 2);
			}

			System.out.println("FLASHING GREEN (End of Message)");
			bot.fillUnderlights(colorGreen);
			Thread.sleep(1000);
			bot.disableUnderlights();


			System.out.println(">>> Transmission Complete.");
		} catch (Exception e) {
			System.out.println("Error with LEDs: " + e.getMessage());
			e.printStackTrace();
		}

	}



	private String recordInput() {
		StringBuilder morse = new StringBuilder();
		boolean[] isFinished = {false};


		//  Lambda expressions act as listeners. Every time a button is pressed it adds a character
		bot.enableButton(Button.X, () -> {morse.append("."); System.out.print("."); });
		bot.enableButton(Button.Y, () -> {morse.append("-"); System.out.print("-"); });
		bot.enableButton(Button.A, () -> {morse.append(" "); System.out.print(" "); });
		bot.enableButton(Button.B, () -> {morse.append("/"); System.out.print("/"); });

		
		// System.in.available() lets the program check for a keyboard input ('0')
		// without freezing the swiftBot buttons allowing them to work simultaneously
		while (!isFinished[0]) {
			try {
				if (System.in.available() > 0) {
					String input = scanner.nextLine();
					if (input.trim().equals("0")) {
						isFinished[0] = true;
					}
				}

				Thread.sleep(100);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}

		}
		bot.disableAllButtons();
		System.out.println();
		return morse.toString().trim();

	} 

	private String decodeMorse(String morse) {
		StringBuilder decoded = new StringBuilder();

		String[] words = morse.split("/");

		for (String word : words) {
			if (word.trim().isEmpty()) continue;

			// A HashMap is very fast at looking up a specific key like the morse code 
			// to return its value (the English letter)
			String[] characters = word.trim().split(" ");

			for (String morseChar : characters) {
				if (morseChar.isEmpty()) continue;

				if (dictionary.containsKey(morseChar)) {
					decoded.append(dictionary.get(morseChar));
				} else {
					return null; // reject input if the sequence doesnt exist
				}
			}
			decoded.append(" ");
		}
		return decoded.toString().trim();
	}

	public String extractDestination(String text) {
		if (text.isEmpty()) return "Unknown";
		String[] parts = text.split(" ");
		return parts[0];
	}




	private void setupDictionary() {
		// Initialises the HashMap to store the morse code dictionary data
		dictionary = new HashMap<>();

		dictionary.put(".-", "A"); dictionary.put("-...", "B");
		dictionary.put("-.-.", "C"); dictionary.put("-..", "D");
		dictionary.put(".", "E"); dictionary.put("..-.", "F");
		dictionary.put("--.", "G"); dictionary.put("....", "H");
		dictionary.put("..", "I"); dictionary.put(".---", "J");
		dictionary.put("-.-", "K"); dictionary.put(".-..", "L");
		dictionary.put("--", "M"); dictionary.put("-.", "N");
		dictionary.put("---", "O"); dictionary.put(".--.", "P");
		dictionary.put("--.-", "Q"); dictionary.put(".-.", "R");
		dictionary.put("...", "S"); dictionary.put("-", "T");
		dictionary.put("..-", "U"); dictionary.put("...-", "V");
		dictionary.put(".--", "W"); dictionary.put("-..-", "X");
		dictionary.put("-.--", "Y"); dictionary.put("--..", "Z");
		dictionary.put("-----", "0"); 

	}

}



//====================================================================
//AUTHENTICATION CLASS:
// Dedicated solely to activating the camera and processing QR codes
//====================================================================
class Authentication {
	private SwiftBotAPI bot;

	public Authentication(SwiftBotAPI bot) {
		this.bot = bot;
	}

	public String authenticateSender() {
		System.out.println(">>> SCANNING SENDER QR Code... (10 second timeout)");
		return performScan();
	}

	public String authenticateReceiver() {
		System.out.println(">>> SCANNING RECEIVER QR Code... (10 second timeout)");
		return performScan();
	}

	private String performScan() {
		// A timeout loop ensures that if no QR code is found, the program 
		// exits back to them menu instead of scanning forever
		long endTime = System.currentTimeMillis() + 10000;
		int[] scanLight = {255, 255, 255};
		try { 
			while (System.currentTimeMillis() < endTime) {
				bot.fillUnderlights(scanLight);
				Thread.sleep(100);
				bot.disableUnderlights();

				BufferedImage img = bot.getQRImage();
				String decodedMessage = bot.decodeQRImage(img);

				if (!decodedMessage.isEmpty()) {
					return decodedMessage;
				}
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			System.out.println("Camera Error: " + e.getMessage());
		}
		return null;
	}
}
//====================================================================
//LOGGER CLASS:
//Handles File input/output. separating this makes it easy to change 
// how files are stored without altering the rest of the system.
//====================================================================
class Logger {
	private static final String FILE_PATH = "SpyBot_Logs.txt";


	public static void logMessage(String sender, String receiver, String message, String destination, String priority, String status) {
		// Opening the filewriter inside the 'try' statement automatically closes  
		// the  file when done, preventing memory leaks or file corruption
		try (FileWriter fw  = new FileWriter(FILE_PATH, true);
				PrintWriter pw = new PrintWriter(fw)) {

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String timestamp = now.format(formatter);

			pw.println("----------------------------------------------");
			pw.println("TIMESTAMP   : " + timestamp);
			pw.println("SENDER      : " + sender);
			pw.println("RECEIVER    : " + receiver);
			pw.println("DESTINATION : " + destination);
			pw.println("PRIORITY     : " + priority);
			pw.println("STATUS    : " + status);
			pw.println("MESSAGE     : " + message);
			pw.println("----------------------------------------------");

			System.out.println(">>> Log successfully saved to: " + FILE_PATH);
		} catch (IOException e) {
			System.out.println(SPY_BOT.RED + ">>> [ERROR] Failed to save log: " + e.getMessage() + SPY_BOT.RESET);
		}
	}

	public static void displayLogs() {
		try {
			File file = new File(FILE_PATH);
			if (file.exists()) {
				Scanner fileReader = new Scanner(file);
				System.out.println(SPY_BOT.CYAN + "\n--- SPYBOT SECURE LOGS ---" + SPY_BOT.RESET);
				while (fileReader.hasNextLine()) {
					System.out.println(fileReader.nextLine());
				}
				fileReader.close();
				System.out.println(SPY_BOT.CYAN + "----------------------------------------------\n" + SPY_BOT.RESET);
			} else {
				System.out.println(SPY_BOT.YELLOW + "\n[i] No logs found. System is clean.\n" + SPY_BOT.RESET);
			}
		} catch (Exception e) {
			System.out.println(SPY_BOT.RED + "Error reading logs: "  + e.getMessage() + SPY_BOT.RESET);
		}
	}


}