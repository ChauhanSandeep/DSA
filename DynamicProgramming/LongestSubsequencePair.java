package DynamicProgramming;

import java.util.Arrays;


/**
 * Problem: Maximum Length of Pair Chain
 *
 * You are given an array of `n` pairs where each pair contains two integers (a, b)
 * such that a < b. A pair (c, d) can follow another pair (a, b) if b < c.
 * Find the length of the longest chain which can be formed from given pairs.
 *
 * Leetcode Link:
 * https://leetcode.com/problems/maximum-length-of-pair-chain/
 *
 * Example:
 * Input: pairs = [[5,24],[15,25],[27,40],[50,60]]
 * Output: 3
 * Explanation: The longest chain is [5,24] -> [27,40] -> [50,60]
 *
 * Follow-up Questions:
 * - Can you construct the chain as well? (Yes, using path reconstruction from DP)
 * - What if you can reverse a pair (a,b) to (b,a)? (Careful! Not allowed by default)
 */
public class LongestSubsequencePair {

  public static void main(String[] args) {
    IntervalPair[] pairs =
        {new IntervalPair(5, 24), new IntervalPair(15, 25), new IntervalPair(27, 40), new IntervalPair(50, 60)};

    System.out.println("Using DP (O(N²)) approach: " + findLongestChainDP(pairs));
    System.out.println("Using Greedy (O(N log N)) approach: " + findLongestChainGreedy(pairs));
  }

  /**
   * Dynamic Programming Approach (O(N²))
   * Similar to Longest Increasing Subsequence (LIS).
   *
   * Steps:
   * 1. Sort pairs by start value (x).
   * 2. For each pair, find the longest chain ending at that index by checking previous pairs.
   * 3. Use a DP array where dp[i] = length of chain ending at i.
   *
   * Time Complexity: O(N²)
   * Space Complexity: O(N)
   */
  public static int findLongestChainDP(IntervalPair[] inputPairs) {
      if (inputPairs == null || inputPairs.length == 0) {
          return 0;
      }

    int length = inputPairs.length;
    IntervalPair[] pairs = Arrays.copyOf(inputPairs, length); // avoid modifying input
    Arrays.sort(pairs, (a, b) -> Integer.compare(a.start, b.start));

    int[] dp = new int[length]; // dp[i] = max chain length ending at i
    Arrays.fill(dp, 1); // Base case: each pair is a chain of length 1

    int maxChainLength = 1;

    for (int end = 1; end < length; end++) {
      for (int start = 0; start < end; start++) {
        if (pairs[start].end < pairs[end].start) {
          dp[end] = Math.max(dp[end], dp[start] + 1);
        }
      }
      maxChainLength = Math.max(maxChainLength, dp[end]);
    }

    return maxChainLength;
  }

  /**
   * Greedy Approach (O(N log N))
   *
   * Steps:
   * 1. Sort all pairs by end value.
   * 2. Always choose the earliest finishing pair that does not overlap with the previous.
   *
   * Time Complexity: O(N log N) due to sorting.
   * Space Complexity: O(1)
   */
  public static int findLongestChainGreedy(IntervalPair[] inputPairs) {
      if (inputPairs == null || inputPairs.length == 0) {
          return 0;
      }

    IntervalPair[] pairs = Arrays.copyOf(inputPairs, inputPairs.length); // preserve input
    Arrays.sort(pairs, (a, b) -> Integer.compare(a.end, b.end));

    int maxChainLength = 1;
    int lastSelectedEnd = pairs[0].end;

    for (int i = 1; i < pairs.length; i++) {
      if (pairs[i].start > lastSelectedEnd) {
        maxChainLength++;
        lastSelectedEnd = pairs[i].end;
      }
    }

    return maxChainLength;
  }
}

/**
 * Class representing a pair (x, y) where x < y.
 */
class IntervalPair {
  int start;
  int end;

  public IntervalPair(int start, int end) {
    this.start = start;
    this.end = end;
  }
}