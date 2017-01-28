package app.utils;

import java.util.Scanner;

public class ConsoleWorker {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleWorker() {
    }

    public static String readLine() {
        String line = SCANNER.nextLine();
        if (isExit(line)) {
            println(Log.CLOSING_APP);
            System.exit(0);
        }
        return line;
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    private static boolean isExit(String line) {
        return "exit".equalsIgnoreCase(line);
    }
}
