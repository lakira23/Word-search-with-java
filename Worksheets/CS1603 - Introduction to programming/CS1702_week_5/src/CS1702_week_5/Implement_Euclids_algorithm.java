package CS1702_week_5;

public class Implement_Euclids_algorithm {

	public static void main(String[] args) {
		int a = 26;
		int b = 88;
		int c;
		
		while (b > 0) {
			c = b;
			b = a % b;
			a = c;
			
			System.out.println(b);
			
			
		}

	}

}
