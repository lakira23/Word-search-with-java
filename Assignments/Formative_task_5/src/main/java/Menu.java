import java.io.IOException;
import java.util.Scanner;

public class Menu {  

	static String ascii_thanks = " ______  __ __   ____  ____   __  _      __ __   ___   __ __  __ \r\n"
			+ "|      ||  |  | /    ||    \\ |  |/ ]    |  |  | /   \\ |  |  ||  |\r\n"
			+ "|      ||  |  ||  o  ||  _  ||  ' /     |  |  ||     ||  |  ||  |\r\n"
			+ "|_|  |_||  _  ||     ||  |  ||    \\     |  ~  ||  O  ||  |  ||__|\r\n"
			+ "  |  |  |  |  ||  _  ||  |  ||     \\    |___, ||     ||  :  | __ \r\n"
			+ "  |  |  |  |  ||  |  ||  |  ||  .  |    |     ||     ||     ||  |\r\n"
			+ "  |__|  |__|__||__|__||__|__||__|\\_|    |____/  \\___/  \\__,_||__|\r\n"
			+ "                                                                 ";

	static String ascii_menu = " (                             )             *                    \r\n"
			+ " )\\ )         (       )  (  ( /(   *   )   (  `                   \r\n"
			+ "(()/((  (  (  )\\ ) ( /(( )\\ )\\())` )  /(   )\\))(    (         (   \r\n"
			+ " /(_))\\))( )\\(()/( )\\())((_|(_)\\  ( )(_)) ((_)()\\  ))\\ (     ))\\  \r\n"
			+ "(_))((_)()((_)/(_)|_))((_)_  ((_)(_(_())  (_()((_)/((_))\\ ) /((_) \r\n"
			+ "/ __|(()((_|_|_) _| |_ | _ )/ _ \\|_   _|  |  \\/  (_)) _(_/((_))(  \r\n"
			+ "\\__ \\ V  V / ||  _|  _|| _ \\ (_) | | |    | |\\/| / -_) ' \\)) || | \r\n"
			+ "|___/\\_/\\_/|_||_|  \\__||___/\\___/  |_|    |_|  |_\\___|_||_| \\_,_| \r\n"
			+ "                                                                  \n";

	static String[] taskList = {"Exit", "Master Mind", "Zigzag", "Snakes and Ladders",  // Fixed: camelCase field name
			"Traffic Light", "SpyBot", "Draw Shape", "Noughts and Crosses",
			"Search for Light", "Dance", "Detect Object"};

	public static int menu() throws InterruptedException {
		System.out.printf(Colours.RED + Colours.BOLD + ascii_menu + Colours.RESET);

		Scanner reader = new Scanner(System.in);

		while (true) {
			System.out.println("Please enter the number for the designated task:");
			for (int i = 0; i < taskList.length; i++) {
				System.out.println("[" + i + "] " + taskList[i]);
			}
			System.out.print(">> ");

			try {

				int ans = reader.nextInt();


				if (ans >= 0 && ans < taskList.length) {

					Thread.sleep(1000);
					return ans;
				} else {
					System.out.println(Colours.RED + Colours.BOLD
							+ "ERROR! Please enter a number between 0 and " + (taskList.length - 1) + "!"
							+ Colours.RESET);
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				reader.nextLine();
				System.out.println(Colours.RED + Colours.BOLD
						+ "ERROR! Please enter a valid number!"
						+ Colours.RESET);
				Thread.sleep(1000);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		while (true) {
			int choice = menu();

			switch (choice) {
			case 0: {
				System.out.println(ascii_thanks);
				System.exit(0);
				break;
			}
			case 1: {
				Master_mind.run_master_mind(args);
				break;
			}
			case 2: {
				ZigZag.run_zigzag(args);
				break;
			}
			case 3: {
				Snakes_and_ladders task_3 = new Snakes_and_ladders();
				task_3.run_snakes_and_ladders(args);
				break;
			}
			case 4: {
				Traffic_lights.run_traffic_lights(args);
				break;
			}
			case 5: {
				SPY_BOT.run_spyBot(args);
				break;
			}
			case 6: {
				Draw_shape.run_draw_shape(args);
				break;
			}
			case 7: {
				FinalNoughtsAndCrosses.run_noughts_and_crosses(args);
				break;
			}
			case 8: {
				System.out.println("Search for Light: not yet implemented.");
				break;
			}
			case 9: {
				System.out.println("Dance: not yet implemented.");
				break;
			}
			case 10: {
				Detect_Object.run_detect_object(args);
				break;
			}
			default: {
				System.out.println(Colours.RED + Colours.BOLD+"ERROR: Please enter a valid number." + Colours.RESET);
				Thread.sleep(2000);
				break;
			}
			}
		}
	}
}