package week_7_Objects_and_Arrays;

public class seminar_activity {

	public static void main(String[] args) {
		
		int[][] C = {
				{100,100,1000},
				{70,100,2000},
				{100,70,3000}
		};
		
		for (int i = 0; i < C.length; i++ ) {
			for (int l = 0; l < C[i].length; l++) {
				System.out.println(C[i][l]);
			}
		}

	}

}
