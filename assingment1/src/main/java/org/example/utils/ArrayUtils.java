package org.example.utils;

import org.example.metrics.MetricsCollector;
import java.util.Random;

public class ArrayUtils {

    public static void swap(int[] array, int i, int j, MetricsCollector metrics) {
        if (i != j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            if (metrics != null) {
                metrics.incrementSwaps();
            }
        }
    }

    public static void swap(int[] array, int i, int j) {
        swap(array, i, j, null);
    }

    public static int partition(int[] array, int low, int high, MetricsCollector metrics) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (metrics != null) {
                metrics.incrementComparisons();
            }
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j, metrics);
            }
        }

        swap(array, i + 1, high, metrics);
        return i + 1;
    }

    public static int partition(int[] array, int low, int high) {
        return partition(array, low, high, null);
    }

    public static void shuffle(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(array, i, j);
        }
    }

    public static void shuffle(int[] array) {
        shuffle(array, new Random());
    }

    public static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }

    public static void checkBounds(int[] array, int index) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for array length %d", index, array.length)
            );
        }
    }

    public static void checkBounds(int[] array, int low, int high) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (low < 0 || high >= array.length || low > high) {
            throw new IndexOutOfBoundsException(
                String.format("Invalid range [%d, %d] for array length %d", low, high, array.length)
            );
        }
    }

    public static void checkNonNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException("Arguments cannot be null");
            }
        }
    }

    public static int median3(int[] array, int low, int mid, int high, MetricsCollector metrics) {
        if (metrics != null) {
            metrics.incrementComparisons();
        }
        if (array[low] > array[mid]) {
            if (metrics != null) {
                metrics.incrementComparisons();
            }
            if (array[mid] > array[high]) {
                return mid;
            } else {
                if (metrics != null) {
                    metrics.incrementComparisons();
                }
                return array[low] > array[high] ? high : low;
            }
        } else {
            if (metrics != null) {
                metrics.incrementComparisons();
            }
            if (array[low] > array[high]) {
                return low;
            } else {
                if (metrics != null) {
                    metrics.incrementComparisons();
                }
                return array[mid] > array[high] ? high : mid;
            }
        }
    }

    public static int median3(int[] array, int low, int mid, int high) {
        return median3(array, low, mid, high, null);
    }

    public static void insertionSort(int[] array, int low, int high, MetricsCollector metrics) {
        for (int i = low + 1; i <= high; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= low && array[j] > key) {
                if (metrics != null) {
                    metrics.incrementComparisons();
                }
                array[j + 1] = array[j];
                if (metrics != null) {
                    metrics.incrementSwaps();
                }
                j--;
            }

            if (j >= low && metrics != null) {
                metrics.incrementComparisons();
            }

            array[j + 1] = key;
            if (j + 1 != i && metrics != null) {
                metrics.incrementSwaps();
            }
        }
    }

    public static void insertionSort(int[] array, int low, int high) {
        insertionSort(array, low, high, null);
    }
}