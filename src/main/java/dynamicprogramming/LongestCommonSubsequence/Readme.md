## Parent Problem: Longest Common Subsequence (LCS)

**Problem Statement:** Given two sequences (strings or arrays), find the length of the longest subsequence that is common to both sequences. A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.
* Intuition :
    1.  If the last characters of both sequences match, then the length of the LCS is 1 plus the length of the LCS of the remaining sequences.
    2.  If the last characters do not match, then the length of the LCS is the maximum of the lengths of the LCS obtained by either excluding the last character of one sequence or excluding the last character of the other sequence. 

### **Memoized Recursive Code (Java):**

```java
public class LCSRelatedProblems {

    public static int longestCommonSubsequence(String text1, String text2) {
        int length1 = text1.length();
        int length2 = text2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Initialize the dp array with -1 to indicate uncomputed states
        for (int i = 0; i <= length1; i++) {
            for (int j = 0; j <= length2; j++) {
                dp[i][j] = -1;
            }
        }

        return longestCommonSubsequenceMemoized(text1, text2, 0, 0, dp);
    }

    public static int longestCommonSubsequenceMemoized(String text1, String text2, int index1, int index2, int[][] dp) {
        // Base cases:
        if (index1 == text1.length() || index2 == text2.length()) {
            return 0;
        }

        // Check if the result is already memoized
        if (dp[index1][index2] != -1) {
            return dp[index1][index2];
        }

        // If the characters at the current indices match:
        if (text1.charAt(index1 - 1) == text2.charAt(index2 - 1)) {
            // Increment the LCS length and move to the previous characters
            dp[index1][index2] = 1 + longestCommonSubsequenceMemoized(text1, text2, index1 + 1, index2 + 1, dp);
        } else {
            // If the characters don't match, choose the maximum LCS length by either:
            // 1. Excluding the character from text1 and finding the LCS of the remaining text1 and text2
            // 2. Excluding the character from text2 and finding the LCS of text1 and the remaining text2
            dp[index1][index2] = Math.max(
                longestCommonSubsequenceMemoized(text1, text2, index1 + 1, index2, dp),
                longestCommonSubsequenceMemoized(text1, text2, index1, index2 + 1, dp)
            );
        }

        return dp[index1][index2];
    }
```
### **Tabulation Code (Java):**

```java
    public static int longestCommonSubsequenceTabulation(String text1, String text2, int length1, int length2) {
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Initialization (Base Cases):
        // If either string is empty, the LCS length is 0
        for (int i = 0; i <= length1; i++) {
            for (int j = 0; j <= length2; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 0;
                }
                // If the characters at the current indices match:
                else if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // Increment the LCS length by 1 (diagonal movement)
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    // If the characters don't match, choose the maximum LCS length from the top or left cell
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[length1][length2];
    }
}
```

#### **Time and Space Complexity:**
**Memoized Recursive:**
- Time Complexity: O(length1 * length2) - Due to memoization.
- Space Complexity: O(length1 * length2) + O(length1 + length2) - For the `dp` table and the recursion call stack.
**Tabulation:**
- Time Complexity: O(length1 * length2)
- Space Complexity: O(length1 * length2)

### **Child Problems (in a sensible study order):**

1.  **Longest Common Substring**

    *   **Problem Name:** Longest Common Substring

    *   **Problem Statement:** Given two strings, find the length of the longest substring that is common to both strings. A substring is a contiguous sequence of characters within a string.

    *   **High-Level Approach:**  Use a DP table `dp[i][j]` to store the length of the longest common *suffix* of `text1[0...i-1]` and `text2[0...j-1]`. The core difference from LCS is that if the characters at `i-1` and `j-1` *don't* match, `dp[i][j]` is reset to 0. We keep track of the maximum value in the DP table, which represents the length of the longest common substring.

    *   **Connection to Parent Problem (LCS):**  While both involve finding common sequences, the key difference lies in *contiguity*. LCS allows gaps (subsequence), while Longest Common Substring requires the common sequence to be a consecutive sequence of characters. The DP solution for Longest Common Substring is a modified version of the LCS approach where non-matching characters reset the length of the common suffix, ensuring contiguity.

    *  **Dynamic Programming Pattern:** dp[i][j] = (text1[i-1] == text2[j-1]) ? 1 + dp[i-1][j-1] : 0 

2.  **Shortest Common Supersequence**

    *   **Problem Name:** Shortest Common Supersequence (SCS)

    *   **Problem Statement:** Given two strings, find the shortest supersequence. A supersequence of two strings is the shortest string that contains both as subsequences. For eg. for strings "abac" and other is "cab", the shortest supersequence is "cabac".

    *   **High-Level Approach:**
        1.  Find the Longest Common Subsequence (LCS) of the two strings.
        2.  The length of the Shortest Common Supersequence (SCS) is: `length(string1) + length(string2) - length(LCS)`.
        3. To construct the shortest common supersequence use the DP table of LCS. Match chars from end of both the strings. If they are same then include in scs string, else add the greater char and move in the same direction of smaller char.

    *   **Connection to Parent Problem (LCS):**  SCS directly *relies* on the LCS. We first compute the LCS length. The formula to find the SCS length relies on the length of the LCS. This clearly demonstrates how the LCS pattern serves as a fundamental building block.

3.  **Minimum Window Subsequence**

    *   **Problem Name:** Minimum Window Subsequence

    *   **Problem Statement:** Given strings S and T, find the minimum window in S which will contain all the characters in T in the correct order. That is, T is a subsequence of S.

    *   **High-Level Approach:** We use dynamic programming to solve this question. We traverse the S and T and when s[i] and t[j] match we consider dp[i][j] = dp[i-1][j-1] , else we assign the value to dp[i-1][j].

    *   **Connection to Parent Problem (LCS):** The problem uses the logic of subsequence, also includes the logic of sliding window. This does not include the direct usage of LCS code as it involves substring, however the logic of subsequences is from LCS.
        This is how the relationship can be established.

