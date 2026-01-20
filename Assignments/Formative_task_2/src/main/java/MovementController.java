import swiftbot.SwiftBotAPI;

public class MovementController {
	 static SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
	
	public static void celebration_movement(int score){
		int bot_power = score * 10;
		
		if (score >= 10) {
			bot_power = 100;
		}
		else if (score < 5) {
			bot_power = 40;
		}
		double bot_speed = bot_power * 0.4212;
		v_movement(bot_power, bot_speed);
	}
	
	private static void v_movement(int power, double speed) {
		
		double time = (30 / speed) * 1000;
		
		swiftBot.move(-50, 50, 500);
		swiftBot.move(power, power, (int) time);
		swiftBot.move(-power, -power, (int) time);
		swiftBot.move(50, -50, 500);
		swiftBot.move(power, power, (int) time);
		swiftBot.move(-power, -power, (int) time);
		swiftBot.move(-50, 50, 250);
		
	}
	

}
