What problem this code solves

It computes the product of a long sequence of consecutive integers modulo 1,000,000,007 in many ranges, without iterating one by one up to very large numbers like 1e9.

It uses those range products to multiply a sequence that consists of:

runs of consecutive integers (like cur, cur+1, …, upto), and

occasional single numbers taken from a sorted array a[].

The high level logic simulates taking numbers in order: it repeatedly consumes either a block of increasing integers or the next element from the array, multiplying them to a growing answer mod 1e9+7, until exactly k terms have been multiplied.

Where this pattern comes from

This is a common structure in problems where one must multiply “gaps” of consecutive values interleaved with specific values from a sorted list.

Instead of multiplying term-by-term, it jumps over entire segments and uses fast range-product routines.

Core components

Fast modular arithmetic utilities

MOD is 1,000,000,007, a prime modulus.

modPow(a, e) computes a^e mod MOD using fast exponentiation.

These are standard and required for modular inverses and exponentiation.

Blocked factorial idea for large range products

Goal: compute factorial-like products for numbers as large as 1e9 without precomputing huge tables.

The code splits the range 1..t into blocks of fixed size B (e.g., 10,000).

It precomputes smallFact[x] for x from 0 to B. This gives instant access to the product of the first r numbers in a block of size B, where r ≤ B.

For the product 1..t:

Let t = q*B + r, with 0 ≤ r < B.

Multiply the product of all full blocks 1..B, B+1..2B, …, (q-1)B+1..qB, then multiply the tail 1..r.

The challenge is that blocks like (B+1..2B) are not equal to (1..B), so we cannot reuse a single number for every block.

The code solves this by computing and memoizing the exact product of each full block ((b-1)B+1..bB) in blockProduct(b), storing it in a map keyed by the block index b.

This means once a full block product is computed, it is cached and reused if the same block is needed again later.

prod1to(t) uses those cached full-block products and the smallFact tail to produce 1..t efficiently.

prodRange(L..R) is computed as (1..R) / (1..L-1) modulo MOD, using modular inverse of prod1to(L-1).

Input and fast reader

A compact fast input reader is used to handle large inputs efficiently.

It supports both int and long.

The simulation over the sorted array

The array a[] is sorted.

There is a counter rem = k for how many numbers remain to multiply.

A variable cur tracks the start of the current “run” of consecutive integers to take next.

A pointer p tracks the next element in the sorted array a[] not yet taken.

The main loop logic

While rem > 0, compare cur and the next array value nextA (or treat nextA as infinity if all array elements are used).

If cur < nextA:

There is a gap of available consecutive integers before hitting nextA.

Take as many as possible from this run, up to either nextA-1 or until rem runs out.

Multiply the product of that range using prodRange(cur, upto).

Decrease rem by the number of terms taken and move cur forward.

Else:

If nextA is exhausted (no array values left), only the run remains: take the next rem consecutive integers from cur, multiply prodRange(cur, cur+rem-1), and finish.

Otherwise, take the single value nextA:

Multiply ans by nextA.

Decrease rem by 1.

Advance the array pointer p.

Reset cur to 1 to begin a new consecutive run next time.

Why resetting cur to 1 matters

This pattern models an alternation between:

consuming a block of consecutive integers starting from 1, and

consuming a specific element from the sorted list,

then again starting a new consecutive run from 1.

The code’s control flow captures this by setting cur = 1 after taking an array element.

Why the blocked factorial approach is needed

The number of terms to multiply can be very large, up to k which itself can be large, and values can go up to 1e9.

Iterating each multiplication would be too slow.

Precomputing factorials up to 1e9 is impossible in time and memory.

Splitting into fixed blocks of size B makes each full block fast to compute once and reusable.

prodRange allows jumping over long runs in O(number of blocks used), which is efficient when the number of different blocks touched is moderate.