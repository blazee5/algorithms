# Assignment 3: City Transportation Network Optimization

## Overview

This project implements **Prim's** and **Kruskal's** algorithms to optimize a city's transportation network by finding the Minimum Spanning Tree (MST). The goal is to connect all city districts with the minimum possible construction cost.

## Project Structure

```
assignment3/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── transportation/
│                   ├── TransportationOptimizer.java (Main class)
│                   ├── algorithm/
│                   │   ├── PrimAlgorithm.java
│                   │   ├── KruskalAlgorithm.java
│                   │   └── UnionFind.java
│                   ├── model/
│                   │   ├── Graph.java
│                   │   ├── Edge.java
│                   │   └── MSTResult.java
│                   └── util/
│                       └── JsonParser.java
├── bin/ (compiled classes)
├── ass_3_input.json (input data)
├── ass_3_output.json (generated results)
└── README.md
```

## Implementation Details

### Algorithms Implemented

1. **Prim's Algorithm**

   - Time Complexity: O(E log V) using a priority queue
   - Space Complexity: O(V + E)
   - Starts from an arbitrary vertex and grows the MST by adding minimum weight edges

2. **Kruskal's Algorithm**
   - Time Complexity: O(E log E) due to edge sorting
   - Space Complexity: O(V + E)
   - Uses Union-Find (Disjoint Set Union) to detect cycles
   - Processes edges in ascending order by weight

### Key Features

- **Operation Counting**: Both algorithms count key operations (comparisons, unions, etc.)
- **Execution Timing**: Measures algorithm performance in milliseconds
- **JSON I/O**: Reads input graphs and writes results in JSON format
- **Union-Find Optimization**: Uses path compression and union by rank
- **Comprehensive Output**: Provides MST edges, total cost, operation count, and execution time

## Compilation and Execution

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6+ (for dependency management)

### Using Maven (Recommended)

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.transportation.TransportationOptimizer"
```

## Input Format

The input file `ass_3_input.json` should follow this structure:

```json
{
  "graphs": [
    {
      "id": 1,
      "nodes": ["A", "B", "C", "D", "E"],
      "edges": [
        {"from": "A", "to": "B", "weight": 4},
        {"from": "A", "to": "C", "weight": 3},
        ...
      ]
    }
  ]
}
```

## Output Format

The program generates `ass_3_output.json` with the following structure:

```json
{
  "results": [
    {
      "graph_id": 1,
      "input_stats": {
        "vertices": 5,
        "edges": 7
      },
      "prim": {
        "mst_edges": [...],
        "total_cost": 16,
        "operations_count": 42,
        "execution_time_ms": 1.52
      },
      "kruskal": {
        "mst_edges": [...],
        "total_cost": 16,
        "operations_count": 37,
        "execution_time_ms": 1.28
      }
    }
  ]
}
```

## Algorithm Comparison

### Prim's Algorithm

- **Best for**: Dense graphs, adjacency list representation
- **Characteristics**:
  - Builds MST incrementally from a starting vertex
  - Suitable when graph is represented with adjacency lists
  - Good cache locality when vertices are processed in sequence

### Kruskal's Algorithm

- **Best for**: Sparse graphs, edge list representation
- **Characteristics**:
  - Considers edges globally (sorted by weight)
  - Requires Union-Find data structure
  - More efficient for sparse graphs
  - Naturally parallel (edges can be sorted independently)

### Performance Observations

From the test results:

- Both algorithms **always produce the same total MST cost** (guaranteed correctness)
- The specific edges and order may differ, but both form valid MSTs
- Operation counts vary based on algorithm-specific steps
- Execution time depends on graph characteristics and implementation details

## Data Structures Used

1. **Graph**: Adjacency list representation with vertex and edge lists
2. **Priority Queue**: Min-heap for Prim's algorithm
3. **Union-Find**: Disjoint set with path compression and union by rank
4. **Edge**: Comparable edge class for sorting and comparison

## Testing

The project includes two test graphs:

- **Graph 1**: 5 vertices, 7 edges
- **Graph 2**: 4 vertices, 5 edges

Both graphs successfully produce identical MST costs from both algorithms, validating correctness.

