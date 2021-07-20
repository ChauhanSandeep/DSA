package Array;

import java.util.Arrays;

public class BinarySearchRange {
    public static void main(String[] args) {
        int[] nums = {5,7,7,8,8,10};
        int target = 8;
        System.out.println("Start and end index for the target is " + Arrays.toString(searchRange(nums, target)));

    }

    /**
     * Find start and end index of a number in sorted array.
     * @param nums
     * @param target
     * @return
     */
    public static int[] searchRange(int[] nums, int target) {
        int[] result = new int[2];
        int first = findFirst(nums, target);
        int last = findLast(nums, target);
        result[0] = first;
        result[1] = last;
        return result;

    }

    public static int findFirst(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        int result = -1;

        while(start<=end) {
            int mid = (start+end)/2;

            if(nums[mid] < target) {
                start = mid+1;
            }else if(nums[mid] > target) {
                end = mid - 1;
            }else{
                end = mid-1; // even if we find the element, search left side
                result = mid;
            }
        }
        return result;
    }

    public static int findLast(int[] nums, int target) {
        int start = 0;
        int end = nums.length -1;
        int result = -1;

        while(start<=end) {
            int mid = (start+end)/2;

            if(nums[mid] < target) {
                start = mid+1;
            }else if(nums[mid] > target) {
                end = mid - 1;
            }else{
                start = mid+1; // even if we find element, search right side
                result = mid;
            }
        }
        return result;
    }
}
