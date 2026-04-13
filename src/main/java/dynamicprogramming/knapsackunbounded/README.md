# Unbounded Knapsack Pattern

## Core Idea
Items can be used **unlimited times**. After taking an item, **stay on same item** (can take again).

## The Pattern (First Principles)

**0/1 Knapsack**: Take item → move to next  
**Unbounded**: Take item → **stay** (can take again)

```
0/1:       dp[i][w] = max(skip, val + dp[i-1][w-wt])
Unbounded: dp[i][w] = max(skip, val + dp[i][w-wt])
                                         ↑ same i!
```

## Universal Template

```java
public int unbounded(int[] items, int[] values, int capacity) {
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
| **Count** | `dp[0] = 1` | `dp[w] += dp[w-wt]` |

## Combinations vs Permutations

**Combinations** ([1,2] == [2,1]):
```java
for (int item : items)               // ← Items outer
    for (int w = item; w <= cap; w++)
        dp[w] += dp[w - item];
```

**Permutations** ([1,2] != [2,1]):
```java
for (int w = 1; w <= cap; w++)      // ← Capacity outer
    for (int item : items)
        dp[w] += dp[w - item];
```

**Time**: O(n × capacity)  
**Space**: O(capacity)
