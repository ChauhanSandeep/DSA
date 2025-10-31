package arrays.greedy;

/**
 * Non Decreasing Array
 *
 * Problem Statement:
 * Given an array nums with n integers, check if it could become non-decreasing
 * by modifying at most one element. We define an array as non-decreasing if
 * nums[i] <= nums[i + 1] holds for every i (0-based) such that (0 <= i <= n - 2).
 *
 * Example:
 * Input: nums = [4,2,3]
 * Output: true
 * Explanation: You could modify the first 4 to 1 to get a non-decreasing array [1,2,3].
 *
 * Input: nums = [4,2,1]
 * Output: false
 * Explanation: You cannot get a non-decreasing array by modifying at most one element.
 *
 * LeetCode Link: https://leetcode.com/problems/non-decreasing-array/
 *
 * Follow-up Questions:
 * 1. What if we can modify at most k elements instead of just one?
 *    Answer: Use DP or greedy with modification counter, checking feasibility at each step.
 * 2. What if we want to find the minimum number of modifications needed?
 *    Answer: Count all violations and use greedy strategy to fix maximum violations with minimum changes.
 * 3. How would you handle the case where we want strictly increasing array?
 *    Answer: Change condition from nums[i] <= nums[i+1] to nums[i] < nums[i+1].
 * 4. What if the array can be rotated before checking?
 *    Answer: Try all possible rotation points and check each rotated version.
 *
 * Related Problems:
 * - 2289. Steps to Make Array Non-decreasing: https://leetcode.com/problems/steps-to-make-array-non-decreasing/
 * - 2263. Make Array Non-decreasing or Non-increasing
 * - 300. Longest Increasing Subsequence
 */
public class NonDecreasingArray {

    /**
     * Checks if array can be made non-decreasing with at most one modification.
     *
     * Algorithm:
     * 1. Scan array to find violations (nums[i] > nums[i+1])
     * 2. If more than one violation found, return false
     * 3. If one violation found, check if we can fix it by:
     *    a) Modifying nums[i] to be ≤ nums[i+1], or
     *    b) Modifying nums[i+1] to be ≥ nums[i]
     * 4. Consider constraints from neighboring elements
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums input array
     * @return true if can be made non-decreasing with ≤1 modification
     */
    public boolean checkPossibility(int[] nums) {
        int size = nums.length;
        int violations = 0;

        for (int i = 0; i < size - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                violations++;

                // More than one violation - impossible
                if (violations > 1) return false;

                // Try to fix the violation
                // Option 1: Modify nums[i] to be <= nums[i+1]
                // Check if this doesn't violate constraint with nums[i-1]
                if (i == 0 || nums[i - 1] <= nums[i + 1]) {
                    nums[i] = nums[i + 1]; // Modify nums[i]
                } else {
                    // Option 2: Modify nums[i+1] to be >= nums[i]
                    // Check if this doesn't violate constraint with nums[i+2]
                    if (i + 2 >= size || nums[i] <= nums[i + 2]) {
                        nums[i + 1] = nums[i]; // Modify nums[i+1]
                    } else {
                        return false; // Can't fix this violation
                    }
                }
            }
        }

        return true;
    }

    /**
     * Alternative approach without modifying input array
     * Algorithm:
     * 1. Identify the index of the first violation
     * 2. Check if modifying either nums[i] or nums[i+1] can
     *  fix the violation without causing new violations
     * 3. Return true if fixable, else false
     *
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public boolean checkPossibilityNoModify(int[] nums) {
        int size = nums.length;
        int violations = 0;
        int violationIndex = -1;

        // Find violations
        for (int i = 0; i < size - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                violations++;
                if (violations > 1) return false;
                violationIndex = i;
            }
        }

        // No violations found
        if (violations == 0) return true;

        // Check if we can fix the single violation
        int i = violationIndex;

        // Can we modify nums[i] to fix violation? If yes, we can make it the same as nums[i+1]
        boolean canModifyLeft = (i == 0) || (nums[i - 1] <= nums[i + 1]);

        // Can we modify nums[i+1] to fix violation? If yes, we can make nums[i+1] the same as nums[i]
        boolean canModifyRight = (i + 2 >= size) || (nums[i] <= nums[i + 2]);

        return canModifyLeft || canModifyRight;
    }
}
