package DynamicProgramming;

import java.util.*;

/**
 * LeetCode Problem: https://leetcode.com/problems/maximum-profit-in-job-scheduling/
 *
 * Problem Statement:
 * Given `n` jobs, where each job has a start time, end time, and profit,
 * return the maximum profit you can obtain by scheduling non-overlapping jobs.
 * 
 * Approach:
 * - Sort jobs by **end time** to process jobs in order.
 * - Use **Dynamic Programming with Binary Search** to optimize for max profit:
 *   - For each job `i`, we have two choices:
 *     1. Include job `i` → Add its profit to the best profit of the latest non-overlapping job.
 *     2. Exclude job `i` → Take the max profit obtained so far.
 *   - Use **binary search** to efficiently find the latest non-overlapping job.
 * 
 * Complexity:
 * - Sorting: **O(n log n)**
 * - DP with Binary Search: **O(n log n)**
 * - **Overall Complexity: O(n log n)**
 */
public class JobSchedule2 {
    
    public static void main(String[] args) {
        int[] startTime = {1, 2, 3, 3};
        int[] endTime = {3, 4, 5, 6};
        int[] profit = {50, 10, 40, 70};

        int maxProfit = new JobSchedule2().jobScheduling(startTime, endTime, profit);
        System.out.println("Maximum Profit: " + maxProfit);  // Output: 120
    }

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;

        // Step 1: Create Job list and sort by `endTime`
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            jobs.add(new Job(startTime[i], endTime[i], profit[i]));
        }
        jobs.sort(Comparator.comparingInt(job -> job.end));

        // Step 2: DP array to store max profit at each step
        int[] dp = new int[n];
        dp[0] = jobs.get(0).profit;

        for (int i = 1; i < n; i++) {
            int includeProfit = jobs.get(i).profit;

            // Find the last non-overlapping job using binary search
            int lastNonOverlappingJob = findLastNonOverlappingJob(jobs, i);
            if (lastNonOverlappingJob != -1) {
                includeProfit += dp[lastNonOverlappingJob];
            }

            // Step 3: Store the max profit by including or excluding the current job
            dp[i] = Math.max(dp[i - 1], includeProfit);
        }

        return dp[n - 1];  // The maximum profit at the last job index
    }

    /**
     * Uses binary search to find the latest job that doesn't overlap with the current job.
     * @param jobs - List of sorted jobs
     * @param currentIndex - Current job index
     * @return Index of last non-overlapping job, or -1 if none found
     */
    private int findLastNonOverlappingJob(List<Job> jobs, int currentIndex) {
        int left = 0, right = currentIndex - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (jobs.get(mid).end <= jobs.get(currentIndex).start) {
                if (jobs.get(mid + 1).end <= jobs.get(currentIndex).start) {
                    left = mid + 1;
                } else {
                    return mid;
                }
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}

/**
 * Job class representing a single job with start time, end time, and profit.
 */
class Job {
    int start, end, profit;
    public Job(int start, int end, int profit) {
        this.start = start;
        this.end = end;
        this.profit = profit;
    }
}
