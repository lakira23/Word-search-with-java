
import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import swiftbot.*;

public class FinalNoughtsAndCrosses {

    static SwiftBotAPI robot;
    static char[][] board = new char[3][3];

    static Scanner input = new Scanner(System.in);
    static Random rand = new Random();

    static String userName;
    static String botName = "SwiftBot";

    static int userScore = 0;
    static int botScore = 0;
    static int round = 1;

    public static void run_noughts_and_crosses(String[] args) {

        displayWelcome();

        System.out.println("Starting game...");

        getUserName();

        boolean playAgain = true;

        while (playAgain) {

            initializeBoard();
            playRound();

            System.out.println("Play again? (Y/N)");

            String choice = input.nextLine();

            if (choice.equalsIgnoreCase("N")) {
                playAgain = false;
            }

            round++;
        }

        System.out.println("Game Over");
    }

    public static void displayWelcome() {
        System.out.println("=== Noughts and Crosses ===");
    }

    public static void getUserName() {

        System.out.println("Enter your name:");
        userName = input.nextLine();
    }

    public static void initializeBoard() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                board[i][j] = '-';

            }
        }
    }

    public static void playRound() {

        boolean gameWon = false;

        int userRoll = rollDice();
        int botRoll = rollDice();

        System.out.println(userName + " rolled: " + userRoll);
        System.out.println(botName + " rolled: " + botRoll);

        boolean userTurn = userRoll >= botRoll;

        while (!gameWon && !checkDraw()) {

            displayBoard();

            if (userTurn) {

                int[] move = getUserMove();
                int row = move[0];
                int col = move[1];

                System.out.println("[" + userName + " - O] moved to [" + row + "," + col + "]");

                gameWon = checkWin('O');

                if (gameWon) {

                    userScore++;

                    System.out.println(userName + " wins!");
                    traceWinningLine();
                }

            } else {

                int[] move = getBotMove();
                int row = move[0];
                int col = move[1];

                System.out.println("[SwiftBot - X] moved to [" + row + "," + col + "]");

                moveSwiftBot();

                gameWon = checkWin('X');

                if (gameWon) {

                    botScore++;

                    System.out.println(botName + " wins!");
                    traceWinningLine();
                }
            }

            userTurn = !userTurn;
        }

        if (!gameWon) {

            System.out.println("Game is a draw!");
            drawMove();
        }

        saveLog();
    }

    public static void displayBoard() {

        System.out.println();

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                System.out.print(board[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println();
    }

    public static int[] getUserMove() {

        while (true) {

            System.out.println("Enter move [row,column] (0-2):");

            String move = input.nextLine();

            try {

                String[] parts = move.split(",");

                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (row >= 0 && row < 3 && col >= 0 && col < 3) {

                    if (board[row][col] == '-') {

                        board[row][col] = 'O';

                        return new int[]{row, col};

                    } else {

                        System.out.println("Square already taken.");
                    }

                } else {

                    System.out.println("Invalid position.");
                }

            } catch (Exception e) {

                System.out.println("Invalid format. Use row,column");
            }
        }
    }

    public static int[] getBotMove() {

        while (true) {

            int row = rand.nextInt(3);
            int col = rand.nextInt(3);

            if (board[row][col] == '-') {

                board[row][col] = 'X';

                return new int[]{row, col};
            }
        }
    }

    public static int rollDice() {

        return rand.nextInt(6) + 1;
    }

    public static boolean checkWin(char symbol) {

        for (int i = 0; i < 3; i++) {

            if (board[i][0] == symbol &&
                board[i][1] == symbol &&
                board[i][2] == symbol)
                return true;
        }

        for (int j = 0; j < 3; j++) {

            if (board[0][j] == symbol &&
                board[1][j] == symbol &&
                board[2][j] == symbol)
                return true;
        }

        if (board[0][0] == symbol &&
            board[1][1] == symbol &&
            board[2][2] == symbol)
            return true;

        if (board[0][2] == symbol &&
            board[1][1] == symbol &&
            board[2][0] == symbol)
            return true;

        return false;
    }

    public static boolean checkDraw() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (board[i][j] == '-') {

                    return false;
                }
            }
        }

        return true;
    }

    public static void moveSwiftBot() {

        try {

            System.out.println("SwiftBot moving...");

            robot.move(50, 50, 1000);

            blinkGreen();

            robot.move(-50, -50, 1000);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void blinkGreen() {

        int[] green = {0, 255, 0};

        try {

            for (int i = 0; i < 3; i++) {

                robot.fillUnderlights(green);

                Thread.sleep(300);

                robot.disableUnderlights();

                Thread.sleep(300);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void drawMove() {

        int[] blue = {0, 0, 255};

        try {

            robot.fillUnderlights(blue);

            robot.move(50, -50, 1500);

            robot.disableUnderlights();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void traceWinningLine() {

        try {

            robot.move(50, 50, 1500);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void saveLog() {

        try {

            FileWriter writer = new FileWriter("game_log.txt", true);

            writer.write("Round: " + round + "\n");
            writer.write("User: " + userName + "\n");
            writer.write("User Score: " + userScore + "\n");
            writer.write("Bot Score: " + botScore + "\n");
            writer.write("Date: " + LocalDateTime.now() + "\n");
            writer.write("------------------------\n");

            writer.close();

        } catch (IOException e) {

            System.out.println("Error saving log file");
        }
    }
}

