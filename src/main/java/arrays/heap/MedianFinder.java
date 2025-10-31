package arrays.heap;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Find median in a stream of incoming integers.
 * What is median?
 * - Median is the middle value in an ordered integer list.
 * - If the size of the list is even, there is no middle value. So the median is the mean of the two middle values.
 * - For example, [2,3,4], the median is 3.
 * - For example, [2,3], the median is (2 + 3) / 2 = 2.5.
 *
 * Problem Statement:
 * The median is the middle value in an ordered integer list. If the size of the list is even,
 * there is no middle value, and the median is the mean of the two middle values.
 *
 * Design a data structure that supports the following two operations:
 * - void addNum(int num) - Add a integer number from the data stream to the data structure.
 * - double findMedian() - Return the median of all elements so far.
 *
 * LeetCode: https://leetcode.com/problems/find-median-from-data-stream/
 */
public class MedianFinder {

    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian()); // Output: 1.5
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian()); // Output: 2.0
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
     * Adds a number into the data structure while maintaining median accessibility.
     *
     * Balanced Heap Insertion with Size Maintenance:
     * Step 1: Add number to max heap first (smaller half)
     * Step 2: Move largest element from max heap to min heap to maintain ordering
     * Step 3: Balance heap sizes if min heap becomes larger than max heap
     * Step 4: Maintain invariant that max heap size - min heap size ∈ {0, 1}
     * Step 5: Ensure max heap top ≤ min heap top for correct partitioning
     *
     * Time Complexity: O(log n) where n is current number of elements
     * - O(log n) for each heap insertion/removal operation
     * - Constant number of heap operations per addNum call
     * Space Complexity: O(1) auxiliary space per call
     *
     * @param num the integer number to add to the data structure
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
     * Returns the median of all elements added so far.
     *
     * Algorithm: Median Calculation from Heap Tops
     * Step 1: Check if heaps have equal size (even total count)
     * Step 2: If equal size, median = average of both heap tops
     * Step 3: If unequal size, median = top of larger heap (max heap by invariant)
     * Step 4: Handle precision by using double arithmetic
     *
     * Time Complexity: O(1) - constant time access to heap tops
     * Space Complexity: O(1) - no additional space used
     *
     * @return the median value as a double
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