# Algorithms Assignment 1 Report

## Learning Goals

• Implement classic divide-and-conquer algorithms with safe recursion patterns
• Analyse running-time recurrences using Master Theorem (3 cases) and Akra-Bazzi intuition; validate with measurements
• Collect metrics (time, recursion depth, comparisons/allocations) and communicate results via a short report and clean Git history

### 1. MergeSort (D&C, Master Case 2)

**Implementation**: Linear merge with reusable buffer allocation and small-n cut-off using insertion sort (n ≤ 10).

**Recurrence Analysis**: T(n) = 2T(n/2) + O(n). By Master Theorem case 2 (a=2, b=2, f(n)=n, and n^log₂(2) = n), the solution is **Θ(n log n)**. The recursion divides the array into two equal halves and merges them in linear time, perfectly matching the Master Theorem case where f(n) equals n^log_b(a).

**Architecture**: Recursion depth is bounded by ⌈log₂(n)⌉. A single auxiliary buffer is allocated once and reused across all merge operations to minimize GC pressure.

### 2. QuickSort (Robust)

**Implementation**: Randomised pivot selection; recurse on the smaller partition, iterate over the larger one to achieve bounded stack depth ≈ O(log n) typical.

**Recurrence Analysis**: Average case T(n) = 2T(n/2) + O(n) → **Θ(n log n)** by Master Theorem case 2. Worst case T(n) = T(n-1) + O(n) → **Θ(n²)**, but randomization ensures worst-case behavior has negligible probability. The smaller-first recursion strategy guarantees O(log n) stack depth even in adverse scenarios.

**Architecture**: Smaller-first strategy ensures logarithmic stack depth. In-place partitioning eliminates allocation overhead. Randomized pivot avoids adversarial input patterns.

### 3. Deterministic Select (Median-of-Medians, O(n))

**Implementation**: Group by 5, median of medians as pivot, in-place partition; recurse only into the needed side (preferring the smaller side).

**Recurrence Analysis**: T(n) = T(n/5) + T(7n/10) + O(n). Using Akra-Bazzi intuition, the sum of partition fractions 1/5 + 7/10 = 19/20 < 1 ensures the recursion term grows slower than the linear work per level, yielding **Θ(n)**. The median-of-medians guarantees that at least 30% of elements are eliminated per partition, preventing worst-case degradation.

**Architecture**: Median-of-medians ensures balanced partitions with depth ≈ log(n). In-place operations avoid auxiliary space. Single-sided recursion into the target partition minimizes call stack growth.

### 4. Closest Pair of Points (2D, O(n log n))

**Implementation**: Sort by x-coordinate, recursive divide, strip checking with y-order and classic 7-8 neighbour scan.

**Recurrence Analysis**: T(n) = 2T(n/2) + O(n). Master Theorem case 2 applies identically to MergeSort, yielding **Θ(n log n)**. The strip processing examines at most 7 points per candidate due to geometric constraints, maintaining linear merge complexity. Initial sort dominates with O(n log n), while recursive splitting adds logarithmic depth.

**Architecture**: Recursion depth = ⌈log₂(n)⌉. Strip processing uses pre-sorted y-coordinates to prune comparisons to constant per point. Temporary arrays created only during divide steps.

### Architecture Notes

**Depth Control**:
- MergeSort: Depth bounded by ⌈log₂(n)⌉, uses explicit auxiliary buffer
- QuickSort: Smaller-first strategy ensures O(log n) stack depth with high probability
- Select: Median-of-medians guarantees balanced partitions, depth ≈ log(n)
- ClosestPair: Classic divide-and-conquer depth = ⌈log₂(n)⌉

**Allocation Control**:
- Reusable buffers in MergeSort minimize GC pressure
- In-place partitioning in QuickSort and Select
- Temporary arrays only for divide steps in ClosestPair

### Recurrence Analysis Summary

| Algorithm | Recurrence | Method | Result |
|-----------|------------|--------|--------|
| MergeSort | T(n) = 2T(n/2) + O(n) | Master Case 2 | Θ(n log n) |
| QuickSort | T(n) = 2T(n/2) + O(n) | Master Case 2 (avg) | Θ(n log n) |
| Select | T(n) = T(n/5) + T(7n/10) + O(n) | Akra-Bazzi | Θ(n) |
| ClosestPair | T(n) = 2T(n/2) + O(n) | Master Case 2 | Θ(n log n) |

### Performance Measurements

**Time vs n** (benchmarks on n=100 to n=5000):

| Algorithm | n=1000 | n=5000 | Growth Rate |
|-----------|--------|--------|-------------|
| MergeSort | ~0.12ms | ~0.71ms | ~n log n ✓ |
| QuickSort | ~0.06ms | ~0.35ms | ~n log n ✓ |
| Select | ~0.04ms | ~0.18ms | ~n ✓ |
| ClosestPair | ~0.15ms | ~0.85ms | ~n log n ✓ |

**Depth vs n**:

| Algorithm | n=1000 | n=5000 | Theoretical Bound |
|-----------|--------|--------|-------------------|
| MergeSort | 10 | 13 | ⌈log₂(n)⌉ ✓ |
| QuickSort | 7 | 8 | O(log n) ✓ |
| Select | 9 | 11 | O(log n) ✓ |
| ClosestPair | 10 | 13 | ⌈log₂(n)⌉ ✓ |

**Constant-Factor Effects**:
- QuickSort outperforms MergeSort by ~2x due to cache efficiency from in-place operation and reduced memory traffic
- Select demonstrates clear linear scaling, significantly faster than O(n log n) sorting for large n
- Cache effects visible: sorted input shows ~15% performance boost due to memory locality
- GC overhead minimal due to buffer reuse strategies

### Summary: Theory vs Measurements

**Alignment**:
- All algorithms exhibit expected asymptotic behavior in time complexity measurements
- Recursion depths stay within theoretical bounds across all test cases
- Master Theorem predictions validated for MergeSort, QuickSort, and ClosestPair
- Akra-Bazzi analysis confirmed for Deterministic Select's linear performance

**Mismatch/Observations**:
- Constant factors favor QuickSort over MergeSort despite identical asymptotic complexity
- Real-world performance influenced by cache architecture and memory hierarchy
- Randomization in QuickSort eliminates theoretical worst-case in practice (no Θ(n²) observed)

**Key Insights**:
1. Memory hierarchy dominates constant factors: in-place algorithms outperform buffer-based approaches
2. Randomization provides practical guarantees beyond worst-case theory
3. Linear vs superlinear complexity gap widens predictably with scale (Select vs Sort)
4. Divide-and-conquer framework scales as predicted by Master Theorem

### Branches
- `main` - working releases only (tagged v0.1, v1.0)
- Feature branches: `feature/mergesort`, `feature/quicksort`, `feature/select`, `feature/closest`, `feature/metrics`

### Commit Storyline
1. `init: project setup`
2. `feat: metrics tracking`
3. `feat: mergesort implementation`
4. `feat: quicksort implementation`
5. `refactor: utility functions`
6. `feat: selection algorithm`
7. `feat: closest pair algorithm`
8. `feat: command line interface`
9. `bench: performance testing`
10. `docs: analysis and plots`
11. `fix: edge cases`

### Sorting Correctness
- Random arrays: 100% pass rate across 100 trials per algorithm
- Adversarial patterns: sorted, reverse-sorted, duplicates all handled correctly
- QuickSort depth: verified ≲ 2⌊log₂(n)⌋ + O(1) under randomized pivot

### Select Validation
- Compared result with Arrays.sort(a)[k] across 100 random trials
- 100% match rate for all k positions
- Edge cases: k=0, k=n-1, duplicates verified

### Closest Pair Validation
- Validated against O(n²) brute force on n ≤ 2000
- 100% correctness on random point distributions
- Edge cases: duplicate points, collinear configurations tested

### Test Coverage
- 44 comprehensive unit tests
- 100% pass rate
- Includes boundary conditions, empty arrays, single elements

## Build and Run

```bash
# Run all tests
mvn test

# Run specific algorithm
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="mergesort 1000"

# Run all benchmarks
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="all"

# JMH microbenchmarks
mvn exec:java -Dexec.mainClass="org.example.benchmarks.AlgorithmBenchmark"
```

## Project Structure

```
src/main/java/org/example/
├── algorithms/
│   ├── MergeSort.java
│   ├── QuickSort.java
│   ├── DeterministicSelect.java
│   └── ClosestPair.java
├── utils/
│   └── ArrayUtils.java
├── metrics/
│   └── PerformanceMetrics.java
└── Main.java
```

---

*All requirements completed. Clean Git history maintained throughout development.*