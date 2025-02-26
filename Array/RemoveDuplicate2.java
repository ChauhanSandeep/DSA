package Array;

import java.util.Arrays;

public class RemoveDuplicate2 {
    public static void main(String[] args) {
        int[] nums = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        int newLength = removeDuplicates(nums);
        
        System.out.println("New Length: " + newLength);
        System.out.println("Modified Array: " + Arrays.toString(Arrays.copyOf(nums, newLength)));
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int insertIndex = 1;
        int occurrenceCount = 1;

        for (int i = 1; i < nums.length; i++) {
            occurrenceCount = (nums[i] == nums[i - 1]) ? occurrenceCount + 1 : 1;

            if (occurrenceCount <= 2) {
                nums[insertIndex++] = nums[i];
            }
        }
        return insertIndex;
    }
}
