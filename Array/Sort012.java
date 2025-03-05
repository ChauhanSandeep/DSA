package Array;

import java.util.Arrays;

public class Sort012 {
    public static void main(String[] args) {
        int[] arr = {2, 0, 2, 1, 1, 0};
        sort012(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort012(int[] nums) {
        int zeroIndex = 0;
        int currentIndex = 0;
        int rightBoundary = nums.length - 1;

        while (currentIndex <= rightBoundary) {
            if (nums[currentIndex] == 0) {
                swap(nums, zeroIndex, currentIndex);
                zeroIndex++;
                currentIndex++;
            } else if (nums[currentIndex] == 1) {
                currentIndex++;
            } else {
                swap(nums, rightBoundary, currentIndex);
                rightBoundary--;  // Do NOT increase `currentIndex` because the swapped element needs checking
            }
        }
    }

    public static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
