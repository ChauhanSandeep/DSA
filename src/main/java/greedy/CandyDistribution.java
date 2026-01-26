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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class CandyDistribution {

    public static void main(String[] args) {
        int[] ratings = {-255, 369, 319, 77, 128, -202, -147, 282, -26, -489, -443};
        System.out.println(new CandyDistribution().minimumCandyRequired(ratings));
    }

    /**
     * Main method: Two-pass greedy algorithm (Optimal for clarity).
     * Step-by-step:
     *  1. Initialize candy array with 1 for each child (minimum requirement)
     *  2. Left-to-right pass:
     *     - If current child's rating > left neighbor's rating
     *     - Give current child: leftNeighborCandies + 1
     *     - This ensures children with higher ratings than left neighbor get more
     *  3. Right-to-left pass:
     *     - If current child's rating > right neighbor's rating
     *     - Update current child: max(current, rightNeighborCandies + 1)
     *     - This ensures children with higher ratings than right neighbor get more
     *     - Use max() to satisfy BOTH left and right constraints
     *  4. Sum all candies and return
     *
     * Key Insight:
     * We can't determine candy count in one pass because a child must satisfy
     * both left AND right neighbor constraints. Two passes ensure we capture
     * dependencies from both directions. Taking max() ensures both constraints met.
     *
     * Algorithm: Two-pass Greedy with max aggregation.
     * Time Complexity: O(n), two linear passes through array.
     * Space Complexity: O(n) for candy array.
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

    /**
     * Alternative method: Single-pass with slope counting (Space optimized O(1)).
     * Step-by-step:
     *  1. Track ascending and descending slopes (runs of increasing/decreasing ratings)
     *  2. For ascending slope: candies = 1 + 2 + 3 + ... + upCount
     *  3. For descending slope: candies = 1 + 2 + 3 + ... + downCount
     *  4. At peak (top of ascending slope before descending):
     *     - Ensure peak gets max(upCount, downCount) + 1 candies
     *     - This ensures peak satisfies both up and down neighbors
     *  5. Handle equal ratings by resetting to baseline
     *
     * Key Insight:
     * Ascending/descending runs have predictable candy patterns (1,2,3...).
     * We can count candies arithmetically without storing array. Peak handling
     * is critical: must give enough to satisfy both slopes.
     *
     * Algorithm: Single-pass with slope arithmetic.
     * Time Complexity: O(n), single pass through array.
     * Space Complexity: O(1), only counters used.
     */
    public int candyOptimized(int[] ratings) {
        int length = ratings.length;
        if (length == 0) return 0;
        
        int totalCandies = 1;  // First child gets 1 candy
        int upSlope = 0;       // Length of current ascending slope
        int downSlope = 0;     // Length of current descending slope
        int peak = 0;          // Height of last peak
        
        for (int i = 1; i < length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                // Ascending: rating increasing
                upSlope++;
                downSlope = 0;
                peak = upSlope;
                totalCandies += upSlope + 1;
                
            } else if (ratings[i] < ratings[i - 1]) {
                // Descending: rating decreasing
                upSlope = 0;
                downSlope++;
                
                // Add candies for descending slope
                totalCandies += downSlope;
                
                // If down slope exceeds peak, add 1 to compensate
                // Peak must be higher than both up and down slopes
                if (downSlope > peak) {
                    totalCandies++;
                }
                
            } else {
                // Equal ratings: reset slopes
                upSlope = 0;
                downSlope = 0;
                peak = 0;
                totalCandies++;  // New child gets baseline 1 candy
            }
        }
        
        return totalCandies;
    }
}
