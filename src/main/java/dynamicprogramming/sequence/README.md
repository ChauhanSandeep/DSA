# Sequence Pattern

## Pattern Overview

The **Sequence** pattern focuses on problems involving finding optimal subsequences or analyzing sequence properties. Unlike substring problems which require contiguity, subsequences maintain relative order but allow gaps.

## Core Characteristics

- **Non-Contiguous**: Elements can be selected with gaps (unlike substrings)
- **Order Preserved**: Relative order of elements must be maintained
- **Optimization**: Usually find longest, shortest, maximum, or count subsequences
- **Comparison**: Often involves comparing elements or sequences

## When to Recognize Sequence Pattern

Look for these indicators:
1. Problem asks for **longest/shortest subsequence** (not substring)
2. Elements can be **skipped** while maintaining order
3. Need to **compare** or **match** elements
4. Keywords: "increasing", "decreasing", "common", "chain", "subsequence"

Example phrases: "longest increasing subsequence", "maximum length chain", "common subsequence", "arithmetic subsequence"

## Generic Steps to Solve Sequence Problems

### Step 1: Identify Sequence Type

**Question**: What kind of subsequence are we looking for?
- Increasing/Decreasing
- Common between two sequences
- Satisfying arithmetic/geometric property
- Maximum/Minimum length with constraints

### Step 2: Define DP State

**Common State Definitions**:
```
1D Sequence:
dp[i] = optimal subsequence ending at index i

2D Sequence (Comparison):
dp[i][j] = optimal value considering sequences up to i and j

With Property:
dp[i][prop] = optimal subsequence ending at i with property 'prop'
```

### Step 3: Establish Base Cases
```java
// For single sequence
dp[0] = 1;  // Base case: single element

// For two sequences (LCS-like)
dp[0][j] = 0;  // Empty first sequence
dp[i][0] = 0;  // Empty second sequence

// With property
dp[0][any_property] = 1;  // Initialize first element
```

### Step 4: Derive Recurrence Relation

**General Pattern for LIS-type problems**:
```java
for (int i = 0; i < n; i++) {
    dp[i] = 1;  // At least the element itself
    
    for (int j = 0; j < i; j++) {
        if (canExtend(j, i)) {  // Check if i can extend j
            dp[i] = Math.max(dp[i], dp[j] + 1);
        }
    }
}
```

**General Pattern for LCS-type problems**:
```java
for (int i = 1; i <= n1; i++) {
    for (int j = 1; j <= n2; j++) {
        if (match(seq1[i-1], seq2[j-1])) {
            dp[i][j] = dp[i-1][j-1] + 1;  // Extend both
        } else {
            dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);  // Skip one
        }
    }
}
```

## Common Problem Templates

### Template 1: Longest Increasing Subsequence (LIS)

**O(n²) DP Approach**:
```java
public int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];  // dp[i] = LIS ending at i
    Arrays.fill(dp, 1);
    int maxLen = 1;
    
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
        maxLen = Math.max(maxLen, dp[i]);
    }
    
    return maxLen;
}
```

**O(n log n) Binary Search Approach**:
```java
public int lengthOfLIS(int[] nums) {
    List<Integer> tails = new ArrayList<>();
    
    for (int num : nums) {
        int pos = binarySearch(tails, num);
        if (pos == tails.size()) {
            tails.add(num);
        } else {
            tails.set(pos, num);
        }
    }
    
    return tails.size();
}

private int binarySearch(List<Integer> tails, int target) {
    int left = 0, right = tails.size();
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (tails.get(mid) < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

### Template 2: Longest Common Subsequence (LCS)

```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
    }
    
    return dp[m][n];
}
```

### Template 3: Arithmetic/Geometric Subsequence

```java
public int longestArithmeticSubsequence(int[] nums) {
    int n = nums.length;
    // dp[i][diff] = length of arithmetic subseq ending at i with diff
    Map<Integer, Integer>[] dp = new HashMap[n];
    int maxLen = 2;
    
    for (int i = 0; i < n; i++) {
        dp[i] = new HashMap<>();
        for (int j = 0; j < i; j++) {
            int diff = nums[i] - nums[j];
            // Extend from j or start new with length 2
            int len = dp[j].getOrDefault(diff, 1) + 1;
            dp[i].put(diff, len);
            maxLen = Math.max(maxLen, len);
        }
    }
    
    return maxLen;
}
```

### Template 4: Maximum Length Chain

```java
public int findLongestChain(int[][] pairs) {
    // Sort by end point
    Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
    
    int n = pairs.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (pairs[j][1] < pairs[i][0]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
    }
    
    return Arrays.stream(dp).max().getAsInt();
}
```

## LIS Variations

### 1. **Longest Decreasing Subsequence**
```java
// Change condition: nums[j] > nums[i]
if (nums[j] > nums[i]) {
    dp[i] = Math.max(dp[i], dp[j] + 1);
}
```

### 2. **Longest Non-Decreasing Subsequence**
```java
// Change condition: nums[j] <= nums[i]
if (nums[j] <= nums[i]) {
    dp[i] = Math.max(dp[i], dp[j] + 1);
}
```

### 3. **Number of LIS**
```java
int[] lengths = new int[n];  // Length of LIS ending at i
int[] counts = new int[n];   // Count of LIS ending at i

for (int i = 0; i < n; i++) {
    lengths[i] = 1;
    counts[i] = 1;
    
    for (int j = 0; j < i; j++) {
        if (nums[j] < nums[i]) {
            if (lengths[j] + 1 > lengths[i]) {
                lengths[i] = lengths[j] + 1;
                counts[i] = counts[j];
            } else if (lengths[j] + 1 == lengths[i]) {
                counts[i] += counts[j];
            }
        }
    }
}
```

### 4. **Russian Doll Envelopes** (2D LIS)
```java
// Sort by width ascending, height descending
Arrays.sort(envelopes, (a, b) -> 
    a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]
);

// Run LIS on heights
// Sorting by height desc prevents same-width envelopes from nesting
```

## Optimization Techniques

### 1. **Binary Search for LIS** - O(n log n)
Instead of checking all previous elements, maintain array of smallest tail elements for each length.

### 2. **Space Optimization for LCS** - O(min(m,n))
Only keep current and previous row:
```java
int[] prev = new int[n + 1];
int[] curr = new int[n + 1];

for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (text1.charAt(i-1) == text2.charAt(j-1)) {
            curr[j] = prev[j-1] + 1;
        } else {
            curr[j] = Math.max(prev[j], curr[j-1]);
        }
    }
    // Swap arrays
    int[] temp = prev;
    prev = curr;
    curr = temp;
}
```

### 3. **Hash Map for Property Tracking**
For arithmetic/geometric subsequences, use hash map to track different properties:
```java
Map<Integer, Integer>[] dp = new HashMap[n];
// dp[i][difference] = length of subseq ending at i with this difference
```

## Time and Space Complexity

**Common Complexities**:
- **LIS (DP)**: O(n²) time, O(n) space
- **LIS (Binary Search)**: O(n log n) time, O(n) space
- **LCS**: O(m × n) time, O(m × n) space or O(min(m,n)) optimized
- **With Properties**: O(n² × P) where P = number of properties tracked

## Common Mistakes to Avoid

1. **Confusing subsequence with substring**: Subsequence allows gaps
2. **Forgetting to track maximum**: Need to track overall maximum, not just dp[n-1]
3. **Wrong comparison**: Check if element can extend previous subsequence
4. **Not initializing dp[i] = 1**: Each element is at least length-1 subsequence
5. **Off-by-one in LCS**: Remember to use i-1 for string indexing

## Problem-Solving Checklist

- [ ] Is it subsequence (with gaps) or substring (contiguous)?
- [ ] One sequence or comparing two sequences?
- [ ] What property must subsequence satisfy?
- [ ] Define dp[i] clearly (ending at i? up to i?)
- [ ] Initialize base case (usually 1 or 0)
- [ ] For LIS: check all j < i that can extend to i
- [ ] For LCS: handle match and non-match cases
- [ ] Track overall maximum, not just last element
- [ ] Can we optimize with binary search or space optimization?

## Related Patterns

- **LCS**: Parent of string matching and edit distance problems
- **Dynamic Programming on Strings**: Broader category including sequences
- **Knapsack**: Some variants involve subsequence selection

## Practice Problems in this Package

1. **Longest Increasing Subsequence** - Classic LIS problem
2. **Longest Arithmetic Subsequence** - LIS with arithmetic property
3. **Longest String Chain** - Chain of words with one-letter difference
4. **Maximum Length Pair Chain** - Chain of pairs with ordering
5. **Maximum Length of Repeated Subarray** - Common substring (note: not subsequence)
