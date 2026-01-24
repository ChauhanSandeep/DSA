# Game Theory Pattern

## Core Idea
Two players, both play optimally. Use **minimax**: maximize your advantage while opponent minimizes it.

## The Pattern (First Principles)

```
Your advantage = your_points - opponent's_best_response

dp[i][j] = max advantage for current player in range [i, j]
         = max(
             value[i] - dp[i+1][j],   // Take left
             value[j] - dp[i][j-1]    // Take right
           )
```

The minus sign is key: you gain, opponent gets their best (which reduces your advantage).

## Universal Template

```java
public boolean canWin(int[] piles) {
    int n = piles.length;
    int[][] dp = new int[n][n];
    
    // Base: single pile
    for (int i = 0; i < n; i++) {
        dp[i][i] = piles[i];
    }
    
    // Fill for increasing lengths
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i + len <= n; i++) {
            int j = i + len - 1;
            
            int takeLeft = piles[i] - dp[i+1][j];
            int takeRight = piles[j] - dp[i][j-1];
            
            dp[i][j] = max(takeLeft, takeRight);
        }
    }
    
    return dp[0][n-1] > 0;  // Positive advantage = win
}
```

## Common Patterns

### Pick from Ends (Stone Game)
```java
dp[i][j] = max(arr[i] - dp[i+1][j], arr[j] - dp[i][j-1])
```

### Variable Take (Stone Game II)
```java
// Can take 1 to 2M piles, M increases
for (int x = 1; x <= 2*M; x++) {
    // Try taking x piles
}
```

### Nim-like (Divisor Game)
```java
// Identify winning/losing positions
// Often has mathematical pattern
```

## Key Insight
It's about **advantage**, not absolute scores. Final answer: `dp[0][n-1] >= 0` means first player wins.

**Time**: O(n²) or O(n² × M)  
**Space**: O(n²) or O(n) optimized
