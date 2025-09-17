package org.example.metrics;

public class MetricsCollector {
    private long comparisons = 0;
    private long swaps = 0;
    private long allocations = 0;
    private int maxDepth = 0;
    private int currentDepth = 0;
    private long startTime = 0;
    private long endTime = 0;

    public void reset() {
        comparisons = 0;
        swaps = 0;
        allocations = 0;
        maxDepth = 0;
        currentDepth = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTiming() {
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
    }

    public long getElapsedTimeNanos() {
        return endTime - startTime;
    }

    public double getElapsedTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementSwaps() {
        swaps++;
    }

    public void incrementAllocations() {
        allocations++;
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
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
        return String.format(
            "MetricsCollector{comparisons=%d, swaps=%d, allocations=%d, maxDepth=%d, timeMs=%.3f}",
            comparisons, swaps, allocations, maxDepth, getElapsedTimeMs()
        );
    }
}