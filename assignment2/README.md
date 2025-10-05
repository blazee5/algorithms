# Kadane's Algorithm Implementation

Maximum subarray sum with position tracking - Assignment 2 implementation.

## Algorithm Overview

Kadane's Algorithm finds the contiguous subarray within a one-dimensional array of numbers which has the largest sum. This implementation tracks:
- Maximum sum value
- Start and end positions of the optimal subarray
- Performance metrics (comparisons, array accesses, execution time)

## Complexity Analysis

**Time Complexity:**
- Best Case: Θ(n)
- Worst Case: Θ(n)
- Average Case: Θ(n)

**Space Complexity:**
- O(1) - constant auxiliary space

The algorithm performs a single pass through the array, making it optimal for this problem.

## Project Structure

```
assignment2/
├── src/main/java/
│   ├── algorithms/KadaneAlgorithm.java
│   ├── metrics/PerformanceTracker.java
│   └── cli/BenchmarkRunner.java
├── src/test/java/
│   └── algorithms/KadaneAlgorithmTest.java
├── docs/
│   └── performance-plots/
├── pom.xml
└── README.md
```

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running Benchmarks

### Full benchmark suite (sizes: 100, 1000, 10000, 100000)
```bash
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner"
```

### Single size test
```bash
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner" -Dexec.args="5000"
```

### Custom sizes
```bash
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner" -Dexec.args="--custom 100 500 1000 5000"
```

### Help
```bash
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner" -Dexec.args="--help"
```

## Usage Example

```java
import algorithms.KadaneAlgorithm;
import metrics.PerformanceTracker;

int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

PerformanceTracker tracker = new PerformanceTracker();
KadaneAlgorithm algorithm = new KadaneAlgorithm(tracker);

tracker.startTiming();
KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
tracker.stopTiming();

System.out.println(result);
tracker.printSummary();
```

## Output Format

Benchmark results are exported to CSV with the following columns:
- InputSize
- ExecutionTimeNanos
- ExecutionTimeMillis
- Comparisons
- ArrayAccesses

## Features

- Single-pass linear time complexity
- Position tracking for maximum subarray
- Comprehensive edge case handling
- Performance metrics collection
- CSV export for benchmark results
- Configurable benchmark sizes
- Extensive unit test coverage

## Edge Cases Handled

- Null arrays
- Empty arrays
- Single element arrays
- All negative elements
- All positive elements
- Arrays with zeros
- Duplicate elements
- Large values

## Implementation Details

The algorithm maintains:
- `currentSum`: running sum of current subarray
- `maxSum`: maximum sum found so far
- `start`, `end`: indices of optimal subarray
- `tempStart`: potential new start position

Key optimization: resets current sum when adding an element is worse than starting fresh from that element.

## Performance Notes

- Warmup iterations: 5
- Benchmark iterations: 10 (averaged)
- Random input range: -1000 to 1000
- Fixed seed (42) for reproducibility

## Assignment Requirements Met

- ✅ Clean, readable Java code
- ✅ Comprehensive unit tests
- ✅ Input validation and error handling
- ✅ Metrics collection
- ✅ CLI interface for testing
- ✅ Position tracking implementation
- ✅ Performance benchmarking