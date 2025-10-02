package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class KadaneAlgorithmTest {
    private KadaneAlgorithm algorithm;
    private PerformanceTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
        algorithm = new KadaneAlgorithm(tracker);
    }

    @Test
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.findMaxSubarray(null));
    }

    @Test
    void testEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> algorithm.findMaxSubarray(new int[]{}));
    }

    @Test
    void testSingleElement() {
        int[] array = {5};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(5, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(0, result.getEndIndex());
    }

    @Test
    void testSingleNegativeElement() {
        int[] array = {-5};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(-5, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(0, result.getEndIndex());
    }

    @Test
    void testAllNegativeElements() {
        int[] array = {-2, -3, -1, -5, -4};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(-1, result.getMaxSum());
        assertEquals(2, result.getStartIndex());
        assertEquals(2, result.getEndIndex());
    }

    @Test
    void testAllPositiveElements() {
        int[] array = {1, 2, 3, 4, 5};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(15, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(4, result.getEndIndex());
    }

    @Test
    void testMixedElements() {
        int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(6, result.getMaxSum());
        assertEquals(3, result.getStartIndex());
        assertEquals(6, result.getEndIndex());
    }

    @Test
    void testMaxAtBeginning() {
        int[] array = {5, -2, 1, -3, 2};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(5, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(0, result.getEndIndex());
    }

    @Test
    void testMaxAtEnd() {
        int[] array = {-2, -3, -1, 5};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(5, result.getMaxSum());
        assertEquals(3, result.getStartIndex());
        assertEquals(3, result.getEndIndex());
    }

    @Test
    void testDuplicateElements() {
        int[] array = {3, 3, 3, 3};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(12, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(3, result.getEndIndex());
    }

    @Test
    void testZeroElements() {
        int[] array = {0, 0, 0, 0};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(0, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(0, result.getEndIndex());
    }

    @Test
    void testMixedWithZero() {
        int[] array = {-1, 0, -2, 0, -3};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(0, result.getMaxSum());
        assertEquals(1, result.getStartIndex());
        assertEquals(1, result.getEndIndex());
    }

    @Test
    void testLargePositiveSum() {
        int[] array = {100, 200, -50, 300, -100, 400};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(850, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(5, result.getEndIndex());
    }

    @Test
    void testAlternatingSignsPositiveStart() {
        int[] array = {5, -2, 4, -1, 3};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(9, result.getMaxSum());
        assertEquals(0, result.getStartIndex());
        assertEquals(4, result.getEndIndex());
    }

    @Test
    void testTwoElements() {
        int[] array = {-1, 2};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(2, result.getMaxSum());
        assertEquals(1, result.getStartIndex());
        assertEquals(1, result.getEndIndex());
    }

    @Test
    void testComplexCase() {
        int[] array = {-2, -5, 6, -2, -3, 1, 5, -6};
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(7, result.getMaxSum());
        assertEquals(2, result.getStartIndex());
        assertEquals(6, result.getEndIndex());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testParameterized(int[] array, int expectedSum, int expectedStart, int expectedEnd) {
        KadaneAlgorithm.MaxSubarrayResult result = algorithm.findMaxSubarray(array);
        assertEquals(expectedSum, result.getMaxSum());
        assertEquals(expectedStart, result.getStartIndex());
        assertEquals(expectedEnd, result.getEndIndex());
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(new int[]{1}, 1, 0, 0),
                Arguments.of(new int[]{-1}, -1, 0, 0),
                Arguments.of(new int[]{1, 2, 3}, 6, 0, 2),
                Arguments.of(new int[]{-1, -2, -3}, -1, 0, 0),
                Arguments.of(new int[]{5, -3, 5}, 7, 0, 2),
                Arguments.of(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6, 3, 6)
        );
    }

    @Test
    void testMetricsAreTracked() {
        int[] array = {1, 2, 3, 4, 5};
        tracker.reset();
        algorithm.findMaxSubarray(array);
        assertTrue(tracker.getArrayAccesses() > 0);
        assertTrue(tracker.getComparisons() > 0);
    }

    @Test
    void testMaxSubarrayResultEquality() {
        KadaneAlgorithm.MaxSubarrayResult result1 = new KadaneAlgorithm.MaxSubarrayResult(10, 0, 5);
        KadaneAlgorithm.MaxSubarrayResult result2 = new KadaneAlgorithm.MaxSubarrayResult(10, 0, 5);
        KadaneAlgorithm.MaxSubarrayResult result3 = new KadaneAlgorithm.MaxSubarrayResult(5, 0, 5);

        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    void testMaxSubarrayResultToString() {
        KadaneAlgorithm.MaxSubarrayResult result = new KadaneAlgorithm.MaxSubarrayResult(10, 2, 5);
        String expected = "MaxSum: 10, Range: [2, 5]";
        assertEquals(expected, result.toString());
    }
}