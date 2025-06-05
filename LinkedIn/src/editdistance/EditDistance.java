package editdistance;

public class EditDistance {

    public int minDistanceRecursive(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        Integer[][] memo = new Integer[len1 + 1][len2 + 1];
        return compute(word1, word2, 0, 0, memo);
    }

    /**
     * Recursively computes minimum edit distance between substrings starting at i and j.
     */
    private int compute(String word1, String word2, int i, int j, Integer[][] memo) {
        // If one string is exhausted, insert/delete remaining characters from the other
      if (i == word1.length()) {
        return word2.length() - j;
      }
      if (j == word2.length()) {
        return word1.length() - i;
      }

        // Return already computed result
      if (memo[i][j] != null) {
        return memo[i][j];
      }

        if (word1.charAt(i) == word2.charAt(j)) {
            // Characters match, move ahead in both
            memo[i][j] = compute(word1, word2, i + 1, j + 1, memo);
        } else {
            // Try all three operations and take the minimum
            int insert = 1 + compute(word1, word2, i, j + 1, memo);     // Insert into word1
            int delete = 1 + compute(word1, word2, i + 1, j, memo);     // Delete from word1
            int replace = 1 + compute(word1, word2, i + 1, j + 1, memo);// Replace in word1

            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }

        return memo[i][j];
    }

  /**
   * Iteratively computes minimum edit distance between two words.
   * @param word1
   * @param word2
   * @return
   */
  public int minDistanceIterative(String word1, String word2) {
    int len1 = word1.length();
    int len2 = word2.length();

    // dp[i][j] = min operations to convert word1[0..i-1] to word2[0..j-1]
    int[][] dp = new int[len1 + 1][len2 + 1];

    // Base cases: converting to/from empty strings
    for (int i = 0; i <= len1; i++) dp[i][0] = i; // All deletes
    for (int j = 0; j <= len2; j++) dp[0][j] = j; // All inserts

    // Fill dp table
    for (int i = 1; i <= len1; i++) {
      char ch1 = word1.charAt(i - 1);
      for (int j = 1; j <= len2; j++) {
        char ch2 = word2.charAt(j - 1);
        if (ch1 == ch2) {
          dp[i][j] = dp[i - 1][j - 1]; // No operation needed
        } else {
          int insert = dp[i][j - 1] + 1;
          int delete = dp[i - 1][j] + 1;
          int replace = dp[i - 1][j - 1] + 1;
          dp[i][j] = Math.min(insert, Math.min(delete, replace));
        }
      }
    }

    return dp[len1][len2];
  }
}