package week_9_user_defined_funtions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Questions {

	public static void main(String[] args) {
		//		int[] temp_1 = {13,44,16,11};
		//		int[] temp_2 = {-13, -44, -16, 11};
		//		String[] temp_3 = {" ", "", "16", "xyz"};
		//
		//		System.out.println(CelsiusToFahrenheit(temp_3));
		//		reverse_string("world");
		//		System.out.println(leap_year_status(1900));

		//				int[] birthday = {05,8,2007};
		//				int[] currrent_day = {22,12,2025};
		//				days_alive(birthday,currrent_day);
		//fib_sequence(20);
		System.out.println(character_counter("123456789") - 1);	
	}

	public static boolean leap_year_status(int year) {
		return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);


	}

	public static int daysInMonth(String month, int year) {

		String[] thirtydays = {"April","June","September","Novemeber"};
		String[] thirtyonedays = {"March", "May","July","August","October","December"};

		if (Arrays.asList(thirtydays).contains(month)) {
			return 30;
		}
		else if (Arrays.asList(thirtyonedays).contains(month)) {
			return 31;
		}

		else {
			if (leap_year_status(year)) {
				return 28;
			}
			else { 
				return 29;
			}
		}
	}


	public static void days_alive(int[] birthday, int[] current_date) {		
		int [] time_lived = new int[3];

		for (int i = 0; i < 3; i++) {
			time_lived[i] = current_date[i] - birthday[i];
		}
		System.out.println(Arrays.toString(time_lived));
		System.out.println((time_lived[0] + (time_lived[1] * 12) + (time_lived[2] * 365)) + " days");

	}

	public static void fib_sequence(int until) {
		ArrayList<Integer> fib_seq = new ArrayList<Integer>();

		fib_seq.add(1);
		fib_seq.add(1);


		boolean sequence_status = true;

		while (sequence_status) {
			sequence_status = false;
			int minus_two_i = fib_seq.get(fib_seq.size() - 2);
			int minus_one_i = fib_seq.get(fib_seq.size() - 1);
			//System.out.println(minus_one_i);
			//System.out.println(minus_two_i);
			int combine = minus_one_i + minus_two_i;
			//System.out.println(combine);

			if (combine < until) {
				sequence_status = true;

			}

			fib_seq.add(combine);
		}

		System.out.println(fib_seq.toString());	
	}

	public static Integer character_counter(String c) {

		if (c.length() == 0) {
			return 1;
		}
		else {
			return 1 + character_counter((String) c.subSequence(0, c.length() - 1));
		}
	}
}
