package arrays.greedy;

import java.util.Arrays;

/**
 * Problem: Non-decreasing Array
 *
 * Decide whether an integer array can become non-decreasing by modifying at most
 * one element. A valid final array must satisfy nums[i] <= nums[i + 1] for every
 * adjacent pair.
 *
 * Leetcode: https://leetcode.com/problems/non-decreasing-array/ (Medium)
 * Rating:   acceptance 24.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Local violation repair
 *
 * Example:
 *   Input:  nums = [4,2,3]
 *   Output: true
 *   Why:    changing 4 down to 2 gives [2,2,3], which is non-decreasing.
 *
 * Follow-ups:
 *   1. What if up to k modifications are allowed?
 *      Count and repair violations greedily, or use DP when choices interact.
 *   2. What if the array must be strictly increasing?
 *      Replace <= with < and account for integer gaps between neighbors.
 *   3. Return the modified array?
 *      Apply the same local repair and keep the changed value.
 *
 * Related: Steps to Make Array Non-decreasing (2289), Longest Increasing Subsequence (300).
 */
public class NonDecreasingArray {

    public static void main(String[] args) {
        NonDecreasingArray solver = new NonDecreasingArray();
        int[][] inputs = {{4, 2, 3}, {4, 2, 1}, {1}};
        boolean[] expected = {true, false, true};

        for (int i = 0; i < inputs.length; i++) {
            int[] input = inputs[i].clone();
            boolean got = solver.checkPossibility(input);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: one drop nums[i] > nums[i + 1] is the only place that may need a
     * modification. Prefer lowering nums[i] when the left neighbor allows it;
     * otherwise raise nums[i + 1] if the right neighbor allows it. A second drop
     * means one change cannot fix the whole array.
     *
     * Algorithm:
     *   1. Scan adjacent pairs and count violations.
     *   2. On the first violation, try the local modification that preserves neighbors.
     *   3. Return false for a second violation or an unfixable local conflict.
     *
     * Time:  O(n) - one pass over adjacent pairs.
     * Space: O(1) - only the violation count is stored.
     *
     * @param nums array to check, modified in place while testing feasibility
     * @return true if at most one modification can make nums non-decreasing
     */
    public boolean checkPossibility(int[] nums) {
        int size = nums.length;
        int violations = 0;

        for (int i = 0; i < size - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                violations++;

                // More than one violation - impossible
                if (violations > 1) return false;

                // Option 1: Decrease nums[i] to become nums[i+1], if nums[i-1] <= nums[i+1]
                if (i == 0 || nums[i - 1] <= nums[i + 1]) {
                    nums[i] = nums[i + 1]; // Modify nums[i]
                } else {
                    // Option 2: Increase nums[i+1] to become nums[i+2], if nums[i] <= nums[i + 2]
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

        // Decrease nums[i] to make same as nums[i+1]. If nums[i-1] <= nums[i+1]
        // Example 1 (i-1), 4(i), 3(i+1) => 1, 3, 3
        boolean canModifyLeft = (i == 0) || (nums[i - 1] <= nums[i + 1]);

        // Increase nums[i+1] to make same as nums[i]. If nums[i] <= nums[i+2]
        // 1(i-1), 3(i), 1(i+1), 4(i+2) => 1, 3, 3, 4
        boolean canModifyRight = (i + 2 >= size) || (nums[i] <= nums[i + 2]);

        return canModifyLeft || canModifyRight;
    }
}
