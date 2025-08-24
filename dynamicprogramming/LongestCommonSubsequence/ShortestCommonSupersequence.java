package dynamicprogramming.LongestCommonSubsequence;

/**
 * Shortest Common Supersequence (SCS)
 *
 * Problem Statement:
 * Given two strings, find the length of their shortest common supersequence.
 * A supersequence of two strings is the shortest string that contains both as subsequences.
 *
 * Example:
 * Input: str1 = "abac", str2 = "cab"
 * Output: 5
 * Explanation: One possible result is "cabac".
 *
 * LeetCode link: https://leetcode.com/problems/shortest-common-supersequence/
 */
public class ShortestCommonSupersequence {

    /**
     * Recursive approach with memoization (Top-down DP).
     *
     * Intuition:
     * - If the characters match, add 1 and move both indices forward.
     * - If characters don't match, we have two choices:
     *    1. Include str1[i] and move i forward.
     *    2. Include str2[j] and move j forward.
     * - Take the minimum of the two choices +1.
     *
     * Steps:
     * 1. Start from (i = 0, j = 0).
     * 2. If i == str1.length, we need to add all remaining characters of str2.
     * 3. If j == str2.length, we need to add all remaining characters of str1.
     * 4. Otherwise, follow the intuition above.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n) for dp array
     */
    public static int shortestCommonSupersequenceRecursive(String str1, String str2) {
        int length1 = str1.length();
        int length2 = str2.length();
        int[][] dp = new int[length1][length2];

        // Initialize with -1 for memoization
        for (int i = 0; i < length1; i++) {
            for (int j = 0; j < length2; j++) {
                dp[i][j] = -1;
            }
        }

        return findSCSLength(str1, str2, 0, 0, dp);
    }

    private static int findSCSLength(String str1, String str2, int i, int j, int[][] dp) {
        if (i == str1.length()) {
            return str2.length() - j; // Add remaining characters from str2
        }
        if (j == str2.length()) {
            return str1.length() - i; // Add remaining characters from str1
        }

        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        if (str1.charAt(i) == str2.charAt(j)) {
            // If characters match, move both
            dp[i][j] = 1 + findSCSLength(str1, str2, i + 1, j + 1, dp);
        } else {
            // Otherwise, take minimum by adding one character
            int skipI = 1 + findSCSLength(str1, str2, i + 1, j, dp); // add char for ith character and calculate for rest of the string
            int skipJ = 1 + findSCSLength(str1, str2, i, j + 1, dp); // add char for jth character and calculate for rest of the string
            dp[i][j] = Math.min(skipI, skipJ);
        }
        return dp[i][j];
    }

    /**
     * Iterative Bottom-up Dynamic Programming approach.
     *
     * Intuition:
     * - Similar to LCS (Longest Common Subsequence) table filling.
     * - If characters match, move diagonally and add 1.
     * - Otherwise, minimum of (left, top) + 1.
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n)
     */
    public static int shortestCommonSuperSequenceIterative(String str1, String str2) {
        int length1 = str1.length();
        int length2 = str2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Fill base cases
        for (int i = 0; i <= length1; i++) {
            dp[i][0] = i; // If str2 is empty then we need to add all characters of str1
        }
        for (int j = 0; j <= length2; j++) {
            dp[0][j] = j; // If str1 is empty then we need to add all character of str2
        }

        // Fill the dp table
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    // add single character in supersequence and move the index in both the strings
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    // add single character from one of the string and take minimum of the previous indices in both string
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        printSuperSequence(dp, str1, str2);

        return dp[length1][length2];
    }

    private static void printSuperSequence(int[][] dp, String str1, String str2) {
        // Step 2: Backtrack to build the actual SCS string
        int i = str1.length(), j = str2.length();
        StringBuilder builder = new StringBuilder();

        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                // If characters match, include once and move diagonally
                builder.append(str1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] < dp[i][j - 1]) {
                // Move where cost is lesser (up)
                builder.append(str1.charAt(i - 1));
                i--;
            } else {
                // Move left
                builder.append(str2.charAt(j - 1));
                j--;
            }
        }

        // If any characters are left in str1 or str2
        while (i > 0) {
            builder.append(str1.charAt(i - 1));
            i--;
        }
        while (j > 0) {
            builder.append(str2.charAt(j - 1));
            j--;
        }

        // Since we built the string backwards, reverse it
        System.out.println(builder.reverse().toString());
    }

    public static void main(String[] args) {
        String str1 = "abac";
        String str2 = "cab";

        System.out.println("Recursive Memoized Approach: SCS length = " + shortestCommonSupersequenceRecursive(str1, str2));
        System.out.println("Iterative DP Approach: SCS length = " + shortestCommonSuperSequenceIterative(str1, str2));
    }
}