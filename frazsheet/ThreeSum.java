package frazsheet;

import java.util.*;

/**
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]]
 * such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 * 
 * Notice that the solution set must not contain duplicate triplets.
 * 
 * Example 1:
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 * 
 * Example 2:
 * Input: nums = [0,1,1]
 * Output: []
 * 
 * Example 3:
 * Input: nums = [0,0,0]
 * Output: [[0,0,0]]
 * 
 * LeetCode: https://leetcode.com/problems/3sum/
 * 
 * Follow-up Questions:
 * 1. How would you modify your solution to find triplets that sum to a target other than 0?
 *    - We can generalize the solution to accept a target sum as a parameter.
 * 2. What if we need to find all unique quadruplets that sum to zero?
 *    - We can extend the solution to handle 4Sum by adding an additional loop.
 * 3. How would you optimize the solution for very large input arrays?
 *    - We can use a hash map for O(n²) time complexity with O(n) space.
 * 
 * Related Problems:
 * - 3Sum Closest (https://leetcode.com/problems/3sum-closest/)
 * - 4Sum (https://leetcode.com/problems/4sum/)
 * - Two Sum (https://leetcode.com/problems/two-sum/)
 */
public class ThreeSum {
    
    /**
     * Finds all unique triplets in the array that sum to zero.
     * 
     * @param nums The input array of integers
     * @return A list of all unique triplets that sum to zero
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        
        // Sort the array to handle duplicates and use two pointers
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate elements
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            int target = -nums[i];
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == target) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // Skip duplicate elements
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
    
    /**
     * General solution to find all unique triplets that sum to a target value.
     * 
     * @param nums The input array of integers
     * @param target The target sum
     * @return A list of all unique triplets that sum to the target
     */
    public List<List<Integer>> threeSumGeneral(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            int currentTarget = target - nums[i];
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == currentTarget) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < currentTarget) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Solution for "3Sum Closest" (LeetCode #16).
     * Finds three integers in nums such that the sum is closest to target.
     */
    public int threeSumClosest(int[] nums, int target) {
        if (nums == null || nums.length < 3) {
            throw new IllegalArgumentException("Input array must have at least 3 elements");
        }
        
        Arrays.sort(nums);
        int closestSum = nums[0] + nums[1] + nums[2];
        
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int currentSum = nums[i] + nums[left] + nums[right];
                
                if (currentSum == target) {
                    return currentSum;
                }
                
                // Update the closest sum if the current sum is closer to the target
                if (Math.abs(currentSum - target) < Math.abs(closestSum - target)) {
                    closestSum = currentSum;
                }
                
                if (currentSum < target) {
                    left++;
                    // Skip duplicate elements
                    while (left < right && nums[left] == nums[left - 1]) left++;
                } else {
                    right--;
                    // Skip duplicate elements
                    while (left < right && nums[right] == nums[right + 1]) right--;
                }
            }
        }
        
        return closestSum;
    }
    
    /**
     * Solution for "4Sum" (LeetCode #18).
     * Finds all unique quadruplets in the array that sum to target.
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 4) {
            return result;
        }
        
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 3; i++) {
            // Skip duplicate elements for the first number
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            for (int j = i + 1; j < nums.length - 2; j++) {
                // Skip duplicate elements for the second number
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                
                int left = j + 1;
                int right = nums.length - 1;
                long currentTarget = (long)target - nums[i] - nums[j];
                
                while (left < right) {
                    long sum = (long)nums[left] + nums[right];
                    
                    if (sum == currentTarget) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        
                        // Skip duplicate elements
                        while (left < right && nums[left] == nums[left + 1]) left++;
                        while (left < right && nums[right] == nums[right - 1]) right--;
                        
                        left++;
                        right--;
                    } else if (sum < currentTarget) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Solution for "Two Sum" (LeetCode #1).
     * Finds two numbers in the array that add up to the target.
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            if (numMap.containsKey(complement)) {
                return new int[] { numMap.get(complement), i };
            }
            
            numMap.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("No two sum solution");
    }
    
    /**
     * Finds all unique pairs in the array that sum to the target.
     * This is a helper method that can be used for the 3Sum problem.
     */
    private List<List<Integer>> twoSumHelper(int[] nums, int start, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int left = start;
        int right = nums.length - 1;
        
        while (left < right) {
            int sum = nums[left] + nums[right];
            
            if (sum == target) {
                result.add(Arrays.asList(nums[left], nums[right]));
                
                // Skip duplicate elements
                while (left < right && nums[left] == nums[left + 1]) left++;
                while (left < right && nums[right] == nums[right - 1]) right--;
                
                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        
        return result;
    }
}
