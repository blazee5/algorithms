package com.transportation;

import com.transportation.algorithm.KruskalAlgorithm;
import com.transportation.algorithm.PrimAlgorithm;
import com.transportation.model.Graph;
import com.transportation.model.MSTResult;
import com.transportation.util.JsonParser;
import com.transportation.util.JsonParser.GraphData;
import com.transportation.util.JsonParser.ResultData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransportationOptimizer {

    private static final String INPUT_FILE = "ass_3_input.json";
    private static final String OUTPUT_FILE = "ass_3_output.json";

    public static void main(String[] args) {
        try {
            System.out.println("=".repeat(70));
            System.out.println("City Transportation Network Optimization");
            System.out.println("Minimum Spanning Tree (MST) using Prim's and Kruskal's Algorithms");
            System.out.println("=".repeat(70));
            System.out.println();

            List<GraphData> graphDataList = JsonParser.readInput(INPUT_FILE);
            System.out.println("Successfully loaded " + graphDataList.size() + " graphs from " + INPUT_FILE);
            System.out.println();

            List<ResultData> results = new ArrayList<>();
            PrimAlgorithm prim = new PrimAlgorithm();
            KruskalAlgorithm kruskal = new KruskalAlgorithm();

            for (GraphData graphData : graphDataList) {
                System.out.println("-".repeat(70));
                System.out.println("Processing Graph " + graphData.id);
                System.out.println("-".repeat(70));

                Graph graph = graphData.toGraph();
                System.out.println("Graph Statistics:");
                System.out.println("  Vertices: " + graph.getVertexCount());
                System.out.println("  Edges: " + graph.getEdgeCount());
                System.out.println();

                System.out.println("Running Prim's Algorithm...");
                MSTResult primResult = prim.findMST(graph);
                System.out.println("  MST Total Cost: " + primResult.getTotalCost());
                System.out.println("  Operations Count: " + primResult.getOperationsCount());
                System.out.println("  Execution Time: " + String.format("%.2f ms", primResult.getExecutionTimeMs()));
                System.out.println("  MST Edges: " + primResult.getMstEdges().size());
                System.out.println();

                System.out.println("Running Kruskal's Algorithm...");
                MSTResult kruskalResult = kruskal.findMST(graph);
                System.out.println("  MST Total Cost: " + kruskalResult.getTotalCost());
                System.out.println("  Operations Count: " + kruskalResult.getOperationsCount());
                System.out.println("  Execution Time: " + String.format("%.2f ms", kruskalResult.getExecutionTimeMs()));
                System.out.println("  MST Edges: " + kruskalResult.getMstEdges().size());
                System.out.println();

                if (primResult.getTotalCost() == kruskalResult.getTotalCost()) {
                    System.out.println("✓ Verification: Both algorithms produced the same MST cost!");
                } else {
                    System.out.println("✗ Warning: MST costs differ between algorithms!");
                }
                System.out.println();

                ResultData result = new ResultData(
                    graphData.id,
                    graph.getVertexCount(),
                    graph.getEdgeCount(),
                    primResult,
                    kruskalResult
                );
                results.add(result);
            }

            JsonParser.writeOutput(OUTPUT_FILE, results);
            System.out.println("=".repeat(70));
            System.out.println("Results written to " + OUTPUT_FILE);
            System.out.println("=".repeat(70));
            System.out.println();

            printSummary(results);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printSummary(List<ResultData> results) {
        System.out.println("ALGORITHM COMPARISON SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println();

        for (ResultData result : results) {
            System.out.println("Graph " + result.graphId + ":");
            System.out.println("  Prim's   - Operations: " + result.primResult.getOperationsCount() +
                             ", Time: " + String.format("%.2f ms", result.primResult.getExecutionTimeMs()));
            System.out.println("  Kruskal's - Operations: " + result.kruskalResult.getOperationsCount() +
                             ", Time: " + String.format("%.2f ms", result.kruskalResult.getExecutionTimeMs()));

            if (result.primResult.getExecutionTimeMs() < result.kruskalResult.getExecutionTimeMs()) {
                System.out.println("  → Prim's was faster by " +
                    String.format("%.2f ms", result.kruskalResult.getExecutionTimeMs() - result.primResult.getExecutionTimeMs()));
            } else {
                System.out.println("  → Kruskal's was faster by " +
                    String.format("%.2f ms", result.primResult.getExecutionTimeMs() - result.kruskalResult.getExecutionTimeMs()));
            }

            System.out.println();
        }

        System.out.println("Key Observations:");
        System.out.println("  • Both algorithms produce the same MST total cost (guaranteed)");
        System.out.println("  • The specific edges in the MST may differ between algorithms");
        System.out.println("  • Operation counts reflect algorithm-specific steps (comparisons, unions, etc.)");
        System.out.println("  • Execution time depends on implementation and graph characteristics");
        System.out.println();
    }
}