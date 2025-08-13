# Sea, You & copriMe - Solution Approach

## Problem Summary
Given an array of n integers, find 4 distinct indices p, q, r, s such that:
- gcd(a[p], a[q]) = 1 (coprime pair)
- gcd(a[r], a[s]) = 1 (another coprime pair)

## Our Approach

### 1. Special Cases with 1s
Since gcd(1, x) = 1 for any x, having 1s in the array makes finding coprime pairs easier:

- **4 or more 1s**: Simply pick any 4 indices with value 1
- **3 ones**: Pick 3 ones and any other number
- **2 ones**: Use the 2 ones as one coprime pair, find another coprime pair from remaining numbers

### 2. Small Arrays (n ≤ 2000) - Brute Force
For smaller arrays, we use exhaustive search:
- Try all possible combinations of 4 distinct indices
- For each combination, check if we can split them into 2 coprime pairs
- This ensures we don't miss any valid solution

### 3. Large Arrays (n > 2000) - Optimized Approach
For larger arrays, brute force is too slow, so we use a smarter strategy:

#### Prime Factorization
- Precompute smallest prime factors using sieve
- For each number, find its distinct prime factors
- Two numbers are coprime if they share no common prime factors

#### Heuristic Search
- **Scoring**: Give each index a score = sum of counts of its prime factors
- **Lower score = better candidate** (fewer conflicts with other numbers)
- **Candidate Selection**: Pick up to 96 best candidates (lowest scores)
- **Pair Finding**: For each candidate, mark all indices sharing prime factors, then find an unmarked partner

#### Search Strategy
1. **With 2+ ones**: Use ones as first pair, search for second pair among remaining
2. **With 1 one**: Find coprime pair among non-ones, pair the remaining one with any other index  
3. **No ones**: Find two disjoint coprime pairs using the heuristic method

### 4. Key Optimizations
- **Fast I/O**: Custom scanner for competitive programming speed
- **Memory Efficient**: Custom IntList to avoid boxing overhead
- **Lazy Allocation**: Only create prime index lists when needed
- **Early Termination**: Stop as soon as valid solution is found

### 5. Why This Works
- **Completeness**: Brute force for small cases ensures we never miss solutions
- **Efficiency**: Heuristic approach handles large cases within time limits
- **Special Case Handling**: Ones are handled optimally since they're coprime with everything
- **Probabilistic Success**: For large random inputs, the heuristic approach has high success rate

