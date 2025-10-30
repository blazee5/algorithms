package com.smartcity.metrics;

public interface Metrics {
    void incrementDfsVisits();
    void incrementEdgeTraversals();
    void incrementQueuePops();
    void incrementQueuePushes();
    void incrementRelaxations();
    
    long getDfsVisits();
    long getEdgeTraversals();
    long getQueuePops();
    long getQueuePushes();
    long getRelaxations();
    
    void reset();
    long getElapsedNanos();
    void startTimer();
    void stopTimer();
}

