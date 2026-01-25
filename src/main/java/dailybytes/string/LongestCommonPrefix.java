package dailybytes.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class contains a method to find the longest common prefix among a list of strings.
 *
 * Algorithm:
 * - Sort the list of strings.
 * - Compare the first and last strings in the sorted list to find the common prefix.
 * - Time Complexity: O(n * log n + m) where n is the number of strings and m is the length of the longest common prefix.
 * - Space Complexity: O(1)
 *
 * LeetCode Problem Link: https://leetcode.com/problems/longest-common-prefix/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        assert longestCommonPrefix(Arrays.asList("colorado", "color", "cold")).equals("col") : "Incorrect";
        assert longestCommonPrefix(Arrays.asList("a", "b", "c")).equals("") : "Incorrect";
        assert longestCommonPrefix(Arrays.asList("spot", "spotty", "spotted")).equals("spot") : "Incorrect";
    }

    /**
     * Finds the longest common prefix among a list of strings.
     * @param strList The list of strings.
     * @return The longest common prefix.
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
