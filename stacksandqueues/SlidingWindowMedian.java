package stacksandqueues;

import java.util.*;

/**
 * Problem: Sliding Window Median
 *
 * The median is the middle value in an ordered integer list. If the size of the list is even,
 * there is no middle value. So the median is the mean of the two middle values.
 *
 * For example, for arr = [2,3,4], the median is 3.
 * For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
 *
 * Given an integer array nums and an integer k, there is a sliding window of size k which is moving
 * from the very left of the array to the very right. You can only see the k numbers in the window.
 * Each time the sliding window moves right by one position. Return the median array for each window
 * in the original array.
 *
 * Example:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [1.0,-1.0,-1.0,3.0,5.0,6.0]
 * Explanation:
 * Window position                Median
 * ---------------                -----
 * [1  3  -1] -3  5  3  6  7       1
 *  1 [3  -1  -3] 5  3  6  7      -1
 *  1  3 [-1  -3  5] 3  6  7      -1
 *  1  3  -1 [-3  5  3] 6  7       3
 *  1  3  -1  -3 [5  3  6] 7       5
 *  1  3  -1  -3  5 [3  6  7]      6
 *
 * LeetCode: https://leetcode.com/problems/sliding-window-median
 *
 * Time Complexity: O(n log k) where n is the number of elements in nums
 * Space Complexity: O(k) for the heaps
 */
public class SlidingWindowMedian {
    private PriorityQueue<Integer> maxHeap; // contains the smaller half
    private PriorityQueue<Integer> minHeap; // contains the larger half

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

    private void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());

        // Ensure maxHeap size is always >= minHeap size
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    private double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        } else {
            return ((double) maxHeap.peek() + minHeap.peek()) * 0.5;
        }
    }

    private void rebalance() {
        // Ensure maxHeap size is always >= minHeap size and difference is at most 1
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
}
