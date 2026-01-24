# Scheduling Pattern

## Core Idea
Select non-overlapping tasks/intervals to optimize profit or minimize cost. **Sort first**, then apply DP.

## The Pattern (First Principles)

```
dp[i] = best solution using first i tasks (sorted)

For task i:
  skip: dp[i] = dp[i-1]
  take: dp[i] = profit[i] + dp[last_non_conflicting]
```

## Universal Template

```java
public int schedule(int[][] tasks) {
    // Sort by end time
    Arrays.sort(tasks, (a, b) -> a[1] - b[1]);
    
    int[] dp = new int[n];
    dp[0] = tasks[0][profit];
    
    for (int i = 1; i < n; i++) {
        int skip = dp[i-1];
        
        // Find last non-conflicting (binary search)
        int j = binarySearch(tasks, i);
        int take = tasks[i][profit] + (j >= 0 ? dp[j] : 0);
        
        dp[i] = max(skip, take);
    }
    
    return dp[n-1];
}
```

## Key Decisions

### Sorting Strategy
- **By end time**: For interval scheduling (maximize tasks)
- **By deadline**: For deadline-based problems
- **By start time**: For interval covering

### Greedy vs DP
- **Greedy**: If optimal substructure is obvious (e.g., activity selection)
- **DP**: When tasks have weights/profits

## Common Variants

| Problem | Sort By | DP or Greedy |
|---------|---------|--------------|
| **Max Profit Jobs** | End time | DP + binary search |
| **Course Schedule III** | Deadline | Greedy + heap |
| **Interval Covering** | Start time | Greedy |

**Time**: O(n log n) for sorting + O(n log n) for DP with binary search  
**Space**: O(n)
