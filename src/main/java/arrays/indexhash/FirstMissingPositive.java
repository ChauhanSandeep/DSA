package arrays.indexhash;

/**
 * Problem Statement:
 * Given an unsorted integer array nums, return the smallest positive integer that is not present in nums.
 * The algorithm must run in O(n) time and use O(1) auxiliary space.
 *
 * Example:
 * Input: nums = [3,4,-1,1]
 * Output: 2
 * Explanation: The array contains 1, 3, 4, and -1. The positive integers present are 1, 3, 4.
 * The smallest missing positive is 2.
 *
 * LeetCode Link: https://leetcode.com/problems/first-missing-positive/
 *
 * Follow-up Questions:
 * 1. What if we need to find the kth missing positive integer instead of the first?
 *    - We can modify the approach to count missing positives up to k, or use binary search on the count of present numbers up to a certain value.
 *      Relevant problem: https://leetcode.com/problems/kth-missing-positive-number/
 * 2. How would you handle this if the array contained duplicates?
 *    - The current approach already handles duplicates by ignoring them during placement or marking, as we only care about presence of each positive from 1 to n.
 * 3. What if the array is read-only and we cannot modify it?
 *    - We would need to use extra space like a set for tracking presence, but that violates O(1) space; alternatively, find a way to compute without modification, possibly with math, but it's challenging for O(n) time.
 */
public class FirstMissingPositive {

    public static void main(String[] args) {
        int[] arr = {1, 2, 0};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    /**
     * Using marking (negating values) to track presence.
     *
     * Step-by-step explanation:
     * 1. Replace all non-positive numbers with n+1 (out of range).
     * 2. For each number in [1, n], use it as index and negate the value there to mark presence.
     *    - Use absolute value to handle already negated indices.
     * 3. Scan for the first non-negative value; its index +1 is missing.
     * 4. If all marked, return n+1. Handle case where 1 is missing separately if needed.
     *
     * Algorithm: Index Marking
     * Time Complexity: O(n) - Two passes over the array.
     * Space Complexity: O(1) - In-place modification.
     *
     * @param nums the input array of integers
     * @return the smallest missing positive integer
     */
    public int firstMissingPositive(int[] nums) { // [3,4,-1,1]
        int length = nums.length;
        boolean containsOne = false;

        // Step 1: Replace non-positive and out-of-range with 1 (temporarily), and check if 1 is present
        for (int i = 0; i < length; i++) {
            if (nums[i] == 1) {
                containsOne = true;
            }
            if (nums[i] <= 0 || nums[i] > length) {
                nums[i] = 1;
            }
        }
        // [3,4,1,1]

        // If 1 is not present, it's the answer (edge case)
        if (!containsOne) {
            return 1;
        }

        // Step 2: Mark presence by negating at index abs(num) - 1
        for (int i = 0; i < length; i++) {
            int index = Math.abs(nums[i]) - 1;
            if (nums[index] > 0) {
                nums[index] = -nums[index];
            }
        }

        // Step 3: Find the first positive (unmarked) index
        for (int i = 0; i < length; i++) {
            if (nums[i] > 0) {
                return i + 1;
            }
        }

        // All marked, missing is n + 1
        return length + 1;
    }
}