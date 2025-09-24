package org.example.benchmarks;

import org.example.algorithms.*;
import org.example.metrics.MetricsCollector;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class AlgorithmBenchmark {

    @Param({"1000", "5000", "10000"})
    private int size;

    private int[] randomArray;
    private int[] sortedArray;
    private int[] reverseArray;
    private Point[] randomPoints;

    @Setup
    public void setup() {
        Random random = new Random(42);

        randomArray = new int[size];
        sortedArray = new int[size];
        reverseArray = new int[size];
        randomPoints = new Point[size];

        for (int i = 0; i < size; i++) {
            randomArray[i] = random.nextInt(size * 10);
            sortedArray[i] = i;
            reverseArray[i] = size - i;
            randomPoints[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
    }

    @Benchmark
    public void mergeSortRandom() {
        MetricsCollector metrics = new MetricsCollector();
        MergeSort sorter = new MergeSort(metrics);
        sorter.sort(randomArray.clone());
    }

    @Benchmark
    public void mergeSortSorted() {
        MetricsCollector metrics = new MetricsCollector();
        MergeSort sorter = new MergeSort(metrics);
        sorter.sort(sortedArray.clone());
    }

    @Benchmark
    public void quickSortRandom() {
        MetricsCollector metrics = new MetricsCollector();
        QuickSort sorter = new QuickSort(metrics, 42);
        sorter.sort(randomArray.clone());
    }

    @Benchmark
    public void quickSortSorted() {
        MetricsCollector metrics = new MetricsCollector();
        QuickSort sorter = new QuickSort(metrics, 42);
        sorter.sort(sortedArray.clone());
    }

    @Benchmark
    public void deterministicSelect() {
        MetricsCollector metrics = new MetricsCollector();
        DeterministicSelect selector = new DeterministicSelect(metrics);
        selector.select(randomArray.clone(), size / 2);
    }

    @Benchmark
    public void quickSelect() {
        MetricsCollector metrics = new MetricsCollector();
        DeterministicSelect selector = new DeterministicSelect(metrics);
        selector.quickSelect(randomArray.clone(), size / 2);
    }

    @Benchmark
    public void closestPairDivideConquer() {
        MetricsCollector metrics = new MetricsCollector();
        ClosestPair finder = new ClosestPair(metrics);
        finder.findClosestPair(randomPoints.clone());
    }

    @Benchmark
    public void closestPairBruteForce() {
        if (size <= 2000) { // Only run brute force for smaller sizes
            MetricsCollector metrics = new MetricsCollector();
            ClosestPair finder = new ClosestPair(metrics);
            finder.bruteForceClosestPair(randomPoints.clone());
        }
    }

    @Benchmark
    public void javaArraysSort() {
        Arrays.sort(randomArray.clone());
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
            .include(AlgorithmBenchmark.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }
}