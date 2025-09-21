package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import org.example.utils.ArrayUtils;

public class DeterministicSelect {
    private final MetricsCollector metrics;

    public DeterministicSelect(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public int select(int[] array, int k) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k must be between 0 and array.length - 1");
        }

        return select(array, 0, array.length - 1, k);
    }

    private int select(int[] array, int low, int high, int k) {
        metrics.enterRecursion();

        try {
            if (high - low < 5) {
                ArrayUtils.insertionSort(array, low, high, metrics);
                return array[k];
            }

            int medianOfMedians = findMedianOfMedians(array, low, high);
            int pivotIndex = partitionAroundValue(array, low, high, medianOfMedians);

            if (pivotIndex == k) {
                return array[pivotIndex];
            } else if (k < pivotIndex) {
                return select(array, low, pivotIndex - 1, k);
            } else {
                return select(array, pivotIndex + 1, high, k);
            }
        } finally {
            metrics.exitRecursion();
        }
    }

    private int findMedianOfMedians(int[] array, int low, int high) {
        int n = high - low + 1;
        int numGroups = (n + 4) / 5;

        int[] medians = new int[numGroups];
        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(low + i * 5 + 4, high);

            ArrayUtils.insertionSort(array, groupLow, groupHigh, metrics);
            medians[i] = array[groupLow + (groupHigh - groupLow) / 2];
        }

        if (numGroups == 1) {
            return medians[0];
        }

        DeterministicSelect recursiveSelect = new DeterministicSelect(metrics);
        return recursiveSelect.select(medians, numGroups / 2);
    }

    private int partitionAroundValue(int[] array, int low, int high, int value) {
        int valueIndex = findValueIndex(array, low, high, value);
        if (valueIndex != -1) {
            ArrayUtils.swap(array, valueIndex, high, metrics);
        }

        return ArrayUtils.partition(array, low, high, metrics);
    }

    private int findValueIndex(int[] array, int low, int high, int value) {
        for (int i = low; i <= high; i++) {
            metrics.incrementComparisons();
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public int quickSelect(int[] array, int k) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k must be between 0 and array.length - 1");
        }

        return quickSelect(array, 0, array.length - 1, k);
    }

    private int quickSelect(int[] array, int low, int high, int k) {
        if (low == high) {
            return array[low];
        }

        metrics.enterRecursion();

        try {
            int pivotIndex = ArrayUtils.partition(array, low, high, metrics);

            if (pivotIndex == k) {
                return array[pivotIndex];
            } else if (k < pivotIndex) {
                return quickSelect(array, low, pivotIndex - 1, k);
            } else {
                return quickSelect(array, pivotIndex + 1, high, k);
            }
        } finally {
            metrics.exitRecursion();
        }
    }
}