package dev.m3v;
import java.util.Scanner;

public class MainBase {
    // ik heb totaal niet alle comments ge autocomplete met vscode... 100%...
    public static void main(String[] args) {
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Game variables
        int codeLength = 4;
        int turn = 0;
        int maxTurns = 10;
        boolean gameOver = false;
        boolean solveMode = false;

        // Clear the terminal at the start
        clearTerminal(); 
        
        // Game instructions
        System.out.println("Welcome to Mastermind!");
        System.out.println("You have a number of turns to guess the correct sequence.");
        System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
        System.out.println("when the gameturn starts choose "+codeLength+" colors (separated by a space, for example: Red Green Blue Yellow)");
        System.out.println("Each round you get "+codeLength+" hints: 'black' means you have a correct color in the correct position, 'white' means you have a correct color in the wrong position and 'none' means that color is not in the sequence.");
        System.out.println("there are 4 difficulties: easy (4 colors, 12 turns), normal (6 colors, 10 turns), hard (8 colors, 8 turns), very hard (10 colors, 6 turns)");
        System.out.println("So good luck!");
        System.out.print("What difficulty do you want to play?: ");
        String difficulty = scanner.nextLine().toLowerCase();
        
        // Set difficulty
        clearTerminal();
        switch (difficulty) {
            case "easy":
                System.out.println("you have chosen easy mode, lets take it nice and slow.");
                codeLength = 4;
                maxTurns = 12;
                break;

            case "normal":
                System.out.println("you have chosen normal mode, a good balance between challenge and fun.");
                codeLength = 6;
                maxTurns = 10;
                break;

            case "hard":
                System.out.println("you have chosen hard mode, brace yourself for a challenge!");
                codeLength = 8;
                maxTurns = 8;
                break;

            case "very hard":
                System.out.println("you have chosen very hard mode, this gonna be fun!");
                codeLength = 10;
                maxTurns = 6;
                break;

            case "solver":
                System.out.println("you have chosen solver mode, why play when your computer can do it for you?");
                codeLength = 4;
                maxTurns = 6;
                solveMode = true;
                break;

            default:
                System.out.println("Invalid difficulty, defaulting to normal.");
                codeLength = 6;
                maxTurns = 10;
                break;
        }

        // Start the gameturn
        System.out.println("Press Enter to start the game...");
        scanner.nextLine();
        clearTerminal();

        // Initialize gameturn arrays
        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue"};
        String answer[] = new String[codeLength];
        String hints[] = new String[codeLength];
        boolean usedAnswer[] = new boolean[codeLength];
        boolean usedGuess[] = new boolean[codeLength];
        String historyGuesses[] = new String[maxTurns];
        String historyHints[] = new String[maxTurns];

        // initialize array dependant variables
        int numberOfColors = colors.length;

        // Generate random answer
        for (int i = 0; i < codeLength; i++) {
            int rand = (int) (Math.random() * numberOfColors);
            answer[i] = colors[rand];
        }

        // Main gameturn loop
        while (!gameOver) {
            // Reset hints and used arrays
            for (int i = 0; i < hints.length; i++) {
                hints[i] = null;
            }

            for (int i = 0; i < usedAnswer.length; i++) {
                usedAnswer[i] = false;
                usedGuess[i] = false;
            }

            // Play a turn
            if (solveMode) {
                solver(answer, maxTurns, codeLength, numberOfColors, turn);
                gameOver = true;
                continue;
            } else if (!gameTurn(turn, codeLength, answer, colors, scanner, hints, usedAnswer, usedGuess, solveMode, historyGuesses, historyHints)) {
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

    private static void displayHints(String[] historyGuesses, String[] historyHints, boolean validInput, int turn) {
        if (!validInput) {
            for (int i = 0; i <= turn && i < historyGuesses.length; i++) {
                if (historyGuesses[i] != null) {
                    System.out.println((i + 1) + ". Guess: " + historyGuesses[i] + "  |  Hints: invalid input");
                }
            }
            System.out.println();
            return;
        }

        System.out.println("Previous moves:");
        for (int i = 0; i <= turn && i < historyGuesses.length; i++) {
            if (historyGuesses[i] != null && historyHints[i] != null) {
                System.out.println((i + 1) + ". Guess: " + historyGuesses[i] + "  |  Hints: " + historyHints[i]);
            }
        }
        System.out.println();
    }

    // Safely join array elements, replacing nulls with "none"
    public static String safeJoin(String[] hints) {
        if (hints == null) {
            return "";
        }

        String result = "";
        for (int i = 0; i < hints.length; i++) {
            String element = hints[i];
            if (element == null) {
                element = "none";
            }

            if (i == 0) {
                result = element; 
            } else {
                result = String.format("%s %s", result, element);
            }
        }
        return result;
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

    private static boolean gameTurn(int turn, int codeLength, String[] answer, String[] colors, Scanner scanner, String[] hints, boolean[] usedAnswer, boolean[] usedGuess,boolean solveMode, String[] historyGuesses, String[] historyHints) {
        // Step 0: Get input
        System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
        System.out.println("choose "+codeLength+" colors: ");
        String input = scanner.nextLine();
        String[] inputColors = input.split(" ");
        boolean validInput = true;

        // Check for valid input
        for (int i = 0; i < inputColors.length; i++) {
            if (inputColors.length != codeLength) {
                clearTerminal();
                historyGuesses[turn] = String.join(" ", inputColors);
                historyHints[turn] = safeJoin(hints);
                validInput = false;
                System.out.println("Invalid input, enter " + codeLength + " colors.");
                return false;
            } else if (inputColors[i].equalsIgnoreCase(colors[0]) || inputColors[i].equalsIgnoreCase(colors[1]) || inputColors[i].equalsIgnoreCase(colors[2]) || inputColors[i].equalsIgnoreCase(colors[3]) || inputColors[i].equalsIgnoreCase(colors[4]) || inputColors[i].equalsIgnoreCase(colors[5])) {
                continue;
            } else {
                clearTerminal();
                historyGuesses[turn] = String.join(" ", inputColors);
                historyHints[turn] = safeJoin(hints);
                validInput = false;
                System.out.println("Invalid input, enter " + codeLength + " colors.");
                return false;
            }
        }

        // Display previous moves
        if (turn > 0) {
            clearTerminal();
            displayHints(historyGuesses, historyHints, validInput, turn);
        }

        // Step 1: black hints
        for (int i = 0; i < codeLength; i++) {
            if (inputColors[i].equalsIgnoreCase(answer[i])) {
                hints[i] = "black";
                usedAnswer[i] = true;
                usedGuess[i] = true;
            }
        }

        // Step 2: white hints
        for (int i = 0; i < codeLength; i++) {
            if (!usedGuess[i]) {
                for (int j = 0; j < codeLength; j++) {
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
        for (int i = 0; i < codeLength; i++) {
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

    public static int[] getFeedback(int[] guess, int[] secret) {
        int length = guess.length;
        boolean[] guessUsed = new boolean[length];
        boolean[] secretUsed = new boolean[length];
        int blacks = 0;
        int whites = 0;

        // Step 1: count blacks
        for (int i = 0; i < length; i++) {
            if (guess[i] == secret[i]) {
                blacks++;
                guessUsed[i] = true;
                secretUsed[i] = true;
            }
        }

        // Step 2: count whites
        for (int i = 0; i < length; i++) {
            if (guessUsed[i]) continue;
            for (int j = 0; j < length; j++) {
                if (secretUsed[j]) continue;
                if (guess[i] == secret[j]) {
                    whites++;
                    guessUsed[i] = true;
                    secretUsed[j] = true;
                    break; // move on to next guess peg
                }
            }
        }

        return new int[]{blacks, whites};
    }

    public static void solver(String[] answer, int maxTurns, int codeLength, int numberOfColors, int turn) {
        // Initialize... everything...
        int totalSolutions = (int) Math.pow(numberOfColors, codeLength);
        boolean solveOver = false;
        int possibleAnswers[][] = new int[codeLength][totalSolutions];
        boolean checkedGuess[] = new boolean[codeLength];
        boolean checkedAnswer[] = new boolean[codeLength];
        String solverHints[] = new String[codeLength];
        int solverInput[] = new int[codeLength];
        int answerInt[] = new int[codeLength];
        int[] actualFeedback = getFeedback(solverInput, answerInt);
        int blacks = actualFeedback[0];
        int whites = actualFeedback[1];

        // Step 0: Convert answer to int array
        for (int i = 0; i < codeLength; i++) {
            for (int j = 0; j < numberOfColors; j++) {
                if (answer[i].equalsIgnoreCase("Red")) {
                    answerInt[i] = 0;
                } else if (answer[i].equalsIgnoreCase("Green")) {
                    answerInt[i] = 1;
                } else if (answer[i].equalsIgnoreCase("Yellow")) {
                    answerInt[i] = 2;
                } else if (answer[i].equalsIgnoreCase("Orange")) {
                    answerInt[i] = 3;
                } else if (answer[i].equalsIgnoreCase("Purple")) {
                    answerInt[i] = 4;
                } else if (answer[i].equalsIgnoreCase("Blue")) {
                    answerInt[i] = 5;
                }
            }
        }

        // Step 1: Generate all possible combinations of colors
        for (int col = 0; col < totalSolutions; col++) {
            int num = col;
            for (int row = codeLength - 1; row >= 0; row--) {
                possibleAnswers[row][col] = num % numberOfColors;
                num /= numberOfColors;
            }
        }
        
        // Step 2: Start with guessing 1122 (green, green, yellow, yellow)
        // if (turn == 0) {
            
        // }

        while (!solveOver) {
            // Step 3: play the guess and get hints
            for (int i = 0; i < codeLength; i++) {
                if (solverInput[i] == answerInt[i]) {
                    solverHints[i] = "black";
                }
            }

            for (int i = 0; i < codeLength; i++) {
                if (!checkedGuess[i]) {
                    for (int j = 0; j < codeLength; j++) {
                        if (!checkedAnswer[j] && solverInput[i] == answerInt[j]) {
                            solverHints[i] = "white";
                            checkedGuess[i] = true;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < codeLength; i++) {
                if (solverHints[i] == null) {
                    solverHints[i] = "none";
                }
            }

            // Step 4: exit if won
            if (checkWin(solverHints)) {
                System.out.println("you have won! the correct sequence was: " + String.join(" ", answer));
                solveOver = true;
            } else if (turn == maxTurns - 1) {
                System.out.println("you have lost! the correct sequence was: " + String.join(" ", answer));
                solveOver = true;
            }

            // Step 5: eliminate impossible answers
            for (int col = 0; col < totalSolutions; col++) {
                if (possibleAnswers[0][col] == -1) continue;

                int[] candidate = new int[codeLength];
                for (int i = 0; i < codeLength; i++) {
                    candidate[i] = possibleAnswers[i][col];
                }

                int[] feedback = getFeedback(solverInput, candidate);

                if (feedback[0] != blacks || feedback[1] != whites) {
                    for (int i = 0; i < codeLength; i++) {
                        possibleAnswers[i][col] = -1;
                    }
                }
            }

            // Debug
            for (int col = 0; col < totalSolutions; col++) {
                if (possibleAnswers[0][col] == -1) continue; // skip eliminated

                System.out.print("Candidate " + col + ": ");
                for (int row = 0; row < codeLength; row++) {
                    System.out.print(possibleAnswers[row][col] + " ");
                }
                System.out.println();
            }
        }
    }
}