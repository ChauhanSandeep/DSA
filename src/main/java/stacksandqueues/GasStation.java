package stacksandqueues;

import java.util.Arrays;

/**
 * Problem: Gas Station
 *
 * Gas stations form a circular route. At station i you gain gas[i] fuel and
 * spend cost[i] fuel to drive to the next station. Return the unique start
 * index that completes the circle, or -1 if total fuel is insufficient.
 *
 * Leetcode: https://leetcode.com/problems/gas-station/ (Medium)
 * Rating:   acceptance 48.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Prefix deficit | Circular route
 *
 * Example:
 *   Input:  gas = [2,3,4], cost = [3,4,3]
 *   Output: -1
 *   Why:    total gas is less than total cost, so the car must run out before completing the circle.
 *
 * Follow-ups:
 *   1. Return all valid starts when uniqueness is not guaranteed?
 *      Compute circular prefix minima and collect starts whose rotated prefix never drops below zero.
 *   2. Tank capacity is limited?
 *      Simulate with capped fuel; total surplus is no longer sufficient, so binary/search candidates.
 *   3. Costs change over time from traffic?
 *      Model cost as edge weights by departure time and solve with time-dependent shortest paths.
 *   4. Need the minimum initial fuel for a fixed start?
 *      Track the minimum prefix balance and start with its absolute deficit.
 *
 * Related: Minimum Number of Refueling Stops (871).
 */
public class GasStation {

    /**
   * Intuition: total gas decides if any solution exists, while local deficits
   * decide which starts are impossible. If the tank drops below zero at i, then
   * every candidate from `startingStation` through i also fails, so the next
   * possible start is i + 1.
   *
   * Algorithm:
   *   1. Scan stations while accumulating total gas, total cost, and current tank.
   *   2. Add gas[i] - cost[i] to the current tank.
   *   3. When current tank is negative, reset start to i + 1 and tank to 0.
   *   4. Return the start only if total gas is at least total cost.
   *
   * Time:  O(n) - one pass over the stations.
   * Space: O(1) - only running totals and one candidate start are stored.
   *
   * @param gas amount of fuel available at each station
   * @param cost fuel needed to travel to the next station
   * @return starting station index if possible, otherwise -1
   */
public static int canCompleteCircuit(int[] gas, int[] cost) {
    if (gas == null || cost == null || gas.length != cost.length) {
      return -1;
    }

    int size = gas.length;
    int totalGas = 0;
    int totalCost = 0;
    int currentTank = 0;
    int startingStation = 0;

    // Step 1: Check total feasibility and simulate journey
    for (int i = 0; i < size; i++) {
      totalGas += gas[i];
      totalCost += cost[i];

      // Step 2: Update current fuel balance
      currentTank += gas[i] - cost[i];

      // Step 3: If balance negative, reset starting point
      if (currentTank < 0) {
        // Key insight: all stations from startingStation to i cannot be valid starts
        startingStation = i + 1;
        currentTank = 0; // Reset fuel balance
      }
      /**
       * Given facts from our algorithm:
       * Condition 1 : totalGas >= totalCost (we checked this)
       * Condition 2 : We failed to complete the journey starting from stations 0, 1, 2, ..., k-1
       * Condition 3 : Starting from station k, we never went negative in our remaining journey to station n-1
       *
       * From condition 2: we know deficit exists (otherwise we wouldn't have failed)
       * From condition 3: we know surplus >= 0 (we didn't go negative from k to n-1)
       * From condition 1: totalGas >= totalCost means (deficit + surplus) >= 0
       * Therefore: surplus >= deficit
       *
       * This means: surplus from stations [k, n-1] is enough to cover deficit from stations [0, k-1]
       * So we don't actually need to re-check stations [0, k-1] when starting from k
       * This is why startingStation = k is guaranteed to be a valid start if totalGas
       */
    }

    // Step 4: Return result based on total feasibility
    return totalGas >= totalCost ? startingStation : -1;
  }
  public static void main(String[] args) {
    int[][] gasCases = { {2, 3, 4}, {1, 2, 3, 4, 5}, {1}, {5} };
    int[][] costCases = { {3, 4, 3}, {3, 4, 5, 1, 2}, {2}, {4} };
    int[] expected = {-1, 3, -1, 0};
    for (int i = 0; i < gasCases.length; i++) {
      int got = canCompleteCircuit(gasCases[i], costCases[i]);
      System.out.printf("gas=%s cost=%s -> %d  expected=%d%n", Arrays.toString(gasCases[i]), Arrays.toString(costCases[i]), got, expected[i]);
    }
  }
}
