# Linear Partition Pattern

## Pattern Overview

The **Linear Partition** pattern involves partitioning a linear sequence (string, array) into segments where each segment satisfies certain constraints. The goal is typically to optimize or count the number of valid partitions.

## Core Characteristics

- **Linear Structure**: Problems involve a sequence (string, array, list)
- **Partitioning Decision**: At each position, decide where to make a cut/partition
- **Validation**: Each partition must satisfy some constraint (valid word, palindrome, etc.)
- **State Transition**: Current state depends on all valid previous partition points

## When to Recognize Linear Partition Pattern

Look for these indicators:
1. Problem involves **breaking/partitioning** a string or array
2. Each partition/segment must satisfy a **constraint**
3. Need to find **minimum cuts**, **count ways**, or **all valid partitions**
4. Decision at position i depends on checking all positions j < i

Example phrases: "partition string", "word break", "decode ways", "minimum cuts", "split array"

## Generic Steps to Solve Linear Partition Problems

### Step 1: Identify Problem Components
- **Input Sequence**: What are we partitioning? (string, array)
- **Validation Function**: How to check if a segment is valid?
- **Objective**: Minimize cuts? Count ways? Find all partitions?

### Step 2: Define DP State

**Common State Definitions**:
```
dp[i] = solution for substring/subarray from 0 to i

Examples:
- dp[i] = true if s[0..i] can be validly partitioned
- dp[i] = minimum cuts needed for s[0..i]
- dp[i] = number of ways to partition s[0..i]
```

### Step 3: Establish Base Cases
```java
// For boolean (can partition?)
dp[0] = true;  // Empty string is valid

// For counting ways
dp[0] = 1;     // One way to partition empty string

// For minimum cuts
dp[0] = 0;     // No cuts needed for empty string
```

### Step 4: Derive Recurrence Relation

**General Pattern**:
```java
for (int i = 1; i <= n; i++) {
    // Try all possible last partitions ending at i
    for (int j = 0; j < i; j++) {
        // Check if segment [j, i) is valid
        if (dp[j] && isValid(s, j, i)) {
            // Update dp[i] based on problem type
            dp[i] = updateFunction(dp[i], dp[j]);
        }
    }
}
```

**Key Insight**: For each position i, we look back at all positions j < i. If s[0..j] can be partitioned (dp[j] is true/valid) AND s[j..i] is valid, then s[0..i] can be partitioned.

### Step 5: Implementation Templates

**Template 1: Boolean Check (Can Partition?)**
```java
public boolean canPartition(String s, Set<String> validSegments) {
    int n = s.length();
    boolean[] dp = new boolean[n + 1];
    dp[0] = true;  // Base case: empty string
    
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < i; j++) {
            // If s[0..j] is valid AND s[j..i] is valid
            if (dp[j] && validSegments.contains(s.substring(j, i))) {
                dp[i] = true;
                break;  // Found one valid partition
            }
        }
    }
    
    return dp[n];
}
```

**Template 2: Count Ways**
```java
public int countPartitions(String s, Set<String> validSegments) {
    int n = s.length();
    int[] dp = new int[n + 1];
    dp[0] = 1;  // One way to partition empty string
    
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < i; j++) {
            if (validSegments.contains(s.substring(j, i))) {
                dp[i] += dp[j];  // Add ways from j
            }
        }
    }
    
    return dp[n];
}
```

**Template 3: Minimize Cuts**
```java
public int minCuts(String s) {
    int n = s.length();
    int[] dp = new int[n + 1];
    
    // Initialize with worst case (i-1 cuts for length i)
    for (int i = 0; i <= n; i++) {
        dp[i] = i - 1;
    }
    
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < i; j++) {
            // If segment [j, i) is valid
            if (isValid(s, j, i)) {
                // dp[i] = min cuts to get to j + 1 cut
                dp[i] = Math.min(dp[i], dp[j] + 1);
            }
        }
    }
    
    return dp[n];
}
```

**Template 4: Find All Partitions**
```java
public List<List<String>> allPartitions(String s) {
    List<List<String>> result = new ArrayList<>();
    backtrack(s, 0, new ArrayList<>(), result);
    return result;
}

private void backtrack(String s, int start, List<String> current, 
                      List<List<String>> result) {
    if (start == s.length()) {
        result.add(new ArrayList<>(current));
        return;
    }
    
    for (int end = start + 1; end <= s.length(); end++) {
        String segment = s.substring(start, end);
        if (isValid(segment)) {
            current.add(segment);
            backtrack(s, end, current, result);
            current.remove(current.size() - 1);
        }
    }
}
```

## Common Problem Variations

### 1. **Word Break** (Can partition into dictionary words?)
```java
// dp[i] = true if s[0..i] can be segmented
// Transition: dp[i] = dp[j] && dict.contains(s[j..i])
```

### 2. **Word Break II** (Find all valid partitions)
```java
// Use backtracking with memoization
// At each position, try all valid words and recurse
```

### 3. **Decode Ways** (Count ways to decode string)
```java
// dp[i] = number of ways to decode s[0..i]
// Transition: dp[i] = dp[i-1] (if valid 1-digit) + 
//                     dp[i-2] (if valid 2-digit)
```

### 4. **Palindrome Partitioning** (Partition into palindromes)
```java
// For minimum cuts: dp[i] = min cuts for s[0..i]
// For all partitions: backtracking with palindrome check
```

## Optimization Techniques

### 1. **Precompute Validity**
For expensive validity checks (like palindrome checking), precompute:
```java
boolean[][] isPalindrome = new boolean[n][n];
// Precompute all [i, j] palindromes in O(n²)

for (int len = 1; len <= n; len++) {
    for (int i = 0; i + len <= n; i++) {
        int j = i + len - 1;
        if (len == 1) isPalindrome[i][j] = true;
        else if (len == 2) isPalindrome[i][j] = (s.charAt(i) == s.charAt(j));
        else isPalindrome[i][j] = (s.charAt(i) == s.charAt(j)) && 
                                   isPalindrome[i+1][j-1];
    }
}
```

### 2. **Trie for Dictionary**
For word break problems with large dictionaries:
```java
// Use Trie for O(m) word lookup instead of O(m × dict_size)
class TrieNode {
    Map<Character, TrieNode> children;
    boolean isWord;
}
```

### 3. **Memoization for Backtracking**
When finding all partitions with expensive validity:
```java
Map<Integer, List<String>> memo = new HashMap<>();
// Memoize partial results to avoid recomputation
```

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(n²) for basic DP, O(n² × validation_cost) with validation
- **Space**: O(n) for 1D DP array

**With Precomputation**:
- **Time**: O(n²) for precomputation + O(n²) for DP = O(n²)
- **Space**: O(n²) for precomputed validity table

## Common Mistakes to Avoid

1. **Incorrect substring indices**: Be careful with inclusive/exclusive bounds
2. **Not checking dp[j] before checking segment**: Always verify prefix is valid first
3. **Wrong base case**: dp[0] should be true/1/0 depending on problem type
4. **Expensive repeated validation**: Precompute validity for common checks
5. **Off-by-one errors**: String indices vs DP array indices

## Problem-Solving Checklist

- [ ] What are we partitioning? (string, array)
- [ ] What makes a partition valid?
- [ ] What's the objective? (boolean, count, minimize, all partitions)
- [ ] Define dp[i] clearly
- [ ] Set correct base case dp[0]
- [ ] For each i, check all j < i
- [ ] Verify dp[j] is valid before checking segment [j, i)
- [ ] Can we precompute validity?
- [ ] Handle edge cases (empty string, single character)

## Related Patterns

- **Unbounded Knapsack**: Similar DP structure but different problem domain
- **LCS/Sequence**: Both involve string DP but different recurrence
- **State Machine**: Can model some partition problems as state transitions

## Practice Problems in this Package

1. **Climbing Stairs** - Count ways to reach top (simple partition)
2. **Decode Ways** - Count ways to decode digit string
3. **Word Break** - Check if string can be segmented into dictionary words
4. **Word Break II** - Find all valid segmentations
5. **Palindrome Partitioning** - Partition string into palindromes
