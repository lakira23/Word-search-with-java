package week_4_CR;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Sorting_based_on_conditions {

	public static List<Integer> GetInt() {
		List<Integer> numbers = new ArrayList<>();

		Scanner scan = new Scanner(System.in);

		for (int i = 0; i < 3; i++) {
			System.out.println("input index " + i + ">>");
			numbers.add(scan.nextInt());
		}
		scan.close();

		System.out.println(numbers);
		
		return numbers;
	}

	public static void main(String[] args) {
		List<Integer> user_inp;
		user_inp = GetInt();
		
		Collections.sort(user_inp);
		
		System.out.println(user_inp);
		
		
	}



}
