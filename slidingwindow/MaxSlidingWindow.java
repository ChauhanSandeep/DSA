package slidingwindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Problem: Find the maximum value in all subarrays of size `k`.
 * 
 * Approach:
 * - Use a **Deque (Double-ended Queue)** to maintain indices of useful elements.
 * - The front of the deque will always store the index of the **maximum element** for the current window.
 * - Before adding a new element:
 *   - Remove elements **smaller than the new one** (they’ll never be the max).
 *   - Remove elements **out of the window** (index < currentWindowStart).
 * 
 * Time Complexity: **O(N)** → Each element is added and removed from the deque **at most once**.
 * Space Complexity: **O(K)** → Deque stores at most `k` elements at any time.
 * 
 * LeetCode Link: https://leetcode.com/problems/sliding-window-maximum/
 */
public class MaxSlidingWindow {
    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println("Sliding Window Maximum: " + Arrays.toString(maxSlidingWindow(nums, k)));
    }

    /**
     * Returns the maximum of every contiguous subarray of size `k`.
     *
     * @param nums Input array
     * @param k    Window size
     * @return Array containing the maximums of all sliding windows
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return new int[0];

        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new LinkedList<>(); // Stores indices of useful elements

        for (int i = 0; i < n; i++) {
            // Remove elements out of the current sliding window
            while (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.pollFirst();
            }

            // Remove elements that are smaller than current element (they won't be max)
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            // Add the current element at the end of the deque
            deque.offerLast(i);

            // Store result once the first window is fully processed
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peek()];
            }
        }

        return result;
    }
}
