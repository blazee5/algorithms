package com.transportation.algorithm;

import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private final Map<String, String> parent;
    private final Map<String, Integer> rank;
    private int operationCount;

    public UnionFind() {
        this.parent = new HashMap<>();
        this.rank = new HashMap<>();
        this.operationCount = 0;
    }

    public void makeSet(String x) {
        parent.put(x, x);
        rank.put(x, 0);
        operationCount++;
    }

    public String find(String x) {
        operationCount++;
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    public boolean union(String x, String y) {
        String rootX = find(x);
        String rootY = find(y);

        operationCount++;

        if (rootX.equals(rootY)) {
            return false;
        }

        int rankX = rank.get(rootX);
        int rankY = rank.get(rootY);

        if (rankX < rankY) {
            parent.put(rootX, rootY);
        } else if (rankX > rankY) {
            parent.put(rootY, rootX);
        } else {
            parent.put(rootY, rootX);
            rank.put(rootX, rankX + 1);
        }

        operationCount++;
        return true;
    }

    public int getOperationCount() {
        return operationCount;
    }
}