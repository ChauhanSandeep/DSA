package arrays.indexhash;



import java.util.Arrays;/**
 * Problem: First Missing Positive
 *
 * Given an unsorted integer array, return the smallest positive integer that does
 * not appear in the array. The solution must run in linear time and use only
 * constant extra space, so the array itself has to serve as the bookkeeping.
 *
 * Leetcode: https://leetcode.com/problems/first-missing-positive/
 * Rating:   acceptance 43.3% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Index hashing | In-place sign marking
 *
 * Example:
 *   Input:  [3,4,-1,1]
 *   Output: 2
 *   Why:    1 is present, 2 is missing, and any values outside 1..n cannot be the
 *           first missing positive before 2.
 *
 * Follow-ups:
 *   1. What if the array is read-only?
 *      Use a HashSet, or accept slower repeated scans if extra space is forbidden.
 *   2. What if you need the kth missing positive?
 *      Count missing positions after marking, then continue beyond n if needed.
 *   3. What if zero is also considered a valid target?
 *      Shift the tracked range and reserve one index to represent the boundary value.
 *
 * Related: Missing Number (268), Kth Missing Positive Number (1539).
 */
public class FirstMissingPositive {
    public static void main(String[] args) {
        FirstMissingPositive solver = new FirstMissingPositive();
        int[][] inputs = {{3, 4, -1, 1}, {1, 2, 0}, {7, 8, 9, 11, 12}};
        int[] expected = {2, 3, 1};

        for (int i = 0; i < inputs.length; i++) {
            int[] copy = inputs[i].clone();
            int got = solver.firstMissingPositive(copy);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: the answer must be in the range 1..n+1 for an array of length n.
     * Any number outside 1..n cannot help prove that one of those n positive slots
     * exists. After confirming that 1 is present, we replace all irrelevant values
     * with 1 so every remaining value is safe to use as an index marker. Then the
     * sign at an index tells us whether the corresponding positive number appeared;
     * the first positive sign left behind points to the missing value.
     *
     * Time:  O(n) - the method uses a constant number of linear passes.
     * Space: O(1) - the input array stores the presence marks in place.
     *
     * @param nums array to inspect and modify in place
     * @return smallest positive integer missing from nums
     */
    public int firstMissingPositive(int[] nums) {
        int len = nums.length;
        boolean containsOne = false;

        // Replace negative numbers, zeros,
        // and numbers larger than n with 1s.
        // After this nums contains only positive numbers.
        for (int i = 0; i < len; i++) {
            // Check whether 1 is in the original array
            if (nums[i] == 1) {
                containsOne = true;
            }
            if (nums[i] <= 0 || nums[i] > len) {
                nums[i] = 1;
            }
        }

        if (!containsOne) {
            return 1;
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