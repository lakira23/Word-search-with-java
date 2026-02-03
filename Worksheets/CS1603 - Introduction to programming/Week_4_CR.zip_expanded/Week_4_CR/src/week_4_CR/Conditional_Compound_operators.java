package week_4_CR;

import java.util.Scanner;
public class Conditional_Compound_operators {

	public static void main(String[] args) {

		//		Scanner input = new Scanner(System.in);
		//		
		//		System.out.println("Input a number> ");
		//		
		//		int num = input.nextInt();
		//		
		//		input.close();
		//		
		for (int num = 0; num <= 100; num++) {
			System.out.println("number is " + num);
			if (num > 0) {
				if (num % 2 == 0  && num % 3 == 0) {System.out.println("divisible by 2 and 3");}
				else if (num % 7 == 0  || num % 9 == 0) {System.out.println("is divisible by 7 or 9");}
				else if  ((num % 2 == 0  && num % 3 == 0) && num % 5 != 0) {System.out.println("isdivisible by 2 and 3 but not 5");}
			}
		}
	}

}
