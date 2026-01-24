# Miscellaneous DP

## Purpose
Problems that don't fit standard patterns or **combine multiple patterns**. Each requires creative thinking.

## Approach

1. **Identify core subproblem**: What repeats?
2. **Define state creatively**: What info do you need?
3. **Find recurrence**: How do states relate?
4. **Check if DP applies**: Overlapping subproblems? Optimal substructure?

## Common Techniques

### Working Backwards
Sometimes easier to go from end to start:
```java
for (int i = n-1; i >= 0; i--) {
    dp[i] = f(dp[i+1], dp[i+2], ...);
}
```

### Multi-Dimensional State
Track multiple aspects:
```java
dp[pos][constraint1][constraint2] = ...
```

### Greedy + DP Hybrid
Sort first, then DP:
```java
Arrays.sort(arr);
for (int i = 0; i < n; i++) {
    dp[i] = ...;
}
```

### Simulation with DP
Simulate process while tracking optimal states (e.g., Champagne Tower).

## Problem-Solving Strategy

1. Try standard patterns first
2. If doesn't fit, think: what makes this problem unique?
3. Work through small examples manually
4. Look for repeating subproblems
5. Design state to capture essential information only

No universal template - adapt based on problem structure.
