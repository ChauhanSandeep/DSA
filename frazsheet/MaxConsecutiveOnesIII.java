package frazsheet;

import java.util.*;

/**
 * 1004. Max Consecutive Ones III
 * 
 * Problem: Given a binary array nums and integer k, return the maximum number
 * of consecutive 1s in the array if you can flip at most k 0s.
 * 
 * Example:
 * Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 * Output: 6
 * Explanation: [1,1,1,0,0,1,1,1,1,1,1] - flip the two 0s at indices 5 and 10
 * 
 * LeetCode: https://leetcode.com/problems/max-consecutive-ones-iii
 * 
 * Follow-up questions:
 * Q: What if we want to minimize the number of flips for a target length?
 * A: Use binary search on the answer with sliding window validation.
 * 
 * Q: Can we handle the case where k is very large?
 * A: If k >= number of zeros, answer is the entire array length.
 * 
 * Q: How to find all maximum-length windows?
 * A: Track all windows that achieve the maximum length during traversal.
 */
public class MaxConsecutiveOnesIII {
    
    /**
     * Sliding window approach counting zeros in current window.
     * 
     * Algorithm:
     * - Use sliding window [left, right] tracking number of zeros
     * - Expand right pointer, count zeros encountered
     * - If zeros > k, shrink window from left until zeros <= k
     * - Track maximum window size achieved
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) constant extra space
     */
    public int longestOnes(int[] nums, int k) {
        int left = 0, zeros = 0, maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            // Expand window
            if (nums[right] == 0) {
                zeros++;
            }
            
            // Shrink window if too many zeros
            while (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Alternative sliding window that maintains exactly k zeros.
     * Moves window instead of shrinking when limit exceeded.
     */
    public int longestOnesFixed(int[] nums, int k) {
        int left = 0, zeros = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeros++;
            }
            
            // Move window when zeros exceed k
            if (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
        }
        
        return nums.length - left;
    }
    
    /**
     * Queue-based approach tracking positions of zeros.
     * Maintains positions of zeros for efficient window management.
     */
    public int longestOnesQueue(int[] nums, int k) {
        Queue<Integer> zeroPositions = new LinkedList<>();
        int left = 0, maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeroPositions.offer(right);
                
                // Remove oldest zero position if we exceed k zeros
                if (zeroPositions.size() > k) {
                    left = zeroPositions.poll() + 1;
                }
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Deque-based approach for tracking zero positions with range queries.
     * More flexible for complex window operations.
     */
    public int longestOnesDeque(int[] nums, int k) {
        Deque<Integer> zeroIndices = new ArrayDeque<>();
        int left = 0, maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeroIndices.offerLast(right);
                
                while (zeroIndices.size() > k) {
                    left = zeroIndices.pollFirst() + 1;
                }
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Binary search approach on the answer.
     * Searches for maximum possible window length.
     */
    public int longestOnesBinarySearch(int[] nums, int k) {
        int left = 0, right = nums.length;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canAchieveLength(nums, k, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // Check if we can achieve window length with at most k flips
    private boolean canAchieveLength(int[] nums, int k, int targetLength) {
        int zeros = 0;
        
        // Check first window
        for (int i = 0; i < targetLength && i < nums.length; i++) {
            if (nums[i] == 0) zeros++;
        }
        
        if (zeros <= k) return true;
        
        // Slide window
        for (int i = targetLength; i < nums.length; i++) {
            if (nums[i] == 0) zeros++;
            if (nums[i - targetLength] == 0) zeros--;
            
            if (zeros <= k) return true;
        }
        
        return false;
    }
    
    /**
     * Prefix sum approach for range zero counting.
     * Uses prefix sums to quickly count zeros in any range.
     */
    public int longestOnesPrefixSum(int[] nums, int k) {
        int n = nums.length;
        int[] prefixZeros = new int[n + 1];
        
        // Build prefix sum of zeros
        for (int i = 0; i < n; i++) {
            prefixZeros[i + 1] = prefixZeros[i] + (nums[i] == 0 ? 1 : 0);
        }
        
        int maxLength = 0;
        
        for (int left = 0; left < n; left++) {
            // Binary search for rightmost valid position
            int lo = left, hi = n - 1;
            
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                int zeros = prefixZeros[mid + 1] - prefixZeros[left];
                
                if (zeros <= k) {
                    maxLength = Math.max(maxLength, mid - left + 1);
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * Two-pass approach: forward and backward scanning.
     * Demonstrates alternative algorithmic structure.
     */
    public int longestOnesTwoPass(int[] nums, int k) {
        // Forward pass
        int maxForward = slidingWindow(nums, k);
        
        // Backward pass (reverse array)
        reverse(nums);
        int maxBackward = slidingWindow(nums, k);
        reverse(nums); // Restore original array
        
        return Math.max(maxForward, maxBackward);
    }
    
    // Standard sliding window helper
    private int slidingWindow(int[] nums, int k) {
        int left = 0, zeros = 0, maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) zeros++;
            
            while (zeros > k) {
                if (nums[left] == 0) zeros--;
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // Reverse array in place
    private void reverse(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }
    
    /**
     * Returns the actual maximum window indices instead of just length.
     * Extension that provides window boundaries.
     */
    public int[] longestOnesWindow(int[] nums, int k) {
        int left = 0, zeros = 0;
        int bestLeft = 0, bestRight = -1, maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeros++;
            }
            
            while (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
            
            if (right - left + 1 > maxLength) {
                maxLength = right - left + 1;
                bestLeft = left;
                bestRight = right;
            }
        }
        
        return new int[]{bestLeft, bestRight, maxLength};
    }
    
    /**
     * Segment-based approach analyzing consecutive segments.
     * Groups consecutive ones and zeros for pattern analysis.
     */
    public int longestOnesSegmented(int[] nums, int k) {
        List<Segment> segments = new ArrayList<>();
        
        // Build segments of consecutive 0s and 1s
        int i = 0;
        while (i < nums.length) {
            int value = nums[i];
            int count = 0;
            
            while (i < nums.length && nums[i] == value) {
                count++;
                i++;
            }
            
            segments.add(new Segment(value, count));
        }
        
        // Use sliding window on segments
        int left = 0, zerosUsed = 0, maxLength = 0;
        
        for (int right = 0; right < segments.size(); right++) {
            Segment seg = segments.get(right);
            
            if (seg.value == 0) {
                zerosUsed += seg.length;
            }
            
            while (zerosUsed > k) {
                Segment leftSeg = segments.get(left);
                if (leftSeg.value == 0) {
                    zerosUsed -= leftSeg.length;
                }
                left++;
            }
            
            // Calculate total length of current window
            int windowLength = 0;
            for (int j = left; j <= right; j++) {
                windowLength += segments.get(j).length;
            }
            
            maxLength = Math.max(maxLength, windowLength);
        }
        
        return maxLength;
    }
    
    // Helper class for segment representation
    private static class Segment {
        int value;
        int length;
        
        Segment(int value, int length) {
            this.value = value;
            this.length = length;
        }
    }
    
    /**
     * Optimized approach with early termination conditions.
     * Includes various optimizations for special cases.
     */
    public int longestOnesOptimized(int[] nums, int k) {
        // Special cases
        if (k == 0) {
            return maxConsecutiveOnes(nums);
        }
        
        int totalZeros = 0;
        for (int num : nums) {
            if (num == 0) totalZeros++;
        }
        
        if (totalZeros <= k) {
            return nums.length; // Can flip all zeros
        }
        
        // Standard sliding window
        return longestOnes(nums, k);
    }
    
    // Helper for k=0 case
    private int maxConsecutiveOnes(int[] nums) {
        int maxLength = 0, currentLength = 0;
        
        for (int num : nums) {
            if (num == 1) {
                currentLength++;
                maxLength = Math.max(maxLength, currentLength);
            } else {
                currentLength = 0;
            }
        }
        
        return maxLength;
    }
}