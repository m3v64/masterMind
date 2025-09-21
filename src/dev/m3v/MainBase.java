package dev.m3v;
import java.util.Scanner;

public class MainBase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Game variables
        int codeLength = 6;
        int turn = 0;
        int totalTurns = 0;
        int maxTurns = 6;
        int winCount = 0;
        int lossCount = 0;
        int iteration = 0;
        boolean gameOver = false;
        boolean solveMode = false;

        clearTerminal();

        System.out.println("Welcome to Mastermind!");
        System.out.println("You have a number of turns to guess the correct sequence.");
        System.out.println("When the game turn starts choose colors separated by a space, for example: Red Green Blue Yellow");
        System.out.println("Each round you get hints: 'black' = correct color in correct position, 'white' = correct color wrong position, 'none' = not in sequence.");
        System.out.println("Difficulties: easy (4 code length, 12 turns), normal (6 code length, 10 turns), hard (8 code length, 8 turns), very hard (10 code length, 6 turns)");
        System.out.print("What difficulty do you want to play?: ");
        String difficulty = scanner.nextLine().toLowerCase();

        clearTerminal();
        switch (difficulty) {
            case "easy":
                System.out.println("You chose easy.");
                codeLength = 4;
                maxTurns = 12;
                break;
            case "normal":
                System.out.println("You chose normal.");
                codeLength = 6;
                maxTurns = 10;
                break;
            case "hard":
                System.out.println("You chose hard.");
                codeLength = 8;
                maxTurns = 8;
                break;
            case "very hard":
                System.out.println("You chose very hard.");
                codeLength = 10;
                maxTurns = 6;
                break;
            case "solver":
                System.out.println("You chose solver mode.");
                solveMode = true;
                // keep codeLength/defaults
                break;
            default:
                System.out.println("Invalid difficulty, defaulting to normal.");
                codeLength = 6;
                maxTurns = 10;
                break;
        }

        // Valid colors
        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue", "Pink", "Brown", "Cyan", "Magenta"};
        int numberOfColors = colors.length;

        // Arrays initialization
        String answer[] = new String[codeLength];
        String hints[] = new String[codeLength];
        boolean usedAnswer[] = new boolean[codeLength];
        boolean usedGuess[] = new boolean[codeLength];
        String historyGuesses[] = new String[maxTurns];
        String historyHints[] = new String[maxTurns];

        // Generate random answer for playing modes
        for (int i = 0; i < codeLength; i++) {
            int rand = (int) (Math.random() * numberOfColors);
            answer[i] = colors[rand];
        }

        while (!gameOver) {
            // Reset hints & used arrays for the turn
            for (int i = 0; i < hints.length; i++) hints[i] = null;
            for (int i = 0; i < usedAnswer.length; i++) {
                usedAnswer[i] = false;
                usedGuess[i] = false;
            }

            if (solveMode) {
                // Solver configuration prompt
                System.out.println("Solver mode - configuration:");
                System.out.print("1) How many iterations? ");
                int iterations = readInt(scanner);
                System.out.print("2) What should the length of the code be? ");
                codeLength = readInt(scanner);
                System.out.print("3) How many turns should it get? ");
                maxTurns = readInt(scanner);

                while (iterations-- > 0) {
                    int turnsUsed = solver(maxTurns, codeLength, numberOfColors);
                    totalTurns = totalTurns + turnsUsed;

                    if (turnsUsed <= maxTurns) {
                        winCount++;
                    } else {
                        lossCount++;
                    }
                    iteration++;

                    double average = (double) totalTurns / iteration;
                    System.out.println("The solver has finished " + iteration + " iterations" + " with an average of " + String.format("%.2f", average) + " turns per iteration.");
                    System.out.println("Current score: " + winCount + " wins and " + lossCount + " losses.");
                }

                gameOver = true;
                continue;
            }

            // Player game turn
            boolean turnResult = gameTurn(turn, codeLength, answer, colors, scanner, hints, usedAnswer, usedGuess, historyGuesses, historyHints);
            if (!turnResult) {
                continue;
            } else {
                turn++;
            }

            System.out.println("Turn " + turn + " of " + maxTurns);

            // Check win
            if (checkWin(hints)) {
                System.out.println("You have won! The correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            } else if (turn >= maxTurns) {
                System.out.println("You have lost! The correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            }
        }

        scanner.close();
    }

    // Avoid invalid input errors
    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    public static void clearTerminal() {
        for (int i = 0; i < 10; i++) {
            System.out.println("");
        }
        System.out.println("---");
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

    public static String safeJoin(String[] hints) {
        if (hints == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hints.length; i++) {
            String element = hints[i] == null ? "none" : hints[i];
            if (i > 0) sb.append(" ");
            sb.append(element);
        }
        return sb.toString();
    }

    private static boolean checkWin(String[] hints) {
        for (String hint : hints) {
            if (!"black".equalsIgnoreCase(hint)) return false;
        }
        return true;
    }

    // Game turn
    private static boolean gameTurn(int turn, int codeLength, String[] answer, String[] colors, Scanner scanner, String[] hints, boolean[] usedAnswer, boolean[] usedGuess, String[] historyGuesses, String[] historyHints) {
        System.out.println("Possible colors (subset may be used): ");
        for (int i = 0; i < colors.length; i++) {
            System.out.print(colors[i] + (i < colors.length - 1 ? ", " : "\n"));
        }
        System.out.println("Choose " + codeLength + " colors (space separated): ");
        String input = scanner.nextLine().trim();
        String[] inputColors;
        if (input.isEmpty()) {
            inputColors = new String[0];
        } else {
            inputColors = input.split("\\s+");
        }

        boolean validInput = true;

        if (inputColors.length != codeLength) {
            clearTerminal();
            historyGuesses[turn] = String.join(" ", inputColors);
            historyHints[turn] = safeJoin(hints);
            validInput = false;
            System.out.println("Invalid input: enter exactly " + codeLength + " colors.");
            return false;
        } else {
            for (int i = 0; i < inputColors.length; i++) {
                boolean found = false;
                for (String color : colors) {
                    if (inputColors[i].equalsIgnoreCase(color)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    clearTerminal();
                    historyGuesses[turn] = String.join(" ", inputColors);
                    historyHints[turn] = safeJoin(hints);
                    validInput = false;
                    System.out.println("Invalid input: unknown color '" + inputColors[i] + "'. Enter valid color names.");
                    return false;
                }
            }
        }

        if (turn > 0) {
            clearTerminal();
            displayHints(historyGuesses, historyHints, validInput, turn - 1);
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
            if (hints[i] == null) hints[i] = "none";
        }

        // store history
        historyGuesses[turn] = String.join(" ", inputColors);
        historyHints[turn] = String.join(" ", hints);

        clearTerminal();
        displayHints(historyGuesses, historyHints, validInput, turn);

        return true;
    }

    // returns [blacks, whites]
    public static int[] getFeedback(int[] guess, int[] secret) {
        int length = guess.length;
        boolean[] guessUsed = new boolean[length];
        boolean[] secretUsed = new boolean[length];
        int blacks = 0;
        int whites = 0;

        for (int i = 0; i < length; i++) {
            if (guess[i] == secret[i]) {
                blacks++;
                guessUsed[i] = true;
                secretUsed[i] = true;
            }
        }

        for (int i = 0; i < length; i++) {
            if (guessUsed[i]) continue;
            for (int j = 0; j < length; j++) {
                if (secretUsed[j]) continue;
                if (guess[i] == secret[j]) {
                    whites++;
                    guessUsed[i] = true;
                    secretUsed[j] = true;
                    break;
                }
            }
        }

        return new int[]{blacks, whites};
    }

    public static int solver(int maxTurns, int codeLength, int numberOfColors) {
        int totalSolutions = (int) Math.pow(numberOfColors, codeLength);

        int[][] candidates = new int[totalSolutions][codeLength];
        boolean[] possible = new boolean[totalSolutions];
        for (int i = 0; i < possible.length; i++) possible[i] = true;

        int[] solverAnswer = new int[codeLength];
        for (int i = 0; i < codeLength; i++) {
            solverAnswer[i] = (int) (Math.random() * numberOfColors);
        }

        // generate all codes
        for (int col = 0; col < totalSolutions; col++) {
            int num = col;
            for (int row = codeLength - 1; row >= 0; row--) {
                candidates[col][row] = num % numberOfColors;
                num /= numberOfColors;
            }
        }

        // initial guess
        int[] guess = new int[codeLength];
        int color = 0;
        for (int i = 0; i < codeLength; i++) {
            guess[i] = color;
            if (i % 2 == 1) color = (color + 1) % numberOfColors;
        }

        for (int turn = 0; turn < maxTurns; turn++) {
            int[] feedback = getFeedback(guess, solverAnswer);

            System.out.print("Turn " + (turn + 1) + " guess=[");
            for (int i = 0; i < guess.length; i++) {
                System.out.print(guess[i] + (i < guess.length - 1 ? ", " : ""));
            }
            System.out.println("] feedback=[" + feedback[0] + " black, " + feedback[1] + " white]");

            // check win
            if (feedback[0] == codeLength) {
                System.out.println("Solved in " + (turn + 1) + " turns!");
                return turn + 1;
            }

            // eliminate impossible codes
            for (int col = 0; col < totalSolutions; col++) {
                if (!possible[col]) continue;
                int[] candidate = candidates[col];
                int[] candFeedback = getFeedback(guess, candidate);
                if (candFeedback[0] != feedback[0] || candFeedback[1] != feedback[1]) {
                    possible[col] = false;
                }
            }

            // pick next code
            guess = null;
            for (int col = 0; col < totalSolutions; col++) {
                if (possible[col]) {
                    guess = candidates[col];
                    break;
                }
            }

            if (guess == null) {
                System.out.println("No candidates left! Solver failed early.");
                return maxTurns + 1;
            }
        }

        System.out.println("Failed to solve in " + maxTurns + " turns.");
        return maxTurns + 1;
    }
}
