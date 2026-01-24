package strings;

import java.util.*;

/**
 * Problem: Divide a String Into Groups of Size k
 *
 * Given a string s, an integer k, and a character fill, divide the string s into groups of size k
 * using the following procedure:
 * - The first group consists of the first k characters of the string
 * - The second group consists of the next k characters of the string, and so on
 * - Each character can be part of exactly one group
 * - For the last group, if the string does not have k characters remaining,
 *   the character fill is used to complete the group
 *
 * Example:
 * Input: s = "abcdefghi", k = 3, fill = "x"
 * Output: ["abc","def","ghi"]
 * Explanation: The first 3 groups contain 3 characters each. No padding needed.
 *
 * Input: s = "abcdefgh", k = 3, fill = "x"
 * Output: ["abc","def","ghx"]
 * Explanation: The last group has only 2 characters, so we pad with 1 'x'.
 *
 * LeetCode: https://leetcode.com/problems/divide-a-string-into-groups-of-size-k/
 *
 * Follow-up Questions:
 * 1. Q: What if k is larger than the string length?
 *    A: The entire string becomes one group, padded with fill characters to reach size k.
 *
 * 2. Q: What if k is 1?
 *    A: Each character becomes its own group, no padding needed unless empty string.
 *
 * 3. Q: How would you handle Unicode characters or multi-byte strings?
 *    A: Current solution works with Unicode as Java strings handle it properly.
 *
 * 4. Q: What about memory optimization for very large strings?
 *    A: Could use streaming approach to process groups one at a time instead of storing all.
 *
 * Related Problems:
 * - String Compression: https://leetcode.com/problems/string-compression/
 * - Split Array into Consecutive Subsequences: https://leetcode.com/problems/split-array-into-consecutive-subsequences/
 * LeetCode Contest Rating: 1273
 **/
public class DivideAStringIntoGroupsOfSizeK {

    /**
     * Divides string into groups of size k using substring extraction and padding.
     *
     * Algorithm:
     * 1. Calculate total number of groups needed using ceiling division
     * 2. Iterate through string in steps of k characters
     * 3. Extract substring of size k (or remaining characters)
     * 4. Pad the last group with fill character if necessary
     * 5. Add each group to result array
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(n) for storing the result groups
     *
     * @param input the input string to divide
     * @param k the size of each group
     * @param fill the character used to pad the last group if needed
     * @return array of strings where each string has exactly k characters
     */
    public String[] divideString(String input, int k, char fill) {
        if (input == null || input.isEmpty()) {
            return new String[0];
        }

        int stringLength = input.length();

        // Calculate number of groups using ceiling division: (n + k - 1) / k
        int numberOfGroups = (stringLength + k - 1) / k;
        String[] result = new String[numberOfGroups];

        // Process each group
        for (int groupIndex = 0; groupIndex < numberOfGroups; groupIndex++) {
            int startIndex = groupIndex * k;
            int endIndex = Math.min(startIndex + k, stringLength);

            // Extract substring for current group
            String currentGroup = input.substring(startIndex, endIndex);

            // Pad with fill character if group is smaller than k
            if (currentGroup.length() < k) {
                currentGroup = padWithFillCharacter(currentGroup, k, fill);
            }

            result[groupIndex] = currentGroup;
        }

        return result;
    }

    // Helper method to pad string with fill character to reach target length
    private String padWithFillCharacter(String str, int targetLength, char fill) {
        StringBuilder padded = new StringBuilder(str);
        while (padded.length() < targetLength) {
            padded.append(fill);
        }
        return padded.toString();
    }

    /**
     * Alternative approach: Pre-pad the string then extract groups.
     * More memory efficient for large strings as it modifies once.
     *
     * Algorithm:
     * 1. Calculate remainder when string length is divided by k
     * 2. If remainder exists, append fill characters to make length divisible by k
     * 3. Extract consecutive substrings of length k
     * 4. Return array of groups
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(n) for the padded string and result
     *
     * @param s the input string to divide
     * @param k the size of each group
     * @param fill the character used to pad the string if needed
     * @return array of strings where each string has exactly k characters
     */
    public String[] divideStringOptimized(String s, int k, char fill) {
        if (s == null || s.isEmpty()) {
            return new String[0];
        }

        int stringLength = s.length();

        // Pre-pad the string if necessary
        int remainder = stringLength % k;
        if (remainder != 0) {
            int paddingNeeded = k - remainder;
            char[] fillArray = new char[paddingNeeded];
            Arrays.fill(fillArray, fill);
            s = s + new String(fillArray);
        }

        // Calculate number of groups after padding
        int numberOfGroups = s.length() / k;
        String[] result = new String[numberOfGroups];

        // Extract groups of exactly k characters each
        for (int i = 0; i < numberOfGroups; i++) {
            int startIndex = i * k;
            int endIndex = startIndex + k;
            result[i] = s.substring(startIndex, endIndex);
        }

        return result;
    }
}
