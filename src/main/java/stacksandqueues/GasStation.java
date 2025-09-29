package stacksandqueues;

/**
 * Gas Station
 *
 * Problem Statement:
 * There are n gas stations along a circular route, where the amount of gas at station i is gas[i].
 * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from station i
 * to its next station (i + 1). You begin the journey with an empty tank at one of the gas stations.
 *
 * Given two integer arrays gas and cost, return the starting gas station's index if you can travel
 * around the circuit once in the clockwise direction, otherwise return -1.
 * If there exists a solution, it is guaranteed to be unique.
 *
 * Example:
 * Input: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
 * Output: 3
 * Explanation: Start at station 3 (index 3) and fill up with 4 unit of gas.
 * Travel to station 4: tank = 0 + 4 - 1 = 3, then add 5 gas: tank = 3 + 5 = 8
 * Travel to station 0: tank = 8 - 2 + 1 = 7
 * Travel to station 1: tank = 7 - 3 + 2 = 6
 * Travel to station 2: tank = 6 - 4 + 3 = 5
 * Travel to station 3: tank = 5 - 5 = 0. Return to starting point with exactly enough gas.
 *
 * LeetCode Link: https://leetcode.com/problems/gas-station
 *
 * Follow-up Questions:
 * 1. What if there are multiple valid starting points?
 *    Answer: Problem guarantees unique solution, but we could modify to return all valid starts.
 * 2. What if we want to find the path that uses minimum gas?
 *    Answer: Use dynamic programming or modify greedy to track minimum consumption path.
 * 3. How would you handle negative gas values (gas leaks)?
 *    Answer: Treat as additional cost and ensure total feasibility check accounts for leaks.
 *    Related: https://leetcode.com/problems/minimum-number-of-refueling-stops/
 * 4. What about optimizing for real-time traffic conditions?
 *    Answer: Use weighted costs and dynamic recalculation as conditions change.
 */
public class GasStation {

  /**
   * Finds the starting gas station index using optimal greedy approach.
   *
   * Algorithm: One-Pass Greedy with Total Feasibility Check
   * Step 1: Check if total gas >= total cost (necessary condition for solution)
   * Step 2: Traverse stations tracking current fuel balance
   * Step 3: When balance goes negative, reset start to next station and balance to 0
   * Step 4: Return the final starting index (guaranteed valid if step 1 passed)
   * Step 5: Key insight: if we fail at station i starting from j, then j+1...i cannot be valid starts
   *
   * Time Complexity: O(n) where n is number of gas stations
   * - Single pass through all stations
   * - Constant time operations per station
   * Space Complexity: O(1) using only a few variables
   *
   * @param gas array where gas[i] is amount of gas at station i
   * @param cost array where cost[i] is gas needed to travel from station i to i+1
   * @return starting station index if circuit possible, -1 otherwise
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
    int[] gas = {1, 2, 3, 4, 5};
    int[] cost = {3, 4, 5, 1, 2};

    int startIndex = canCompleteCircuit(gas, cost);
    System.out.println("Start Index: " + startIndex); // Expected Output: 3
  }
}
