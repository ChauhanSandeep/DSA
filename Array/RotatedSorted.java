package Array;

public class RotatedSorted {
    public static void main(String[] args) {
        int[] arr = {6,7,0,1,2,4,5};
        int target = 2;
        int index = search(arr, target);
        System.out.println(index);
    }

    /**
     * Given a sorted array which is rotated, find the index of target number
     * @param nums
     * @param target
     * @return
     */
    public static int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while(left <= right) {
            int mid = (left + right)/2;
            if(nums[mid] == target) return mid;

            else if (nums[mid] <= nums[right] && target >= nums[mid] && target <= nums[right]) {
                left = mid+1;
            }
            else if(nums[mid] >= nums[left] && target >= nums[left] && target <= nums[mid]) {
                right = mid-1;
            }
            else if (nums[right] <= nums[mid]) {
                left = mid+1;
            }
            else {
                right = mid-1;
            }
        }
        return -1;
    }
}
