package Heap;

import java.util.Arrays;

/**
 * You are given an array of events. Each event is like [startDay, endDay, value] where startDay and endDay
 * are inclusive. If you choose to attend the event, you have to attend all days(startDay to endDay inclusive).
 * In exchange, you will get value. Find the max value you can get.
 *
 * https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
 */
public class MaxEvents2 {
  public static void main(String[] args) {
    int[][] events = {
        {1, 2, 4}, // [startDay, endDay, value]
        {3, 4, 3},
        {2, 3, 1}
    };
    int k = 2;
    System.out.println(new MaxEvents2().maxValue(events, k));
  }

  private int[][] dp;
  private int eventCount;

  public int maxValue(int[][] events, int k) {
    if (events == null || events.length == 0) return 0;

    Arrays.sort(events, (a, b) -> a[0] - b[0]); // Sort by start day
    this.eventCount = events.length;
    this.dp = new int[eventCount][k + 1];

    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }

    return findMaxValue(events, k, 0);
  }

  private int findMaxValue(int[][] events, int remaining, int index) {
    if (index >= eventCount || remaining == 0) return 0;
    if (dp[index][remaining] != -1) return dp[index][remaining];

    int valueWithoutCurrent = findMaxValue(events, remaining, index + 1);

    int nextIndex = binarySearch(events, events[index][1] + 1);
    int valueWithCurrent = events[index][2] + findMaxValue(events, remaining - 1, nextIndex);

    return dp[index][remaining] = Math.max(valueWithoutCurrent, valueWithCurrent);
  }

  private int binarySearch(int[][] events, int start) {
    int left = 0, right = eventCount;
    while (left < right) {
      int mid = left + (right - left) / 2;
      if (events[mid][0] >= start) {
        right = mid;
      } else {
        left = mid + 1;
      }
    }
    return left;
  }
}