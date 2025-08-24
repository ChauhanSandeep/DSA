package strings;

/**
 * LeetCode 791. Custom Sort String
 *
 * You are given two strings order and s. All characters of order are unique and were sorted
 * in some custom order previously. Permute characters of s to match the order.
 *
 * Example 1:
 * Input: order = "cba", s = "abcd"
 * Output: "cbad"
 * Explanation: "c", "b", "a" appear in order, so they should be arranged as "c", "b", "a"
 *
 * LeetCode Link: https://leetcode.com/problems/custom-sort-string/
 */
public class CustomSortString {

    /**
     * Sorts string s based on custom order using character frequency counting.
     *
     * Algorithm:
     * 1. Count frequency of each character in string s
     * 2. Iterate through order string and append characters in that order
     * 3. Append remaining characters (not in order) at the end
     *
     * Time Complexity: O(n + m) where n = length of s, m = length of order
     * Space Complexity: O(1) - frequency array of fixed size 26 for lowercase letters
     */
    public String customSortString(String order, String s) {
        // Count frequency of each character in s
        int[] frequency = new int[26];
        for (char c : s.toCharArray()) {
            frequency[c - 'a']++;
        }

        StringBuilder result = new StringBuilder();

        // Add characters in the order specified by order string
        for (char c : order.toCharArray()) {
            int count = frequency[c - 'a'];
            for (int i = 0; i < count; i++) {
                result.append(c);
            }
            frequency[c - 'a'] = 0;
        }

        // Add remaining characters (not in order)
        for (int i = 0; i < 26; i++) {
            int count = frequency[i];
            char c = (char) ('a' + i);
            for (int j = 0; j < count; j++) {
                result.append(c);
            }
        }

        return result.toString();
    }
}
