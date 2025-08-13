// import java.io.*;
// import java.util.*;

// public class Pathless {
//     static class FastScanner {
//         private final InputStream in;
//         private final byte[] buffer = new byte[1 << 16];
//         private int ptr = 0, len = 0;
//         FastScanner(InputStream is) { in = is; }
//         private int read() throws IOException {
//             if (ptr >= len) {
//                 len = in.read(buffer);
//                 ptr = 0;
//                 if (len <= 0) return -1;
//             }
//             return buffer[ptr++];
//         }
//         int nextInt() throws IOException {
//             int c;
//             do c = read(); while (c <= ' ' && c != -1);
//             int sgn = 1;
//             if (c == '-') { sgn = -1; c = read(); }
//             int v = 0;
//             while (c > ' ') { v = v * 10 + (c - '0'); c = read(); }
//             return v * sgn;
//         }
//     }

//     static void output(int[] arr, StringBuilder out) {
//         for (int i = 0; i < arr.length; i++) {
//             if (i > 0) out.append(' ');
//             out.append(arr[i]);
//         }
//         out.append('\n');
//     }

//     public static void main(String[] args) throws Exception {
//         FastScanner fs = new FastScanner(System.in);
//         StringBuilder out = new StringBuilder();
//         int t = fs.nextInt();
//         while (t-- > 0) {
//             int n = fs.nextInt();
//             int s = fs.nextInt();
//             int[] a = new int[n];
//             int sum = 0;
//             int c0 = 0, c1 = 0, c2 = 0;
//             for (int i = 0; i < n; i++) {
//                 a[i] = fs.nextInt();
//                 sum += a[i];
//                 if (a[i] == 0) c0++;
//                 else if (a[i] == 1) c1++;
//                 else c2++;
//             }

//             if (s < sum) {
//                 // Any permutation blocks; print original (or sorted)
//                 output(a, out);
//                 continue;
//             }

//             int need = s - sum; // >= 0

//             // Try all choices for the last two values (positions n-1 and n) using counts
//             int[][] tails = {
//                 {0,0}, {0,1}, {0,2},
//                 {1,0}, {1,1}, {1,2},
//                 {2,0}, {2,1}, {2,2}
//             };

//             boolean printed = false;
//             for (int[] tail : tails) {
//                 int x = tail[0], y = tail[1];
//                 int r0 = c0, r1 = c1, r2 = c2;
//                 // Check counts availability for placing x, y as last two positions (ordered)
//                 if (x == 0) r0--; else if (x == 1) r1--; else r2--;
//                 if (y == 0) r0--; else if (y == 1) r1--; else r2--;
//                 if (r0 < 0 || r1 < 0 || r2 < 0) continue;

//                 int step = x + y;
//                 boolean blocks;
//                 if (step == 0) {
//                     // No detour can change the sum; only exact base possible
//                     blocks = (need != 0);
//                 } else {
//                     blocks = (need % step != 0);
//                 }
//                 if (!blocks) continue;

//                 // Build permutation: fill first n-2 positions arbitrarily with remaining multiset,
//                 // e.g., nondecreasing to keep it simple, then put x, y at the end.
//                 int[] b = new int[n];
//                 int idx = 0;
//                 for (int i = 0; i < r0; i++) b[idx++] = 0;
//                 for (int i = 0; i < r1; i++) b[idx++] = 1;
//                 for (int i = 0; i < r2; i++) b[idx++] = 2;
//                 b[n - 2] = x;
//                 b[n - 1] = y;
//                 output(b, out);
//                 printed = true;
//                 break;
//             }

//             if (!printed) out.append("-1\n");
//         }
//         System.out.print(out.toString());
//     }
// }

import java.io.*;
import java.util.*;

public class Main {
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c, sgn = 1, val = 0;
            do { c = read(); } while (c <= ' ' && c != -1);
            if (c == '-') { sgn = -1; c = read(); }
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }
            return val * sgn;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        StringBuilder out = new StringBuilder();
        int t = fs.nextInt();
        while (t-- > 0) {
            int n = fs.nextInt();
            int s = fs.nextInt();
            int[] a = new int[n];
            int c0 = 0, c1 = 0, c2 = 0;
            int T = 0;
            for (int i = 0; i < n; i++) {
                a[i] = fs.nextInt();
                T += a[i];
                if (a[i] == 0) c0++;
                else if (a[i] == 1) c1++;
                else c2++;
            }

            if (s < T) {
                // Any arrangement prevents Alice; print the given one.
                for (int i = 0; i < n; i++) {
                    if (i > 0) out.append(' ');
                    out.append(a[i]);
                }
                out.append('\n');
            } else if (s == T + 1) {
                // Arrange to avoid any 0-1 adjacency: [0...0][2...2][1...1]
                boolean first = true;
                for (int i = 0; i < c0; i++) { if (!first) out.append(' '); out.append(0); first = false; }
                for (int i = 0; i < c2; i++) { if (!first) out.append(' '); out.append(2); first = false; }
                for (int i = 0; i < c1; i++) { if (!first) out.append(' '); out.append(1); first = false; }
                out.append('\n');
            } else {
                out.append("-1\n");
            }
        }
        System.out.print(out.toString());
    }
}