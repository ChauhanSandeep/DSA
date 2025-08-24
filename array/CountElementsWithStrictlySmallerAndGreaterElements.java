package array;

import java.util.*;

/**
 * Count Elements With Strictly Smaller And Greater Elements
 *
 * Problem: Count elements that have at least one strictly smaller and one strictly greater element.
 *
 * Example: nums = [11,7,2,15] -> Output: 2
 * Elements 7 and 11 satisfy the condition. 2 has no smaller, 15 has no greater.
 *
 * LeetCode: https://leetcode.com/problems/count-elements-with-strictly-smaller-and-greater-elements
 *
 * Follow-up Questions:
 * - How to find the actual elements instead of count? (Store elements in result list)
 * - What if we want elements with at least k smaller/greater? (Modify counting condition)
 * - Can we solve without finding min/max? (Use sorting approach)
 */
public class CountElementsWithStrictlySmallerAndGreaterElements {

    /**
     * Counts elements that have both strictly smaller and greater elements.
     *
     * Algorithm:
     * 1. Find minimum and maximum values in the array
     * 2. Count elements that are neither minimum nor maximum
     * 3. Elements between min and max (exclusive) satisfy the condition
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums input array of integers
     * @return count of elements with strictly smaller and greater elements
     */
    public int countElements(int[] nums) {
        if (nums.length <= 2) return 0;

        int min = Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(nums).max().orElse(Integer.MIN_VALUE);

        // If all elements are same, no element satisfies condition
        if (min == max) return 0;

        int count = 0;
        for (int num : nums) {
            if (num > min && num < max) {
                count++;
            }
        }

        return count;
    }

    /**
     * Manual min/max finding approach (more explicit)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int countElementsManual(int[] nums) {
        if (nums.length <= 2) return 0;

        int min = nums[0];
        int max = nums[0];

        // Find min and max in single pass
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        if (min == max) return 0;

        // Count elements strictly between min and max
        int count = 0;
        for (int num : nums) {
            if (num > min && num < max) {
                count++;
            }
        }

        return count;
    }

    /**
     * Set-based approach for verification
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int countElementsSet(int[] nums) {
        if (nums.length <= 2) return 0;

        Set<Integer> uniqueValues = new HashSet<>();
        for (int num : nums) {
            uniqueValues.add(num);
        }

        if (uniqueValues.size() <= 2) return 0;

        int min = Collections.min(uniqueValues);
        int max = Collections.max(uniqueValues);

        int count = 0;
        for (int num : nums) {
            if (num > min && num < max) {
                count++;
            }
        }

        return count;
    }
}
