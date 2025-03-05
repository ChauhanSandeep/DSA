package Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SumSubarray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("For positive elements: " + Arrays.toString(findSubarrayWithSum(arr, 12)));
        System.out.println("For any elements: " + Arrays.toString(findSubarrayWithSum2(arr, 12)));
    }

    /**
     * Find a subarray that sums to `sum` when all elements are positive.
     * Uses the Sliding Window (Two Pointer) technique.
     * Time Complexity: O(N)
     */
    public static int[] findSubarrayWithSum(int[] arr, int sum) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("The array is invalid");
        }

        int start = 0, currSum = 0;

        for (int end = 0; end < arr.length; end++) {
            currSum += arr[end];

            // Shrink the window if sum exceeds target
            while (currSum > sum && start <= end) {
                currSum -= arr[start++];
            }

            // Check if we found the sum
            if (currSum == sum) {
                return new int[]{start, end}; // Returns 0-based index
            }
        }
        return new int[]{-1, -1}; // No valid subarray found
    }

    /**
     * Find a subarray that sums to `sum` when elements can be positive or negative.
     * Uses HashMap to store prefix sums.
     * Time Complexity: O(N)
     */
    public static int[] findSubarrayWithSum2(int[] arr, int sum) {
        Map<Integer, Integer> sumIndexMap = new HashMap<>();
        int currSum = 0;

        // Base case: if subarray starts from index 0
        sumIndexMap.put(0, -1);

        for (int i = 0; i < arr.length; i++) {
            currSum += arr[i];

            // Check if a subarray with sum exists
            if (sumIndexMap.containsKey(currSum - sum)) {
                return new int[]{sumIndexMap.get(currSum - sum) + 1, i}; // Returns 0-based index
            }

            // Store the prefix sum
            sumIndexMap.put(currSum, i);
        }
        return new int[]{-1, -1}; // No valid subarray found
    }
}
