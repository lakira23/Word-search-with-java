import java.util.*;

public class FeedbackService {

    // returns the symbol feedback like "++-" 
    // + = correct colour in correct position
    // - = correct colour but wrong position
    public static String getFeedback(List<Character> code, List<Character> guess) {

        int plus = 0;
        int minus = 0;

        // copies so original list dont need to be modified
        List<Character> codeCopy = new ArrayList<>(code);
        List<Character> guessCopy = new ArrayList<>(guess);

        // first pass - check correct positions
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                plus++;

                // mark as used so it wont be counted again
                codeCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // second pass - correct colour wrong position
        for (int i = 0; i < guessCopy.size(); i++) {

            // skip already matched values
            if (guessCopy.get(i) != null && codeCopy.contains(guessCopy.get(i))) {
                minus++;

                // remove matched value to avoid double counting
                codeCopy.set(codeCopy.indexOf(guessCopy.get(i)), null);
            }
        }

        // return feedback with all + first then -
        return "+".repeat(plus) + "-".repeat(minus);
    }
    
    // returns detailed feedback as numbers instead of symbols
    // [0] = correct position, [1] = correct colour wrong position
    public static int[] getDetailedFeedback(List<Character> code, List<Character> guess) {

        int plus = 0;
        int minus = 0;

        // same logic as above but returns numbers instead
        List<Character> codeCopy = new ArrayList<>(code);
        List<Character> guessCopy = new ArrayList<>(guess);

        // correct position
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                plus++;
                codeCopy.set(i, null);
                guessCopy.set(i, null);
            }
        }

        // correct colour wrong position
        for (int i = 0; i < guessCopy.size(); i++) {
            if (guessCopy.get(i) != null && codeCopy.contains(guessCopy.get(i))) {
                minus++;
                codeCopy.set(codeCopy.indexOf(guessCopy.get(i)), null);
            }
        }

        return new int[]{plus, minus};
    }
}