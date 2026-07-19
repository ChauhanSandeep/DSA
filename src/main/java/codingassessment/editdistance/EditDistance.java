package codingassessment.editdistance;

import java.util.Arrays;

/**
 * Problem: Edit Distance
 *
 * Given two strings, return the fewest single-character edits needed to turn the
 * first string into the second. The allowed edits are insert, delete, and replace.
 * Empty strings are valid inputs, so some answers are just the other length.
 *
 * Leetcode: https://leetcode.com/problems/edit-distance/ (Medium)
 * Rating:   acceptance 60.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | String alignment | Levenshtein distance
 *
 * Example:
 *   Input:  word1 = "horse", word2 = "ros"
 *   Output: 3
 *   Why:    replace h with r, delete the extra r, then delete e; no two-edit path
 *           can fix both the order and the two extra characters.
 *
 * Follow-ups:
 *   1. Reduce memory from O(m*n) to O(min(m,n))?
 *      Keep only the previous and current DP rows, because each cell reads one row back.
 *   2. Return the actual edit script, not just the count?
 *      Store parent choices in the table and walk backward from dp[m][n].
 *   3. Give insert, delete, and replace different costs?
 *      Use the same recurrence, but add the configured cost for each candidate move.
 *   4. Support adjacent swaps as one edit?
 *      Extend the recurrence with a Damerau-Levenshtein transition when two chars cross.
 *
 * Related: Delete Operation for Two Strings (583), Minimum ASCII Delete Sum for Two Strings (712),
 * Longest Common Subsequence (1143).
 */
public class EditDistance {

    /**
     * Intuition: the brute-force tree tries every possible edit script, but the
     * same leftover suffixes show up again and again through different edit orders.
     * Name that repeated state: "what is the cheapest way to convert word1[i..]
     * into word2[j..]?" If the next characters match, both pointers move for free.
     * Otherwise the first edit must be insert, delete, or replace, and memoization
     * stores the best answer for each suffix pair.
     *
     * Algorithm:
     *   1. Allocate memo for every firstIndex and secondIndex suffix pair.
     *   2. Recursively solve from indexes 0 and 0.
     *   3. Return remaining length when either string is exhausted.
     *   4. Cache matching-character moves or the minimum of insert, delete, replace.
     *
     * Time:  O(m*n) - there are m*n index pairs, and each pair does constant work once.
     * Space: O(m*n) - memo stores every suffix pair plus O(m+n) recursion depth.
     *
     * @param word1 source string to convert
     * @param word2 target string to build
     * @return minimum number of edits needed to convert word1 into word2
     */
    public int minDistanceRecursive(String word1, String word2) {
        int firstWordLen = word1.length();
        int secondWordLen = word2.length();
        Integer[][] memo = new Integer[firstWordLen + 1][secondWordLen + 1];
        return computeMinDistance(word1, word2, 0, 0, memo);
    }

    /** Computes the best edit count for the two suffixes beginning at the given indexes. */
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
     * Intuition: the same suffix idea can be flipped into a prefix table that is
     * easier to trace in an interview. dp[i][j] represents the cheapest conversion
     * from the first i chars of word1 to the first j chars of word2. Empty prefixes
     * are just "delete everything" or "insert everything." For a non-empty pair,
     * the final move must come from one of three already-smaller prefixes: left
     * means insert, top means delete, and diagonal means match or replace.
     *
     * Algorithm:
     *   1. Create dp where dp[i][j] is the cost for word1[0..i-1] to word2[0..j-1].
     *   2. Initialize empty-prefix rows and columns with insert/delete counts.
     *   3. Fill each cell from match, insert, delete, and replace transitions.
     *   4. Return dp[firstWordLen][secondWordLen].
     *
     * Time:  O(m*n) - the table has one cell per pair of prefix lengths, and each is filled once.
     * Space: O(m*n) - the DP table stores all prefix-pair answers.
     *
     * @param word1 source string to convert
     * @param word2 target string to build
     * @return minimum number of edits needed to convert word1 into word2
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

    public static void main(String[] args) {
        EditDistance solver = new EditDistance();

        String[][] inputs = {
            {"horse", "ros"},
            {"intention", "execution"},
            {"", "abc"}
        };
        int[] expected = {3, 5, 3};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.minDistanceIterative(inputs[i][0], inputs[i][1]);
            System.out.printf("words=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }
}