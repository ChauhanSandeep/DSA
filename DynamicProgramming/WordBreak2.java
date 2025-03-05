package DynamicProgramming;

import java.util.*;

/**
 * Problem: Word Break II
 * LeetCode: https://leetcode.com/problems/word-break-ii/
 *
 * Given a string `s` and a dictionary of words `wordDict`, return all possible sentences
 * formed by inserting spaces such that each word is a valid dictionary word.
 *
 * Constraints:
 * - The same word in `wordDict` may be reused multiple times.
 * - Return sentences in any order.
 *
 * Approach:
 * - Use recursion with backtracking to explore all possible segmentations.
 * - Optimize with memoization to cache results for previously computed substrings.
 * - Time Complexity: O(N^2 + 2^N), Space Complexity: O(N^2 + 2^N) (due to recursion depth and result storage).
 */
public class WordBreak2 {
    public static void main(String[] args) {
        String s = "catsanddog";
        List<String> wordDict = Arrays.asList("cat", "cats", "and", "sand", "dog");

        System.out.println("Backtracking: " + wordBreakBacktrack(s, wordDict));
        System.out.println("Memoization: " + wordBreakMemoized(s, wordDict));
    }

    /**
     * Approach 1: Recursive Backtracking
     */
    public static List<String> wordBreakBacktrack(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        List<String> result = new ArrayList<>();
        backtrack(s, 0, wordSet, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(String str, int start, Set<String> dict, List<String> currWords, List<String> result) {
        if (start == str.length()) {
            result.add(String.join(" ", currWords));
            return;
        }

        for (int end = start + 1; end <= str.length(); end++) {
            String word = str.substring(start, end);
            if (dict.contains(word)) {
                currWords.add(word);
                backtrack(str, end, dict, currWords, result);
                currWords.remove(currWords.size() - 1); // Backtrack
            }
        }
    }

    /**
     * Approach 2: Recursive + Memoization
     */
    public static List<String> wordBreakMemoized(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        Map<String, List<String>> memo = new HashMap<>();
        return memoizedHelper(s, wordSet, memo);
    }

    private static List<String> memoizedHelper(String str, Set<String> dict, Map<String, List<String>> memo) {
        if (memo.containsKey(str)) return memo.get(str);
        if (str.isEmpty()) return Arrays.asList("");

        List<String> result = new ArrayList<>();
        for (int i = 1; i <= str.length(); i++) {
            String prefix = str.substring(0, i);
            if (dict.contains(prefix)) {
                List<String> suffixWays = memoizedHelper(str.substring(i), dict, memo);
                for (String sentence : suffixWays) {
                    result.add(prefix + (sentence.isEmpty() ? "" : " ") + sentence);
                }
            }
        }
        memo.put(str, result);
        return result;
    }
}
