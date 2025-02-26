package Array;
import java.util.ArrayList;

public class SumSubarray2 {
    public static void main(String[] args) {
        int arr[] = {1, 1, 1};
        int sum = 2;
        System.out.println(subarraySum(arr, arr.length, sum));
    }

    /**
     * Find the first subarray that sums to `sum`.
     * Uses the Sliding Window approach.
     * Time Complexity: O(N), Space Complexity: O(1)
     */
    public static ArrayList<Integer> subarraySum(int[] arr, int n, int sum) {
        int start = 0, currSum = 0;
        ArrayList<Integer> resultList = new ArrayList<>();

        for (int end = 0; end < n; end++) {
            currSum += arr[end];

            // Shrink the window if the sum exceeds the required sum
            while (currSum > sum && start <= end) {
                currSum -= arr[start];
                start++;
            }

            // If we find the required sum, return 1-based indices
            if (currSum == sum) {
                resultList.add(start + 1); // Convert to 1-based index
                resultList.add(end + 1);
                return resultList;
            }
        }

        resultList.add(-1); // No subarray found
        return resultList;
    }
}
