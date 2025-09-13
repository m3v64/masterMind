package dev.m3v;
import java.util.Arrays;
import java.util.Scanner;

public class MainBase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numberOfColors = 4;
        int index = 0;
        int turn = 0;
        int maxTurns = 10;
        boolean gameOver = false;

        clearTerminal();
        
        System.out.println("Welcome to Mastermind!");
        System.out.println("You have 10 turns to guess the correct sequence.");
        System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
        System.out.println("when the game starts choose "+numberOfColors+" colors (separated by a space, for example: Red Green Blue Yellow): ");
        System.out.println("Each round you get "+numberOfColors+" hints: 'black' means you have a correct color in the correct position, 'white' means you have a correct color in the wrong position and 'none' means that color is not in the sequence.");
        System.out.println("there are 4 difficulties: easy (4 colors, 12 turns), normal (6 colors, 10 turns), hard (8 colors, 8 turns), very hard (10 colors, 6 turns)");
        System.out.print("What difficulty do you want to play?: ");

        String difficulty = scanner.nextLine().toLowerCase();
        switch (difficulty) {
            case "easy":
                numberOfColors = 4;
                maxTurns = 12;
                break;
            case "normal":
                numberOfColors = 6;
                maxTurns = 10;
                break;
            case "hard":
                numberOfColors = 8;
                maxTurns = 8;
                break;
            case "very hard":
                numberOfColors = 10;
                maxTurns = 6;
                break;
            default:
                System.out.println("Invalid difficulty, defaulting to normal.");
                numberOfColors = 6;
                maxTurns = 10;
                break;
        }
        System.out.println("Good luck!");
        System.out.println("Press Enter to start the game...");
        scanner.nextLine();

        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue"};
        String hints[] = new String[numberOfColors];
        String answer[] = new String[numberOfColors];
        boolean usedAnswer[] = new boolean[numberOfColors];
        boolean usedGuess[] = new boolean[numberOfColors];
        String[] historyGuesses = new String[maxTurns];
        String[] historyHints = new String[maxTurns];

        while (index < numberOfColors) {
            int rand = (int) (Math.random() * 6);
            answer[index] = colors[rand];
            index++;
        }

        while (!gameOver) {
            Arrays.fill(hints, null);
            Arrays.fill(usedAnswer, false);
            Arrays.fill(usedGuess, false);

            try {
                clearTerminal();
                if (turn > 0) {
                    System.out.println("The possible colors are: Red, Green, Yellow, Orange, Purple and Blue");
                    System.out.println("Previous moves:");
                    for (int i = 0; i < turn; i++) {
                        System.out.println((i + 1) + ". Guess: " + historyGuesses[i] + "  |  Hints: " + historyHints[i]);
                    }
                    System.out.println();
                }
                System.out.println("choose "+numberOfColors+" colors: ");
                String input = scanner.nextLine();
                String[] inputColors = input.split(" ");

                for (int i = 0; i < numberOfColors; i++) {
                    if (inputColors[i].equalsIgnoreCase(answer[i])) {
                        hints[i] = "black";
                        usedAnswer[i] = true;
                        usedGuess[i] = true;
                    }
                }

                for (int i = 0; i < numberOfColors; i++) {
                    if (usedGuess[i]) continue;
                    for (int j = 0; j < numberOfColors; j++) {
                        if (!usedAnswer[j] && inputColors[i].equalsIgnoreCase(answer[j])) {
                            hints[i] = "white";
                            usedAnswer[j] = true;
                            usedGuess[i] = true;
                            break;
                        }
                    }
                    if (!usedGuess[i]) {
                        hints[i] = "none";
                    }

                    historyGuesses[turn] = String.join(" ", inputColors);
                    historyHints[turn] = String.join(" ", hints);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter "+numberOfColors+" colors separated by spaces.");
                continue;
            }

            if (printHintsAndCheckWin(hints)) {
                System.out.println("you have won! the correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            } else if (turn == 9) {
                System.out.println("you have lost! the correct sequence was: " + String.join(" ", answer));
                gameOver = true;
            } else {
                System.out.println("your hints are: " + String.join(" ", hints));
            }

            turn++;
        }

        scanner.close();
    }

    public static void clearTerminal() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static boolean printHintsAndCheckWin(String[] hints) {
        for (String hint : hints) {
            if (!"black".equals(hint)) {
                return false;
            }
        }
        return true;
    }
}