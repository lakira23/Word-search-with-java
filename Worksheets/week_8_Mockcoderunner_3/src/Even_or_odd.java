public class Even_or_odd {

	public static void main(String[] args) {
		double value = 3.0;
		check_status((int) value);
	}

	public static boolean check_status(int num) {
		if (num % 2 == 0) {
			return true;
		}
		else {

			return false;
		}
	}

}
