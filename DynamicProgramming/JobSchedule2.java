package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i],
 * obtaining a profit of profit[i].
 *
 * You're given the startTime, endTime and profit arrays, return the maximum profit you can
 * take such that there are no two jobs in the subset with overlapping time range.
 */
public class JobSchedule2 {
    public static void main(String[] args) {
        int[] startTime = {1, 2, 3, 3};
        int[] endTime = {3, 4, 5, 6};
        int[] profit = {50, 10, 40, 70};

        int maxProfit = new JobSchedule2().jobScheduling(startTime, endTime, profit);
        System.out.println(maxProfit);
    }

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int len = startTime.length;

        List<Interval> intervalList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            intervalList.add(new Interval(startTime[i], endTime[i], profit[i]));
        }

        Collections.sort(intervalList, (a, b) -> {
            if (a.start == b.start) return a.end - b.end;
            return a.start - b.start;
        });

        int[] dp = new int[startTime.length];
        Arrays.fill(dp, -1);
        return findProfit(intervalList, 0, dp);

    }

    public int findProfit(List<Interval> intervalList, int startIndex, int[] dp) {
        if (startIndex >= intervalList.size()) return 0;
        if (startIndex == intervalList.size() - 1) return intervalList.get(startIndex).profit;
        if (dp[startIndex] != -1) return dp[startIndex];

        int nextStart = findNextInterval(intervalList, startIndex);
        dp[startIndex] = Math.max(intervalList.get(startIndex).profit + findProfit(intervalList, nextStart, dp),
                findProfit(intervalList, startIndex + 1, dp));

        return dp[startIndex];
    }

    public int findNextInterval(List<Interval> intervalList, int currentIndex) {
        int i = currentIndex ;
        for (; i < intervalList.size(); i++) {
            if (intervalList.get(i).start >= intervalList.get(currentIndex).end) return i;
        }
        return i;
    }
}

class Interval {
    int start;
    int end;
    int profit;

    public Interval(int start, int end, int profit) {
        this.start = start;
        this.end = end;
        this.profit = profit;
    }
}
