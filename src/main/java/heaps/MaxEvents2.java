package heaps;

import java.util.Arrays;


/**
 * Problem: Maximum Number of Events That Can Be Attended II
 *
 * Given events [startDay, endDay, value], attend at most k non-overlapping events
 * and maximize total value. Attending an event occupies every day in its interval,
 * so the next chosen event must start after the current one ends.
 *
 * Leetcode: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/ (Hard)
 * Rating:   2041 (zerotrac Elo)
 * Pattern:  Dynamic programming | Weighted interval scheduling | Binary search
 *
 * Example:
 *   Input:  events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
 *   Output: 7
 *   Why:    take [1,2,4] and [3,4,3]; they do not overlap and total 4 + 3 = 7.
 *
 * Follow-ups:
 *   1. Can this be written bottom-up?
 *      Sort by end day and fill dp over event prefix and remaining picks.
 *   2. How do you optimize repeated next-event searches?
 *      Precompute next non-overlapping indices with binary search after sorting.
 *   3. What if k is unbounded?
 *      It becomes classic weighted interval scheduling with one dimension of DP.
 *   4. What if events can touch on the same day?
 *      Change the binary-search condition from start >= end + 1 to start >= end.
 *
 * Related: Maximum Number of Events That Can Be Attended (1353), Non-overlapping Intervals (435).
 */

public class MaxEvents2 {

  public static void main(String[] args) {
    MaxEvents2 solver = new MaxEvents2();
    int[][][] inputs = { {{1, 2, 4}, {3, 4, 3}, {2, 3, 1}}, {} };
    int[] kValues = {2, 2};
    int[] expected = {7, 0};

    for (int i = 0; i < inputs.length; i++) {
      int[][] copy = new int[inputs[i].length][];
      for (int row = 0; row < inputs[i].length; row++) {
        copy[row] = inputs[i][row].clone();
      }
      int got = solver.getMaxEventValue(copy, kValues[i]);
      System.out.printf("events=%s k=%d -> %d  expected=%d%n",
          Arrays.deepToString(inputs[i]), kValues[i], got, expected[i]);
    }
  }

  private int[][] memoTable;
  private int eventCount;

    /**
   * Intuition: at each sorted event, the only meaningful choice is take it or
   * skip it. Taking it earns its value and jumps to the first event starting
   * after its end; skipping moves to the next event with the same remaining slots.
   * Memoization keeps those overlapping states from being recomputed.
   *
   * Algorithm:
   *   1. Return 0 for empty input or k = 0.
   *   2. Sort events by start day, then end day, and create a memo table.
   *   3. Recursively compare skipping the current event versus attending it.
   *   4. Use binary search to jump to the next non-overlapping event.
   *
   * Time:  O(n * k * log n) - each state does a binary search for the next event.
   * Space: O(n * k) - memoTable stores one value per index and remaining event count.
   *
   * @param events events as [startDay, endDay, value]
   * @param k maximum number of events that may be attended
   * @return maximum total value from at most k non-overlapping events
   */

  public int getMaxEventValue(int[][] events, int k) {
    if (events == null || events.length == 0 || k == 0) {
        return 0;
    }

    // Sort events based on start day; for stability, if same start, sort by end.
    Arrays.sort(events, (a, b) -> (a[0] != b[0]) ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

    this.eventCount = events.length;
    this.memoTable = new int[eventCount][k + 1]; // memo[i][j] = max value from i-th event with j events left

    // Initialize DP with -1 (uncomputed state)
    for (int[] row : memoTable) {
      Arrays.fill(row, -1);
    }

    computeMaxRec(events, k, 0);
    return memoTable[0][k]; // Start from first event with k slots available
  }

  /** Returns the best value from index with allowedEvents picks left. */
  private int computeMaxRec(int[][] events, int allowedEvents, int index) {
    // Base cases
      if (index >= eventCount || allowedEvents == 0) {
          return 0;
      }
      if (memoTable[index][allowedEvents] != -1) {
          return memoTable[index][allowedEvents];
      }

    // Case 1: Skip current event
    int maxIfSkipped = computeMaxRec(events, allowedEvents, index + 1);

    // Case 2: Attend current event
    int nextIndex = findNextAvailableEvent(events, events[index][1] + 1); // next available event starts after current ends
    int maxIfAttended = events[index][2] + computeMaxRec(events, allowedEvents - 1, nextIndex);

    // Store and return result
    return memoTable[index][allowedEvents] = Math.max(maxIfSkipped, maxIfAttended);
  }

    /**
     * Alternative bottom-up DP approach sorted by end time.
     * More intuitive for understanding the weighted interval scheduling pattern.
     *
     * Algorithm:
     * 1. Sort events by end day (ascending)
     * 2. dp[i][j] = max value using first i events with at most j events attended
     * 3. For each event i and limit j:
     *    - Option 1: Don't take event i → dp[i][j] = dp[i-1][j]
     *    - Option 2: Take event i → dp[i][j] = value[i] + dp[lastNonOverlapping][j-1]
     *    - Take maximum of both options
     * 4. Find last non-overlapping event using binary search
     *
     * Time Complexity: O(n * log(n) + n * k * log(n))
     * Space Complexity: O(n * k)
     *
     * @param events array of events
     * @param k maximum events to attend
     * @return maximum value achievable
     */
    public int maxValueBottomUp(int[][] events, int k) {
        if (events == null || events.length == 0 || k == 0) {
            return 0;
        }
        
        // Step 1: Sort events by end day (for bottom-up processing)
        Arrays.sort(events, (eventA, eventB) -> Integer.compare(eventA[1], eventB[1]));
        
        int numEvents = events.length;
        
        // Step 2: Initialize DP table
        // dp[i][j] = max value considering first i events with at most j events attended
        int[][] dp = new int[numEvents + 1][k + 1];
        
        // Step 3: Fill DP table
        for (int eventIdx = 1; eventIdx <= numEvents; eventIdx++) {
            // Current event is at index (eventIdx - 1) in 0-indexed events array
            int startDay = events[eventIdx - 1][0];
            int endDay = events[eventIdx - 1][1];
            int value = events[eventIdx - 1][2];
            
            for (int eventsCount = 1; eventsCount <= k; eventsCount++) {
                // Option 1: Skip current event
                int skipValue = dp[eventIdx - 1][eventsCount];
                
                // Option 2: Take current event
                // Find last event that doesn't overlap (ends before current starts)
                int lastNonOverlapping = findLastNonOverlappingEvent(events, eventIdx - 1, startDay);
                int takeValue = value + dp[lastNonOverlapping][eventsCount - 1];
                
                // Take maximum of skip and take
                dp[eventIdx][eventsCount] = Math.max(skipValue, takeValue);
            }
        }
        
        // Answer is maximum value using all events with at most k events attended
        return dp[numEvents][k];
    }

    /** Finds the prefix length ending before startDay for bottom-up DP. */
    private int findLastNonOverlappingEvent(int[][] events, int currentIdx, int startDay) {
        int left = 0;
        int right = currentIdx;
        int result = 0; // 0 means no previous non-overlapping event (use dp[0][j] = 0)
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // If this event ends before startDay, it doesn't overlap
            // We want the latest such event, so search right half
            if (events[mid][1] < startDay) {
                result = mid + 1; // +1 because dp array is 1-indexed
                left = mid + 1;
            } else {
                // This event overlaps, search left half
                right = mid;
            }
        }
        
        return result;
    }

  /** Finds the first event whose start day is at least targetDay. */
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
