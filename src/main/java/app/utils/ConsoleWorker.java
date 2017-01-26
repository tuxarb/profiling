package app.utils;

import java.util.Scanner;

public class ConsoleWorker {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleWorker() {
    }

    public static String readLine() {
        return SCANNER.nextLine();
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }
}
