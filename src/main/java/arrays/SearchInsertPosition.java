package arrays;

import java.util.Arrays;


/**
 * Search Insert Position
 *
 * Problem: Find index where target should be inserted in sorted array to maintain order.
 * Array contains distinct values.
 *
 * Example: nums = [1,3,5,6], target = 5 -> Output: 2
 * Target 5 is found at index 2.
 *
 * Example: nums = [1,3,5,6], target = 2 -> Output: 1
 * Target 2 should be inserted at index 1.
 *
 * LeetCode: https://leetcode.com/problems/search-insert-position
 *
 * Follow-up Questions:
 * - What if array contains duplicates? (Find leftmost or rightmost insertion point)
 * - How to handle floating point targets? (Same binary search logic)
 * - What if we want to find all possible insertion positions? (Handle duplicate ranges)
 */
public class SearchInsertPosition {

    /**
     * Finds insertion position using binary search.
     *
     * Algorithm:
     * 1. Use binary search to find target or insertion point
     * 2. If target found, return its index
     * 3. If not found, left pointer indicates insertion position
     * 4. Maintain invariant: left always points to insertion position
     *
     * Time Complexity: O(log n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums sorted array of distinct integers
     * @param target value to search or insert
     * @return index where target is found or should be inserted
     */
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // When loop ends, left is the insertion position
        return left;
    }

    /**
     * Alternative implementation with explicit insertion logic
     * Time Complexity: O(log n), Space Complexity: O(1)
     */
    public int searchInsertExplicit(int[] nums, int target) {
        int left = 0;
        int right = nums.length;

        // Find leftmost position where nums[i] >= target
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    /**
     * Recursive binary search approach
     * Time Complexity: O(log n), Space Complexity: O(log n) due to recursion
     */
    public int searchInsertRecursive(int[] nums, int target) {
        return binarySearchRecursive(nums, target, 0, nums.length - 1);
    }

    private int binarySearchRecursive(int[] nums, int target, int left, int right) {
        if (left > right) {
            return left; // Insertion position
        }

        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            return binarySearchRecursive(nums, target, mid + 1, right);
        } else {
            return binarySearchRecursive(nums, target, left, mid - 1);
        }
    }

    /**
     * Linear search approach (less efficient but simpler)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int searchInsertLinear(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }

        // Target should be inserted at end
        return nums.length;
    }

    /**
     * Using built-in binary search for comparison
     * Time Complexity: O(log n), Space Complexity: O(1)
     */
    public int searchInsertBuiltIn(int[] nums, int target) {
        int index = Arrays.binarySearch(nums, target);

        if (index >= 0) {
            return index; // Found
        } else {
            return -(index + 1); // Convert negative insertion point
        }
    }

    /**
     * Generic version that works with any comparable type
     * Time Complexity: O(log n), Space Complexity: O(1)
     */
    public <T extends Comparable<T>> int searchInsertGeneric(T[] nums, T target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = nums[mid].compareTo(target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    /**
     * Version handling duplicate elements (finds leftmost insertion point)
     * Time Complexity: O(log n), Space Complexity: O(1)
     */
    public int searchInsertLeftmost(int[] nums, int target) {
        int left = 0;
        int right = nums.length;

        // Find leftmost position where nums[i] >= target
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    /**
     * Version handling duplicate elements (finds rightmost insertion point)
     * Time Complexity: O(log n), Space Complexity: O(1)
     */
    public int searchInsertRightmost(int[] nums, int target) {
        int left = 0;
        int right = nums.length;

        // Find leftmost position where nums[i] > target
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    /**
     * Helper method to validate binary search invariants
     */
    private boolean validateInsertionPosition(int[] nums, int target, int position) {
        // Check that insertion maintains sorted order
        boolean leftOk = (position == 0) || (nums[position - 1] < target);
        boolean rightOk = (position == nums.length) || (nums[position] >= target);

        return leftOk && rightOk;
    }
}
