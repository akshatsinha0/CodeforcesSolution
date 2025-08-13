Goal

From the array, pick four distinct indices so that we can form two disjoint pairs, and in each pair the two values are coprime (their gcd is 1). If it is not possible, print 0.

Key constraints we use

Values lie in [1, m] with m up to 1e6, and the total number of elements over all tests is up to 2e5.

We only need two pairs, so we never need more than two occurrences of the same value in an answer.

Main strategy

Reduce the search space safely

For each distinct value v, keep at most two indices with that value.

This is safe because any valid answer uses at most two indices per value, and it drastically reduces how many items we try to pair.

Build quick access by value

Group the kept indices by their value and remember one representative index for each value.

If there are any 1s, they are perfect partners because 1 is coprime with any number.

Try to give each kept index a coprime partner

For a number x, we try to find another kept index y such that gcd(x, y) = 1.

We follow a simple escalating order to keep it fast:
a) If there are any kept 1s, try pairing with one of them immediately (unless x itself is 1 paired with itself).
b) Otherwise, try up to one representative from many different values. As values are varied, the chance of being coprime is high.
c) If still not found, do a short brute sweep over a small nearby subset of the kept list and check gcd directly.

This gives us a candidate “partner” for as many indices as possible.

Select two disjoint coprime pairs

Now we have a set of candidate coprime connections.

We greedily pick the first edge we can, mark both endpoints used, then look for another edge whose endpoints are not used.

If we find two such edges, we output the four original indices. Otherwise we output 0.



Trying 1s first, then one representative from many values, and then a small local scan balances speed and reliability. Most numbers are coprime with many others, so a partner is usually found quickly.

Greedy selection of two disjoint edges is sufficient: if there exist two disjoint coprime pairs among the kept items, the greedy pass will find them.

Performance intuition

Amount of data after compression is at most min(n, 2 × number of distinct values). This keeps searches small.

GCD checks are very fast.

The sieve for smallest prime factors is built once up to the maximum m seen; it is a standard precomputation and allows quick prime-related checks if needed later.

In short

Shrink to at most two per value.

Quickly connect each item to some coprime partner using simple heuristics.

Pick two disjoint coprime pairs; print them if found, else 0.