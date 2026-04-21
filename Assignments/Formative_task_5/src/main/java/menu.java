

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class menu {
	static boolean run_flag = true;
	static String[] task_list = {"Exit", "Master Mind", "Zigzag", "Snakes and ladder", "Traffic Light", "SpyBot", "Draw Shape", "Noughts and Crosses", "Search for Light", "Dance","Detect Object"};


	public static int menu() {
		System.out.printf("""

				%1$s%2$s***************************************************************%3$s
				%1$s%2$s*                     SWIFTBOT MENU                  		  *%3$s
				%1$s%2$s***************************************************************%3$s

				""", Colours.CYAN, Colours.BOLD, Colours.RESET);

		while (run_flag) {
			try {
				System.out.println("Please enter the number for the designated task: ");
				for (int i = 0; i < task_list.length; i++) {
					System.out.println("[" + i + "]" + " " + task_list[i]);
				}

				try {
					Scanner reader = new Scanner(System.in);
					System.out.println(">> ");
					int ans = reader.nextInt();

					if (ans >= 0 && ans <= 10) {
						reader.close();
						return ans;
					}

					else {
						System.out.println(Colours.RED + Colours.BOLD + "ERROR! Please enter a number between 1 and 10!" + Colours.RESET);
						System.out.println("");
					}

				}

				catch (Exception e) {
					System.out.println("");
					System.out.println(Colours.RED + Colours.BOLD + "ERROR! Please enter a number!" + Colours.RESET);
					System.out.println("");
				}

			}
			catch (Exception e) {
				System.out.println("System Error, trying again!");
			}

		}
		return 0;




	}


	public static void main(String[] args) throws InterruptedException, IOException {
		int choice = menu();


		switch (choice) {
		case 1: {
			System.out.println("masterminf");
			break;
			//mastermind	
		}

		case 2: {
			break;
			//ZigZag
		}

		case 3: {
			//snakes and ladders
			Snakes_and_ladders task_three = new Snakes_and_ladders();
			task_three.run_snakes_and_ladders(args);
			break;

		}

		case 4: {
			break;
			//Traffic lights
		}

		case 5: {
			//SpyBot

			SPY_BOT task_five = new SPY_BOT();
			task_five.run_spyBot(args);
			break;
		}

		case 6: {
			//Draw shapes
			break;
		}

		case 7: {
			//Noughts and Crosses
			break;
		}

		case 8: {
			//Search for Lights
			break;
		}

		case 9: {
			//Dance
			break;
		}

		case 10: {
			//Detect Object
			Detect_Object task_ten = new Detect_Object();
			task_ten.run_detect_object(args);
			break;
		}

		case 0: {

			System.exit(0);
		}

		default:
			System.out.println("ERROR: Please enter a valid number.");
			break;
		}




	}

}
