# String Matching Pattern

## Pattern Overview

The **String Matching** pattern involves comparing and transforming strings using dynamic programming. Problems typically ask about edit distance, pattern matching, subsequences, or string transformations.

## Core Characteristics

- **Two Strings**: Usually involves comparing two strings
- **Character-by-Character**: Process strings character by character
- **2D DP Table**: dp[i][j] represents solution for first i chars of string1 and first j chars of string2
- **Operations**: Insert, delete, replace, match characters

## When to Recognize String Matching Pattern

Look for these indicators:
1. Problem involves **two strings** to compare
2. Need to find **edit distance**, **match patterns**, or **transformations**
3. **Character operations**: insert, delete, replace, match
4. Questions about **subsequences** or **interleaving**

Example phrases: "edit distance", "delete operations", "wildcard matching", "regular expression", "interleaving strings", "distinct subsequences"

## Generic Steps to Solve String Matching Problems

### Step 1: Understand the Problem Type
- **Edit Distance**: Minimum operations to convert one string to another
- **Pattern Matching**: Check if pattern matches string (with wildcards)
- **Subsequence**: Count/check subsequences
- **Interleaving**: Check if string3 is interleaving of string1 and string2

### Step 2: Define DP State
```
dp[i][j] = solution considering first i characters of string1 
           and first j characters of string2

Examples:
- dp[i][j] = minimum edit operations for s1[0..i] to s2[0..j]
- dp[i][j] = true if s1[0..i] matches pattern s2[0..j]
- dp[i][j] = count of s2[0..j] as subsequence in s1[0..i]
```

### Step 3: Establish Base Cases
```java
// Empty strings
dp[0][0] = base_value;  // Both strings empty

// One string empty
for (int i = 1; i <= m; i++) {
    dp[i][0] = cost_of_deleting_i_characters;
}
for (int j = 1; j <= n; j++) {
    dp[0][j] = cost_of_inserting_j_characters;
}
```

### Step 4: Derive Recurrence Relation

**General Pattern**:
```java
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (s1.charAt(i-1) == s2.charAt(j-1)) {
            // Characters match
            dp[i][j] = handleMatch(dp, i, j);
        } else {
            // Characters don't match
            dp[i][j] = handleMismatch(dp, i, j);
        }
    }
}
```

## Common Problem Templates

### Template 1: Edit Distance (Levenshtein Distance)

**Problem**: Minimum operations (insert, delete, replace) to convert s1 to s2.

```java
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    // Base cases
    for (int i = 0; i <= m; i++) dp[i][0] = i;  // Delete all
    for (int j = 0; j <= n; j++) dp[0][j] = j;  // Insert all
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i-1) == word2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1];  // No operation needed
            } else {
                dp[i][j] = 1 + Math.min(
                    dp[i-1][j],      // Delete from word1
                    Math.min(
                        dp[i][j-1],  // Insert to word1
                        dp[i-1][j-1] // Replace
                    )
                );
            }
        }
    }
    
    return dp[m][n];
}
```

**Key Insight**: Three operations possible when characters mismatch.

### Template 2: Delete Operations for Two Strings

**Problem**: Minimum delete operations to make two strings equal.

```java
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    // Base cases
    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i-1) == word2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1];  // Keep both
            } else {
                dp[i][j] = 1 + Math.min(
                    dp[i-1][j],   // Delete from word1
                    dp[i][j-1]    // Delete from word2
                );
            }
        }
    }
    
    return dp[m][n];
}
```

**Alternative**: `m + n - 2 * LCS(word1, word2)` where LCS = Longest Common Subsequence.

### Template 3: Wildcard/Pattern Matching

**Problem**: Check if pattern (with `*` and `?`) matches string.

```java
public boolean isMatch(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    
    // Base case: empty string and empty pattern
    dp[0][0] = true;
    
    // Pattern with * can match empty string
    for (int j = 1; j <= n; j++) {
        if (p.charAt(j-1) == '*') {
            dp[0][j] = dp[0][j-1];
        }
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j-1) == '*') {
                // * matches empty or one+ characters
                dp[i][j] = dp[i][j-1] || dp[i-1][j];
            } else if (p.charAt(j-1) == '?' || 
                       s.charAt(i-1) == p.charAt(j-1)) {
                // ? matches any single char, or exact match
                dp[i][j] = dp[i-1][j-1];
            }
        }
    }
    
    return dp[m][n];
}
```

### Template 4: Regular Expression Matching

**Problem**: Pattern with `.` (any char) and `*` (zero or more of previous char).

```java
public boolean isMatch(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    
    dp[0][0] = true;
    
    // Handle patterns like a*, a*b*, a*b*c*
    for (int j = 2; j <= n; j++) {
        if (p.charAt(j-1) == '*') {
            dp[0][j] = dp[0][j-2];
        }
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            char sc = s.charAt(i-1);
            char pc = p.charAt(j-1);
            
            if (pc == '*') {
                // * can match zero of previous char
                dp[i][j] = dp[i][j-2];
                
                // Or match one+ if previous pattern char matches current
                char prevPC = p.charAt(j-2);
                if (prevPC == '.' || prevPC == sc) {
                    dp[i][j] = dp[i][j] || dp[i-1][j];
                }
            } else if (pc == '.' || pc == sc) {
                dp[i][j] = dp[i-1][j-1];
            }
        }
    }
    
    return dp[m][n];
}
```

**Key Insight**: `*` modifies the previous character, allowing 0 or more occurrences.

### Template 5: Distinct Subsequences

**Problem**: Count distinct subsequences of s that equals t.

```java
public int numDistinct(String s, String t) {
    int m = s.length(), n = t.length();
    long[][] dp = new long[m + 1][n + 1];
    
    // Base case: empty t can be formed in 1 way
    for (int i = 0; i <= m; i++) {
        dp[i][0] = 1;
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // Don't use s[i-1]
            dp[i][j] = dp[i-1][j];
            
            // Use s[i-1] if it matches t[j-1]
            if (s.charAt(i-1) == t.charAt(j-1)) {
                dp[i][j] += dp[i-1][j-1];
            }
        }
    }
    
    return (int) dp[m][n];
}
```

### Template 6: Interleaving String

**Problem**: Check if s3 is formed by interleaving s1 and s2.

```java
public boolean isInterleave(String s1, String s2, String s3) {
    int m = s1.length(), n = s2.length();
    if (m + n != s3.length()) return false;
    
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[0][0] = true;
    
    // Using only s1
    for (int i = 1; i <= m; i++) {
        dp[i][0] = dp[i-1][0] && s1.charAt(i-1) == s3.charAt(i-1);
    }
    
    // Using only s2
    for (int j = 1; j <= n; j++) {
        dp[0][j] = dp[0][j-1] && s2.charAt(j-1) == s3.charAt(j-1);
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            int k = i + j - 1;  // Index in s3
            dp[i][j] = (dp[i-1][j] && s1.charAt(i-1) == s3.charAt(k)) ||
                       (dp[i][j-1] && s2.charAt(j-1) == s3.charAt(k));
        }
    }
    
    return dp[m][n];
}
```

### Template 7: Is Subsequence

**Problem**: Check if s is subsequence of t.

```java
public boolean isSubsequence(String s, String t) {
    int i = 0, j = 0;
    
    while (i < s.length() && j < t.length()) {
        if (s.charAt(i) == t.charAt(j)) {
            i++;
        }
        j++;
    }
    
    return i == s.length();
}
```

**DP Approach** (useful for follow-up with multiple queries):
```java
public boolean isSubsequence(String s, String t) {
    int m = s.length(), n = t.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    
    // Empty s is subsequence of any t
    for (int j = 0; j <= n; j++) {
        dp[0][j] = true;
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s.charAt(i-1) == t.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1];
            } else {
                dp[i][j] = dp[i][j-1];
            }
        }
    }
    
    return dp[m][n];
}
```

## Space Optimization

Most string matching problems can be optimized to O(n) space:

```java
// Instead of dp[m+1][n+1], use two arrays
int[] prev = new int[n + 1];
int[] curr = new int[n + 1];

for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        // Use prev[j], prev[j-1], curr[j-1]
        curr[j] = computeValue(prev, curr, i, j);
    }
    // Swap arrays
    int[] temp = prev;
    prev = curr;
    curr = temp;
}
```

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(m × n) where m, n are string lengths
- **Space**: 
  - Full DP: O(m × n)
  - Space-optimized: O(min(m, n))

## Common Mistakes to Avoid

1. **Index confusion**: dp[i][j] uses s1[i-1] and s2[j-1]
2. **Base case errors**: Initialize dp[0][0] and edges correctly
3. **Character comparison**: Use charAt(i-1) when i starts from 1
4. **Pattern matching**: Handle special characters (`*`, `.`, `?`) carefully
5. **Integer overflow**: Use `long` for counting problems

## Problem-Solving Checklist

- [ ] Is it single string or comparing two strings?
- [ ] What operations are allowed? (insert, delete, replace, match)
- [ ] Define dp[i][j] clearly
- [ ] Initialize base cases (dp[0][0], dp[i][0], dp[0][j])
- [ ] Handle character match and mismatch separately
- [ ] For patterns, handle special characters (`*`, `.`, `?`)
- [ ] Can we optimize space to O(n)?
- [ ] Check for edge cases (empty strings, single character)

## Related Patterns

- **Longest Common Subsequence**: Foundation for many string problems
- **Edit Distance**: Classic string transformation problem
- **Sequence**: Subsequence problems overlap with sequence pattern

## Practice Problems in this Package

1. **Delete Operation for Two Strings** - Minimum deletes to make equal
2. **Distinct Subsequence** - Count distinct subsequences
3. **Interleaving String** - Check if s3 is interleaving of s1 and s2
4. **Is Subsequence** - Check if s is subsequence of t
5. **Regular Expression Matching** - Pattern matching with . and *
