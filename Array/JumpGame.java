package Array;

public class JumpGame {

    /**
     * Given an array of non-negative integers nums, you are initially positioned at the first index of the array.
     * Each element in the array represents your maximum jump length at that position.
     * Determine if you are able to reach the last index.
     */
    public static void main(String[] args) {
        int[] nums = {2,3,1,1,4};
        System.out.println("Can jump the array? " + canJumpOptimum(nums));
    }


    /**
     * We are finding maxJump from a index and validate if we can jump to an index from which end is reachable
     * This is not optimum.
     */
    public static boolean canJump(int[] nums) {
        boolean[] arr = new boolean[nums.length];
        arr[nums.length-1] = true;
        for(int index= nums.length - 2; index>=0; index--) {
            int maxJump = nums[index];
            for(int jump=0; jump<=maxJump; jump++){
                if (index + jump >= nums.length || arr[index + jump] == true) {
                    arr[index] = true;
                    break;
                }
            }
        }
        return arr[0];
    }

    /**
     * This is much better solution.
     * we are checking if we can jump more or equal to lastGoodIndex from current index.
     * https://www.youtube.com/watch?v=Zb4eRjuPHbM
     */
    public static boolean canJumpOptimum(int[] nums) {
        int lastGoodIndex = nums.length-1;

        for(int i= nums.length-2; i>=0; i--) {
            if(i+nums[i] >= lastGoodIndex) lastGoodIndex = i;
        }
        return lastGoodIndex == 0;
    }
}
