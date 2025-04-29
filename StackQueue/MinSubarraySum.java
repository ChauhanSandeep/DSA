package StackQueue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Sum of Subarray Minimums - Monotonic Stack Approach
 * ---------------------------------------------------
 * Given an array, return the sum of the minimum value in every subarray.
 * For example:
 * Input: arr = [3,1,2,4]
 * Output: 17
 * Explanation:
 * Subarrays are [3], [1], [2], [4], [3,1], [1,2], [2,4], [3,1,2], [1,2,4], [3,1,2,4].
 * Minimums are (taking min from each subarray) 3, 1, 2, 4, 1, 1, 2, 1, 1, 1.
 * Sum is 17.
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
        Deque<Integer> increasingStack = new ArrayDeque<>(); // Monotonic increasing increasingStack

        increasingStack.push(-1); // Sentinel value for boundary handling

        // For each element find the left and right minimum indices. The current element is the minimum for all subarrays
        // between the left and right minimum indices.
        for (int i = 0; i < length; i++) {
            while (increasingStack.peek() != -1 && nums[increasingStack.peek()] > nums[i]) {
                // current element is smaller than the top of the increasingStack.
                // So the current element is the next smaller element for the top of the increasingStack
                int rightMinimumNumberIndex = i;
                int currentMinNumberIndex = increasingStack.pop(); // the minimum element in the subarray between left and right minimum indices
                int leftMinimumNumberIndex = increasingStack.peek();
                result = (result + calculateContributionOfCurrentIndex(nums, leftMinimumNumberIndex, currentMinNumberIndex, rightMinimumNumberIndex)) % MODULO;
            }
            increasingStack.push(i);
        }

        // Process remaining elements in increasingStack
        while (increasingStack.peek() != -1) {
            // For these elements, the next smaller element is the end of the array
            int rightMinimumNumberIndex = length;
            int currentMinNumberIndex = increasingStack.pop(); // the minimum element in the subarray between left and right minimum indices
            int leftMinimumNumberIndex = increasingStack.peek();
            result = (result + calculateContributionOfCurrentIndex(nums, leftMinimumNumberIndex, currentMinNumberIndex, rightMinimumNumberIndex)) % MODULO;
        }

        return (int) result;
    }

    /**
     * Calculates the contribution of nums[currentIndex] being the minimum element
     * for all subarrays between leftMinimumNumberIndex and rightMinimumNumberIndex.
     *
     * Formula: nums[currentIndex] * (#subarrays where it's minimum)
     *         = nums[currentIndex] * (currentIndex - leftMinimumNumberIndex) * (rightMinimumNumberIndex - currentIndex)
     */
    private long calculateContributionOfCurrentIndex(int[] nums, int leftMinimumNumberIndex, int currentIndex, int rightMinimumNumberIndex) {
        int leftCount = currentIndex - leftMinimumNumberIndex;
        int rightCount = rightMinimumNumberIndex - currentIndex;
        return (long) nums[currentIndex] * leftCount * rightCount;
    }
}
