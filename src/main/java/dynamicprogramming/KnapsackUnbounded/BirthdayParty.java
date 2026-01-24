package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;


/**
 * Problem Statement:
 * Tushar's Birthday Party
 *
 * You are given:
 * - `eatingCapacities[]`: Array of friends' eating capacities.
 * - `dishCapacities[]`: Array of dish filling capacities.
 * - `dishCosts[]`: Array of corresponding dish costs.
 *
 * A friend is satisfied if the sum of dish capacities they consume exactly equals their eating capacity.
 * Each dish can be used unlimited times (unbounded). The goal is to minimize the total cost of satisfying all friends.
 *
 * Constraints:
 * - Each friend can be satisfied independently.
 * - Dish capacities and costs are positive integers.
 *
 * Example:
 * eatingCapacities = [2, 4]
 * dishCapacities   = [1, 2]
 * dishCosts        = [3, 5]
 * Output: 11
 * Explanation: For friend 1 → use one dish of capacity 2 (cost = 5),
 *              For friend 2 → use two dishes of capacity 2 (cost = 5 + 5 = 10)
 *              Total = 5 + 10 = 15. But there could be a cheaper combination with (1+1) etc.
 *
 * InterviewBit Link:
 * https://www.interviewbit.com/problems/tushars-birthday-party/
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. What if some dishes are only allowed a limited number of times?
 *    → Then it becomes a **Bounded Knapsack** problem.
 *
 * 2. What if each dish has a satisfaction score and we need to maximize it within budget?
 *    → Turns into a **Knapsack Maximization** variant with cost as weight.
 *
 * 3. What if some friends are vegetarians and can’t eat certain dishes?
 *    → Add filtering logic per friend based on dietary constraints.
 */
public class BirthdayParty {

  /**
   * Computes the minimum total cost to satisfy all friends.
   * For each friend, we calculate the least cost to fill their eating capacity using any number of dishes.
   *
   * Time Complexity: O(F * C * D)
   *   - F = number of friends
   *   - C = maximum eating capacity
   *   - D = number of dishes
   *
   * Space Complexity: O(C) for the DP array
   *
   * @param eatingCapacities Array of each friend's eating capacity
   * @param dishCapacities   Capacity (filling value) of each dish
   * @param dishCosts        Cost of each dish
   * @return Minimum total cost to satisfy all friends
   */
  public int findMinimumCost(int[] eatingCapacities, int[] dishCapacities, int[] dishCosts) {
    // Find the maximum capacity among all friends to build one DP table up to that limit
    int maxCapacity = Arrays.stream(eatingCapacities).max().orElse(0);

    // Precompute minimum cost to fill every capacity up to maxCapacity using unbounded knapsack
    int[] minCostForCapacity = computeMinCostForAllCapacities(maxCapacity, dishCapacities, dishCosts);

    // Sum the cost for each friend's required capacity
    int totalMinCost = 0;
    for (int capacity : eatingCapacities) {
      totalMinCost += minCostForCapacity[capacity];
    }

    return totalMinCost;
  }

  /**
   * Computes the minimum cost to exactly reach every capacity up to maxCapacity.
   * Uses bottom-up DP approach (Unbounded Knapsack variation).
   *
   * Time Complexity: O(C * D)
   *   - C = maxCapacity
   *   - D = number of dishes
   *
   * @param maxCapacity     The maximum capacity we need to compute for
   * @param dishCapacities  The filling capacity of each dish
   * @param dishCosts       The cost of each dish
   * @return An array where index i stores the minimum cost to reach capacity i
   */
  private int[] computeMinCostForAllCapacities(int maxCapacity, int[] dishCapacities, int[] dishCosts) {
    int[] minCostForCapacity = new int[maxCapacity + 1];
    Arrays.fill(minCostForCapacity, Integer.MAX_VALUE);
    minCostForCapacity[0] = 0; // Base case: 0 cost to fill capacity 0

    for (int capacity = 1; capacity <= maxCapacity; capacity++) {
      for (int i = 0; i < dishCapacities.length; i++) {
        int dishSize = dishCapacities[i];
        int dishCost = dishCosts[i];

        if (capacity >= dishSize && minCostForCapacity[capacity - dishSize] != Integer.MAX_VALUE) {
          minCostForCapacity[capacity] = Math.min(minCostForCapacity[capacity], minCostForCapacity[capacity - dishSize] + dishCost);
        }
      }
    }

    return minCostForCapacity;
  }
}