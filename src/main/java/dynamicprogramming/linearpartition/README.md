# Linear Partition Pattern

## Core Idea
Break a sequence into **segments** where each segment must satisfy a constraint. Find optimal way to partition.

## The Pattern (First Principles)

For position `i`, try all possible **last segment endings**:
```
dp[i] = best way to partition [0...i]
      = min/max over all j < i of:
          (dp[j] + cost/validity of segment [j+1...i])
```

## Universal Template

```java
public int partition(String s, Set<String> valid) {
    int n = s.length();
    int[] dp = new int[n + 1];
    dp[0] = 0; // base case
    
    for (int i = 1; i <= n; i++) {
        dp[i] = INFINITY; // or appropriate init
        
        // Try all possible last segments
        for (int j = 0; j < i; j++) {
            if (isValid(s, j, i)) {
                dp[i] = optimize(dp[i], dp[j] + cost);
            }
        }
    }
    
    return dp[n];
}
```

## Common Problems

| Problem | Valid Segment | Goal |
|---------|--------------|------|
| **Word Break** | Word in dictionary | Can partition? |
| **Palindrome Partition** | Is palindrome | Min cuts |
| **Decode Ways** | Valid digit(s) | Count ways |

## Optimization
Precompute validity for O(n²) segments if checking is expensive (e.g., palindromes).

**Time**: O(n²) or O(n² × validation)  
**Space**: O(n)
