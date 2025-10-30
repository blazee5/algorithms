package com.smartcity.graph.scc;

import com.smartcity.model.Graph;
import com.smartcity.metrics.BasicMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    public void testSimpleCycle() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(graph, new BasicMetrics());
        TarjanSCC.SCCResult result = tarjan.findSCCs();

        assertEquals(2, result.components.size());
        assertTrue(result.components.get(0).contains(0) || result.components.get(1).contains(0));
    }

    @Test
    public void testPureDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(graph, new BasicMetrics());
        TarjanSCC.SCCResult result = tarjan.findSCCs();

        assertEquals(4, result.components.size());
        assertEquals(4, result.condensationGraph.getN());
    }

    @Test
    public void testMultipleSCCs() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(4, 5, 1);

        TarjanSCC tarjan = new TarjanSCC(graph, new BasicMetrics());
        TarjanSCC.SCCResult result = tarjan.findSCCs();

        assertTrue(result.components.size() >= 3);
    }

    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(5);
        TarjanSCC tarjan = new TarjanSCC(graph, new BasicMetrics());
        TarjanSCC.SCCResult result = tarjan.findSCCs();

        assertEquals(5, result.components.size());
    }
}

