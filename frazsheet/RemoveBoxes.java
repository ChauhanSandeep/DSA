package frazsheet;

/**
 * You are given several boxes with different colors represented by different positive numbers.
 * You may experience several rounds to remove boxes until there is no box left. Each time you can
 * choose some continuous boxes with the same color (i.e., composed of k boxes, k >= 1), remove them
 * and get k * k points.
 * 
 * Return the maximum points you can get.
 * 
 * Example 1:
 * Input: boxes = [1,3,2,2,2,3,4,3,1]
 * Output: 23
 * Explanation:
 * [1, 3, 2, 2, 2, 3, 4, 3, 1] 
 * ----> [1, 3, 3, 4, 3, 1] (3*3=9 points)
 * ----> [1, 3, 3, 3, 1] (1*1=1 points)
 * ----> [1, 1] (3*3=9 points)
 * ----> [] (2*2=4 points)
 * 
 * Example 2:
 * Input: boxes = [1,1,1]
 * Output: 9
 * 
 * LeetCode: https://leetcode.com/problems/remove-boxes/
 * 
 * Follow-up Questions:
 * 1. How would you modify the solution if we could remove non-contiguous boxes of the same color?
 *    - The problem would become a variation of the subset sum problem, which can be solved with dynamic programming.
 * 2. What if the input size is very large (e.g., 1000 boxes)?
 *    - The memoization with DP state (l, r, k) helps, but for very large inputs, we might need further optimizations.
 * 3. How would you find the sequence of moves that gives the maximum points?
 *    - We could modify the solution to track the sequence of moves that leads to the maximum score.
 * 
 * Related Problems:
 * - Burst Balloons (https://leetcode.com/problems/burst-balloons/)
 * - Strange Printer (https://leetcode.com/problems/strange-printer/)
 */
public class RemoveBoxes {
    private int[][][] memo;
    
    /**
     * Calculates the maximum points that can be obtained by removing boxes.
     * 
     * @param boxes Array of box colors
     * @return Maximum points
     */
    public int removeBoxes(int[] boxes) {
        int n = boxes.length;
        // memo[l][r][k] represents the maximum points for boxes[l..r] with k same colored boxes before
        memo = new int[n][n][n];
        return dfs(boxes, 0, n - 1, 0);
    }
    
    /**
     * Helper method that performs DFS with memoization to find the maximum points.
     * 
     * @param boxes Array of box colors
     * @param l Left index of current subarray
     * @param r Right index of current subarray
     * @param k Number of boxes with the same color as boxes[r] that are before l
     * @return Maximum points for the current subproblem
     */
    private int dfs(int[] boxes, int l, int r, int k) {
        if (l > r) {
            return 0;
        }
        
        // If we've already computed this subproblem, return the cached result
        if (memo[l][r][k] > 0) {
            return memo[l][r][k];
        }
        
        // To handle the case where we have consecutive same colors at the end
        while (r > l && boxes[r] == boxes[r - 1]) {
            r--;
            k++;
        }
        
        // Option 1: Remove the last group of same-colored boxes
        int maxScore = dfs(boxes, l, r - 1, 0) + (k + 1) * (k + 1);
        
        // Option 2: Try to merge with previous same-colored boxes
        for (int i = l; i < r; i++) {
            if (boxes[i] == boxes[r]) {
                // If we find a box with the same color as boxes[r], we can merge them
                // The idea is to remove boxes[i+1..r-1] first, then merge boxes[i] and boxes[r]
                int score = dfs(boxes, l, i, k + 1) + dfs(boxes, i + 1, r - 1, 0);
                maxScore = Math.max(maxScore, score);
            }
        }
        
        memo[l][r][k] = maxScore;
        return maxScore;
    }
    
    /**
     * Bottom-up dynamic programming solution.
     * This approach is more complex but avoids recursion stack overhead.
     */
    public int removeBoxesDP(int[] boxes) {
        int n = boxes.length;
        // dp[l][r][k] represents the maximum points for boxes[l..r] with k same colored boxes before
        int[][][] dp = new int[n][n][n];
        
        // Base case: single box
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= i; k++) {
                dp[i][i][k] = (k + 1) * (k + 1);
            }
        }
        
        // Fill the table for subarrays of increasing lengths
        for (int len = 1; len < n; len++) {
            for (int l = 0; l < n - len; l++) {
                int r = l + len;
                
                // Try all possible k values (number of same colored boxes before l)
                for (int k = 0; k <= l; k++) {
                    // Option 1: Remove the last group of same-colored boxes
                    int maxScore = dp[l][r-1][0] + (k + 1) * (k + 1);
                    
                    // Option 2: Try to merge with previous same-colored boxes
                    for (int i = l; i < r; i++) {
                        if (boxes[i] == boxes[r]) {
                            int score = (i > l ? dp[l][i-1][k+1] : 0) + 
                                      (i + 1 <= r - 1 ? dp[i+1][r-1][0] : 0);
                            maxScore = Math.max(maxScore, score);
                        }
                    }
                    
                    dp[l][r][k] = maxScore;
                }
            }
        }
        
        return n == 0 ? 0 : dp[0][n-1][0];
    }
    
    /**
     * Optimized version with memoization using a map to reduce memory usage.
     * This is more memory efficient for large inputs.
     */
    public int removeBoxesOptimized(int[] boxes) {
        int n = boxes.length;
        // Using a map to store only the computed states
        // Key: (l * 10000 + r * 100 + k) as a unique identifier for the state (l, r, k)
        // This assumes n <= 100, which is reasonable for the problem constraints
        int[][][] memo = new int[100][100][100];
        return dfsOptimized(boxes, 0, n - 1, 0, memo);
    }
    
    private int dfsOptimized(int[] boxes, int l, int r, int k, int[][][] memo) {
        if (l > r) {
            return 0;
        }
        
        if (memo[l][r][k] > 0) {
            return memo[l][r][k];
        }
        
        // Optimize by skipping consecutive same colors at the end
        int rr = r;
        int kk = k;
        while (rr > l && boxes[rr] == boxes[rr - 1]) {
            rr--;
            kk++;
        }
        
        // Option 1: Remove the last group of same-colored boxes
        int maxScore = dfsOptimized(boxes, l, rr - 1, 0, memo) + (kk + 1) * (kk + 1);
        
        // Option 2: Try to merge with previous same-colored boxes
        for (int i = l; i < rr; i++) {
            if (boxes[i] == boxes[rr]) {
                int score = dfsOptimized(boxes, l, i, kk + 1, memo) + 
                           dfsOptimized(boxes, i + 1, rr - 1, 0, memo);
                maxScore = Math.max(maxScore, score);
            }
        }
        
        memo[l][r][k] = maxScore;
        return maxScore;
    }
}
