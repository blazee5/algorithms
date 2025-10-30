package com.smartcity.graph.dagsp;

import com.smartcity.model.Graph;
import com.smartcity.metrics.Metrics;
import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;

    public DAGShortestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public ShortestPathResult shortestPaths(int source) {
        metrics.startTimer();
        int n = graph.getN();
        
        List<Integer> topoOrder = getTopologicalOrder();
        
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        for (int u : topoOrder) {
            if (dist[u] == Integer.MAX_VALUE) {
                continue;
            }
            
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                metrics.incrementRelaxations();
                int v = edge.to;
                int newDist = dist[u] + edge.weight;
                
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    parent[v] = u;
                }
            }
        }

        metrics.stopTimer();
        
        return new ShortestPathResult(dist, parent, source);
    }

    public LongestPathResult longestPath(int source) {
        metrics.startTimer();
        int n = graph.getN();
        
        List<Integer> topoOrder = getTopologicalOrder();
        
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;
        
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        for (int u : topoOrder) {
            if (dist[u] == Integer.MIN_VALUE) {
                continue;
            }
            
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                metrics.incrementRelaxations();
                int v = edge.to;
                int newDist = dist[u] + edge.weight;
                
                if (newDist > dist[v]) {
                    dist[v] = newDist;
                    parent[v] = u;
                }
            }
        }

        int maxDist = Integer.MIN_VALUE;
        int maxNode = source;
        for (int i = 0; i < n; i++) {
            if (dist[i] != Integer.MIN_VALUE && dist[i] > maxDist) {
                maxDist = dist[i];
                maxNode = i;
            }
        }

        List<Integer> criticalPath = reconstructPath(parent, source, maxNode);
        
        metrics.stopTimer();
        
        return new LongestPathResult(dist, parent, source, criticalPath, maxDist);
    }

    private List<Integer> getTopologicalOrder() {
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
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        return topoOrder;
    }

    private List<Integer> reconstructPath(int[] parent, int source, int target) {
        List<Integer> path = new ArrayList<>();
        int current = target;
        
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        
        Collections.reverse(path);
        return path;
    }

    public static class ShortestPathResult {
        public final int[] distances;
        public final int[] parent;
        public final int source;

        public ShortestPathResult(int[] distances, int[] parent, int source) {
            this.distances = distances;
            this.parent = parent;
            this.source = source;
        }

        public List<Integer> reconstructPath(int target) {
            List<Integer> path = new ArrayList<>();
            if (distances[target] == Integer.MAX_VALUE) {
                return path;
            }
            
            int current = target;
            
            while (current != -1) {
                path.add(current);
                if (current == source) {
                    break;
                }
                current = parent[current];
            }
            
            Collections.reverse(path);
            return path;
        }
    }

    public static class LongestPathResult {
        public final int[] distances;
        public final int[] parent;
        public final int source;
        public final List<Integer> criticalPath;
        public final int maxDistance;

        public LongestPathResult(int[] distances, int[] parent, int source,
                                List<Integer> criticalPath, int maxDistance) {
            this.distances = distances;
            this.parent = parent;
            this.source = source;
            this.criticalPath = criticalPath;
            this.maxDistance = maxDistance;
        }
    }
}

