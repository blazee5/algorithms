package org.example.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    public static void writeMetrics(String filename, List<BenchmarkResult> results) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("algorithm,n,timeMs,comparisons,swaps,allocations,maxDepth\n");

            for (BenchmarkResult result : results) {
                writer.write(String.format("%s,%d,%.3f,%d,%d,%d,%d\n",
                    result.getAlgorithmName(),
                    result.getInputSize(),
                    result.getTimeMs(),
                    result.getComparisons(),
                    result.getSwaps(),
                    result.getAllocations(),
                    result.getMaxDepth()
                ));
            }
        }
    }

    public static void appendMetric(String filename, BenchmarkResult result) throws IOException {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(String.format("%s,%d,%.3f,%d,%d,%d,%d\n",
                result.getAlgorithmName(),
                result.getInputSize(),
                result.getTimeMs(),
                result.getComparisons(),
                result.getSwaps(),
                result.getAllocations(),
                result.getMaxDepth()
            ));
        }
    }
}