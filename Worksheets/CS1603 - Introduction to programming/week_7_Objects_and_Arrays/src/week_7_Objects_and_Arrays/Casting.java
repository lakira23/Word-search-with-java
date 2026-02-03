package week_7_Objects_and_Arrays;

import java.util.ArrayList;

public class Casting {

	public static void main(String[] args) {
//		double price = 49.99;
//		int discountedPrice = (int) price;
		
		char letter = 'A';
		int ascii = (int) letter;

		System.out.println(ascii);
		
		ArrayList<Data> people = new ArrayList<Data>();
		
		people.add(new Data("Fred", 21));
		people.add(new Data("Jo", 43));
		people.add(new Data("Zoe", 37));
		
		people.add(2, new Data("Harry", 78));
		
		for (int i = 0; i < people.size(); i ++) {
			people.get(i).Print();
			}


	}

}
