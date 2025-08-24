package array;

import java.util.*;

/**
 * Remove Duplicates From Sorted Array
 *
 * Problem: Remove duplicates from sorted array in-place. Return new length.
 * Each unique element should appear only once, maintaining relative order.
 *
 * Example: nums = [1,1,2] -> Output: 2, nums = [1,2,_]
 * Remove duplicates so nums becomes [1,2] with length 2.
 *
 * LeetCode: https://leetcode.com/problems/remove-duplicates-from-sorted-array
 *
 * Follow-up Questions:
 * - How to remove duplicates from unsorted array? (Use set or sort first)
 * - What if each element can appear at most k times? (Generalize comparison logic)
 * - Can we solve without extra space? (Current solution is already O(1) space)
 */
public class RemoveDuplicatesFromSortedArray {

    /**
     * Removes duplicates from sorted array using two-pointer technique.
     *
     * Algorithm:
     * 1. Use slow pointer for position of next unique element
     * 2. Use fast pointer to scan through array
     * 3. When new unique element found, place it at slow pointer position
     * 4. Return slow pointer + 1 as new array length
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums sorted array with duplicates
     * @return new length after removing duplicates
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length <= 1) return nums.length;

        int slow = 0; // Position for next unique element

        for (int fast = 1; fast < nums.length; fast++) {
            // Found new unique element
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }

        return slow + 1;
    }

    /**
     * Alternative implementation with clearer variable names
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeDuplicatesAlternative(int[] nums) {
        if (nums.length == 0) return 0;

        int writeIndex = 0;

        for (int readIndex = 0; readIndex < nums.length; readIndex++) {
            // Write element if it's first occurrence
            if (writeIndex == 0 || nums[readIndex] != nums[writeIndex - 1]) {
                nums[writeIndex] = nums[readIndex];
                writeIndex++;
            }
        }

        return writeIndex;
    }

    /**
     * Enhanced version that returns the actual unique elements
     * Time Complexity: O(n), Space Complexity: O(n) for result array
     */
    public int[] removeDuplicatesReturnArray(int[] nums) {
        if (nums.length == 0) return new int[0];

        List<Integer> unique = new ArrayList<>();
        unique.add(nums[0]);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                unique.add(nums[i]);
            }
        }

        return unique.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Generic version allowing at most k duplicates
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int removeDuplicatesAtMostK(int[] nums, int k) {
        if (nums.length <= k) return nums.length;

        int writeIndex = k;

        for (int readIndex = k; readIndex < nums.length; readIndex++) {
            // Allow element if it's different from element k positions back
            if (nums[readIndex] != nums[writeIndex - k]) {
                nums[writeIndex] = nums[readIndex];
                writeIndex++;
            }
        }

        return writeIndex;
    }

    /**
     * Set-based approach for comparison (works for unsorted arrays too)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int removeDuplicatesUsingSet(int[] nums) {
        Set<Integer> seen = new LinkedHashSet<>();

        for (int num : nums) {
            seen.add(num);
        }

        int index = 0;
        for (int num : seen) {
            nums[index++] = num;
        }

        return seen.size();
    }

    /**
     * Brute force approach for verification (less efficient)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int removeDuplicatesBruteForce(int[] nums) {
        if (nums.length == 0) return 0;

        int uniqueCount = 1;

        for (int i = 1; i < nums.length; i++) {
            boolean isDuplicate = false;

            // Check if current element appears before
            for (int j = 0; j < uniqueCount; j++) {
                if (nums[i] == nums[j]) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                nums[uniqueCount] = nums[i];
                uniqueCount++;
            }
        }

        return uniqueCount;
    }

    /**
     * Stream-based approach for functional programming style
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int removeDuplicatesStream(int[] nums) {
        int[] unique = Arrays.stream(nums).distinct().toArray();

        // Copy back to original array
        System.arraycopy(unique, 0, nums, 0, unique.length);

        return unique.length;
    }
}
