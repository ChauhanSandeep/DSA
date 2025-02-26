package Backtracking;

import java.util.Arrays;

public class EqualSumSubset {
    public static void main(String[] args) {
        int[] nums = {2, 2, 2, 2, 3, 4, 5};
        int k = 4;
        System.out.println(canPartitionKSubsets(nums, k)); // Output: true

        nums = new int[]{2, 3, 1, 2, 3, 4, 5};
        System.out.println(canPartitionKSubsets(nums, k)); // Output: false
    }

    public static boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();

        if (sum % k != 0) return false;

        int targetSum = sum / k;
        Arrays.sort(nums);
        reverse(nums); // Sort in descending order

        boolean[] visited = new boolean[nums.length];
        return backtrack(nums, visited, 0, 0, k, targetSum);
    }

    private static boolean backtrack(int[] nums, boolean[] visited, int startIdx, int currSum, int remainingBuckets, int targetSum) {
        if (remainingBuckets == 1) return true; // Last subset must be valid if others are

        if (currSum == targetSum) {
            return backtrack(nums, visited, 0, 0, remainingBuckets - 1, targetSum);
        }

        for (int i = startIdx; i < nums.length; i++) {
            if (visited[i] || currSum + nums[i] > targetSum) continue;

            visited[i] = true;
            if (backtrack(nums, visited, i + 1, currSum + nums[i], remainingBuckets, targetSum)) {
                return true;
            }
            visited[i] = false; // Backtrack
        }

        return false;
    }

    private static void reverse(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}

