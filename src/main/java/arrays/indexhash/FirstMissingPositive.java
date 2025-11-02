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
        int[] arr = {3, 4, -1, 0, 1, 10};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    private static final int INVALID_NUMBER = Integer.MAX_VALUE;

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
        int length = nums.length;

        // Phase 1: Normalize array - replace all invalid numbers
        for (int i = 0; i < length; i++) {
            if (nums[i] <= 0 || nums[i] > length) {
                nums[i] = INVALID_NUMBER;
            }
        }

        // Phase 2: Mark presence of numbers by negating values at corresponding indices
        for (int i = 0; i < length; i++) {
            int number = Math.abs(nums[i]);

            // Only mark if number is in valid range [1, n]
            if (number >= 1 && number <= length) {
                int indexToMark = number - 1;  // Convert to 0-indexed position

                // Negate to mark presence (only if not already negative)
                if (nums[indexToMark] > 0) {
                    nums[indexToMark] = -nums[indexToMark];
                }
            }
        }

        // Phase 3: Find first positive value; its index +1 is the answer
        for (int i = 0; i < length; i++) {
            if (nums[i] > 0) {
                return i + 1;  // Index +1 is the missing number
            }
        }

        // Phase 4: All indices marked, so all numbers [1,n] are present
        return length + 1;
    }
}