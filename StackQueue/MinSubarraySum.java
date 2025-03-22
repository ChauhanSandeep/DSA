package StackQueue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Sum of Subarray Minimums - Monotonic Stack Approach
 * ---------------------------------------------------
 * Given an array, return the sum of the minimum value in every subarray.
 *
 * **Approach:**
 * - Use a **monotonic increasing stack** to efficiently compute the contribution of each element.
 * - For each element `nums[i]`, determine:
 *   - `previousSmallerIndex`: The index of the closest smaller element on the left.
 *   - `nextSmallerIndex`: The index of the closest smaller element on the right.
 * - Each element contributes to **all subarrays where it is the minimum**.
 * - Formula: `(nums[i] * (i - previousSmallerIndex) * (nextSmallerIndex - i))`
 *
 * **Time Complexity:** O(n)  (Each element is pushed/popped from the stack at most once)  
 * **Space Complexity:** O(n) (Stack storage)  
 *
 * **LeetCode Link:**  
 * https://leetcode.com/problems/sum-of-subarray-minimums/
 */
public class MinSubarraySum {

    private static final int MODULO = (int) 1e9 + 7;

    public static void main(String[] args) {
        int[] nums = {3, 5, 4, 12, 8, 10, 11, 4};
        System.out.println("Sum of Subarray Minimums: " + new MinSubarraySum().sumSubarrayMins(nums));
    }

    /**
     * Calculates the sum of the minimum values of all subarrays.
     *
     * @param nums Input array
     * @return Sum of all subarray minimums modulo 1e9+7
     */
    public int sumSubarrayMins(int[] nums) {
        if (nums == null || nums.length == 0) return 0; // Edge case: Empty input

        int length = nums.length;
        long result = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // Monotonic increasing stack

        stack.push(-1); // Sentinel value for boundary handling

        for (int i = 0; i < length; i++) {
            while (stack.peek() != -1 && nums[stack.peek()] > nums[i]) {
                result = (result + calculateContribution(stack, nums, i)) % MODULO;
            }
            stack.push(i);
        }

        // Process remaining elements in stack
        while (stack.peek() != -1) {
            result = (result + calculateContribution(stack, nums, length)) % MODULO;
        }

        return (int) result;
    }

    /**
     * Calculates the contribution of the current top of the stack.
     *
     * @param stack Monotonic increasing stack
     * @param nums  Input array
     * @param nextSmallerIndex The index of the next smaller element (or array length for final pass)
     * @return Contribution of the popped element to the final sum
     */
    private long calculateContribution(Deque<Integer> stack, int[] nums, int nextSmallerIndex) {
        int currentIndex = stack.pop();
        int previousSmallerIndex = stack.peek(); // Previous smaller element index
        return (long) nums[currentIndex] * (nextSmallerIndex - currentIndex) * (currentIndex - previousSmallerIndex);
    }
}
