package arrays.heap;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Problem: Find Median from Data Stream
 *
 * Design a data structure that accepts numbers one at a time and can return the
 * median of all numbers seen so far. For an odd count, the median is the middle
 * value after sorting; for an even count, it is the average of the two middle values.
 *
 * Leetcode: https://leetcode.com/problems/find-median-from-data-stream/
 * Rating:   acceptance 54.7% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Design | Heap | Two balanced halves
 *
 * Example:
 *   Input:  addNum(1), addNum(2), findMedian(), addNum(3), findMedian()
 *   Output: 1.5, 2.0
 *   Why:    [1,2] has two middle values 1 and 2, while [1,2,3] has middle value 2.
 *
 * Follow-ups:
 *   1. What if numbers can be deleted too?
 *      Use two heaps with lazy deletion maps to discard removed values at the top.
 *   2. What if you need the 90th percentile instead of the median?
 *      Keep two partitions sized according to the desired percentile.
 *   3. What if the stream is distributed across machines?
 *      Use mergeable sketches or partition summaries rather than raw heaps.
 */
public class MedianFinder {
    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.printf("stream=[1, 2] -> %.1f  expected=1.5%n", medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.printf("stream=[1, 2, 3] -> %.1f  expected=2.0%n", medianFinder.findMedian());
    }

    private final PriorityQueue<Integer> maxHeap; // Stores smaller half (max at top)
    private final PriorityQueue<Integer> minHeap; // Stores larger half (min at top)

    /**
     * Initializes the MedianFinder object using two heaps approach.
     *
     * Intuition:
     * - To find the median, we need to know the middle element(s) of the list.
     * - We can maintain two heaps to store the smaller and larger halves of the list.
     * - The median is either the top of the max heap (odd count) or the average of the two heap tops (even count).
     *
     * Algorithm: Dual Heap Data Structure Setup
     * Step 1: Initialize max heap for smaller half of numbers (largest of small numbers at top)
     * Step 2: Initialize min heap for larger half of numbers (smallest of large numbers at top)
     * Step 3: Maintain invariant that heap sizes differ by at most 1
     * Step 4: Ensure all elements in max heap ≤ all elements in min heap
     *
     * Time Complexity: O(1) for initialization
     * Space Complexity: O(1) initially, grows to O(n) where n is total numbers added
     */
    public MedianFinder() {
        // Max heap for smaller half - use reverse comparator for max behavior
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // Min heap for larger half - natural ordering gives min behavior
        minHeap = new PriorityQueue<>();
    }

    /**
     * Intuition: the median only needs the values around the middle, not the whole
     * sorted order. Keep the smaller half in a max-heap and the larger half in a
     * min-heap. By moving the largest lower value into the min heap after every
     * insert, all lower values stay less than or equal to all upper values. Then,
     * if the min heap becomes larger, move its smallest value back so the lower
     * heap either has the same size or one extra value.
     *
     * Time:  O(log n) - each insertion performs a constant number of heap operations.
     * Space: O(n) - all streamed numbers are stored across the two heaps.
     *
     * @param num number to add to the data stream
     */
    public void addNum(int num) {
        // Step 1: Always add to max heap first
        maxHeap.offer(num);

        // Step 2: Move top of max heap to min heap to maintain ordering invariant
        // This ensures max heap contains only elements ≤ min heap elements
        minHeap.offer(maxHeap.poll());

        // Step 3: Rebalance if min heap becomes larger
        // Maintain size invariant: |maxHeap| - |minHeap| ∈ {0, 1}
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    /**
     * Intuition: the heap sizes encode whether the stream length is odd or even.
     * If the max heap has one extra value, its top is the exact middle. If the
     * heaps are equal size, the two middle values are the tops of the two heaps,
     * because every max-heap value is less than or equal to every min-heap
     * value. Averaging those two tops gives the median without sorting the stream.
     *
     * Time:  O(1) - peeking at heap tops does not depend on stream size.
     * Space: O(1) - finding the median allocates no additional storage.
     *
     * @return median of all numbers added so far
     */
    public double findMedian() {
        // Check heap size equality
        if (maxHeap.size() > minHeap.size()) {
            // Odd total count - max heap has the extra element
            return maxHeap.peek();
        } else {
            // Even total count - average of both tops
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }


    /**
     * Thread-safe implementation using synchronized methods.
     * Suitable for concurrent access scenarios.
     */
    public static class MedianFinderThreadSafe {

        private final PriorityQueue<Integer> maxHeap;
        private final PriorityQueue<Integer> minHeap;
        private final Object lock = new Object();

        /**
         * Initializes thread-safe median finder.
         *
         * Algorithm: Thread-Safe Dual Heap Setup
         * Step 1: Initialize heaps similar to standard approach
         * Step 2: Create synchronization lock for thread safety
         * Step 3: All operations will be synchronized on the lock object
         *
         * Time Complexity: O(1)
         * Space Complexity: O(1) initially
         */
        public MedianFinderThreadSafe() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        /**
         * Thread-safe number addition.
         *
         * Algorithm: Synchronized Heap Operations
         * Step 1: Acquire exclusive lock for thread safety
         * Step 2: Perform same heap operations as standard implementation
         * Step 3: Release lock automatically via synchronized block
         *
         * Time Complexity: O(log n) plus synchronization overhead
         * Space Complexity: O(1) auxiliary space per call
         *
         * @param num the number to add
         */
        public void addNum(int num) {
            synchronized (lock) {
                maxHeap.offer(num);
                minHeap.offer(maxHeap.poll());

                if (minHeap.size() > maxHeap.size()) {
                    maxHeap.offer(minHeap.poll());
                }
            }
        }

        /**
         * Thread-safe median calculation.
         *
         * Algorithm: Synchronized Median Access
         * Step 1: Acquire shared lock for consistent read
         * Step 2: Calculate median using same logic as standard implementation
         * Step 3: Return result while maintaining thread safety
         *
         * Time Complexity: O(1) plus synchronization overhead
         * Space Complexity: O(1)
         *
         * @return the median value
         */
        public double findMedian() {
            synchronized (lock) {
                if (maxHeap.size() > minHeap.size()) {
                    return maxHeap.peek();
                } else {
                    return (maxHeap.peek() + minHeap.peek()) / 2.0;
                }
            }
        }
    }

}