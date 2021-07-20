package Array;

import java.util.Arrays;

public class JumpGame2 {
    public static void main(String[] args) {
        int[] nums = {2,3,1,1,4};
        int jumps = jump(nums);
        System.out.println("Min jumps required to reach end is " + jumps);
    }


    /**
     * Given an array of non-negative integers nums, you are initially positioned at the first index of the array.
     * Each element in the array represents your maximum jump length at that position.
     * Your goal is to reach the last index in the minimum number of jumps.
     * You can assume that you can always reach the last index.
     */
    public static int jump(int[] nums) {
        int[] arr = new int[nums.length];
        Arrays.fill(arr, nums.length + 1);
        arr[nums.length-1] = 0;
        for(int index= nums.length - 2; index>=0; index--) {
            int maxJump = nums[index];
            for(int jump=0; jump<=maxJump; jump++){
                if(index + jump >= nums.length) {
                    arr[index] = 1;
                }else if (arr[index + jump] + 1 < arr[index]) {
                    arr[index] = arr[index + jump] + 1;
                }
            }
        }
        return arr[0];
    }
}
