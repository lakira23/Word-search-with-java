package week_15_mock;

import java.util.ArrayList;

public class questions {

	public static void main(String[] args) {
		//System.out.println(JoulesToWattHours(75589));
//		System.out.println(Quadrant(null, "5"));
//		System.out.println(Quadrant("5", null));
//		System.out.println(Quadrant(null, null));
//		System.out.println(Quadrant(null, " "));
//		System.out.println(Quadrant("  ", "  "));
//		System.out.println(Quadrant("", ""));
		
		ArrayList<String> test = new ArrayList<String>();
		test.add("55.93");
		//test.add(null);
		//test.add(" ");
		//test.add("");
		//test.add("Zear");
		test.add("-91.19");
		System.out.println(CToF(test));
	}

	public static double JoulesToWattHours (double j) {
		if (j <= 0) {
			return -1;
		}

		else if (j > 1000000) {
			return -2;
		}

		else {
			double answer =  j / 3600.0;
			return answer;
		}
	}

	public static int Quadrant(String x, String y) {
		try {


			if (x == null || y == null) {
				return -1;
			}
			else if (x.isBlank() || y.isBlank() ||x.contains(" ") || y.contains(" ")) {
				return -1;
			}

			int intx = Integer.parseInt(x);
			int inty = Integer.parseInt(y);

			if (intx == 0 || inty == 0) {
				return -3;
			}

			if (intx>0 && inty > 0) {
				return 1;
			}	
			if (intx < 0 && inty > 0) {
				return 2;
			}
			if (intx < 0 && inty < 0) {
				return 3;
			}
			if (intx > 0 && inty <0 ) {
				return 4;
			}
			return 0;
		}
		catch (Exception e) {
			return -2;
		}
	}
	
public static  ArrayList<Double> CToF(ArrayList<String> C){
	ArrayList<Double> F = new ArrayList<Double>();
	
	for (int i = 0; i < C.size(); i ++) {
		if (C.get(i) == null || C.get(i).isEmpty()) {
			System.out.println(i + " is null or empty");
			return null;
			
		}
		
		try {
			double convert = Double.parseDouble(C.get(i)) * 9/5 + 32;
			System.out.println( convert +" is the convertion");
			F.add(convert);
			
		}
		catch (Exception e) {
			System.out.println(i + " is error");
			return new ArrayList<Double>();
		}
			
	}
	
	return F;
}
	
	
}
