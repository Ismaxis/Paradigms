package search;

public class BinarySearchUni {
    // n := args.length
    // Pre: b > 0 &&
    //      forall i=0...n-1 args[i] - строковое представление целого числа от -2^31 до 2^31 - 1 &&
    //      а[i] = Integer.parseInt(args[i]) &&
    //      exists R in [0; n) :
    //          forall i in [0; R - 1]: a[i - 1] < a[i] &&
    //          forall i in [R; n):     a[i - 1] > a[i]
    // Post: min(R) is printed to System.out
    public static void main(String[] args) {
        int[] a = new int[args.length];
        int sum = 0;
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
            sum += a[i];
        }

        int minLength;
        if (sum % 2 == 1) {
            minLength = iterativeBinSearch(a);
        }
        else {
            minLength = recursiveBinSearch(a);
        }
        System.out.println(minLength);
    }

    // Pre: exists res in [0; n) :
    //          forall i in [0; res - 1] a[i - 1] < a[i] &&
    //          forall i in [res; n)     a[i - 1] > a[i]
    // Post: R = min(r in [0; n) :
    //    forall i in [0; res - 1] a[i - 1] < a[i] &&
    //    forall i in [res; n)     a[i - 1] > a[i])
    public static int iterativeBinSearch(final int[] a) {
        // exists res in [0; n) :
        //          forall i in [0; res - 1] a[i - 1] < a[i] &&
        //          forall i in [res; n)     a[i - 1] > a[i]
        int l = 0;
        // l' = 0;
        int r = a.length;
        // r' = n;

        // Inv: 0 <= l' && r' <= n && r' - l' > 1 && l' <= R < r'
        while(r - l > 1) {
            // 0 <= l' && r' <= n && r - l > 1 && l' <= R < r'
            int m = (r + l) / 2;
            // l' < m < r' [Proof is the same as in the Recursive version]
            if (a[m - 1] <= a[m]) {
                // 0 <= l' < m < r' <= n && a[m - 1] <= a[m]
                // l' < m <= R
                l = m;
                // l' = m
                // 0 <= l' < r' <= n && l' <= R < r'
            } else {
                // 0 <= l' < m < r' <= n && a[m - 1] > a[m]
                // R < m < r'
                r = m;
                // r' = m
                // 0 <= l' < r' < n && l' <= R < r'
            }
        }
        // r' - l' <= 1 && 0 <= l' < r' <= n && l' <= R < r' &&
        //     exists res in [0; n) :
        //          forall i in [0; res - 1] a[i - 1] < a[i] &&
        //          forall i in [res; n)     a[i - 1] > a[i]

        // l' < r'
        // r' - l' > 0
        // 0 < r' - l' <= 1
        // r' - l' == 1
        // r' == l' + 1

        // 0 <= l' < r' <= n && l' = r' - 1
        // 0 <= l' && l' < n
        // 0 <= l' < n

        // a[l' - 1] <= a[l'] && forall i [l' + 1; n) a[i - 1] > a[i]
        //      && 0 <= l' < n
        // l' = min(res in [0; n) :
        //          forall i in [0; res - 1] a[i - 1] < a[i] &&
        //          forall i in [res; n)     a[i - 1] > a[i])
        return l;
        // R = min(res in [0; n) :
        //          forall i in [0; res - 1] a[i - 1] < a[i] &&
        //          forall i in [res; n)     a[i - 1] > a[i])
    }

    // Pre: exists res in [0; n) :
    //    forall i in [0; res - 1] a[i - 1] < a[i] &&
    //    forall i in [res; n)     a[i - 1] > a[i]
    // Post: R = min(res in [0; n) :
    //    forall i in [0; res - 1] a[i - 1] < a[i] &&
    //    forall i in [res; n)     a[i - 1] > a[i])
    public static int recursiveBinSearch(final int[] a) {
        return recursiveBinSearch(a, 0, a.length);
    }

    // Pre: exists res in [0; n) :
    //    forall i in [0; R - 1] a[i - 1] < a[i] &&
    //    forall i in [R; n)     a[i - 1] > a[i] &&
    //    r' - l' > 1 && 0 <= l' <= res < r' <= n
    // Post: R = min(r in [0; n) :
    //    forall i in [0; r - 1] a[i - 1] < a[i] &&
    //    forall i in [r; n)     a[i - 1] > a[i])
    public static int recursiveBinSearch(final int[] a, final int l, final int r) {
        // exists res in [0; n) :
        //          forall i in [0; res - 1] a[i - 1] < a[i] &&
        //          forall i in [res; n)     a[i - 1] > a[i]
        if (r - l == 1) {
            // [Proof is the same as in the Iterative version]
            // l' = min(res in [0; n) :
            //          forall i in [0; res - 1] a[i - 1] < a[i] &&
            //          forall i in [res; n)     a[i - 1] > a[i])
            return l;
            // R = min(res in [0; n) :
            //          forall i in [0; res - 1] a[i - 1] < a[i] &&
            //          forall i in [res; n)     a[i - 1] > a[i])
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

        if (a[m - 1] <= a[m]) {
            // 0 <= l' < m < r' <= n && a[m - 1] <= a[m]
            // l' < m <= R < r'
            return recursiveBinSearch(a, m, r);
            // R = min(res in [0; n) :
            //          forall i in [0; res - 1] a[i - 1] < a[i] &&
            //          forall i in [res; n)     a[i - 1] > a[i])
        } else {
            // 0 <= l' < m < r' <= n && a[m - 1] > a[m]
            // R < m < r'
            return recursiveBinSearch(a, l, m);
            // R = min(res in [0; n) :
            //          forall i in [0; res - 1] a[i - 1] < a[i] &&
            //          forall i in [res; n)     a[i - 1] > a[i])
        }
    }
}
