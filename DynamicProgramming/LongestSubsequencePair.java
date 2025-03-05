package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Maximum Length of Chain of Pairs
 * 
 * Given an array of pairs (x, y), where `x < y`, find the longest chain such that
 * for every pair (a, b) -> (c, d), `b < c` (i.e., they don’t overlap).
 * 
 * Approaches:
 * 1. **O(N²) DP Approach** (Classic LIS Variation)
 *    - Sort pairs by first element.
 *    - Use DP to find the longest valid chain where `prev.y < curr.x`.
 * 
 * 2. **O(N log N) Greedy Approach** (Optimized)
 *    - Sort pairs by their second element.
 *    - Select the first non-overlapping pair iteratively.
 * 
 * Time Complexity:
 * - **DP Approach**: O(N²)
 * - **Greedy Approach**: O(N log N) (Sorting + Single Pass)
 */
public class LongestSubsequencePair {
    public static void main(String[] args) {
        Pair[] pairs = {
            new Pair(5, 24), new Pair(15, 25), new Pair(27, 40), new Pair(50, 60)
        };

        System.out.println("LIS using DP (O(N²)): " + findLongestChainDP(pairs));
        System.out.println("LIS using Greedy (O(N log N)): " + findLongestChainGreedy(pairs));
    }

    /**
     * Approach 1: O(N²) Dynamic Programming
     * Similar to Longest Increasing Subsequence (LIS)
     */
    public static int findLongestChainDP(Pair[] pairList) {
        if (pairList == null || pairList.length == 0) return 0;

        int n = pairList.length;
        Arrays.sort(pairList, (a, b) -> Integer.compare(a.x, b.x)); // Sort by first element

        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Initialize LIS length as 1 for each element

        int maxLength = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (pairList[j].y < pairList[i].x) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }

    /**
     * Approach 2: O(N log N) Greedy Algorithm
     * - Sort pairs by their second element.
     * - Select pairs greedily by choosing the first non-overlapping one.
     */
    public static int findLongestChainGreedy(Pair[] pairList) {
        if (pairList == null || pairList.length == 0) return 0;

        Arrays.sort(pairList, (a, b) -> Integer.compare(a.y, b.y)); // Sort by second element

        int maxLength = 1, prevEnd = pairList[0].y;
        for (int i = 1; i < pairList.length; i++) {
            if (pairList[i].x > prevEnd) { // Non-overlapping condition
                maxLength++;
                prevEnd = pairList[i].y;
            }
        }

        return maxLength;
    }
}

/**
 * Pair class representing (x, y) intervals
 */
class Pair {
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
