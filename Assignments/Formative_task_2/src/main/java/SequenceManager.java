

import java.util.Random;
import java.util.ArrayList;
public class SequenceManager {
	 static ArrayList<String> sequence = new ArrayList<String>(); 
	
	
	public static void AddRandomColour () {
		String[] colours = {"Red","Blue","Green","White"};
		Random rand = new Random();
		String randomColour = colours[rand.nextInt(colours.length)];
				
		sequence.add(randomColour);
	}
	
	public static ArrayList<String> Get_Sequence() {
		return sequence;
	}
	
	public static void ResetSequence() {
		sequence.clear();
	  
	}

}