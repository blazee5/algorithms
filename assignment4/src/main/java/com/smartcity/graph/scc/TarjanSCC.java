package com.smartcity.graph.scc;

import com.smartcity.model.Graph;
import com.smartcity.metrics.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private int index;
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> components;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public SCCResult findSCCs() {
        metrics.startTimer();
        int n = graph.getN();
        index = 0;
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        components = new ArrayList<>();
        
        Arrays.fill(ids, -1);
        
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }
        
        metrics.stopTimer();
        
        Map<Integer, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int vertex : components.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }
        
        Graph condensationGraph = buildCondensationGraph(vertexToComponent);
        
        return new SCCResult(components, condensationGraph, vertexToComponent);
    }

    private void dfs(int u) {
        metrics.incrementDfsVisits();
        // Set discovery time and low-link value
        ids[u] = index;
        low[u] = index;
        index++;
        stack.push(u);
        onStack[u] = true;

        for (Graph.Edge edge : graph.getNeighbors(u)) {
            metrics.incrementEdgeTraversals();
            int v = edge.to;
            
            if (ids[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], ids[v]);
            }
        }

        // If u is a root node, pop the stack and create an SCC
        if (low[u] == ids[u]) {
            List<Integer> component = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                component.add(v);
            } while (v != u);
            components.add(component);
        }
    }

    private Graph buildCondensationGraph(Map<Integer, Integer> vertexToComponent) {
        int numComponents = components.size();
        Graph condensation = new Graph(numComponents);
        Set<String> edgeSet = new HashSet<>();

        for (int u = 0; u < graph.getN(); u++) {
            int compU = vertexToComponent.get(u);
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                int compV = vertexToComponent.get(v);
                
                if (compU != compV) {
                    String edgeKey = compU + "," + compV;
                    if (!edgeSet.contains(edgeKey)) {
                        edgeSet.add(edgeKey);
                        int minWeight = Integer.MAX_VALUE;
                        for (Graph.Edge e : graph.getNeighbors(u)) {
                            if (vertexToComponent.get(e.to) == compV) {
                                minWeight = Math.min(minWeight, e.weight);
                            }
                        }
                        condensation.addEdge(compU, compV, minWeight);
                    }
                }
            }
        }

        return condensation;
    }

    public static class SCCResult {
        public final List<List<Integer>> components;
        public final Graph condensationGraph;
        public final Map<Integer, Integer> vertexToComponent;

        public SCCResult(List<List<Integer>> components, Graph condensationGraph, 
                        Map<Integer, Integer> vertexToComponent) {
            this.components = components;
            this.condensationGraph = condensationGraph;
            this.vertexToComponent = vertexToComponent;
        }
    }
}

