package dev.m3v;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue", "Space"};
        String answer[] = new String[4];
        boolean gameOver = false;
        int i = 0;
        int turn = 0;
        String result1, result2, result3, result4;

        while (i < 4) {
            int rand = (int) (Math.random() * 7);
            answer[i] = colors[rand];
            i++;
        }

        while (!gameOver) {
            if (turn == 0) {
                System.out.println("Welcome to Mastermind!");
                System.out.println("The colors are: Red, Green, Yellow, Orange, Purple, Blue and Space");
                System.out.println("Each round you get 4 hints: 'zwart' means you have a correct color in the correct position, 'wit' means you have a correct color in the wrong position and 'null' means that color is not in the sequence.");
                System.out.println("You have 10 turns to guess the correct sequence.");
                System.out.println("Good luck!");
            }

            System.out.println("alright, choose 4 colors (separated by a space, for example: Red Green Blue Yellow): ");
            String input = scanner.nextLine();
            String[] inputColors = input.split(" ");

            if (inputColors[0].equalsIgnoreCase(answer[0])) {
                result1 = "zwart";
            } else if (inputColors[0].equalsIgnoreCase(answer[1]) || inputColors[0].equalsIgnoreCase(answer[2]) || inputColors[0].equalsIgnoreCase(answer[3])) {
                result1 = "wit";
            } else {
                result1 = "null";
            }

            if (inputColors[1].equalsIgnoreCase(answer[1])) {
                result2 = "zwart";
            } else if (inputColors[1].equalsIgnoreCase(answer[0]) || inputColors[1].equalsIgnoreCase(answer[2]) || inputColors[1].equalsIgnoreCase(answer[3])) {
                result2 = "wit";
            } else {
                result2 = "null";
            }

            if (inputColors[2].equalsIgnoreCase(answer[2])) {
                result3 = "zwart";
            } else if (inputColors[2].equalsIgnoreCase(answer[0]) || inputColors[2].equalsIgnoreCase(answer[1]) || inputColors[2].equalsIgnoreCase(answer[3])) {
                result3 = "wit";
            } else {
                result3 = "null";
            }

            if (inputColors[3].equalsIgnoreCase(answer[3])) {
                result4 = "zwart";
            } else if (inputColors[3].equalsIgnoreCase(answer[0]) || inputColors[3].equalsIgnoreCase(answer[1]) || inputColors[3].equalsIgnoreCase(answer[2])) {
                result4 = "wit";
            } else {
                result4 = "null";
            }

            if (answer[0].equalsIgnoreCase(inputColors[0]) && answer[1].equalsIgnoreCase(inputColors[1]) && answer[2].equalsIgnoreCase(inputColors[2]) && answer[3].equalsIgnoreCase(inputColors[3])) {
                System.out.println("Congratulations! You've guessed the correct sequence!");
                gameOver = true;
            } else if (turn == 9) {
                System.out.println("Sorry, you've used all your turns. The correct sequence was: " + answer[0] + " " + answer[1] + " " + answer[2] + " " + answer[3]);
                gameOver = true;
            } else {
                System.out.println("Incorrect guess. Try again. here are your hints: "+result1 + " " + result2 + " " + result3 + " " + result4);
            }

            turn++;
        }
        scanner.close();
    }
}