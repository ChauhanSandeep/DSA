package Array;

import java.util.*;

/**
 * Given an integer n, return the least number of perfect square numbers that sum to n.
 * Example:
 * Input: n = 13
 * Output: 2 (Explanation: 13 = 4 + 9)
 */
public class PerfectSquares {
    public static void main(String[] args) {
        PerfectSquares solution = new PerfectSquares();
        System.out.println("DP Solution: " + solution.numSquaresDp(13));
        System.out.println("BFS Solution: " + solution.numSquaresBFS(13));
    }

    /**
     * Dynamic Programming Approach
     * Time Complexity: O(n * sqrt(n))
     * Space Complexity: O(n)
     */
    public int numSquaresDp(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);

        // Perfect square numbers require only 1 step
        for (int i = 1; i * i <= n; i++) {
            dp[i * i] = 1;
        }

        dp[0] = 0; // Base case

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }

        return dp[n];
    }

    /**
     * Optimized BFS Approach (Shortest Path)
     * Treats the problem as an unweighted graph traversal
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int numSquaresBFS(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(n);
        visited.add(n);
        int level = 0;

        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int num = queue.poll();
                for (int j = 1; j * j <= num; j++) {
                    int remainder = num - j * j;
                    if (remainder == 0) return level;
                    if (!visited.contains(remainder)) {
                        queue.add(remainder);
                        visited.add(remainder);
                    }
                }
            }
        }
        return level;
    }
}
