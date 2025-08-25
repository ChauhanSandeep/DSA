package com.sandeep.frazsheet.binarysearch;

/**
 * Problem: Single Element in a Sorted Array
 * 
 * You are given a sorted array consisting of only integers where every element appears exactly
 * twice, except for one element which appears exactly once.
 * Return the single element that appears only once.
 * Your solution must run in O(log n) time and O(1) space.
 * 
 * Example:
 * Input: nums = [1,1,2,3,3,4,4,8,8]
 * Output: 2
 * Explanation: 2 is the single element that appears only once.
 * 
 * LeetCode: https://leetcode.com/problems/single-element-in-a-sorted-array
 * 
 * Follow-up Questions:
 * 1. What if every element appears exactly thrice except one that appears once?
 *    Answer: Use bit manipulation with 3-state counting or modify binary search logic.
 * 
 * 2. How would you handle the case where multiple elements appear only once?
 *    Answer: XOR all elements to find XOR of all unique elements, then separate using bit manipulation.
 * 
 * 3. What if the array is not sorted?
 *    Answer: Use XOR operation to find the single element in O(n) time, O(1) space.
 *    Related: https://leetcode.com/problems/single-number/
 * 
 * @author Sandeep
 */
public class SingleElementInASortedArray {
    
    /**
     * Finds single element using binary search with parity analysis.
     * 
     * Algorithm:
     * 1. Use binary search on the sorted array
     * 2. Check if middle element is the single element
     * 3. If not, determine which half contains the single element based on index parity
     * 4. If mid is even and nums[mid] == nums[mid+1], single element is on right
     * 5. If mid is even and nums[mid] != nums[mid+1], single element is on left
     * 6. Apply opposite logic when mid is odd
     * 
     * Time Complexity: O(log n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param nums Sorted array with one single element
     * @return The element that appears exactly once
     */
    public int singleNonDuplicate(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        if (nums.length == 1) {
            return nums[0];
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Ensure mid is even for consistent comparison
            if (mid % 2 == 1) {
                mid--; // Move to even index
            }
            
            // Check if the single element is at mid or mid+1
            if (nums[mid] == nums[mid + 1]) {
                // Pair starts at even index, single element is on the right
                left = mid + 2;
            } else {
                // Pair is broken, single element is on the left (including mid)
                right = mid;
            }
        }
        
        return nums[left];
    }
    
    /**
     * Alternative implementation with explicit single element detection.
     * More intuitive logic but same time complexity.
     * 
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateAlternative(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Check if mid is the single element
            if ((mid == 0 || nums[mid] != nums[mid - 1]) && 
                (mid == nums.length - 1 || nums[mid] != nums[mid + 1])) {
                return nums[mid];
            }
            
            // Determine which side has the single element
            int leftSize = mid;
            if (mid > 0 && nums[mid] == nums[mid - 1]) {
                leftSize = mid - 1; // Don't count the pair
            }
            
            if (leftSize % 2 == 1) {
                // Left side has odd number of elements, single element is on left
                right = mid - 1;
            } else {
                // Left side has even number of elements, single element is on right
                left = mid + 1;
            }
        }
        
        return nums[left];
    }
    
    /**
     * Clean implementation using XOR property for comparison.
     * Uses bit manipulation to determine search direction.
     * 
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateXOR(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // XOR mid with 1 to get its pair index
            // If mid is even, mid^1 = mid+1
            // If mid is odd, mid^1 = mid-1
            if (nums[mid] == nums[mid ^ 1]) {
                // Current element matches its pair, single element is on the right
                left = mid + 1;
            } else {
                // Current element doesn't match its pair, single element is on the left
                right = mid;
            }
        }
        
        return nums[left];
    }
    
    /**
     * Brute force linear search for comparison (O(n) time).
     * Useful for understanding and small inputs.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateLinear(int[] nums) {
        // XOR all elements - duplicates cancel out, single element remains
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    /**
     * Edge case optimized binary search.
     * Handles boundary conditions more explicitly.
     * 
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public int singleNonDuplicateOptimized(int[] nums) {
        int n = nums.length;
        
        // Handle edge cases
        if (n == 1) return nums[0];
        if (nums[0] != nums[1]) return nums[0];
        if (nums[n-1] != nums[n-2]) return nums[n-1];
        
        // Binary search in the middle portion
        int left = 1;
        int right = n - 2;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // Check if mid is the single element
            if (nums[mid] != nums[mid-1] && nums[mid] != nums[mid+1]) {
                return nums[mid];
            }
            
            // Determine search direction based on position and pairing
            if ((mid % 2 == 1 && nums[mid] == nums[mid-1]) || 
                (mid % 2 == 0 && nums[mid] == nums[mid+1])) {
                // We are on the left side of single element
                left = mid + 1;
            } else {
                // We are on the right side of single element
                right = mid - 1;
            }
        }
        
        return -1; // Should never reach here for valid input
    }
    
    /**
     * Recursive implementation of binary search approach.
     * Same logic but uses recursion instead of iteration.
     * 
     * Time Complexity: O(log n)
     * Space Complexity: O(log n) for recursion stack
     */
    public int singleNonDuplicateRecursive(int[] nums) {
        return findSingleRecursive(nums, 0, nums.length - 1);
    }
    
    // Helper method for recursive approach
    private int findSingleRecursive(int[] nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }
        
        int mid = left + (right - left) / 2;
        
        // Ensure mid is even
        if (mid % 2 == 1) {
            mid--;
        }
        
        if (nums[mid] == nums[mid + 1]) {
            return findSingleRecursive(nums, mid + 2, right);
        } else {
            return findSingleRecursive(nums, left, mid);
        }
    }
    
    /**
     * Validates the input array follows the problem constraints.
     * 
     * @param nums Input array to validate
     * @return true if array is valid according to problem constraints
     */
    public boolean isValidArray(int[] nums) {
        if (nums == null || nums.length == 0 || nums.length % 2 == 0) {
            return false;
        }
        
        // Check if array is sorted
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i-1]) {
                return false;
            }
        }
        
        // Check if exactly one element appears once (using XOR)
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // XOR result should be non-zero (the single element)
        return xorResult != 0;
    }
    
    /**
     * Finds all elements and their frequencies for debugging.
     * 
     * @param nums Input array
     * @return Map of element to frequency
     */
    public java.util.Map<Integer, Integer> getElementFrequencies(int[] nums) {
        java.util.Map<Integer, Integer> frequencies = new java.util.HashMap<>();
        
        for (int num : nums) {
            frequencies.put(num, frequencies.getOrDefault(num, 0) + 1);
        }
        
        return frequencies;
    }
}