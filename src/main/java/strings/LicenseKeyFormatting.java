package strings;

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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class LicenseKeyFormatting {

    /**
     * Formats license key by grouping characters and converting to uppercase.
     *
     * Algorithm:
     * 1. Initialize a StringBuilder for the result and a counter for characters in the current group.
     * 2. Traverse the input string from end to start:
     *   - If the character is not a dash, append its uppercase version to the result
     *   and increment the counter.
     *   - If the counter reaches groupSize, append a dash to the result and reset the counter.
     * 3. After processing all characters, reverse the result string to get the correct order
     *  and return it.
     *
     * Time Complexity: O(n) where n is length of input string
     * Space Complexity: O(n) for the result string
     */
    public String licenseKeyFormatting(String key, int groupSize) {
        StringBuilder result = new StringBuilder();
        int count = 0;

        // Process from right to left
        for (int i = key.length() - 1; i >= 0; i--) {
            char c = key.charAt(i);

            if (c != '-') {
                if (count == groupSize) {
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
