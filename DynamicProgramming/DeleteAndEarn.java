package DynamicProgramming;

/**
 * You are given an integer array nums. You want to maximize the number of points you get by performing the following operation any number of times:
 * Pick any nums[i] and delete it to earn nums[i] points. Afterwards, you must delete every element equal to nums[i] - 1 and every element equal to nums[i] + 1.
 * Return the maximum number of points you can earn by applying the above operation some number of times.
 * https://leetcode.com/problems/delete-and-earn/
 */
public class DeleteAndEarn {
    public static void main(String[] args) {
        int[] arr = {3, 4, 2};
        int result = new DeleteAndEarn().deleteAndEarn(arr);
        System.out.println(result);
    }

    public int deleteAndEarn(int[] nums) {
        int[] numValue = new int[10001];
        int[] dp = new int[10003];

        for(int num: nums) {
            numValue[num] += num;
        }

        for(int i=10000; i>=0; i--) {
            dp[i] = Math.max(dp[i+1],           // not take current element
                    numValue[i] + dp[i+2]);     // take current element
        }
        return dp[0];
    }

}
