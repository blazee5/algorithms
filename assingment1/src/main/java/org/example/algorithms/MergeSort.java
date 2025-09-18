package org.example.algorithms;

import org.example.metrics.MetricsCollector;

public class MergeSort {
    private static final int INSERTION_SORT_CUTOFF = 10;
    private final MetricsCollector metrics;

    public MergeSort(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int[] auxiliary = new int[array.length];
        metrics.incrementAllocations();

        mergeSort(array, auxiliary, 0, array.length - 1);
    }

    private void mergeSort(int[] array, int[] auxiliary, int low, int high) {
        metrics.enterRecursion();

        try {
            if (high - low <= INSERTION_SORT_CUTOFF) {
                insertionSort(array, low, high);
                return;
            }

            int mid = low + (high - low) / 2;

            mergeSort(array, auxiliary, low, mid);
            mergeSort(array, auxiliary, mid + 1, high);

            if (array[mid] <= array[mid + 1]) {
                metrics.incrementComparisons();
                return;
            }

            merge(array, auxiliary, low, mid, high);
        } finally {
            metrics.exitRecursion();
        }
    }

    private void merge(int[] array, int[] auxiliary, int low, int mid, int high) {
        System.arraycopy(array, low, auxiliary, low, high - low + 1);

        int i = low;
        int j = mid + 1;
        int k = low;

        while (i <= mid && j <= high) {
            metrics.incrementComparisons();
            if (auxiliary[i] <= auxiliary[j]) {
                array[k++] = auxiliary[i++];
            } else {
                array[k++] = auxiliary[j++];
            }
            metrics.incrementSwaps();
        }

        while (i <= mid) {
            array[k++] = auxiliary[i++];
            metrics.incrementSwaps();
        }

        while (j <= high) {
            array[k++] = auxiliary[j++];
            metrics.incrementSwaps();
        }
    }

    private void insertionSort(int[] array, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= low && array[j] > key) {
                metrics.incrementComparisons();
                array[j + 1] = array[j];
                metrics.incrementSwaps();
                j--;
            }

            if (j >= low) {
                metrics.incrementComparisons();
            }

            array[j + 1] = key;
            if (j + 1 != i) {
                metrics.incrementSwaps();
            }
        }
    }
}