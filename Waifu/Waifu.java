import java.io.*;
import java.util.*;

public class Main {
    static final long MOD = 1_000_000_007L;

    // Blocked factorial with memoized block products: works up to 1e9
    static final int B = 10000; // block size (tune 5k..20k)
    static long[] smallFact;    // 0..B
    static HashMap<Long, Long> blockProd = new HashMap<>();

    static void initSmall() {
        smallFact = new long[B + 1];
        smallFact[0] = 1;
        for (int i = 1; i <= B; i++) smallFact[i] = (smallFact[i - 1] * i) % MOD;
    }
    static long modPow(long a, long e) {
        long r = 1;
        while (e > 0) {
            if ((e & 1) == 1) r = (r * a) % MOD;
            a = (a * a) % MOD;
            e >>= 1;
        }
        return r;
    }
    // product of ((b-1)B+1 .. bB)
    static long blockProduct(long b) {
        Long key = b;
        Long v = blockProd.get(key);
        if (v != null) return v;
        long start = (b - 1) * (long)B + 1;
        long p = 1;
        long end = b * (long)B;
        for (long x = start; x <= end; x++) p = (p * (x % MOD)) % MOD;
        blockProd.put(key, p);
        return p;
    }
    // product 1..t
    static long prod1to(long t) {
        if (t <= B) return smallFact[(int)t];
        long q = t / B;
        int r = (int)(t % B);
        long res = 1;
        for (long b = 1; b <= q; b++) res = (res * blockProduct(b)) % MOD;
        res = (res * smallFact[r]) % MOD;
        return res;
    }
    // product L..R (L>=1)
    static long prodRange(long L, long R) {
        if (L > R) return 1;
        long a = prod1to(R);
        long b = prod1to(L - 1);
        return a * modPow(b, MOD - 2) % MOD;
    }

    static class FS {
        final InputStream in;
        final byte[] buf = new byte[1 << 16];
        int p = 0, n = 0;
        FS(InputStream is){ in = is; }
        int read() throws IOException {
            if (p >= n) { n = in.read(buf); p = 0; if (n <= 0) return -1; }
            return buf[p++];
        }
        long nextLong() throws IOException {
            int c; do c = read(); while (c <= ' ' && c != -1);
            boolean neg = false; if (c=='-'){ neg=true; c=read(); }
            long v = 0; while (c > ' '){ v = v*10 + (c-'0'); c=read(); }
            return neg ? -v : v;
        }
        int nextInt() throws IOException { return (int)nextLong(); }
    }

    public static void main(String[] args) throws Exception {
        initSmall();
        FS fs = new FS(System.in);
        StringBuilder out = new StringBuilder();
        int t = fs.nextInt();
        while (t-- > 0) {
            int n = fs.nextInt();
            long k = fs.nextLong();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = fs.nextInt();
            Arrays.sort(a);

            // Use optimized simulation that avoids storing too many elements
            PriorityQueue<Long> pq = new PriorityQueue<>();
            for (int x : a) pq.add((long)x);
            
            long ans = 1;
            for (int op = 0; op < k; op++) {
                long min = pq.poll();
                ans = ans * min % MOD;
                
                // Add 1, 2, ..., min-1 to the set, but be smart about it
                // Only add elements that could potentially be processed
                long toAdd = Math.min(min - 1, k - op - 1);
                for (long i = 1; i <= toAdd; i++) {
                    pq.add(i);
                }
            }
            
            out.append(ans).append('\n');
        }
        System.out.print(out.toString());
    }
}
