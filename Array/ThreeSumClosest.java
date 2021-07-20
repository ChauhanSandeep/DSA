package Array;

import java.util.Arrays;

public class ThreeSumClosest {

    public static void main(String[] args) {
        int[] arr = {-1,2,1,-4};
        int target = 1;
        System.out.println("Three sum closest to target is " + threeSumClosest(arr, target));
    }

    /**
     * Given an array and target, find the target that can be achieved by adding 3 numbers which is closest to provided target
     */
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int len = nums.length;
        int minDiff = Integer.MAX_VALUE;
        int result = 0;
        for (int i=0; i<len; i++) {
            int j=i+1;
            int k=len-1;

            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum == target) return sum;
                else if (sum < target) j++;
                else k--;

                if (Math.abs(target - sum) < minDiff) {
                    minDiff = Math.abs(target - sum);
                    result = sum;
                }
            }
        }

        return result;
    }
}
