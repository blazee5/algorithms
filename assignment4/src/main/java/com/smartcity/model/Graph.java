package com.smartcity.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final List<List<Edge>> adjList;
    private final List<List<Edge>> reverseAdjList;

    public Graph(int n) {
        this.n = n;
        this.adjList = new ArrayList<>();
        this.reverseAdjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
            reverseAdjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(u, v, weight));
        reverseAdjList.get(v).add(new Edge(v, u, weight));
    }

    public int getN() {
        return n;
    }

    public List<Edge> getNeighbors(int u) {
        return adjList.get(u);
    }

    public List<Edge> getReverseNeighbors(int u) {
        return reverseAdjList.get(u);
    }

    public List<List<Edge>> getAdjList() {
        return adjList;
    }

    public static class Edge {
        public final int from;
        public final int to;
        public final int weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}

