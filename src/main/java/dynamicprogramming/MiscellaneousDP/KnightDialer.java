package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * 935. Knight Dialer
 *
 * Problem: A chess knight can move as indicated in the chess diagram below.
 * We place our knight on any numbered key of a phone pad, and it makes n-1 hops.
 * How many distinct numbers can you dial in n hops?
 *
 * Example:
 * Input: n = 1
 * Output: 10
 * Explanation: We need to dial 1 number, so placing on any key gives 10 possibilities.
 *
 * Input: n = 2
 * Output: 20
 *
 * LeetCode: https://leetcode.com/problems/knight-dialer
 *
 * Follow-up questions:
 * Q: What if the phone pad layout is different?
 * A: Modify the adjacency mapping based on the new layout.
 *
 * Q: How to optimize for very large n?
 * A: Use matrix exponentiation to achieve O(log n) complexity.
 *
 * Q: Can we count paths that avoid certain numbers?
 * A: Modify DP state to exclude forbidden positions.
 * LeetCode Contest Rating: 1690
 */
public class KnightDialer {

    private static final int MOD = 1000000007;

    // Knight moves from each digit (precomputed adjacency list)
    private static final int[][] MOVES = {
        {4, 6},       // 0 can go to 4, 6
        {6, 8},       // 1 can go to 6, 8
        {7, 9},       // 2 can go to 7, 9
        {4, 8},       // 3 can go to 4, 8
        {0, 3, 9},    // 4 can go to 0, 3, 9
        {},           // 5 has no valid moves
        {0, 1, 7},    // 6 can go to 0, 1, 7
        {2, 6},       // 7 can go to 2, 6
        {1, 3},       // 8 can go to 1, 3
        {2, 4}        // 9 can go to 2, 4
    };

    /**
     * Dynamic Programming approach with memoization.
     *
     * Algorithm: Top-down DP
     * - For each digit and remaining moves, calculate number of paths
     * - Use memoization to avoid recalculating same states
     * - Sum up paths starting from all digits
     *
     * Time Complexity: O(n) after memoization
     * Space Complexity: O(n) for memoization table
     */
    public int knightDialer(int n) {
        Integer[][] memo = new Integer[n + 1][10];
        long result = 0;

        // Try starting from each digit
        for (int digit = 0; digit <= 9; digit++) {
            result = (result + dfs(n - 1, digit, memo)) % MOD;
        }

        return (int) result;
    }

    // DFS with memoization
    private long dfs(int remainingMoves, int currentDigit, Integer[][] memo) {
        if (remainingMoves == 0) {
            return 1;
        }

        if (memo[remainingMoves][currentDigit] != null) {
            return memo[remainingMoves][currentDigit];
        }

        long count = 0;
        for (int nextDigit : MOVES[currentDigit]) {
            count = (count + dfs(remainingMoves - 1, nextDigit, memo)) % MOD;
        }

        memo[remainingMoves][currentDigit] = (int) count;
        return count;
    }

    /**
     * Bottom-up Dynamic Programming approach.
     * More space efficient and iterative.
     */
    public int knightDialerBottomUp(int n) {
        if (n == 1) return 10;

        // dp[i] represents number of ways to reach digit i
        long[] dp = new long[10];
        Arrays.fill(dp, 1); // Base case: 1 way to dial each digit in 1 move

        // Calculate for each additional move
        for (int move = 2; move <= n; move++) {
            long[] nextDp = new long[10];

            for (int digit = 0; digit <= 9; digit++) {
                for (int prevDigit : MOVES[digit]) {
                    nextDp[digit] = (nextDp[digit] + dp[prevDigit]) % MOD;
                }
            }

            dp = nextDp;
        }

        // Sum all possibilities
        long result = 0;
        for (long count : dp) {
            result = (result + count) % MOD;
        }

        return (int) result;
    }

    /**
     * Matrix exponentiation approach for very large n.
     * Achieves O(log n) time complexity.
     */
    public int knightDialerMatrix(int n) {
        if (n == 1) return 10;

        // Build transition matrix
        long[][] transition = new long[10][10];
        for (int from = 0; from <= 9; from++) {
            for (int to : MOVES[from]) {
                transition[to][from] = 1;
            }
        }

        // Matrix exponentiation
        long[][] result = matrixPower(transition, n - 1);

        // Sum all entries (starting from any digit, ending at any digit)
        long total = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                total = (total + result[i][j]) % MOD;
            }
        }

        return (int) total;
    }

    // Matrix exponentiation helper
    private long[][] matrixPower(long[][] matrix, int power) {
        int n = matrix.length;
        long[][] result = new long[n][n];

        // Initialize result as identity matrix
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }

        while (power > 0) {
            if (power % 2 == 1) {
                result = multiplyMatrix(result, matrix);
            }
            matrix = multiplyMatrix(matrix, matrix);
            power /= 2;
        }

        return result;
    }

    // Matrix multiplication with modulo
    private long[][] multiplyMatrix(long[][] a, long[][] b) {
        int n = a.length;
        long[][] result = new long[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }

        return result;
    }

    /**
     * Space-optimized iterative approach.
     * Uses only O(1) extra space by utilizing the structure of moves.
     */
    public int knightDialerOptimized(int n) {
        if (n == 1) return 10;

        // Group digits by their movement patterns
        // Group A: 0, group B: 1,3,7,9, group C: 2,8, group D: 4,6
        long a = 1, b = 4, c = 2, d = 2; // Initial counts for each group

        for (int i = 2; i <= n; i++) {
            long newA = (2 * d) % MOD;                    // 0 comes from 4,6
            long newB = (2 * c + 2 * d) % MOD;           // 1,3,7,9 come from 2,8,4,6
            long newC = (2 * b) % MOD;                    // 2,8 come from 1,3,7,9
            long newD = (a + 2 * b) % MOD;               // 4,6 come from 0,1,3,7,9

            a = newA;
            b = newB;
            c = newC;
            d = newD;
        }

        return (int) ((a + b + c + d) % MOD);
    }

    /**
     * Alternative DP approach tracking individual positions.
     * More explicit about phone pad layout.
     */
    public int knightDialerExplicit(int n) {
        // Phone pad layout:
        // 1 2 3
        // 4 5 6
        // 7 8 9
        //   0

        Map<Integer, int[]> positions = new HashMap<>();
        positions.put(1, new int[]{0, 0});
        positions.put(2, new int[]{0, 1});
        positions.put(3, new int[]{0, 2});
        positions.put(4, new int[]{1, 0});
        positions.put(5, new int[]{1, 1});
        positions.put(6, new int[]{1, 2});
        positions.put(7, new int[]{2, 0});
        positions.put(8, new int[]{2, 1});
        positions.put(9, new int[]{2, 2});
        positions.put(0, new int[]{3, 1});

        Map<int[], Integer> positionToDigit = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : positions.entrySet()) {
            positionToDigit.put(entry.getValue(), entry.getKey());
        }

        Integer[][][] memo = new Integer[n + 1][4][3];
        long result = 0;

        for (int digit = 0; digit <= 9; digit++) {
            int[] pos = positions.get(digit);
            result = (result + dfsExplicit(n - 1, pos[0], pos[1], memo, positionToDigit)) % MOD;
        }

        return (int) result;
    }

    // DFS with explicit position tracking
    private long dfsExplicit(int remainingMoves, int row, int col, Integer[][][] memo,
                           Map<int[], Integer> positionToDigit) {
        if (remainingMoves == 0) return 1;
        if (row < 0 || row >= 4 || col < 0 || col >= 3) return 0;
        if (row == 3 && col != 1) return 0; // Only position (3,1) is valid in bottom row
        if (row == 1 && col == 1) return 0; // Position (1,1) is digit 5, no moves allowed

        if (memo[remainingMoves][row][col] != null) {
            return memo[remainingMoves][row][col];
        }

        // Knight moves: ±2 in one direction, ±1 in perpendicular direction
        int[][] knightMoves = {{-2,-1}, {-2,1}, {-1,-2}, {-1,2}, {1,-2}, {1,2}, {2,-1}, {2,1}};

        long count = 0;
        for (int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            count = (count + dfsExplicit(remainingMoves - 1, newRow, newCol, memo, positionToDigit)) % MOD;
        }

        memo[remainingMoves][row][col] = (int) count;
        return count;
    }

    /**
     * Recursive approach without memoization (for small n only).
     * Demonstrates the basic recurrence relation.
     */
    public int knightDialerRecursive(int n) {
        if (n == 1) return 10;

        long result = 0;
        for (int digit = 0; digit <= 9; digit++) {
            result = (result + countPaths(n - 1, digit)) % MOD;
        }

        return (int) result;
    }

    // Recursive path counting (inefficient without memoization)
    private long countPaths(int remainingMoves, int currentDigit) {
        if (remainingMoves == 0) return 1;

        long count = 0;
        for (int nextDigit : MOVES[currentDigit]) {
            count = (count + countPaths(remainingMoves - 1, nextDigit)) % MOD;
        }

        return count;
    }
}