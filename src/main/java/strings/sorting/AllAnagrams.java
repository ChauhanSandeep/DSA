package strings.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Find All Anagrams in a String
 *
 * Given two strings `text` and `pattern`, return all start indices of `pattern`'s anagrams in `text`.
 *
 * Better Example:
 * Input: text = "xyzabcabaxy", pattern = "ab"
 * Output: [3, 6, 7]
 * Explanation:
 * - "ab" → anagram at index 3
 * - "ba" → anagram at index 6
 * - "ab" → anagram at index 7
 *
 * 🔗 Leetcode: https://leetcode.com/problems/find-all-anagrams-in-a-string/
 *
 * 🔁 Follow-Up Questions:
 * 1. What if the string contains unicode characters?
 *    ➤ Use `Map<Character, Integer>` instead of fixed arrays.
 * 2. Can we find count of distinct anagrams instead of indices?
 *    ➤ Maintain a `Set<String>` of anagram substrings and return its size.
 * 3. What if matching is case-insensitive?
 *    ➤ Normalize the input using `.toLowerCase()` before processing.
 */
public class AllAnagrams {

    public static void main(String[] args) {
        List<Integer> result = new AllAnagrams().findAnagrams("abab", "ab");
        System.out.println(result); // Output: [0, 1, 2]
    }


    /**
     * Steps:
     * 1. Use a sliding window of size equal to `pattern`.
     * 2. Maintain a frequency count of characters in `pattern`.
     * 3. For each window in `text`, compare character counts.
     * 4. If counts match, add the start index to result.
     *
     * Time Complexity: O(N * 26) = O(N) where N = text length
     * Space Complexity: O(26) = O(1) for character counts
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