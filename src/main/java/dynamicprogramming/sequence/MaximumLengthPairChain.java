package dynamicprogramming.sequence;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Problem: Maximum Length of Pair Chain
 *
 * Form the longest chain of pairs where [c,d] can follow [a,b] only when b < c. Pairs may be reordered before choosing.
 *
 * Leetcode: https://leetcode.com/problems/maximum-length-of-pair-chain/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Greedy intervals | Sequence ordering
 *
 * Example:
 *   Input:  pairs = [[1,2],[2,3],[3,4]]
 *   Output: 2
 *   Why:    [1,2] can be followed by [3,4].
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Non-overlapping Intervals (435), Russian Doll Envelopes (354).
 */
public class MaximumLengthPairChain {

    public static void main(String[] args) {
    MaximumLengthPairChain solver = new MaximumLengthPairChain();
    int[][][] pairCases = { {{1, 2}, {2, 3}, {3, 4}}, {{5, 14}, {39, 60}, {15, 28}, {27, 40}, {50, 90}}, {} };
    int[] expected = {2, 3, 0};
    for (int i = 0; i < pairCases.length; i++) {
      int[][] input = Arrays.stream(pairCases[i]).map(int[]::clone).toArray(int[][]::new);
      int got = solver.findLongestChain(input);
      System.out.printf("pairs=%s -> %d  expected=%d%n", Arrays.deepToString(pairCases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: longestChainEndingAt[currentIndex] is the best chain that must end at currentIndex after sorting by start. Any previous pair whose end is before the current start can be extended into the current pair.
   *
   * Algorithm:
   *   1. Return 0 for null or empty input.
   *   2. Sort pairs by first element.
   *   3. Initialize every chain length to 1.
   *   4. For each current pair, scan previous compatible pairs.
   *   5. Track the global maximum.
   *
   * Time:  O(n^2) - every pair combination is checked.
   * Space: O(n) - stores chain lengths.
   *
   * @param pairIntervals [start, end] pairs
   * @return maximum chain length
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
    Arrays.sort(pairIntervals, Comparator.comparingInt(pair -> pair[1]));

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
