package arrays.heap;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Problem: Minimum Cost of Buying Candies With Discount
 * 
 * A shop is selling candies at a discount. For every two candies sold, the shop
 * gives a third candy for free. The customer can choose any candy to take away
 * for free as long as the cost of the chosen candy is less than or equal to the
 * minimum cost of the two candies bought.
 * 
 * Given a 0-indexed integer array cost, where cost[i] denotes the cost of the
 * ith candy, return the minimum cost of buying all the candies.
 * 
 * Example:
 * Input: cost = [1,2,3]
 * Output: 5
 * Explanation: We buy candies with costs 2 and 3, and take candy with cost 1 for free.
 * Total cost = 2 + 3 = 5. This is the only way to buy all candies.
 * 
 * Constraints:
 * - 1 <= cost.length <= 100
 * - 1 <= cost[i] <= 100
 * 
 * LeetCode Problem: https://leetcode.com/problems/minimum-cost-of-buying-candies-with-discount
 * 
 * Follow-up Questions:
 * 
 * 1. What if the promotion changes to "buy 3 get 1 free" or "buy k get 1 free"?
 *    Answer: Generalize the solution by sorting in descending order and skipping
 *    every (k+1)th candy. The algorithm structure remains the same but the step
 *    size changes from 3 to (k+1).
 * 
 * 2. How would you handle if each candy has a different discount multiplier?
 *    Answer: Add a weight/multiplier to each candy. Sort by (cost * multiplier)
 *    in descending order. Apply greedy strategy considering weighted costs for
 *    determining which candies to get free.
 * 
 * 3. What if you can only afford a budget B, what's the maximum candies you can get?
 *    Answer: Use dynamic programming or greedy with budget constraint. Track
 *    remaining budget, prioritize expensive candies to maximize free ones, stop
 *    when budget exhausted. Return count of candies obtained.
 * 
 * 4. How would you modify if free candy must be strictly less than minimum bought?
 *    Answer: Add validation check when selecting free candy. If third candy equals
 *    minimum of first two, skip it and try next cheaper option. May need additional
 *    logic to handle edge cases.
 * 
 * 5. What if you need to return the actual candies bought vs free, not just cost?
 *    Answer: Store candy indices or values during sorting. Track which candies are
 *    paid vs free based on position (every 3rd is free). Return two lists: bought
 *    and free candies.
 * LeetCode Contest Rating: 1261
 */
public class MinimumCostOfBuyingCandiesWithDiscount {

    /**
     * Calculates minimum cost to buy all candies with discount.
     *
     * Key insight: To minimize total cost, maximize value of free candies. Get
     * the most expensive candies as freebies by buying even more expensive ones.
     * Sorting ensures we always pay for expensive candies and get cheaper ones free.
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
        
        // Process in groups of 3: pay for first 2, get 3rd free
        for (int i = 0; i < sortedCost.length; i++) {
            // Skip every 3rd candy (0-indexed: positions 2, 5, 8, ...)
            if ((i + 1) % 3 != 0) {
                totalCost += sortedCost[i];
            }
        }

        return totalCost;
    }
}
