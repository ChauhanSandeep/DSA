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
class BinaryIndexedTree {
    private final int[] tree;
    private final int size;
    
    // 1-indexed: tree[0] unused, actual data at tree[1] to tree[size]
    public BinaryIndexedTree(int size) {
        this.size = size;
        this.tree = new int[size + 1];
    }
    
    // Add delta to index (1-indexed): O(log n)
    public void add(int index, int delta) {
        while (index <= size) {
            tree[index] += delta;
            index += getLowBit(index);  // Move to next responsible index
        }
    }
    
    // Get prefix sum [1, index]: O(log n)
    public int getPrefix(int index) {
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= getLowBit(index);  // Move to parent
        }
        return sum;
    }
    
    // Get lowest set bit (also called LSB)
    private int getLowBit(int index) {
        return index & (-index);
    }
}

// Usage with 0-indexed array
class NumArray {
    private final int[] originalNums;
    private final BinaryIndexedTree fenwickTree;
    
    public NumArray(int[] nums) {
        this.originalNums = nums.clone();
        this.fenwickTree = new BinaryIndexedTree(nums.length);
        for (int i = 0; i < nums.length; i++) {
            fenwickTree.add(i + 1, nums[i]);  // Convert to 1-indexed
        }
    }
    
    public void update(int index, int val) {
        int delta = val - originalNums[index];
        fenwickTree.add(index + 1, delta);  // Convert to 1-indexed
        originalNums[index] = val;
    }
    
    public int sumRange(int left, int right) {
        return fenwickTree.getPrefix(right + 1) - fenwickTree.getPrefix(left);
    }
}
```

## How It Works

### The Low Bit Magic: `index & (-index)`

**What it does**: Isolates the rightmost set bit

```
index = 6 (binary: 110)
-index (two's complement): ...11111010
index & (-index) = 010 (binary) = 2

Index 6 is responsible for range [5, 6] (length = 2)
```

### Update Operation

```java
add(index, delta):
    while (index <= size):
        tree[index] += delta
        index += getLowBit(index)  // Jump to next index that needs update
```

**Example**: Update index 3
- Update tree[3] (covers [3])
- Jump to 3 + getLowBit(3) = 3 + 1 = 4
- Update tree[4] (covers [1-4])
- Jump to 4 + getLowBit(4) = 4 + 4 = 8
- Continue until index > size

### Query Operation

```java
getPrefix(index):
    sum = 0
    while (index > 0):
        sum += tree[index]
        index -= getLowBit(index)  // Jump to parent
    return sum
```

**Example**: Query prefix sum up to index 7
- Add tree[7] (covers [7])
- Jump to 7 - getLowBit(7) = 7 - 1 = 6
- Add tree[6] (covers [5-6])
- Jump to 6 - getLowBit(6) = 6 - 2 = 4
- Add tree[4] (covers [1-4])
- index becomes 0, stop

## Complete Example with 0-indexed Input

See the full implementation in `RangeSumQueryMutable.java` which uses this exact pattern:

```java
// Problem: Range Sum Query - Mutable (LeetCode #307)
public class RangeSumQueryMutable {
    private final int[] originalNums;
    private final BinaryIndexedTree fenwickTree;
    
    public RangeSumQueryMutable(int[] nums) {
        this.originalNums = nums.clone();
        this.fenwickTree = new BinaryIndexedTree(nums.length);
        
        // Build the fenwick tree by adding each element
        for (int i = 0; i < nums.length; i++) {
            fenwickTree.add(i + 1, nums[i]);  // Convert to 1-indexed
        }
    }
    
    public void update(int index, int val) {
        int delta = val - originalNums[index];
        fenwickTree.add(index + 1, delta);  // Convert to 1-indexed
        originalNums[index] = val;
    }
    
    public int sumRange(int left, int right) {
        return fenwickTree.getPrefix(right + 1) - fenwickTree.getPrefix(left);
    }
}
```

## 2D Fenwick Tree

For 2D range sum queries (e.g., LeetCode #304 extension):

```java
class BinaryIndexedTree2D {
    private final int[][] tree;
    private final int rows;
    private final int cols;
    
    public BinaryIndexedTree2D(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tree = new int[rows + 1][cols + 1];
    }
    
    public void add(int row, int col, int delta) {
        for (int i = row; i <= rows; i += getLowBit(i)) {
            for (int j = col; j <= cols; j += getLowBit(j)) {
                tree[i][j] += delta;
            }
        }
    }
    
    public int getPrefix(int row, int col) {
        int sum = 0;
        for (int i = row; i > 0; i -= getLowBit(i)) {
            for (int j = col; j > 0; j -= getLowBit(j)) {
                sum += tree[i][j];
            }
        }
        return sum;
    }
    
    public int getRangeSum(int row1, int col1, int row2, int col2) {
        return getPrefix(row2, col2) - getPrefix(row1 - 1, col2) 
             - getPrefix(row2, col1 - 1) + getPrefix(row1 - 1, col1 - 1);
    }
    
    private int getLowBit(int index) { 
        return index & (-index); 
    }
}
```

## Quick Reference

### Key Formulas
```java
// Low bit (rightmost set bit)
getLowBit(index) = index & (-index)

// Range sum [left, right]
rangeSum = getPrefix(right) - getPrefix(left - 1)

// Index conversion (0-indexed to 1-indexed)
fenwickIndex = arrayIndex + 1
```

### Common Operations
```java
// Point update: change originalNums[index] to val
int delta = val - originalNums[index];
fenwickTree.add(index + 1, delta);

// Range query: sum from left to right
fenwickTree.getPrefix(right + 1) - fenwickTree.getPrefix(left)

// Point query: get value at index
fenwickTree.getPrefix(index + 1) - fenwickTree.getPrefix(index)
```

### Why 1-indexed?
- Bit manipulation `index & (-index)` returns 0 for index=0
- 1-indexed avoids special case handling
- Tree naturally starts from index 1
- Easier bit manipulation operations

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

**Q**: Can Binary Indexed Tree handle range updates?
- **A**: Yes, but complex. Use difference array technique: maintain BIT of differences, requires two BITs for range update + range query.

**Q**: Why is it called Binary Indexed Tree?
- **A**: Uses binary representation of indices to determine parent-child relationships via bit manipulation with `getLowBit()`.

**Q**: What's the time complexity to build from scratch?
- **A**: O(n log n) if using repeated `add()`. Can be optimized to O(n) with direct construction.

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
❌ Not converting 0-indexed input to 1-indexed (should use `index + 1`)  
❌ Using `index & index - 1` instead of `index & (-index)`  
❌ Wrong range sum formula: should be `getPrefix(right) - getPrefix(left - 1)`  
❌ Initializing tree size as `size` instead of `size + 1`

## Implementation Tips

### Fast Build (O(n) instead of O(n log n))
```java
void buildFast(int[] nums) {
    for (int i = 1; i <= size; i++) {
        tree[i] += nums[i - 1];
        int j = i + getLowBit(i);
        if (j <= size) tree[j] += tree[i];
    }
}
```

### Binary Search on BIT
Find index where prefix sum >= target:
```java
int findIndex(int target) {
    int index = 0;
    int mask = Integer.highestOneBit(size);
    while (mask > 0) {
        int nextIndex = index + mask;
        if (nextIndex <= size && tree[nextIndex] < target) {
            index = nextIndex;
            target -= tree[nextIndex];
        }
        mask >>= 1;
    }
    return index + 1;
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

**Quick Interview Prep**: Master the basic template, understand `getLowBit(index) = index & (-index)`, and practice #307. Remember 1-indexing! 🚀
