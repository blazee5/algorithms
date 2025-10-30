package com.smartcity.metrics;

public class BasicMetrics implements Metrics {
    private long dfsVisits = 0;
    private long edgeTraversals = 0;
    private long queuePops = 0;
    private long queuePushes = 0;
    private long relaxations = 0;
    private long startTime = 0;
    private long elapsedNanos = 0;

    @Override
    public void incrementDfsVisits() {
        dfsVisits++;
    }

    @Override
    public void incrementEdgeTraversals() {
        edgeTraversals++;
    }

    @Override
    public void incrementQueuePops() {
        queuePops++;
    }

    @Override
    public void incrementQueuePushes() {
        queuePushes++;
    }

    @Override
    public void incrementRelaxations() {
        relaxations++;
    }

    @Override
    public long getDfsVisits() {
        return dfsVisits;
    }

    @Override
    public long getEdgeTraversals() {
        return edgeTraversals;
    }

    @Override
    public long getQueuePops() {
        return queuePops;
    }

    @Override
    public long getQueuePushes() {
        return queuePushes;
    }

    @Override
    public long getRelaxations() {
        return relaxations;
    }

    @Override
    public void reset() {
        dfsVisits = 0;
        edgeTraversals = 0;
        queuePops = 0;
        queuePushes = 0;
        relaxations = 0;
        elapsedNanos = 0;
    }

    @Override
    public long getElapsedNanos() {
        return elapsedNanos;
    }

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        elapsedNanos = System.nanoTime() - startTime;
    }
}

