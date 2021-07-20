package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {

    public static void main(String[] args) {
        int[][] intervals = {
                {1,3},
                {2,6},
                {8,10},
                {15,18}
        };
        int[][] mergedIntervals = merge(intervals);
        System.out.println(Arrays.deepToString(mergedIntervals));
    }
    public static int[][] merge(int[][] intervals) {
        if(intervals == null || intervals.length <= 1) return intervals;

        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> resList = new ArrayList<>();
        int start = intervals[0][0];
        int end = intervals[0][1];

        for(int[] interval : intervals) {
            if(interval[0] <= end) {
                end = Math.max(end, interval[1]);
            }else{
                resList.add(new int[] {start, end});
                start = interval[0];
                end = interval[1];
            }
        }

        resList.add(new int[] {start, end});
        int[][] result = new int[resList.size()][2];
        for(int i=0; i<resList.size(); i++) {
            result[i][0] = resList.get(i)[0];
            result[i][1] = resList.get(i)[1];
        }
        return result;
    }
}
