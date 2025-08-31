package arrays;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Minimum Cost Of Buying Candies With Discount
 *
 * Problem: Buy all candies with minimum cost. For every 2 candies bought at full price,
 * get 1 candy free (cheapest among the 3).
 *
 * Example: cost = [1,2,3] -> Output: 5
 * Buy candies costing 2 and 3, get candy costing 1 for free. Total = 2 + 3 = 5.
 *
 * LeetCode: https://leetcode.com/problems/minimum-cost-of-buying-candies-with-discount
 *
 * Follow-up Questions:
 * - What if discount is k candies for every n bought? (Generalize the grouping logic)
 * - How to maximize savings instead of minimize cost? (Choose groups optimally)
 * - What if candies have different discount rates? (Use dynamic programming)
 */
public class MinimumCostOfBuyingCandiesWithDiscount {

    /**
     * Calculates minimum cost to buy all candies with discount.
     *
     * Algorithm:
     * 1. Sort candy costs in descending order (expensive first)
     * 2. Group candies in sets of 3, taking 2 most expensive and getting cheapest free
     * 3. For remaining candies (less than 3), buy all at full price
     * 4. Sum up all costs excluding free candies
     *
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(1) if sorting in-place, O(n) for Arrays.sort()
     *
     * @param cost array of candy costs
     * @return minimum cost to buy all candies
     */
    public int minimumCostAlternative(int[] cost) {
        // Convert to Integer array for reverse sorting
        Integer[] sortedCost = Arrays.stream(cost).boxed().toArray(Integer[]::new);
        Arrays.sort(sortedCost, Collections.reverseOrder());

        int totalCost = 0;

        for (int i = 0; i < sortedCost.length; i++) {
            // Skip every 3rd element (0-indexed: skip indices 2, 5, 8, ...)
            if (i % 3 != 2) {
                totalCost += sortedCost[i];
            }
        }

        return totalCost;
    }

    /**
     * Mathematical approach with cleaner logic
     * Time Complexity: O(n log n),
     * Space Complexity: O(1)
     */
    public int minimumCostMath(int[] cost) {
        Arrays.sort(cost);

        int totalCost = 0;
        int length = cost.length;

        // Calculate from expensive to cheap, counting backwards
        for (int i = length - 1; i >= 0; i--) {
            // Position from the end (0, 1, 2, 3, 4, 5, ...)
            int positionFromEnd = length - 1 - i;

            // Every 3rd candy from expensive end is free
            if (positionFromEnd % 3 != 2) {
                totalCost += cost[i];
            }
        }

        return totalCost;
    }
}
