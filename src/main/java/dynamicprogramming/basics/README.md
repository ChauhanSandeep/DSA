# Basics - Dynamic Programming

## Pattern Overview

The **Basics** pattern covers fundamental dynamic programming problems that introduce core DP concepts. These problems typically involve simple state transitions and serve as building blocks for more complex patterns.

## Core Characteristics

- **Simple State Definition**: Usually 1D state (dp[i] represents solution up to index i)
- **Clear Recurrence Relations**: Direct relationship between current state and previous states
- **Base Cases**: Often straightforward with 1-2 base cases
- **Foundation Problems**: Fibonacci-like sequences and simple accumulation patterns

## Common Problem Types

1. **Fibonacci Sequence Problems**: Current state depends on previous 1-2 states
2. **Pascal's Triangle**: Building values based on previous row values
3. **Simple Accumulation**: Building up results incrementally

## Generic Steps to Solve Basic DP Problems

### 1. **Identify the Problem Structure**
- Look for overlapping subproblems (solving same subproblem multiple times)
- Check if problem has optimal substructure (optimal solution contains optimal solutions to subproblems)
- Identify if current state depends on previous states in a predictable pattern

### 2. **Define the State**
- Determine what dp[i] represents
- Common patterns:
  - `dp[i]` = solution for first i elements
  - `dp[i]` = value at position i
  - `dp[i]` = answer ending at position i

### 3. **Establish Base Cases**
- What are the simplest inputs? (usually empty, single element, or first few elements)
- Initialize these values explicitly

### 4. **Derive the Recurrence Relation**
- How does dp[i] relate to previous states?
- Common patterns:
  - `dp[i] = dp[i-1] + dp[i-2]` (Fibonacci-like)
  - `dp[i] = f(dp[i-1], current_value)` (Accumulation)
  - `dp[i] = combination of previous states`

### 5. **Choose Implementation Approach**

**Top-Down (Memoization)**:
```java
int solve(int n, int[] memo) {
    // Base case
    if (n <= base_condition) return base_value;
    
    // Check memo
    if (memo[n] != -1) return memo[n];
    
    // Compute and store
    memo[n] = recurrence_relation(n);
    return memo[n];
}
```

**Bottom-Up (Tabulation)**:
```java
int solve(int n) {
    int[] dp = new int[n + 1];
    
    // Initialize base cases
    dp[0] = base_value_0;
    dp[1] = base_value_1;
    
    // Fill table iteratively
    for (int i = 2; i <= n; i++) {
        dp[i] = recurrence_relation(i);
    }
    
    return dp[n];
}
```

### 6. **Optimize Space (if applicable)**
- If dp[i] only depends on previous k states, use k variables instead of full array
- Example: Fibonacci only needs 2 variables instead of full array

```java
int solve(int n) {
    int prev2 = base_value_0;
    int prev1 = base_value_1;
    
    for (int i = 2; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }
    
    return prev1;
}
```

## Problem-Solving Template

```java
public int solveBasicDP(int n) {
    // Step 1: Handle edge cases
    if (n <= 0) return 0;
    if (n == 1) return base_case_1;
    
    // Step 2: Initialize DP array or variables
    int[] dp = new int[n + 1];
    dp[0] = base_case_0;
    dp[1] = base_case_1;
    
    // Step 3: Fill DP array using recurrence relation
    for (int i = 2; i <= n; i++) {
        dp[i] = computeRecurrence(dp, i);
    }
    
    // Step 4: Return final answer
    return dp[n];
}
```

## Key Insights

1. **Start Simple**: Basic DP problems are great for understanding the DP paradigm
2. **Recognize Patterns**: Many complex problems reduce to these basic patterns
3. **Space Optimization**: Always consider if full array is needed or just last few states
4. **Both Approaches Work**: Memoization (top-down) and tabulation (bottom-up) are equally valid

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(n) - single pass through the problem space
- **Space**: O(n) for full DP array, O(1) if space-optimized

## Common Mistakes to Avoid

1. **Not handling base cases properly** - Always initialize first few values
2. **Off-by-one errors** - Be careful with array indices
3. **Not optimizing space** - If only last k states needed, don't use full array
4. **Incorrect recurrence** - Verify the relationship between states carefully

## Related Patterns

- **Linear Partition**: Extension of basics with more complex state transitions
- **State Machine**: Similar single-state tracking but with multiple states per position
- **Sequence**: Building on basics but tracking longest/best sequences

## Practice Problems in this Package

1. **Fibonacci Number** - Classic DP introduction, fibonacci sequence
2. **Pascal's Triangle** - 2D variation, building triangle row by row
