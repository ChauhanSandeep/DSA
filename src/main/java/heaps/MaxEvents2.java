package heaps;

import java.util.Arrays;


/**
 * -------------------------------------------------------------
 * 💡 Problem: Maximum Number of Events That Can Be Attended II
 * -------------------------------------------------------------
 * You are given an array `events`, where each event is represented as [startDay, endDay, value].
 * You can attend at most `k` events, and attending an event requires you to be available for all the days
 * from its start to end. Your task is to select events (up to k) such that the total value is maximized,
 * and no two attended events overlap.
 *
 * 🔹 Example:
 * Input: events = [
 *  [1,2,4],
 *  [3,4,3],
 *  [2,3,1]],
 * k = 2
 * Output: 7
 * Explanation: Attend event [1,2,4] and [3,4,3] for a total value of 7.
 *
 * 🔗 Leetcode Link: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 *
 * -------------------------------------------------------------
 * 🔄 Follow-up Questions:
 * -------------------------------------------------------------
 * 1. Can we do this in bottom-up DP (tabulation) instead of recursion + memoization?
 *    → Yes. It's more iterative, usually easier to debug, and better stack-wise.
 * 2. What if each event has a cost instead of value and we want to **minimize** total cost?
 *    → Similar approach but maximize becomes minimize, and base case logic needs tweaks.
 * 3. Can this be solved with Segment Trees?
 *    → Yes, in O(N log N) using event processing + range max queries. See: LC hard variants.
 */
public class MaxEvents2 {

  public static void main(String[] args) {
    int[][] events = {{1, 2, 4}, {3, 4, 3}, {2, 3, 1}};
    int k = 2;
    System.out.println("Max value: " + new MaxEvents2().getMaxEventValue(events, k));
  }

  private int[][] memoTable;
  private int eventCount;

  /**
   * Solves the problem using recursive DP with memoization and binary search.
   *
   * Steps:
   * 1. Sort the events by start time for binary search applicability.
   * 2. For each event:
   *      - Either attend it → add its value and jump to the next non-overlapping event.
   *      - Or skip it → move to the next event without using a slot.
   * 3. Use memoization to store intermediate results.
   *
   * Time Complexity: O((N * K) + N log N)
   * - N*K for memoization.
   * - log N for binary search at each step.
   * Space Complexity: O(N * K) for the memoization table.
   */
  public int getMaxEventValue(int[][] events, int k) {
      if (events == null || events.length == 0 || k == 0) {
          return 0;
      }

    // Sort events based on start day; for stability, if same start, sort by end.
    Arrays.sort(events, (a, b) -> (a[0] != b[0]) ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

    this.eventCount = events.length;
    this.memoTable = new int[eventCount][k + 1]; // memo[i][j] = max value from event i with j slots left

    // Initialize DP with -1 (uncomputed state)
    for (int[] row : memoTable) {
      Arrays.fill(row, -1);
    }

    computeMax(events, k, 0);
    return memoTable[0][k]; // Start from first event with k slots available
  }

  /**
   * Recursive method to compute maximum value using top-down memoization.
   *
   * @param events    Sorted list of events.
   * @param allowedEvents Number of events we can still attend.
   * @param index     Current index in event list.
   * @return Maximum value obtainable from current state.
   */
  private int computeMax(int[][] events, int allowedEvents, int index) {
    // Base cases
      if (index >= eventCount || allowedEvents == 0) {
          return 0;
      }
      if (memoTable[index][allowedEvents] != -1) {
          return memoTable[index][allowedEvents];
      }

    // Case 1: Skip current event
    int maxIfSkipped = computeMax(events, allowedEvents, index + 1);

    // Case 2: Attend current event
    int nextIndex = findNextAvailableEvent(events, events[index][1] + 1); // next available event starts after current ends
    int maxIfAttended = events[index][2] + computeMax(events, allowedEvents - 1, nextIndex);

    // Store and return result
    return memoTable[index][allowedEvents] = Math.max(maxIfSkipped, maxIfAttended);
  }

  /**
   * Finds index of the next event that starts after given day using binary search.
   *
   * @param events     Sorted list of events.
   * @param targetDay  Day to start searching for.
   * @return Index of the next non-overlapping event.
   */
  private int findNextAvailableEvent(int[][] events, int targetDay) {
    int left = 0, right = eventCount;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (events[mid][0] >= targetDay) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }
    return left;
  }
}
