package dynamicprogramming;

import java.util.Arrays;


/**
 * Minimum Cost to Merge Stones
 *
 * There are n piles of stones arranged in a row. The ith pile has stones[i] stones.
 * A move consists of merging exactly k consecutive piles into one pile, and the cost
 * of this move is equal to the total number of stones in these k piles.
 *
 * Return the minimum cost to merge all piles into one pile. If it is impossible, return -1.
 *
 * Key constraint: You can only merge exactly k consecutive piles at a time.
 * Each merge operation reduces the number of piles by (k-1).
 *
 * Example:
 * Input: stones = [3,2,4,1], k = 2
 * Output: 20
 * Explanation:
 * - Merge [3,2] for cost 5, left with [5,4,1]
 * - Merge [4,1] for cost 5, left with [5,5]
 * - Merge [5,5] for cost 10, left with [10]
 * Total cost: 5 + 5 + 10 = 20
 *
 * LeetCode: https://leetcode.com/problems/minimum-cost-to-merge-stones/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if different stone types have different merge costs beyond just weight?
 *    Answer: Extend DP state to include stone type combinations and modify cost calculation.
 * 2. How would you handle circular stone arrangements where ends can connect?
 *    Answer: Use circular DP by considering all possible starting points and circular splits.
 * 3. What if we can merge any consecutive subsequence, not just exactly k piles?
 *    Answer: Modify DP transitions to consider all possible merge sizes from 2 to remaining piles.
 * 4. How to optimize for very large arrays with repeated patterns?
 *    Answer: Use matrix exponentiation or identify repeating subproblems for memoization.
 *
 * Related Problems:
 * - LeetCode 312: Burst Balloons (Interval DP)
 * - LeetCode 1130: Minimum Cost Tree From Leaf Values (Interval DP)
 * - LeetCode 375: Guess Number Higher or Lower II (Interval DP)
 */
public class MinimumCostMergeStones {

  public static void main(String[] args) {
    MinimumCostMergeStones solver = new MinimumCostMergeStones();

    // Test case for k=2 (binary merge)
    int[] stones = {3, 2, 4, 1};
    System.out.println("Min cost (k=2): " + solver.mergeStones(stones, 2)); // Expected: 20
  }

  /**
   * Finds minimum cost to merge all stone piles into one using 3D dynamic programming.
   *
   * Algorithm: Interval Dynamic Programming with 3D State
   * State: dp[i][j][m] = minimum cost to merge stones[i-1] to stones[j-1] into exactly m piles
   *
   * Key insights:
   * 1. Impossibility check: (n-1) % (k-1) must equal 0 for merging to be possible
   * 2. Each merge reduces pile count by (k-1), so we need (n-1)/(k-1) total merges
   * 3. To merge range [i,j] into 1 pile: first merge into k piles, then final merge
   * 4. Split points must align with k-merge constraints (increment by k-1)
   *
   * Recurrence Relations:
   * - dp[i][j][m] = min(dp[i][split][1] + dp[split+1][j][m-1]) for valid splits
   * - dp[i][j][1] = dp[i][j][k] + sum(stones[i-1] to stones[j-1])
   *
   * Time Complexity: O(n^3 * k) - three nested loops for range, one for pile count
   * Space Complexity: O(n^2 * k) - 3D DP array plus prefix sum array
   *
   * @param stones array representing stone counts in each pile
   * @param k number of consecutive piles that must be merged together
   * @return minimum cost to merge all piles into one, or -1 if impossible
   */
  public int mergeStones(int[] stones, int k) {
    int size = stones.length;

    // Check impossibility: each merge reduces count by (k-1)
    // To go from n piles to 1 pile, we need (n-1)/(k-1) merges
    if ((size - 1) % (k - 1) != 0) {
      return -1;
    }

    // Build prefix sum array for O(1) range sum queries
    int[] prefixSum = buildPrefixSumArray(stones);

    // Initialize DP table with impossible states as infinity
    int[][][] dp = initializeDpTable(size, k);

    // Fill DP table using interval DP approach
    fillDpTable(dp, prefixSum, size, k);

    return dp[1][size][1];
  }

  /**
   * Builds prefix sum array for efficient range sum calculation.
   * prefixSum[i] = sum of stones[0] to stones[i-1]
   *
   * @param stones original stone array
   * @return prefix sum array with 1-indexed structure
   */
  private int[] buildPrefixSumArray(int[] stones) {
    int numPiles = stones.length;
    int[] prefixSum = new int[numPiles + 1];

    for (int i = 1; i <= numPiles; i++) {
      prefixSum[i] = prefixSum[i - 1] + stones[i - 1];
    }

    return prefixSum;
  }

  /**
   * Initializes 3D DP table with base cases and impossible state markers.
   *
   * @param numPiles number of stone piles
   * @param k merge constraint parameter
   * @return initialized DP table
   */
  private int[][][] initializeDpTable(int numPiles, int k) {
    int[][][] dp = new int[numPiles + 1][numPiles + 1][k + 1];
    final int INFINITY = Integer.MAX_VALUE / 2; // Avoid overflow in additions

    // Initialize all states as impossible (infinity)
    for (int i = 0; i <= numPiles; i++) {
      for (int j = 0; j <= numPiles; j++) {
        Arrays.fill(dp[i][j], INFINITY);
      }
    }

    // Base case: single pile is already 1 pile with zero cost
    for (int i = 1; i <= numPiles; i++) {
      dp[i][i][1] = 0;
    }

    return dp;
  }

  /**
   * Fills DP table using interval dynamic programming approach.
   *
   * Algorithm:
   * 1. Process intervals of increasing length (bottom-up)
   * 2. For each interval [start, end], try forming different pile counts
   * 3. For m piles: try splitting into (1 pile) + (m-1 piles) at valid positions
   * 4. For 1 pile: first form k piles, then merge with final cost
   *
   * @param dp 3D DP table to fill
   * @param prefixSum prefix sum array for range sum queries
   * @param numPiles total number of stone piles
   * @param k merge constraint parameter
   */
  private void fillDpTable(int[][][] dp, int[] prefixSum, int numPiles, int k) {
    // Process intervals of increasing length
    for (int intervalLength = 2; intervalLength <= numPiles; intervalLength++) {
      // Try all possible starting positions for current interval length
      for (int start = 1; start + intervalLength - 1 <= numPiles; start++) {
        int end = start + intervalLength - 1;

        // Try forming different numbers of piles (2 to k)
        for (int targetPiles = 2; targetPiles <= k; targetPiles++) {
          // Try all valid split positions (must align with k-merge constraint)
          for (int split = start; split < end; split += k - 1) {
            // Merge [start, split] into 1 pile, [split+1, end] into (targetPiles-1) piles
            if (dp[start][split][1] != Integer.MAX_VALUE / 2 &&
                dp[split + 1][end][targetPiles - 1] != Integer.MAX_VALUE / 2) {

              dp[start][end][targetPiles] = Math.min(
                  dp[start][end][targetPiles],
                  dp[start][split][1] + dp[split + 1][end][targetPiles - 1]
              );
            }
          }
        }

        // Merge k piles into 1 pile (final merge step)
        if (dp[start][end][k] != Integer.MAX_VALUE / 2) {
          // Cost = cost to form k piles + cost of final merge (sum of all stones)
          int finalMergeCost = prefixSum[end] - prefixSum[start - 1];
          dp[start][end][1] = dp[start][end][k] + finalMergeCost;
        }
      }
    }
  }
}
