package Array;

import java.util.Arrays;

public class RemoveDuplicate2 {
    public static void main(String[] args) {
        int[] nums = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        int newLength = removeDuplicates(nums);

        System.out.println("New Length: " + newLength);
        System.out.println("Modified Array: " + Arrays.toString(Arrays.copyOf(nums, newLength)));
    }

  /**
   * Removes duplicates from a sorted array while allowing at most two occurrences.
   * Modifies the array in-place and returns the new length.
   *
   * Approach:
   * 1. Use a pointer (`insertIndex`) to track where the next valid element should be placed.
   * 2. Use `count` to track occurrences of the current number.
   * 3. If a number appears at most twice, copy it to `insertIndex` and move the pointer.
   *
   * Time Complexity: O(n)  (Iterates through the array once)
   * Space Complexity: O(1)  (Modifies input in-place without extra space)
   *
   * @param nums Sorted array of integers.
   * @return Length of modified array with duplicates removed.
   */
  public static int removeDuplicates(int[] nums) {
    if (nums.length == 0) return 0;

    int insertIndex = 1; // Where the next valid element should be placed
    int count = 1; // Count occurrences of current number

    for (int i = 1; i < nums.length; i++) {
      if (nums[i] == nums[i - 1]) {
        count++; // Same number as before, increase count
      } else {
        count = 1; // New number, reset count
      }

      // Allow at most two occurrences
      if (count <= 2) {
        nums[insertIndex] = nums[i];
        insertIndex++;
      }
    }
    return insertIndex;
  }
}
