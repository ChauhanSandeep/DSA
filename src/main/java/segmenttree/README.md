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
    private final int[] nums;
    private final int[] segmentTree;
    private final int arraySize;
    
    public SegmentTree(int[] nums) {
        this.nums = nums.clone();
        this.arraySize = nums.length;
        this.segmentTree = new int[4 * arraySize];
        buildTree(0, 0, arraySize - 1);
    }
    
    // Build: O(n)
    private void buildTree(int segmentTreeIndex, int start, int end) {
        if (start == end) {
            segmentTree[segmentTreeIndex] = nums[start];
            return;
        }
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * segmentTreeIndex + 1;
        int rightChildIndex = 2 * segmentTreeIndex + 2;
        
        buildTree(leftChildIndex, start, mid);
        buildTree(rightChildIndex, mid + 1, end);
        segmentTree[segmentTreeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }
    
    // Query: O(log n) - Three cases: no overlap, complete overlap, partial overlap
    private int queryTree(int treeIndex, int start, int end, int leftRange, int rightRange) {
        if (rightRange < start || leftRange > end) return 0;  // No overlap
        if (leftRange <= start && end <= rightRange) return segmentTree[treeIndex];  // Complete overlap
        
        // Partial overlap
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;
        return queryTree(leftChildIndex, start, mid, leftRange, rightRange) 
             + queryTree(rightChildIndex, mid + 1, end, leftRange, rightRange);
    }
    
    // Update: O(log n)
    private void updateTree(int treeIndex, int start, int end, int updateIndex, int updateValue) {
        if (start == end) {
            segmentTree[treeIndex] = updateValue;
            return;
        }
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;
        
        if (updateIndex <= mid) {
            updateTree(leftChildIndex, start, mid, updateIndex, updateValue);
        } else {
            updateTree(rightChildIndex, mid + 1, end, updateIndex, updateValue);
        }
        segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }
}
```

## Operations for Different Queries

**Range Minimum:**
```java
segmentTree[treeIndex] = Math.min(segmentTree[leftChildIndex], segmentTree[rightChildIndex]);
// Return Integer.MAX_VALUE for no overlap
```

**Range Maximum:**
```java
segmentTree[treeIndex] = Math.max(segmentTree[leftChildIndex], segmentTree[rightChildIndex]);
// Return Integer.MIN_VALUE for no overlap
```

**Range GCD:**
```java
segmentTree[treeIndex] = gcd(segmentTree[leftChildIndex], segmentTree[rightChildIndex]);
```

## Lazy Propagation (Range Updates)

**Use when:** Need to update entire ranges efficiently (e.g., add value to all elements in [L, R])

```java
class LazySegmentTree {
    private final int[] segmentTree;
    private final int[] lazy;
    private final int arraySize;
    
    public LazySegmentTree(int[] nums) {
        this.arraySize = nums.length;
        this.segmentTree = new int[4 * arraySize];
        this.lazy = new int[4 * arraySize];
        buildTree(nums, 0, 0, arraySize - 1);
    }
    
    private void buildTree(int[] nums, int treeIndex, int start, int end) {
        if (start == end) {
            segmentTree[treeIndex] = nums[start];
            return;
        }
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;
        
        buildTree(nums, leftChildIndex, start, mid);
        buildTree(nums, rightChildIndex, mid + 1, end);
        segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }
    
    private void pushLazy(int treeIndex, int start, int end) {
        if (lazy[treeIndex] != 0) {
            segmentTree[treeIndex] += (end - start + 1) * lazy[treeIndex];
            if (start != end) {
                lazy[2 * treeIndex + 1] += lazy[treeIndex];
                lazy[2 * treeIndex + 2] += lazy[treeIndex];
            }
            lazy[treeIndex] = 0;
        }
    }
    
    // Range Update: Add 'delta' to all elements in [updateLeft, updateRight]
    private void updateRange(int treeIndex, int start, int end, 
                            int updateLeft, int updateRight, int delta) {
        pushLazy(treeIndex, start, end);
        if (start > updateRight || end < updateLeft) return;
        
        if (updateLeft <= start && end <= updateRight) {
            segmentTree[treeIndex] += (end - start + 1) * delta;
            if (start != end) {
                lazy[2 * treeIndex + 1] += delta;
                lazy[2 * treeIndex + 2] += delta;
            }
            return;
        }
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;
        
        updateRange(leftChildIndex, start, mid, updateLeft, updateRight, delta);
        updateRange(rightChildIndex, mid + 1, end, updateLeft, updateRight, delta);
        segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }
    
    private int queryRange(int treeIndex, int start, int end, int leftRange, int rightRange) {
        pushLazy(treeIndex, start, end);
        if (start > rightRange || end < leftRange) return 0;
        if (leftRange <= start && end <= rightRange) return segmentTree[treeIndex];
        
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;
        return queryRange(leftChildIndex, start, mid, leftRange, rightRange) + 
               queryRange(rightChildIndex, mid + 1, end, leftRange, rightRange);
    }
}
```

## Quick Reference

### Array Indexing
```java
int[] segmentTree = new int[4 * arraySize];    // Always use 4*arraySize
int leftChildIndex = 2 * treeIndex + 1;
int rightChildIndex = 2 * treeIndex + 2;
```

### Three Cases in Query
1. **No overlap**: `if (rightRange < start || leftRange > end) return 0;`
2. **Complete overlap**: `if (leftRange <= start && end <= rightRange) return segmentTree[treeIndex];`
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
