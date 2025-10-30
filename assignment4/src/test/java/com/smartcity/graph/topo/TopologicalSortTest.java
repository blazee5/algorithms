package com.smartcity.graph.topo;

import com.smartcity.model.Graph;
import com.smartcity.metrics.BasicMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {
    @Test
    public void testSimpleTopoSort() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        TopologicalSort topo = new TopologicalSort(graph, new BasicMetrics());
        TopologicalSort.TopoResult result = topo.topologicalSort();

        assertEquals(4, result.order.size());
        assertTrue(result.order.indexOf(0) < result.order.indexOf(1));
        assertTrue(result.order.indexOf(1) < result.order.indexOf(2));
    }

    @Test
    public void testComplexTopoSort() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);

        TopologicalSort topo = new TopologicalSort(graph, new BasicMetrics());
        TopologicalSort.TopoResult result = topo.topologicalSort();

        assertEquals(6, result.order.size());
        assertTrue(result.order.indexOf(0) < result.order.indexOf(3));
        assertTrue(result.order.indexOf(1) < result.order.indexOf(3));
        assertTrue(result.order.indexOf(2) < result.order.indexOf(3));
    }

    @Test
    public void testSingleNode() {
        Graph graph = new Graph(1);
        TopologicalSort topo = new TopologicalSort(graph, new BasicMetrics());
        TopologicalSort.TopoResult result = topo.topologicalSort();

        assertEquals(1, result.order.size());
        assertEquals(0, result.order.get(0));
    }
}

