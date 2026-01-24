# Unbounded Knapsack Pattern

## Core Idea
Items can be used **unlimited times**. The key difference from 0/1 Knapsack: after taking an item, you **stay on the same item** (can take it again).

## The Pattern (First Principles)

**0/1 Knapsack**: Take item → move to next item  
**Unbounded**: Take item → **stay on same item** (can take again)

```
0/1:       dp[i][w] = max(skip, take + dp[i-1][w-weight])
Unbounded: dp[i][w] = max(skip, take + dp[i][w-weight])
                                         ↑ same i, not i-1!
```


## Universal Template

```java
// 1D Space-Optimized (Most Common)
public int unboundedKnapsack(int[] items, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];
    
    for (int w = 1; w <= capacity; w++) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] <= w) {
                dp[w] = max(dp[w], values[i] + dp[w - items[i]]);
            }
        }
    }
    
    return dp[capacity];
}
```

## Three Variants

| Goal | Initialize | Update |
|------|-----------|---------|
| **Maximize** | `dp[0] = 0` | `dp[w] = max(dp[w], val + dp[w-wt])` |
| **Minimize** | `dp[0] = 0, rest = ∞` | `dp[w] = min(dp[w], 1 + dp[w-wt])` |
| **Count Ways** | `dp[0] = 1` | `dp[w] += dp[w-wt]` |

## Critical: Combinations vs Permutations

**Combinations** (order doesn't matter): `[1,2] == [2,1]`
```java
for (int item : items)              // ← Items outer
    for (int w = item; w <= capacity; w++)
        dp[w] += dp[w - item];
```

**Permutations** (order matters): `[1,2] != [2,1]`
```java
for (int w = 1; w <= capacity; w++) // ← Capacity outer
    for (int item : items)
        dp[w] += dp[w - item];
```

**Time**: O(n × capacity)  
**Space**: O(capacity)
