package search;

public class BinarySearch {
    // Pre: args.length > 0 &&
    //      forall i=0...args.length-1 args[i] - строковое представление целого числа от -2^31 до 2^31 - 1 &&
    //      forall i=1...args.length-2 a[i] <= a[i - 1] - где а[i - 1] = Integer.parseInt(args[i])
    // Post: min(i : a[i] <= x) is printed to System.out
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);

        // n := args.length - 1
        // a[-1] == -inf && a[n] == +inf
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }

        int index = iterativeBinSearch(a, x);
//        int index = recursiveBinSearch(a, x);
        System.out.println(index);
    }

    // n := a.length
    // Pre: forall i=0...n-1 a[i] <= a[i - 1] && a[-1] == -inf && a[n] == +inf
    // Post: R = min(0 <= i <= n : a[i] <= x)
    public static int iterativeBinSearch(final int[] a, final int x) {
        // forall i=0...n-1 a[i] <= a[i - 1]
        int l = -1;
        // l' = -1;
        int r = a.length;
        // r' = n;

        // Inv: -1 <= l' && r' <= n && r' - l' > 1 && a[r'] <= x < a[l']
        while(r - l > 1) {
            // -1 <= l' && r' <= n && r - l > 1 && a[r'] <= x < a[l']
            int m = (r + l) / 2;
            // l' < m < r' [Proof is the same as in the Recursive version]
            if (a[m] > x) {
                // -1 <= l' < m < r' <= n && a[m] > x
                // -1 < m < r' <= n && a[r'] <= x < a[m]
                l = m;
                // l' = m
                // -1 < l' < r' <= n && a[r'] <= x < a[l']
            } else {
                // -1 <= l' < m < r' <= n && a[m] <= x
                // -1 <= l' < m <= n && a[m] <= x < a[l']
                r = m;
                // r' = m
                // -1 <= l' < r' < n && a[r'] <= x < a[l']
            }
        }
        // r' - l' <= 1 && -1 <= l' < r' <= n && a[r'] <= x < a[l'] &&
        //      && forall i=0...n-1 a[i] <= a[i - 1]

        // l' < r'
        // r' - l' > 0
        // 0 < r' - l' <= 1
        // r' - l' == 1
        // r' == l' + 1

        // -1 <= l' < r' <= n && r' = l' + 1
        // -1 <= r' - 1 && r' <= n
        // 0 <= r' <= n

        // a[r'] <= x && a[r' - 1] > x && forall i=0...n-1 a[i] <= a[i - 1]
        //      && 0 <= r' <= n
        // r' = min(0 <= i <= n : a[i] <= x)
        return r;
        // R = min(0 <= i <= n : a[i] <= x)
    }

    // n := a.length
    // Pre: forall i=0...n-1 a[i] <= a[i - 1] && a[-1] == -inf && a[n] == +inf
    // Post: R = min(0 <= i <= n : a[i] <= x)
    public static int recursiveBinSearch(final int[] a, final int x) {
        return recursiveBinSearch(a, x, -1, a.length);
    }

    // n := a.length
    // Pre: forall i=0...n-1 a[i] <= a[i - 1] && -1 <= l < r <= n && a[r] <= x < a[l]
    // Post: R = min(-1 <= l < i <= r <= n : a[i] <= x)
    public static int recursiveBinSearch(final int[] a, final int x, final int l, final int r) {
        // forall i=0...n-1 a[i] <= a[i - 1] && -1 <= l < r <= n && a[r] <= x < a[l]
        if (r - l == 1) {
            // -1 <= l < r <= n && r - l == 1 && a[r] <= x < a[l]
            // r == l + 1
            // -1 <= l < n
            // 0 <= l + 1 < n + 1
            // 0 <= r <= n && a[r] <= x < a[l]
            // r = min(-1 <= l < i <= r <= n : a[i] <= x)
            return r;
            // R = min(-1 <= l < i <= r <= n : a[i] <= x)
        }
        // r - l != 1 && l < r
        // r - l > 1
        int m = (l + r) / 2;
        // l < m < r

        // Proof, that l <= m < r:
        // l < r
        // 2*l < l + r < 2*r
        // l <= (l + r - (l + r)mod2)/2 < r

        // Proof, that l < m:
        // r - l > 1
        // r > l + 1
        // (l + l + 1 - (l + r)mod2)/2 < (l + r - (l + r)mod2)/2 = m
        // (2*l + 1 - (l + r)mod2)//2 = (2*l + 1 - (l + r)mod2 - (2*l + 1 - (l + r)mod2)mod2)/2 =
        //      = (2*l + 1 - (l + r)mod2 - (1 - (l + r)mod2)mod2)/2

        // 1. (l + r)mod2 == 0
        //    (2*l + 1 - 0 - (1 - 0)mod2)/2 = (2*l + 1 - 1)/2 = l
        // 2. (l + r)mod2 == 1
        //    (2*l + 1 - 1 - (1 - 1)mod2)/2 = (2*l + 0 - 0)/2 = l

        // (2*l + 1 - (l + r)mod2 - (1 - (l + r)mod2)mod2)/2 = l &&
        // (l + l + 1 - (l + r)mod2)/2 < (l + r - (l + r)mod2)/2 = m

        // l < m

        if (a[m] > x) {
            // l < m < r && a[m] > x
            // -1 < m < r <= n && a[r] <= x < a[m]
            return recursiveBinSearch(a, x, m, r);
            // R = min(-1 <= l < i <= r <= n : a[i] <= x)
        } else {
            // l < m < r && a[m] <= x
            // -1 <= l < m < n && a[m] <= x < a[l]
            return recursiveBinSearch(a, x, l, m);
            // R = min(-1 <= l < i <= r <= n : a[i] <= x)
        }
    }
}
