package org.example.metrics;

public class BenchmarkResult {
    private final String algorithmName;
    private final int inputSize;
    private final double timeMs;
    private final long comparisons;
    private final long swaps;
    private final long allocations;
    private final int maxDepth;

    public BenchmarkResult(String algorithmName, int inputSize, MetricsCollector metrics) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeMs = metrics.getElapsedTimeMs();
        this.comparisons = metrics.getComparisons();
        this.swaps = metrics.getSwaps();
        this.allocations = metrics.getAllocations();
        this.maxDepth = metrics.getMaxDepth();
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public double getTimeMs() {
        return timeMs;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getAllocations() {
        return allocations;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public String toString() {
        return String.format("%s(n=%d): %.3fms, %d comparisons, %d swaps, depth=%d",
            algorithmName, inputSize, timeMs, comparisons, swaps, maxDepth);
    }
}