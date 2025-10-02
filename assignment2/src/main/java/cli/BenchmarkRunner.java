package cli;

import algorithms.KadaneAlgorithm;
import metrics.PerformanceTracker;

import java.io.IOException;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] DEFAULT_SIZES = {100, 1000, 10000, 100000};
    private static final int WARMUP_ITERATIONS = 5;
    private static final int BENCHMARK_ITERATIONS = 10;

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();

        if (args.length > 0) {
            if (args[0].equals("--help") || args[0].equals("-h")) {
                printUsage();
                return;
            }

            if (args[0].equals("--custom")) {
                runner.runCustomBenchmark(args);
            } else {
                runner.runSingleTest(args);
            }
        } else {
            runner.runFullBenchmark();
        }
    }

    private static void printUsage() {
        System.out.println("Kadane's Algorithm Benchmark Runner");
        System.out.println("\nUsage:");
        System.out.println("  java -jar benchmark.jar                    - Run full benchmark suite");
        System.out.println("  java -jar benchmark.jar <size>             - Run single test with specified size");
        System.out.println("  java -jar benchmark.jar --custom <sizes>   - Run custom benchmark with multiple sizes");
        System.out.println("  java -jar benchmark.jar --help             - Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java -jar benchmark.jar 5000");
        System.out.println("  java -jar benchmark.jar --custom 100 500 1000 5000");
    }

    public void runFullBenchmark() {
        System.out.println("=== Kadane's Algorithm Benchmark Suite ===\n");

        PerformanceTracker globalTracker = new PerformanceTracker();

        for (int size : DEFAULT_SIZES) {
            System.out.printf("Running benchmark for size: %,d%n", size);
            runBenchmarkForSize(size, globalTracker);
            System.out.println();
        }

        try {
            String filename = "benchmark_results.csv";
            globalTracker.exportToCSV(filename);
            System.out.printf("Results exported to %s%n", filename);
        } catch (IOException e) {
            System.err.println("Error exporting results: " + e.getMessage());
        }

        globalTracker.printResults();
    }

    public void runSingleTest(String[] args) {
        try {
            int size = Integer.parseInt(args[0]);
            if (size <= 0) {
                System.err.println("Error: Size must be positive");
                return;
            }

            System.out.printf("Running single test for size: %,d%n%n", size);
            PerformanceTracker tracker = new PerformanceTracker();
            runBenchmarkForSize(size, tracker);
            tracker.printResults();
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid size. Use --help for usage information.");
        }
    }

    public void runCustomBenchmark(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: --custom requires at least one size parameter");
            printUsage();
            return;
        }

        System.out.println("=== Custom Benchmark ===\n");
        PerformanceTracker tracker = new PerformanceTracker();

        for (int i = 1; i < args.length; i++) {
            try {
                int size = Integer.parseInt(args[i]);
                if (size <= 0) {
                    System.err.printf("Skipping invalid size: %s%n", args[i]);
                    continue;
                }

                System.out.printf("Running benchmark for size: %,d%n", size);
                runBenchmarkForSize(size, tracker);
                System.out.println();
            } catch (NumberFormatException e) {
                System.err.printf("Skipping invalid size: %s%n", args[i]);
            }
        }

        try {
            String filename = "custom_benchmark_results.csv";
            tracker.exportToCSV(filename);
            System.out.printf("Results exported to %s%n", filename);
        } catch (IOException e) {
            System.err.println("Error exporting results: " + e.getMessage());
        }

        tracker.printResults();
    }

    private void runBenchmarkForSize(int size, PerformanceTracker globalTracker) {
        int[][] testArrays = generateTestArrays(size);

        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            int[] warmupArray = testArrays[i % testArrays.length];
            KadaneAlgorithm algorithm = new KadaneAlgorithm();
            algorithm.findMaxSubarray(warmupArray);
        }

        long totalTime = 0;
        long totalComparisons = 0;
        long totalArrayAccesses = 0;

        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            int[] testArray = testArrays[i % testArrays.length];
            PerformanceTracker tracker = new PerformanceTracker();
            KadaneAlgorithm algorithm = new KadaneAlgorithm(tracker);

            tracker.startTiming();
            algorithm.findMaxSubarray(testArray);
            tracker.stopTiming();

            totalTime += tracker.getExecutionTimeNanos();
            totalComparisons += tracker.getComparisons();
            totalArrayAccesses += tracker.getArrayAccesses();
        }

        long avgTime = totalTime / BENCHMARK_ITERATIONS;
        long avgComparisons = totalComparisons / BENCHMARK_ITERATIONS;
        long avgArrayAccesses = totalArrayAccesses / BENCHMARK_ITERATIONS;

        globalTracker.addResult(size, avgTime, avgComparisons, avgArrayAccesses);

        System.out.printf("  Average Time: %.6f ms%n", avgTime / 1_000_000.0);
        System.out.printf("  Average Comparisons: %,d%n", avgComparisons);
        System.out.printf("  Average Array Accesses: %,d%n", avgArrayAccesses);
    }

    private int[][] generateTestArrays(int size) {
        int[][] arrays = new int[BENCHMARK_ITERATIONS][];
        Random random = new Random(42);

        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            arrays[i] = new int[size];
            for (int j = 0; j < size; j++) {
                arrays[i][j] = random.nextInt(2000) - 1000;
            }
        }

        return arrays;
    }

    public void runInteractiveDemo() {
        System.out.println("=== Kadane's Algorithm Demo ===\n");

        int[][] demoArrays = {
                {-2, 1, -3, 4, -1, 2, 1, -5, 4},
                {-2, -3, -1, -5, -4},
                {1, 2, 3, 4, 5},
                {-1, 0, -2, 0, -3},
                {5, -3, 5}
        };

        for (int[] array : demoArrays) {
            System.out.print("Array: [");
            for (int i = 0; i < array.length; i++) {
                System.out.print(array[i]);
                if (i < array.length - 1) System.out.print(", ");
            }
            System.out.println("]");

            PerformanceTracker tracker = new PerformanceTracker();
            KadaneAlgorithm algorithm = new KadaneAlgorithm(tracker);

            tracker.startTiming();
            KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
            tracker.stopTiming();

            System.out.println("Result: " + result);
            tracker.printSummary();
        }
    }
}