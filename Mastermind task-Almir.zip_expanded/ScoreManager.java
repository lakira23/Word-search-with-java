public class ScoreManager {

    // keeps track of scores for both sides
    private int playerScore = 0;
    private int computerScore = 0;

    // called when player wins a game
    public void playerWins() {
        playerScore++;
    }

    // called when computer wins (player loses)
    public void computerWins() {
        computerScore++;
    }

    // returns score in simple format like "1-0"
    public String getScore() {
        return playerScore + "-" + computerScore;
    }
}