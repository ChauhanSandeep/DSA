package strings.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Problem: Custom Sort String
 *
 * Given a custom order string and another string, permute the second string so
 * characters that appear in order follow that relative order. Characters absent
 * from order may appear after the ordered characters.
 *
 * Leetcode: https://leetcode.com/problems/custom-sort-string/ (Medium)
 * Rating:   acceptance 72.4% (Medium), contest rating 1424
 * Pattern:  Sorting | Custom comparator | Character priority map
 *
 * Example:
 *   Input:  order = "cba", s = "abcd"
 *   Output: "cbad"
 *   Why:    c, b, and a must appear in that order; d is not constrained by order.
 *
 * Follow-ups:
 *   1. Optimize for long strings with lowercase letters only?
 *      Count frequencies, emit ordered characters, then emit the leftovers.
 *   2. Support duplicate characters in order?
 *      Use the first occurrence as priority or reject invalid order input.
 *   3. Use a custom alphabet with Unicode?
 *      Store priorities in a Map<Character, Integer> instead of a fixed array.
 *
 * Related: Relative Sort Array (1122), Sort Characters By Frequency (451).
 */
public class CustomSortString {

    public static void main(String[] args) {
        CustomSortString solver = new CustomSortString();
        String[] orders = {"cba", "bcafg", "kqep"};
        String[] inputs = {"abcd", "abcd", "pekeq"};
        String[] expected = {"cbad", "bcad", "kqeep"};

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.customSortString(orders[i], inputs[i]);
            System.out.printf("order=%s s=%s -> %s  expected=%s%n", orders[i], inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: sorting becomes straightforward once every character has a numeric
     * priority. Characters listed in order receive their index as priority; all
     * other characters receive a large default priority so they move after the
     * constrained characters.
     *
     * Algorithm:
     *   1. Return str directly when it is null or empty.
     *   2. Build a map from each order character to its priority index.
     *   3. Box str characters into an array so a comparator can sort them.
     *   4. Sort by mapped priority and append the sorted characters into the result.
     *
     * Time:  O(n log n) - sorting n characters dominates.
     * Space: O(n + m) - boxed characters plus the priority map for order.
     *
     * @param order custom character ordering
     * @param str string to permute according to order
     * @return a permutation of str that respects order
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

        // Convert string to Character array (boxed) for sorting with custom comparator
        // Note: We need Character[] (not char[]) because Arrays.sort with Comparator
        // only works with Object arrays, not primitive arrays
        Character[] chars = new Character[str.length()];
        for (int i = 0; i < str.length(); i++) {
            chars[i] = str.charAt(i);
        }

        // Sort using custom priority (characters not in order get high priority to ppear at end)
        Arrays.sort(chars, (c1, c2) -> {
            int priority1 = charPriority.getOrDefault(c1, Integer.MAX_VALUE);
            int priority2 = charPriority.getOrDefault(c2, Integer.MAX_VALUE);
            return Integer.compare(priority1, priority2);
        });

        // Convert back to string
        StringBuilder result = new StringBuilder();
        for (char c : chars) {
            result.append(c);
        }
        return result.toString();
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
     * Space Complexity: O(1) as we use fixed-size array for counting (26 lowercase
     * letters)
     *
     * @param order the string defining custom sort order
     * @param str   the string to be sorted according to custom order
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

    /** Appends the same character count times to the builder. */
    private void appendCharacterMultipleTimes(StringBuilder sb, char c, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
    }
}
