package StackQueue;

import java.util.LinkedList;

/**
 * https://leetcode.com/problems/count-number-of-nice-subarrays/
 */
public class NiceSubarray {

    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 1, 1};
        int k = 3;
        System.out.println(new NiceSubarray().numberOfSubarrays(arr, k));
    }


    public int numberOfSubarrays(int[] nums, int k) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(-1);

        int result = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 != 0) {
                queue.offer(i);
            }
            while (queue.size() > k + 1) {
                queue.poll();
            }
            if (queue.size() == k + 1) {
                int start = queue.get(1); // starting of the current subarray
                int prev = queue.get(0); // index of odd number before `start`
                // current subarray include the subarrays starting between `prev` and `start`
                result += start - prev;
            }
        }
        return result;
    }
}
