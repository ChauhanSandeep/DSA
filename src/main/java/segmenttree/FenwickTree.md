# Fenwick Tree (Binary Indexed Tree) - Quick Revision Guide

## What is Fenwick Tree?
A space-efficient data structure for **prefix sum queries** and **point updates** on arrays.
- **Build**: O(n log n) | **Query**: O(log n) | **Update**: O(log n)
- **Space**: O(n) - More efficient than Segment Tree (4n)

Also known as **Binary Indexed Tree (BIT)**.

## Visual Example
Array: `[3, 2, -1, 6, 5]` with 1-based indexing

```
BIT stores partial sums based on binary representation:
Index:  1   2   3   4   5
Value:  3   5   -1  10  5
Range: [1] [1-2] [3] [1-4] [5]
```

**Key Insight**: Each index stores sum of a range determined by its lowest set bit.

## When to Use?
✅ Only need prefix sums (cumulative sums from start)  
✅ Point updates (single element changes)  
✅ Simpler implementation than Segment Tree  
✅ Space efficiency critical (n vs 4n)  
❌ Need range updates → Use Segment Tree with Lazy Propagation  
❌ Need range min/max/gcd → Use Segment Tree

## Core Template

```java
class FenwickTree {
    int[] tree;
    int n;
    
    // 1-indexed: tree[0] unused, actual data at tree[1] to tree[n]
    public FenwickTree(int n) {
        this.n = n;
        tree = new int[n + 1];
    }
    
    // Build from array: O(n log n)
    public FenwickTree(int[] nums) {
        n = nums.length;
        tree = new int[n + 1];
        for (int i = 0; i < n; i++) {
            add(i + 1, nums[i]);  // Convert 0-indexed to 1-indexed
        }
    }
    
    // Add delta to index (1-indexed): O(log n)
    void add(int idx, int delta) {
        while (idx <= n) {
            tree[idx] += delta;
            idx += lowBit(idx);  // Move to next responsible index
        }
    }
    
    // Get prefix sum [1, idx]: O(log n)
    int prefixSum(int idx) {
        int sum = 0;
        while (idx > 0) {
            sum += tree[idx];
            idx -= lowBit(idx);  // Move to parent
        }
        return sum;
    }
    
    // Range sum [L, R] (1-indexed): O(log n)
    int rangeSum(int L, int R) {
        return prefixSum(R) - prefixSum(L - 1);
    }
    
    // Point update: set index to value (1-indexed)
    void update(int idx, int val, int oldVal) {
        add(idx, val - oldVal);
    }
    
    // Get lowest set bit (also called LSB)
    int lowBit(int x) {
        return x & (-x);
    }
}
```

## How It Works

### The Low Bit Magic: `x & (-x)`

**What it does**: Isolates the rightmost set bit

```
x = 6 (binary: 110)
-x (two's complement): ...11111010
x & (-x) = 010 (binary) = 2

Index 6 is responsible for range [5, 6] (length = 2)
```

### Update Operation

```java
add(idx, delta):
    while (idx <= n):
        tree[idx] += delta
        idx += lowBit(idx)  // Jump to next index that needs update
```

**Example**: Update index 3
- Update tree[3] (covers [3])
- Jump to 3 + lowBit(3) = 3 + 1 = 4
- Update tree[4] (covers [1-4])
- Jump to 4 + lowBit(4) = 4 + 4 = 8
- Continue until idx > n

### Query Operation

```java
prefixSum(idx):
    sum = 0
    while (idx > 0):
        sum += tree[idx]
        idx -= lowBit(idx)  // Jump to parent
    return sum
```

**Example**: Query prefix sum up to index 7
- Add tree[7] (covers [7])
- Jump to 7 - lowBit(7) = 7 - 1 = 6
- Add tree[6] (covers [5-6])
- Jump to 6 - lowBit(6) = 6 - 2 = 4
- Add tree[4] (covers [1-4])
- idx becomes 0, stop

## Complete Example with 0-indexed Input

```java
// Problem: Range Sum Query - Mutable (LeetCode #307)
class NumArray {
    FenwickTree bit;
    int[] nums;
    
    public NumArray(int[] nums) {
        this.nums = nums.clone();
        bit = new FenwickTree(nums.length);
        for (int i = 0; i < nums.length; i++) {
            bit.add(i + 1, nums[i]);  // Convert to 1-indexed
        }
    }
    
    public void update(int index, int val) {
        int delta = val - nums[index];
        bit.add(index + 1, delta);  // Convert to 1-indexed
        nums[index] = val;
    }
    
    public int sumRange(int left, int right) {
        return bit.prefixSum(right + 1) - bit.prefixSum(left);
    }
}
```

## 2D Fenwick Tree

For 2D range sum queries (e.g., LeetCode #304 extension):

```java
class FenwickTree2D {
    int[][] tree;
    int rows, cols;
    
    public FenwickTree2D(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tree = new int[rows + 1][cols + 1];
    }
    
    void add(int r, int c, int delta) {
        for (int i = r; i <= rows; i += lowBit(i)) {
            for (int j = c; j <= cols; j += lowBit(j)) {
                tree[i][j] += delta;
            }
        }
    }
    
    int prefixSum(int r, int c) {
        int sum = 0;
        for (int i = r; i > 0; i -= lowBit(i)) {
            for (int j = c; j > 0; j -= lowBit(j)) {
                sum += tree[i][j];
            }
        }
        return sum;
    }
    
    int rangeSum(int r1, int c1, int r2, int c2) {
        return prefixSum(r2, c2) - prefixSum(r1 - 1, c2) 
             - prefixSum(r2, c1 - 1) + prefixSum(r1 - 1, c1 - 1);
    }
    
    int lowBit(int x) { return x & (-x); }
}
```

## Quick Reference

### Key Formulas
```java
// Low bit (rightmost set bit)
lowBit(x) = x & (-x)

// Range sum [L, R]
rangeSum(L, R) = prefixSum(R) - prefixSum(L - 1)

// Index conversion (0-indexed to 1-indexed)
bitIndex = arrayIndex + 1
```

### Common Operations
```java
// Point update: change nums[i] to val
add(i + 1, val - nums[i])

// Range query: sum from left to right
prefixSum(right + 1) - prefixSum(left)

// Point query: get value at index i
prefixSum(i + 1) - prefixSum(i)
```

### Why 1-indexed?
- Bit manipulation `x & (-x)` returns 0 for x=0
- 1-indexed avoids special case handling
- Tree naturally starts from index 1

## Fenwick Tree vs Segment Tree

| Feature | Fenwick Tree (BIT) | Segment Tree |
|---------|-------------------|--------------|
| Space | O(n) | O(4n) |
| Implementation | Simpler (20 lines) | More complex (60+ lines) |
| Operations | Prefix sums only | Sum, Min, Max, GCD, etc. |
| Range Updates | Complex | Easy with lazy propagation |
| Build Time | O(n log n) | O(n) |
| Query/Update | O(log n) | O(log n) |
| **Use When** | Only prefix queries | Multiple operations or range updates |

## Common Interview Questions

**Q**: Why use Fenwick Tree over Segment Tree?
- **A**: Simpler code, less space (n vs 4n), easier to implement in interviews when only prefix sums needed.

**Q**: Can Fenwick Tree handle range updates?
- **A**: Yes, but complex. Use difference array technique: maintain BIT of differences, requires two BITs for range update + range query.

**Q**: Why is it called Binary Indexed Tree?
- **A**: Uses binary representation of indices to determine parent-child relationships via bit manipulation.

**Q**: What's the time complexity to build from scratch?
- **A**: O(n log n) if using repeated add(). Can be optimized to O(n) with direct construction.

## LeetCode Problems for FAANG

### Must Practice (Top 3)
1. **#307** - Range Sum Query - Mutable (Basic BIT)
2. **#315** - Count of Smaller Numbers After Self (BIT + coordinate compression)
3. **#327** - Count of Range Sum (BIT with prefix sums)

### Additional Practice
4. **#493** - Reverse Pairs (Modified BIT)
5. **#1649** - Create Sorted Array through Instructions
6. **#308** - Range Sum Query 2D - Mutable (2D BIT)

### Advanced
7. **#1505** - Minimum Possible Integer After at Most K Adjacent Swaps (BIT for inversions)

## Common Mistakes

❌ Forgetting to use 1-indexed (accessing tree[0])  
❌ Not converting 0-indexed input to 1-indexed  
❌ Using `x & x - 1` instead of `x & (-x)`  
❌ Wrong range sum formula: should be `prefixSum(R) - prefixSum(L-1)`  
❌ Initializing tree size as `n` instead of `n+1`

## Implementation Tips

### Fast Build (O(n) instead of O(n log n))
```java
void buildFast(int[] nums) {
    for (int i = 1; i <= n; i++) {
        tree[i] += nums[i - 1];
        int j = i + lowBit(i);
        if (j <= n) tree[j] += tree[i];
    }
}
```

### Binary Search on BIT
Find index where prefix sum >= target:
```java
int findIndex(int target) {
    int idx = 0, mask = Integer.highestOneBit(n);
    while (mask > 0) {
        int next = idx + mask;
        if (next <= n && tree[next] < target) {
            idx = next;
            target -= tree[next];
        }
        mask >>= 1;
    }
    return idx + 1;
}
```

## Related Files in Package
- `RangeSumQueryMutable.java` - BIT implementation example (see BinaryIndexedTree inner class)

## When to Use What?

```
Need range queries and updates?
├─ Only prefix sums? → Fenwick Tree (BIT)
└─ Multiple operations (min/max/gcd)? → Segment Tree
    ├─ No range updates? → Basic Segment Tree
    └─ Range updates? → Segment Tree with Lazy Propagation
```

---

**Quick Interview Prep**: Master the basic template, understand `lowBit(x) = x & (-x)`, and practice #307. Remember 1-indexing! 🚀
