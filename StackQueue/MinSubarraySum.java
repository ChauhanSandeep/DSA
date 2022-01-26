package StackQueue;

import java.util.Stack;

/**
 * https://leetcode.com/problems/sum-of-subarray-minimums/
 */
public class MinSubarraySum {

    public static void main(String[] args) {
        int[] nums = {3,5,4,12,8,10, 11,4};
        //            0,1,2,3, 4, 5, 6,7
        System.out.println(new MinSubarraySum().sumSubarrayMins(nums));
    }

    public int sumSubarrayMins(int[] nums) {
        int len = nums.length;
        long mod = (long)1e9 + 7;
        long result = 0;

        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int curr;
        int prevSmaller;

        for (int i = 0; i < len; i++) {
            while (stack.peek() != -1 && nums[stack.peek()] > nums[i]) {
                int nextSmaller = i;
                curr = stack.pop();
                prevSmaller = stack.peek();

                result = (result + (long)nums[curr] * (nextSmaller - curr) * (curr - prevSmaller)) % mod;
            }
            stack.push(i);
        }

        while(stack.peek() != -1) {
            int nextSmaller = len;
            curr = stack.pop();
            prevSmaller = stack.peek();
            result = (result + (long)nums[curr] * (nextSmaller - curr) * (curr - prevSmaller)) % mod;
        }
        return (int)result;
    }

}
