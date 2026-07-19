package dailybytes.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Problem: Longest Common Prefix
 *
 * Given a list of strings, return the longest prefix shared by every string.
 * Sorting places the two most different boundary strings at the ends, so their
 * common prefix is the common prefix of the entire list.
 *
 * Leetcode: https://leetcode.com/problems/longest-common-prefix/ (Easy)
 * Rating:   acceptance 48.0% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Sorting | Boundary comparison
 *
 * Example:
 *   Input:  strList = [colorado, color, cold]
 *   Output: "col"
 *   Why:    after sorting, the first and last strings still share only "col",
 *           so no string in between can force a shorter prefix.
 *
 * Follow-ups:
 *   1. Avoid mutating the input list?
 *      Copy the list before sorting or use vertical scanning instead.
 *   2. Support repeated prefix queries over a dictionary?
 *      Build a trie and track the first branching point.
 *   3. Find the longest common suffix?
 *      Compare characters from the end or reverse the strings first.
 *   4. Handle very long strings with early termination?
 *      Scan column by column and stop at the first mismatch.
 *
 * Related: Implement Trie (208), Search Suggestions System (1268).
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        List<List<String>> inputs = Arrays.asList(
            Arrays.asList("colorado", "color", "cold"),
            Arrays.asList("a", "b", "c"),
            Arrays.asList("spot", "spotty", "spotted")
        );
        String[] expected = { "col", "", "spot" };

        for (int i = 0; i < inputs.size(); i++) {
            List<String> input = inputs.get(i);
            String shown = input.toString();
            String output = longestCommonPrefix(input);
            System.out.printf("strings=%s -> %s  expected=%s%n", shown, output, expected[i]);
        }
    }

    /**
     * Intuition: in sorted order, strings with the smallest and largest lexical
     * values form the widest spread. Any prefix common to those two boundary
     * strings must also be common to every string between them, so the problem
     * reduces to comparing only the first and last sorted strings.
     *
     * Algorithm:
     *   1. Return an empty string for a null or empty list.
     *   2. Return the sole string when the list has one element.
     *   3. Sort strList in place.
     *   4. Return the common prefix of the first and last sorted strings.
     *
     * Time:  O(n log n + m) - sorting dominates, then up to m prefix characters are compared.
     * Space: O(1) - aside from the sort's internal work, no extra data structure is used.
     *
     * @param strList list of strings to inspect
     * @return longest prefix shared by every string
     */
    public static String longestCommonPrefix(List<String> strList) {
        if (strList == null || strList.isEmpty()) {
            return "";
        }
        if (strList.size() == 1) {
            return strList.get(0);
        }

        Collections.sort(strList);
        return longestPrefix(strList.get(0), strList.get(strList.size() - 1));
    }

    /**
     * Finds the common prefix between two strings.
     * @param str1 The first string.
     * @param str2 The second string.
     * @return The common prefix.
     */
    public static String longestPrefix(String str1, String str2) {
        StringBuilder prefixBuilder = new StringBuilder();
        for (int i = 0; i < str1.length() && i < str2.length(); i++) {
            if (str1.charAt(i) == str2.charAt(i)) {
                prefixBuilder.append(str1.charAt(i));
            } else {
                break;
            }
        }
        return prefixBuilder.toString();
    }
}
