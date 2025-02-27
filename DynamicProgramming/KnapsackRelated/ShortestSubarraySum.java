package DynamicProgramming.KnapsackRelated;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Find shortest subarray with sum atleast k
 * https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/discuss/143726/C%2B%2BJavaPython-O(N)-Using-Deque
 */
public class ShortestSubarraySum {

    public static void main(String[] args) {
        int[] arr = {2,-1,2,3};
        int target = 4;
        System.out.println("Min sub array length is " + new ShortestSubarraySum().shortestSubarray(arr, target));
    }

    /**
     * Minimum length subarray whose sum is greater than equal to `target`
     * All integers in array are positive
     */
    public int minSubArrayLen(int target, int[] nums) {
        int j = 0;
        int len = nums.length;
        int result = len + 1;

        for (int i = 0; i < len; i++) { // sliding window
            target -= nums[i];
            while (target <= 0) {
                result = Math.min(result, i - j + 1);
                target += nums[j++];
            }
        }
        return result  == len + 1 ? 0 : result;
    }

    /**
     * Minimum length subarray whose sum is greater than equal to `target`
     * Array integers can be positive or negative
     */
    public int shortestSubarray(int[] nums, int target) {
        int len = nums.length;
        long[] arrSum = new long[len + 1];

        for(int i=0; i<arrSum.length-1; i++) {
            arrSum[i + 1] = arrSum[i] + (long)nums[i];
        }

        int result = len + 1;
        Deque<Integer> queue = new ArrayDeque<>();
        for(int i=0; i<len+1; i++) {
            while(!queue.isEmpty() && arrSum[queue.getLast()] >= arrSum[i]) {
//                last index is negative
                queue.pollLast();
            }
            while(!queue.isEmpty() && arrSum[i] - arrSum[queue.getFirst()] >= target) {
//                a valid subarray
                result = Math.min(result, i - queue.getFirst());
                queue.pollFirst();
            }
            queue.addLast(i);
        }
        return result == len + 1 ? -1 : result;
    }

}
