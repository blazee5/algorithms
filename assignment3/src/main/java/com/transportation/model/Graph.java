package com.transportation.model;

import java.util.*;

public class Graph {
    private final Set<String> vertices;
    private final List<Edge> edges;
    private final Map<String, List<Edge>> adjacencyList;

    public Graph() {
        this.vertices = new HashSet<>();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(String vertex) {
        vertices.add(vertex);
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);

        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }

    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<Edge> getAdjacentEdges(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Graph: %d vertices, %d edges\n",
                  getVertexCount(), getEdgeCount()));
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");

        for (Edge edge : edges) {
            sb.append("  ").append(edge).append("\n");
        }

        return sb.toString();
    }
}