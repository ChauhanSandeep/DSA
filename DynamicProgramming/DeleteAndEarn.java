package DynamicProgramming;

/**
 * You are given an integer array nums. You want to maximize the number of points you get by performing the following operation any number of times:
 * Pick any nums[i] and delete it to earn nums[i] points. Afterwards, you must delete every element equal to nums[i] - 1 and every element equal to nums[i] + 1.
 * Return the maximum number of points you can earn by applying the above operation some number of times.
 * https://leetcode.com/problems/delete-and-earn/
 */
public class DeleteAndEarn {
    public static void main(String[] args) {
        int[] arr = {10000};
        int result = new DeleteAndEarn().deleteAndEarn(arr);
        System.out.println(result);
    }

    public int deleteAndEarn(int[] nums) {

        int[] count = new int[10001];
        for(int num: nums) {
            count[num]++;
        }

        int[] dp = new int[10001];
        dp[0] = 0;
        dp[1] = count[1];

        for(int i=2; i<dp.length; i++) {
            dp[i] = Math.max(i * count[i] + dp[i-2], dp[i-1]);
        }
        return dp[10000];
    }

}
