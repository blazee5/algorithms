package com.transportation.algorithm;

import com.transportation.model.Edge;
import com.transportation.model.Graph;
import com.transportation.model.MSTResult;

import java.util.*;

public class PrimAlgorithm {
    private int operationCount;

    public MSTResult findMST(Graph graph) {
        operationCount = 0;
        long startTime = System.nanoTime();

        Set<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0, 0, 0.0);
        }

        List<Edge> mstEdges = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<EdgeWithVertex> minHeap = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.edge.getWeight())
        );

        String startVertex = vertices.iterator().next();
        visited.add(startVertex);
        operationCount++;

        for (Edge edge : graph.getAdjacentEdges(startVertex)) {
            minHeap.offer(new EdgeWithVertex(edge, edge.getTo()));
            operationCount++;
        }

        int totalCost = 0;

        while (!minHeap.isEmpty() && visited.size() < vertices.size()) {
            EdgeWithVertex current = minHeap.poll();
            operationCount++;

            String toVertex = current.toVertex;

            operationCount++;
            if (visited.contains(toVertex)) {
                continue;
            }

            mstEdges.add(current.edge);
            visited.add(toVertex);
            totalCost += current.edge.getWeight();
            operationCount += 3;

            for (Edge edge : graph.getAdjacentEdges(toVertex)) {
                operationCount++;
                if (!visited.contains(edge.getTo())) {
                    minHeap.offer(new EdgeWithVertex(edge, edge.getTo()));
                    operationCount++;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operationCount, executionTimeMs);
    }

    private static class EdgeWithVertex {
        Edge edge;
        String toVertex;

        EdgeWithVertex(Edge edge, String toVertex) {
            this.edge = edge;
            this.toVertex = toVertex;
        }
    }
}