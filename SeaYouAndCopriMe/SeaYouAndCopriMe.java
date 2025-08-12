import java.io.*;
import java.util.*;

public class SeaYouAndCopriMe {
    static final int MAXM = 1_000_000;
    static int[] spf = new int[MAXM + 1];

    static void buildSPF(int limit) {
        if (spf[2] != 0) return;
        for (int i = 2; i <= limit; i++) spf[i] = 0;
        for (int i = 2; i <= limit; i++) {
            if (spf[i] == 0) {
                spf[i] = i;
                if ((long) i * i <= limit) {
                    for (int j = i * i; j <= limit; j += i) {
                        if (spf[j] == 0) spf[j] = i;
                    }
                }
            }
        }
        spf[1] = 1;
    }

    static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    static int[] distinctPrimes(int x) {
        int[] tmp = new int[8];
        int sz = 0;
        int last = -1;
        while (x > 1) {
            int p = spf[x];
            if (p == 0) p = x;
            if (p != last) tmp[sz++] = p;
            last = p;
            x /= p;
            while (x % p == 0) x /= p;
        }
        return Arrays.copyOf(tmp, sz);
    }

    static class FastScanner {
        final InputStream in;
        final byte[] buf = new byte[1 << 16];
        int p = 0, n = 0;
        FastScanner(InputStream is) { in = is; }
        int read() throws IOException {
            if (p >= n) {
                n = in.read(buf); p = 0;
                if (n <= 0) return -1;
            }
            return buf[p++];
        }
        int nextInt() throws IOException {
            int c; do c = read(); while (c <= ' ' && c != -1);
            int sgn = 1;
            if (c == '-') { sgn = -1; c = read(); }
            int v = 0;
            while (c > ' ') { v = v * 10 + (c - '0'); c = read(); }
            return v * sgn;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        int t = fs.nextInt();

        int[] ns = new int[t];
        int[] ms = new int[t];
        int[][] arrs = new int[t][];
        int maxM = 1;
        for (int tc = 0; tc < t; tc++) {
            int n = fs.nextInt();
            int m = fs.nextInt();
            ns[tc] = n; ms[tc] = m;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = fs.nextInt();
            arrs[tc] = a;
            if (m > maxM) maxM = m;
        }
        buildSPF(maxM);

        StringBuilder out = new StringBuilder();

        for (int tc = 0; tc < t; tc++) {
            int n = ns[tc];
            int m = ms[tc];
            int[] a = arrs[tc];

            // Keep at most two indices per value
            int[] used = new int[m + 1];
            ArrayList<Integer> keptVal = new ArrayList<>();
            ArrayList<Integer> keptIdx = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int v = a[i];
                if (used[v] < 2) {
                    used[v]++;
                    keptVal.add(v);
                    keptIdx.add(i + 1);
                }
            }

            int K = keptVal.size();
            if (K < 4) {
                out.append("0\n");
                continue;
            }

            // Buckets by value; also collect one representative index per value
            ArrayList<Integer>[] byVal = new ArrayList[m + 1];
            int[] repIdx = new int[m + 1];
            for (int i = 0; i < K; i++) {
                int v = keptVal.get(i);
                if (byVal[v] == null) byVal[v] = new ArrayList<>();
                byVal[v].add(i); // store position in kept arrays
                if (repIdx[v] == 0) repIdx[v] = i + 1; // +1 to distinguish 0
            }

            // Pre-list of indices that are value 1 (coprime with everything)
            ArrayList<Integer> ones = new ArrayList<>();
            for (int v = 1; v <= m; v++) {
                if (v == 1 && byVal[v] != null) {
                    ones.addAll(byVal[v]);
                }
            }

            // Build edges list lazily; store for each kept index a partner edge index
            int[] partner = new int[K];
            Arrays.fill(partner, -1);

            // For each kept index, try to find one coprime partner
            for (int i = 0; i < K; i++) {
                if (partner[i] != -1) continue;
                int vi = keptVal.get(i);

                // Fast path: try any '1' position (other than i)
                if (vi != 1 && !ones.isEmpty()) {
                    for (int pos : ones) {
                        if (pos != i) {
                            partner[i] = pos;
                            break;
                        }
                    }
                    if (partner[i] != -1) continue;
                }

                // Try one representative from several different values
                int tried = 0;
                for (int v = 1; v <= m && tried < 50; v++) {
                    if (byVal[v] == null) continue;
                    int repPos = repIdx[v] - 1;
                    if (repPos < 0 || repPos == i) continue;
                    if (gcd(vi, keptVal.get(repPos)) == 1) {
                        partner[i] = repPos;
                        break;
                    }
                    tried++;
                }

                if (partner[i] != -1) continue;

                // As a fallback, brute try a small number of random/nearby positions
                for (int j = i + 1; j < K && j < i + 200; j++) {
                    if (gcd(vi, keptVal.get(j)) == 1) { partner[i] = j; break; }
                }
                if (partner[i] != -1) continue;
                for (int j = 0; j < i && j < 200; j++) {
                    if (gcd(vi, keptVal.get(j)) == 1) { partner[i] = j; break; }
                }
            }

            // Pick two disjoint edges greedily
            boolean[] usedVertex = new boolean[K];
            int[] ans = new int[4];
            int found = 0;
            for (int i = 0; i < K && found < 2; i++) {
                int j = partner[i];
                if (j == -1) continue;
                if (usedVertex[i] || usedVertex[j]) continue;
                usedVertex[i] = usedVertex[j] = true;
                ans[found * 2] = keptIdx.get(i);
                ans[found * 2 + 1] = keptIdx.get(j);
                found++;
            }

            if (found == 2) {
                out.append(ans[0]).append(' ').append(ans[1]).append(' ')
                   .append(ans[2]).append(' ').append(ans[3]).append('\n');
            } else {
                out.append("0\n");
            }
        }

        System.out.print(out.toString());
    }
}

