package strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Problem: Custom Sort String
 *
 * You are given two strings order and s. All the characters of order are unique and were
 * sorted in some custom order previously. Permute the characters of s so that they match
 * the order that order was sorted. More specifically, if character x occurs before
 * character y in order, then x should occur before y in the permuted string.
 *
 * Return any permutation of s that satisfies this property.
 *
 * Example:
 * Input: order = "cba", s = "abcd"
 * Output: "cbad"
 * Explanation:
 * - "c", "b", "a" appear in order, so the order should be "c", "b", "a"
 * - "d" is not in order, so it can be placed anywhere (placed at end here)
 *
 * Input: order = "cbafg", s = "abcd"
 * Output: "cbad"
 * Explanation: Characters "c", "b", "a" from order come first in that order, then "d"
 *
 * LeetCode: https://leetcode.com/problems/custom-sort-string/
 *
 * Follow-up Questions:
 * 1. Q: What if the order string contains duplicate characters?
 *    A: Problem states order has unique characters, but we could handle by using first occurrence.
 *
 * 2. Q: How would you handle Unicode characters or case sensitivity?
 *    A: Current solution works with Unicode. For case-insensitive, convert to same case first.
 *
 * 3. Q: What about very large strings with memory constraints?
 *    A: Consider counting approach to avoid creating character arrays, or streaming approach.
 *
 * 4. Q: How would you optimize if order string is much larger than s?
 *    A: Use Set for faster lookups when checking if character exists in order.
 *
 * Related Problems:
 * - Sort Array by Increasing Frequency: https://leetcode.com/problems/sort-array-by-increasing-frequency/
 * - Relative Sort Array: https://leetcode.com/problems/relative-sort-array/
 * - Valid Anagram: https://leetcode.com/problems/valid-anagram/
 */
public class CustomSortString {

    /**
     * Sorts string str according to custom order using priority-based sorting.
     *
     * Algorithm:
     * 1. Create priority mapping where each character in order gets index as priority
     * 2. Characters not in order get default priority (placed at end)
     * 3. Convert string to character array and sort using custom comparator
     * 4. Join sorted characters back into string
     *
     * Time Complexity: O(n log n) where n is length of string str
     * Space Complexity: O(n) for character array and priority mapping
     *
     * @param order the string defining custom sort order
     * @param str the string to be sorted according to custom order
     * @return string str sorted according to order
     */
    public String customSortString(String order, String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Create priority mapping for characters in order
        Map<Character, Integer> charPriority = new HashMap<>();
        for (int i = 0; i < order.length(); i++) {
            charPriority.put(order.charAt(i), i);
        }

        // Convert string to list for sorting
        List<Character> charList = new ArrayList<>();
        for (char c : str.toCharArray()) {
            charList.add(c);
        }

        // Sort using custom priority (characters not in order get high priority to appear at end)
        charList.sort((c1, c2) -> {
            int priority1 = charPriority.getOrDefault(c1, Integer.MAX_VALUE);
            int priority2 = charPriority.getOrDefault(c2, Integer.MAX_VALUE);
            return Integer.compare(priority1, priority2);
        });

        // Convert back to string
        return charList.stream()
            .map(obj -> String.valueOf(obj))
            .collect(Collectors.joining());
    }

    /**
     * Alternative approach using frequency counting and reconstruction.
     * More efficient for strings with many repeated characters.
     *
     * Algorithm:
     * 1. Count frequency of each character in string str
     * 2. Iterate through order string and append each character based on its count
     * 3. Append remaining characters (not in order) at the end
     * 4. Return reconstructed string
     *
     * Time Complexity: O(n + m) where n is length of str and m is length of order
     * Space Complexity: O(1) as we use fixed-size array for counting (26 lowercase letters)
     *
     * @param order the string defining custom sort order
     * @param str the string to be sorted according to custom order
     * @return string str sorted according to order
     */
    public String customSortStringOptimized(String order, String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Count frequency of each character in str
        int[] charCount = new int[26];
        for (char c : str.toCharArray()) {
            charCount[c - 'a']++;
        }

        StringBuilder result = new StringBuilder();

        // Process characters in order sequence
        for (char orderChar : order.toCharArray()) {
            int count = charCount[orderChar - 'a'];
            // Append all occurrences of current character
            appendCharacterMultipleTimes(result, orderChar, count);
            // Mark character as processed
            charCount[orderChar - 'a'] = 0;
        }

        // Append remaining characters (not in order)
        for (int i = 0; i < 26; i++) {
            if (charCount[i] > 0) {
                char remainingChar = (char) (i + 'a');
                appendCharacterMultipleTimes(result, remainingChar, charCount[i]);
            }
        }

        return result.toString();
    }

    // Helper method to append character multiple times efficiently
    private void appendCharacterMultipleTimes(StringBuilder sb, char c, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
    }
}
