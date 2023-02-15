package queue.tests;

public final class Printer {
    private static int curIndent = 0;
    public static void incTab() {
        curIndent++;
    }
    public static void decTab() {
        curIndent--;
    }
    public static int getIndent() {
        return curIndent;
    }
    public static void print(String str) {
        System.out.print(getTabulatedString(str, curIndent));
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
    private static String getTabulatedString(String str, int tabCount) {
        return " ".repeat(4 * tabCount) + str;
    }
}
