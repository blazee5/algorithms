package com.smartcity.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.smartcity.model.Graph;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {
    public static GraphData parseGraph(String filePath) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            
            int n = json.get("n").getAsInt();
            boolean directed = json.get("directed").getAsBoolean();
            String weightModel = json.get("weight_model").getAsString();
            int source = json.has("source") ? json.get("source").getAsInt() : 0;
            
            Graph graph = new Graph(n);
            JsonArray edges = json.getAsJsonArray("edges");
            
            for (int i = 0; i < edges.size(); i++) {
                JsonObject edge = edges.get(i).getAsJsonObject();
                int u = edge.get("u").getAsInt();
                int v = edge.get("v").getAsInt();
                int w = edge.get("w").getAsInt();
                graph.addEdge(u, v, w);
            }
            
            return new GraphData(graph, source, weightModel);
        }
    }

    public static class GraphData {
        public final Graph graph;
        public final int source;
        public final String weightModel;

        public GraphData(Graph graph, int source, String weightModel) {
            this.graph = graph;
            this.source = source;
            this.weightModel = weightModel;
        }
    }
}

