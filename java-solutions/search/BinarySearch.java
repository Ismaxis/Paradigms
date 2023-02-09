package search;

public class BinarySearch {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(0);
            return;
        }
        int x = Integer.parseInt(args[0]);

        // Fake binary search O(n)
        for (int i = 1; i < args.length; i++) {
            if (Integer.parseInt(args[i]) <= x) {
                System.out.println(i - 1);
                return;
            }
        }
        System.out.println(args.length - 1);
    }
}
