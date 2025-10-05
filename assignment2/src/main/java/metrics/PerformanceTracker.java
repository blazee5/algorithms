package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PerformanceTracker {
    private long comparisons;
    private long arrayAccesses;
    private long startTime;
    private long endTime;
    private final List<BenchmarkResult> results;

    public PerformanceTracker() {
        this.comparisons = 0;
        this.arrayAccesses = 0;
        this.results = new ArrayList<>();
    }

    public void reset() {
        comparisons = 0;
        arrayAccesses = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTiming() {
        startTime = System.nanoTime();
    }

    public void stopTiming() {
        endTime = System.nanoTime();
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementArrayAccesses() {
        arrayAccesses++;
    }

    public void incrementArrayAccesses(int count) {
        arrayAccesses += count;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getArrayAccesses() {
        return arrayAccesses;
    }

    public long getExecutionTimeNanos() {
        return endTime - startTime;
    }

    public double getExecutionTimeMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void addResult(int inputSize, long executionTime, long comparisons, long arrayAccesses) {
        results.add(new BenchmarkResult(inputSize, executionTime, comparisons, arrayAccesses));
    }

    public void addCurrentResult(int inputSize) {
        results.add(new BenchmarkResult(inputSize, getExecutionTimeNanos(), comparisons, arrayAccesses));
    }

    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("InputSize,ExecutionTimeNanos,ExecutionTimeMillis,Comparisons,ArrayAccesses");
            for (BenchmarkResult result : results) {
                writer.printf("%d,%d,%.6f,%d,%d%n",
                        result.inputSize,
                        result.executionTimeNanos,
                        result.executionTimeNanos / 1_000_000.0,
                        result.comparisons,
                        result.arrayAccesses);
            }
        }
    }

    public void printSummary() {
        System.out.println("\n=== Performance Metrics ===");
        System.out.printf("Execution Time: %.6f ms%n", getExecutionTimeMillis());
        System.out.printf("Comparisons: %,d%n", comparisons);
        System.out.printf("Array Accesses: %,d%n", arrayAccesses);
        System.out.println("===========================\n");
    }

    public void printResults() {
        System.out.println("\n=== Benchmark Results ===");
        System.out.printf("%-12s %-20s %-20s %-15s %-15s%n",
                "Input Size", "Time (ns)", "Time (ms)", "Comparisons", "Array Accesses");
        System.out.println("-".repeat(90));
        for (BenchmarkResult result : results) {
            System.out.printf("%-12d %-20d %-20.6f %-15d %-15d%n",
                    result.inputSize,
                    result.executionTimeNanos,
                    result.executionTimeNanos / 1_000_000.0,
                    result.comparisons,
                    result.arrayAccesses);
        }
        System.out.println("=========================\n");
    }

    private static class BenchmarkResult {
        final int inputSize;
        final long executionTimeNanos;
        final long comparisons;
        final long arrayAccesses;

        BenchmarkResult(int inputSize, long executionTimeNanos, long comparisons, long arrayAccesses) {
            this.inputSize = inputSize;
            this.executionTimeNanos = executionTimeNanos;
            this.comparisons = comparisons;
            this.arrayAccesses = arrayAccesses;
        }
    }
}