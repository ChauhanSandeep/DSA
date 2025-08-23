package String;

/**
 * LeetCode 482. License Key Formatting
 *
 * You are given a license key represented as a string s that consists of only alphanumeric
 * characters and dashes. We want to reformat the string s such that each group contains
 * exactly k characters, except for the first group, which could be shorter than k.
 *
 * Example 1:
 * Input: s = "5F3Z-2e-9-w", k = 4
 * Output: "5F3Z-2E9W"
 *
 * LeetCode Link: https://leetcode.com/problems/license-key-formatting/
 */
public class LicenseKeyFormatting {

    /**
     * Formats license key by grouping characters and converting to uppercase.
     *
     * Algorithm:
     * 1. Remove all dashes and convert to uppercase
     * 2. Calculate first group size (remainder when dividing by k)
     * 3. Build result by taking appropriate number of characters for each group
     * 4. Add dashes between groups
     *
     * Time Complexity: O(n) where n is length of input string
     * Space Complexity: O(n) for the result string
     */
    public String licenseKeyFormatting(String s, int k) {
        StringBuilder result = new StringBuilder();
        int count = 0;

        // Process from right to left
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);

            if (c != '-') {
                if (count == k) {
                    result.append('-');
                    count = 0;
                }
                result.append(Character.toUpperCase(c));
                count++;
            }
        }

        return result.reverse().toString();
    }
}
