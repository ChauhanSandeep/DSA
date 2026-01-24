# Grid Path Pattern

## Core Idea
Navigate a 2D grid from start to end. Value at `(i,j)` depends on where you came from (usually top or left).

## The Pattern (First Principles)

```
dp[i][j] = optimal value to reach cell (i, j)
         = grid[i][j] + optimize(dp[i-1][j], dp[i][j-1])
                               ↑ from top   ↑ from left
```

## Universal Template

```java
public int gridDP(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // Base: first cell
    dp[0][0] = grid[0][0];
    
    // Fill first row (can only come from left)
    for (int j = 1; j < n; j++)
        dp[0][j] = dp[0][j-1] + grid[0][j];
    
    // Fill first column (can only come from top)
    for (int i = 1; i < m; i++)
        dp[i][0] = dp[i-1][0] + grid[i][0];
    
    // Fill rest
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = grid[i][j] + optimize(dp[i-1][j], dp[i][j-1]);
        }
    }
    
    return dp[m-1][n-1];
}
```

## Space Optimization
Only need previous row → use 1D array:
```java
int[] dp = new int[n];
// dp[j] has value from previous row
// dp[j-1] has value from current row (left)
```

## Common Variants

| Problem | Operation |
|---------|-----------|
| Min Path Sum | `min(from_top, from_left)` |
| Max Path Sum | `max(from_top, from_left)` |
| Unique Paths | `sum(from_top, from_left)` |
| With Obstacles | `if obstacle: dp[i][j] = 0` |

**Time**: O(m × n)  
**Space**: O(n) optimized
