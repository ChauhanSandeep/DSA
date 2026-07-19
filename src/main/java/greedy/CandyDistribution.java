package greedy;

import java.util.Arrays;

/**
 * Problem: Candy Distribution
 *
 * Children stand in a line, each with a rating. Give every child at least one
 * candy, and any child with a higher rating than an adjacent child must get more
 * candies than that neighbor. Return the minimum total candies.
 *
 * Leetcode: https://leetcode.com/problems/candy/ (Hard)
 * Rating:   acceptance 48.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Local neighbor rules | Two passes and slope counting
 *
 * Example:
 *   Input:  ratings = [1,0,2]
 *   Output: 5
 *   Why:    candies [2,1,2] is the smallest assignment where both rating-1
 *           children beat their rating-0 neighbor.
 *
 * Follow-ups:
 *   1. Can this be solved in O(1) extra space?
 *      Count rising and falling slopes and compensate when the falling slope outgrows its peak.
 *   2. What if equal ratings must receive equal candies?
 *      Group equal runs first, then apply the two-pass rule between groups.
 *   3. What if the line is circular?
 *      Start from a global minimum rating to break the circle, then solve the line.
 *   4. What if each child has a candy cap?
 *      Compute the minimum assignment, then validate whether every value fits under the cap.
 *
 * Related: Candy (135), Gas Station (134).
 */
public class CandyDistribution {

    public static void main(String[] args) {
        CandyDistribution solver = new CandyDistribution();
        int[][] inputs = { {}, {1, 0, 2}, {1, 2, 2}, {-255, 369, 319, 77, 128} };
        int[] expected = {0, 5, 4, 9};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minimumCandyRequired(inputs[i]);
            System.out.printf("ratings=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: trying to satisfy both neighbors in one pass is brittle because
     * a later right-neighbor constraint can invalidate an earlier choice. Split
     * the rules into two one-sided lower bounds. The left-to-right pass gives the
     * cheapest candies that satisfy every left edge; the right-to-left pass adds
     * only what is needed for right edges. Taking max keeps both constraints true
     * without adding unnecessary candy.
     *
     * Algorithm:
     *   1. Give every child one candy.
     *   2. Scan left to right and raise candies on increasing rating edges.
     *   3. Scan right to left and raise candies on decreasing rating edges with max.
     *   4. Sum the final candy counts.
     *
     * Time:  O(n) - two linear scans touch each rating a constant number of times.
     * Space: O(n) - one candy count is stored per child.
     *
     * @param ratings child ratings in line order
     * @return minimum candies needed to satisfy the neighbor rules
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
     * Intuition: the two-pass array is really measuring slopes. A rising run like
     * 1 < 2 < 3 needs a candy staircase, and a falling run needs the same
     * staircase from the other side. Counting active up and down slopes lets us
     * add those staircase costs directly. The only shared point is the peak, so
     * when the falling slope grows taller than the old peak, one extra candy is
     * added to keep the peak higher than both sides.
     *
     * Algorithm:
     *   1. Track current ascending slope, descending slope, and peak height.
     *   2. Add a growing staircase cost for ascending ratings.
     *   3. Add descending slope cost, plus one when the down slope exceeds the peak.
     *   4. Reset slopes and add one candy on equal ratings.
     *
     * Time:  O(n) - one scan processes each adjacent rating pair once.
     * Space: O(1) - only slope counters and the total are stored.
     *
     * @param ratings child ratings in line order
     * @return minimum candies needed to satisfy the neighbor rules
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
