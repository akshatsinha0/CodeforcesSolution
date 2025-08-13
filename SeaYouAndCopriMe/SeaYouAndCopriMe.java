import java.io.*;
import java.util.*;

/**
 * Codeforces: H. Sea, You & copriMe
 */
public class SeeYouAndCopriMe {
    static final int MAXM = 1_000_000;
    static int[] spf = new int[MAXM + 1];
    static boolean spfBuilt = false;

    static void buildSPF() {
        if (spfBuilt) return;
        for (int i = 2; i <= MAXM; i++) spf[i] = 0;
        for (int i = 2; i <= MAXM; i++) {
            if (spf[i] == 0) {
                spf[i] = i;
                if ((long) i * i <= MAXM) {
                    for (int j = i * i; j <= MAXM; j += i) {
                        if (spf[j] == 0) spf[j] = i;
                    }
                }
            }
        }
        spf[0] = 0; spf[1] = 1;
        spfBuilt = true;
    }

    static int[] distinctPrimes(int x) {
        if (x == 1) return new int[0];
        int last = 0;
        int cnt = 0;
        int y = x;
        // Count distinct
        while (y > 1) {
            int p = spf[y];
            if (p == 0) p = y; // safety
            if (p != last) cnt++;
            last = p;
            while (y % p == 0) y /= p;
        }
        int[] res = new int[cnt];
        y = x; last = 0; int idx = 0;
        while (y > 1) {
            int p = spf[y];
            if (p == 0) p = y;
            if (p != last) res[idx++] = p;
            last = p;
            while (y % p == 0) y /= p;
        }
        return res;
    }

    // Custom dynamic int list to avoid boxing overhead
    static class IntList {
        int[] a;
        int sz;
        IntList() { a = new int[4]; sz = 0; }
        void add(int v) {
            if (sz == a.length) a = Arrays.copyOf(a, a.length * 2);
            a[sz++] = v;
        }
        int size() { return sz; }
        int get(int i) { return a[i]; }
        int[] toArray() { return Arrays.copyOf(a, sz); }
    }

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
            int c, s = 1, x = 0;
            do { c = read(); } while (c <= 32);
            if (c == '-') { s = -1; c = read(); }
            while (c > 32) {
                x = x * 10 + (c - '0');
                c = read();
            }
            return x * s;
        }
    }

    public static void main(String[] args) throws Exception {
        buildSPF();

        FastScanner fs = new FastScanner(System.in);
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();
        // For timestamp marking (avoid clearing arrays)
        int globalMarkTime = 1;

        while (t-- > 0) {
            int n = fs.nextInt();
            int m = fs.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = fs.nextInt();

            // Prepare per-index distinct primes, count per prime, and index lists by prime
            // Only allocate per-prime lists lazily.
            IntList[] idxByPrime = new IntList[m + 1];
            int[] primeCount = new int[m + 1];
            int[][] primesOfIdx = new int[n][];
            IntList ones = new IntList();

            for (int i = 0; i < n; i++) {
                if (a[i] == 1) {
                    primesOfIdx[i] = new int[0];
                    ones.add(i);
                } else {
                    int[] ps = distinctPrimes(a[i]);
                    primesOfIdx[i] = ps;
                    for (int p : ps) {
                        if (idxByPrime[p] == null) idxByPrime[p] = new IntList();
                        idxByPrime[p].add(i);
                        primeCount[p]++;
                    }
                }
            }

            // Quick small-n brute force
            if (n <= 2000) {
                int p1 = -1, q1 = -1, p2 = -1, q2 = -1;
                
                // Try all possible ways to choose 4 distinct indices and split them into 2 coprime pairs
                boolean found = false;
                outermost:
                for (int i = 0; i < n && !found; i++) {
                    for (int j = i + 1; j < n && !found; j++) {
                        if (gcd(a[i], a[j]) == 1) {
                            for (int k = 0; k < n && !found; k++) {
                                if (k == i || k == j) continue;
                                for (int l = k + 1; l < n && !found; l++) {
                                    if (l == i || l == j) continue;
                                    if (gcd(a[k], a[l]) == 1) {
                                        p1 = i; q1 = j; p2 = k; q2 = l;
                                        found = true;
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (found) {
                    out.append(p1 + 1).append(' ').append(q1 + 1).append(' ')
                       .append(p2 + 1).append(' ').append(q2 + 1).append('\n');
                } else {
                    // Try ones shortcuts
                    boolean printed = false;
                    int k1 = ones.size();
                    if (k1 >= 4) {
                        out.append(ones.get(0) + 1).append(' ').append(ones.get(1) + 1).append(' ')
                           .append(ones.get(2) + 1).append(' ').append(ones.get(3) + 1).append('\n');
                        printed = true;
                    } else if (k1 >= 3) {
                        int other = -1;
                        for (int i = 0; i < n; i++) if (a[i] != 1) { other = i; break; }
                        if (other != -1) {
                            out.append(ones.get(0) + 1).append(' ').append(ones.get(1) + 1).append(' ')
                               .append(ones.get(2) + 1).append(' ').append(other + 1).append('\n');
                            printed = true;
                        }
                    } else if (k1 >= 2) {
                        // pair the ones, and search another coprime pair among rest
                        boolean foundPair = false;
                        boolean[] used = new boolean[n];
                        used[ones.get(0)] = used[ones.get(1)] = true;
                        outer3:
                        for (int i = 0; i < n; i++) if (!used[i]) {
                            for (int j = i + 1; j < n; j++) if (!used[j]) {
                                if (gcd(a[i], a[j]) == 1) {
                                    out.append(ones.get(0) + 1).append(' ').append(ones.get(1) + 1).append(' ')
                                       .append(i + 1).append(' ').append(j + 1).append('\n');
                                    foundPair = true;
                                    break outer3;
                                }
                            }
                        }
                        if (foundPair) printed = true;
                    }
                    if (!printed) out.append("0\n");
                }
                continue;
            }

            // Large n: use heuristic + marking; also ones shortcuts
            int k1 = ones.size();
            // Case 1: 4 ones
            if (k1 >= 4) {
                out.append(ones.get(0) + 1).append(' ').append(ones.get(1) + 1).append(' ')
                   .append(ones.get(2) + 1).append(' ').append(ones.get(3) + 1).append('\n');
                continue;
            }
            // Case 2: 3 ones
            if (k1 >= 3) {
                int other = -1;
                for (int i = 0; i < n; i++) if (a[i] != 1) { other = i; break; }
                if (other != -1) {
                    out.append(ones.get(0) + 1).append(' ').append(ones.get(1) + 1).append(' ')
                       .append(ones.get(2) + 1).append(' ').append(other + 1).append('\n');
                    continue;
                }
            }

            // Exclusion markers for building pairs
            int[] excludedStamp = new int[n];
            int excludedTime = 1;

            int p1 = -1, q1 = -1, p2 = -1, q2 = -1;

            // Try to use ones smartly to reduce search
            if (k1 >= 2) {
                // Use the pair of ones as first pair; search another pair among remaining
                int oneA = ones.get(0), oneB = ones.get(1);
                excludedTime++;
                excludedStamp[oneA] = excludedTime;
                excludedStamp[oneB] = excludedTime;

                int[] pair = findOneCoprimePair(n, primesOfIdx, primeCount, idxByPrime, excludedStamp, excludedTime);
                if (pair != null) {
                    p1 = oneA; q1 = oneB;
                    p2 = pair[0]; q2 = pair[1];
                } else {
                    // If k1 >= 2 but cannot find another pair, maybe we can use a single 1 and two others that form a coprime pair
                    // Try: find a coprime pair among non-1; then pair the remaining 1 with any other index
                    excludedTime++;
                    for (int i = 0; i < n; i++) excludedStamp[i] = 0; // reset
                    int[] pair2 = findOneCoprimePair(n, primesOfIdx, primeCount, idxByPrime, excludedStamp, 1);
                    if (pair2 != null) {
                        p1 = pair2[0]; q1 = pair2[1];
                        // Find any k not used and not equal to ones already used (we have at least 2 ones)
                        boolean[] used = new boolean[n];
                        used[p1] = used[q1] = true;
                        int k = -1;
                        for (int idx = 0; idx < n; idx++) if (!used[idx]) { k = idx; break; }
                        if (k != -1) {
                            p2 = ones.get(0); q2 = k;
                        }
                    }
                }
            } else if (k1 == 1) {
                // Find one pair among non-1s; then pair the remaining 1 with any other distinct index
                int one = ones.get(0);
                excludedTime++;
                excludedStamp[one] = excludedTime;
                int[] pair = findOneCoprimePair(n, primesOfIdx, primeCount, idxByPrime, excludedStamp, excludedTime);
                if (pair != null) {
                    p1 = pair[0]; q1 = pair[1];
                    boolean[] used = new boolean[n];
                    used[p1] = used[q1] = true;
                    used[one] = true;
                    int k = -1;
                    for (int idx = 0; idx < n; idx++) if (!used[idx]) { k = idx; break; }
                    if (k != -1) {
                        p2 = one; q2 = k;
                    }
                }
            } else {
                // No ones: find two disjoint coprime pairs
                int[] pair1 = findOneCoprimePair(n, primesOfIdx, primeCount, idxByPrime, excludedStamp, excludedTime);
                if (pair1 != null) {
                    p1 = pair1[0]; q1 = pair1[1];
                    excludedTime++;
                    excludedStamp[p1] = excludedTime;
                    excludedStamp[q1] = excludedTime;
                    int[] pair2 = findOneCoprimePair(n, primesOfIdx, primeCount, idxByPrime, excludedStamp, excludedTime);
                    if (pair2 != null) {
                        p2 = pair2[0]; q2 = pair2[1];
                    }
                }
            }

            if (p1 != -1 && p2 != -1) {
                out.append(p1 + 1).append(' ').append(q1 + 1).append(' ')
                   .append(p2 + 1).append(' ').append(q2 + 1).append('\n');
            } else {
                out.append("0\n");
            }
        }

        System.out.print(out);
    }

    static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Helper method to find one coprime pair among indices not excluded
    static int[] findOneCoprimePair(int n, int[][] primesOfIdx, int[] primeCount, 
                                   IntList[] idxByPrime, int[] excludedStamp, int excludedTime) {
        int[] badStamp = new int[n];
        int badTime = 1;
        final int CAND_LIMIT = 96; // try up to this many best candidates

        // Build candidate pool: pick up to CAND_LIMIT indices with smallest scores
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (u, v) -> Integer.compare(v[0], u[0]) // max-heap by score
        );
        for (int i = 0; i < n; i++) {
            if (excludedStamp[i] == excludedTime) continue;
            // compute score s(i) = sum primeCount[p] for p|a[i]
            int[] ps = primesOfIdx[i];
            int s = 0;
            for (int p : ps) s += primeCount[p];
            
            if (pq.size() < CAND_LIMIT) pq.offer(new int[]{s, i});
            else if (s < pq.peek()[0]) {
                pq.poll();
                pq.offer(new int[]{s, i});
            }
        }
        if (pq.isEmpty()) return null;
        
        // Extract to list (we want ascending score)
        ArrayList<int[]> cands = new ArrayList<>(pq);
        cands.sort(Comparator.comparingInt(x -> x[0]));

        // For each candidate, mark union of prime lists and scan for an unmarked partner
        for (int[] cand : cands) {
            int i = cand[1];
            badTime++;
            // mark all indices sharing any prime with i
            for (int p : primesOfIdx[i]) {
                IntList lst = idxByPrime[p];
                if (lst == null) continue;
                int sz = lst.size();
                for (int t = 0; t < sz; t++) {
                    int idx = lst.get(t);
                    badStamp[idx] = badTime;
                }
            }
            // Find first j not marked and not excluded and j != i
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                if (excludedStamp[j] == excludedTime) continue;
                if (badStamp[j] != badTime) {
                    // j shares no prime with i => gcd=1
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}