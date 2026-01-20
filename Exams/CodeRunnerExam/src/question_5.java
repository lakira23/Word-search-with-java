
public class question_5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	try {
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
	}
	catch (Exeception e) {
		return "ERROR#1";
	}

}
