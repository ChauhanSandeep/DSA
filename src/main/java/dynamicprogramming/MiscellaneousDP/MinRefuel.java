package dynamicprogramming.MiscellaneousDP;

import java.util.PriorityQueue;
import java.util.Arrays;


/**
 * Problem: Minimum Number of Refueling Stops
 *
 * You start with some fuel and pass stations on the way to a target distance. Each
 * station has a position and fuel amount. Return the fewest refuels needed to
 * reach the target, or -1 if it is impossible.
 *
 * Leetcode: https://leetcode.com/problems/minimum-number-of-refueling-stops/
 * Rating:   2074 (zerotrac Elo)
 * Pattern:  Greedy | Max heap | Reachability DP alternative
 *
 * Example:
 *   Input:  target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]]
 *   Output: 2
 *   Why:    refuel at distance 10, later choose the best reachable fuel again, and
 *           two total stops are enough to reach 100.
 *
 * Follow-ups:
 *   1. Can this be solved with DP?
 *      Yes, dp[stops] stores the farthest distance reachable with exactly that many refuels.
 *   2. What if station fuel has a price and we minimize cost?
 *      Use a cost-aware shortest path or greedy fuel-price strategy depending on constraints.
 *   3. Return the chosen station indices?
 *      Store station indices in the heap and record each polled station.
 *
 * Related: Gas Station (134), Course Schedule III (630).
 */
public class MinRefuel {

    public static void main(String[] args) {
        MinRefuel solver = new MinRefuel();
        int[] targets = {1, 100, 100};
        int[] startFuel = {1, 1, 10};
        int[][][] stations = {
            {},
            {{10, 100}},
            {{10, 60}, {20, 30}, {30, 30}, {60, 40}}
        };
        int[] expected = {0, -1, 2};

        for (int i = 0; i < targets.length; i++) {
            int got = solver.minRefuelStops(targets[i], startFuel[i], stations[i]);
            System.out.printf("target=%d startFuel=%d stations=%s -> %d  expected=%d%n",
                targets[i], startFuel[i], Arrays.deepToString(stations[i]), got, expected[i]);
        }
    }


  /**
     * Intuition (interview default): drive as far as possible before deciding to
     * refuel. Every station already passed is a valid past choice, and if we get
     * stuck, the best past choice is the reachable station with the most fuel. A
     * max heap keeps those available fuels ready. This greedy choice is safe
     * because refueling earlier or later from the same passed station gives the
     * same added reach, so choosing the largest fuel maximizes future options.
     *
     * Algorithm:
     *   1. Push every station whose position is within currentFuel into a max heap.
     *   2. If the target is still unreachable and the heap is empty, return -1.
     *   3. Otherwise refuel from the largest reachable station and count one stop.
     *   4. Repeat until currentFuel reaches target.
     *
     * Time:  O(n log n) - each station is pushed once and at most once popped from the heap.
     * Space: O(n) - the heap can hold all reachable stations not yet used.
     *
     * @param target destination distance
     * @param startFuel initial fuel amount
     * @param stations stations[i] is [position, fuel]
     * @return minimum number of refuels, or -1 if impossible
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
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

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
