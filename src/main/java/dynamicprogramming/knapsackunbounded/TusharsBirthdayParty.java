package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;

/**
 * Problem: Tushar's Birthday Party
 *
 * Each friend has an exact eating capacity. Dishes have capacities and costs, may be ordered unlimited times, and the goal is minimum total cost for all friends.
 *
 * InterviewBit: https://www.interviewbit.com/problems/tushars-birthday-party/
 * Rating:   not available
 * Pattern:  Dynamic programming | Unbounded knapsack | Minimum cost fill
 *
 * Example:
 *   Input:  friends = [2, 4], dishes = [1, 2], costs = [3, 5]
 *   Output: 15
 *   Why:    capacity 2 costs 5 and capacity 4 costs 10, for a total of 15.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 */
public class TusharsBirthdayParty {

    public static void main(String[] args) {
    TusharsBirthdayParty solution = new TusharsBirthdayParty();
    int[][] eatingCases = { {2, 4, 6}, {} };
    int[][] dishCapacityCases = { {1, 2, 3}, {1, 2, 3} };
    int[][] dishCostCases = { {3, 5, 7}, {3, 5, 7} };
    int[] expected = {29, 0};
    for (int i = 0; i < eatingCases.length; i++) {
      int got = solution.findMinimumCost(eatingCases[i], dishCapacityCases[i], dishCostCases[i]);
      System.out.printf("friends=%s dishes=%s costs=%s -> %d  expected=%d%n", Arrays.toString(eatingCases[i]), Arrays.toString(dishCapacityCases[i]), Arrays.toString(dishCostCases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: minCostForCapacity[c] is the cheapest exact fill for capacity c using unlimited dishes. After precomputing through the largest friend capacity, each friend contributes one lookup to the total.
   *
   * Algorithm:
   *   1. Find maxCapacity.
   *   2. Compute minimum cost for every capacity up to maxCapacity.
   *   3. Initialize totalMinCost.
   *   4. Add the precomputed cost for each friend.
   *   5. Return the total.
   *
   * Time:  O(maxCapacity * dishCapacities.length + eatingCapacities.length) - precompute plus lookups.
   * Space: O(maxCapacity) - one cost array.
   *
   * @param eatingCapacities friend capacities
   * @param dishCapacities dish filling values
   * @param dishCosts dish costs
   * @return minimum total cost
   */
public int findMinimumCost(int[] eatingCapacities, int[] dishCapacities, int[] dishCosts) {
    // Find the maximum capacity among all friends to build one DP table up to that
    // limit
    int maxCapacity = Arrays.stream(eatingCapacities).max().orElse(0);

    // Precompute minimum cost to fill every capacity up to maxCapacity using
    // unbounded knapsack
    int[] minCostForCapacity = computeMinCostForAllCapacities(maxCapacity, dishCapacities, dishCosts);

    // Sum the cost for each friend's required capacity
    int totalMinCost = 0;
    for (int capacity : eatingCapacities) {
      totalMinCost += minCostForCapacity[capacity];
    }

    return totalMinCost;
  }

  /**
   * Computes the minimum total cost to satisfy all friends (Recursive with
   * Memoization).
   * For each friend, we calculate the least cost to fill their eating capacity
   * using any number of dishes.
   *
   * Time Complexity: O(F * C * D)
   * - F = number of friends
   * - C = maximum eating capacity
   * - D = number of dishes
   *
   * Space Complexity: O(C) for memoization + O(C) recursion stack
   *
   * @param eatingCapacities Array of each friend's eating capacity
   * @param dishCapacities   Capacity (filling value) of each dish
   * @param dishCosts        Cost of each dish
   * @return Minimum total cost to satisfy all friends
   */
  public int findMinimumCostRecursive(int[] eatingCapacities, int[] dishCapacities, int[] dishCosts) {
    int maxCapacity = Arrays.stream(eatingCapacities).max().orElse(0);

    // Memoization array: memo[i] = minimum cost to reach capacity i
    Integer[] memo = new Integer[maxCapacity + 1];

    int totalMinCost = 0;
    for (int capacity : eatingCapacities) {
      totalMinCost += computeMinCostRecursive(capacity, dishCapacities, dishCosts, memo);
    }

    return totalMinCost;
  }

    /** Returns minimum exact-fill cost for one capacity. */
private int computeMinCostRecursive(int targetCapacity, int[] dishCapacities,
      int[] dishCosts, Integer[] memo) {
    // Base case: no capacity to fill
    if (targetCapacity == 0) {
      return 0;
    }

    // Return memoized result if available
    if (memo[targetCapacity] != null) {
      return memo[targetCapacity];
    }

    int minCost = Integer.MAX_VALUE;

    // Try using each dish
    for (int i = 0; i < dishCapacities.length; i++) {
      int dishCapacity = dishCapacities[i];
      int dishCost = dishCosts[i];

      // Can we use this dish?
      if (dishCapacity <= targetCapacity) {
        int remainingCapacity = targetCapacity - dishCapacity;
        int costForRemaining = computeMinCostRecursive(remainingCapacity, dishCapacities, dishCosts, memo);

        // If remaining capacity is achievable, update minimum
        if (costForRemaining != Integer.MAX_VALUE) {
          minCost = Math.min(minCost, dishCost + costForRemaining);
        }
      }
    }

    // Memoize and return
    memo[targetCapacity] = minCost;
    return minCost;
  }

    /** Builds minimum exact-fill costs up to maxCapacity. */
private int[] computeMinCostForAllCapacities(int maxCapacity, int[] dishCapacities, int[] dishCosts) {
    int[] minCostForCapacity = new int[maxCapacity + 1];
    Arrays.fill(minCostForCapacity, Integer.MAX_VALUE);
    minCostForCapacity[0] = 0; // Base case: 0 cost to fill capacity 0

    for (int capacity = 1; capacity <= maxCapacity; capacity++) {
      for (int i = 0; i < dishCapacities.length; i++) {
        int dishSize = dishCapacities[i];
        int dishCost = dishCosts[i];

        if (capacity >= dishSize && minCostForCapacity[capacity - dishSize] != Integer.MAX_VALUE) {
          minCostForCapacity[capacity] = Math.min(minCostForCapacity[capacity],
              minCostForCapacity[capacity - dishSize] + dishCost);
        }
      }
    }

    return minCostForCapacity;
  }
}