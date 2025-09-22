package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {
    private MetricsCollector metrics;
    private ClosestPair closestPair;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
        closestPair = new ClosestPair(metrics);
    }

    @Test
    void testTwoPoints() {
        Point[] points = {
            new Point(0, 0),
            new Point(3, 4)
        };

        ClosestPair.PointPair result = closestPair.findClosestPair(points);
        assertEquals(5.0, result.distance, 1e-9);
        assertTrue((result.p1.equals(points[0]) && result.p2.equals(points[1])) ||
                   (result.p1.equals(points[1]) && result.p2.equals(points[0])));
    }

    @Test
    void testThreePoints() {
        Point[] points = {
            new Point(0, 0),
            new Point(1, 0),
            new Point(5, 5)
        };

        ClosestPair.PointPair result = closestPair.findClosestPair(points);
        assertEquals(1.0, result.distance, 1e-9);
    }

    @Test
    void testSquarePoints() {
        Point[] points = {
            new Point(0, 0),
            new Point(1, 0),
            new Point(0, 1),
            new Point(1, 1)
        };

        ClosestPair.PointPair result = closestPair.findClosestPair(points);
        assertEquals(1.0, result.distance, 1e-9);
    }

    @Test
    void testRandomPoints() {
        Random random = new Random(42);
        Point[] points = new Point[100];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(random.nextDouble() * 100, random.nextDouble() * 100);
        }

        metrics.reset();
        metrics.startTiming();
        ClosestPair.PointPair divideConquerResult = closestPair.findClosestPair(points);
        metrics.stopTiming();

        MetricsCollector bruteForceMetrics = new MetricsCollector();
        ClosestPair bruteForceFinder = new ClosestPair(bruteForceMetrics);

        bruteForceMetrics.startTiming();
        ClosestPair.PointPair bruteForceResult = bruteForceFinder.bruteForceClosestPair(points);
        bruteForceMetrics.stopTiming();

        assertEquals(bruteForceResult.distance, divideConquerResult.distance, 1e-9,
            "Divide and conquer should give same result as brute force");
    }

    @Test
    void testDuplicatePoints() {
        Point[] points = {
            new Point(0, 0),
            new Point(0, 0),
            new Point(5, 5)
        };

        ClosestPair.PointPair result = closestPair.findClosestPair(points);
        assertEquals(0.0, result.distance, 1e-9);
    }

    @Test
    void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> closestPair.findClosestPair(null));
        assertThrows(IllegalArgumentException.class, () -> closestPair.findClosestPair(new Point[0]));
        assertThrows(IllegalArgumentException.class, () -> closestPair.findClosestPair(new Point[]{new Point(0, 0)}));
    }
}