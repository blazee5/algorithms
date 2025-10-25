package com.transportation.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.transportation.model.Edge;
import com.transportation.model.Graph;
import com.transportation.model.MSTResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class JsonParser {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static List<GraphData> readInput(String filename) throws IOException {
        String content = Files.readString(Paths.get(filename));
        InputData input = gson.fromJson(content, InputData.class);
        return input.graphs;
    }

    public static void writeOutput(String filename, List<ResultData> results) throws IOException {
        OutputData output = new OutputData(results);
        String json = gson.toJson(output);
        Files.writeString(Paths.get(filename), json);
    }

    private static class InputData {
        List<GraphData> graphs;
    }

    private static class OutputData {
        List<ResultData> results;

        OutputData(List<ResultData> results) {
            this.results = results;
        }
    }

    public static class GraphData {
        public int id;
        public List<String> nodes;
        public List<EdgeData> edges;

        public Graph toGraph() {
            Graph graph = new Graph();
            for (String node : nodes) {
                graph.addVertex(node);
            }
            for (EdgeData edge : edges) {
                graph.addEdge(edge.from, edge.to, edge.weight);
            }
            return graph;
        }
    }

    public static class EdgeData {
        public String from;
        public String to;
        public int weight;

        public EdgeData() {
        }

        public EdgeData(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static class ResultData {
        @SerializedName("graph_id")
        public final int graphId;

        @SerializedName("input_stats")
        public final InputStats inputStats;

        public final AlgorithmResult prim;
        public final AlgorithmResult kruskal;

        public final transient int vertices;
        public final transient int edges;
        public final transient MSTResult primResult;
        public final transient MSTResult kruskalResult;

        public ResultData(int graphId, int vertices, int edges,
                         MSTResult primResult, MSTResult kruskalResult) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
            this.inputStats = new InputStats(vertices, edges);
            this.prim = new AlgorithmResult(primResult);
            this.kruskal = new AlgorithmResult(kruskalResult);
        }
    }

    private static class InputStats {
        int vertices;
        int edges;

        InputStats(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
    }

    private static class AlgorithmResult {
        @SerializedName("mst_edges")
        List<EdgeData> mstEdges;

        @SerializedName("total_cost")
        int totalCost;

        @SerializedName("operations_count")
        int operationsCount;

        @SerializedName("execution_time_ms")
        double executionTimeMs;

        AlgorithmResult(MSTResult result) {
            this.mstEdges = result.getMstEdges().stream()
                    .map(e -> new EdgeData(e.getFrom(), e.getTo(), e.getWeight()))
                    .collect(Collectors.toList());
            this.totalCost = result.getTotalCost();
            this.operationsCount = result.getOperationsCount();
            this.executionTimeMs = result.getExecutionTimeMs();
        }
    }
}
