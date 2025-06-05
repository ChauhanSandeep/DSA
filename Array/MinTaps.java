package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Given an array representing garden taps, where each element denotes the range of the tap at that index,
 * this solution finds the minimum number of taps required to water the entire garden.
 * Example:
 * Input: target = 5, ranges = [4, 3, 1, 2, 0, 0]
 * Output: 1 (only 1 index tap is needed to cover the entire garden)
 *
 * Approach:
 * - Use a greedy algorithm to find the minimum number of taps needed.
 * - Each tap can water a range of plants. We need to find the minimum number of taps
 *   that can cover the entire garden from leftmost to rightmost.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/
 *
 * Algorithm: Greedy Interval Covering with Precomputation
 * Time Complexity: O(n) - We process the garden length efficiently.
 * Space Complexity: O(n) - Uses an auxiliary array to store maximum reach from each position.
 *
 * Follow-up:
 * Q: What if each tap had a cost instead of uniform activation?
 * A: Now it's a weighted interval cover problem; you can use Dijkstra/DP.
 */
public class MinTaps {

  public static void main(String[] args) {
    int target = 5;
    int[] ranges = {4, 3, 1, 2, 0, 0};
    int taps = new MinTaps().minTaps(target, ranges);
    System.out.println("Minimum taps required: " + taps);
  }

  /**
   * **Approach (Greedy Algorithm - O(n^2)):**
   * - Start from position `0` (leftmost point of the garden).
   * - In each step, find the **tap that extends coverage the farthest** from the current position.
   * - If no tap can extend coverage, return `-1` (garden cannot be fully watered).
   * - Continue until the entire garden (`target`) is covered.
   *
   * **Time Complexity:** O(n^2) (Can be optimized to O(n) using a sorted jump approach)
   * **Space Complexity:** O(1) (No extra space used)
   *
   * @param target Length of the garden (last index to be covered)
   * @param ranges Array where `ranges[i]` represents the tap range at position `i`
   * @return Minimum taps required to fully water the garden, or `-1` if impossible
   */
  public int minTaps(int target, int[] ranges) {
    int currentCoverageEnd = 0;  // Tracks the farthest point currently covered
    int nextCoverageEnd = 0;     // Tracks the farthest extension possible
    int tapsUsed = 0;            // Counts the number of taps turned on

    while (currentCoverageEnd < target) {
      // Find the tap that extends coverage the farthest within the current reachable range
      for (int i = 0; i < ranges.length; i++) {
        int leftLimit = i - ranges[i];   // Left boundary of this tap's range
        int rightLimit = i + ranges[i];  // Right boundary of this tap's range

        // If this tap covers the current uncovered area and extends beyond the current max
        if (leftLimit <= currentCoverageEnd && rightLimit > nextCoverageEnd) {
          nextCoverageEnd = rightLimit;
        }
      }

      // If no new coverage is found, it's impossible to cover the full garden
        if (currentCoverageEnd == nextCoverageEnd) {
            return -1;
        }

      // Activate a tap and extend the coverage
      tapsUsed++;
      currentCoverageEnd = nextCoverageEnd;
    }

    return tapsUsed;
  }

  /**
   * **Approach (Greedy Algorithm - O(n^2)):**
   * - Start from position `0` (leftmost point of the garden).
   * - In each step, find the **tap that extends coverage the farthest** from the current position.
   * - If no tap can extend coverage, return `-1` (garden cannot be fully watered).
   * - Continue until the entire garden (`target`) is covered.
   * - Also, keep track of the indices of taps used to achieve this coverage.
   *
   * **Time Complexity:** O(n^2) (Can be optimized to O(n) using a sorted jump approach)
   * **Space Complexity:** O(n) (to store the list of taps used)
   *
   * @param target Length of the garden (last index to be covered)
   * @param ranges Array where `ranges[i]` represents the tap range at position `i`
   * @return List of taps' indices that need to be turned on to fully water the garden, or `[-1]` if impossible
   */
  public List<Integer> minTapsWithPath(int target, int[] ranges) {
    int currentCoverageEnd = 0;  // Farthest point currently covered
    int nextCoverageEnd = 0;     // Farthest extension possible
    List<Integer> tapsUsedList = new ArrayList<>();  // List of tap indices used

    while (currentCoverageEnd < target) {
      int selectedTap = -1;  // Track the tap selected in this round

      // Find the tap that extends coverage the farthest within the current reachable range
      for (int i = 0; i < ranges.length; i++) {
        int leftLimit = i - ranges[i];
        int rightLimit = i + ranges[i];

        // If this tap covers the current uncovered area and extends beyond current max
        if (leftLimit <= currentCoverageEnd && rightLimit > nextCoverageEnd) {
          nextCoverageEnd = rightLimit;
          selectedTap = i;
        }
      }

      // If no new coverage is found, it's impossible to cover the garden
      if (currentCoverageEnd == nextCoverageEnd) {
        return Arrays.asList(-1);  // Return [-1] to indicate failure
      }

      // Activate the selected tap and extend the coverage
      tapsUsedList.add(selectedTap);
      currentCoverageEnd = nextCoverageEnd;
    }

    return tapsUsedList;
  }
}