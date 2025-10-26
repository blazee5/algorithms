# Analytical Report: City Transportation Network Optimization
## Minimum Spanning Tree Algorithm Comparison

**Course**: Algorithms and Data Structures
**Assignment**: Assignment 3 - MST Optimization
**Date**: October 26, 2025

---

## Executive Summary

This report analyzes the implementation and performance of two fundamental graph algorithms—**Prim's Algorithm** and **Kruskal's Algorithm**—for solving the Minimum Spanning Tree (MST) problem in the context of optimizing a city's transportation network. Both algorithms were implemented in Java and tested on multiple graph datasets to compare their efficiency, performance characteristics, and practical applicability.

---

## 1. Input Data Summary

The analysis utilized two test graphs representing different city transportation scenarios:

### Graph 1: Medium-Sized Network
- **Vertices**: 5 districts (A, B, C, D, E)
- **Edges**: 7 potential roads
- **Edge weights**: Range from 2 to 8 (construction costs)
- **Graph density**: 7 edges out of possible 10 (70% dense)

### Graph 2: Small Network
- **Vertices**: 4 districts (A, B, C, D)
- **Edges**: 5 potential roads
- **Edge weights**: Range from 1 to 5
- **Graph density**: 5 edges out of possible 6 (83% dense)

Both graphs represent **undirected, weighted, connected graphs** where:
- Each vertex represents a city district
- Each edge represents a potential road with construction cost
- The goal is to connect all districts with minimum total cost

---

## 2. Algorithm Implementation Details

### 2.1 Prim's Algorithm

**Approach**: Greedy, vertex-centric
**Data Structures**: Priority Queue (Min-Heap), Visited Set
**Time Complexity**: O(E log V) with binary heap
**Space Complexity**: O(V + E)

**Implementation Strategy**:
1. Start with an arbitrary vertex (first in the set)
2. Maintain a priority queue of edges sorted by weight
3. Repeatedly select the minimum weight edge connecting a visited vertex to an unvisited vertex
4. Mark the new vertex as visited and add its edges to the priority queue
5. Continue until all vertices are visited

**Key Operations Counted**:
- Vertex additions to visited set
- Edge insertions/removals from priority queue
- Comparisons for checking visited status
- Total cost accumulation

### 2.2 Kruskal's Algorithm

**Approach**: Greedy, edge-centric
**Data Structures**: Edge List, Union-Find (Disjoint Set Union)
**Time Complexity**: O(E log E) for sorting + O(E α(V)) for union-find
**Space Complexity**: O(V + E)

**Implementation Strategy**:
1. Sort all edges by weight in ascending order
2. Initialize Union-Find structure with each vertex in its own set
3. Iterate through sorted edges
4. For each edge, check if it connects vertices in different sets (no cycle)
5. If valid, add edge to MST and union the sets
6. Continue until V-1 edges are added

**Key Operations Counted**:
- Sorting comparisons (n log n approximation)
- Union-Find operations (find and union calls)
- Edge examinations
- Total cost accumulation

**Union-Find Optimizations**:
- **Path Compression**: Flattens tree structure during find operations
- **Union by Rank**: Attaches smaller tree under larger tree root

---

## 3. Results Analysis

### 3.1 Graph 1 Results

| Metric | Prim's Algorithm | Kruskal's Algorithm |
|--------|------------------|---------------------|
| **MST Total Cost** | 16 | 16 |
| **MST Edges** | 4 | 4 |
| **Operations Count** | 42 | 68 |
| **Execution Time** | 3.33 ms | 0.78 ms |

**MST Edges (Prim's)**:
```
A-C (3), C-B (2), B-D (5), D-E (6)
Total: 3 + 2 + 5 + 6 = 16
```

**MST Edges (Kruskal's)**:
```
B-C (2), A-C (3), B-D (5), D-E (6)
Total: 2 + 3 + 5 + 6 = 16
```

**Observations**:
- ✓ Both algorithms produce **identical total cost** (16)
- The edge sets are equivalent (same edges, different representation)
- Kruskal's performed **fewer operations** in terms of algorithmic steps but had **faster execution time**
- Operation count difference reflects different counting methodologies

### 3.2 Graph 2 Results

| Metric | Prim's Algorithm | Kruskal's Algorithm |
|--------|------------------|---------------------|
| **MST Total Cost** | 6 | 6 |
| **MST Edges** | 3 | 3 |
| **Operations Count** | 29 | 47 |
| **Execution Time** | 0.02 ms | 0.02 ms |

**MST Edges (Both Algorithms)**:
```
A-B (1), B-C (2), C-D (3)
Total: 1 + 2 + 3 = 6
```

**Observations**:
- ✓ Both algorithms produce **identical results** (same cost and edges)
- Execution times are negligible for this small graph
- Prim's had fewer operations on this denser graph
- Results validate correctness of both implementations

---

## 4. Comparative Analysis

### 4.1 Efficiency Comparison

**Operation Count**:
- **Graph 1**: Prim's (42) vs. Kruskal's (68) - Prim's more efficient
- **Graph 2**: Prim's (29) vs. Kruskal's (47) - Prim's more efficient

The operation count favors Prim's algorithm in both test cases. However, operation counting methodologies differ:
- Prim's counts priority queue operations and comparisons
- Kruskal's includes sorting overhead (n log n approximation) and Union-Find operations

**Execution Time**:
- **Graph 1**: Kruskal's significantly faster (0.78 ms vs 3.33 ms)
- **Graph 2**: Both equally fast (0.02 ms each)

For the medium-sized graph, Kruskal's execution was **4.3× faster** despite higher operation count, suggesting better constant factors and cache efficiency in practice.

### 4.2 Algorithm Characteristics

| Aspect | Prim's Algorithm | Kruskal's Algorithm |
|--------|------------------|---------------------|
| **Approach** | Vertex-centric (grows tree) | Edge-centric (global view) |
| **Best Graph Type** | Dense graphs | Sparse graphs |
| **Data Structure** | Priority Queue | Sorted Edge List + Union-Find |
| **Starting Point** | Requires starting vertex | No starting vertex needed |
| **Parallelization** | Difficult | Easier (edge sorting is parallel) |
| **Memory Access** | Better locality | Random access for Union-Find |
| **Implementation** | Simpler (no Union-Find) | More complex (Union-Find required) |

### 4.3 Theoretical vs. Practical Performance

**Theoretical Complexity**:
- Prim's: O(E log V) = O(7 log 5) ≈ 11.9 for Graph 1
- Kruskal's: O(E log E) = O(7 log 7) ≈ 13.7 for Graph 1

**Practical Observations**:
- Both graphs are **dense** (70% and 83% connectivity)
- Prim's should theoretically perform better on dense graphs
- Execution time results show Kruskal's faster in practice for Graph 1
- This suggests implementation-specific optimizations and JVM performance characteristics

---

## 5. Conclusions and Recommendations

### 5.1 Key Findings

1. **Correctness Validation**: Both algorithms consistently produce the correct MST with identical total cost, validating their implementations.

2. **Performance Trade-offs**:
   - Operation count metrics favor Prim's algorithm
   - Actual execution time favors Kruskal's for larger graphs
   - Very small graphs show negligible performance differences

3. **Graph Density Impact**:
   - Both test graphs were dense (>70% connectivity)
   - Prim's theoretical advantage on dense graphs not realized in practice
   - Implementation quality and constant factors matter more than asymptotic complexity for small graphs

### 5.2 Algorithm Selection Guidelines

**Choose Prim's Algorithm when**:
- Graph is represented as an adjacency list
- Working with very dense graphs (E ≈ V²)
- Starting from a specific vertex is natural for the problem
- Simpler implementation is preferred (no Union-Find needed)
- Memory locality is critical

**Choose Kruskal's Algorithm when**:
- Graph is represented as an edge list
- Working with sparse graphs (E << V²)
- Edge weights need to be globally sorted anyway
- Parallelization is important (edge sorting is parallelizable)
- The problem naturally involves considering edges globally

### 5.3 Practical Considerations for City Transportation Networks

For real-world city transportation optimization:

1. **Graph Size**: Cities typically have sparse road networks (E ≈ V), favoring Kruskal's algorithm

2. **Edge Representation**: Road construction projects often start with edge lists (potential roads with costs), making Kruskal's more natural

3. **Distributed Computing**: Kruskal's edge-centric approach allows for distributed sorting and processing

4. **Incremental Updates**: If new districts or roads are added frequently, Prim's might be easier to update incrementally

5. **Visualization**: Prim's tree-growing approach is easier to visualize for stakeholders

### 5.4 Future Improvements

1. **Larger Test Sets**: Test with graphs of varying sizes (10s, 100s, 1000s of vertices)

2. **Density Analysis**: Create sparse (E ≈ V) and dense (E ≈ V²) graph variants

3. **Advanced Data Structures**:
   - Fibonacci heap for Prim's (O(E + V log V))
   - Borůvka's algorithm for parallel MST

4. **Benchmarking**: More rigorous timing with warm-up runs and statistical analysis

5. **Real-World Data**: Apply to actual city transportation networks
