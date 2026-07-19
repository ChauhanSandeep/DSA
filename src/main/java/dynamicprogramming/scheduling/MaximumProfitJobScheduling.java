package dynamicprogramming.scheduling;

import java.util.*;


/**
 * Problem: Maximum Profit in Job Scheduling
 *
 * Choose non-overlapping jobs with maximum total profit. A job ending at time x is compatible with one starting at time x.
 *
 * Leetcode: https://leetcode.com/problems/maximum-profit-in-job-scheduling/ (Hard)
 * Rating:   contest Elo 2023
 * Pattern:  Dynamic programming | Weighted interval scheduling | Binary search
 *
 * Example:
 *   Input:  start = [1,2,3,3], end = [3,4,5,6], profit = [50,10,40,70]
 *   Output: 120
 *   Why:    jobs [1,3] and [3,6] do not overlap and earn 50 + 70.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Non-overlapping Intervals (435), Course Schedule III (630).
 */
public class MaximumProfitJobScheduling {

    public static void main(String[] args) {
    MaximumProfitJobScheduling scheduler = new MaximumProfitJobScheduling();
    int[][] startCases = { {1, 2, 3, 3}, {1, 2, 3, 4, 6}, {} };
    int[][] endCases = { {3, 4, 5, 6}, {3, 5, 10, 6, 9}, {} };
    int[][] profitCases = { {50, 10, 40, 70}, {20, 20, 100, 70, 60}, {} };
    int[] expected = {120, 150, 0};
    for (int i = 0; i < startCases.length; i++) {
      int got = scheduler.jobScheduling(startCases[i], endCases[i], profitCases[i]);
      System.out.printf("start=%s end=%s profit=%s -> %d  expected=%d%n", Arrays.toString(startCases[i]), Arrays.toString(endCases[i]), Arrays.toString(profitCases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: after sorting jobs by endTime, maxProfitUpToJob[i] is the best profit using jobs through i. For each job, compare skipping it with taking it plus the best previous non-overlapping job found by binary search.
   *
   * Algorithm:
   *   1. Build and sort JobSchedule objects by end time.
   *   2. Initialize the first DP value.
   *   3. For each job, compute include profit.
   *   4. Binary-search the previous compatible job and add its DP value.
   *   5. Store max(exclude, include).
   *
   * Time:  O(n log n) - sorting plus one binary search per job.
   * Space: O(n) - jobs and DP array.
   *
   * @param startTime job starts
   * @param endTime job ends
   * @param profit job profits
   * @return maximum non-overlapping profit
   */
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int numberOfJobs = startTime.length;

    // Edge case: no jobs available
    if (numberOfJobs == 0) {
      return 0;
    }

    // Create job objects and sort by end time for optimal substructure
    List<JobSchedule> jobs = new ArrayList<>();
    for (int i = 0; i < numberOfJobs; i++) {
      jobs.add(new JobSchedule(startTime[i], endTime[i], profit[i]));
    }
    jobs.sort(Comparator.comparingInt(job -> job.endTime));

    // DP array where maxProfitUpToJob[i] = maximum profit achievable up to job i
    int[] maxProfitUpToJob = new int[numberOfJobs];
    maxProfitUpToJob[0] = jobs.get(0).profit;

    // Process each job and decide whether to include it or not
    for (int currentJobIndex = 1; currentJobIndex < numberOfJobs; currentJobIndex++) {
      int profitIfCurrentJobIncluded = jobs.get(currentJobIndex).profit;

      // Find latest job (on left side) that doesn't overlap with current job
      int previousNonOverlappingJobIndex = findPreviousNonOverlappingJob(jobs, currentJobIndex);
      if (previousNonOverlappingJobIndex != -1) {
        profitIfCurrentJobIncluded += maxProfitUpToJob[previousNonOverlappingJobIndex];
      }

      // Choose maximum between including current job or excluding it
      maxProfitUpToJob[currentJobIndex] = Math.max(maxProfitUpToJob[currentJobIndex - 1], // exclude current job
          profitIfCurrentJobIncluded             // include current job
      );
    }

    return maxProfitUpToJob[numberOfJobs - 1];
  }

  /**
   * Same optimum as {@link #jobScheduling(int[], int[], int[])}, computed via recursion:
   * {@code maxProfit(i) = max(maxProfit(i-1), profit[i] + maxProfit(prevNonOverlapping))}.
   * The call chain starts at index {@code 0} and steps forward to {@code n} ({@code index -> index + 1}),
   * filling {@code memo[0 .. n-1]} in order (same recurrence as the iterative DP table).
   *
   * Time Complexity: O(n log n) — sorting plus binary search per index along the forward chain
   * Space Complexity: O(n) — memo array plus recursion depth {@code n}
   */
  public int jobSchedulingRecursive(int[] startTime, int[] endTime, int[] profit) {
    int numberOfJobs = startTime.length;
    if (numberOfJobs == 0) {
      return 0;
    }

    List<JobSchedule> jobs = new ArrayList<>();
    for (int i = 0; i < numberOfJobs; i++) {
      jobs.add(new JobSchedule(startTime[i], endTime[i], profit[i]));
    }
    jobs.sort(Comparator.comparingInt(job -> job.endTime));

    int[] memo = new int[numberOfJobs];
    return maxProfitForwardRecursive(jobs, 0, memo);
  }

    /** Fills job-scheduling memo values forward. */
private int maxProfitForwardRecursive(List<JobSchedule> jobs, int index, int[] memo) {
    int n = jobs.size();
    if (index >= n) {
      return memo[n - 1];
    }

    int profitIfExcluded = index == 0 ? 0 : memo[index - 1];

    int previousNonOverlappingJobIndex = findPreviousNonOverlappingJob(jobs, index);
    int profitFromPrev = previousNonOverlappingJobIndex >= 0 ? memo[previousNonOverlappingJobIndex] : 0;
    int profitIfIncluded = jobs.get(index).profit + profitFromPrev;

    memo[index] = Math.max(profitIfExcluded, profitIfIncluded);

    return maxProfitForwardRecursive(jobs, index + 1, memo);
  }

    /** Finds the latest compatible job before the current job. */
private int findPreviousNonOverlappingJob(List<JobSchedule> jobs, int currentJobIndex) {
    int left = 0;
    int right = currentJobIndex - 1;
    int latestValidJobIndex = -1;
    int currentJobStartTime = jobs.get(currentJobIndex).startTime;

    while (left <= right) {
      int mid = left + (right - left) / 2;

      // If job at mid ends before or when current job starts, it's valid
      if (jobs.get(mid).endTime <= currentJobStartTime) {
        latestValidJobIndex = mid;
        left = mid + 1; // look for later valid job
      } else {
        right = mid - 1; // current mid job overlaps, look earlier
      }
    }

    return latestValidJobIndex;
  }

  /**
   * Represents a job with start time, end time, and profit
   */
  private static class JobSchedule {
    final int startTime;
    final int endTime;
    final int profit;

    JobSchedule(int startTime, int endTime, int profit) {
      this.startTime = startTime;
      this.endTime = endTime;
      this.profit = profit;
    }
  }
}