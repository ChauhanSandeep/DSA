package codingassessment.editdistance;

/**
 * Problem: Edit Distance (Levenshtein Distance)
 *
 * Given two strings word1 and word2, return the minimum number of operations required
 * to convert word1 to word2. You can perform three operations:
 * - Insert a character
 * - Delete a character
 * - Replace a character
 *
 * 🔗 LeetCode: https://leetcode.com/problems/edit-distance/
 *
 * 📝 Example:
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 *   horse -> rorse (replace 'h' with 'r')
 *   rorse -> rose (remove 'r')
 *   rose -> ros (remove 'e')
 *
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 *
 * 🎯 Constraints:
 * - 0 <= word1.length, word2.length <= 500
 * - word1 and word2 consist of lowercase English letters
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: How would you optimize space complexity?
 *    A: Use 1D DP array instead of 2D. Since we only need previous row to compute current row,
 *       maintain two arrays (current and previous) or single array with careful updating.
 *       Space reduces from O(m*n) to O(min(m,n)).
 *
 * 2. Q: How would you reconstruct the actual sequence of operations?
 *    A: Backtrack through DP table from dp[m][n] to dp[0][0]. At each cell, determine which
 *       operation was used (insert/delete/replace) based on which neighbor gave minimum value.
 *       Store operations in list while backtracking.
 *
 * 3. Q: What if operations have different costs (e.g., insert=1, delete=2, replace=3)?
 *    A: Modify recurrence relation to use weighted costs: min(insert*cost1, delete*cost2, replace*cost3).
 *       Same algorithm structure, just different operation costs.
 *
 * 4. Q: How would you handle case-insensitive comparison?
 *    A: Convert both strings to lowercase before processing. Or modify character comparison
 *       to use equalsIgnoreCase() or toLowerCase() comparison.
 *
 * 5. Q: What if we want to find longest common subsequence (LCS) instead?
 *    A: Similar DP approach: dp[i][j] = dp[i-1][j-1]+1 if chars match, else max(dp[i-1][j], dp[i][j-1]).
 *       Edit distance and LCS are related: edit_distance = m + n - 2*LCS (when only insert/delete allowed).
 *
 * Related Problems:
 * - Longest Common Subsequence (LeetCode #1143)
 * - Delete Operation for Two Strings (LeetCode #583)
 * - Minimum ASCII Delete Sum for Two Strings (LeetCode #712)
 */
public class EditDistance {

    /**
     * Computes minimum edit distance using top-down DP with memoization.
     *
     * Algorithm:
     * 1. Start from beginning of both strings
     * 2. If characters match, move to next in both strings (no operation needed)
     * 3. If characters differ, try all three operations and take minimum
     * 4. Use memoization to cache results for subproblems
     *
     * Time Complexity: O(m * n) where m, n are lengths of word1 and word2
     * Space Complexity: O(m * n) for memoization table + O(m + n) for recursion stack
     *
     * @param word1 First string
     * @param word2 Second string
     * @return Minimum number of operations to convert word1 to word2
     */
    public int minDistanceRecursive(String word1, String word2) {
        int firstWordLen = word1.length();
        int secondWordLen = word2.length();
        Integer[][] memo = new Integer[firstWordLen + 1][secondWordLen + 1];
        return computeMinDistance(word1, word2, 0, 0, memo);
    }

    /**
     * Recursive helper that computes minimum edit distance from positions i and j.
     * Uses memoization to avoid recomputing overlapping subproblems.
     *
     * @param word1 First string
     * @param word2 Second string
     * @param firstIndex Current position in word1
     * @param secondIndex Current position in word2
     * @param memo Memoization table to cache computed results
     * @return Minimum operations to convert word1[firstIndex..] to word2[secondIndex..]
     */
    private int computeMinDistance(String word1, String word2, int firstIndex, int secondIndex, Integer[][] memo) {
        // Base case: if one string exhausted, need to insert/delete remaining characters
        if (firstIndex == word1.length()) {
            return word2.length() - secondIndex;
        }
        if (secondIndex == word2.length()) {
            return word1.length() - firstIndex;
        }

        // Return cached result if available
        if (memo[firstIndex][secondIndex] != null) {
            return memo[firstIndex][secondIndex];
        }

        if (word1.charAt(firstIndex) == word2.charAt(secondIndex)) {
            // Characters match - no operation needed, move to next in both
            memo[firstIndex][secondIndex] = computeMinDistance(word1, word2, firstIndex + 1, secondIndex + 1, memo);
        } else {
            // Characters differ - try all three operations
            int insertOp = 1 + computeMinDistance(word1, word2, firstIndex, secondIndex + 1, memo);
            int deleteOp = 1 + computeMinDistance(word1, word2, firstIndex + 1, secondIndex, memo);
            int replaceOp = 1 + computeMinDistance(word1, word2, firstIndex + 1, secondIndex + 1, memo);

            memo[firstIndex][secondIndex] = Math.min(insertOp, Math.min(deleteOp, replaceOp));
        }

        return memo[firstIndex][secondIndex];
    }

    /**
     * Computes minimum edit distance using bottom-up dynamic programming.
     *
     * Algorithm:
     * 1. Create DP table where dp[i][j] = min operations to convert word1[0..i-1] to word2[0..j-1]
     * 2. Initialize base cases: converting to/from empty string
     * 3. Fill table: if chars match, take diagonal; else take min of three operations
     * 4. Return dp[m][n] as final answer
     *
     * Key insight: Build solution from smaller subproblems. Each cell depends only on
     * three neighbors (left, top, diagonal), enabling efficient bottom-up computation.
     *
     * Time Complexity: O(m * n) where m, n are lengths of word1 and word2
     * Space Complexity: O(m * n) for DP table
     *
     * @param word1 First string
     * @param word2 Second string
     * @return Minimum number of operations to convert word1 to word2
     */
    public int minDistanceIterative(String word1, String word2) {
        int firstWordLen = word1.length();
        int secondWordLen = word2.length();

        // DP table: dp[i][j] = min operations to convert word1[0..i-1] to word2[0..j-1]
        int[][] dp = new int[firstWordLen + 1][secondWordLen + 1];

        // Base cases: converting to/from empty string requires inserting/deleting all characters
        for (int i = 0; i <= firstWordLen; i++) {
            dp[i][0] = i;  // Delete all characters from word1
        }
        for (int j = 0; j <= secondWordLen; j++) {
            dp[0][j] = j;  // Insert all characters into word1
        }

        // Fill DP table bottom-up
        for (int i = 1; i <= firstWordLen; i++) {
            char firstChar = word1.charAt(i - 1);
            for (int j = 1; j <= secondWordLen; j++) {
                char secondChar = word2.charAt(j - 1);
                
                if (firstChar == secondChar) {
                    // Characters match - no operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Try all three operations and take minimum
                    int insertOp = dp[i][j - 1] + 1;      // Insert character
                    int deleteOp = dp[i - 1][j] + 1;      // Delete character
                    int replaceOp = dp[i - 1][j - 1] + 1; // Replace character
                    dp[i][j] = Math.min(insertOp, Math.min(deleteOp, replaceOp));
                }
            }
        }

        return dp[firstWordLen][secondWordLen];
    }
}