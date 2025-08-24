package dynamicprogramming;

import java.util.PriorityQueue;


/**
 * Problem: Minimum Refueling Stops
 *
 * You are given:
 * - A `target` distance you need to travel.
 * - `startFuel`, the initial amount of fuel in your tank.
 * - A list of `stations`, where stations[i] = [position_i, fuel_i] means a station at position_i offers fuel_i liters.
 *
 * Return the **minimum number of refueling stops** to reach the target.
 * If you cannot reach the target, return **-1**.
 *
 * Example:
 * Input:
 *   target = 100,
 *   startFuel = 25,
 *   stations = [
 *   [25,25],
 *   [50,25],
 *   [75,25]
 *   ]
 * Output: 3
 * Explanation:
 * - Start at position 0 with 25 fuel.
 * - Stop at station 1 (position 25, fuel 25) to refuel to 50.
 * * - Stop at station 2 (position 50, fuel 25) to refuel to 75.
 *
 * LeetCode Link:
 * https://leetcode.com/problems/minimum-number-of-refueling-stops/
 *
 * Follow-up Questions:
 * 1. Can we use dynamic programming to solve this problem?
 *    ➤ Yes. DP[i] = max distance reachable with i refuels.
 * 2. What if all fuel stations are sorted in reverse?
 *    ➤ Doesn't matter; algorithm still works since it greedily adds reachable stations.
 */
public class MinRefuel {

  public static void main(String[] args) {
    int[][] stations = {{25, 25}, {50, 25}, {75, 25}};
    int target = 100;
    int startFuel = 25;

    int result = new MinRefuel().minRefuelStops(target, startFuel, stations);
    System.out.println("Minimum Refueling Stops: " + result); // Output: 3
  }

  /**
   * Greedy + Max-Heap Approach:
   * Always choose to refuel from the station with the **maximum** fuel reachable so far.
   *
   * Algorithm:
   * 1. Traverse stations in order.
   * 2. Add all stations within reach to a max-heap.
   * 3. If no station is available and target is not yet reached, return -1.
   * 4. Greedily refuel from the station with the most fuel (poll from heap).
   * 5. Repeat until the target is reachable.
   *
   * Time Complexity: O(n log n) due to heap operations.
   * Space Complexity: O(n) for the heap.
   */
  public int minRefuelStops(int target, int startFuel, int[][] stations) {
      if (startFuel >= target) {
          return 0;
      }

    int numStops = 0;
    int stationIndex = 0;
    int currentFuel = startFuel;
    int totalStations = stations.length;

    // Max-heap to store fuel capacities of reachable stations
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);

    // While the current fuel can't take us to the target
    while (currentFuel < target) {
      // Add all stations we can currently reach with available fuel into the heap
      while (stationIndex < totalStations && stations[stationIndex][0] <= currentFuel) {
        maxHeap.offer(stations[stationIndex][1]);
        stationIndex++;
      }

      // If no fuel stations are available and we can't reach the target
        if (maxHeap.isEmpty()) {
            return -1;
        }

      // Refuel from the station with the most fuel
      currentFuel += maxHeap.poll();
      numStops++;
    }

    return numStops;
  }

  /**
   * DP-based solution to find minimum refueling stops.
   *
   * Algorithm:
   * - Define `dp[i]` as the **maximum distance** we can reach with exactly `i` refuels.
   * - Initialize dp[0] = startFuel, and the rest as 0.
   * - For each station:
   *     - Traverse `dp[]` in reverse (to prevent using the same station multiple times in a round).
   *     - If we can reach the station with `i` stops (`dp[i] >= station.position`), then we can consider
   *       refueling at this station to update `dp[i + 1] = max(dp[i + 1], dp[i] + station.fuel)`
   * - After processing all stations, the smallest `i` such that `dp[i] >= target` is our answer.
   *
   * Time Complexity: O(n^2), where n = number of stations (each station updates up to n entries)
   * Space Complexity: O(n) for the dp array
   */
  public int minRefuelStopsUsingDP(int target, int startFuel, int[][] stations) {
    int n = stations.length;
    long[] dp = new long[n + 1];  // dp[i] = max distance reachable with i refuels
    dp[0] = startFuel;

    for (int i = 0; i < n; i++) {
      int stationPos = stations[i][0];
      int stationFuel = stations[i][1];

      // Traverse backwards to ensure no reuse in the same iteration
      for (int refuels = i; refuels >= 0; refuels--) {
        if (dp[refuels] >= stationPos) {
          dp[refuels + 1] = Math.max(dp[refuels + 1], dp[refuels] + stationFuel);
        }
      }
    }

    // Find the minimum number of stops required to reach or exceed the target
    for (int i = 0; i <= n; i++) {
      if (dp[i] >= target) {
        return i;
      }
    }

    return -1;
  }
}