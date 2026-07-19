package stacksandqueues;

import java.util.*;

/**
 * Problem: Sliding Window Median
 *
 * The median is the middle value in an ordered list; if the list has even size,
 * it is the average of the two middle values. A window of size k moves across
 * nums, and we return the median for each full window.
 *
 * Leetcode: https://leetcode.com/problems/sliding-window-median/ (Hard)
 * Rating:   acceptance 39.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Two heaps | Sliding window | Median maintenance
 *
 * Example:
 *   Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [1.0,-1.0,-1.0,3.0,5.0,6.0]
 *   Why:    sorting each length-3 window gives medians 1, -1, -1, 3, 5, and 6.
 *
 * Follow-ups:
 *   1. Need faster heap-based removal?
 *      Use two heaps with lazy deletion maps and valid-size counters.
 *   2. Need arbitrary percentile instead of median?
 *      Keep an order-statistics tree or Fenwick tree over compressed values.
 *   3. Need exact medians for a stream with unbounded k?
 *      The same two-half structure works, but memory grows with k.
 *   4. Need decimal values instead of integers?
 *      Store doubles in multisets and define deterministic tie handling.
 *
 * Related: Sliding Window Maximum (239), Find Median from Data Stream (295).
 */
public class SlidingWindowMedian {
    private PriorityQueue<Integer> maxHeap; // contains the smaller half
    private PriorityQueue<Integer> minHeap; // contains the larger half

        /**
     * Intuition: the median is the boundary between the smaller half and larger
     * half. `maxHeap` keeps the smaller half with its largest value on top, while
     * `minHeap` keeps the larger half with its smallest value on top. Balanced
     * sizes make the heap tops enough to compute each median.
     *
     * Algorithm:
     *   1. Initialize maxHeap and minHeap.
     *   2. Add each incoming value with `addNum`.
     *   3. Once a full window exists, record `findMedian`.
     *   4. Remove the outgoing value from the heap that should contain it and rebalance.
     *
     * Time:  O(n * k) - PriorityQueue.remove(value) can scan the window.
     * Space: O(k) - both heaps store the current window.
     *
     * @param nums input array
     * @param k window size
     * @return median for each full window
     */
public double[] medianSlidingWindow(int[] nums, int k) {
        // Max heap: contains the smaller half of the numbers (inverted to simulate max heap)
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // Min heap: contains the larger half of the numbers
        minHeap = new PriorityQueue<>();

        double[] result = new double[nums.length - k + 1];

        for (int i = 0; i < nums.length; i++) {
            // Add the new number to one of the heaps
            addNum(nums[i]);

            // If we have at least k elements in the window
            if (i >= k - 1) {
                // Add the median to the result
                result[i - k + 1] = findMedian();

                // Remove the element going out of the window
                int toRemove = nums[i - k + 1];
                if (toRemove <= maxHeap.peek()) {
                    maxHeap.remove(toRemove);
                } else {
                    minHeap.remove(toRemove);
                }

                // Rebalance the heaps
                rebalance();
            }
        }

        return result;
    }

    /** Adds a number to the two-heap median structure. */
    private void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());

        // Ensure maxHeap size is always >= minHeap size
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    /** Returns the median from the balanced heap tops. */
    private double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        } else {
            return ((double) maxHeap.peek() + minHeap.peek()) * 0.5;
        }
    }

    /** Restores the heap size invariant after removal. */
    private void rebalance() {
        // Ensure maxHeap size is always >= minHeap size and difference is at most 1
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public static void main(String[] args) {
        SlidingWindowMedian solver = new SlidingWindowMedian();
        int[][] inputs = { {1}, {1, 3, -1, -3, 5, 3, 6, 7}, {1, 2}, {2147483647, 2147483647} };
        int[] kValues = {1, 3, 2, 2};
        String[] expected = {"[1.0]", "[1.0, -1.0, -1.0, 3.0, 5.0, 6.0]", "[1.5]", "[2.147483647E9]"};
        for (int i = 0; i < inputs.length; i++) {
            double[] got = solver.medianSlidingWindow(inputs[i], kValues[i]);
            System.out.printf("nums=%s k=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), kValues[i], Arrays.toString(got), expected[i]);
        }
    }
}
