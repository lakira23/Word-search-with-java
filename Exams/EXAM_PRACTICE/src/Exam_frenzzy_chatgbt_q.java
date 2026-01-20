import java.lang.reflect.Array;
import java.util.ArrayList;

public class Exam_frenzzy_chatgbt_q {

	public static void main(String[] args) {
		joulesToWattHours(500);

	}

	private static double joulesToWattHours(double j) {
		if (j <= 0) {
			return -1;
		}
		else if (j > 1000000) {
			return -2;
		}
		else {
			double convertion = j * 1.0/3600.0;
			return convertion;
		}
	}

	private static int quadrant(String x, String y) {
		if (x == null || y == null || x.contains(" ") || y.contains(" ") || x.isEmpty() || y.isEmpty()) {
			return -1;
		}

		else if (x.equals("0") || y.equals(" ")) {
			return -3;
		}

		try {
			int intx = Integer.parseInt(x);
			int inty = Integer.parseInt(y);

			if (intx < 0 && inty > 0) {
				return 1;
			}

			else if (intx > 0 && inty > 0) {
				return 2;
			}

			else if (intx < 0 && inty < 0) {
				return 3;
			}

			else if (intx > 0 && inty < 0) {
				return 4;
			}
		}

		catch (Exception e) {
			return -2;
		}

		return 0;
	}

	private static ArrayList<Double> CToF (ArrayList<String> C){
		ArrayList<Double> F = new ArrayList<Double>();

		for (int i = 0; i < C.size(); i++) {
			try { 
				if (C.size() == 0) { return null;}
				F.add(Double.parseDouble(C.get(i)) * 9/5 + 32);
				
			}
			
			catch (NullPointerException e) {
				return null;			
				}
			catch (NumberFormatException e) {
				return new ArrayList<Double>();
			}
		}
		return F;
	}
}
