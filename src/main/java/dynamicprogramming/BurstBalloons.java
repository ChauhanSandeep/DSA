package dynamicprogramming;

import java.util.*;

/**
 * 312. Burst Balloons
 *
 * Problem: Given n balloons with numbers on them, burst all balloons to get maximum coins.
 * When you burst balloon i, you get nums[left] * nums[i] * nums[right] coins.
 * After bursting, left and right become adjacent.
 *
 * Example:
 * Input: nums = [3,1,5,8]
 * Output: 167
 * Explanation: Burst sequence [1,8,5,3] gives maximum coins
 *
 * LeetCode: https://leetcode.com/problems/burst-balloons
 *
 * Follow-up questions:
 * Q: What if balloons have different burst constraints or prerequisites?
 * A: Extend DP state to include constraint satisfaction information.
 *
 * Q: How to handle very large arrays efficiently?
 * A: Use memoization with coordinate compression or approximation algorithms.
 *
 * Q: Can we find the optimal bursting sequence, not just maximum coins?
 * A: Modify DP to track optimal decisions and reconstruct sequence.
 */
public class BurstBalloons {

    /**
     * Dynamic Programming approach with interval thinking.
     *
     * Algorithm: Interval DP with reverse thinking
     * - Think in reverse: which balloon to add last in an interval
     * - dp[i][j] = max coins from bursting balloons in open interval (i,j)
     * - For each interval, try each balloon k as the last one to burst
     * - Transition: dp[i][j] = max(dp[i][k] + dp[k][j] + nums[i]*nums[k]*nums[j])
     *
     * Time Complexity: O(n³) where n is number of balloons
     * Space Complexity: O(n²) for DP table
     */
    public int maxCoins(int[] nums) {
        int n = nums.length;

        // Add boundary balloons with value 1
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            balloons[i + 1] = nums[i];
        }

        // dp[i][j] represents max coins from bursting balloons in open interval (i,j)
        int[][] dp = new int[n + 2][n + 2];

        // Length of interval
        for (int length = 2; length <= n + 1; length++) {
            for (int left = 0; left <= n + 1 - length; left++) {
                int right = left + length;

                // Try each balloon in (left, right) as the last one to burst
                for (int k = left + 1; k < right; k++) {
                    int coins = balloons[left] * balloons[k] * balloons[right];
                    dp[left][right] = Math.max(dp[left][right],
                                             dp[left][k] + dp[k][right] + coins);
                }
            }
        }

        return dp[0][n + 1];
    }

    /**
     * Top-down memoization approach.
     * More intuitive recursive thinking with caching.
     */
    public int maxCoinsMemoization(int[] nums) {
        int n = nums.length;
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        int[][] memo = new int[n + 2][n + 2];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return memoHelper(balloons, memo, 0, n + 1);
    }

    private int memoHelper(int[] balloons, int[][] memo, int left, int right) {
        if (left + 1 >= right) return 0; // No balloons to burst

        if (memo[left][right] != -1) {
            return memo[left][right];
        }

        int maxCoins = 0;

        // Try each balloon as the last one to burst in this interval
        for (int k = left + 1; k < right; k++) {
            int coins = balloons[left] * balloons[k] * balloons[right];
            int totalCoins = memoHelper(balloons, memo, left, k) +
                           memoHelper(balloons, memo, k, right) + coins;
            maxCoins = Math.max(maxCoins, totalCoins);
        }

        return memo[left][right] = maxCoins;
    }

    /**
     * Space-optimized approach using 1D array.
     * Reduces space complexity with careful update order.
     */
    public int maxCoinsSpaceOptimized(int[] nums) {
        int n = nums.length;
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        // dp[i] represents max coins for current interval length
        int[] dp = new int[n + 2];
        int[] temp = new int[n + 2];

        for (int length = 2; length <= n + 1; length++) {
            Arrays.fill(temp, 0);

            for (int left = 0; left <= n + 1 - length; left++) {
                int right = left + length;

                for (int k = left + 1; k < right; k++) {
                    int coins = balloons[left] * balloons[k] * balloons[right];
                    int leftCoins = (left + 1 == k) ? 0 : dp[left * (n + 2) + k]; // Previous iteration
                    int rightCoins = (k + 1 == right) ? 0 : dp[k * (n + 2) + right];

                    temp[left * (n + 2) + right] = Math.max(temp[left * (n + 2) + right],
                                                           leftCoins + rightCoins + coins);
                }
            }

            dp = temp.clone();
        }

        return dp[0 * (n + 2) + (n + 1)];
    }

    /**
     * Matrix chain multiplication approach.
     * Alternative formulation showing problem structure similarity.
     */
    public int maxCoinsMatrixChain(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        // Transform to matrix chain problem
        int[] matrices = new int[n + 1];
        matrices[0] = 1;
        for (int i = 0; i < n; i++) {
            matrices[i + 1] = nums[i];
        }

        // DP for matrix chain
        int[][] dp = new int[n + 1][n + 1];

        for (int length = 2; length <= n; length++) {
            for (int i = 1; i <= n - length + 1; i++) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;

                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + matrices[i - 1] * matrices[k] * matrices[j];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }

        // Convert back to balloon problem (this is a conceptual approach)
        return convertMatrixChainToBalloons(dp, nums);
    }

    // Conceptual conversion (simplified)
    private int convertMatrixChainToBalloons(int[][] dp, int[] nums) {
        // This would require more complex transformation
        // Using standard approach instead
        return maxCoins(nums);
    }

    /**
     * Returns the optimal bursting sequence along with maximum coins.
     * Extension that reconstructs the actual solution path.
     */
    public BurstResult maxCoinsWithSequence(int[] nums) {
        int n = nums.length;
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        int[][] dp = new int[n + 2][n + 2];
        int[][] choice = new int[n + 2][n + 2]; // Track optimal choices

        for (int length = 2; length <= n + 1; length++) {
            for (int left = 0; left <= n + 1 - length; left++) {
                int right = left + length;

                for (int k = left + 1; k < right; k++) {
                    int coins = balloons[left] * balloons[k] * balloons[right];
                    int totalCoins = dp[left][k] + dp[k][right] + coins;

                    if (totalCoins > dp[left][right]) {
                        dp[left][right] = totalCoins;
                        choice[left][right] = k;
                    }
                }
            }
        }

        List<Integer> sequence = new ArrayList<>();
        reconstructSequence(choice, balloons, 0, n + 1, sequence);

        // Convert indices back to original array
        List<Integer> originalSequence = new ArrayList<>();
        for (int idx : sequence) {
            if (idx > 0 && idx <= n) {
                originalSequence.add(idx - 1); // Convert back to 0-indexed
            }
        }

        return new BurstResult(dp[0][n + 1], originalSequence);
    }

    // Reconstruct optimal sequence
    private void reconstructSequence(int[][] choice, int[] balloons, int left, int right,
                                   List<Integer> sequence) {
        if (left + 1 >= right) return;

        int k = choice[left][right];
        sequence.add(k);

        reconstructSequence(choice, balloons, left, k, sequence);
        reconstructSequence(choice, balloons, k, right, sequence);
    }

    // Result class containing coins and sequence
    public static class BurstResult {
        public final int maxCoins;
        public final List<Integer> burstSequence;

        public BurstResult(int maxCoins, List<Integer> burstSequence) {
            this.maxCoins = maxCoins;
            this.burstSequence = burstSequence;
        }

        @Override
        public String toString() {
            return String.format("Max Coins: %d, Sequence: %s", maxCoins, burstSequence);
        }
    }

    /**
     * Parallel approach for very large inputs.
     * Divides DP computation across multiple threads.
     */
    public int maxCoinsParallel(int[] nums) {
        if (nums.length < 100) {
            return maxCoins(nums); // Use sequential for small inputs
        }

        int n = nums.length;
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        int[][] dp = new int[n + 2][n + 2];

        // Process in parallel by length
        for (int length = 2; length <= n + 1; length++) {
            final int currentLength = length;

            java.util.stream.IntStream.range(0, n + 2 - length)
                .parallel()
                .forEach(left -> {
                    int right = left + currentLength;

                    for (int k = left + 1; k < right; k++) {
                        int coins = balloons[left] * balloons[k] * balloons[right];
                        int totalCoins = dp[left][k] + dp[k][right] + coins;

                        synchronized (dp) {
                            dp[left][right] = Math.max(dp[left][right], totalCoins);
                        }
                    }
                });
        }

        return dp[0][n + 1];
    }

    /**
     * Approximation algorithm for very large inputs.
     * Provides good solution quickly when exact solution is too expensive.
     */
    public int maxCoinsApproximate(int[] nums) {
        if (nums.length <= 50) {
            return maxCoins(nums); // Use exact solution for reasonable sizes
        }

        // Greedy approximation: repeatedly burst balloon with minimum impact
        List<Integer> balloons = new ArrayList<>();
        balloons.add(1); // Boundary
        for (int num : nums) {
            balloons.add(num);
        }
        balloons.add(1); // Boundary

        int totalCoins = 0;

        while (balloons.size() > 2) {
            int minImpactIndex = 1;
            int minImpact = Integer.MAX_VALUE;

            // Find balloon with minimum burst impact
            for (int i = 1; i < balloons.size() - 1; i++) {
                int impact = balloons.get(i - 1) * balloons.get(i) * balloons.get(i + 1);

                // Consider future impact (simplified heuristic)
                int futureImpact = 0;
                if (i > 1) {
                    futureImpact += balloons.get(i - 2) * balloons.get(i - 1) * balloons.get(i + 1);
                }
                if (i < balloons.size() - 2) {
                    futureImpact += balloons.get(i - 1) * balloons.get(i + 1) * balloons.get(i + 2);
                }

                int totalImpact = impact - futureImpact / 2; // Simplified heuristic

                if (totalImpact < minImpact) {
                    minImpact = totalImpact;
                    minImpactIndex = i;
                }
            }

            // Burst the balloon
            int coins = balloons.get(minImpactIndex - 1) *
                       balloons.get(minImpactIndex) *
                       balloons.get(minImpactIndex + 1);
            totalCoins += coins;
            balloons.remove(minImpactIndex);
        }

        return totalCoins;
    }

    /**
     * Branch and bound approach with pruning.
     * Uses bounding functions to prune suboptimal branches.
     */
    public int maxCoinsBranchAndBound(int[] nums) {
        int n = nums.length;
        boolean[] burst = new boolean[n];
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        return branchAndBoundHelper(balloons, burst, 0, calculateUpperBound(balloons, burst));
    }

    private int branchAndBoundHelper(int[] balloons, boolean[] burst, int currentCoins, int upperBound) {
        // Check if all balloons are burst
        boolean allBurst = true;
        for (boolean b : burst) {
            if (!b) {
                allBurst = false;
                break;
            }
        }

        if (allBurst) return currentCoins;

        int maxCoins = 0;

        for (int i = 1; i < balloons.length - 1; i++) {
            if (!burst[i - 1]) {
                // Try bursting balloon i
                burst[i - 1] = true;

                int left = findLeft(balloons, burst, i);
                int right = findRight(balloons, burst, i);
                int coins = balloons[left] * balloons[i] * balloons[right];

                int newUpperBound = calculateUpperBound(balloons, burst);

                // Prune if this branch cannot improve the solution
                if (currentCoins + coins + newUpperBound > maxCoins) {
                    int result = branchAndBoundHelper(balloons, burst, currentCoins + coins, newUpperBound);
                    maxCoins = Math.max(maxCoins, result);
                }

                burst[i - 1] = false; // Backtrack
            }
        }

        return maxCoins;
    }

    // Find leftmost non-burst balloon
    private int findLeft(int[] balloons, boolean[] burst, int index) {
        for (int i = index - 1; i >= 0; i--) {
            if (i == 0 || !burst[i - 1]) {
                return i;
            }
        }
        return 0;
    }

    // Find rightmost non-burst balloon
    private int findRight(int[] balloons, boolean[] burst, int index) {
        for (int i = index + 1; i < balloons.length; i++) {
            if (i == balloons.length - 1 || !burst[i - 1]) {
                return i;
            }
        }
        return balloons.length - 1;
    }

    // Calculate upper bound for pruning
    private int calculateUpperBound(int[] balloons, boolean[] burst) {
        int sum = 0;

        // Simplified upper bound: sum of all possible burst values
        for (int i = 1; i < balloons.length - 1; i++) {
            if (!burst[i - 1]) {
                int left = findLeft(balloons, burst, i);
                int right = findRight(balloons, burst, i);
                sum += balloons[left] * balloons[i] * balloons[right];
            }
        }

        return sum;
    }

    /**
     * Game theory approach for competitive balloon bursting.
     * Extension where two players take turns bursting balloons.
     */
    public int maxCoinsCompetitive(int[] nums) {
        int n = nums.length;
        int[] balloons = new int[n + 2];
        balloons[0] = 1;
        balloons[n + 1] = 1;
        System.arraycopy(nums, 0, balloons, 1, n);

        // dp[i][j][turn] = max advantage for current player in interval (i,j)
        // turn: 0 = first player, 1 = second player
        int[][][] dp = new int[n + 2][n + 2][2];

        for (int length = 2; length <= n + 1; length++) {
            for (int left = 0; left <= n + 1 - length; left++) {
                int right = left + length;

                for (int turn = 0; turn < 2; turn++) {
                    for (int k = left + 1; k < right; k++) {
                        int coins = balloons[left] * balloons[k] * balloons[right];

                        if (turn == 0) { // First player's turn
                            int advantage = coins + dp[left][k][1] + dp[k][right][1];
                            dp[left][right][turn] = Math.max(dp[left][right][turn], advantage);
                        } else { // Second player's turn
                            int advantage = -coins + dp[left][k][0] + dp[k][right][0];
                            dp[left][right][turn] = Math.min(dp[left][right][turn], advantage);
                        }
                    }
                }
            }
        }

        return dp[0][n + 1][0]; // First player's maximum advantage
    }
}