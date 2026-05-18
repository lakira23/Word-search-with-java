import java.util.ArrayList;
import java.util.Arrays;

public class word_search {

	public static void main(String[] args) {
		
	}

	//___QUESTION ONE

	public static int valid_square(char letter) {
		int flag = 0; // 0 indicate error

		String letter_s = Character.toString(letter); //if its a char

		if (letter_s.matches("[A-Z]+")) {
			//checks if its part of the alphabet and is capital
			flag = 1; 
		}
		
//		if (letter >= 'A' && letter <= 'Z') {
//	        return 1;
//	    }
//	    return 0;
		
		return flag;
	}

	//___QUESTION TWO
	public static int valid_word(String word) {
		int flag = 1;

		//Check null
		if (word == null) {
			flag = 0;
		}

		//check empty
		else if (word.length() == 0) {
			flag = 0;
		}

		else {
			//checks invalid character
			for (int i = 0; i < word.length(); i++) {
				char each_character = word.charAt(i); //index gatherer
				//String each_letter = String.valueOf(word.charAt(i)); //character at index

				if (valid_square(each_character) == 0) {
					//checks if caps and in alphabet
					flag = 0;
				}
			}
		}

		return flag;
	}

	//___QUESTION THREE
	private static int is_square(char[][] board) {

		int amount_rows = board.length; 

		for (int i = 0; i < amount_rows; i++) { //iterate through the board
			int amount_coloumns = (board[i]).length; 
			if (amount_coloumns != amount_rows) { //checking if sqr
				return 0;
			}
		}
		return 1;

	}

	public static int valid_board(char[][] board) {
		int flag = 1; //1 is valid

		if (board == null) {
			//checks if the entire board is null - [null]
			flag = 0;
		}
		else if (board.length == 0) {
			//checks for an empty board
			flag = 0;
		}
		else {
			//checks if there is any nulls
			for (int i = 0; i < board.length; i++) {
				if (board[i] == null) {
					flag = 0;
					break;
				}
			}

			if (flag != 0) {
				//iterate through the board

				if  (is_square(board) == 0) {
					//checks if its square
					flag = 0;
				}

				else {
					for (int i = 0; i < board.length; i++) {
						char [] each_row = board[i];//gets each row
			
						for (int j = 0; j < each_row.length && flag == 1; j++) {
							//each character in a row
							if (valid_square(each_row[j]) == 0) {
								//checks if each character is caps and in alphabet
								flag = 0;
								break;
							}
						}
					}		
				}
			}
		}
		return flag;
	}

	//___QUESTION FOUR
	public static int valid_dictionary(char[][] board, String[] dictionary) {
		int flag = 1;

		if (dictionary == null) {
			flag = 0; //checks if null
		}

		else if (dictionary.length == 0) {
			//checks if the dictionary is empty
			flag = 0;
		}

		else {
			//iterate through the dictionary
			for (int i = 0; i < dictionary.length; i++) {

				//checks if each word is valid
				if (valid_word(dictionary[i]) == 0) {
					flag = 0;
					break;
				}

				//checks if the word isn't too long for the board
				else if (dictionary[i].length() > board.length) {
					flag = 0;
					break;
				}
			}

		}

		return flag;
	}

	//___QUESTION FIVE
	public static int locate_words(String word, String [] dictionary) {
		int flag = 1;

		if (valid_word(word) == 0) {
			flag = 0;
		}

		else if (dictionary == null) {
			flag = 0;
		}

		else if (dictionary.length <= 0) {
			flag = 0;
		}

		else {
			flag = 0;
			for (String each_word : dictionary) {
				if (word.equals(each_word)) {
					flag = 1;
				}
			}
		}

		return flag;
	}

	//___QUESTION SIX
	private static boolean check_south(String word, char[][] board,int row, int col) {
		if (row + word.length() > board.length) return false;

		for (int i = 0; i < word.length(); i++) {				
			if (board[row + i][col] != word.charAt(i)) {
				return false;
			}
		}

		return true;
	}

	private static boolean check_east(String word, char[][] board,int row, int col) {
		if (col + word.length() > board.length) return false;

		for (int i = 0; i < word.length(); i++) {				
			if (board[row][col + i] != word.charAt(i)) {
				return false;
			}
		}

		return true;
	}

	private static boolean check_south_east(String word, char[][] board,int row, int col) {
		if (row + word.length() > board.length) return false;
		if (col + word.length() > board.length) return false;

		for (int i = 0; i < word.length(); i++) {				
			if (board[row + i][col + i] != word.charAt(i)) {
				return false;
			}
		}

		return true;
	}

	private static boolean check_south_west(String word, char[][] board,int row, int col) {
		if (row + word.length() > board.length) return false;
		if (col - (word.length() - 1) < 0) return false;

		for (int i = 0; i < word.length(); i++) {				
			if (board[row + i][col - i] != word.charAt(i)) {
				return false;
			}
		}

		return true;

	}	

	public static int find_words(String word, char[][] board, int[] cords) {
		int flag; //1 = south, 2 = south east, 3 = south west, 4 = east, 0 = invalid

		if (valid_word(word) == 0) {
			flag = 0;
		}

		else if (valid_board(board) == 0) {
			flag = 0;
		}

		else {
			int row = cords[0]; //top to bottom
			int col = cords[1]; //left to right

			if (check_south(word, board, row,col)) {
				flag = 1;
			}

			else if (check_south_east(word, board, row,col)) {
				flag = 2;
			}

			else if (check_south_west(word, board, row,col)) {
				flag = 3;
			}

			else if (check_east(word, board, row,col)) {
				flag = 4;
			}

			else {
				flag = 0; //not found
			}
		}
		return flag;
	}

	//___QUESTION SEVEN
	public static int[] count_dictionary(char[][] board, String [] dictionary) {
		int [] count = new int[dictionary.length];

		for (int word_index = 0; word_index < dictionary.length; word_index++) {
			//System.out.println("the word is : " + dictionary[word_index]);

			int word_count = 0;

			for (int i = 0; i < board.length; i++) {
				//each row
				for (int j = 0; j < board[i].length; j++) {
					//each char
					int[] cords = {i,j};
					int reponse = find_words(dictionary[word_index], board, cords);

					if (reponse == 1 || reponse == 2 || reponse == 3 || reponse == 4) {
						//word has been found
						word_count += 1;
					}

				}
			}
			count[word_index] = word_count;

		}

		return count;
	}
}
