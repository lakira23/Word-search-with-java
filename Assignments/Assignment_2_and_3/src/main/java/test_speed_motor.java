import java.util.Scanner;
import swiftbot.SwiftBotAPI;


public class test_speed_motor {
	
	static SwiftBotAPI swiftbot = SwiftBotAPI.INSTANCE;
	static int bot_power = 50;
	
	//90 degres = 1030 ms
	//180 degress = 1995
	
	//180 degress = 775 ms
	//90 degress = 400ms
	
	//straight for 25 = 850 ms
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int extra_p = -10;
		while (true) {
			System.out.println("give time");
			
			int time = scan.nextInt();
			if (time == 0) {
				break;
			}
			else {
			//swiftbot.move(bot_power, -bot_power, the_time);
			swiftbot.move(-(bot_power + extra_p),bot_power, time);
				
			System.out.println("------------------------\n");
			System.out.println("that was : "+ time);
			System.out.println("");
		}
		}	
	}

}
