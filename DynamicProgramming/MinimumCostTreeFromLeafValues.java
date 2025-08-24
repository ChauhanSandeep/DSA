package DynamicProgramming;

import java.util.Stack;

/**
 * Given an array arr of positive integers, consider all binary trees such that:
 * 1. Each node has either 0 or 2 children
 * 2. The values of arr correspond to the values of each leaf in an in-order traversal of the tree.
 * 3. The value of each non-leaf node is equal to the product of the largest leaf value in its left and right subtree.
 * 
 * Return the minimum possible sum of the values of each non-leaf node.
 * 
 * Example 1:
 * Input: arr = [6,2,4]
 * Output: 32
 * Explanation: There are two possible trees. The first has non-leaf node sum 36, the second has 32.
 * 
 * Example 2:
 * Input: arr = [4,11]
 * Output: 44
 * 
 * LeetCode: https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/
 * 
 * Follow-up Questions:
 * 1. How would you handle negative numbers in the array?
 *    - The problem states that all numbers are positive, but if negatives were allowed, we'd need to track both min and max products.
 * 2. What if the array is very large (e.g., length 1000)?
 *    - The O(n) stack-based solution should handle this efficiently.
 * 3. How would you modify the solution to also return the structure of the optimal tree?
 *    - We could extend the solution to also track the tree structure during the stack operations.
 * 
 * Related Problems:
 * - Unique Binary Search Trees (https://leetcode.com/problems/unique-binary-search-trees/)
 * - Binary Tree Maximum Path Sum (https://leetcode.com/problems/binary-tree-maximum-path-sum/)
 */
public class MinimumCostTreeFromLeafValues {
    /**
     * Calculates the minimum possible sum of the values of each non-leaf node.
     * 
     * @param arr Array of positive integers representing leaf values
     * @return Minimum possible sum of non-leaf node values
     */
    public int mctFromLeafValues(int[] arr) {
        int result = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(Integer.MAX_VALUE); // Sentinel value
        
        for (int num : arr) {
            // While current element is greater than stack top
            while (stack.peek() <= num) {
                int mid = stack.pop();
                // Add the product of current element and stack top (or previous element if smaller)
                result += mid * Math.min(stack.peek(), num);
            }
            stack.push(num);
        }
        
        // Process remaining elements in the stack
        while (stack.size() > 2) {
            result += stack.pop() * stack.peek();
        }
        
        return result;
    }
    
    /**
     * Dynamic Programming solution for reference (O(n^3) time, O(n^2) space)
     */
    public int mctFromLeafValuesDP(int[] arr) {
        int n = arr.length;
        // dp[i][j] = min sum for arr[i..j]
        int[][] dp = new int[n][n];
        // max[i][j] = max value in arr[i..j]
        int[][] max = new int[n][n];
        
        // Initialize max values for all ranges
        for (int i = 0; i < n; i++) {
            max[i][i] = arr[i];
            for (int j = i + 1; j < n; j++) {
                max[i][j] = Math.max(max[i][j - 1], arr[j]);
            }
        }
        
        // Fill dp table for different lengths
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                // Try all possible partitions
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], 
                                      dp[i][k] + dp[k + 1][j] + 
                                      max[i][k] * max[k + 1][j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
}
