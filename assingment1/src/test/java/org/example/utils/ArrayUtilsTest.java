package org.example.utils;

import org.example.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {
    private MetricsCollector metrics;

    @BeforeEach
    void setUp() {
        metrics = new MetricsCollector();
    }

    @Test
    void testSwap() {
        int[] array = {1, 2, 3, 4, 5};
        ArrayUtils.swap(array, 0, 4, metrics);
        assertArrayEquals(new int[]{5, 2, 3, 4, 1}, array);
        assertEquals(1, metrics.getSwaps());
    }

    @Test
    void testSwapSameIndex() {
        int[] array = {1, 2, 3, 4, 5};
        ArrayUtils.swap(array, 2, 2, metrics);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertEquals(0, metrics.getSwaps());
    }

    @Test
    void testPartition() {
        int[] array = {3, 6, 8, 10, 1, 2, 1};
        int pivotValue = array[array.length - 1];
        int pivotIndex = ArrayUtils.partition(array, 0, array.length - 1, metrics);

        assertEquals(pivotValue, array[pivotIndex]);

        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(array[i] <= array[pivotIndex],
                String.format("Element at index %d (%d) should be <= pivot (%d)", i, array[i], array[pivotIndex]));
        }

        for (int i = pivotIndex + 1; i < array.length; i++) {
            assertTrue(array[i] >= array[pivotIndex],
                String.format("Element at index %d (%d) should be >= pivot (%d)", i, array[i], array[pivotIndex]));
        }

        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getSwaps() >= 0);
    }

    @Test
    void testShuffle() {
        int[] original = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] array = original.clone();

        ArrayUtils.shuffle(array, new Random(42));

        assertEquals(original.length, array.length);

        boolean isShuffled = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != original[i]) {
                isShuffled = true;
                break;
            }
        }
        assertTrue(isShuffled, "Array should be shuffled");

        java.util.Arrays.sort(array);
        assertArrayEquals(original, array);
    }

    @Test
    void testIsSorted() {
        assertTrue(ArrayUtils.isSorted(new int[]{1, 2, 3, 4, 5}));
        assertTrue(ArrayUtils.isSorted(new int[]{1, 1, 2, 2, 3}));
        assertTrue(ArrayUtils.isSorted(new int[]{5}));
        assertTrue(ArrayUtils.isSorted(new int[]{}));

        assertFalse(ArrayUtils.isSorted(new int[]{1, 3, 2, 4, 5}));
        assertFalse(ArrayUtils.isSorted(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void testCheckBounds() {
        int[] array = {1, 2, 3, 4, 5};

        assertDoesNotThrow(() -> ArrayUtils.checkBounds(array, 0));
        assertDoesNotThrow(() -> ArrayUtils.checkBounds(array, 4));
        assertDoesNotThrow(() -> ArrayUtils.checkBounds(array, 0, 4));
        assertDoesNotThrow(() -> ArrayUtils.checkBounds(array, 2, 2));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.checkBounds(null, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> ArrayUtils.checkBounds(array, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> ArrayUtils.checkBounds(array, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> ArrayUtils.checkBounds(array, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> ArrayUtils.checkBounds(array, 0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> ArrayUtils.checkBounds(array, 3, 2));
    }

    @Test
    void testCheckNonNull() {
        assertDoesNotThrow(() -> ArrayUtils.checkNonNull("test", 42, new Object()));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.checkNonNull("test", null, new Object()));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.checkNonNull((Object) null));
    }

    @Test
    void testMedian3() {
        int[] array = {5, 2, 8};

        assertEquals(0, ArrayUtils.median3(array, 0, 1, 2, metrics));
        assertEquals(3, metrics.getComparisons());

        metrics.reset();
        array = new int[]{2, 5, 8};
        assertEquals(1, ArrayUtils.median3(array, 0, 1, 2, metrics));

        metrics.reset();
        array = new int[]{8, 5, 2};
        assertEquals(1, ArrayUtils.median3(array, 0, 1, 2, metrics));
    }

    @Test
    void testInsertionSort() {
        int[] array = {5, 2, 8, 1, 9};
        ArrayUtils.insertionSort(array, 0, array.length - 1, metrics);

        assertArrayEquals(new int[]{1, 2, 5, 8, 9}, array);
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getSwaps() >= 0);
    }

    @Test
    void testInsertionSortPartial() {
        int[] array = {0, 5, 2, 8, 1, 9, 10};
        ArrayUtils.insertionSort(array, 1, 5, metrics);

        assertArrayEquals(new int[]{0, 1, 2, 5, 8, 9, 10}, array);
    }
}