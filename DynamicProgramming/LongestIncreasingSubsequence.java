package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find the length of longest increasing subsequence in array
 */
public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60, 60};
        int lis = lengthOfLIS(arr);
        System.out.println(lis);
    }

    /**
     *
     * O(n^2) time complexity
     */
    public static int findLIS(int[] arr) {
        if (arr == null || arr.length == 1) return 1;
        int[] dp = new int[arr.length];
        Arrays.fill(dp, 1); // either initiate all element as 1 or return result+1 in the end

        int result = 1;
        for(int i=1; i<arr.length; i++) {
            for(int j=0; j<i; j++) {
                if(arr[j] < arr[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    result = Math.max(result, dp[i]);
                }
            }

        }
        return result;
    }

    /**
     * O(n^2) time complexity in worst case but much efficient generally
     */
    public static int lengthOfLIS(int[] nums) {
        // list size will be ans but list will not contain valid subsequence
        List<Integer> list = new ArrayList<>();
        list.add(nums[0]);

        for(int i= 0; i<nums.length; i++){
            int curr = nums[i];
            if(curr > list.get(list.size() - 1)) {
                list.add(curr);
            }else{
                // Find the first element in list that is greater than or equal to curr
                /*int j = 0;
                while(curr>list.get(j)){ // we can use binary seearch also for O(n log n) time complexity
                    j++;
                }*/
                int j = binarySearch(list, curr);
                list.set(j, curr);
            }
        }

        return list.size();
    }

    private static int binarySearch(List<Integer> sub, int num) {
        int left = 0;
        int right = sub.size() - 1;

        while (left < right) {
            int mid = (left + right) / 2;
            if (sub.get(mid) == num) return mid;

            if (sub.get(mid) < num) left = mid + 1;
            else right = mid;
        }
        return left;
    }
}
