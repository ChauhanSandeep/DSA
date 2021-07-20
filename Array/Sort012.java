package Array;

import java.util.Arrays;

public class Sort012 {
    public static void main(String[] args) {
        int[] arr = {2,0,2,1,1,0};
        sort012(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort012(int[] nums) {
        int zeroIndex = 0;
        int oneIndex = 0;
        int twoIndex = nums.length - 1;

        while(oneIndex <= twoIndex) {
            if(nums[oneIndex] == 0) {
                swap(nums, zeroIndex, oneIndex);
                zeroIndex++;
                oneIndex++;
            }else if(nums[oneIndex] == 1) {
                oneIndex++;
            }else {
                swap(nums, twoIndex, oneIndex);
                twoIndex--;
            }
        }
    }

    public static void swap(int[] nums, int i, int j) {
        if(i == j) return;
        nums[i] = nums[i]^nums[j];
        nums[j] = nums[i]^nums[j];
        nums[i] = nums[i]^nums[j];
    }
}
