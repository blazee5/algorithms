package org.example.cli;

import org.example.algorithms.*;
import org.example.metrics.*;
import org.example.utils.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgorithmRunner {

    public static void main(String[] args) {
        AlgorithmRunner runner = new AlgorithmRunner();

        if (args.length == 0) {
            runner.runAllBenchmarks();
        } else {
            runner.parseArgsAndRun(args);
        }
    }

    private void parseArgsAndRun(String[] args) {
        String algorithm = args[0].toLowerCase();
        int size = args.length > 1 ? Integer.parseInt(args[1]) : 1000;
        String outputFile = args.length > 2 ? args[2] : "results.csv";

        switch (algorithm) {
            case "mergesort" -> runMergeSort(size, outputFile);
            case "quicksort" -> runQuickSort(size, outputFile);
            case "select" -> runSelect(size, outputFile);
            case "closest" -> runClosestPair(size, outputFile);
            case "all" -> runAllBenchmarks(outputFile);
            default -> {
                System.err.println("Unknown algorithm: " + algorithm);
                printUsage();
            }
        }
    }

    private void printUsage() {
        System.out.println("Usage: java AlgorithmRunner <algorithm> [size] [output.csv]");
        System.out.println("Algorithms: mergesort, quicksort, select, closest, all");
        System.out.println("Default size: 1000");
        System.out.println("Default output: results.csv");
    }

    private void runAllBenchmarks() {
        runAllBenchmarks("benchmark_results.csv");
    }

    private void runAllBenchmarks(String outputFile) {
        System.out.println("Running comprehensive algorithm benchmarks...");

        List<BenchmarkResult> results = new ArrayList<>();
        int[] sizes = {100, 500, 1000, 2000, 5000};

        for (int size : sizes) {
            System.out.printf("Testing size: %d%n", size);

            results.addAll(runMergeSortBenchmark(size));
            results.addAll(runQuickSortBenchmark(size));
            results.addAll(runSelectBenchmark(size));
            results.addAll(runClosestPairBenchmark(size));
        }

        try {
            CSVWriter.writeMetrics(outputFile, results);
            System.out.printf("Results written to %s%n", outputFile);
        } catch (IOException e) {
            System.err.printf("Error writing to file: %s%n", e.getMessage());
        }
    }

    private void runMergeSort(int size, String outputFile) {
        List<BenchmarkResult> results = runMergeSortBenchmark(size);
        writeResults(results, outputFile, "MergeSort");
    }

    private List<BenchmarkResult> runMergeSortBenchmark(int size) {
        List<BenchmarkResult> results = new ArrayList<>();

        // Test different data patterns
        results.add(benchmarkMergeSort(generateRandomArray(size), "MergeSort-Random"));
        results.add(benchmarkMergeSort(generateSortedArray(size), "MergeSort-Sorted"));
        results.add(benchmarkMergeSort(generateReverseSortedArray(size), "MergeSort-Reverse"));

        return results;
    }

    private BenchmarkResult benchmarkMergeSort(int[] array, String name) {
        MetricsCollector metrics = new MetricsCollector();
        MergeSort sorter = new MergeSort(metrics);

        metrics.startTiming();
        sorter.sort(array);
        metrics.stopTiming();

        if (!ArrayUtils.isSorted(array)) {
            throw new RuntimeException("Array not properly sorted by " + name);
        }

        return new BenchmarkResult(name, array.length, metrics);
    }

    private void runQuickSort(int size, String outputFile) {
        List<BenchmarkResult> results = runQuickSortBenchmark(size);
        writeResults(results, outputFile, "QuickSort");
    }

    private List<BenchmarkResult> runQuickSortBenchmark(int size) {
        List<BenchmarkResult> results = new ArrayList<>();

        results.add(benchmarkQuickSort(generateRandomArray(size), "QuickSort-Random"));
        results.add(benchmarkQuickSort(generateSortedArray(size), "QuickSort-Sorted"));
        results.add(benchmarkQuickSort(generateReverseSortedArray(size), "QuickSort-Reverse"));

        return results;
    }

    private BenchmarkResult benchmarkQuickSort(int[] array, String name) {
        MetricsCollector metrics = new MetricsCollector();
        QuickSort sorter = new QuickSort(metrics, 42);

        metrics.startTiming();
        sorter.sort(array);
        metrics.stopTiming();

        if (!ArrayUtils.isSorted(array)) {
            throw new RuntimeException("Array not properly sorted by " + name);
        }

        return new BenchmarkResult(name, array.length, metrics);
    }

    private void runSelect(int size, String outputFile) {
        List<BenchmarkResult> results = runSelectBenchmark(size);
        writeResults(results, outputFile, "Select");
    }

    private List<BenchmarkResult> runSelectBenchmark(int size) {
        List<BenchmarkResult> results = new ArrayList<>();

        results.add(benchmarkSelect(generateRandomArray(size), "DeterministicSelect"));

        return results;
    }

    private BenchmarkResult benchmarkSelect(int[] array, String name) {
        MetricsCollector metrics = new MetricsCollector();
        DeterministicSelect selector = new DeterministicSelect(metrics);

        int k = array.length / 2; // Find median

        metrics.startTiming();
        int result = selector.select(array.clone(), k);
        metrics.stopTiming();

        return new BenchmarkResult(name, array.length, metrics);
    }

    private void runClosestPair(int size, String outputFile) {
        List<BenchmarkResult> results = runClosestPairBenchmark(size);
        writeResults(results, outputFile, "ClosestPair");
    }

    private List<BenchmarkResult> runClosestPairBenchmark(int size) {
        List<BenchmarkResult> results = new ArrayList<>();

        results.add(benchmarkClosestPair(generateRandomPoints(size), "ClosestPair-Random"));

        return results;
    }

    private BenchmarkResult benchmarkClosestPair(Point[] points, String name) {
        MetricsCollector metrics = new MetricsCollector();
        ClosestPair finder = new ClosestPair(metrics);

        metrics.startTiming();
        ClosestPair.PointPair result = finder.findClosestPair(points);
        metrics.stopTiming();

        return new BenchmarkResult(name, points.length, metrics);
    }

    private void writeResults(List<BenchmarkResult> results, String outputFile, String algorithm) {
        try {
            CSVWriter.writeMetrics(outputFile, results);
            System.out.printf("%s results written to %s%n", algorithm, outputFile);

            for (BenchmarkResult result : results) {
                System.out.println(result);
            }
        } catch (IOException e) {
            System.err.printf("Error writing to file: %s%n", e.getMessage());
        }
    }

    private int[] generateRandomArray(int size) {
        Random random = new Random(42);
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }

    private int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    private int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    private Point[] generateRandomPoints(int size) {
        Random random = new Random(42);
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
        return points;
    }
}