import swiftbot.*;
import java.util.List;

public class SwiftBotFeedback {

    // reference to the robot
    private SwiftBotAPI swiftBot;

    public SwiftBotFeedback(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    // reacts to feedback using colours
    // green = correct position, yellow = correct colour wrong place, red = no match
    public void react(String feedback) throws InterruptedException {

        if (feedback.contains("+")) {
            swiftBot.fillUnderlights(new int[]{0,255,0}); // correct positions
        } else if (feedback.contains("-")) {
            swiftBot.fillUnderlights(new int[]{255,255,0}); // correct colour wrong place
        } else {
            swiftBot.fillUnderlights(new int[]{255,0,0}); // no matches
        }

        Thread.sleep(800); // keep light on briefly
        swiftBot.disableUnderlights();
    }

    // called when player wins
    public void win() throws InterruptedException {
        swiftBot.fillUnderlights(new int[]{0,255,0}); // green
        swiftBot.move(100,-100,1000); // small spin movement
    }

    // called when player loses
    public void lose() throws InterruptedException {
        swiftBot.fillUnderlights(new int[]{255,0,0}); // red
        swiftBot.move(-50,-50,1000); // move backwards
    }
    
    // shows a single colour using underlights
    public void showColour(char c) throws InterruptedException {

        int[] colour;

        // map character to RGB value
        switch (c) {
            case 'R': colour = new int[]{255, 0, 0}; break;
            case 'G': colour = new int[]{0, 255, 0}; break;
            case 'B': colour = new int[]{0, 0, 255}; break;
            case 'Y': colour = new int[]{255, 255, 0}; break;
            case 'O': colour = new int[]{255, 165, 0}; break;
            case 'P': colour = new int[]{255, 105, 180}; break;
            default:  colour = new int[]{255, 255, 255}; // fallback
        }

        swiftBot.fillUnderlights(colour);
        Thread.sleep(400);
        swiftBot.disableUnderlights();
    }
    
    // replays the full guessed sequence user entered using underlights
    public void showSequence(List<Character> guess) throws InterruptedException {

        Thread.sleep(500); // small delay before playback

        for (char c : guess) {
            showColour(c);
            Thread.sleep(200); // spacing between colours
        }
    }
}