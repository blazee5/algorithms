package algorithms;

import metrics.PerformanceTracker;

public class KadaneAlgorithm {
    private final PerformanceTracker tracker;

    public KadaneAlgorithm() {
        this.tracker = new PerformanceTracker();
    }

    public KadaneAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public MaxSubarrayResult findMaxSubarray(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }

        tracker.incrementArrayAccesses();
        int maxSum = array[0];
        int currentSum = array[0];
        int start = 0;
        int end = 0;
        int tempStart = 0;

        for (int i = 1; i < array.length; i++) {
            tracker.incrementArrayAccesses();
            int currentElement = array[i];

            tracker.incrementComparisons();
            if (currentElement > currentSum + currentElement) {
                currentSum = currentElement;
                tempStart = i;
            } else {
                currentSum = currentSum + currentElement;
            }

            tracker.incrementComparisons();
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }

        return new MaxSubarrayResult(maxSum, start, end);
    }

    public PerformanceTracker getTracker() {
        return tracker;
    }

    public static class MaxSubarrayResult {
        private final int maxSum;
        private final int startIndex;
        private final int endIndex;

        public MaxSubarrayResult(int maxSum, int startIndex, int endIndex) {
            this.maxSum = maxSum;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public int getMaxSum() {
            return maxSum;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        @Override
        public String toString() {
            return String.format("MaxSum: %d, Range: [%d, %d]", maxSum, startIndex, endIndex);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            MaxSubarrayResult that = (MaxSubarrayResult) obj;
            return maxSum == that.maxSum && startIndex == that.startIndex && endIndex == that.endIndex;
        }

        @Override
        public int hashCode() {
            int result = maxSum;
            result = 31 * result + startIndex;
            result = 31 * result + endIndex;
            return result;
        }
    }
}