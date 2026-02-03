package Week_7_coderunner_mock2;

public class questions {

	public static void main(String[] args) {
		//DoSomeCalculation(34.5,53.5,65,7);
		//System.out.println(MakeSequence(5));
		System.out.println(CalculateGradeLetter(null,null));
	}

	public static void DoSomeCalculation(double a, double b, double c, double d) 
	{
		System.out.println((a+b)/(c+d));

	}

	public static String MakeSequence(int N) 
	{
		String output = "";
		if (N < 1) {
			output = "";

		}

		for (int i = 1; i <= N; i++) {
			for (int l = 0; l <= N - i; l ++) {
				output += i;
			}
		}

		return output;

	}

	public static String CalculateGradeLetter(String strGrade, String strBonus)
	{
		String grade_letter;
		
		
		if (strGrade == null || strBonus == null) {  
			return "-1";
			
		}

		if ((strGrade.strip() == ""|| strBonus.strip() == "")) {
			return "-2";
		}

		try {
			int intGrade = Integer.parseInt(strGrade);
			int intBonus = Integer.parseInt(strBonus);


			int overall_grade = intGrade + intBonus;

			if (overall_grade >= 70) {
				grade_letter = "A";
			}

			else if (overall_grade >= 60) {
				grade_letter = "B";
			}

			else if (overall_grade >= 50) {
				grade_letter = "C";
			} 
			else if (overall_grade >= 40) {
				grade_letter = "D";
			}

			else {
				grade_letter = "F";
			}

		}
		catch (NumberFormatException e) {
			grade_letter = "-3";
		}
		
		return grade_letter;
	}

}
