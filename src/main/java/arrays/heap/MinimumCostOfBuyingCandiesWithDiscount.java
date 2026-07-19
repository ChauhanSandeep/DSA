package arrays.heap;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Problem: Minimum Cost of Buying Candies With Discount
 *
 * A shop lets you buy two candies and take a third candy for free, as long as
 * the free candy costs no more than the cheaper paid candy. Given all candy costs,
 * return the minimum total amount needed to take every candy home.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-of-buying-candies-with-discount/
 * Rating:   1261 (zerotrac Elo, Q1, biweekly-contest-70)
 * Pattern:  Arrays | Greedy | Sort descending and skip every third
 *
 * Example:
 *   Input:  [6,5,7,9,2,2]
 *   Output: 23
 *   Why:    sorted descending gives 9,7,6,5,2,2; pay for 9,7, skip 6, pay for
 *           5,2, skip 2, so the paid total is 23.
 *
 * Follow-ups:
 *   1. What if the offer is buy k get one free?
 *      Sort descending and skip every (k + 1)-th candy.
 *   2. What if the free candy must be strictly cheaper?
 *      Greedy grouping needs extra checks and may require matching with a multiset.
 *   3. What if you need to output paid and free candy indices?
 *      Sort cost-index pairs and collect skipped positions as free candies.
 */
public class MinimumCostOfBuyingCandiesWithDiscount {

    public static void main(String[] args) {
        MinimumCostOfBuyingCandiesWithDiscount solver = new MinimumCostOfBuyingCandiesWithDiscount();
        int[][] inputs = {{1, 2, 3}, {6, 5, 7, 9, 2, 2}, {5}};
        int[] expected = {5, 23, 5};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minimumCostAlternative(inputs[i]);
            System.out.printf("cost=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /** Backward-compatible wrapper for the canonical greedy solution. */
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
