package arrays;

import java.util.*;

/**
 * All Divisions With The Highest Score Of A Binary Array
 *
 * Problem: Given a binary array nums, find all indices that yield the highest division score.
 * Division score at index i = count of 0s in left subarray + count of 1s in right subarray.
 *
 * Example: nums = [0,0,1,0] -> Output: [2,4]
 * At index 2: left=[0,0] has 2 zeros, right=[1,0] has 1 one, score = 3
 * At index 4: left=[0,0,1,0] has 3 zeros, right=[] has 0 ones, score = 3
 *
 * LeetCode: https://leetcode.com/problems/all-divisions-with-the-highest-score-of-a-binary-array
 *
 * Follow-up Questions:
 * - How to handle if we need to find minimum score instead? (Track minimum instead of maximum)
 * - What if array contains other numbers? (Generalize the scoring function)
 * - Can we solve in less than O(n) time? (No, need to examine each position)
 */
public class AllDivisionsWithTheHighestScoreOfABinaryArray {

    /**
     * Finds all division indices with the highest possible score.
     *
     * Algorithm:
     * 1. Track zeros in left part and ones in right part
     * 2. Initially: leftZeros = 0, rightOnes = total ones count
     * 3. For each position, calculate score = leftZeros + rightOnes
     * 4. Update counters based on current element
     * 5. Track maximum score and collect all indices achieving it
     *
     * Time Complexity: O(n) where n is length of array
     * Space Complexity: O(k) where k is number of indices with max score
     *
     * @param nums binary array
     * @return list of indices with highest division score
     */
    public List<Integer> maxScoreIndices(int[] nums) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;

        // Count total ones in the array
        int totalOnes = 0;
        for (int num : nums) {
            if (num == 1) totalOnes++;
        }

        int leftZeros = 0;
        int rightOnes = totalOnes;
        int maxScore = rightOnes; // Initial score at index 0
        result.add(0);

        // Process each position from 0 to n-1
        for (int i = 0; i < n; i++) {
            // Update counters based on current element
            if (nums[i] == 0) {
                leftZeros++;
            } else {
                rightOnes--;
            }

            int currentScore = leftZeros + rightOnes;

            // Update result based on current score
            if (currentScore > maxScore) {
                maxScore = currentScore;
                result.clear();
                result.add(i + 1);
            } else if (currentScore == maxScore) {
                result.add(i + 1);
            }
        }

        return result;
    }

    /**
     * Brute force approach for verification (less efficient)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public List<Integer> maxScoreIndicesBruteForce(int[] nums) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;
        int maxScore = Integer.MIN_VALUE;

        // Check all possible division points
        for (int i = 0; i <= n; i++) {
            int leftZeros = 0, rightOnes = 0;

            // Count zeros in left part
            for (int j = 0; j < i; j++) {
                if (nums[j] == 0) leftZeros++;
            }

            // Count ones in right part
            for (int j = i; j < n; j++) {
                if (nums[j] == 1) rightOnes++;
            }

            int score = leftZeros + rightOnes;
            if (score > maxScore) {
                maxScore = score;
                result.clear();
                result.add(i);
            } else if (score == maxScore) {
                result.add(i);
            }
        }

        return result;
    }
}
