package com.smartcity.graph.dagsp;

import com.smartcity.model.Graph;
import com.smartcity.metrics.BasicMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {
    @Test
    public void testShortestPath() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGShortestPath dagSP = new DAGShortestPath(graph, new BasicMetrics());
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPaths(0);

        assertEquals(0, result.distances[0]);
        assertEquals(2, result.distances[1]);
        assertEquals(1, result.distances[2]);
        assertEquals(2, result.distances[3]);
        assertEquals(4, result.distances[4]);
    }

    @Test
    public void testLongestPath() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGShortestPath dagSP = new DAGShortestPath(graph, new BasicMetrics());
        DAGShortestPath.LongestPathResult result = dagSP.longestPath(0);

        assertTrue(result.maxDistance > 0);
        assertFalse(result.criticalPath.isEmpty());
        assertEquals(0, result.criticalPath.get(0));
    }

    @Test
    public void testSingleNode() {
        Graph graph = new Graph(1);
        DAGShortestPath dagSP = new DAGShortestPath(graph, new BasicMetrics());
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPaths(0);

        assertEquals(0, result.distances[0]);
    }

    @Test
    public void testUnreachableNodes() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        DAGShortestPath dagSP = new DAGShortestPath(graph, new BasicMetrics());
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPaths(0);

        assertEquals(0, result.distances[0]);
        assertEquals(1, result.distances[1]);
        assertEquals(Integer.MAX_VALUE, result.distances[2]);
        assertEquals(Integer.MAX_VALUE, result.distances[3]);
    }
}

