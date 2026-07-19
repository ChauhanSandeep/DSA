package strings;

import java.util.*;

/**
 * Problem: Divide a String Into Groups of Size k
 *
 * Split a string into consecutive groups of exactly k characters. Pad only the
 * final group with the fill character when it is shorter than k.
 *
 * Leetcode: https://leetcode.com/problems/divide-a-string-into-groups-of-size-k/ (Easy)
 * Rating:   zerotrac 1273 (Q1, weekly-276)
 * Pattern:  String | Chunking | Padding
 *
 * Example:
 *   Input:  s = "abcdefgh", k = 3, fill = 'x'
 *   Output: ["abc", "def", "ghx"]
 *   Why:    the last group has two characters, so one fill character is appended.
 *
 * Follow-ups:
 *   1. Stream groups? Emit each completed group instead of storing all of them.
 *   2. Unicode code points? Iterate by code point instead of char index.
 *   3. Invalid k? Reject k <= 0 before computing group count.
 *
 * Related: String Compression (443).
 */
public class DivideAStringIntoGroupsOfSizeK {

    public static void main(String[] args) {
        DivideAStringIntoGroupsOfSizeK solver = new DivideAStringIntoGroupsOfSizeK();
        String[] inputs = {"abcdefghi", "abcdefgh", ""};
        int[] sizes = {3, 3, 3};
        char[] fills = {'x', 'x', 'x'};
        String[][] expected = {{"abc", "def", "ghi"}, {"abc", "def", "ghx"}, {}};
        for (int i = 0; i < inputs.length; i++) {
            String[] got = solver.divideString(inputs[i], sizes[i], fills[i]);
            System.out.printf("s=%s k=%d fill=%c -> %s  expected=%s%n", inputs[i], sizes[i], fills[i], Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }


        /**
     * Intuition: groups are fixed-width chunks. Ceiling division gives the number
     * of chunks, and only the last chunk can be short, so padding is needed only
     * after extracting that final substring.
     *
     * Algorithm:
     *   1. Return an empty array for null or empty input.
     *   2. Compute group count with ceiling division.
     *   3. Extract each substring window of up to k characters.
     *   4. Pad short groups with fill and store them.
     *
     * Time:  O(n) - every input character is copied once.
     * Space: O(n) - returned groups store the divided string.
     *
     * @param input string to divide
     * @param k group size
     * @param fill padding character for the final group
     * @return groups of exactly k characters
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
    /** Pads a string with fill until it reaches the target length. */
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
