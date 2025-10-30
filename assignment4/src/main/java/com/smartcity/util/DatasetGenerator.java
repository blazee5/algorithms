package com.smartcity.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private static final Random random = new Random(42);

    public static void generateSmallDatasets() throws IOException {
        generateDataset("small_1.json", 8, 10, true, false);
        generateDataset("small_2.json", 6, 8, false, false);
        generateDataset("small_3.json", 10, 12, true, false);
    }

    public static void generateMediumDatasets() throws IOException {
        generateDataset("medium_1.json", 15, 25, true, true);
        generateDataset("medium_2.json", 12, 18, false, false);
        generateDataset("medium_3.json", 20, 30, true, true);
    }

    public static void generateLargeDatasets() throws IOException {
        generateDataset("large_1.json", 30, 60, true, true);
        generateDataset("large_2.json", 25, 45, false, false);
        generateDataset("large_3.json", 50, 100, true, true);
    }

    private static void generateDataset(String filename, int n, int maxEdges, 
                                        boolean allowCycles, boolean multipleSCCs) 
                                        throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("directed", true);
        json.addProperty("n", n);
        json.addProperty("source", 0);
        json.addProperty("weight_model", "edge");

        JsonArray edges = new JsonArray();
        Set<String> edgeSet = new HashSet<>();

        if (allowCycles && multipleSCCs) {
            generateMultipleSCCs(n, edges, edgeSet, maxEdges);
        } else if (allowCycles) {
            generateSingleCycle(n, edges, edgeSet, maxEdges);
        } else {
            generateDAG(n, edges, edgeSet, maxEdges);
        }

        json.add("edges", edges);

        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data/" + filename)) {
            gson.toJson(json, writer);
        }
    }

    private static void generateDAG(int n, JsonArray edges, Set<String> edgeSet, int maxEdges) {
        int edgeCount = 0;
        List<Integer> topoOrder = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            topoOrder.add(i);
        }
        Collections.shuffle(topoOrder, random);

        for (int i = 0; i < topoOrder.size() - 1 && edgeCount < maxEdges; i++) {
            int u = topoOrder.get(i);
            int v = topoOrder.get(i + 1);
            String edgeKey = u + "," + v;
            if (!edgeSet.contains(edgeKey)) {
                edgeSet.add(edgeKey);
                JsonObject edge = new JsonObject();
                edge.addProperty("u", u);
                edge.addProperty("v", v);
                edge.addProperty("w", random.nextInt(10) + 1);
                edges.add(edge);
                edgeCount++;
            }
        }

        for (int i = 0; i < topoOrder.size() && edgeCount < maxEdges; i++) {
            int u = topoOrder.get(i);
            int skip = random.nextInt(3) + 2;
            if (i + skip < topoOrder.size()) {
                int v = topoOrder.get(i + skip);
                String edgeKey = u + "," + v;
                if (!edgeSet.contains(edgeKey)) {
                    edgeSet.add(edgeKey);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("u", u);
                    edge.addProperty("v", v);
                    edge.addProperty("w", random.nextInt(10) + 1);
                    edges.add(edge);
                    edgeCount++;
                }
            }
        }
    }

    private static void generateSingleCycle(int n, JsonArray edges, Set<String> edgeSet, int maxEdges) {
        int cycleSize = Math.min(n, 4);
        for (int i = 0; i < cycleSize; i++) {
            int u = i;
            int v = (i + 1) % cycleSize;
            String edgeKey = u + "," + v;
            if (!edgeSet.contains(edgeKey)) {
                edgeSet.add(edgeKey);
                JsonObject edge = new JsonObject();
                edge.addProperty("u", u);
                edge.addProperty("v", v);
                edge.addProperty("w", random.nextInt(10) + 1);
                edges.add(edge);
            }
        }

        int remainingEdges = maxEdges - cycleSize;
        for (int i = 0; i < remainingEdges; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                String edgeKey = u + "," + v;
                if (!edgeSet.contains(edgeKey)) {
                    edgeSet.add(edgeKey);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("u", u);
                    edge.addProperty("v", v);
                    edge.addProperty("w", random.nextInt(10) + 1);
                    edges.add(edge);
                }
            }
        }
    }

    private static void generateMultipleSCCs(int n, JsonArray edges, Set<String> edgeSet, int maxEdges) {
        int numSCCs = 3;
        int nodesPerSCC = n / numSCCs;
        int edgeCount = 0;

        for (int scc = 0; scc < numSCCs; scc++) {
            int start = scc * nodesPerSCC;
            int end = (scc == numSCCs - 1) ? n : start + nodesPerSCC;
            int sccSize = end - start;

            if (sccSize >= 2) {
                for (int i = start; i < end - 1; i++) {
                    int u = i;
                    int v = i + 1;
                    String edgeKey = u + "," + v;
                    if (!edgeSet.contains(edgeKey)) {
                        edgeSet.add(edgeKey);
                        JsonObject edge = new JsonObject();
                        edge.addProperty("u", u);
                        edge.addProperty("v", v);
                        edge.addProperty("w", random.nextInt(10) + 1);
                        edges.add(edge);
                        edgeCount++;
                    }
                }
                if (sccSize >= 2) {
                    int u = end - 1;
                    int v = start;
                    String edgeKey = u + "," + v;
                    if (!edgeSet.contains(edgeKey)) {
                        edgeSet.add(edgeKey);
                        JsonObject edge = new JsonObject();
                        edge.addProperty("u", u);
                        edge.addProperty("v", v);
                        edge.addProperty("w", random.nextInt(10) + 1);
                        edges.add(edge);
                        edgeCount++;
                    }
                }
            }
        }

        for (int i = edgeCount; i < maxEdges; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                String edgeKey = u + "," + v;
                if (!edgeSet.contains(edgeKey)) {
                    edgeSet.add(edgeKey);
                    JsonObject edge = new JsonObject();
                    edge.addProperty("u", u);
                    edge.addProperty("v", v);
                    edge.addProperty("w", random.nextInt(10) + 1);
                    edges.add(edge);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new java.io.File("data").mkdirs();
        generateSmallDatasets();
        generateMediumDatasets();
        generateLargeDatasets();
        System.out.println("Generated 9 datasets in data/ directory");
    }
}

