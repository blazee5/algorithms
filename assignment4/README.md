# Smart City Scheduling Optimizer - Assignment 4

## Overview

For this assignment, I implemented graph algorithms for smart city task scheduling. The main components are:

1. **Strongly Connected Components (SCC)** - I used Tarjan's algorithm
2. **Topological Sorting** - I went with Kahn's algorithm (BFS-based)
3. **Shortest and Longest Paths in DAGs** - implemented using DP over topological order

The idea is to process city-service tasks (like street cleaning, repairs, maintenance) with dependency graphs. The hardest part was handling the cyclic dependencies and building the condensation graph correctly.

## Algorithms Implemented

### 1. Strongly Connected Components (SCC)

I chose Tarjan's algorithm over Kosaraju because it only needs one DFS pass and has better cache locality.

- **Time Complexity**: O(V + E)
- **Output**: List of SCCs, condensation graph (DAG)
- **Metrics tracked**: DFS visits, edge traversals

### 2. Topological Sort

For topological sorting I used Kahn's algorithm (the BFS-based one). It's easier to implement than the DFS version and the queue operations are straightforward to track.

- **Time Complexity**: O(V + E)
- **Output**: Valid topological order of components and original tasks
- **Metrics tracked**: Queue pops/pushes

### 3. Shortest/Longest Paths in DAG

This part uses dynamic programming over the topological order. For longest paths I basically inverted the logic (using max instead of min).

- **Time Complexity**: O(V + E)
- **Features**:
  - Single-source shortest paths
  - Longest path (critical path)
- **Metrics tracked**: Relaxations
- **Weight Model**: Edge weights (from the JSON input)

## Input Format

The input file (`tasks.json`) follows this structure:

```json
{
  "directed": true,
  "n": 8,
  "edges": [
    { "u": 0, "v": 1, "w": 3 },
    { "u": 1, "v": 2, "w": 2 }
  ],
  "source": 4,
  "weight_model": "edge"
}
```

- `directed`: Always true (directed graph)
- `n`: Number of vertices (tasks)
- `edges`: List of directed edges with weights
- `source`: Source node for shortest/longest path computations
- `weight_model`: "edge" (weights on edges)

## Building and Running

### Prerequisites

- Java 11+
- Maven 3.6+

### Build

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Run Main Program

```bash
mvn compile exec:java -Dexec.mainClass="com.smartcity.SchedulingOptimizer" -Dexec.args="data/tasks.json"
```

Or compile and run directly:

```bash
mvn package
java -cp target/scheduling-optimizer-1.0.0.jar com.smartcity.SchedulingOptimizer data/tasks.json
```

### Generate Datasets

```bash
mvn compile exec:java -Dexec.mainClass="com.smartcity.util.DatasetGenerator"
```

## Datasets

I generated 9 datasets stored in `/data/` following the assignment requirements:

### Small (6-10 nodes)

- `small_1.json`: 8 nodes, 10 edges, has cycles
- `small_2.json`: 6 nodes, 8 edges, pure DAG
- `small_3.json`: 10 nodes, 12 edges, has cycles

### Medium (10-20 nodes)

- `medium_1.json`: 15 nodes, 25 edges, multiple SCCs
- `medium_2.json`: 12 nodes, 18 edges, pure DAG
- `medium_3.json`: 20 nodes, 30 edges, multiple SCCs

### Large (20-50 nodes)

- `large_1.json`: 30 nodes, 60 edges, multiple SCCs
- `large_2.json`: 25 nodes, 45 edges, pure DAG
- `large_3.json`: 50 nodes, 100 edges, multiple SCCs

The generator uses a fixed seed (42) so the results are reproducible.

## Performance Metrics

The system tracks:

- **DFS visits**: Number of DFS calls (SCC)
- **Edge traversals**: Edges explored
- **Queue operations**: Pops/pushes (Topological sort)
- **Relaxations**: Edge relaxations (DAG shortest paths)
- **Execution time**: Nanosecond precision via `System.nanoTime()`

## Example Output

```
=== Smart City Scheduling Optimizer ===

Graph: 8 nodes
Source node: 4
Weight model: edge

=== 1. Strongly Connected Components (SCC) ===
Number of SCCs: 3
  SCC 0: [1, 2, 3] (size: 3)
  SCC 1: [0] (size: 1)
  SCC 2: [4, 5, 6, 7] (size: 4)
Condensation graph: 3 nodes
Time: 0.234 ms
DFS visits: 8
Edge traversals: 7

=== 2. Topological Sort ===
Topological order (components): [2, 0, 1]
Derived order (original tasks): [4, 5, 6, 7, 1, 2, 3, 0]
Time: 0.045 ms
Queue pops: 3
Queue pushes: 3

=== 3. Shortest Paths in DAG ===
Source component: 2
Shortest distances from component 2:
  Component 2: 0
  Component 0: 5
Time: 0.032 ms
Relaxations: 2

=== 4. Longest Path (Critical Path) ===
Critical path length: 8
Critical path (components): [2, 0]
Critical path (original tasks): [4, 1]
Time: 0.028 ms
Relaxations: 2
```

## Experimental Results

Here are the performance metrics I collected from running all three algorithms on the 9 datasets.

### Dataset Summary

| Dataset  | Nodes (n) | Edges (m) | Type   | SCCs | Structure                       |
| -------- | --------- | --------- | ------ | ---- | ------------------------------- |
| small_1  | 8         | 7         | Cyclic | 5    | Contains 1 large SCC (4 nodes)  |
| small_2  | 6         | 7         | DAG    | 6    | Pure DAG (all single-node SCCs) |
| small_3  | 10        | 11        | Cyclic | 7    | Contains 1 large SCC (4 nodes)  |
| medium_1 | 15        | 25        | Cyclic | 1    | Single large SCC (entire graph) |
| medium_2 | 12        | 18        | DAG    | 12   | Pure DAG (all single-node SCCs) |
| medium_3 | 20        | 27        | Cyclic | 1    | Single large SCC (entire graph) |
| large_1  | 30        | 58        | Cyclic | 1    | Single large SCC (entire graph) |
| large_2  | 25        | 45        | DAG    | 25   | Pure DAG (all single-node SCCs) |
| large_3  | 50        | 99        | Cyclic | 1    | Single large SCC (entire graph) |

### SCC Detection Results (Tarjan's Algorithm)

| Dataset  | Nodes | Edges | SCCs | DFS Visits | Edge Trav. | Time (ms) |
| -------- | ----- | ----- | ---- | ---------- | ---------- | --------- |
| small_1  | 8     | 7     | 5    | 8          | 7          | 0.046     |
| small_2  | 6     | 7     | 6    | 6          | 7          | 0.026     |
| small_3  | 10    | 11    | 7    | 10         | 11         | 0.030     |
| medium_1 | 15    | 25    | 1    | 15         | 25         | 0.035     |
| medium_2 | 12    | 18    | 12   | 12         | 18         | 0.037     |
| medium_3 | 20    | 27    | 1    | 20         | 27         | 0.035     |
| large_1  | 30    | 58    | 1    | 30         | 58         | 0.048     |
| large_2  | 25    | 45    | 25   | 25         | 45         | 0.053     |
| large_3  | 50    | 99    | 1    | 50         | 99         | 0.052     |

**My observations**:

- DFS visits always equal the number of nodes (each vertex visited exactly once)
- Edge traversals match the number of edges in the graph
- Execution time scales linearly with graph size, which confirms the O(V + E) complexity
- Pure DAGs produce more SCCs (each node is its own component)
- Cyclic graphs compress into fewer, larger SCCs which makes sense

### Topological Sort Results (Kahn's Algorithm)

| Dataset  | Nodes | Cond. Nodes | Queue Pops | Queue Pushes | Time (ms) |
| -------- | ----- | ----------- | ---------- | ------------ | --------- |
| small_1  | 8     | 5           | 5          | 5            | 0.026     |
| small_2  | 6     | 6           | 6          | 6            | 0.022     |
| small_3  | 10    | 7           | 7          | 7            | 0.023     |
| medium_1 | 15    | 1           | 1          | 1            | 0.021     |
| medium_2 | 12    | 12          | 12         | 12           | 0.031     |
| medium_3 | 20    | 1           | 1          | 1            | 0.016     |
| large_1  | 30    | 1           | 1          | 1            | 0.019     |
| large_2  | 25    | 25          | 25         | 25           | 0.039     |
| large_3  | 50    | 1           | 1          | 1            | 0.017     |

**What I noticed**:

- Queue pops and pushes are always equal (makes sense - each node enters and leaves once)
- Operations count matches the number of condensation graph nodes
- Graphs with single SCCs have minimal topological work (just 1 component)
- Execution time is consistently fast (under 0.04 ms for all datasets)
- For highly cyclic graphs, performance doesn't really depend on the original graph size

### DAG Shortest Path Results

| Dataset  | Cond. Nodes | Relaxations (SP) | Time SP (ms) | Relaxations (LP) | Time LP (ms) | Critical Path Length |
| -------- | ----------- | ---------------- | ------------ | ---------------- | ------------ | -------------------- |
| small_1  | 5           | 2                | 0.018        | 2                | 0.010        | 7                    |
| small_2  | 6           | 1                | 0.016        | 1                | 0.010        | 8                    |
| small_3  | 7           | 0                | 0.014        | 0                | 0.009        | 0                    |
| medium_1 | 1           | 0                | 0.014        | 0                | 0.006        | 0                    |
| medium_2 | 12          | 8                | 0.021        | 8                | 0.015        | 20                   |
| medium_3 | 1           | 0                | 0.009        | 0                | 0.005        | 0                    |
| large_1  | 1           | 0                | 0.011        | 0                | 0.006        | 0                    |
| large_2  | 25          | 33               | 0.033        | 33               | 0.029        | 110                  |
| large_3  | 1           | 0                | 0.010        | 0                | 0.005        | 0                    |

**What I found**:

- Relaxations correlate with condensation graph edges (not the original edges)
- Single-component graphs (fully cyclic) have 0 relaxations - nothing to relax
- DAG structures show more relaxations (like large_2: 33 relaxations for 25 nodes)
- Longest path computation is slightly faster than shortest path even though they have the same complexity
- Critical path length varies a lot based on graph structure and weights

### Analysis by Graph Structure

#### Pure DAGs (small_2, medium_2, large_2)

These graphs have no cycles, so each node is its own SCC. SCC detection is fast but finds many small components. Topological sort does more work (one queue operation per node). DAG shortest path has higher relaxation counts and produces meaningful shortest/longest paths. These would be ideal for dependency scheduling when you don't have circular dependencies.

#### Fully Cyclic Graphs (medium_1, medium_3, large_1, large_3)

These collapse into a single large SCC containing all nodes. SCC detection visits all nodes but only finds one component. Topological sort has minimal work (just 1 component). DAG shortest path has zero relaxations since everything condensed to a single node. In practice this means you'd need to break cycles somehow.

#### Mixed Structures (small_1, small_3)

These have multiple SCCs of different sizes. SCC detection does balanced work with some compression. Topological sort has moderate queue operations. DAG shortest path has some relaxations on the condensed DAG. This is probably more realistic for real-world scenarios.

## Algorithm Analysis

### SCC Detection (Tarjan's)

The main bottleneck is the DFS traversal, especially for sparse graphs. Dense graphs increase edge traversals linearly. Larger SCCs need more DFS calls but give better compression ratios. From my experiments, it definitely scales linearly with V + E. One thing to watch out for is memory - it needs O(V) stack space in worst case due to recursion depth.

### Topological Sort (Kahn's)

Kahn's algorithm is optimal for DAGs. The bottleneck is queue operations which scale with graph size. It requires SCC preprocessing if the graph has cycles (which is why I run SCC first). Performance is really good on condensed graphs - under 0.04 ms for all my tests. The max queue size equals the number of nodes with in-degree 0.

### DAG Shortest/Longest Paths

The big advantage here is O(V + E) complexity compared to O(E log V) for Dijkstra. The bottleneck is relaxations which equal the edge count in the condensation graph. Longest path is useful for finding the critical path (bottlenecks in task scheduling). Pre-computing the topological order enables the DP approach. Obviously this only works on acyclic structures - that's why I need the SCC compression step first.

## Testing

I wrote JUnit tests covering:

- Simple cycles and pure DAGs
- Multiple SCCs
- Edge cases (empty graphs, single nodes, unreachable nodes)
- Correctness verification for all algorithms

Run tests with:

```bash
mvn test
```

## Design Decisions

1. **SCC Algorithm**: I chose Tarjan over Kosaraju mainly because it only needs one DFS pass and has better cache locality
2. **Topological Sort**: Went with Kahn's algorithm - it's simpler to implement and efficient
3. **Weight Model**: Using edge weights as specified in the requirements
4. **Condensation**: Built a minimal DAG that preserves reachability between components

## Code Organization

- **Packages**: Organized by algorithm type (`graph.scc`, `graph.topo`, `graph.dagsp`)
- **Documentation**: Added Javadoc for the public classes
- **Tests**: JUnit tests including edge cases
- **Metrics**: Used a common metrics interface for tracking performance

## Running from Scratch

All datasets are generated deterministically using seed=42. To build and run from a clean clone:

```bash
git clone <repo>
cd assignment4
mvn clean install
mvn exec:java -Dexec.mainClass="com.smartcity.util.DatasetGenerator"
mvn exec:java -Dexec.mainClass="com.smartcity.SchedulingOptimizer" -Dexec.args="data/small_1.json"
```

## Conclusions

### When to Use Each Algorithm

1. **SCC Detection**: Essential when you need to analyze dependencies in cyclic graphs. Run this first to detect and compress cycles.
2. **Topological Sort**: Required for scheduling tasks with dependencies. Only works on DAGs, so run SCC first if you have cycles.
3. **DAG Shortest Paths**: Optimal for minimizing task duration. Much faster than Dijkstra for DAGs.
4. **Critical Path**: Use this to identify bottlenecks in project scheduling - tells you which tasks are most critical.

### Practical Takeaways

- Always run SCC compression before topological sort if you might have cycles
- For DAGs, the specialized shortest path algorithm is way more efficient than general algorithms like Dijkstra
- Critical path analysis is really useful for finding project bottlenecks
- Tracking metrics helped me understand where the performance bottlenecks are

Overall this assignment helped me understand how these algorithms work together - you need SCC to handle cycles, then topological sort to order things, then shortest/longest paths for scheduling optimization.
