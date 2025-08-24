package frazsheet;

/**
 * Problem: Split Array Largest Sum
 * 
 * Given an array nums which consists of non-negative integers and an integer m, you can split the 
 * array into m non-empty continuous subarrays. Write an algorithm to minimize the largest sum among these m subarrays.
 * 
 * Example:
 * Input: nums = [7,2,5,10,8], m = 2
 * Output: 18
 * Explanation: There are four ways to split the array into two subarrays.
 * The best way is to split it into [7,2,5] and [10,8], where the largest sum among the two subarrays is only 18.
 * 
 * LeetCode: https://leetcode.com/problems/split-array-largest-sum
 * 
 * Time Complexity: O(n * log(S)) where n is the length of nums and S is the sum of elements in nums
 * Space Complexity: O(1)
 */
public class SplitArrayLargestSum {
    public int splitArray(int[] nums, int m) {
        // The minimum possible largest sum is the maximum element in the array
        // The maximum possible largest sum is the sum of all elements
        int left = 0;
        int right = 0;
        
        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }
        
        // Binary search to find the minimum possible largest sum
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Check if it's possible to split the array into m subarrays
            // where each subarray has sum <= mid
            if (canSplit(nums, m, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // Helper method to check if we can split the array into m subarrays with max sum <= maxSum
    private boolean canSplit(int[] nums, int m, int maxSum) {
        int count = 1;
        int currentSum = 0;
        
        for (int num : nums) {
            currentSum += num;
            
            if (currentSum > maxSum) {
                currentSum = num;
                count++;
                
                if (count > m) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
