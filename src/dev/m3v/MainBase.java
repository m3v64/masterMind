package dev.m3v;
import java.util.Scanner;

public class MainBase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int numberOfColors = 4;
        int index = 0;
        int turn = 0;
        boolean gameOver = false;

        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue", "Space"};
        String hints[] = new String[numberOfColors];
        boolean usedAnswer[] = new boolean[numberOfColors];
        boolean usedGuess[] = new boolean[numberOfColors];
        String answer[] = new String[numberOfColors];

        while (index < numberOfColors) {
            int rand = (int) (Math.random() * 7);
            answer[index] = colors[rand];
            index++;
        }

        while (!gameOver) {
            if (turn == 0) {
                System.out.println("Welcome to Mastermind!");
                System.out.println("You have 10 turns to guess the correct sequence.");
                System.out.println("The colors are: Red, Green, Yellow, Orange, Purple, Blue and Space");
                System.out.println("when the game starts choose "+numberOfColors+" colors (separated by a space, for example: Red Green Blue Yellow): ");
                System.out.println("Each round you get "+numberOfColors+" hints: 'black' means you have a correct color in the correct position, 'white' means you have a correct color in the wrong position and 'none' means that color is not in the sequence.");
                System.out.println("Good luck!");
            }

            try {
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
            } else {
                System.out.println("your hints are: " + String.join(" ", hints));
            }

            turn++;
        }

        scanner.close();
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