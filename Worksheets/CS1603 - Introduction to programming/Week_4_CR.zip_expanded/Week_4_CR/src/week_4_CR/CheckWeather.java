package week_4_CR;

//public class CheckWeather {
//
//	public static void main(String[] args) {
//
//		// Define the weather as raining
//		int x = 3;
//
//		// Check the variable
//		if (x == 1) {
//			System.out.println("Bring an umbrella.");
//		} else if (x == 2) {
//			System.out.println("No need for an umbrella.");
//		} else {
//			System.out.println("Not defined");
//			
//		}
//	}
//
//
//}

public class CheckWeather {

    public static void main(String[] args) {
        int x = 1; // Raining?
        int y = 1; // Snowing?

        if (x == 1 || y == 1) {
            System.out.println("There will be sleet");
        } else {
            System.out.println("No sleet, perhaps just rain?");
        }
    }
}






