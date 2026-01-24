# Unbounded Knapsack Pattern

## Pattern Overview

The **Unbounded Knapsack** pattern is a variation of the classic 0/1 Knapsack where items can be used **unlimited times**. This pattern is used when you have infinite supply of each item and need to optimize some objective (maximize value, minimize cost, count ways, etc.).

## Core Characteristics

- **Unlimited Use**: Each item can be selected multiple times
- **Optimization Goal**: Maximize/minimize some value or count combinations
- **Capacity Constraint**: Must respect a capacity or target limit
- **Order Doesn't Matter**: (1,2,5) and (2,1,5) are same - we count combinations, not permutations

## Key Difference from 0/1 Knapsack

| Aspect | 0/1 Knapsack | Unbounded Knapsack |
|--------|--------------|-------------------|
| Item Usage | Each item used at most once | Each item can be used unlimited times |
| State Transition | Move to next item after decision | Can stay on same item after inclusion |
| DP Recurrence | `dp[i][w] = max(dp[i-1][w], dp[i-1][w-wt[i]] + val[i])` | `dp[i][w] = max(dp[i-1][w], dp[i][w-wt[i]] + val[i])` |

**Critical Insight**: In unbounded knapsack, after including an item, we don't move to the next item - we stay on the current item (`dp[i][...]` instead of `dp[i-1][...]`), allowing it to be picked again.

## When to Recognize Unbounded Knapsack Pattern

Look for these indicators:
1. **Unlimited supply** of items/options mentioned
2. Need to achieve a **target** (sum, capacity, amount)
3. **Optimize** value, count ways, or minimize cost
4. Can use same item/choice **multiple times**

Example phrases: "infinite coins", "unlimited supply", "rod can be cut multiple times", "unbounded resources"

## Generic Steps to Solve Unbounded Knapsack Problems

### Step 1: Identify Problem Components
- **Items/Options**: What can you choose from? (coins, rod lengths, items)
- **Capacity/Target**: What limit must you respect? (amount, rod length, weight)
- **Objective**: What to optimize? (minimize coins, maximize value, count ways)

### Step 2: Define DP State
```
dp[i][capacity] = optimal solution using first i items with given capacity
```

Or space-optimized (1D):
```
dp[capacity] = optimal solution to achieve this capacity
```

### Step 3: Establish Base Cases
```java
// For minimization problems (e.g., coin change - minimum coins)
dp[0] = 0;  // Base case: 0 capacity needs 0 items
Arrays.fill(dp, Integer.MAX_VALUE); // Initialize with "impossible"

// For maximization problems (e.g., rod cutting)
dp[0] = 0;  // Base case
// Other values naturally start at 0

// For counting ways (e.g., coin change II)
dp[0] = 1;  // One way to make 0: select nothing
// Other values start at 0
```

### Step 4: Derive Recurrence Relation

**General Pattern**:
```java
for (int item = 0; item < n; item++) {
    for (int capacity = 1; capacity <= targetCapacity; capacity++) {
        // Don't include current item
        int exclude = dp[capacity];
        
        // Include current item (if it fits)
        int include = 0;
        if (items[item] <= capacity) {
            include = dp[capacity - items[item]] + operation(item);
            // Note: dp[capacity - items[item]], NOT dp[previous][...]
        }
        
        dp[capacity] = optimize(exclude, include); // max/min/sum
    }
}
```

**Key Insight**: When including item `i`, we reference `dp[capacity - weight[i]]` (same row), not previous row, because we can reuse the item.

### Step 5: Implementation Template

**2D Approach** (Tabulation):
```java
public int unboundedKnapsack(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];
    
    // Base case: dp[0][...] = 0 (no items)
    // dp[...][0] = 0 (no capacity)
    
    for (int i = 1; i <= n; i++) {
        for (int w = 1; w <= capacity; w++) {
            // Option 1: Don't include item i
            dp[i][w] = dp[i-1][w];
            
            // Option 2: Include item i (if it fits)
            if (weights[i-1] <= w) {
                // Key: dp[i][w - weight] allows reusing item i
                dp[i][w] = Math.max(dp[i][w], 
                                   dp[i][w - weights[i-1]] + values[i-1]);
            }
        }
    }
    
    return dp[n][capacity];
}
```

**1D Space-Optimized Approach**:
```java
public int unboundedKnapsackOptimized(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];
    
    // Base case
    dp[0] = 0;
    
    // For each capacity
    for (int w = 1; w <= capacity; w++) {
        // Try each item
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= w) {
                dp[w] = Math.max(dp[w], 
                               dp[w - weights[i]] + values[i]);
            }
        }
    }
    
    return dp[capacity];
}
```

## Common Variations

### 1. **Minimize Items/Cost** (e.g., Coin Change)
```java
// Initialize with "infinity"
Arrays.fill(dp, amount + 1);
dp[0] = 0;

for (int coin : coins) {
    for (int amt = coin; amt <= amount; amt++) {
        dp[amt] = Math.min(dp[amt], dp[amt - coin] + 1);
    }
}

return dp[amount] > amount ? -1 : dp[amount];
```

### 2. **Count Ways** (e.g., Coin Change II)
```java
dp[0] = 1;  // One way to make 0

// IMPORTANT: Outer loop on coins to avoid counting permutations
for (int coin : coins) {
    for (int amt = coin; amt <= amount; amt++) {
        dp[amt] += dp[amt - coin];
    }
}

return dp[amount];
```

### 3. **Maximize Value** (e.g., Rod Cutting)
```java
dp[0] = 0;

for (int length = 1; length <= n; length++) {
    for (int cutLength = 1; cutLength <= length; cutLength++) {
        dp[length] = Math.max(dp[length], 
                             dp[length - cutLength] + prices[cutLength]);
    }
}

return dp[n];
```

## Order Matters: Combinations vs Permutations

**For Combinations** (order doesn't matter):
```java
// Outer loop: items, Inner loop: capacity
for (int item : items) {
    for (int capacity = item; capacity <= target; capacity++) {
        dp[capacity] += dp[capacity - item];
    }
}
```

**For Permutations** (order matters):
```java
// Outer loop: capacity, Inner loop: items
for (int capacity = 1; capacity <= target; capacity++) {
    for (int item : items) {
        if (item <= capacity) {
            dp[capacity] += dp[capacity - item];
        }
    }
}
```

## Time and Space Complexity

- **Time Complexity**: O(n × capacity) where n is number of items
- **Space Complexity**: 
  - 2D approach: O(n × capacity)
  - 1D optimized: O(capacity)

## Common Mistakes to Avoid

1. **Using dp[i-1][...] instead of dp[i][...]**: This makes it 0/1 knapsack, not unbounded
2. **Wrong loop order for counting**: Use items outer loop to count combinations, not permutations
3. **Not initializing base case correctly**: dp[0] initialization depends on problem type
4. **Forgetting boundary checks**: Always check if item fits before including

## Problem-Solving Checklist

- [ ] Can items be reused unlimited times?
- [ ] What is the capacity/target constraint?
- [ ] What's the objective: minimize, maximize, or count?
- [ ] Define dp[i] or dp[i][j] clearly
- [ ] Set correct base case (0, 1, or infinity)
- [ ] Use dp[same row] for inclusion, not previous row
- [ ] Choose correct loop order (combinations vs permutations)
- [ ] Handle edge cases (target = 0, no items, negative values)

## Related Patterns

- **0/1 Knapsack (Bounded)**: Each item used at most once
- **Complete Knapsack**: Another name for unbounded knapsack
- **Linear Partition**: Similar 1D DP but different problem structure

## Practice Problems in this Package

1. **Coin Change** - Minimize number of coins to make amount
2. **Coin Change II** - Count ways to make amount with unlimited coins
