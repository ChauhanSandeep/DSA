# Segment Tree - Complete Guide for Technical Interviews

## Table of Contents
- [Introduction to Segment Trees](#introduction-to-segment-trees)
- [Why Use Segment Trees?](#why-use-segment-trees)
- [Core Concepts](#core-concepts)
- [Segment Tree vs Other Data Structures](#segment-tree-vs-other-data-structures)
- [Use Cases](#use-cases)
- [Basic Segment Tree Implementation](#basic-segment-tree-implementation)
- [Range Sum Query - Mutable](#range-sum-query---mutable)
- [Lazy Propagation](#lazy-propagation)
- [Step-by-Step Learning Guide](#step-by-step-learning-guide)
- [Common Patterns and Tips](#common-patterns-and-tips)
- [LeetCode Problems](#leetcode-problems)

## Introduction to Segment Trees

A **Segment Tree** is a binary tree data structure used for storing intervals or segments. It allows for efficient range queries and updates on an array.

### Key Characteristics
- **Tree Structure**: Each node represents a segment (range) of the array
- **Leaf Nodes**: Represent individual array elements
- **Internal Nodes**: Store aggregate information about their child segments
- **Height**: O(log n) for an array of size n
- **Space**: O(4n) ≈ O(n) nodes needed

### Visual Representation

For array: `[1, 3, 5, 7, 9, 11]`

```
                    [0-5] sum=36
                   /            \
              [0-2] sum=9      [3-5] sum=27
              /      \          /         \
          [0-1]     [2]     [3-4]        [5]
          sum=4    sum=5   sum=16      sum=11
          /   \             /    \
        [0]   [1]         [3]    [4]
       sum=1 sum=3       sum=7  sum=9
```

## Why Use Segment Trees?

### Problem Scenario
Imagine you have an array and need to:
1. **Query**: Find sum/min/max of elements in range [L, R]
2. **Update**: Change value at index i
3. Do both operations **frequently**

### Naive Approach Limitations

| Operation | Naive Array | Prefix Sum | Segment Tree |
|-----------|-------------|------------|--------------|
| Range Query | O(n) | O(1) | O(log n) |
| Point Update | O(1) | O(n) | O(log n) |
| Range Update | O(n) | O(n) | O(log n) with lazy propagation |

**Segment Tree provides balanced performance for both queries and updates!**

## Core Concepts

### 1. Node Representation
Each node stores:
- **Range**: The segment [start, end] it represents
- **Value**: Aggregate value (sum, min, max, etc.)
- **Children**: Left child covers [start, mid], right child covers [mid+1, end]

### 2. Array Indexing
For a node at index `i`:
- **Left Child**: `2*i + 1`
- **Right Child**: `2*i + 2`
- **Parent**: `(i-1)/2`

### 3. Key Operations

#### Build Tree
```
Time Complexity: O(n)
Space Complexity: O(n)

Algorithm:
1. If start == end, it's a leaf node (single element)
2. Otherwise, split into two halves
3. Recursively build left and right subtrees
4. Combine results from children
```

#### Range Query
```
Time Complexity: O(log n)

Three Cases:
1. No Overlap: Return neutral value (0 for sum, INF for min)
2. Complete Overlap: Return node's value
3. Partial Overlap: Query both children and combine
```

#### Point Update
```
Time Complexity: O(log n)

Algorithm:
1. Navigate to the leaf node
2. Update the leaf value
3. Propagate changes upward to root
```

## Segment Tree vs Other Data Structures

| Feature | Segment Tree | Binary Indexed Tree (Fenwick) | Prefix Sum Array |
|---------|--------------|-------------------------------|------------------|
| Range Query | ✅ O(log n) | ✅ O(log n) | ✅ O(1) |
| Point Update | ✅ O(log n) | ✅ O(log n) | ❌ O(n) |
| Range Update | ✅ O(log n) with lazy | ❌ Complex | ❌ O(n) |
| Operations Supported | Sum, Min, Max, GCD, etc. | Sum, Prefix operations | Sum only |
| Space | O(4n) | O(n) | O(n) |
| Implementation | Medium | Easier | Very Easy |
| Flexibility | Very High | Moderate | Low |

### When to Use Segment Tree?
✅ Need both range queries and updates  
✅ Support multiple operations (min, max, sum, GCD)  
✅ Range updates with lazy propagation  
✅ Problem explicitly mentions "range queries"

### When to Use BIT (Fenwick Tree)?
✅ Only need prefix sums/queries  
✅ Simpler implementation preferred  
✅ Space efficiency is critical  
✅ No range updates needed

## Use Cases

### 1. Range Sum Queries with Updates
**Problem**: Maintain an array where you frequently:
- Update individual elements
- Query sum of elements in any range

**Example**: LeetCode #307 - Range Sum Query - Mutable

**Why Segment Tree?**
- Efficient O(log n) for both operations
- Better than O(n) for updates in prefix sum

### 2. Range Minimum/Maximum Query (RMQ)
**Problem**: Find minimum or maximum element in a given range

**Example**: Stock price analysis - "What was the lowest price between day 10 and day 20?"

**Code Pattern**:
```java
// For Range Minimum Query
segmentTree[node] = Math.min(segmentTree[leftChild], segmentTree[rightChild]);
```

### 3. Range GCD/LCM Queries
**Problem**: Find GCD or LCM of all elements in a range

**Mathematical Property**: 
- GCD(a, b, c) = GCD(GCD(a, b), c)
- This associative property makes it perfect for segment trees

### 4. Lazy Propagation for Range Updates
**Problem**: Update all elements in a range [L, R] and query ranges

**Example**: LeetCode #732 - My Calendar III

**Why Lazy Propagation?**
- Defer updates until absolutely necessary
- Convert O(n) range update to O(log n)

### 5. Counting Elements in Range
**Problem**: Count how many elements in range [L, R] satisfy a condition

**Examples**:
- Count numbers divisible by k
- Count numbers greater than x

### 6. Finding K-th Element
**Problem**: Find the k-th smallest element in a range

**Approach**: Combine with coordinate compression and segment tree

### 7. Maximum Subarray Sum in Range
**Problem**: Find maximum subarray sum in a given range

**Node Storage**:
- Max prefix sum
- Max suffix sum
- Total sum
- Max subarray sum

## Basic Segment Tree Implementation

### Complete Implementation for Range Sum Queries

```java
public class SegmentTree {
    private final int[] nums;
    private final int[] segmentTree;
    private final int arraySize;

    /**
     * Build a segment tree for range sum queries.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(4n) ≈ O(n)
     * 
     * @param nums Input array
     */
    public SegmentTree(int[] nums) {
        this.nums = nums.clone();
        this.arraySize = nums.length;
        this.segmentTree = new int[4 * arraySize]; // 4n space is safe upper bound
        buildTree(0, 0, arraySize - 1);
    }

    /**
     * Recursively build the segment tree.
     * 
     * Algorithm:
     * Step 1: If leaf node (start == end), store the element value
     * Step 2: Calculate mid point to divide segment
     * Step 3: Recursively build left subtree for [start, mid]
     * Step 4: Recursively build right subtree for [mid+1, end]
     * Step 5: Combine results from children (sum in this case)
     * 
     * @param treeIndex Current node index in segment tree
     * @param start Start of current segment
     * @param end End of current segment
     */
    private void buildTree(int treeIndex, int start, int end) {
        // Step 1: Base case - leaf node
        if (start == end) {
            segmentTree[treeIndex] = nums[start];
            return;
        }

        // Step 2: Calculate mid point
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;

        // Step 3 & 4: Build left and right subtrees
        buildTree(leftChildIndex, start, mid);
        buildTree(rightChildIndex, mid + 1, end);

        // Step 5: Combine results (sum for this example)
        segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }

    /**
     * Query the sum of elements in range [queryLeft, queryRight].
     * 
     * Algorithm:
     * Step 1: Check for no overlap - return 0 (neutral value)
     * Step 2: Check for complete overlap - return current node's value
     * Step 3: Partial overlap - query both children and combine
     * 
     * Time Complexity: O(log n)
     * 
     * @param queryLeft Left boundary of query range
     * @param queryRight Right boundary of query range
     * @return Sum of elements in the range
     */
    public int rangeQuery(int queryLeft, int queryRight) {
        return rangeQuery(0, 0, arraySize - 1, queryLeft, queryRight);
    }

    private int rangeQuery(int treeIndex, int start, int end, int queryLeft, int queryRight) {
        // Step 1: No overlap
        if (queryRight < start || queryLeft > end) {
            return 0; // Neutral value for sum
        }

        // Step 2: Complete overlap
        if (queryLeft <= start && end <= queryRight) {
            return segmentTree[treeIndex];
        }

        // Step 3: Partial overlap - query both children
        int mid = start + (end - start) / 2;
        int leftSum = rangeQuery(2 * treeIndex + 1, start, mid, queryLeft, queryRight);
        int rightSum = rangeQuery(2 * treeIndex + 2, mid + 1, end, queryLeft, queryRight);

        return leftSum + rightSum;
    }

    /**
     * Update value at index to newValue.
     * 
     * Algorithm:
     * Step 1: Navigate to leaf node containing the index
     * Step 2: Update the leaf node
     * Step 3: Propagate changes upward to root
     * 
     * Time Complexity: O(log n)
     * 
     * @param index Index to update
     * @param newValue New value to set
     */
    public void update(int index, int newValue) {
        updateTree(0, 0, arraySize - 1, index, newValue);
        nums[index] = newValue;
    }

    private void updateTree(int treeIndex, int start, int end, int index, int newValue) {
        // Step 1: Reached leaf node
        if (start == end) {
            segmentTree[treeIndex] = newValue;
            return;
        }

        // Step 2: Navigate to appropriate child
        int mid = start + (end - start) / 2;
        int leftChildIndex = 2 * treeIndex + 1;
        int rightChildIndex = 2 * treeIndex + 2;

        if (index <= mid) {
            updateTree(leftChildIndex, start, mid, index, newValue);
        } else {
            updateTree(rightChildIndex, mid + 1, end, index, newValue);
        }

        // Step 3: Update current node from children
        segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }
}
```

### Usage Example

```java
// Create segment tree from array
int[] nums = {1, 3, 5, 7, 9, 11};
SegmentTree st = new SegmentTree(nums);

// Query sum of range [1, 4] -> 3 + 5 + 7 + 9 = 24
int sum = st.rangeQuery(1, 4);
System.out.println("Sum of range [1, 4]: " + sum);

// Update index 2 to value 10 (was 5)
st.update(2, 10);

// Query again [1, 4] -> 3 + 10 + 7 + 9 = 29
sum = st.rangeQuery(1, 4);
System.out.println("Sum after update: " + sum);
```

## Range Sum Query - Mutable

This is a classic segment tree problem similar to the implementation in `RangeSumQueryMutable.java`.

### Problem Statement
Given an integer array `nums`, handle multiple queries of:
1. **Update**: Change value at index i
2. **SumRange**: Calculate sum from index left to right (inclusive)

### Approach 1: Using Segment Tree (from existing code)

```java
public class NumArray {
    private final int[] nums;
    private final int[] segmentTree;
    private final int arraySize;

    public NumArray(int[] nums) {
        this.nums = nums.clone();
        this.arraySize = nums.length;
        this.segmentTree = new int[4 * arraySize];
        buildTree(0, 0, arraySize - 1);
    }

    private void buildTree(int treeIndex, int start, int end) {
        if (start == end) {
            segmentTree[treeIndex] = nums[start];
            return;
        }

        int mid = start + (end - start) / 2;
        int leftChild = 2 * treeIndex + 1;
        int rightChild = 2 * treeIndex + 2;

        buildTree(leftChild, start, mid);
        buildTree(rightChild, mid + 1, end);

        segmentTree[treeIndex] = segmentTree[leftChild] + segmentTree[rightChild];
    }

    public void update(int index, int val) {
        updateTree(0, 0, arraySize - 1, index, val);
        nums[index] = val;
    }

    private void updateTree(int treeIndex, int start, int end, int index, int val) {
        if (start == end) {
            segmentTree[treeIndex] = val;
            return;
        }

        int mid = start + (end - start) / 2;
        int leftChild = 2 * treeIndex + 1;
        int rightChild = 2 * treeIndex + 2;

        if (index <= mid) {
            updateTree(leftChild, start, mid, index, val);
        } else {
            updateTree(rightChild, mid + 1, end, index, val);
        }

        segmentTree[treeIndex] = segmentTree[leftChild] + segmentTree[rightChild];
    }

    public int sumRange(int left, int right) {
        return queryTree(0, 0, arraySize - 1, left, right);
    }

    private int queryTree(int treeIndex, int start, int end, int left, int right) {
        if (right < start || left > end) {
            return 0;
        }

        if (left <= start && end <= right) {
            return segmentTree[treeIndex];
        }

        int mid = start + (end - start) / 2;
        int leftSum = queryTree(2 * treeIndex + 1, start, mid, left, right);
        int rightSum = queryTree(2 * treeIndex + 2, mid + 1, end, left, right);

        return leftSum + rightSum;
    }
}
```

### Complexity Analysis
- **Build**: O(n) - Visit each node once
- **Update**: O(log n) - Height of tree
- **Query**: O(log n) - At most visit 4 nodes per level

## Lazy Propagation

### What is Lazy Propagation?

**Problem**: Regular segment tree does range updates in O(n log n)
- For range [L, R], we update each element individually
- Each update is O(log n), so total is O(n log n)

**Solution**: Lazy Propagation
- **Defer** updates until absolutely necessary
- Mark nodes as "lazy" with pending updates
- When visiting a node, apply pending updates first
- Reduces range update to O(log n)

### When to Use Lazy Propagation?

✅ **Range updates** are needed (update all elements in [L, R])  
✅ Updates happen more frequently than queries  
✅ Problem involves "add value to range" or "set range to value"  
❌ Only point updates needed (regular segment tree is enough)

### Lazy Propagation Implementation

```java
public class LazySegmentTree {
    private final int[] segmentTree;
    private final int[] lazy; // Stores pending updates
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
        buildTree(nums, 2 * treeIndex + 1, start, mid);
        buildTree(nums, 2 * treeIndex + 2, mid + 1, end);

        segmentTree[treeIndex] = segmentTree[2 * treeIndex + 1] + segmentTree[2 * treeIndex + 2];
    }

    /**
     * Apply pending lazy updates to current node.
     * 
     * Algorithm:
     * Step 1: If no pending update (lazy[node] == 0), return
     * Step 2: Update current node value
     * Step 3: If not a leaf, propagate lazy value to children
     * Step 4: Clear lazy value for current node
     */
    private void pushLazy(int treeIndex, int start, int end) {
        // Step 1: Check if there's a pending update
        if (lazy[treeIndex] != 0) {
            // Step 2: Apply the update to current node
            segmentTree[treeIndex] += (end - start + 1) * lazy[treeIndex];

            // Step 3: If not a leaf, propagate to children
            if (start != end) {
                lazy[2 * treeIndex + 1] += lazy[treeIndex];
                lazy[2 * treeIndex + 2] += lazy[treeIndex];
            }

            // Step 4: Clear lazy value
            lazy[treeIndex] = 0;
        }
    }

    /**
     * Update range [updateLeft, updateRight] by adding delta.
     * 
     * Algorithm:
     * Step 1: Apply any pending lazy updates
     * Step 2: Check for no overlap - return
     * Step 3: Check for complete overlap - update and mark children as lazy
     * Step 4: Partial overlap - recursively update children
     * 
     * Time Complexity: O(log n)
     */
    public void updateRange(int updateLeft, int updateRight, int delta) {
        updateRange(0, 0, arraySize - 1, updateLeft, updateRight, delta);
    }

    private void updateRange(int treeIndex, int start, int end, 
                            int updateLeft, int updateRight, int delta) {
        // Step 1: Apply pending updates
        pushLazy(treeIndex, start, end);

        // Step 2: No overlap
        if (start > updateRight || end < updateLeft) {
            return;
        }

        // Step 3: Complete overlap
        if (updateLeft <= start && end <= updateRight) {
            // Update current node
            segmentTree[treeIndex] += (end - start + 1) * delta;

            // Mark children as lazy (if not leaf)
            if (start != end) {
                lazy[2 * treeIndex + 1] += delta;
                lazy[2 * treeIndex + 2] += delta;
            }
            return;
        }

        // Step 4: Partial overlap - update both children
        int mid = start + (end - start) / 2;
        updateRange(2 * treeIndex + 1, start, mid, updateLeft, updateRight, delta);
        updateRange(2 * treeIndex + 2, mid + 1, end, updateLeft, updateRight, delta);

        // Update current node from children
        segmentTree[treeIndex] = segmentTree[2 * treeIndex + 1] + segmentTree[2 * treeIndex + 2];
    }

    /**
     * Query sum in range [queryLeft, queryRight].
     */
    public int rangeQuery(int queryLeft, int queryRight) {
        return rangeQuery(0, 0, arraySize - 1, queryLeft, queryRight);
    }

    private int rangeQuery(int treeIndex, int start, int end, 
                          int queryLeft, int queryRight) {
        // Always apply pending updates first
        pushLazy(treeIndex, start, end);

        // No overlap
        if (start > queryRight || end < queryLeft) {
            return 0;
        }

        // Complete overlap
        if (queryLeft <= start && end <= queryRight) {
            return segmentTree[treeIndex];
        }

        // Partial overlap
        int mid = start + (end - start) / 2;
        int leftSum = rangeQuery(2 * treeIndex + 1, start, mid, queryLeft, queryRight);
        int rightSum = rangeQuery(2 * treeIndex + 2, mid + 1, end, queryLeft, queryRight);

        return leftSum + rightSum;
    }
}
```

### Lazy Propagation Example

```java
int[] nums = {1, 2, 3, 4, 5};
LazySegmentTree lst = new LazySegmentTree(nums);

// Add 10 to all elements in range [1, 3]
lst.updateRange(1, 3, 10);
// Array becomes: [1, 12, 13, 14, 5]

// Query sum of range [0, 4] -> 1 + 12 + 13 + 14 + 5 = 45
int sum = lst.rangeQuery(0, 4);
System.out.println("Sum: " + sum);
```

## Step-by-Step Learning Guide

### Phase 1: Understanding the Basics (Day 1-2)

#### Step 1: Understand the Problem
- Why do we need segment trees?
- What are the limitations of naive approaches?
- When should you use segment tree vs BIT vs other structures?

#### Step 2: Learn Core Concepts
- Tree representation and indexing
- Parent-child relationships (2*i+1, 2*i+2)
- Leaf nodes vs internal nodes
- Time and space complexity

#### Step 3: Visualize
- Draw segment tree for small arrays [1, 3, 5, 7]
- Trace through build operation step by step
- Understand how ranges are represented

### Phase 2: Implementation (Day 3-5)

#### Step 4: Implement Basic Operations
Start with Range Sum Query:

**4.1 Build Tree**
```java
// Start here - understand recursion and base case
private void buildTree(int treeIndex, int start, int end) {
    if (start == end) {
        segmentTree[treeIndex] = nums[start];
        return;
    }
    int mid = start + (end - start) / 2;
    buildTree(2 * treeIndex + 1, start, mid);
    buildTree(2 * treeIndex + 2, mid + 1, end);
    segmentTree[treeIndex] = segmentTree[2*treeIndex+1] + segmentTree[2*treeIndex+2];
}
```

**4.2 Range Query**
```java
// Master the three cases: no overlap, complete, partial
private int query(int treeIndex, int start, int end, int left, int right) {
    if (right < start || left > end) return 0; // No overlap
    if (left <= start && end <= right) return segmentTree[treeIndex]; // Complete
    // Partial overlap
    int mid = start + (end - start) / 2;
    return query(2*treeIndex+1, start, mid, left, right) +
           query(2*treeIndex+2, mid+1, end, left, right);
}
```

**4.3 Point Update**
```java
// Navigate to leaf and propagate up
private void update(int treeIndex, int start, int end, int index, int val) {
    if (start == end) {
        segmentTree[treeIndex] = val;
        return;
    }
    int mid = start + (end - start) / 2;
    if (index <= mid) {
        update(2*treeIndex+1, start, mid, index, val);
    } else {
        update(2*treeIndex+2, mid+1, end, index, val);
    }
    segmentTree[treeIndex] = segmentTree[2*treeIndex+1] + segmentTree[2*treeIndex+2];
}
```

#### Step 5: Practice Different Operations
Modify your code to support:
- Range minimum query (change sum to Math.min)
- Range maximum query (change sum to Math.max)
- Range GCD query

### Phase 3: Advanced Concepts (Day 6-8)

#### Step 6: Learn Lazy Propagation
- Understand why it's needed
- Implement range updates
- Practice problems requiring lazy propagation

#### Step 7: Solve Standard Problems
Practice in this order:
1. LeetCode #307 - Range Sum Query - Mutable (Basic)
2. LeetCode #732 - My Calendar III (Lazy propagation)
3. LeetCode #218 - The Skyline Problem (Advanced)

### Phase 4: Mastery (Day 9-10)

#### Step 8: Optimize and Refine
- Iterative vs Recursive implementations
- Space optimization techniques
- Coordinate compression for large ranges

#### Step 9: Solve Mixed Problems
- Combine with other algorithms (binary search, DP)
- Handle edge cases (empty arrays, single elements)
- Time-space tradeoffs

### Quick Revision Checklist

Before an interview, ensure you can:
- [ ] Explain why segment tree is needed (O(log n) for both query and update)
- [ ] Draw segment tree for array [1, 3, 5, 7]
- [ ] Write buildTree() from memory
- [ ] Explain three cases in range query (no overlap, complete, partial)
- [ ] Implement point update with upward propagation
- [ ] Explain when to use lazy propagation
- [ ] Compare segment tree vs BIT vs prefix sum array
- [ ] Solve LeetCode #307 in under 20 minutes

## Common Patterns and Tips

### Pattern 1: Identify Segment Tree Problems

**Keywords to look for:**
- "Range queries"
- "Update and query"
- "Interval queries"
- "Find sum/min/max in range"
- "Modify elements frequently"

**Example Problem Statements:**
- "Given an array, support operations: update(index, val) and sumRange(left, right)"
- "Find minimum element in range [L, R] with frequent updates"
- "Count elements in range satisfying a property"

### Pattern 2: Choose the Right Operation

**For Range Sum:**
```java
segmentTree[node] = segmentTree[leftChild] + segmentTree[rightChild];
```

**For Range Minimum:**
```java
segmentTree[node] = Math.min(segmentTree[leftChild], segmentTree[rightChild]);
```

**For Range Maximum:**
```java
segmentTree[node] = Math.max(segmentTree[leftChild], segmentTree[rightChild]);
```

**For Range GCD:**
```java
segmentTree[node] = gcd(segmentTree[leftChild], segmentTree[rightChild]);
```

### Pattern 3: Handle Edge Cases

**Always check:**
```java
// Empty array
if (nums == null || nums.length == 0) return 0;

// Invalid range
if (left < 0 || right >= n || left > right) return 0;

// Single element
if (start == end) { /* handle leaf node */ }
```

### Pattern 4: Lazy Propagation Decision Tree

```
Do you need range updates? 
├─ NO → Use regular segment tree
└─ YES → Do updates happen frequently?
    ├─ NO → Regular segment tree might be OK
    └─ YES → Use lazy propagation
```

### Common Mistakes to Avoid

❌ **Mistake 1**: Forgetting to apply lazy updates before querying
```java
// WRONG
private int query(...) {
    if (complete overlap) return segmentTree[node];
}

// CORRECT
private int query(...) {
    pushLazy(node, start, end); // Always push first!
    if (complete overlap) return segmentTree[node];
}
```

❌ **Mistake 2**: Incorrect array size
```java
// WRONG
int[] tree = new int[2 * n];

// CORRECT
int[] tree = new int[4 * n]; // Always use 4*n for safety
```

❌ **Mistake 3**: Not updating parent after child modification
```java
// WRONG
updateTree(leftChild, ...);
updateTree(rightChild, ...);
// Missing: segmentTree[node] = combine(left, right);

// CORRECT
updateTree(leftChild, ...);
updateTree(rightChild, ...);
segmentTree[node] = segmentTree[leftChild] + segmentTree[rightChild];
```

### Tips for Interview Success

1. **Start Simple**: Begin with range sum query before attempting complex operations
2. **Explain First**: Before coding, explain your approach and why segment tree is appropriate
3. **Use Templates**: Memorize basic build/query/update templates
4. **Test Edge Cases**: Single element, empty array, complete overlap
5. **Time/Space Analysis**: Always mention O(n) build, O(log n) query/update, O(n) space

## LeetCode Problems

### Beginner Level
1. **[#307 - Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/)** ⭐⭐⭐
   - **Topic**: Basic segment tree
   - **Key Learning**: Build, query, point update
   - **Similar Code**: See `RangeSumQueryMutable.java` in this package

### Intermediate Level
2. **[#732 - My Calendar III](https://leetcode.com/problems/my-calendar-iii/)** ⭐⭐⭐⭐
   - **Topic**: Lazy propagation, coordinate compression
   - **Key Learning**: Range updates, maximum queries
   - **Similar Code**: See `MyCalendarThree.java` in this package

3. **[#715 - Range Module](https://leetcode.com/problems/range-module/)**
   - **Topic**: Range updates with lazy propagation
   - **Key Learning**: Boolean range operations

4. **[#699 - Falling Squares](https://leetcode.com/problems/falling-squares/)**
   - **Topic**: Coordinate compression + segment tree
   - **Key Learning**: Dynamic coordinate mapping

### Advanced Level
5. **[#218 - The Skyline Problem](https://leetcode.com/problems/the-skyline-problem/)** ⭐⭐⭐⭐⭐
   - **Topic**: Segment tree with coordinate compression
   - **Key Learning**: Complex range maximum queries
   - **Similar Code**: See `TheSkylineProblem.java` in this package

6. **[#493 - Reverse Pairs](https://leetcode.com/problems/reverse-pairs/)**
   - **Topic**: Segment tree for counting inversions
   - **Key Learning**: Order statistics with segment tree

7. **[#315 - Count of Smaller Numbers After Self](https://leetcode.com/problems/count-of-smaller-numbers-after-self/)**
   - **Topic**: Segment tree with coordinate compression
   - **Key Learning**: Dynamic counting queries

### Practice Strategy

**Week 1**: Basics
- Solve #307 multiple times until you can do it without reference
- Implement different operations (min, max, gcd)

**Week 2**: Lazy Propagation
- Study and implement lazy propagation template
- Solve #732 and #715

**Week 3**: Advanced
- Combine segment tree with other concepts
- Solve #218, #493, #315

---

## Summary

### Key Takeaways

1. **When to Use**: Frequent queries + updates on array ranges
2. **Time Complexity**: O(log n) for query and update
3. **Space Complexity**: O(n) with 4n nodes
4. **Core Operations**: Build (O(n)), Query (O(log n)), Update (O(log n))
5. **Advanced**: Lazy propagation for range updates

### Quick Reference Card

```java
// Array size
int[] segmentTree = new int[4 * n];

// Child indices
int leftChild = 2 * i + 1;
int rightChild = 2 * i + 2;

// Build tree
buildTree(0, 0, n-1);

// Query range [L, R]
query(0, 0, n-1, L, R);

// Update index i to val
update(0, 0, n-1, i, val);

// Three cases in query:
// 1. No overlap: return neutral
// 2. Complete overlap: return node value
// 3. Partial overlap: query both children
```

### Related Files in This Package
- `RangeSumQueryMutable.java` - Basic segment tree with BIT alternative
- `MyCalendarThree.java` - Lazy propagation example
- `TheSkylineProblem.java` - Advanced segment tree with coordinate compression

---

**Happy Learning! Master segment trees and ace your coding interviews! 🚀**
