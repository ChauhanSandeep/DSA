package arrays.twopointers;

import java.util.*;


/**
 * Problem: Most Accessed Resource in a Time Window
 *
 * Given logs of timestamp, user, and resource, return the resource with the
 * largest number of accesses inside any configurable time window. Timestamps are
 * grouped by resource, sorted, and scanned with a sliding window.
 *
 * Leetcode: Similar to https://leetcode.com/problems/analyze-user-website-visit-pattern/ (Medium)
 * Pattern:  Hash map | Sorting | Sliding window per resource
 *
 * Example:
 *   Input:  logs = [["100","u1","r"],["200","u2","r"],["500","u3","r"]], windowSize = 300
 *   Output: [r, 2]
 *   Why:    timestamps 100 and 200 fit in one 300-second window, but 500 does not join them.
 *
 * Follow-ups:
 *   1. Handle logs as a stream instead of a batch?
 *      Keep a per-resource deque and evict old timestamps as new events arrive.
 *   2. Return all resources tied for the maximum?
 *      Track a list of resources whenever a count equals the current best.
 *   3. Include users active in the winning window?
 *      Store full events per resource and maintain user IDs inside the sliding window.
 *
 * Related: Analyze User Website Visit Pattern (1152), Sliding Window Maximum (239).
 */
public class MaxResourceAccess {

public static void main(String[] args) {
  String[][][] cases = {
      {{"58523", "user_1", "resource_1"}, {"62314", "user_2", "resource_2"}, {"54001", "user_1", "resource_3"},
          {"200", "user_6", "resource_5"}, {"215", "user_6", "resource_4"}, {"54060", "user_2", "resource_3"},
          {"53760", "user_3", "resource_3"}, {"58522", "user_22", "resource_1"}, {"53651", "user_5", "resource_3"},
          {"2", "user_6", "resource_1"}, {"100", "user_6", "resource_6"}, {"400", "user_7", "resource_2"},
          {"100", "user_8", "resource_6"}, {"54359", "user_1", "resource_3"}},
      {{"10", "u1", "r"}}
  };
  int[] windows = { 300, 300 };
  String[][] expected = { {"resource_3", "3"}, {"r", "1"} };

  for (int i = 0; i < cases.length; i++) {
    String[] got = mostAccessedResource(cases[i], windows[i]);
    System.out.printf("logs=%s window=%d -> %s  expected=%s%n",
        Arrays.deepToString(cases[i]), windows[i], Arrays.toString(got), Arrays.toString(expected[i]));
  }
}

  /**
 * Intuition: accesses for different resources never interact, so first split
 * the logs by resource. Once one resource's timestamps are sorted, the best
 * window is found by expanding rightPointer and advancing leftPointer only
 * when the time span becomes too large.
 *
 * Algorithm:
 *   1. Build resourceToTimestamps from the log rows.
 *   2. For each resource, sort its timestamps.
 *   3. Use a sliding window to count the most timestamps within windowSize.
 *   4. Return the resource with the largest window count.
 *
 * Time:  O(n log n) - timestamps are sorted within resource groups.
 * Space: O(n) - every timestamp is stored in the grouping map.
 *
 * @param logs rows of timestamp, user, and resource
 * @param windowSize inclusive time-window width
 * @return resource name and maximum access count as strings, or null for empty logs
 */
  public static String[] mostAccessedResource(String[][] logs, int windowSize) {
      if (logs == null || logs.length == 0) {
          return null;
      }

    // Map resource -> list of access timestamps
    Map<String, List<Integer>> resourceToTimestamps = new HashMap<>(); // <resource, List<timestamps>>
    for (String[] log : logs) {
      int timestamp = Integer.parseInt(log[0]);
      String resource = log[2];
      resourceToTimestamps.computeIfAbsent(resource, k -> new ArrayList<>()).add(timestamp);
    }

    String maxResource = "";
    int maxAccessCount = 0;

    // Evaluate each resource
    for (Map.Entry<String, List<Integer>> entry : resourceToTimestamps.entrySet()) {
      String resource = entry.getKey();
      List<Integer> timestamps = entry.getValue();

      int count = getMaxAccessInWindow(timestamps, windowSize);
      if (count > maxAccessCount) {
        maxResource = resource;
        maxAccessCount = count;
      }
    }

    return new String[]{maxResource, String.valueOf(maxAccessCount)};
  }

  /**
   * Sliding window to compute max accesses within a time window.
   *
   * - Sort timestamps.
   * - Use two pointers (left, right) to represent the current window.
   * - Expand right pointer, and move left pointer to maintain window size.
   * - Track maximum count of accesses in any valid window.
   */
  private static int getMaxAccessInWindow(List<Integer> timestamps, int windowSize) {
    Collections.sort(timestamps);
    int maxCount = 0;
    int leftPointer = 0;

    // Expand right pointer, shrink left pointer as needed
    for (int rightPointer = 0; rightPointer < timestamps.size(); rightPointer++) {
      while (timestamps.get(rightPointer) - timestamps.get(leftPointer) > windowSize) {
        leftPointer++;
      }
      maxCount = Math.max(maxCount, rightPointer - leftPointer + 1);
    }
    return maxCount;
  }
}
