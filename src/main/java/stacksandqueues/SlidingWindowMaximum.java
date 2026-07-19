package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

/**
 * Problem: Sliding Window Maximum
 *
 * A window of size k moves from left to right across an integer array. For each
 * window position, return the maximum value visible inside that window. The
 * output has one value for every full window.
 *
 * Leetcode: https://leetcode.com/problems/sliding-window-maximum/ (Hard)
 * Rating:   acceptance 49.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Monotonic deque | Sliding window | Range maximum
 *
 * Example:
 *   Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [3,3,5,5,6,7]
 *   Why:    the six length-3 windows have maximums 3, 3, 5, 5, 6, and 7 in order.
 *
 * Follow-ups:
 *   1. Need sliding window minimum instead?
 *      Reverse the deque comparison to keep values increasing.
 *   2. Need both min and max in each window?
 *      Maintain two deques, one decreasing for max and one increasing for min.
 *   3. Need arbitrary range maximum queries after preprocessing?
 *      Use a sparse table for static arrays or a segment tree for updates.
 *   4. The input is a stream?
 *      Keep the same deque of candidate indices and expire indices older than k.
 *
 * Related: Sliding Window Median (480), Constrained Subsequence Sum (1425).
 */
public class SlidingWindowMaximum {
        /**
     * Intuition: any smaller value behind a newer larger value can never become
     * the window maximum. The deque stores candidate indices in decreasing value
     * order, so its front is always the maximum for the current window.
     *
     * Algorithm:
     *   1. Scan indices from left to right.
     *   2. Remove indices that are outside the current window.
     *   3. Remove smaller values from the back of the deque.
     *   4. Add the current index.
     *   5. Record the front value once the first full window exists.
     *
     * Time:  O(n) - each index enters and leaves the deque at most once.
     * Space: O(k) - the deque stores candidates from the window.
     *
     * @param nums input array
     * @param k window size
     * @return maximum value for each full window
     */
public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) {
            return new int[0];
        }

        int length = nums.length;
        int[] result = new int[length - k + 1];
        int resultIndex = 0;

        // Deque to store indices of elements in the current window
        // The deque maintains elements in decreasing order of their values
        Deque<Integer> deque = new ArrayDeque<>(); // monotonic decreasing deque

        for (int i = 0; i < length; i++) {
            // Remove indices that are out of the current window from the front
            while (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.poll();
            }

            // Remove smaller elements from the back of the deque
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            // Add current index to the deque
            deque.offer(i);

            // The front of the deque is the maximum element for the current window
            if (i >= k - 1) {
                result[resultIndex++] = nums[deque.peek()];
            }
        }

        return result;
    }

    /**
     * Alternative approach using a max-heap (priority queue).
     * This approach has O(n log k) time complexity.
     */
    public int[] maxSlidingWindowHeap(int[] nums, int k) {
        if (nums == null || k <= 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = new int[n - k + 1];
        int ri = 0;

        // Max heap to store elements in the current window
        java.util.PriorityQueue<Integer> maxHeap = new java.util.PriorityQueue<>(
            (a, b) -> nums[b] - nums[a]
        );

        for (int i = 0; i < n; i++) {
            // Remove elements that are out of the current window
            while (!maxHeap.isEmpty() && maxHeap.peek() <= i - k) {
                maxHeap.poll();
            }

            // Add current element to the heap
            maxHeap.offer(i);

            // The maximum element is at the top of the heap
            if (i >= k - 1) {
                result[ri++] = nums[maxHeap.peek()];
            }
        }

        return result;
    }

    /**
     * Finds the minimum in each sliding window.
     * This is a variation of the original problem.
     */
    public int[] minSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = new int[n - k + 1];
        int ri = 0;

        // Deque to store indices of elements in the current window
        // The deque maintains elements in increasing order of their values
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // Remove indices that are out of the current window from the front
            while (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.poll();
            }

            // Remove larger elements from the back of the deque
            while (!deque.isEmpty() && nums[deque.peekLast()] > nums[i]) {
                deque.pollLast();
            }

            // Add current index to the deque
            deque.offer(i);

            // The front of the deque is the minimum element for the current window
            if (i >= k - 1) {
                result[ri++] = nums[deque.peek()];
            }
        }

        return result;
    }

    /**
     * Finds both min and max in each sliding window.
     * Returns a 2D array where result[i][0] is min and result[i][1] is max for window i.
     */
    public int[][] minMaxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) {
            return new int[0][0];
        }

        int n = nums.length;
        int[][] result = new int[n - k + 1][2];
        int ri = 0;

        // Deques to track min and max
        Deque<Integer> minDeque = new ArrayDeque<>();
        Deque<Integer> maxDeque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // Remove out-of-window elements
            while (!minDeque.isEmpty() && minDeque.peek() < i - k + 1) {
                minDeque.poll();
            }
            while (!maxDeque.isEmpty() && maxDeque.peek() < i - k + 1) {
                maxDeque.poll();
            }

            // Maintain min deque (increasing order)
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] > nums[i]) {
                minDeque.pollLast();
            }

            // Maintain max deque (decreasing order)
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] < nums[i]) {
                maxDeque.pollLast();
            }

            // Add current index to both deques
            minDeque.offer(i);
            maxDeque.offer(i);

            // Record min and max for the current window
            if (i >= k - 1) {
                result[ri][0] = nums[minDeque.peek()];
                result[ri][1] = nums[maxDeque.peek()];
                ri++;
            }
        }

        return result;
    }

    /**
     * Solution for "Minimum Size Subarray Sum" (LeetCode #209).
     * This is a related sliding window problem.
     */
    public int minSubArrayLen(int target, int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int minLength = Integer.MAX_VALUE;
        int windowSum = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];

            while (windowSum >= target) {
                minLength = Math.min(minLength, right - left + 1);
                windowSum -= nums[left];
                left++;
            }
        }

        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    public static void main(String[] args) {
        SlidingWindowMaximum solver = new SlidingWindowMaximum();
        int[][] inputs = { {1}, {1, 3, -1, -3, 5, 3, 6, 7}, {9, 11}, {4, -2} };
        int[] kValues = {1, 3, 2, 1};
        String[] expected = {"[1]", "[3, 3, 5, 5, 6, 7]", "[11]", "[4, -2]"};
        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.maxSlidingWindow(inputs[i], kValues[i]);
            System.out.printf("nums=%s k=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), kValues[i], Arrays.toString(got), expected[i]);
        }
    }
}
