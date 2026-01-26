package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * You are given an array of integers nums, there is a sliding window of size k
 * which is moving from the very left of the array to the very right.
 * You can only see the k numbers in the window. Each time the sliding window moves
 * right by one position. Return the max sliding window.
 *
 * Example 1:
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation:
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 *
 * Example 2:
 * Input: nums = [1,-1], k = 1
 * Output: [1,-1]
 *
 * LeetCode: https://leetcode.com/problems/sliding-window-maximum/
 *
 * Follow-up Questions:
 * 1. How would you modify your solution to find the minimum in each sliding window?
 *    - We can use a similar approach but maintain a monotonically increasing deque.
 * 2. What if we need to find both min and max in each window efficiently?
 *    - We can use two deques to track both min and max simultaneously.
 * 3. How would you handle very large input arrays that don't fit in memory?
 *    - We could process the array in chunks that fit in memory.
 *
 * Related Problems:
 * - Minimum Window Substring (https://leetcode.com/problems/minimum-window-substring/)
 * - Longest Substring Without Repeating Characters (https://leetcode.com/problems/longest-substring-without-repeating-characters/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SlidingWindowMaximum {
    /**
     * Finds the maximum in each sliding window using a deque.
     *
     * @param nums The input array of integers
     * @param k The size of the sliding window
     * @return An array containing the maximum of each window
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
}
