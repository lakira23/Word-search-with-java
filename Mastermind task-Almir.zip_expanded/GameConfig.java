public class GameConfig {
	// ended up choosing to use direct variables in the final implementation, this is just design idea
    // stores settings for a game session
    private int codeLength;
    private int maxAttempts;

    // constructor to initialise config values
    public GameConfig(int codeLength, int maxAttempts) {
        this.codeLength = codeLength;
        this.maxAttempts = maxAttempts;
    }

    // returns how many colours are in the code
    public int getCodeLength() {
        return codeLength;
    }

    // returns how many attempts the player has
    public int getMaxAttempts() {
        return maxAttempts;
    }
}