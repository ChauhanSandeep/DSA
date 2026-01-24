# Basics Pattern

## Core Idea
Current state depends on a **fixed number of previous states**. This is the simplest DP pattern where `dp[i]` is computed from `dp[i-1]`, `dp[i-2]`, etc.

## The Pattern (First Principles)

**Question**: How do I solve for position `i`?  
**Answer**: Look at the last few positions and combine them.

```
dp[i] = f(dp[i-1], dp[i-2], ..., dp[i-k])
```

## Universal Template

```java
public int solve(int n) {
    // Base cases
    if (n <= 1) return base_value;
    
    // Space-optimized (only keep last k values)
    int prev2 = base_0;  // dp[i-2]
    int prev1 = base_1;  // dp[i-1]
    
    for (int i = 2; i <= n; i++) {
        int current = combine(prev1, prev2);  // Recurrence
        prev2 = prev1;
        prev1 = current;
    }
    
    return prev1;
}
```

## Common Recurrences

| Pattern | Recurrence | Example |
|---------|-----------|---------|
| **Fibonacci** | `dp[i] = dp[i-1] + dp[i-2]` | Climbing Stairs |
| **Single Previous** | `dp[i] = f(dp[i-1], arr[i])` | Running Sum |
| **Fixed Window** | `dp[i] = max/min of dp[i-k] to dp[i-1]` | Max in Window |

## Key Insight

If you only need the last `k` values to compute the next one, **don't use an array** - use `k` variables.

**Time**: O(n)  
**Space**: O(1) with optimization, O(n) with full array
