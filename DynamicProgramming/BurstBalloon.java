package DynamicProgramming;

/**
 * You are given n balloons, indexed from 0 to n - 1. Each balloon is painted with a number
 * on it represented by an array nums. You are asked to burst all the balloons.
 *
 * If you burst the ith balloon, you will get nums[i - 1] * nums[i] * nums[i + 1] coins.
 * If i - 1 or i + 1 goes out of bounds of the array, then treat it as if there is a
 * balloon with a 1 painted on it. Return max coins that can be collected
 *
 * https://www.youtube.com/watch?v=YzvF8CqPafI
 */
public class BurstBalloon {

    public static void main(String[] args) {
        int[] arr = {3,1,5,8};
        System.out.println(new BurstBalloon().maxGain(arr));
    }

    public int maxGain(int[] arr) {
        int[][] dp = new int[arr.length][arr.length];

        for (int gap = 0; gap < dp.length; gap++) {

            for (int left = 0, right = gap; right < dp.length; left++, right++) {
                int gain = Integer.MIN_VALUE;

                for (int curr = left; curr <= right; curr++) {
                    int leftGain = curr == left ? 0 : dp[left][curr - 1];
                    int rightGain = curr == right ? 0 : dp[curr + 1][right];
                    int val = (left == 0 ? 1 : arr[left - 1]) * arr[curr] * (right == arr.length - 1 ? 1 : arr[right + 1]);

                    int tempGain = leftGain + rightGain + val;
                    gain = Math.max(gain, tempGain);
                }
                dp[left][right] = gain;
            }
        }
        return dp[0][dp.length - 1];
    }
}
