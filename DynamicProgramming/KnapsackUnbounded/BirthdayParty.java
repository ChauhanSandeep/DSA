package DynamicProgramming.KnapsackUnbounded;

import java.util.Arrays;

/**
 * Problem: Tushar's Birthday Party
 *
 * Given:
 * - `eatingCapacities[]`: Eating capacity of each friend.
 * - `dishCapacities[]`: Capacity (filling value) of each dish.
 * - `dishCosts[]`: Cost of each dish.
 *
 * A friend is satisfied if the sum of dish capacities they consume equals their eating capacity.
 * The goal is to find the minimum total cost required to satisfy all friends.
 *
 * Approach:
 * - This is a variation of the Unbounded Knapsack problem.
 * - For each friend, compute the minimum cost required to exactly reach their eating capacity.
 * - Use Dynamic Programming (Bottom-Up) to precompute the minimum cost for each capacity.
 *
 * Time Complexity: O(N * M), where:
 * - N = maximum eating capacity among friends.
 * - M = number of dish options.
 *
 * Space Complexity: O(N) for the DP array.
 *
 * LeetCode Link: Not available, but similar to Unbounded Knapsack.
 * InterviewBit Link: https://www.interviewbit.com/problems/tushars-birthday-party/
 */
public class BirthdayParty {

    /**
     * Computes the minimum cost to satisfy all friends.
     *
     * @param eatingCapacities Array representing each friend's eating capacity.
     * @param dishCapacities   Array representing the filling capacity of each dish.
     * @param dishCosts        Array representing the cost of each dish.
     * @return Minimum total cost to satisfy all friends.
     */
    public int findMinimumCost(int[] eatingCapacities, int[] dishCapacities, int[] dishCosts) {
        int totalMinCost = 0;

        for (int capacity : eatingCapacities) {
            totalMinCost += computeMinCostForCapacity(capacity, dishCapacities, dishCosts);
        }

        return totalMinCost;
    }

    /**
     * Computes the minimum cost required to exactly reach the given eating capacity.
     * Uses a bottom-up DP approach (Unbounded Knapsack variation).
     *
     * @param capacity        The target capacity to reach.
     * @param dishCapacities  The capacity of each dish.
     * @param dishCosts       The cost of each dish.
     * @return Minimum cost to reach the given capacity, or Integer.MAX_VALUE if not possible.
     */
    private int computeMinCostForCapacity(int capacity, int[] dishCapacities, int[] dishCosts) {
        int[] minCost = new int[capacity + 1];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[0] = 0;

        for (int currentCapacity = 1; currentCapacity <= capacity; currentCapacity++) {
            for (int i = 0; i < dishCapacities.length; i++) {
                if (currentCapacity >= dishCapacities[i] && minCost[currentCapacity - dishCapacities[i]] != Integer.MAX_VALUE) {
                    minCost[currentCapacity] = Math.min(minCost[currentCapacity], dishCosts[i] + minCost[currentCapacity - dishCapacities[i]]);
                }
            }
        }

        return minCost[capacity];
    }
}