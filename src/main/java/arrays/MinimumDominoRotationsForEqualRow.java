package arrays;

/**
 * Minimum Domino Rotations For Equal Row
 *
 * Problem: Given two rows of dominoes, find minimum rotations to make all dominoes
 * in one row have the same value. Return -1 if impossible.
 *
 * Example: tops = [2,1,2,4,2,2], bottoms = [5,2,6,2,3,2] -> Output: 2
 * Can make all tops = 2 with 2 rotations, or all bottoms = 2 with 3 rotations.
 *
 * LeetCode: https://leetcode.com/problems/minimum-domino-rotations-for-equal-row
 *
 * Follow-up Questions:
 * - What if we have more than 2 rows? (Check all possible target values across all rows)
 * - How to handle case with multiple valid solutions? (Return minimum among all)
 * - What if rotation cost varies? (Use dynamic programming with costs)
 */
public class MinimumDominoRotationsForEqualRow {

    /**
     * Finds minimum rotations to make one row uniform.
     *
     * Algorithm:
     * 1. Only values from first domino can make entire row uniform
     * 2. Check if tops[0] can make all tops uniform or all bottoms uniform
     * 3. Check if bottoms[0] can make all tops uniform or all bottoms uniform
     * 4. For each candidate, count rotations needed and verify feasibility
     * 5. Return minimum rotations among all valid possibilities
     *
     * Time Complexity: O(n) where n is number of dominoes
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param tops top values of dominoes
     * @param bottoms bottom values of dominoes
     * @return minimum rotations needed, or -1 if impossible
     */
    public int minDominoRotations(int[] tops, int[] bottoms) {
        int n = tops.length;

        // Check if we can make all tops equal to tops[0]
        int rotationsTopToTop = getRotations(tops, bottoms, tops[0], true);

        // Check if we can make all bottoms equal to tops[0]
        int rotationsTopToBottom = getRotations(tops, bottoms, tops[0], false);

        // Check if we can make all tops equal to bottoms[0]
        int rotationsBottomToTop = getRotations(tops, bottoms, bottoms[0], true);

        // Check if we can make all bottoms equal to bottoms[0]
        int rotationsBottomToBottom = getRotations(tops, bottoms, bottoms[0], false);

        // Find minimum among valid options
        int minRotations = Integer.MAX_VALUE;

        if (rotationsTopToTop != -1) minRotations = Math.min(minRotations, rotationsTopToTop);
        if (rotationsTopToBottom != -1) minRotations = Math.min(minRotations, rotationsTopToBottom);
        if (rotationsBottomToTop != -1) minRotations = Math.min(minRotations, rotationsBottomToTop);
        if (rotationsBottomToBottom != -1) minRotations = Math.min(minRotations, rotationsBottomToBottom);

        return minRotations == Integer.MAX_VALUE ? -1 : minRotations;
    }

    /**
     * Helper method to calculate rotations needed for a specific target and row
     *
     * @param tops top values
     * @param bottoms bottom values
     * @param target target value to achieve uniformity
     * @param targetTop true if making top row uniform, false for bottom row
     * @return number of rotations needed, or -1 if impossible
     */
    private int getRotations(int[] tops, int[] bottoms, int target, boolean targetTop) {
        int rotations = 0;

        for (int i = 0; i < tops.length; i++) {
            if (targetTop) {
                // Want all tops to be target
                if (tops[i] == target) {
                    // Already correct, no rotation needed
                    continue;
                } else if (bottoms[i] == target) {
                    // Can rotate to get target on top
                    rotations++;
                } else {
                    // Neither top nor bottom has target - impossible
                    return -1;
                }
            } else {
                // Want all bottoms to be target
                if (bottoms[i] == target) {
                    // Already correct, no rotation needed
                    continue;
                } else if (tops[i] == target) {
                    // Can rotate to get target on bottom
                    rotations++;
                } else {
                    // Neither top nor bottom has target - impossible
                    return -1;
                }
            }
        }

        return rotations;
    }

    /**
     * Optimized approach checking only necessary candidates
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int minDominoRotationsOptimized(int[] tops, int[] bottoms) {
        // Only two possible candidates: tops[0] and bottoms[0]
        int candidate1 = tops[0];
        int candidate2 = bottoms[0];

        int result = Integer.MAX_VALUE;

        // Check candidate1
        int rotations1Top = checkCandidate(tops, bottoms, candidate1, true);
        int rotations1Bottom = checkCandidate(tops, bottoms, candidate1, false);

        if (rotations1Top != -1) result = Math.min(result, rotations1Top);
        if (rotations1Bottom != -1) result = Math.min(result, rotations1Bottom);

        // Check candidate2 (only if different from candidate1)
        if (candidate2 != candidate1) {
            int rotations2Top = checkCandidate(tops, bottoms, candidate2, true);
            int rotations2Bottom = checkCandidate(tops, bottoms, candidate2, false);

            if (rotations2Top != -1) result = Math.min(result, rotations2Top);
            if (rotations2Bottom != -1) result = Math.min(result, rotations2Bottom);
        }

        return result == Integer.MAX_VALUE ? -1 : result;
    }

    // Helper method for optimized approach
    private int checkCandidate(int[] tops, int[] bottoms, int target, boolean makeTopUniform) {
        int rotations = 0;

        for (int i = 0; i < tops.length; i++) {
            boolean hasTarget = (tops[i] == target) || (bottoms[i] == target);
            if (!hasTarget) return -1; // Impossible

            if (makeTopUniform) {
                if (tops[i] != target) rotations++;
            } else {
                if (bottoms[i] != target) rotations++;
            }
        }

        return rotations;
    }
}
