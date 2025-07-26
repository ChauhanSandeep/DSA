package DynamicProgramming;

import java.util.*;


/**
 * Maximum Profit in Job Scheduling
 *
 * Problem Statement:
 * We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i],
 * obtaining a profit of profit[i]. You're given the startTime, endTime and profit arrays,
 * return the maximum profit you can take such that there are no two jobs in the subset with overlapping time range.
 * If you choose a job that ends at time X you will be able to start another job that starts at time X.
 *
 * Example:
 * Input:
 * startTime = [1, 2, 3, 3],
 * endTime =   [3, 4, 5, 6],
 * profit =    [50,10,40,70]
 * Output: 120
 * Explanation: The subset chosen is the first and fourth job.
 * Time range [1-3]+[3-6], we get profit of 120 = 50 + 70.
 *
 * LeetCode: https://leetcode.com/problems/maximum-profit-in-job-scheduling/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we want to find the actual jobs selected?
 *  - Maintain a parent array during DP to backtrack
 * 2. What if jobs have different priorities?
 *  - Modify profit calculation to include priority weights
 * 3. How to handle real-time job scheduling?
 *  - Use segment trees or interval trees for dynamic updates
 * 4. Memory optimization for large datasets?
 *  - Use space-optimized DP or coordinate compression
 */
public class MaximumProfitJobScheduling {

  public static void main(String[] args) {
    int[] startTime = {1, 2, 3, 3};
    int[] endTime = {3, 4, 5, 6};
    int[] profit = {50, 10, 40, 70};

    MaximumProfitJobScheduling scheduler = new MaximumProfitJobScheduling();
    int maxProfit = scheduler.jobScheduling(startTime, endTime, profit);
    System.out.println("Maximum Profit: " + maxProfit);  // Output: 120

    int optimizedProfit = scheduler.jobSchedulingOptimized(startTime, endTime, profit);
    System.out.println("Optimized Maximum Profit: " + optimizedProfit);  // Output: 120
  }

  /**
   * Finds maximum profit from non-overlapping jobs using Dynamic Programming with Binary Search
   *
   * Algorithm: Dynamic Programming with Binary Search
   * Steps:
   * 1. Create job objects and sort by end time for processing in chronological order
   * 2. Use DP where dp[i] represents maximum profit achievable up to job i
   * 3. For each job, choose max between including current job or excluding it
   * 4. Use binary search to efficiently find latest non-overlapping job
   *
   * Time Complexity: O(n log n) - sorting + binary search for each job
   * Space Complexity: O(n) - for jobs list and DP array
   *
   * @param startTime array of job start times
   * @param endTime array of job end times
   * @param profit array of job profits
   * @return maximum profit achievable from non-overlapping jobs
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
   * Uses binary search to find the latest job that doesn't overlap with current job.
   * This finds non overlapping job on the left side of currentJobIndex.
   *
   * @param jobs sorted list of jobs by end time
   * @param currentJobIndex index of current job being processed
   * @return index of latest non-overlapping job, or -1 if none exists
   */
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