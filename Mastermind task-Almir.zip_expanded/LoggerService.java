import java.io.FileWriter;
import java.io.IOException;

public class LoggerService {

    // file where all game logs are stored
    private String filePath = "game_log.txt";

    //writes a single line to the log file
    public void log(String message) {

        try (FileWriter fw = new FileWriter(filePath, true)) {

            //append message to file
            fw.write(message + "\n");

        } catch (IOException e) {

            // error message if handling fails
            System.out.println("Logging error!");
        }
    }

    // returns file path to be shown to user
    public String getFilePath() {
        return filePath;
    }
}