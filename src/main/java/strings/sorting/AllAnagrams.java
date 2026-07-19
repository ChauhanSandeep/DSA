package strings.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Find All Anagrams in a String
 *
 * Given text and pattern, return every starting index where a substring of text
 * is an anagram of pattern. The window length is fixed to pattern.length().
 *
 * Leetcode: https://leetcode.com/problems/find-all-anagrams-in-a-string/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Sliding window | Frequency counts | Fixed-size window
 *
 * Example:
 *   Input:  text = "abab", pattern = "ab"
 *   Output: [0, 1, 2]
 *   Why:    "ab", "ba", and "ab" are all anagrams of the pattern.
 *
 * Follow-ups:
 *   1. Support Unicode characters?
 *      Use a Map<Character, Integer> instead of a 26-entry array.
 *   2. Return distinct anagram substrings instead of indices?
 *      Add matching windows to a Set<String>.
 *   3. Handle case-insensitive matching?
 *      Normalize both text and pattern before building counts.
 *
 * Related: Permutation in String (567), Valid Anagram (242), Group Anagrams (49).
 */
public class AllAnagrams {

    public static void main(String[] args) {
        AllAnagrams solver = new AllAnagrams();
        String[] texts = {"abab", "cbaebabacd", "aa"};
        String[] patterns = {"ab", "abc", "bb"};
        int[][] expected = { {0, 1, 2}, {0, 6}, {} };

        for (int i = 0; i < texts.length; i++) {
            java.util.List<Integer> got = solver.findAnagrams(texts[i], patterns[i]);
            System.out.printf("text=%s pattern=%s -> %s  expected=%s%n",
                texts[i], patterns[i], got, java.util.Arrays.toString(expected[i]));
        }
    }



    /**
     * Intuition: every candidate substring has the same length as pattern. Maintain
     * one sliding window and a deficit count: when the window contains exactly the
     * needed characters, matchCount drops to zero and the left index is an answer.
     *
     * Algorithm:
     *   1. Return an empty result when text or pattern is null, or pattern is longer than text.
     *   2. Count needed pattern characters in charCount.
     *   3. Expand the right side, decrementing needed counts and matchCount for useful characters.
     *   4. Shrink when the window is too large, restoring counts; record left when matchCount is zero.
     *
     * Time:  O(n) - each text character enters and leaves the window at most once.
     * Space: O(1) - the frequency array has 26 entries.
     *
     * @param text string to search
     * @param pattern anagram pattern to match
     * @return start indices of all anagram windows
     */
    public List<Integer> findAnagrams(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (text == null || pattern == null || text.length() < pattern.length()) {
            return result;
        }

        int[] charCount = new int[26];
        for (char c : pattern.toCharArray()) {
            charCount[c - 'a']++;
        }

        int left = 0, right = 0, matchCount = pattern.length();

        while (right < text.length()) {
            char rightChar = text.charAt(right);
            // If the character is part of the pattern, decrease match count
            if (charCount[rightChar - 'a']-- > 0) {
                matchCount--;
            }

            // If the window size exceeds pattern length, move left pointer
            if (right - left + 1 > pattern.length()) {
                char leftChar = text.charAt(left);
                // If the left character was part of the pattern, increase match count
                if (charCount[leftChar - 'a']++ >= 0) {
                    matchCount++;
                }
                left++;
            }

            if (matchCount == 0) {
                result.add(left);
            }

            right++;
        }

        return result;
    }
}