package strings.patternmatching;

/**
 * Problem Statement:
 * Given two strings `s` and `t`, return the minimum number of characters that need to be appended to the end of `s`
 * so that `t` becomes a subsequence of `s`.
 * A subsequence is a sequence that can be derived from another string
 * by deleting some or no characters without changing the order of the remaining characters.
 *
 * Example:
 * Input: s = "coaching", t = "coding"
 * Output: 4
 * Explanation: We can append "ding" to the end of "coaching" to form "coachingding", where "coding" is a subsequence.
 *
 * Leetcode URL:
 * https://leetcode.com/problems/append-characters-to-string-to-make-subsequence
 *
 * Follow-up Questions:
 * 1. How would your solution change if you were allowed to insert characters anywhere, not just at the end?
 *    → You’d likely need a dynamic programming solution based on Longest Common Subsequence (LCS).
 *    → Ref: https://leetcode.com/problems/edit-distance/
 *
 * 2. Can this problem be extended to handle wildcard characters in `t` (e.g., '?', '*')?
 *    → It becomes similar to pattern matching or wildcard matching.
 *    → Ref: https://leetcode.com/problems/wildcard-matching/
 * LeetCode Contest Rating: 1363
 **/

public class AppendCharactersToMakeSubsequence {

    /**
     * Finds the minimum number of characters that need to be appended to source
     * so that target becomes a subsequence of source.
     *
     * Algorithm:
     * - Use two pointers: i for source, j for target.
     * - Traverse through both strings.
     * - If characters match, move both pointers forward.
     * - If not, move pointer i only.
     * - At the end, j represents the number of characters from target that matched in order.
     * - The remaining characters (target.length() - j) need to be appended to source.
     *
     * Time Complexity: O(n), where n = length of source
     * Space Complexity: O(1), constant space
     *
     * @param source Source string
     * @param target Target string (to become a subsequence)
     * @return Minimum number of characters to append to make target a subsequence of source
     */
    public int appendCharacters(String source, String target) {
        // Handle null inputs explicitly
      if (source == null || target == null) {
        return 0;
      }

        int sourceIndex = 0; // Pointer for source
        int targetIndex = 0; // Pointer for target

        // Traverse both strings to find matching subsequence
        while (sourceIndex < source.length() && targetIndex < target.length()) {
            if (source.charAt(sourceIndex) == target.charAt(targetIndex)) {
                targetIndex++; // move both if characters match
            }
            sourceIndex++; // always move pointer sourceIndex
        }

        // targetIndex is the number of matched characters
        // so (target.length() - targetIndex) characters need to be appended
        return target.length() - targetIndex;
    }
}