# Sequence Pattern

## Core Idea
Find optimal **subsequence** (can skip elements, order maintained). Often comparing or finding longest/best.

## The Pattern (First Principles)

### Single Sequence (LIS-type)
```
dp[i] = best subsequence ending at index i

dp[i] = 1 + max(dp[j]) for all j < i where arr[j] < arr[i]
```

### Two Sequences (LCS-type)
```
dp[i][j] = solution for s1[0..i] and s2[0..j]

If s1[i] == s2[j]:  dp[i][j] = 1 + dp[i-1][j-1]
Else:               dp[i][j] = max(dp[i-1][j], dp[i][j-1])
```

## Universal Templates

### LIS (O(n²))
```java
int[] dp = new int[n];
Arrays.fill(dp, 1);

for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++) {
        if (arr[j] < arr[i]) {
            dp[i] = max(dp[i], dp[j] + 1);
        }
    }
}
return max(dp);
```

### LCS
```java
int[][] dp = new int[m+1][n+1];

for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (s1[i-1] == s2[j-1]) {
            dp[i][j] = dp[i-1][j-1] + 1;
        } else {
            dp[i][j] = max(dp[i-1][j], dp[i][j-1]);
        }
    }
}
return dp[m][n];
```

## Key Insight
**Subsequence** ≠ **Substring**. Subsequence allows gaps, substring requires contiguity.

**Time**: O(n²) for LIS, O(m×n) for LCS  
**Space**: O(n) or O(m×n)
