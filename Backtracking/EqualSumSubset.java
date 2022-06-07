package Backtracking;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Check if its possible to divide integer array into k subsets with equal sum
 */
public class EqualSumSubset {
    public static void main(String[] args) {
        int[] nums = {2, 2, 2, 2, 3, 4, 5};
        int k = 4;
        boolean result = new EqualSumSubset().canPartitionKSubsets(nums, k);
        System.out.println(result);

        nums = new int[]{2, 3, 1, 2, 3, 4, 5};
        result = new EqualSumSubset().canPartitionKSubsets(nums, k);
        System.out.println(result);
    }

    private boolean backtrack(int[] arr, int index, int currCount, int currSum, int targetCount,
                              int targetSum, boolean[] taken, HashMap<String, Boolean> memo) {
        int len = arr.length;

        if (currCount == targetCount - 1) return true;
        if (currSum > targetSum) return false;

        String takenStr = createKey(taken);
        if (memo.containsKey(takenStr)) return memo.get(takenStr);

        if (currSum == targetSum) {
            boolean ans = backtrack(arr, 0, currCount + 1, 0, targetCount, targetSum, taken, memo);
            memo.put(takenStr, ans);
            return ans;
        }

        // Try not picked elements to make some combinations.
        for (int i = index; i < len; ++i) {
            if (!taken[i]) {
                taken[i] = true;
                if (backtrack(arr, i + 1, currCount, currSum + arr[i], targetCount, targetSum, taken, memo)) {
                    memo.put(takenStr, true);
                    return true;
                }
                taken[i] = false;
            }
        }
        memo.put(takenStr, false);
        return false;
    }

    private String createKey(boolean[] taken) {
        StringBuilder builder = new StringBuilder();
        for (boolean b : taken) {
            builder.append(b ? '1' : '0');
        }
        return builder.toString();
    }

    private void reverse(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        int len = nums.length;

        for (int i = 0; i < len; ++i) {
            sum += nums[i];
        }

        if (sum % k != 0) {
            return false;
        }

        // Sort in decreasing order.
        Arrays.sort(nums);
        reverse(nums);

        int targetSum = sum / k;

        boolean[] taken = new boolean[len];
        HashMap<String, Boolean> memo = new HashMap<>();

        return backtrack(nums, 0, 0, 0, k, targetSum, taken, memo);
    }
}
