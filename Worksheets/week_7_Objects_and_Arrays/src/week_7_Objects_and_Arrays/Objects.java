package week_7_Objects_and_Arrays;

public class Objects {

	public static void main(String[] args) {
		Data [] x = {
				new Data("Fred",41),
				new Data("Jo",43),
				new Data("Zoe", 37)
		};
		
		for (int i = 0; i < x.length; i ++) {
			x[i].Print();
		}
		
		

	}
	

}
