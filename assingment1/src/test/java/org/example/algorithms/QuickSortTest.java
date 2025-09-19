package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {
    private MetricsCollector metrics;
    private QuickSort quickSort;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
        quickSort = new QuickSort(metrics, 42); // Fixed seed for reproducible tests
    }

    @Test
    void testEmptyArray() {
        int[] array = {};
        quickSort.sort(array);
        assertEquals(0, array.length);
    }

    @Test
    void testSingleElement() {
        int[] array = {42};
        quickSort.sort(array);
        assertArrayEquals(new int[]{42}, array);
    }

    @Test
    void testTwoElements() {
        int[] array = {2, 1};
        quickSort.sort(array);
        assertArrayEquals(new int[]{1, 2}, array);
    }

    @Test
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5};
        quickSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    void testReverseSorted() {
        int[] array = {5, 4, 3, 2, 1};
        quickSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    void testDuplicateElements() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        quickSort.sort(array);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}, array);
    }

    @Test
    void testAllSameElements() {
        int[] array = {5, 5, 5, 5, 5};
        quickSort.sort(array);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, array);
    }

    @Test
    void testRandomArray() {
        Random random = new Random(42);
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }

        int[] expected = array.clone();
        Arrays.sort(expected);

        metrics.startTiming();
        quickSort.sort(array);
        metrics.stopTiming();

        assertArrayEquals(expected, array);
        assertTrue(metrics.getMaxDepth() > 0);
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getSwaps() >= 0);
    }

    @Test
    void testRecursionDepthBounds() {
        int[] array = new int[1024];
        for (int i = 0; i < array.length; i++) {
            array[i] = array.length - i;
        }

        quickSort.sort(array);

        int expectedMaxDepth = 2 * (int) Math.floor(Math.log(array.length) / Math.log(2)) + 10;
        assertTrue(metrics.getMaxDepth() <= expectedMaxDepth,
            String.format("Recursion depth %d should be bounded by ~2*log(n)=%d with randomization",
                metrics.getMaxDepth(), expectedMaxDepth));
    }

    @Test
    void testRandomizedPivotImprovement() {
        int[] worstCase = new int[100];
        for (int i = 0; i < worstCase.length; i++) {
            worstCase[i] = i + 1;
        }

        MetricsCollector metrics1 = new MetricsCollector();
        QuickSort qs1 = new QuickSort(metrics1, 42);
        int[] array1 = worstCase.clone();
        qs1.sort(array1);

        MetricsCollector metrics2 = new MetricsCollector();
        QuickSort qs2 = new QuickSort(metrics2, 84);
        int[] array2 = worstCase.clone();
        qs2.sort(array2);

        assertArrayEquals(array1, array2);
        assertTrue(metrics1.getMaxDepth() <= 2 * Math.log(worstCase.length) / Math.log(2) + 20);
        assertTrue(metrics2.getMaxDepth() <= 2 * Math.log(worstCase.length) / Math.log(2) + 20);
    }
}