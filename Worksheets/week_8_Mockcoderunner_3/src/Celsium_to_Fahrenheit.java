package week_8
import java.util.ArrayList;

public class Celsium_to_Fahrenheit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CalculateExpression(2.0, 3.0, 4.0, 4.0);

		//System.out.println(PMT(50000, 6, 10));
		//CalculateAngle(5, 6, 7);
		int[][] cordinates = {
				{1,1},
				{8,2},
				{10,10}
		};
		
		PointInCircle(cordinates);
	}


	public static double CalculateExpression(double n, double x, double y, double z) 
	{
		double inner = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
		return 7.2 * n * inner;

	} 

	public static double PMT (double P, double r, double n) {

		r = (r/100) / 12; //rate per month
		n = n * 12; //number of months

		double numerator = P * r * Math.pow(1 + r, n);
		System.out.println(Math.pow(1+r, n));
		double denominator = Math.pow(1 + r, n) - 1;
		System.out.println(denominator);

		return numerator / denominator;
	}

	public static double CalculateAngle (double a, double b, double c) {

		double outcome = -1.0;

		if ((a + b > c) && (a + c > b) && (b + c > a)) {
			double numerator = Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2);
			double denominator = 2 * b * c;

			double cos_value = numerator / denominator;
			double angle_r = Math.acos(cos_value);
			double angle_d = Math.toDegrees(angle_r);

			outcome = angle_d;
		}
		return outcome;
	}

	public static ArrayList<Integer> PointInCircle(int[][] points){
		int h = 3;
		int k = 2;
		int r = 5;

		int double_r = (int) Math.pow(r, 2);


		ArrayList<Integer> output = new ArrayList<Integer>();

		for (int[] point: points) {

			double circle_equation = Math.pow(point[0] - h, 2) + Math.pow(point[1] - k, 2);

			if (circle_equation < double_r) {
				output.add(1);
			}
			else if (circle_equation == double_r) {
				output.add(2);
			}
			else if (circle_equation > double_r) {
				output.add(3);
			}
		}
		System.out.println(output);
		return output;
	}
}
