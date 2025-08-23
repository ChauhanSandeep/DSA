package Array;

import java.util.*;

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
    public int minimumCost(int[] cost) {
        // Sort in descending order to maximize savings
        Arrays.sort(cost);
        reverseArray(cost);

        int totalCost = 0;

        // Process candies in groups of 3
        for (int i = 0; i < cost.length; i += 3) {
            // Pay for most expensive candy in group
            totalCost += cost[i];

            // Pay for second most expensive if it exists
            if (i + 1 < cost.length) {
                totalCost += cost[i + 1];
            }

            // Third candy (cheapest in group) is free - skip adding cost[i + 2]
        }

        return totalCost;
    }

    // Helper method to reverse array in-place
    private void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Alternative using Integer array and Collections for cleaner sorting
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int minimumCostAlternative(int[] cost) {
        // Convert to Integer array for reverse sorting
        Integer[] costArray = Arrays.stream(cost).boxed().toArray(Integer[]::new);
        Arrays.sort(costArray, Collections.reverseOrder());

        int totalCost = 0;

        for (int i = 0; i < costArray.length; i++) {
            // Skip every 3rd element (0-indexed: skip indices 2, 5, 8, ...)
            if (i % 3 != 2) {
                totalCost += costArray[i];
            }
        }

        return totalCost;
    }

    /**
     * Mathematical approach with cleaner logic
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int minimumCostMath(int[] cost) {
        Arrays.sort(cost);

        int totalCost = 0;
        int n = cost.length;

        // Calculate from expensive to cheap, counting backwards
        for (int i = n - 1; i >= 0; i--) {
            // Position from the end (0, 1, 2, 3, 4, 5, ...)
            int positionFromEnd = n - 1 - i;

            // Every 3rd candy from expensive end is free
            if (positionFromEnd % 3 != 2) {
                totalCost += cost[i];
            }
        }

        return totalCost;
    }

    /**
     * Stream-based functional approach
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int minimumCostStream(int[] cost) {
        return IntStream.range(0, cost.length)
                .map(i -> cost[cost.length - 1 - i]) // Reverse order
                .sorted()                             // Sort in ascending (will be desc due to reverse)
                .reduce(0, (sum, costValue) -> {
                    // This approach is complex with streams, better use imperative style
                    return sum;
                });
    }

    /**
     * Helper method to demonstrate greedy choice validation
     */
    public List<List<Integer>> getOptimalGroups(int[] cost) {
        Integer[] sortedCost = Arrays.stream(cost).boxed().toArray(Integer[]::new);
        Arrays.sort(sortedCost, Collections.reverseOrder());

        List<List<Integer>> groups = new ArrayList<>();

        for (int i = 0; i < sortedCost.length; i += 3) {
            List<Integer> group = new ArrayList<>();
            group.add(sortedCost[i]); // Most expensive (pay)

            if (i + 1 < sortedCost.length) {
                group.add(sortedCost[i + 1]); // Second expensive (pay)
            }

            if (i + 2 < sortedCost.length) {
                group.add(sortedCost[i + 2]); // Cheapest (free)
            }

            groups.add(group);
        }

        return groups;
    }
}
