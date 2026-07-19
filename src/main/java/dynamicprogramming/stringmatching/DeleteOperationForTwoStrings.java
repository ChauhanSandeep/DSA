package dynamicprogramming.stringmatching;

/**
 * Problem: Delete Operation for Two Strings
 *
 * Return the minimum deletions needed to make two strings equal. Characters may be deleted from either string.
 *
 * Leetcode: https://leetcode.com/problems/delete-operation-for-two-strings/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Longest common subsequence | String matching
 *
 * Example:
 *   Input:  word1 = "sea", word2 = "eat"
 *   Output: 2
 *   Why:    deleting s and t leaves the common string ea.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Edit Distance (72), Longest Common Subsequence (1143).
 */
public class DeleteOperationForTwoStrings {

    public static void main(String[] args) {
        DeleteOperationForTwoStrings solution = new DeleteOperationForTwoStrings();
        String[][] inputs = { {"sea", "eat"}, {"leetcode", "etco"}, {"", "abc"} };
        int[] expected = {2, 4, 3};
        for (int i = 0; i < inputs.length; i++) {
            int got = solution.minDistance(inputs[i][0], inputs[i][1]);
            System.out.printf("word1=%s word2=%s -> %d  expected=%d%n", inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }


        /**
     * Intuition: dp[index1][index2] is the LCS length for the two prefixes. Matching characters extend the diagonal LCS; otherwise the best LCS drops one last character from either prefix. Everything outside the LCS must be deleted.
     *
     * Algorithm:
     *   1. Create dp[length1 + 1][length2 + 1].
     *   2. Iterate both prefix lengths.
     *   3. Extend diagonally on matching characters.
     *   4. Otherwise take the best of top and left.
     *   5. Convert LCS length to deletions from both strings.
     *
     * Time:  O(length1 * length2) - every prefix pair is processed.
     * Space: O(length1 * length2) - stores the table.
     *
     * @param word1 first string
     * @param word2 second string
     * @return minimum deletions
     */
public int minDistance(String word1, String word2) {
        int length1 = word1.length();
        int length2 = word2.length();

        // dp[i][j] represents the LCS of word1[0..i-1] and word2[0..j-1]
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Fill the DP table
        for (int index1 = 1; index1 <= length1; index1++) {
            for (int index2 = 1; index2 <= length2; index2++) {
                if (word1.charAt(index1 - 1) == word2.charAt(index2 - 1)) {
                    dp[index1][index2] = dp[index1 - 1][index2 - 1] + 1;
                } else {
                    dp[index1][index2] = Math.max(dp[index1 - 1][index2], dp[index1][index2 - 1]);
                }
            }
        }

        int lcsLength = dp[length1][length2];
        return (length1 - lcsLength) + (length2 - lcsLength);
    }

    /**
     * Space-optimized solution using 1D DP array
     *
     * Steps to solve:
     * 1. Instead of using a 2D array, we use two 1D arrays: current and previous.
     * 2. We only need to keep track of the previous row to compute the current row.
     * 3. This reduces the space complexity from O(m*n) to O(min(m,n)).
     */
    public int minDistanceOptimized(String word1, String word2) {
        // Ensure word1 is the shorter string to minimize space usage
        if (word1.length() > word2.length()) {
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }

        int length1 = word1.length();
        int length2 = word2.length();

        // Use two 1D arrays instead of a 2D array
        int[] prev = new int[length2 + 1]; // stores the previous row of the DP table
        int[] curr = new int[length2 + 1]; // stores the current row of the DP table

        for (int index1 = 1; index1 <= length1; index1++) {
            for (int index2 = 1; index2 <= length2; index2++) {
                if (word1.charAt(index1 - 1) == word2.charAt(index2 - 1)) { // Characters match
                    curr[index2] = prev[index2 - 1] + 1;
                } else { // Characters don't match
                    // Take the maximum of the two possible options.
                    // prev[index2] contains dp[i-1][j] and curr[index2 - 1] contains dp[i][j-1]
                    curr[index2] = Math.max(prev[index2], curr[index2 - 1]);
                }
            }

            // Swap the arrays for the next iteration
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        int lcsLength = prev[length2];
        return (length1 - lcsLength) + (length2 - lcsLength);
    }

    /**
     * Alternative approach without using LCS
     *
     * Steps to solve:
     * 1. Create a 2D DP array where dp[i][j] represents the minimum number of deletions
     *    required to make word1[0..i-1] and word2[0..j-1] equal.
     * 2. If the current characters match, dp[i][j] = dp[i-1][j-1].
     * 3. If they don't match, dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1]).
     * 4. The result is stored in dp[m][n].
     */
    public int minDistanceDirect(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        // dp[i][j] represents the minimum number of deletions to make word1[0..i-1] and word2[0..j-1] equal
        int[][] dp = new int[m + 1][n + 1];

        // Initialize the first row and column
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Need to delete all characters in word1[0..i-1]
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Need to delete all characters in word2[0..j-1]
        }

        // Fill the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // Characters match, no deletion needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Delete either from word1 or word2, take the minimum
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    /**
     * Space-optimized version of the direct approach using 1D array
     */
    public int minDistanceDirectOptimized(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        // Use a single 1D array for DP
        int[] dp = new int[n + 1];

        // Initialize the first row (when word1 is empty)
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }

        // Fill the DP array
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // Store the value of dp[i-1][j-1]
            dp[0] = i; // Update the first column (when word2 is empty)

            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // Store the current dp[i-1][j] before updating

                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = 1 + Math.min(dp[j], dp[j - 1]);
                }

                prev = temp; // Update prev for the next iteration
            }
        }

        return dp[n];
    }
}
