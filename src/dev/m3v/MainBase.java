package dev.m3v;
import java.util.Arrays;
import java.util.Scanner;

public class MainBase {
    // ik heb totaal niet alle comments ge autocomplete met vscode... 100%...
    public static void main(String[] args) {
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Game variables
        int numberOfColors = 4;
        int turn = 0;
        int maxTurns = 10;
        boolean gameOver = false;
        boolean solveMode = false;

        clearTerminal(); // Clear the terminal at the start
        
        // Game instructions
        System.out.println("Welcome to Mastermind!");
        System.out.println("You have a number of turns to guess the correct sequence.");
        System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
        System.out.println("when the game starts choose "+numberOfColors+" colors (separated by a space, for example: Red Green Blue Yellow)");
        System.out.println("Each round you get "+numberOfColors+" hints: 'black' means you have a correct color in the correct position, 'white' means you have a correct color in the wrong position and 'none' means that color is not in the sequence.");
        System.out.println("there are 4 difficulties: easy (4 colors, 12 turns), normal (6 colors, 10 turns), hard (8 colors, 8 turns), very hard (10 colors, 6 turns)");
        System.out.print("What difficulty do you want to play?: ");
        String difficulty = scanner.nextLine().toLowerCase();
        
        // Set difficulty
        clearTerminal();
        switch (difficulty) {
            case "easy":
                System.out.println("you have chosen easy mode, lets take it nice and slow.");
                numberOfColors = 4;
                maxTurns = 12;
                break;
            case "normal":
                System.out.println("you have chosen normal mode, a good balance between challenge and fun.");
                numberOfColors = 6;
                maxTurns = 10;
                break;
            case "hard":
                System.out.println("you have chosen hard mode, brace yourself for a challenge!");
                numberOfColors = 8;
                maxTurns = 8;
                break;
            case "very hard":
                System.out.println("you have chosen very hard mode, this gonna be fun!");
                numberOfColors = 10;
                maxTurns = 6;
                break;
            case "solver":
                System.out.println("you have chosen solver mode, why play when your computer can do it for you?");
                numberOfColors = 4;
                maxTurns = 6;
                solveMode = true;
                break;
            default:
                System.out.println("Invalid difficulty, defaulting to normal.");
                numberOfColors = 6;
                maxTurns = 10;
                break;
        }

        // Start the game
        System.out.println("Good luck!");
        System.out.println("Press Enter to start the game...");
        scanner.nextLine();
        clearTerminal();

        // Initialize game arrays
        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue"};
        String answer[] = new String[numberOfColors];
        String hints[] = new String[numberOfColors];
        boolean usedAnswer[] = new boolean[numberOfColors];
        boolean usedGuess[] = new boolean[numberOfColors];
        String historyGuesses[] = new String[maxTurns];
        String historyHints[] = new String[maxTurns];

        // Generate random answer
        for (int i = 0; i < numberOfColors; i++) {
            int rand = (int) (Math.random() * 6);
            answer[i] = colors[rand];
        }

        // Main game loop
        while (!gameOver) {
            // Reset hints and used arrays
            Arrays.fill(hints, null);
            Arrays.fill(usedAnswer, false);
            Arrays.fill(usedGuess, false);

            // Play a turn
            if (!game(turn, numberOfColors, answer, colors, scanner, hints, usedAnswer, usedGuess, solveMode, historyGuesses, historyHints)) {
                continue;
            } else {
                turn++;
            }
            System.out.println("Turn " + turn + " of " + maxTurns);

            // Check for win/loss
            if (checkWin(hints)) {
                System.out.println("you have won! the correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            } else if (turn == maxTurns - 1) {
                System.out.println("you have lost! the correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            }
        }
        scanner.close();
    }

    // Simple terminal clear by printing new lines
    public static void clearTerminal() {
        for (int i = 0; i < 50; i++) {
            System.out.println("");
        }
    }

    public static void displayHints(String[] historyGuesses, String[] historyHints, boolean validInput, int turn) {
        if (!validInput) {
            for (int i = 0; i <= turn; i++) {
                if (historyGuesses[i] != null) {
                    System.out.println((i + 1) + ". Guess: " + historyGuesses[i] + "  |  Hints: invalid input");
                }
            }
            System.out.println();
            return;
        }
        System.out.println("Previous moves:");
        for (int i = 0; i <= turn; i++) {
            if (historyGuesses[i] != null && historyHints[i] != null) {
                System.out.println((i + 1) + ". Guess: " + historyGuesses[i] + "  |  Hints: " + historyHints[i]);
            }
        }
        System.out.println();
    }

    // Print hints and check for win condition
    private static boolean checkWin(String[] hints) {
        for (String hint : hints) {
            if (!"black".equalsIgnoreCase(hint)) {
                return false;
            }
        }
        return true;
    }

    private static boolean game(int turn, int numberOfColors, String[] answer, String[] colors, Scanner scanner, String[] hints, boolean[] usedAnswer, boolean[] usedGuess,boolean solveMode, String[] historyGuesses, String[] historyHints) {
        // Step 0: Get input
        System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
        System.out.println("choose "+numberOfColors+" colors: ");
        String input = scanner.nextLine();
        String[] inputColors = input.split(" ");
        boolean validInput = true;

        for (int i = 0; i < inputColors.length; i++) {
            if (inputColors[i].equalsIgnoreCase(colors[0]) || inputColors[i].equalsIgnoreCase(colors[1]) || inputColors[i].equalsIgnoreCase(colors[2]) || inputColors[i].equalsIgnoreCase(colors[3]) || inputColors[i].equalsIgnoreCase(colors[4]) || inputColors[i].equalsIgnoreCase(colors[5])) {
                continue;
            } else if (inputColors.length != numberOfColors) {
                clearTerminal();
                historyGuesses[turn] = String.join(" ", inputColors);
                historyHints[turn] = String.join(" ", hints);
                validInput = false;
                System.out.println("Invalid input, enter " + numberOfColors + " colors.");
                return false;
            } else {
                clearTerminal();
                historyGuesses[turn] = String.join(" ", inputColors);
                historyHints[turn] = String.join(" ", hints);
                validInput = false;
                System.out.println("Invalid input, enter " + numberOfColors + " colors.");
                return false;
            }
        }

        // Display previous moves
        if (turn > 0) {
            clearTerminal();
            displayHints(historyGuesses, historyHints, validInput, turn);
        }

        // Step 1: black hints
        for (int i = 0; i < numberOfColors; i++) {
            if (inputColors[i].equalsIgnoreCase(answer[i])) {
                hints[i] = "black";
                usedAnswer[i] = true;
                usedGuess[i] = true;
            }
        }

        // Step 2: white hints
        for (int i = 0; i < numberOfColors; i++) {
            if (!usedGuess[i]) {
                for (int j = 0; j < numberOfColors; j++) {
                    if (!usedAnswer[j] && inputColors[i].equalsIgnoreCase(answer[j])) {
                        hints[i] = "white";
                        usedAnswer[j] = true;
                        usedGuess[i] = true;
                        break;
                    }
                }
            }
        }

        // Step 3: none hints
        for (int i = 0; i < numberOfColors; i++) {
            if (hints[i] == null) {
                hints[i] = "none";
            }
        }

        // Store history
        historyGuesses[turn] = String.join(" ", inputColors);
        historyHints[turn] = String.join(" ", hints);

        // Display current hints
        clearTerminal();
        displayHints(historyGuesses, historyHints, validInput, turn);
        return true;
    }

    public static void solver(int numberOfColors) {
        // Initialize possible answers array
        int totalSolutions = (int) Math.pow(6, numberOfColors);
        int possibleAnswers[][] = new int[numberOfColors][totalSolutions];

        // Generate all possible combinations of colors
        for (int col = 0; col < totalSolutions; col++) {
            int num = col;
            for (int row = numberOfColors - 1; row >= 0; row--) {
                possibleAnswers[row][col] = num % 6;
                num /= 6;
            }
        }
        // rest of solver not implemented yet
    }
}