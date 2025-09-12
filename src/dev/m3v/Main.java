package dev.m3v;
public class Main {
    public static void main(String[] args) throws Exception {
        String colors[] = {"Red", "Green", "Yellow", "Orange", "Purple", "Blue", "space"};
        String answer[] = new String[4];
        int i = 0;

        while (i < 4) {
            int rand = (int) (Math.random() * 7);
            answer[i] = colors[rand];
            System.out.println(answer[i]);
            i++;
        }
    }
}
