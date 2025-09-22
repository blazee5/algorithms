package org.example.algorithms;

import org.example.metrics.MetricsCollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPair {
    private final MetricsCollector metrics;

    public ClosestPair(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public static class PointPair {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public PointPair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = p1.distanceTo(p2);
        }

        @Override
        public String toString() {
            return String.format("Pair{%s, %s, distance=%.3f}", p1, p2, distance);
        }
    }

    public PointPair findClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        Point[] sortedByX = points.clone();
        Point[] sortedByY = points.clone();

        Arrays.sort(sortedByX, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(sortedByY, Comparator.comparingDouble(p -> p.y));

        metrics.incrementAllocations(); // For sorted arrays

        return closestPairRec(sortedByX, sortedByY, 0, points.length - 1);
    }

    private PointPair closestPairRec(Point[] sortedByX, Point[] sortedByY, int left, int right) {
        metrics.enterRecursion();

        try {
            int n = right - left + 1;

            if (n <= 3) {
                return bruteForce(sortedByX, left, right);
            }

            int mid = left + (right - left) / 2;
            Point midPoint = sortedByX[mid];

            List<Point> leftY = new ArrayList<>();
            List<Point> rightY = new ArrayList<>();

            for (Point point : sortedByY) {
                metrics.incrementComparisons();
                if (point.x <= midPoint.x) {
                    leftY.add(point);
                } else {
                    rightY.add(point);
                }
            }

            metrics.incrementAllocations(); // For leftY and rightY arrays

            PointPair leftPair = closestPairRec(sortedByX, leftY.toArray(new Point[0]), left, mid);
            PointPair rightPair = closestPairRec(sortedByX, rightY.toArray(new Point[0]), mid + 1, right);

            PointPair minPair = leftPair.distance <= rightPair.distance ? leftPair : rightPair;
            metrics.incrementComparisons();

            return checkStrip(sortedByY, midPoint, minPair);

        } finally {
            metrics.exitRecursion();
        }
    }

    private PointPair bruteForce(Point[] points, int left, int right) {
        PointPair minPair = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                metrics.incrementComparisons();
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    minPair = new PointPair(points[i], points[j]);
                }
            }
        }

        return minPair;
    }

    private PointPair checkStrip(Point[] sortedByY, Point midPoint, PointPair currentMin) {
        List<Point> strip = new ArrayList<>();

        for (Point point : sortedByY) {
            metrics.incrementComparisons();
            if (Math.abs(point.x - midPoint.x) < currentMin.distance) {
                strip.add(point);
            }
        }

        metrics.incrementAllocations(); // For strip array

        PointPair minPair = currentMin;

        for (int i = 0; i < strip.size(); i++) {
            Point p1 = strip.get(i);

            for (int j = i + 1; j < strip.size() && j < i + 8; j++) {
                Point p2 = strip.get(j);
                metrics.incrementComparisons();

                if (p2.y - p1.y >= currentMin.distance) {
                    break;
                }

                double distance = p1.distanceTo(p2);
                metrics.incrementComparisons();
                if (distance < minPair.distance) {
                    minPair = new PointPair(p1, p2);
                }
            }
        }

        return minPair;
    }

    public PointPair bruteForceClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        PointPair minPair = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                metrics.incrementComparisons();
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    minPair = new PointPair(points[i], points[j]);
                }
            }
        }

        return minPair;
    }
}