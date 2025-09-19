package org.example.algorithms;

import org.example.metrics.MetricsCollector;
import java.util.Random;

public class QuickSort {
    private final MetricsCollector metrics;
    private final Random random;

    public QuickSort(MetricsCollector metrics) {
        this.metrics = metrics;
        this.random = new Random();
    }

    public QuickSort(MetricsCollector metrics, long seed) {
        this.metrics = metrics;
        this.random = new Random(seed);
    }

    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] array, int low, int high) {
        while (low < high) {
            metrics.enterRecursion();

            try {
                int pivotIndex = randomizedPartition(array, low, high);

                int leftSize = pivotIndex - low;
                int rightSize = high - pivotIndex;

                if (leftSize <= rightSize) {
                    quickSort(array, low, pivotIndex - 1);
                    low = pivotIndex + 1;
                } else {
                    quickSort(array, pivotIndex + 1, high);
                    high = pivotIndex - 1;
                }
            } finally {
                metrics.exitRecursion();
            }
        }
    }

    private int randomizedPartition(int[] array, int low, int high) {
        int randomIndex = low + random.nextInt(high - low + 1);
        swap(array, randomIndex, high);
        return partition(array, low, high);
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incrementComparisons();
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        if (i != j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            metrics.incrementSwaps();
        }
    }
}