package com.smartcity;

import com.smartcity.graph.dagsp.DAGShortestPath;
import com.smartcity.graph.scc.TarjanSCC;
import com.smartcity.graph.topo.TopologicalSort;
import com.smartcity.metrics.BasicMetrics;
import com.smartcity.metrics.Metrics;
import com.smartcity.model.Graph;
import com.smartcity.util.JsonParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SchedulingOptimizer {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java SchedulingOptimizer <path-to-tasks.json>");
            System.exit(1);
        }

        String filePath = args[0];
        
        try {
            JsonParser.GraphData graphData = JsonParser.parseGraph(filePath);
            Graph graph = graphData.graph;
            int source = graphData.source;

            System.out.println("=== Smart City Scheduling Optimizer ===\n");
            System.out.println("Graph: " + graph.getN() + " nodes");
            System.out.println("Source node: " + source);
            System.out.println("Weight model: " + graphData.weightModel + "\n");

            Metrics metrics = new BasicMetrics();

            System.out.println("=== 1. Strongly Connected Components (SCC) ===");
            TarjanSCC tarjan = new TarjanSCC(graph, metrics);
            TarjanSCC.SCCResult sccResult = tarjan.findSCCs();
            
            System.out.println("Number of SCCs: " + sccResult.components.size());
            for (int i = 0; i < sccResult.components.size(); i++) {
                List<Integer> component = sccResult.components.get(i);
                System.out.println("  SCC " + i + ": " + component + " (size: " + component.size() + ")");
            }
            System.out.println("Condensation graph: " + sccResult.condensationGraph.getN() + " nodes");
            System.out.println("Time: " + metrics.getElapsedNanos() / 1_000_000.0 + " ms");
            System.out.println("DFS visits: " + metrics.getDfsVisits());
            System.out.println("Edge traversals: " + metrics.getEdgeTraversals() + "\n");

            metrics.reset();

            System.out.println("=== 2. Topological Sort ===");
            TopologicalSort topoSort = new TopologicalSort(sccResult.condensationGraph, metrics);
            TopologicalSort.TopoResult topoResult = topoSort.topologicalSort();
            
            System.out.println("Topological order (components): " + topoResult.order);
            
            List<Integer> originalOrder = deriveOriginalOrder(topoResult.order, sccResult);
            System.out.println("Derived order (original tasks): " + originalOrder);
            System.out.println("Time: " + metrics.getElapsedNanos() / 1_000_000.0 + " ms");
            System.out.println("Queue pops: " + metrics.getQueuePops());
            System.out.println("Queue pushes: " + metrics.getQueuePushes() + "\n");

            metrics.reset();

            System.out.println("=== 3. Shortest Paths in DAG ===");
            DAGShortestPath dagSP = new DAGShortestPath(sccResult.condensationGraph, metrics);
            
            int sourceComponent = sccResult.vertexToComponent.get(source);
            System.out.println("Source component: " + sourceComponent);
            
            DAGShortestPath.ShortestPathResult shortestResult = dagSP.shortestPaths(sourceComponent);
            System.out.println("Shortest distances from component " + sourceComponent + ":");
            for (int i = 0; i < shortestResult.distances.length; i++) {
                if (shortestResult.distances[i] != Integer.MAX_VALUE) {
                    System.out.println("  Component " + i + ": " + shortestResult.distances[i]);
                }
            }
            
            if (shortestResult.distances.length > 0 && shortestResult.distances[0] != Integer.MAX_VALUE) {
                List<Integer> shortestPath = shortestResult.reconstructPath(0);
                System.out.println("Example shortest path to component 0: " + shortestPath);
            }
            
            System.out.println("Time: " + metrics.getElapsedNanos() / 1_000_000.0 + " ms");
            System.out.println("Relaxations: " + metrics.getRelaxations() + "\n");

            metrics.reset();

            System.out.println("=== 4. Longest Path (Critical Path) ===");
            DAGShortestPath.LongestPathResult longestResult = dagSP.longestPath(sourceComponent);
            
            System.out.println("Critical path length: " + longestResult.maxDistance);
            System.out.println("Critical path (components): " + longestResult.criticalPath);
            
            List<Integer> originalCriticalPath = mapComponentPathToOriginal(
                longestResult.criticalPath, sccResult);
            System.out.println("Critical path (original tasks): " + originalCriticalPath);
            System.out.println("Time: " + metrics.getElapsedNanos() / 1_000_000.0 + " ms");
            System.out.println("Relaxations: " + metrics.getRelaxations());

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<Integer> deriveOriginalOrder(List<Integer> componentOrder, 
                                                     TarjanSCC.SCCResult sccResult) {
        List<Integer> originalOrder = new java.util.ArrayList<>();
        Map<Integer, Integer> vertexToComponent = sccResult.vertexToComponent;
        
        for (int compId : componentOrder) {
            List<Integer> component = sccResult.components.get(compId);
            originalOrder.addAll(component);
        }
        
        return originalOrder;
    }

    private static List<Integer> mapComponentPathToOriginal(List<Integer> componentPath,
                                                           TarjanSCC.SCCResult sccResult) {
        List<Integer> originalPath = new java.util.ArrayList<>();
        
        for (int compId : componentPath) {
            List<Integer> component = sccResult.components.get(compId);
            if (!component.isEmpty()) {
                originalPath.add(component.get(0));
            }
        }
        
        return originalPath;
    }
}

