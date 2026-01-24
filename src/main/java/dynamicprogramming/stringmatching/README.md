# String Matching Pattern

## Core Idea
Compare two strings character by character. Use 2D DP where `dp[i][j]` represents solution for first `i` chars of s1 and first `j` chars of s2.

## The Pattern (First Principles)

```
dp[i][j] = solution for s1[0..i-1] and s2[0..j-1]

If s1[i-1] == s2[j-1]:  handle match
Else:                   handle mismatch
```

## Universal Template

```java
public int stringDP(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m+1][n+1];
    
    // Base cases
    for (int i = 0; i <= m; i++) dp[i][0] = i; // or 0
    for (int j = 0; j <= n; j++) dp[0][j] = j; // or 0
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + ...;  // Match
            } else {
                dp[i][j] = min/max(
                    dp[i-1][j],   // Delete from s1
                    dp[i][j-1],   // Insert to s1 (or delete from s2)
                    dp[i-1][j-1]  // Replace
                ) + cost;
            }
        }
    }
    
    return dp[m][n];
}
```

## Common Problems

| Problem | Match | Mismatch |
|---------|-------|----------|
| **LCS** | `dp[i-1][j-1] + 1` | `max(dp[i-1][j], dp[i][j-1])` |
| **Edit Distance** | `dp[i-1][j-1]` | `1 + min(insert, delete, replace)` |
| **Delete for Equal** | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` |

## Space Optimization
Only need previous row → use two 1D arrays or rolling array.

**Time**: O(m × n)  
**Space**: O(min(m, n)) optimized
