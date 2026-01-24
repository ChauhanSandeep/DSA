package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;

/**
 * Problem: Coins in a Line (Two Players Game)
 *
 * Description:
 * - Given `n` coins in a line (where `n` is even), two players take turns picking a coin from either end.
 * - The goal is to maximize the amount collected by the first player.
 * - Both players play optimally.
 *
 * Example:
 *  Input: [5, 4, 8, 10]
 *  Output: 15
 *  Explanation:
 *  - Player 1 picks 10 (right end), Player 2 picks 5 (left end).
 *  Player 1 then picks 8 (right end), Player 2 picks 4 (left end).
 *  Player 1's total = 10 + 8 = 18, Player 2's total = 5 + 4 = 9.
 *  Player 1 wins with a total of 18 coins.
 *
 * Problem Link:
 * https://www.interviewbit.com/problems/coins-in-a-line/
 */
public class CoinsInLine {
    public static void main(String[] args) {
        int[] coins = {5, 4, 8, 10};
        CoinsInLine game = new CoinsInLine();
        System.out.println("Max coins (Recursive DP): " + game.maxCoinsRecursive(coins));
        System.out.println("Max coins (Optimized DP): " + game.maxCoinsOptimized(coins));
    }

    /**
     * Finds maximum coins first player can collect using recursive DP with player tracking.
     *
     * Algorithm:
     * 1. State Definition:
     *    - memo[leftIndex][rightIndex][playerTurn] = max coins first player can collect
     *      from coins[leftIndex...rightIndex] when it's playerTurn's move
     *    - playerTurn: 0 = Player 2 (opponent), 1 = Player 1 (us)
     *
     * 2. Base Case:
     *    - If leftIndex > rightIndex: no coins left, return 0
     *
     * 3. Recursive Cases:
     *    a) Player 1's Turn (maximizing):
     *       - Option 1: Pick left coin → coins[left] + recurse(left+1, right, Player 2)
     *       - Option 2: Pick right coin → coins[right] + recurse(left, right-1, Player 2)
     *       - Choose maximum of both options
     *    
     *    b) Player 2's Turn (minimizing Player 1's score):
     *       - Player 2 plays optimally to minimize Player 1's future gains
     *       - Option 1: Pick left → recurse(left+1, right, Player 1)
     *       - Option 2: Pick right → recurse(left, right-1, Player 1)
     *       - Choose minimum of both (worst case for Player 1)
     *
     * 4. Memoization:
     *    - Cache results to avoid recomputing same subproblems
     *
     * Key Insight: This is a minimax game tree problem. Player 1 maximizes while
     * Player 2 minimizes Player 1's score (which is equivalent to Player 2 maximizing
     * their own score since the game is zero-sum relative to choices).
     *
     * Time Complexity: O(n²) where n is number of coins
     * - State space: O(n²) possible (left, right) pairs × 2 players
     * - Each state computed once due to memoization
     *
     * Space Complexity: O(n²) for memoization table + O(n) recursion stack
     *
     * @param coins array of coin values
     * @return maximum total value first player can collect
     */
    public int maxCoinsRecursive(int[] coins) {
        int numCoins = coins.length;
        
        // memo[leftIndex][rightIndex][playerTurn]
        // playerTurn: 0 = Player 2, 1 = Player 1
        int[][][] memo = new int[numCoins][numCoins][2];
        
        // Initialize memoization table with -1 (indicates uncomputed state)
        for (int[][] matrix : memo) {
            for (int[] row : matrix) {
                Arrays.fill(row, -1);
            }
        }
        
        // Start with full array and Player 1's turn
        return solveRecursive(coins, 0, numCoins - 1, true, memo);
    }

    /**
     * Recursive helper with memoization.
     * Computes maximum coins Player 1 can collect from coins[leftIndex...rightIndex].
     */
    private int solveRecursive(int[] coins, int leftIndex, int rightIndex, 
                               boolean isPlayer1Turn, int[][][] memo) {
        // Base case: no coins left to pick
        if (leftIndex > rightIndex) {
            return 0;
        }
        
        // Convert boolean to index for memoization (1 for Player 1, 0 for Player 2)
        int playerIndex = isPlayer1Turn ? 1 : 0;
        
        // Return cached result if already computed
        if (memo[leftIndex][rightIndex][playerIndex] != -1) {
            return memo[leftIndex][rightIndex][playerIndex];
        }
        
        int result;
        
        if (isPlayer1Turn) {
            // Player 1's turn: maximize their score
            // Option 1: Pick the left coin
            int pickLeftCoin = coins[leftIndex] + solveRecursive(coins, leftIndex + 1, rightIndex, false, memo);
            
            // Option 2: Pick the right coin
            int pickRightCoin = coins[rightIndex] + solveRecursive(coins, leftIndex, rightIndex - 1, false, memo);
            
            // Choose the option that maximizes Player 1's total
            result = Math.max(pickLeftCoin, pickRightCoin);
        } else {
            // Player 2's turn: they play optimally, minimizing Player 1's future score

            // Option 1: Player 2 picks left coin (Player 1 continues with remaining)
            int opponentPicksLeft = solveRecursive(coins, leftIndex + 1, rightIndex, true, memo);
            
            // Option 2: Player 2 picks right coin (Player 1 continues with remaining)
            int opponentPicksRight = solveRecursive(coins, leftIndex, rightIndex - 1, true, memo);
            
            // Player 2 chooses optimally for themselves, which minimizes Player 1's score
            result = Math.min(opponentPicksLeft, opponentPicksRight);
        }
        
        // Cache the result before returning
        memo[leftIndex][rightIndex][playerIndex] = result;
        return result;
    }

    /**
     * Finds maximum coins first player can collect using optimized DP (without player tracking).
     *
     * Algorithm:
     * 1. State Definition:
     *    - dp[leftIndex][rightIndex] = max coins current player can collect
     *      from coins[leftIndex...rightIndex] when it's their turn
     *    - This implicitly handles both players by considering opponent's optimal response
     *
     * 2. Base Case:
     *    - If leftIndex > rightIndex: return 0 (no coins available)
     *
     * 3. Recursive Transition:
     *    When current player picks left coin:
     *    - They get coins[left]
     *    - Opponent plays next from coins[left+1...right]
     *    - Opponent will choose optimally, leaving us with the minimum of:
     *      a) If opponent picks left: we continue with coins[left+2...right]
     *      b) If opponent picks right: we continue with coins[left+1...right-1]
     *    
     *    Similarly for picking right coin:
     *    - They get coins[right]
     *    - Opponent will leave us with minimum of:
     *      a) If opponent picks left: we continue with coins[left+1...right-1]
     *      b) If opponent picks right: we continue with coins[left...right-2]
     *
     * 4. Final Answer:
     *    - Choose maximum between picking left and picking right
     *
     * Key Insight: We don't need to explicitly track whose turn it is. Instead, we
     * compute the maximum the current player can get, assuming the opponent responds
     * optimally. The min() represents the opponent's optimal choice that minimizes
     * our future gains.
     *
     * Time Complexity: O(n²) where n is number of coins
     * - We fill an n×n DP table
     * - Each cell computed once
     *
     * Space Complexity: O(n²) for DP table + O(n) recursion stack
     *
     * @param coins array of coin values
     * @return maximum total value first player can collect
     */
    public int maxCoinsOptimized(int[] coins) {
        int numCoins = coins.length;
        
        // dp[leftIndex][rightIndex] = max coins current player can collect
        // from coins[leftIndex...rightIndex]
        int[][] dp = new int[numCoins][numCoins];
        
        // Start with full array
        return solveOptimized(coins, 0, numCoins - 1, dp);
    }

    /**
     * Optimized recursive helper with memoization.
     * Computes maximum coins current player can collect from coins[leftIndex...rightIndex].
     *
     * The key difference from the first approach: we don't track player turns explicitly.
     * Instead, we assume the opponent always responds optimally (minimizing our future gains).
     *
     * @param coins array of coin values
     * @param leftIndex leftmost available coin index
     * @param rightIndex rightmost available coin index
     * @param dp memoization table
     * @return maximum coins current player can collect from this range
     */
    private int solveOptimized(int[] coins, int leftIndex, int rightIndex, int[][] dp) {
        // Base case: no coins available in this range
        if (leftIndex > rightIndex) {
            return 0;
        }
        
        // Return cached result if already computed
        if (dp[leftIndex][rightIndex] != 0) {
            return dp[leftIndex][rightIndex];
        }
        
        // Choice 1: Current player picks the LEFT coin
        // After picking left, opponent plays optimally from coins[left+1...right]
        // Opponent will choose to minimize our future gains:
        //   - If opponent picks left next: we get coins[left+2...right]
        //   - If opponent picks right next: we get coins[left+1...right-1]
        // Opponent chooses the option that leaves us with LESS
        int pickLeftCoin = coins[leftIndex] + Math.min(
            solveOptimized(coins, leftIndex + 2, rightIndex, dp),      // Opponent picked left
            solveOptimized(coins, leftIndex + 1, rightIndex - 1, dp)   // Opponent picked right
        );
        
        // Choice 2: Current player picks the RIGHT coin
        // After picking right, opponent plays optimally from coins[left...right-1]
        // Opponent will choose to minimize our future gains:
        //   - If opponent picks left next: we get coins[left+1...right-1]
        //   - If opponent picks right next: we get coins[left...right-2]
        // Opponent chooses the option that leaves us with LESS
        int pickRightCoin = coins[rightIndex] + Math.min(
            solveOptimized(coins, leftIndex + 1, rightIndex - 1, dp),  // Opponent picked left
            solveOptimized(coins, leftIndex, rightIndex - 2, dp)       // Opponent picked right
        );
        
        // Current player chooses the option that maximizes their total
        dp[leftIndex][rightIndex] = Math.max(pickLeftCoin, pickRightCoin);
        
        return dp[leftIndex][rightIndex];
    }
}
