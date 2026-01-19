
public class ExamQuestions {

	public static void main(String[] args) {
		System.out.println(Hello("Lakira", "Wee", 5));
		//System.out.println(B(0,0));
		//System.out.println(KineticEnergy(0, 0));
		//System.out.println(ValidPoints(-150, -150, -150, -150));
		//System.out.println(MakeSequence(10,18));
		//double[] T = {14.5 , 15.2 , 16.0 , 15.8, 17.1, 18.3, 19.0};
		//System.out.println(PredictWeather(T, 5));

		int [][] triangles = 
			{
					{1, 2, 2, 4, 6, 2, 90}, //angle is 90, (fx,fy) = (6,2)
					{-126, -30, -126, -131, -147, -137, 93}, //angle is 93, (fx,fy) = (-147,-137)
					null, //invalid null row
					{}, //invalid empty row
					{37, -125, -94}, //invalid row length
					{-136, -114, -18, 105, -133, -35, 893} //invalid degree >360
			};
		
		//System.out.println(TriangleRotation(triangles));

	}

	public static String Hello(String forename, String surname, int age) {
		return "Hello"+ " " + forename + " " + surname + " are you " + age + " years old?";
	}

	public static double B(double w, double h) {
		//test1
		if (w <= 0 || h <= 0) {
			return -1;
		}
		else {
			return w / (h * h);
		}
	}

	public static double KineticEnergy(double m, double v) {
		//test 1

		if (m < 0 || v < 0) {
			return -1;
		}
		else {
			return (0.5 * m * (v * v));
		}
	}

	public static int ValidPoints(int x1, int y1, int x2, int y2) {

		if (x1 >= -100 && x1 <= 100 && y1 >= -100 && y1 <= 100 && x2 >= -100 && x2 <= 100 && y2 >= -100 && y2 <= 100) {
			return 1;
		}

		else if (x1 >= -100 && x1 <= 100 && y1 >= -100 && y1 <= 100) {
			return 2;
		}

		else if (x2 >= -100 && x2 <= 100 && y2 >= -100 && y2 <= 100) {
			return 3;
		}

		else {
			return 4;
		}
	}

	public static String MakeSequence(int S, int N) {

		if (N < S) {
			return "ERROR#2";
		}
		String answer = "";

		for (int i = 0; i <= (N - S); i ++) {
			if (i != 0) {
				answer = answer + " ";
			}

			int current = S + i;
			answer = answer + (current * current);
		}

		return answer;
	}

	public static double[] PredictWeather(double[] T, int xdays) {
		if (T == null || T.length == 0) {
			double[] test1 = {-1.0};
			System.out.println("test1");
			return test1;
		}

		if (xdays < 1) {
			double[] test2 = {-2.0};
			System.out.println("test2");
			return test2;
		}

		double sum_diff = 0;

		for (int i = 0; i < T.length; i++) {
			if (T[i] < -82.2 || T[i] > 56.7) {
				double[] test3 = {-3.0};
				System.out.println("test3");
				return test3;
			}
			if (i > 0) {
				sum_diff = sum_diff + (T[i] - T[i-1]);
			}
		}

		double change = sum_diff / (T.length - 1);
		System.out.println(change);
		double[] P = new double[xdays];

		for (int i = 0; i < P.length; i++) {
			P[i] = T[(T.length) - 1] + (change * (i + 1));
		}

		for (double each : P) {
			System.out.println(each);
		}
		return P;

	}

	public static double[][] TriangleRotation(int triangles[][]) {

		double[][] answer = new double[7][triangles.length];

		for (int i = 0; i < triangles.length; i++) {
			if (triangles[i] == null) {
				double[] test1 = {-1.0};
				answer[i] = test1;
			}
			else {

				if (triangles[i].length != 7) {
					double[] test2 = {-2.0};
					answer[i] = test2;
				}

				else {

					int x1 = triangles[i][0];
					int y1 = triangles[i][1];

					int x2 = triangles[i][2];
					int y2 = triangles[i][3];

					int x3 = triangles[i][4];
					int y3 = triangles[i][5];

					int angle = triangles[i][6];

					if (angle < 0 || angle > 360) {
						double[] test3 = {-3.0};
						answer[i] = test3;
					}

					int fx = triangles[i][4];
					int fy = triangles[i][5];

					double theta = angle * (Math.PI / 360);

					double rotation_x1 = ((x1 - fx) * Math.cos(theta) - (y1 - fy) * Math.sin(theta)) + fx;
					double rotation_y1 = ((x1 - fx) * Math.sin(theta) + (y1 - fy) * Math.cos(theta)) + fy;

					double rotation_x2 = ((x2 - fx) * Math.cos(theta) - (y2 - fy) * Math.sin(theta)) + fx;
					double rotation_y2 = ((x2 - fx) * Math.sin(theta) + (y2 - fy) * Math.cos(theta)) + fy;

					double rotation_x3 = ((x3 - fx) * Math.cos(theta) - (y2 - fy) * Math.sin(theta)) + fx;
					double rotation_y3 = ((x3 - fx) * Math.sin(theta) + (y2 - fy) * Math.cos(theta)) + fy;

					answer[i] = new double[] {
							rotation_x1,rotation_x1,
							rotation_x2,rotation_y2,
							rotation_x3,rotation_y3
					};
				}
			}
		}

		return answer;
	}

}
