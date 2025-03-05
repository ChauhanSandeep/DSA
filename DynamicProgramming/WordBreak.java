package DynamicProgramming;

import java.util.*;

/**
 * Problem: Word Break
 * LeetCode: https://leetcode.com/problems/word-break/
 *
 * Given a string `s` and a dictionary of words `wordDict`, determine if `s` can be segmented
 * into a space-separated sequence of one or more dictionary words.
 *
 * Constraints:
 * - The same word in `wordDict` may be reused multiple times in the segmentation.
 *
 * Approach 1: Recursion + Memoization (Top-Down Dynamic Programming)
 * - Try to break `s` at every index where a valid dictionary word is found.
 * - Use memoization (`Boolean[] memo`) to store results of subproblems.
 * - Time Complexity: O(N^2), Space Complexity: O(N) (recursion depth + memo array).
 *
 * Approach 2: Iterative Dynamic Programming (Bottom-Up)
 * - Use a boolean DP table `dp[i]` where `dp[i]` is true if `s[0...i-1]` can be segmented.
 * - Time Complexity: O(N^2), Space Complexity: O(N) (DP array).
 */
public class WordBreak {
    public static void main(String[] args) {
        String s;
        List<String> dict;

        // Test Case 1: Long string with valid segmentation
        s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        dict = Arrays.asList("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "ab");
        System.out.println("Recursive: " + wordBreakRecursive(s, dict));
        System.out.println("Iterative: " + wordBreakIterative(s, dict));

        // Test Case 2: Long string with no valid segmentation
        s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        dict = Arrays.asList("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa");
        System.out.println("Recursive: " + wordBreakRecursive(s, dict));
        System.out.println("Iterative: " + wordBreakIterative(s, dict));

        // Test Case 3: Simple valid case
        s = "leetcode";
        dict = Arrays.asList("leet", "code");
        System.out.println("Recursive: " + wordBreakRecursive(s, dict));
        System.out.println("Iterative: " + wordBreakIterative(s, dict));
    }

    /**
     * Approach 1: Recursive + Memoization
     */
    public static boolean wordBreakRecursive(String s, List<String> wordDict) {
        Boolean[] memo = new Boolean[s.length()];
        Set<String> wordSet = new HashSet<>(wordDict);
        return wordBreakHelper(s, 0, wordSet, memo);
    }

    private static boolean wordBreakHelper(String str, int start, Set<String> dict, Boolean[] memo) {
        if (start == str.length()) return true;
        if (memo[start] != null) return memo[start];

        for (int end = start + 1; end <= str.length(); end++) {
            if (dict.contains(str.substring(start, end))) {
                if (wordBreakHelper(str, end, dict, memo)) {
                    return memo[start] = true;
                }
            }
        }
        return memo[start] = false;
    }

    /**
     * Approach 2: Iterative Dynamic Programming (Bottom-Up)
     */
    public static boolean wordBreakIterative(String s, List<String> wordDict) {
        int n = s.length();
        Set<String> wordSet = new HashSet<>(wordDict);
        boolean[] dp = new boolean[n + 1];

        // Base case: an empty string is always segmentable
        dp[0] = true;

        // Build the DP table
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}
