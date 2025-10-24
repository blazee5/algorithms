package com.transportation.model;

public class Edge implements Comparable<Edge> {
    private final String from;
    private final String to;
    private final int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("%s-%s (%d)", from, to, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Edge)) return false;

        Edge other = (Edge) obj;
       
        return weight == other.weight &&
               ((from.equals(other.from) && to.equals(other.to)) ||
                (from.equals(other.to) && to.equals(other.from)));
    }

    @Override
    public int hashCode() {
        int hash1 = from.hashCode() + to.hashCode();
        return 31 * hash1 + weight;
    }
}