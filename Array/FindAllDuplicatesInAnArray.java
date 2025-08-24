package Array;

import java.util.*;

/**
 * 442. Find All Duplicates in an Array
 * 
 * Problem: Given an integer array nums where 1 ≤ nums[i] ≤ n (n = array length),
 * some elements appear twice and others once. Return all integers that appear twice.
 * 
 * Example:
 * Input: nums = [4,3,2,7,8,2,3,1]
 * Output: [2,3]
 * 
 * LeetCode: https://leetcode.com/problems/find-all-duplicates-in-an-array
 * 
 * Follow-up questions:
 * Q: Can we solve without extra space and in O(n) time?
 * A: Yes, use array indices as hash by negating values at corresponding positions.
 * 
 * Q: What if numbers can be outside [1,n] range?
 * A: Use HashMap or Set instead of index-based marking approach.
 * 
 * Q: How to handle if array is read-only?
 * A: Cannot use in-place marking; must use extra space with Set/Map.
 */
public class FindAllDuplicatesInAnArray {
    
    /**
     * Optimal solution using array indices as hash map.
     * Negates values to mark visited indices without extra space.
     * 
     * Algorithm:
     * - For each number, use its value as index (subtract 1)
     * - If number at that index is positive, negate it (first visit)
     * - If number at that index is negative, it's duplicate (second visit)
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1) excluding output array
     */
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]) - 1;
            
            if (nums[index] < 0) {
                // Already visited, this is a duplicate
                result.add(Math.abs(nums[i]));
            } else {
                // First visit, mark as visited by negating
                nums[index] = -nums[index];
            }
        }
        
        return result;
    }
    
    /**
     * Alternative approach using cyclic sort concept.
     * Places each number at its correct position.
     */
    public List<Integer> findDuplicatesCyclicSort(int[] nums) {
        List<Integer> result = new ArrayList<>();
        
        // Place each number at its correct position
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        
        // Find numbers not at their correct positions
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                result.add(nums[i]);
            }
        }
        
        return result;
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * HashMap approach for general case (when constraint 1≤nums[i]≤n doesn't hold).
     * Uses extra space but works for any integer values.
     */
    public List<Integer> findDuplicatesHashMap(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> count = new HashMap<>();
        
        // Count frequencies
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        
        // Collect duplicates
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 2) {
                result.add(entry.getKey());
            }
        }
        
        return result;
    }
    
    /**
     * Set-based approach for detecting duplicates.
     * Simple and readable but uses O(n) extra space.
     */
    public List<Integer> findDuplicatesSet(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            if (seen.contains(num)) {
                result.add(num);
            } else {
                seen.add(num);
            }
        }
        
        return result;
    }
    
    /**
     * Mathematical approach using sum formula.
     * Only works when we know exactly which numbers should be present.
     */
    public List<Integer> findDuplicatesMath(int[] nums) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;
        
        // Calculate expected sum if all numbers 1 to n appear once
        long expectedSum = (long) n * (n + 1) / 2;
        
        // Calculate actual sum
        long actualSum = 0;
        for (int num : nums) {
            actualSum += num;
        }
        
        // The difference tells us the sum of duplicates
        // This approach is limited and doesn't identify individual duplicates
        // Keeping for educational purposes but not practical for this problem
        
        return findDuplicates(nums); // Fall back to optimal solution
    }
    
    /**
     * Bit manipulation approach using XOR properties.
     * Works when we know there's exactly one number appearing twice.
     */
    public List<Integer> findDuplicatesBitwise(int[] nums) {
        // This approach is more suitable for finding a single duplicate
        // For multiple duplicates, the index marking approach is better
        return findDuplicates(nums);
    }
    
    /**
     * Non-destructive version that preserves original array.
     * Uses additional space to avoid modifying input.
     */
    public List<Integer> findDuplicatesPreserveArray(int[] nums) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length + 1];
        
        for (int num : nums) {
            if (visited[num]) {
                result.add(num);
            } else {
                visited[num] = true;
            }
        }
        
        return result;
    }
    
    /**
     * Sorting-based approach.
     * Modifies array order but easy to understand.
     */
    public List<Integer> findDuplicatesSorting(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Arrays.sort(nums);
        
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] && (result.isEmpty() || result.get(result.size() - 1) != nums[i])) {
                result.add(nums[i]);
            }
        }
        
        return result;
    }
}