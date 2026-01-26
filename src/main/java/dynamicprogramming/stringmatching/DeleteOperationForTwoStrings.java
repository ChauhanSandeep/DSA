package dynamicprogramming.stringmatching;

/**
 * Problem: Delete Operation for Two Strings (LeetCode #583)
 *
 * Problem Statement:
 * Given two strings word1 and word2, return the minimum number of steps required to make word1 and word2 the same.
 * In one step, you can delete exactly one character in either string.
 *
 * Example 1:
 * Input: word1 = "sea", word2 = "eat"
 * Output: 2
 * Explanation: You need one step to make "sea" to "ea" and another step to make "eat" to "ea".
 *
 * Example 2:
 * Input: word1 = "leetcode", word2 = "etco"
 * Output: 4
 * Explanation: You need four steps to make leetcode to etco. (delete 'l', 'e', 'd', 'e')
 *
 * Approach:
 * This problem can be reduced to finding the length of the longest common subsequence (LCS) between the two strings.
 * The minimum number of deletions required is equal to:
 * (length of word1 - LCS) + (length of word2 - LCS)
 *
 * We can solve this using dynamic programming where dp[i][j] represents the length of the LCS of
 * word1[0..i-1] and word2[0..j-1].
 *
 * Follow-up Questions:
 * 1. What if we can perform insertions and deletions?
 *    Answer: The problem would become the edit distance problem, where we can perform insert, delete, or replace operations.
 *
 * 2. What if each operation has a different cost?
 *    Answer: We would need to modify the DP approach to account for different costs for insertions and deletions.
 *
 * 3. Can you solve it in O(n) space?
 *    Answer: Yes, we can optimize the space complexity by using two 1D arrays instead of a 2D array.
 *
 * LeetCode: https://leetcode.com/problems/delete-operation-for-two-strings/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class DeleteOperationForTwoStrings {

    /**
     * Calculates the minimum number of steps to make two strings equal by deleting characters.
     *
     * Steps to solve:
     * 1. Find the length of the longest common subsequence (LCS) between the two strings.
     * 2. The minimum number of deletions required is:
     *    (length of word1 - LCS) + (length of word2 - LCS)
     * 3. To find the LCS, we use dynamic programming:
     *    a. Create a 2D DP array where dp[i][j] represents the LCS of word1[0..i-1] and word2[0..j-1].
     *    b. If the current characters match, dp[i][j] = dp[i-1][j-1] + 1.
     *    c. If they don't match, dp[i][j] = max(dp[i-1][j], dp[i][j-1]).
     *
     * Time Complexity: O(m*n) where m and n are the lengths of word1 and word2
     * Space Complexity: O(m*n) for the DP table, which can be optimized to O(min(m,n))
     *
     * @param word1 The first input string
     * @param word2 The second input string
     * @return The minimum number of deletions required
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
