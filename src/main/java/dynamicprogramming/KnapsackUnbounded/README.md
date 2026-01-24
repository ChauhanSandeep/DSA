# Unbounded Knapsack (KnapsackUnbounded)

## Pattern Overview

The **Unbounded Knapsack** pattern is a variation of the classic 0/1 Knapsack where items can be used **unlimited times**. This pattern is fundamental for solving optimization problems where resources can be reused.

## Core Characteristics

- **Unlimited Use**: Each item/option can be selected multiple times
- **Capacity Constraint**: Must respect a weight/capacity limit
- **Optimization Goal**: Maximize value, minimize cost, or count combinations
- **Flexible Selection**: No restriction on reusing items

## Key Difference from 0/1 Knapsack

| Aspect | 0/1 Knapsack | Unbounded Knapsack |
|--------|--------------|-------------------|
| Item Usage | Each item once | Each item unlimited times |
| State Transition | `dp[i-1][w-wt[i]]` | `dp[i][w-wt[i]]` |
| Use Case | Unique items | Repeatable resources |

**Critical Difference**: In unbounded knapsack, after including an item at index `i`, we stay at the same index (`dp[i][...]`), allowing the item to be selected again.

## When to Recognize Unbounded Knapsack Pattern

Look for these indicators:
1. **Unlimited supply** of items/choices
2. **Target** to achieve (weight, amount, sum)
3. **Optimize** value, count ways, or minimize operations
4. Items can be **reused**

Example phrases: "infinite coins", "unlimited supply", "cut rod multiple times", "unbounded resources"

## Generic Solution Template

### 2D DP Approach
```java
public int unboundedKnapsack(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];
    
    // Base case: 0 items or 0 capacity = 0 value
    
    for (int i = 1; i <= n; i++) {
        for (int w = 1; w <= capacity; w++) {
            // Don't include item i
            dp[i][w] = dp[i-1][w];
            
            // Include item i (if it fits)
            if (weights[i-1] <= w) {
                // Key: dp[i][w-weight] not dp[i-1][...]
                dp[i][w] = Math.max(dp[i][w], 
                                   values[i-1] + dp[i][w - weights[i-1]]);
            }
        }
    }
    
    return dp[n][capacity];
}
```

### 1D Space-Optimized Approach
```java
public int unboundedKnapsackOptimized(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];
    
    // For each capacity
    for (int w = 1; w <= capacity; w++) {
        // Try each item
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= w) {
                dp[w] = Math.max(dp[w], 
                               values[i] + dp[w - weights[i]]);
            }
        }
    }
    
    return dp[capacity];
}
```

## Common Problem Variations

### 1. **Rod Cutting** - Maximize profit from cutting rod

```java
public int cutRod(int[] prices, int n) {
    // prices[i] = price of rod of length i+1
    int[] dp = new int[n + 1];
    
    for (int length = 1; length <= n; length++) {
        for (int cutLength = 1; cutLength <= length; cutLength++) {
            dp[length] = Math.max(dp[length], 
                                 prices[cutLength - 1] + dp[length - cutLength]);
        }
    }
    
    return dp[n];
}
```

**Key Insight**: Try all possible first cuts, use DP for remaining length.

### 2. **Coin Change (Minimum Coins)** - Minimize coins to make amount

```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);  // Initialize with "infinity"
    dp[0] = 0;  // Base case
    
    for (int amt = 1; amt <= amount; amt++) {
        for (int coin : coins) {
            if (coin <= amt) {
                dp[amt] = Math.min(dp[amt], dp[amt - coin] + 1);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}
```

**Key Insight**: For each amount, try all coins and take minimum.

### 3. **Coin Change II (Count Ways)** - Count ways to make amount

```java
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;  // One way to make 0: use no coins
    
    // IMPORTANT: Outer loop on coins to avoid counting permutations
    for (int coin : coins) {
        for (int amt = coin; amt <= amount; amt++) {
            dp[amt] += dp[amt - coin];
        }
    }
    
    return dp[amount];
}
```

**Key Insight**: Outer loop on coins ensures we count combinations, not permutations.

### 4. **Perfect Squares** - Minimum squares that sum to n

```java
public int numSquares(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;
    
    // Precompute all perfect squares <= n
    List<Integer> squares = new ArrayList<>();
    for (int i = 1; i * i <= n; i++) {
        squares.add(i * i);
    }
    
    for (int i = 1; i <= n; i++) {
        for (int square : squares) {
            if (square <= i) {
                dp[i] = Math.min(dp[i], dp[i - square] + 1);
            }
        }
    }
    
    return dp[n];
}
```

### 5. **Birthday Party** - Items with weights and values, capacity constraint

This is the classic unbounded knapsack - use the template above.

## Combinations vs Permutations

**Critical Concept**: Loop order matters when counting!

**For Combinations** (order doesn't matter):
```java
// [1,2,5] and [2,1,5] are the SAME
for (int item : items) {              // Outer: items
    for (int capacity = item; capacity <= target; capacity++) {  // Inner: capacity
        dp[capacity] += dp[capacity - item];
    }
}
```

**For Permutations** (order matters):
```java
// [1,2,5] and [2,1,5] are DIFFERENT
for (int capacity = 1; capacity <= target; capacity++) {  // Outer: capacity
    for (int item : items) {          // Inner: items
        if (item <= capacity) {
            dp[capacity] += dp[capacity - item];
        }
    }
}
```

## Problem-Solving Steps

### Step 1: Identify Components
- **Items**: What can be selected? (coins, rod cuts, squares)
- **Capacity/Target**: What limit exists? (amount, rod length, sum)
- **Objective**: Maximize, minimize, or count?

### Step 2: Define DP State
```
dp[capacity] = optimal solution for given capacity
```

### Step 3: Set Base Case
```java
// For minimization
dp[0] = 0;
Arrays.fill(dp, 1, n+1, Integer.MAX_VALUE);

// For maximization
dp[0] = 0;

// For counting
dp[0] = 1;
```

### Step 4: Fill DP Table
```java
for (each capacity) {
    for (each item) {
        if (item fits) {
            update dp[capacity] using dp[capacity - item_weight]
        }
    }
}
```

## Time and Space Complexity

- **Time Complexity**: O(n × capacity) where n = number of items
- **Space Complexity**: 
  - 2D: O(n × capacity)
  - 1D optimized: O(capacity)

## Common Mistakes to Avoid

1. **Using `dp[i-1][...]` instead of `dp[i][...]`**: Makes it 0/1 knapsack
2. **Wrong loop order for counting**: Use items outer loop for combinations
3. **Incorrect base case**: Initialize based on problem type (0, 1, or infinity)
4. **Forgetting item can fit check**: Always verify `weight <= capacity`
5. **Integer overflow in counting**: Use `long` if numbers get large

## Problem-Solving Checklist

- [ ] Can items be reused unlimited times?
- [ ] What's the capacity/target constraint?
- [ ] What's the objective? (maximize, minimize, count)
- [ ] Define `dp[i]` clearly
- [ ] Set correct base case
- [ ] Use `dp[same_index]` for inclusion (unbounded property)
- [ ] Choose correct loop order (combinations vs permutations)
- [ ] Handle edge cases (target = 0, no items)

## Related Patterns

- **0/1 Knapsack (Bounded)**: Each item used at most once
- **Complete Knapsack**: Another name for unbounded knapsack
- **Coin Change**: Classic unbounded knapsack application

## Practice Problems in this Package

1. **Coin Change** - Minimum coins to make amount
2. **Coin Change II** - Count ways to make amount
3. **Rod Cutting** - Maximize profit from cutting rod
4. **Birthday Party** - Classic unbounded knapsack problem
5. **Call Counter** - Resource allocation problem

## Key Takeaway

**Remember**: The defining characteristic of unbounded knapsack is `dp[i][w - weight[i]]` instead of `dp[i-1][w - weight[i]]`. This allows items to be selected multiple times, making it "unbounded."
