package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectTest {
    private MetricsCollector metrics;
    private DeterministicSelect select;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
        select = new DeterministicSelect(metrics);
    }

    @Test
    void testSelectSingleElement() {
        int[] array = {42};
        assertEquals(42, select.select(array, 0));
    }

    @Test
    void testSelectSmallArray() {
        int[] array = {3, 1, 4, 1, 5};
        assertEquals(1, select.select(array.clone(), 0)); // minimum
        assertEquals(5, select.select(array.clone(), 4)); // maximum
        assertEquals(3, select.select(array.clone(), 2)); // median
    }

    @Test
    void testSelectLargeArray() {
        Random random = new Random(42);
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }

        for (int k = 0; k < array.length; k += 10) {
            int[] originalArray = array.clone();
            int[] sortedArray = array.clone();
            Arrays.sort(sortedArray);

            metrics.reset();
            metrics.startTiming();
            int result = select.select(originalArray, k);
            metrics.stopTiming();

            assertEquals(sortedArray[k], result,
                String.format("Select(%d) should return %d-th smallest element", k, k));
        }
    }

    @Test
    void testSelectDuplicates() {
        int[] array = {5, 5, 5, 5, 5, 1, 1, 1, 9, 9};
        int[] sorted = array.clone();
        Arrays.sort(sorted);

        for (int k = 0; k < array.length; k++) {
            assertEquals(sorted[k], select.select(array.clone(), k));
        }
    }

    @Test
    void testSelectBoundaryConditions() {
        int[] array = {9, 3, 7, 1, 5};

        assertEquals(1, select.select(array.clone(), 0)); // minimum
        assertEquals(9, select.select(array.clone(), 4)); // maximum
    }

    @Test
    void testSelectInvalidInputs() {
        int[] array = {1, 2, 3};

        assertThrows(IllegalArgumentException.class, () -> select.select(null, 0));
        assertThrows(IllegalArgumentException.class, () -> select.select(new int[0], 0));
        assertThrows(IllegalArgumentException.class, () -> select.select(array, -1));
        assertThrows(IllegalArgumentException.class, () -> select.select(array, 3));
    }

    @Test
    void testQuickSelectComparison() {
        Random random = new Random(42);
        int[] array = new int[50];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100);
        }

        for (int k = 0; k < array.length; k += 5) {
            int deterministicResult = select.select(array.clone(), k);

            metrics.reset();
            int quickSelectResult = select.quickSelect(array.clone(), k);

            assertEquals(deterministicResult, quickSelectResult,
                "Deterministic select and quickselect should return same result");
        }
    }

    @Test
    void testLinearTimeComplexity() {
        Random random = new Random(42);
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000);
        }

        metrics.reset();
        metrics.startTiming();
        int median = select.select(array.clone(), array.length / 2);
        metrics.stopTiming();

        int[] sorted = array.clone();
        Arrays.sort(sorted);
        assertEquals(sorted[array.length / 2], median);

        double timeMs = metrics.getElapsedTimeMs();
        assertTrue(timeMs < 100, "Select should complete in reasonable time for O(n) complexity");
    }

    @Test
    void testRecursionDepthBounds() {
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        metrics.reset();
        select.select(array, array.length / 2);

        int expectedMaxDepth = (int) (Math.log(array.length) * 10);
        assertTrue(metrics.getMaxDepth() <= expectedMaxDepth,
            String.format("Recursion depth %d should be reasonable for deterministic select",
                metrics.getMaxDepth()));
    }

    @Test
    void testWorstCaseResilience() {
        int[] worstCase = new int[100];
        for (int i = 0; i < worstCase.length; i++) {
            worstCase[i] = i;
        }

        for (int k = 0; k < worstCase.length; k += 10) {
            metrics.reset();
            metrics.startTiming();
            int result = select.select(worstCase.clone(), k);
            metrics.stopTiming();

            assertEquals(k, result);
            assertTrue(metrics.getElapsedTimeMs() < 50,
                "Deterministic select should handle sorted arrays efficiently");
        }
    }
}