# Segment Tree - Quick Revision Guide

## What is Segment Tree?
A binary tree for efficient **range queries** and **point/range updates** on arrays.
- **Build**: O(n) | **Query**: O(log n) | **Update**: O(log n)
- **Space**: O(4n) ≈ O(n)

## Visual Example
Array: `[1, 3, 5, 7]`
```
        [0-3]=16
       /        \
   [0-1]=4    [2-3]=12
   /   \      /    \
 [0]=1 [1]=3 [2]=5 [3]=7
```

## When to Use?
✅ Range queries + frequent updates  
✅ Need multiple operations (sum, min, max, GCD)  
✅ Range updates with lazy propagation  
❌ Only prefix sums → Use BIT (Fenwick Tree)  
❌ No updates → Use Prefix Sum Array

## Core Template - Range Sum

```java
class SegmentTree {
    int[] tree, nums;
    int n;
    
    public SegmentTree(int[] nums) {
        this.nums = nums;
        n = nums.length;
        tree = new int[4 * n];
        build(0, 0, n - 1);
    }
    
    // Build: O(n)
    void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = nums[start];
            return;
        }
        int mid = (start + end) / 2;
        build(2*node+1, start, mid);
        build(2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    
    // Query: O(log n) - Three cases: no overlap, complete overlap, partial overlap
    int query(int node, int start, int end, int L, int R) {
        if (R < start || L > end) return 0;  // No overlap
        if (L <= start && end <= R) return tree[node];  // Complete overlap
        int mid = (start + end) / 2;  // Partial overlap
        return query(2*node+1, start, mid, L, R) + query(2*node+2, mid+1, end, L, R);
    }
    
    // Update: O(log n)
    void update(int node, int start, int end, int idx, int val) {
        if (start == end) {
            tree[node] = val;
            return;
        }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
}
```

## Operations for Different Queries

**Range Minimum:**
```java
tree[node] = Math.min(tree[2*node+1], tree[2*node+2]);
// Return Integer.MAX_VALUE for no overlap
```

**Range Maximum:**
```java
tree[node] = Math.max(tree[2*node+1], tree[2*node+2]);
// Return Integer.MIN_VALUE for no overlap
```

**Range GCD:**
```java
tree[node] = gcd(tree[2*node+1], tree[2*node+2]);
```

## Lazy Propagation (Range Updates)

**Use when:** Need to update entire ranges efficiently (e.g., add value to all elements in [L, R])

```java
class LazySegmentTree {
    int[] tree, lazy;
    int n;
    
    public LazySegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        lazy = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }
    
    void build(int[] nums, int node, int start, int end) {
        if (start == end) {
            tree[node] = nums[start];
            return;
        }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    
    void pushLazy(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += (end - start + 1) * lazy[node];
            if (start != end) {
                lazy[2*node+1] += lazy[node];
                lazy[2*node+2] += lazy[node];
            }
            lazy[node] = 0;
        }
    }
    
    // Range Update: Add 'delta' to all elements in [L, R]
    void updateRange(int node, int start, int end, int L, int R, int delta) {
        pushLazy(node, start, end);
        if (start > R || end < L) return;
        if (L <= start && end <= R) {
            tree[node] += (end - start + 1) * delta;
            if (start != end) {
                lazy[2*node+1] += delta;
                lazy[2*node+2] += delta;
            }
            return;
        }
        int mid = (start + end) / 2;
        updateRange(2*node+1, start, mid, L, R, delta);
        updateRange(2*node+2, mid+1, end, L, R, delta);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    
    int queryRange(int node, int start, int end, int L, int R) {
        pushLazy(node, start, end);
        if (start > R || end < L) return 0;
        if (L <= start && end <= R) return tree[node];
        int mid = (start + end) / 2;
        return queryRange(2*node+1, start, mid, L, R) + 
               queryRange(2*node+2, mid+1, end, L, R);
    }
}
```

## Quick Reference

### Array Indexing
```java
int[] tree = new int[4 * n];    // Always use 4*n
int leftChild = 2 * i + 1;
int rightChild = 2 * i + 2;
```

### Three Cases in Query
1. **No overlap**: `if (R < start || L > end) return 0;`
2. **Complete overlap**: `if (L <= start && end <= R) return tree[node];`
3. **Partial overlap**: Query both children and combine

### Common Interview Questions
- **Q**: When to use Segment Tree vs BIT?
- **A**: Segment Tree when need range updates, multiple operations (min/max/gcd), or lazy propagation. BIT when only prefix sums needed.

- **Q**: Why 4n space?
- **A**: Worst case for full binary tree. Safe upper bound for any array size.

- **Q**: Can we do iterative implementation?
- **A**: Yes, but recursive is cleaner and easier in interviews.

## LeetCode Problems for FAANG

### Must Practice (Top 3)
1. **#307** - Range Sum Query - Mutable (Basic template)
2. **#732** - My Calendar III (Lazy propagation)  
3. **#218** - The Skyline Problem (Advanced)

### Additional Practice
4. **#715** - Range Module
5. **#699** - Falling Squares
6. **#493** - Reverse Pairs
7. **#315** - Count of Smaller Numbers After Self

## Common Mistakes
❌ Using `2*n` space instead of `4*n`  
❌ Forgetting to push lazy updates before querying  
❌ Not updating parent after child modification  
❌ Wrong neutral values (0 for sum, INF for min, -INF for max)

## Related Files in Package
- `RangeSumQueryMutable.java` - Basic segment tree + BIT alternative
- `MyCalendarThree.java` - Lazy propagation example
- `TheSkylineProblem.java` - Advanced with coordinate compression
