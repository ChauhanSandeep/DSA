package Array;

import java.util.Arrays;

/**
 * Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].
 */
public class ProductExceptSelf {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4};
        System.out.println(Arrays.toString(productExceptSelf2(nums)));
    }

    /**
     * Without using division
     */
    public static int[] productExceptSelf1(int[] nums) {
        int len = nums.length;

        int[] left = new int[len];
        int[] right = new int[len];

        left[0] = 1;
        for(int i=1; i<len; i++) {
            left[i] = left[i-1] * nums[i-1];
        }

        right[len-1] = 1;
        for(int i=len-2; i>=0; i--) {
            right[i] = right[i+1] * nums[i+1];
        }
        // System.out.println(Arrays.toString(left));
        // System.out.println(Arrays.toString(right));

        int[] result = new int[len];
        for(int i=0; i<len; i++) {
            result[i] = left[i] * right[i];
        }
        return result;
    }

    /**
     * Without using division and no extra space
     */
    public static int[] productExceptSelf2(int[] nums) {
        int len = nums.length;

        int[] result = new int[len];
        result[0] = 1;
        for(int i=1; i<len; i++) {
            result[i] = nums[i-1] * result[i-1];
        }

        int rightMul = 1;
        for(int i=len-2; i>=0; i--) {
            rightMul = rightMul * nums[i+1];
            result[i] = rightMul * result[i];
        }
        return result;
    }

}
