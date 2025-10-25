package com.transportation.algorithm;

import com.transportation.model.Edge;
import com.transportation.model.Graph;
import com.transportation.model.MSTResult;

import java.util.*;

public class KruskalAlgorithm {
    private int operationCount;

    public MSTResult findMST(Graph graph) {
        operationCount = 0;
        long startTime = System.nanoTime();

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        sortedEdges.sort(Comparator.comparingInt(Edge::getWeight));

        int n = sortedEdges.size();
        operationCount += n;

        UnionFind uf = new UnionFind();
        for (String vertex : graph.getVertices()) {
            uf.makeSet(vertex);
        }

        for (Edge edge : sortedEdges) {
            String from = edge.getFrom();
            String to = edge.getTo();

            if (uf.union(from, to)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                operationCount += 2;

                if (mstEdges.size() == graph.getVertexCount() - 1) {
                    break;
                }
            }
        }

        operationCount += uf.getOperationCount();

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operationCount, executionTimeMs);
    }
}