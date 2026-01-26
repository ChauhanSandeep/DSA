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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FirstMissingPositive {

    public static void main(String[] args) {
        int[] arr = {3, 4, -1, 0, 1, 10};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    /**
     * Finds first missing positive using marking by negating array values.
     *
     * Algorithm:
     * - Replace all invalid numbers (non-positive or > n) with INVALID_NUMBER
     *   - Ensures we only care about numbers in [1, n]
     *   - Invalid numbers won't interfere with marking phase
     * - For each number in [1, n], negate value at corresponding index to mark presence
     *   - For number x, mark index (x-1) by negating its value
     *   - Use absolute value to handle already negated indices
     *   - If number x exists, nums[x-1] will be negative
     * - Scan for first positive value; its index +1 is the answer
     *   - If nums[i] > 0, then number (i+1) is missing
     *   - First positive value found is the answer
     * Step 4: If all marked, return n+1
     *         - All numbers [1, n] are present
     *         - Answer must be n+1
     *
     * Key insight: Array becomes hash table where index represents number and
     * sign represents presence. Positive = missing, Negative = present.
     *
     * Time Complexity: O(N) where N is array length. Three passes: normalize, mark, scan.
     * Space Complexity: O(1) using only constant extra space. Array modified in-place.
     *
     * @param nums array of integers (will be modified)
     * @return smallest missing positive integer
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;

        // Replace negative numbers, zeros,
        // and numbers larger than n with 1s.
        // After this nums contains only positive numbers.
        for (int i = 0; i < len; i++) {
            // Check whether 1 is in the original array
            if (nums[i] == 1) {
                return 1;
            }
            if (nums[i] <= 0 || nums[i] > len) {
                nums[i] = 1;
            }
        }

        // Mark whether integers 1 to n are in nums
        // Use index as a hash key and negative sign as a presence detector.
        for (int i = 0; i < len; i++) {
            int value = Math.abs(nums[i]);
            if (value == len) {
                // Means that number of value n (i.e., len) is present
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[value] = -Math.abs(nums[value]);
            }
        }

        // First positive in nums is smallest missing positive integer
        for (int i = 1; i < len; i++) {
            if (nums[i] > 0) return i;
        }

        // nums[0] stores whether n (i.e., len) is in nums
        if (nums[0] > 0) {
            return len;
        }

        // If nums contains all elements 1 to n
        // the smallest missing positive number is n + 1
        return len + 1;
    }
}