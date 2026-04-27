import java.util.*;

public class CodeGenerator {

    // available colours for the game
    private static final char[] COLOURS = {'R','G','B','Y','O','P'};

    // generates a random code with no repeating colours
    public static List<Character> generateCode(int length) {

        List<Character> list = new ArrayList<>();

        // add all possible colours into a list
        for (char c : COLOURS) {
            list.add(c);
        }

        // randomise order of list
        Collections.shuffle(list);

        // take only the required number of colours
        return list.subList(0, length);
    }
}