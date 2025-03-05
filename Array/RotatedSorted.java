package Array;

public class RotatedSorted {
    public static void main(String[] args) {
        int[] arr = {6, 7, 0, 1, 2, 4, 5};
        int target = 2;
        int index = search(arr, target);
        System.out.println("Target found at index: " + index);
    }

    /**
     * Given a sorted array which is rotated, find the index of target number
     * @param nums - Rotated sorted array
     * @param target - Number to search
     * @return Index of target in nums, or -1 if not found
     */
    public static int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevents integer overflow

            if (nums[mid] == target) return mid;

            // Check if left half is sorted
            if (nums[left] <= nums[mid]) { 
                if (target >= nums[left] && target < nums[mid]) { 
                    right = mid - 1; // Target is in left half
                } else {
                    left = mid + 1; // Search right half
                }
            } 
            // Otherwise, right half must be sorted
            else { 
                if (target > nums[mid] && target <= nums[right]) { 
                    left = mid + 1; // Target is in right half
                } else {
                    right = mid - 1; // Search left half
                }
            }
        }
        return -1; // Target not found
    }
}
