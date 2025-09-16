# Algorithms Assignment 1: Divide and Conquer

## Overview

This project implements classic divide-and-conquer algorithms with safe recursion patterns and provides comprehensive analysis of their performance characteristics.

## Learning Goals

- Implement classic divide-and-conquer algorithms with safe recursion patterns
- Analyse running-time recurrences using Master Theorem (3 cases) and Akra-Bazzi intuition
- Validate theoretical analysis with measurements
- Collect metrics (time, recursion depth, comparisons/allocations) and communicate results

## Algorithms Implemented (40%)

### 1. MergeSort (D&C, Master Case 2)
- Linear merge with reusable buffer
- Small-n cut-off using insertion sort
- **Status**: Not implemented

### 2. QuickSort (Robust)
- Randomised pivot selection
- Recurse on smaller partition, iterate over larger (bounded stack ≈ O(log n))
- **Status**: Not implemented

### 3. Deterministic Select
- Median-of-Medians algorithm, O(n) complexity
- Group by 5, median of medians as pivot
- In-place partition, recurse only into needed side
- **Status**: Not implemented

### 4. Closest Pair of Points (2D, O(n log n))
- Sort by x-coordinate, recursive split
- "Strip" check by y-order with classic 7-8 neighbour scan
- **Status**: Not implemented

## Architecture

### Metrics Collection
- Time measurement for performance analysis
- Recursion depth tracking for stack safety
- Comparison and allocation counters
- CSV output for data analysis

### Testing Strategy
- Correctness verification on random and adversarial arrays
- Recursion depth bounds verification
- Comparison with standard library implementations

## Analysis

*Analysis will be added as algorithms are implemented*

## Build and Run

```bash
# Compile
mvn compile

# Run tests
mvn test

# Run benchmarks
mvn exec:java -Dexec.mainClass="org.example.benchmarks.BenchmarkRunner"
```

## Project Status

- [x] Maven setup with JUnit 5 and JMH
- [x] Initial README
- [ ] Metrics system
- [ ] MergeSort implementation
- [ ] QuickSort implementation
- [ ] Utility functions
- [ ] Deterministic Select
- [ ] Closest Pair of Points
- [ ] CLI interface
- [ ] Comprehensive testing
- [ ] Performance analysis and report