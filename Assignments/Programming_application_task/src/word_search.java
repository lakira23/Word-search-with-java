import java.util.ArrayList;
import java.util.Arrays;

public class word_search {

	public static void main(String[] args) {
		System.out.println("test2");

	}

	private static boolean is_capital(String letter) {
		return letter == letter.toUpperCase();
	}
	public static int valid_square(String letter) {
		int flag = 1;

		if (!is_capital(letter)) {
			//the letter is not capital
			flag = 0;
		}
		return flag;
	}

	public static int valid_word(String word) {
		int flag = 1;

		if (word == null) {
			flag = 0;
		}

		else if (word.length() == 0) {
			flag = 0;
		}

		else {
			for (int i = 0; i < word.length(); i++) {
				String current_letter = String.valueOf(word.charAt(i));

				if (!is_capital(current_letter)) {
					flag = 0;
				}
			}
		}
		return flag;
	}

	public static int valid_board(String[][] board) {
		//need to implement checks for if its too small or big
		int flag = 1;
		
		for (int i = 0; i < board.length; i++) {
			String [] each_row = board[i];
			
			if (each_row == null) {
				//checks if each row is null
				flag = 0;
			}
			
			for (int j = 0; j < each_row.length; j++) {
				String each_letter = each_row[j];
				
				if (each_letter == null) {
					//checks if each letter is null
					flag = 0;
				}
				
				else if (valid_square(each_letter) == 0){
					flag = 0; //checks if capital
				}
				
				
			}
		}
		
		
		
		return flag;
		

	}

	public static int valid_dictionary(String word) {
		int flag = 1;
		//need to chack for if too big or small
		
		if (word == null) {
			flag = 0; //checks if null
		}
		
		else if (valid_word(word) == 0) {
			flag = 0; //checks if the word is valid
		}
		
		return flag;
	}

	public static int locate_words(String word, String [] dictionary) {
		int flag = 1;
		
		if (dictionary.length == 0) {
			flag = 0; //zero size
		}
		
		else {
			for (int i = 0; i < dictionary.length; i++) {
				if (valid_dictionary(dictionary[i]) == 0) {
					flag = 0; //checks if each word in the dictionary is valid
				}
			}
			
			if (!Arrays.asList(dictionary).contains(word)) {
				//doesn;t include the word in the dictioanry
				
				
			}
		}
		
		return flag;
	}

	public static String find_words() {
		return null;
	}

	public static int[] count_dictionary() {
		return null;
	}
}
