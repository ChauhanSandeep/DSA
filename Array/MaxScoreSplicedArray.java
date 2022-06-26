package Array;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://leetcode.com/contest/weekly-contest-299/problems/maximum-score-of-spliced-array/
 */
public class MaxScoreSplicedArray {
    public int maximumSplicedArray(int[] nums1, int[] nums2) {
        int sum1= 0;
        int sum2 = 0;

        for (int num: nums1) sum1 += num;
        for (int num: nums2) sum2 += num;

        int result = Math.max(sum1, sum2);

        int maxSoFar = 0;
        int max = 0;
        for (int i = 0; i < nums1.length; i++) {
            maxSoFar +=(nums2[i] - nums1[i]);
            max = Math.max(max, maxSoFar);
            if (maxSoFar < 0) maxSoFar = 0;
        }

        result = Math.max(result, (sum1 + max));
        maxSoFar= 0;
        max = 0;

        for (int i = 0; i < nums1.length; i++) {
            maxSoFar += (nums1[i] - nums2[i]);
            max = Math.max(max, maxSoFar);
            if (maxSoFar < 0) maxSoFar = 0;
        }

        result = Math.max(result, sum2 + max);
        return result;
    }
}
