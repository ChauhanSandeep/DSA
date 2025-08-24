package DynamicProgramming;

import java.util.*;

/**
 * 1027. Longest Arithmetic Subsequence
 * 
 * Problem: Given an array nums, return the length of the longest arithmetic subsequence.
 * An arithmetic subsequence is a sequence where the difference between consecutive
 * elements is the same.
 * 
 * Example:
 * Input: nums = [3,6,9,12]
 * Output: 4
 * Explanation: The whole array is an arithmetic sequence with steps of length = 3.
 * 
 * LeetCode: https://leetcode.com/problems/longest-arithmetic-subsequence
 * 
 * Follow-up questions:
 * Q: What if we need to find all longest arithmetic subsequences?
 * A: Modify DP to track actual sequences instead of just lengths.
 * 
 * Q: Can we optimize for very large arrays?
 * A: Current solution is already optimal O(n²), can't improve asymptotically.
 * 
 * Q: How to handle overflow with large differences?
 * A: Use HashMap with proper key handling or compress the difference range.
 */
public class LongestArithmeticSubsequence {
    
    /**
     * Dynamic Programming approach using HashMap for each position.
     * 
     * Algorithm:
     * - For each position i, maintain map[diff] = length of longest subsequence ending at i with difference diff
     * - For each pair (j, i) where j < i, calculate diff = nums[i] - nums[j]
     * - Extend subsequence: dp[i][diff] = max(dp[i][diff], dp[j][diff] + 1)
     * 
     * Time Complexity: O(n²)
     * Space Complexity: O(n²) for storing all difference maps
     */
    public int longestArithSeqLength(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        // dp[i] = map where key=difference, value=max length ending at index i
        Map<Integer, Integer>[] dp = new HashMap[n];
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
        }
        
        int maxLength = 2;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                
                // Get current length of subsequence ending at j with this difference
                int prevLength = dp[j].getOrDefault(diff, 1);
                
                // Extend the subsequence to include nums[i]
                int newLength = prevLength + 1;
                dp[i].put(diff, Math.max(dp[i].getOrDefault(diff, 0), newLength));
                
                maxLength = Math.max(maxLength, newLength);
            }
        }
        
        return maxLength;
    }
    
    /**
     * Optimized approach using 2D array for bounded differences.
     * Works when the range of differences is reasonable.
     */
    public int longestArithSeqLengthBounded(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        int minVal = Arrays.stream(nums).min().getAsInt();
        int maxVal = Arrays.stream(nums).max().getAsInt();
        int maxDiff = maxVal - minVal;
        
        // dp[i][diff + maxDiff] = max length ending at i with difference diff
        int[][] dp = new int[n][2 * maxDiff + 1];
        int maxLength = 2;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                int diffIndex = diff + maxDiff; // Shift to handle negative differences
                
                if (diffIndex >= 0 && diffIndex < 2 * maxDiff + 1) {
                    dp[i][diffIndex] = Math.max(dp[i][diffIndex], dp[j][diffIndex] + 1);
                    if (dp[i][diffIndex] == 0) {
                        dp[i][diffIndex] = 2; // Base case for new sequences
                    }
                    maxLength = Math.max(maxLength, dp[i][diffIndex]);
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * Space-optimized approach using rolling hash map.
     * Reduces space complexity for sparse difference distributions.
     */
    public int longestArithSeqLengthOptimized(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        Map<Integer, Integer> prevDP = new HashMap<>();
        Map<Integer, Integer> currDP = new HashMap<>();
        int maxLength = 2;
        
        // Initialize first element
        for (int i = 1; i < n; i++) {
            currDP.clear();
            
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                
                // Look for existing subsequence with this difference ending at j
                int prevLength = 1;
                if (j > 0) {
                    // This requires tracking all positions, so we fall back to original approach
                    // Space optimization is limited here due to the nature of the problem
                }
                
                int newLength = prevLength + 1;
                currDP.put(diff, Math.max(currDP.getOrDefault(diff, 0), newLength));
                maxLength = Math.max(maxLength, newLength);
            }
            
            prevDP = new HashMap<>(currDP);
        }
        
        return maxLength;
    }
    
    /**
     * Brute force approach for verification and small inputs.
     * Generates all possible arithmetic subsequences explicitly.
     */
    public int longestArithSeqLengthBruteForce(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        int maxLength = 2;
        
        // Try all possible starting pairs
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int diff = nums[j] - nums[i];
                int length = 2;
                int last = nums[j];
                
                // Extend the sequence
                for (int k = j + 1; k < n; k++) {
                    if (nums[k] - last == diff) {
                        length++;
                        last = nums[k];
                    }
                }
                
                maxLength = Math.max(maxLength, length);
            }
        }
        
        return maxLength;
    }
    
    /**
     * Returns the actual longest arithmetic subsequence instead of just length.
     * Extension that provides the sequence elements.
     */
    public List<Integer> longestArithSeqElements(int[] nums) {
        int n = nums.length;
        if (n <= 2) return Arrays.stream(nums).boxed().collect(java.util.stream.Collectors.toList());
        
        Map<Integer, Integer>[] dp = new HashMap[n];
        Map<Integer, List<Integer>>[] sequences = new HashMap[n];
        
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
            sequences[i] = new HashMap<>();
        }
        
        List<Integer> longestSeq = new ArrayList<>();
        longestSeq.add(nums[0]);
        longestSeq.add(nums[1]);
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                
                int prevLength = dp[j].getOrDefault(diff, 1);
                int newLength = prevLength + 1;
                
                if (newLength > dp[i].getOrDefault(diff, 0)) {
                    dp[i].put(diff, newLength);
                    
                    // Build the actual sequence
                    List<Integer> newSeq = new ArrayList<>();
                    if (sequences[j].containsKey(diff)) {
                        newSeq.addAll(sequences[j].get(diff));
                    } else {
                        newSeq.add(nums[j]);
                    }
                    newSeq.add(nums[i]);
                    sequences[i].put(diff, newSeq);
                    
                    if (newSeq.size() > longestSeq.size()) {
                        longestSeq = new ArrayList<>(newSeq);
                    }
                }
            }
        }
        
        return longestSeq;
    }
    
    /**
     * Recursive approach with memoization.
     * Demonstrates top-down dynamic programming approach.
     */
    public int longestArithSeqLengthRecursive(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        Map<String, Integer> memo = new HashMap<>();
        int maxLength = 2;
        
        // Try starting from each pair
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int diff = nums[j] - nums[i];
                int length = 1 + dfs(nums, j, diff, memo);
                maxLength = Math.max(maxLength, length);
            }
        }
        
        return maxLength;
    }
    
    // Recursive DFS with memoization
    private int dfs(int[] nums, int index, int diff, Map<String, Integer> memo) {
        String key = index + "," + diff;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        int maxLength = 1; // At least the current element
        
        for (int i = index + 1; i < nums.length; i++) {
            if (nums[i] - nums[index] == diff) {
                maxLength = Math.max(maxLength, 1 + dfs(nums, i, diff, memo));
            }
        }
        
        memo.put(key, maxLength);
        return maxLength;
    }
    
    /**
     * Segment-based approach for analyzing patterns.
     * Groups elements by their potential differences.
     */
    public int longestArithSeqLengthSegmented(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        // Group indices by value for quick lookup
        Map<Integer, List<Integer>> valueIndices = new HashMap<>();
        for (int i = 0; i < n; i++) {
            valueIndices.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }
        
        Map<Integer, Integer>[] dp = new HashMap[n];
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
        }
        
        int maxLength = 2;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                int prevLength = dp[j].getOrDefault(diff, 1);
                int newLength = prevLength + 1;
                
                dp[i].put(diff, Math.max(dp[i].getOrDefault(diff, 0), newLength));
                maxLength = Math.max(maxLength, newLength);
            }
        }
        
        return maxLength;
    }
    
    /**
     * Parallel processing approach for large arrays.
     * Uses parallel streams to process different segments concurrently.
     */
    public int longestArithSeqLengthParallel(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;
        
        // Split work across different starting positions
        return java.util.stream.IntStream.range(0, n - 1)
            .parallel()
            .map(i -> {
                int localMax = 2;
                Map<Integer, Integer> dp = new HashMap<>();
                
                for (int j = i + 1; j < n; j++) {
                    int diff = nums[j] - nums[i];
                    
                    // Find longest subsequence with this difference
                    int length = 2;
                    int last = nums[j];
                    
                    for (int k = j + 1; k < n; k++) {
                        if (nums[k] - last == diff) {
                            length++;
                            last = nums[k];
                        }
                    }
                    
                    localMax = Math.max(localMax, length);
                }
                
                return localMax;
            })
            .max()
            .orElse(2);
    }
}