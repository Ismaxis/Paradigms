package queue.tests;

public final class Printer {
    private static int curIndent = 0;
    public static void incTab() {
        curIndent++;
    }
    public static void incTab(int count) {
        curIndent += count;
    }
    public static void decTab() {
        curIndent--;
    }
    public static void decTab(int count) {
        curIndent -= count;
    }
    public static int getIndent() {
        return curIndent;
    }
    public static void print(String str) {
        System.out.print(getTabulatedString(str));
    }
    public static void println() {
        println("", 0);
    }
    public static void println(String str) {
        println(str, curIndent);
    }
    public static void println(String str, int tabCount) {
        System.out.println(getTabulatedString(str, tabCount));
    }
    public static void printf(String format, Object... args) {
        System.out.printf(getTabulatedString(format), args);
    }
    private static String getTabulatedString(String str) {
        return getTabulatedString(str, curIndent);
    }
    private static String getTabulatedString(String str, int tabCount) {
        return " ".repeat(4 * tabCount) + str;
    }
}
