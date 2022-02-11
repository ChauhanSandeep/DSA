package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Need to understand this
 * https://www.interviewbit.com/problems/equal-average-partition/
 */
public class EqualAveragePartition {

    public static void main(String[] args) {
        int[] input = {1,3};
        System.out.println(new EqualAveragePartition().splitArraySameAverage((input)));
    }

    int size;
    public boolean splitArraySameAverage(int[] nums) {
        if(nums == null || nums.length == 0) return false;

        this.size = nums.length;
        Boolean[] dp = new Boolean[size];
        int sum = 0;
        for(Integer num: nums) {
            sum += num;
        }

        double target = (double)sum/size;
        return splitRec(nums, 0, 0, 0, target, dp);
    }

    public boolean splitRec(int[] nums, int currSum, int currCount, int currIndex, double target, Boolean[] dp) {
        if(currIndex >= size) {
            return currCount != 0 && currCount != size && (double) currSum / currCount == target;
        }


//        if(dp[currIndex] != null) return dp[currIndex];

        for(int i=currIndex; i<size; i++) {
            boolean take = splitRec(nums, currSum + nums[currIndex], currCount +1, currIndex+1, target, dp);
            boolean leave = splitRec(nums, currSum, currCount, currIndex + 1, target, dp);
            if(take || leave) {
                dp[currIndex] = true;
                return dp[currIndex];
            }
        }
        dp[currIndex] = false;
        return dp[currIndex];
    }


}
