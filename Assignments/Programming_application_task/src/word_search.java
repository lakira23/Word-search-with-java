
public class word_search {

	public static void main(String[] args) {
		System.out.println("test2");

	}
	
	public static int valid_sequence(String word) {
		int flag = 1;
		for (int i = 0; i < word.length(); i++) {
			String current_letter = String.valueOf(word.charAt(i));
			if (current_letter != current_letter.toUpperCase()) {
				//the letter is not capital
				flag = 0;
			}
		}
		
		return flag;
	}

}
