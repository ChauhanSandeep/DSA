package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;

/**
 * Problem: Coins in a Line
 *
 * Two players alternately take one coin from either end of a line of coins. Both
 * play optimally. Return the maximum value the first player can collect.
 *
 * Source: InterviewBit - Coins in a Line
 * Pattern:  Dynamic programming | Interval DP | Minimax recurrence
 *
 * Example:
 *   Input:  coins = [5,4,8,10]
 *   Output: 15
 *   Why:    the first player can take 10 first; whatever the opponent does, the
 *           remaining best guaranteed total for the first player is 15.
 *
 * Follow-ups:
 *   1. Return the first player's moves?
 *      Store whether left or right was chosen for each interval while filling DP.
 *   2. What if players may take one or two coins per turn?
 *      Expand the interval transition to all legal left/right take counts.
 *   3. What if there are three players?
 *      The state must track whose turn it is and each player's score vector, not just one max value.
 *
 * Related: Predict the Winner (486), Stone Game (877).
 */
public class CoinsInLine {

    public static void main(String[] args) {
        CoinsInLine solver = new CoinsInLine();
        int[][] inputs = {{}, {5}, {5, 4, 8, 10}, {8, 15, 3, 7}};
        int[] expected = {0, 5, 15, 22};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maxCoinsOptimized(inputs[i]);
            System.out.printf("coins=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
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
     * Intuition (interview default): dp[left][right] is the maximum value the
     * current player can collect from that interval. If the current player takes
     * the left coin, the opponent then chooses an end and will leave the smaller of
     * the two future intervals. The same logic applies to taking the right coin.
     * That min inside the recurrence is the opponent's optimal response, while the
     * outer max is our choice.
     *
     * Algorithm:
     *   1. Create dp[leftIndex][rightIndex] for the best score from that interval.
     *   2. Recursively try taking the left coin and the right coin.
     *   3. After each take, assume the opponent chooses the branch that minimizes our future score.
     *   4. Memoize and return the better of the two choices.
     *
     * Time:  O(n^2) - each interval is computed once with O(1) recursive work.
     * Space: O(n^2) - the interval memo table stores every left/right pair.
     *
     * @param coins coin values in order
     * @return maximum value the first player can collect
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
