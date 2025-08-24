package dynamicprogramming;

import java.util.Arrays;


/**
 * Problem: Maximum Length of Pair Chain
 *
 * You are given an array of n pairs where pairs[i] = [lefti, righti] and lefti < righti.
 * A pair p2 = [c, d] follows a pair p1 = [a, b] if b < c. A chain of pairs can be formed in this fashion.
 * Return the length longest chain which can be formed.
 * You do not need to use up all the given intervals. You can select pairs in any order.
 *
 * Example:
 * Input: pairs = [[1,2],[2,3],[3,4]]
 * Output: 2
 * Explanation: The longest chain is [1,2] → [3,4].
 *
 * LeetCode Problem Link: https://leetcode.com/problems/maximum-length-of-pair-chain/
 *
 * Follow-up Questions:
 * 1. Q: What if we need to find the actual chain sequence, not just length?
 *    A: Store parent pointers in DP approach or track selected pairs in greedy approach
 * 2. Q: What if pairs can overlap (b <= c instead of b < c)?
 *    A: Change condition to pairs[j][1] <= pairs[i][0] in DP transition
 * 3. Q: Find minimum number of chains to cover all pairs?
 *    A: This becomes interval scheduling - sort by end time and use greedy approach
 * 4. Q: What if each pair has a weight and we want maximum weight chain?
 *    A: Modify DP to track maximum weight instead of length: dp[i] = max(dp[i], dp[j] + weight[i])
 * 5. Q: Maximum number of non-overlapping intervals?
 *    A: Classic activity selection problem - greedy by earliest finish time
 *       (https://leetcode.com/problems/non-overlapping-intervals/)
 */
public class MaximumLengthPairChain {

  public static void main(String[] args) {
    int[][] pairIntervals = {{5, 14}, {39, 60}, {15, 28}, {27, 40}, {50, 90}};
    MaximumLengthPairChain solver = new MaximumLengthPairChain();
    System.out.println("Maximum chain length (DP): " + solver.findLongestChain(pairIntervals));
    System.out.println("Maximum chain length (Greedy): " + solver.findLongestChainGreedy(pairIntervals));
  }

  /**
   * Finds maximum length of pair chain using dynamic programming approach.
   *
   * Algorithm Steps:
   * 1. Sort pairs by first element to ensure proper ordering for DP
   * 2. Initialize DP array where dp[i] represents longest chain ending at index i
   * 3. For each pair, check all previous pairs to find valid extensions
   * 4. Update dp[i] with maximum chain length that can end at position i
   * 5. Return maximum value in dp array
   *
   * Time Complexity: O(n^2) for nested loops to check all pair combinations
   * Space Complexity: O(n) for DP array storage
   *
   * @param pairIntervals Array of pairs where each pair is [start, end]
   * @return Maximum length of chain that can be formed
   */
  public int findLongestChain(int[][] pairIntervals) {
    if (pairIntervals == null || pairIntervals.length == 0) {
      return 0;
    }

    int numPairs = pairIntervals.length;

    // Sort pairs by first element to ensure correct DP ordering
    Arrays.sort(pairIntervals, (firstPair, secondPair) -> Integer.compare(firstPair[0], secondPair[0]));

    // DP array where dp[i] represents longest chain ending at index i
    int[] longestChainEndingAt = new int[numPairs];
    Arrays.fill(longestChainEndingAt, 1); // Each pair forms chain of length 1

    int maxChainLength = 1;

    // Build DP table by checking all previous pairs for valid extensions
    for (int currentIndex = 1; currentIndex < numPairs; currentIndex++) {
      for (int previousIndex = 0; previousIndex < currentIndex; previousIndex++) {

        // Check if previous pair can be chained with current pair
        if (pairIntervals[previousIndex][1] < pairIntervals[currentIndex][0]) {
          longestChainEndingAt[currentIndex] =
              Math.max(longestChainEndingAt[currentIndex], longestChainEndingAt[previousIndex] + 1);
        }
      }

      // Update global maximum
      maxChainLength = Math.max(maxChainLength, longestChainEndingAt[currentIndex]);
    }

    return maxChainLength;
  }

  /**
   * Finds maximum length of pair chain using greedy approach (optimal solution).
   *
   * Algorithm Steps:
   * 1. Sort pairs by second element (end time) in ascending order
   * 2. Greedily select pairs with earliest end time that don't conflict
   * 3. A pair is valid if its start > last selected pair's end
   * 4. This greedy choice is optimal because selecting earliest end maximizes future options
   *
   * Proof of Correctness:
   * - By choosing pair with earliest end time, we leave maximum room for future pairs
   * - Any other choice would end later, potentially blocking more future pairs
   * - This leads to optimal solution (Activity Selection Problem)
   *
   * Time Complexity: O(n log n) for sorting pairs
   * Space Complexity: O(1) excluding input storage
   *
   * @param pairIntervals Array of pairs where each pair is [start, end]
   * @return Maximum length of chain that can be formed
   */
  public int findLongestChainGreedy(int[][] pairIntervals) {
    if (pairIntervals == null || pairIntervals.length == 0) {
      return 0;
    }

    // Sort pairs by end time (second element) for greedy selection
    Arrays.sort(pairIntervals, (firstPair, secondPair) -> Integer.compare(firstPair[1], secondPair[1]));

    int chainLength = 0;
    int lastSelectedEnd = Integer.MIN_VALUE;

    // Greedily select non-overlapping pairs
    for (int[] currentPair : pairIntervals) {
      int currentStart = currentPair[0];
      int currentEnd = currentPair[1];

      // Select pair if it doesn't overlap with last selected pair
      if (currentStart > lastSelectedEnd) {
        chainLength++;
        lastSelectedEnd = currentEnd;
      }
    }

    return chainLength;
  }

  /**
   * Alternative DP approach that also returns the actual chain sequence.
   *
   * Algorithm Steps:
   * 1. Use DP to find longest chain length
   * 2. Store parent pointers to reconstruct chain
   * 3. Backtrack from optimal ending position to build actual sequence
   *
   * Time Complexity: O(n^2) for DP computation
   * Space Complexity: O(n) for DP array and parent tracking
   *
   * @param pairIntervals Array of pairs where each pair is [start, end]
   * @return Array containing the actual longest chain sequence
   */
  public int[][] findLongestChainWithSequence(int[][] pairIntervals) {
    if (pairIntervals == null || pairIntervals.length == 0) {
      return new int[0][0];
    }

    int numPairs = pairIntervals.length;

    // Sort pairs by first element
    Arrays.sort(pairIntervals, (a, b) -> Integer.compare(a[0], b[0]));

    int[] longestChainEndingAt = new int[numPairs];
    int[] parentIndex = new int[numPairs];
    Arrays.fill(longestChainEndingAt, 1);
    Arrays.fill(parentIndex, -1);

    int maxLength = 1;
    int bestEndingIndex = 0;

    // Build DP table with parent tracking
    for (int currentIndex = 1; currentIndex < numPairs; currentIndex++) {
      for (int previousIndex = 0; previousIndex < currentIndex; previousIndex++) {

        if (pairIntervals[previousIndex][1] < pairIntervals[currentIndex][0]) {
          if (longestChainEndingAt[previousIndex] + 1 > longestChainEndingAt[currentIndex]) {
            longestChainEndingAt[currentIndex] = longestChainEndingAt[previousIndex] + 1;
            parentIndex[currentIndex] = previousIndex;
          }
        }
      }

      // Track best ending position
      if (longestChainEndingAt[currentIndex] > maxLength) {
        maxLength = longestChainEndingAt[currentIndex];
        bestEndingIndex = currentIndex;
      }
    }

    // Reconstruct chain sequence
    int[][] chainSequence = new int[maxLength][2];
    int sequenceIndex = maxLength - 1;
    int currentIndex = bestEndingIndex;

    while (currentIndex != -1) {
      chainSequence[sequenceIndex] = pairIntervals[currentIndex];
      sequenceIndex--;
      currentIndex = parentIndex[currentIndex];
    }

    return chainSequence;
  }
}
