Let T be the sum of all elements in the array (independent of order). Any valid walk from 1 to n visits every index at least once, so the minimum possible sum Alice can get is T (by walking straight from 1 to n). Any back-and-forth oscillation on an adjacent pair (i, i+1) adds exactly a[i] + a[i+1] more to the total. Thus achievable sums are:
- T (no oscillations), and
- T + any nonnegative combination of adjacent-pair sums.
Bob’s only control is the order, which determines which adjacent-pair sums appear.
- If there is an adjacent 0–1 somewhere, then a pair-sum of 1 exists, so Alice can increase the sum by 1 repeatedly and achieve any s \ge T.
- Bob should therefore avoid 0–1 adjacency. With at least one 2, Bob can arrange [all 0s]all 2s][all 1s] (or the reverse), so adjacent sums are from {0, 2, 3, 4} and never 1. With increments 2 and 3 available, Alice can achieve all T + x for x = 0 or x \ge 2, but not x = 1.
So:
- If s < T: impossible for Alice; any arrangement works.
- If s = T: Alice just walks straight; Bob cannot block.
- If s = T + 1: Bob can block by avoiding 0–1 adjacency (e.g., 0s, then 2s, then 1s).
- If s \ge T + 2: Alice can achieve it regardless; Bob cannot block.

Algorithm
- Compute T = \sum a_i.
- If s < T: print the array as-is.
- Else if s = T + 1: print all 0s, then all 2s, then all 1s.
- Else: print -1.
