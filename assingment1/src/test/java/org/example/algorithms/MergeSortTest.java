package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {
    private MetricsCollector metrics;
    private MergeSort mergeSort;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
        mergeSort = new MergeSort(metrics);
    }

    @Test
    void testEmptyArray() {
        int[] array = {};
        mergeSort.sort(array);
        assertEquals(0, array.length);
    }

    @Test
    void testSingleElement() {
        int[] array = {42};
        mergeSort.sort(array);
        assertArrayEquals(new int[]{42}, array);
    }

    @Test
    void testTwoElements() {
        int[] array = {2, 1};
        mergeSort.sort(array);
        assertArrayEquals(new int[]{1, 2}, array);
    }

    @Test
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5};
        mergeSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    void testReverseSorted() {
        int[] array = {5, 4, 3, 2, 1};
        mergeSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    void testDuplicateElements() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        mergeSort.sort(array);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}, array);
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
        mergeSort.sort(array);
        metrics.stopTiming();

        assertArrayEquals(expected, array);
        assertTrue(metrics.getMaxDepth() > 0);
        assertTrue(metrics.getComparisons() > 0);
    }

    @Test
    void testRecursionDepthBounds() {
        int[] array = new int[1024];
        for (int i = 0; i < array.length; i++) {
            array[i] = array.length - i;
        }

        mergeSort.sort(array);

        int expectedMaxDepth = (int) Math.ceil(Math.log(array.length) / Math.log(2));
        assertTrue(metrics.getMaxDepth() <= expectedMaxDepth + 5,
            String.format("Recursion depth %d should be close to log(n)=%d",
                metrics.getMaxDepth(), expectedMaxDepth));
    }
}