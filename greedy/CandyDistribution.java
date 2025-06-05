package greedy;

import java.util.Arrays;

/**
 * LeetCode 135: Candy Distribution
 * Problem Link: https://leetcode.com/problems/candy/
 *
 * Given an array representing student ratings, distribute the minimum number of candies
 * such that:
 * 1. Each child gets at least one candy.
 * 2. Higher-rated children get more candies than their adjacent lower-rated ones.
 *
 * For example
 * Input: [1, 0, 2]
 * Output: 5
 * Explanation: 2 candies for the first child, 1 candy for the second child, and 2 candies for the third child.
 *
 */
public class CandyDistribution {

    public static void main(String[] args) {
        int[] ratings = {-255, 369, 319, 77, 128, -202, -147, 282, -26, -489, -443};
        System.out.println(new CandyDistribution().minimumCandyRequired(ratings));
    }

    /**
     * Calculates the minimum candies needed to satisfy the problem constraints.
     *
     * Approach:
     * - **Left-to-Right Pass**: Ensure children with higher ratings than the previous child get more candies.
     * - **Right-to-Left Pass**: Ensure children with higher ratings than the next child get more candies.
     * - Use a **candies array** initialized to 1 for all children.
     * - **Sum up the candy counts** after adjusting in both passes.
     *
     * This guarantees solution because:
     * - Every child has at least 1 candy from the start.
     * - Every local peak in ratings gets more candies than both neighbors.
     * - Every increasing or decreasing sequence is respected by at least one pass.
     * - By using Math.max(...) in the second pass, we never violate the result of the first pass.
     *
     * Time Complexity: O(N) - We traverse the ratings array twice.
     * Space Complexity: O(N) - We use an extra array for candy distribution.
     *
     * @param ratings An array representing student ratings.
     * @return The minimum number of candies required.
     */
    public int minimumCandyRequired(int[] ratings) {
        if (ratings == null || ratings.length == 0) return 0;

        int size = ratings.length;
        int[] candies = new int[size];
        Arrays.fill(candies, 1); // Every child gets at least one candy

        // Left to Right Pass: Ensure increasing ratings get more candies
        for (int i = 1; i < size; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // Right to Left Pass: Ensure decreasing ratings get more candies
        int totalCandies = candies[size - 1]; // Initialize with last child's candy
        for (int i = size - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
            totalCandies += candies[i]; // Accumulate total candies
        }

        return totalCandies;
    }
}
