package com.transportation.model;

import java.util.List;

public class MSTResult {
    private final List<Edge> mstEdges;
    private final int totalCost;
    private final int operationsCount;
    private final double executionTimeMs;

    public MSTResult(List<Edge> mstEdges, int totalCost,
                     int operationsCount, double executionTimeMs) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getOperationsCount() {
        return operationsCount;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MST Edges:\n");

        for (Edge edge : mstEdges) {
            sb.append("  ").append(edge).append("\n");
        }

        sb.append(String.format("Total Cost: %d\n", totalCost));
        sb.append(String.format("Operations: %d\n", operationsCount));
        sb.append(String.format("Execution Time: %.2f ms\n", executionTimeMs));

        return sb.toString();
    }
}