package arrays.twopointers;

import java.util.*;


/**
 * Problem: Find the resource with the highest number of accesses within any 5-minute window.
 *
 * Input: Logs in the form [timestamp, user, resource]
 * Example:
 * logs1 = [
 *   {"58523", "user_1", "resource_1"},
 *   {"62314", "user_2", "resource_2"},
 *   {"54001", "user_1", "resource_3"},
 *   {"200",   "user_6", "resource_5"},
 *   {"215",   "user_6", "resource_4"},
 *   {"54060", "user_2", "resource_3"},
 *   {"53760", "user_3", "resource_3"},
 *   {"58522", "user_22","resource_1"},
 *   {"53651", "user_5", "resource_3"},
 *   {"2",     "user_6", "resource_1"},
 *   {"100",   "user_6", "resource_6"},
 *   {"400",   "user_7", "resource_2"},
 *   {"100",   "user_8", "resource_6"},
 *   {"54359", "user_1", "resource_3"}
 * ]
 *
 * Output: [resource_3, 3]  // meaning resource_3 had 3 accesses in some 5-minute window
 *
 * LeetCode Similar Problem:
 * - Analyze User Website Visit Pattern: https://leetcode.com/problems/analyze-user-website-visit-pattern/
 *
 * Follow-up Questions:
 * 1. Can we generalize the window size (not fixed at 5 minutes)?
 *    - Yes, make the window size configurable as a parameter.
 * 2. How to handle very large logs efficiently?
 *    - Use streaming algorithms with sliding windows instead of sorting all timestamps.
 * 3. What if we also want the users who accessed in that window?
 *    - Maintain user IDs in the sliding window while counting.
 * LeetCode Contest Rating: 1851
 **/
public class MaxResourceAccess {

  public static void main(String[] args) {
    String[][] logs1 =
        {{"58523", "user_1", "resource_1"}, {"62314", "user_2", "resource_2"}, {"54001", "user_1", "resource_3"},
            {"200", "user_6", "resource_5"}, {"215", "user_6", "resource_4"}, {"54060", "user_2", "resource_3"},
            {"53760", "user_3", "resource_3"}, {"58522", "user_22", "resource_1"}, {"53651", "user_5", "resource_3"},
            {"2", "user_6", "resource_1"}, {"100", "user_6", "resource_6"}, {"400", "user_7", "resource_2"},
            {"100", "user_8", "resource_6"}, {"54359", "user_1", "resource_3"},};

    String[] result = mostAccessedResource(logs1, 300);
    System.out.println(Arrays.toString(result));
  }

  /**
   * Main intuition for solution:
   * We need to count accesses per resource within a sliding time window.
   * For each resource, we gather all access timestamps, sort them, and then use a two-pointer technique to find the maximum number of accesses
   * that fall within any 5-minute (300 seconds) window.
   *
   * Steps:
   * 1. Parse logs to map each resource to its list of access timestamps.
   * 2. For each resource, sort its timestamps.
   * 3. Use two pointers to find the maximum number of accesses within any 300-second window.
   *
   * Algorithm: Sliding Window
   * Time Complexity: O(N log N) due to sorting timestamps per resource
   * Space Complexity: O(N) for storing timestamps
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
