package app.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final String BACKGROUND_IMAGE = "/images/triangle.jpg"; //absolute path to resources
    public static final String BUTTON_IMAGE = "/images/round-button.jpg";
    public static final String PROGRESS = "/images/loading.gif";
    public static final String POSTGRESQL = "Postgresql";
    public static final String MYSQL = "Mysql";
    public static final String ORACLE = "Oracle";
    public static final String SQL_SERVER = "SqlServer";
    public static final String OTHER_DBMS = "Other";
    public static final String SEPARATOR = System.getProperty("file.separator");

    private Utils() {
    }

    public static Long getNumberFromString(String s) {
        StringBuilder result = new StringBuilder();
        char[] chars = s.toCharArray();

        for (char symb : chars) {
            if (Character.isDigit(symb))
                result.append(symb);

        }
        return Long.parseLong(result.toString());
    }

    public static String formatNumber(long number) {
        Locale loc = new Locale(Locale.ENGLISH.getLanguage());
        NumberFormat formatter = NumberFormat.getInstance(loc);

        return formatter.format(number);
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));

    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return format.format(date);
    }

    public static boolean isMemoryLine(String str) {
        return str.contains("пам") || str.contains("mem");
    }

    public static String getPathToLogs() {
        return Log.SEE_LOGS + "[ " +
                System.getProperty("user.home") + SEPARATOR +
                ".profiling_logs" + SEPARATOR + " ]";
    }

    public static String getUserCommandInfo(ProcessHandle processHandle) {
        ProcessHandle.Info info = processHandle.info();
        boolean flag = false;
        if (info.arguments().isPresent()) {
            flag = true;
        }
        return " [ " +
                info.command().orElse("") +
                (flag ? " " + Arrays.toString(info.arguments().get()) : "") +
                " ]" + ", pid -> " + "[ " + processHandle.getPid() + " ]";
    }
}