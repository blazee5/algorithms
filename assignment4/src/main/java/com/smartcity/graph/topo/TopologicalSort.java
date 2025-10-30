package com.smartcity.graph.topo;

import com.smartcity.model.Graph;
import com.smartcity.metrics.Metrics;
import java.util.*;

public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public TopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public TopoResult topologicalSort() {
        metrics.startTimer();
        int n = graph.getN();
        int[] inDegree = new int[n];
        
        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueuePushes();
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueuePops();
            topoOrder.add(u);

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                metrics.incrementEdgeTraversals();
                int v = edge.to;
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementQueuePushes();
                }
            }
        }

        metrics.stopTimer();
        
        return new TopoResult(topoOrder);
    }

    public static class TopoResult {
        public final List<Integer> order;

        public TopoResult(List<Integer> order) {
            this.order = order;
        }
    }
}

