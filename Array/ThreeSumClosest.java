package Array;

import java.util.Arrays;

public class ThreeSumClosest {
    public static void main(String[] args) {
        int[] arr = {-1, 2, 1, -4};
        int target = 1;
        System.out.println("Three sum closest to target is " + threeSumClosest(arr, target));
    }

    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int len = nums.length;
        int minDiff = Integer.MAX_VALUE;
        int result = 0;

        for (int i = 0; i < len - 2; i++) { // Fix off-by-one error
            int j = i + 1, k = len - 1;

            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];

                // If sum matches the target exactly, return immediately
                if (sum == target) return sum;

                // Update result if closer to target
                int diff = Math.abs(target - sum);
                if (diff < minDiff) {
                    minDiff = diff;
                    result = sum;
                }

                // Move pointers accordingly
                if (sum < target) j++;
                else k--;
            }
        }

        return result;
    }
}
