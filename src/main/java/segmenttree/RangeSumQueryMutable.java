package segmenttree;

/**
 * Range Sum Query - Mutable
 *
 * Problem Statement:
 * Given an integer array nums, handle multiple queries of the following types:
 * 1. Update the value of an element in nums
 * 2. Calculate the sum of elements between indices left and right inclusive
 *
 * Example:
 * Input: nums = [1, 3, 5]
 * sumRange(0, 2) returns 9 (1 + 3 + 5)
 * update(1, 2) changes array to [1, 2, 5]
 * sumRange(0, 2) returns 8 (1 + 2 + 5)
 *
 * LeetCode Link: https://leetcode.com/problems/range-sum-query-mutable
 *
 * Follow-up Questions:
 * 1. How would you handle 2D range sum queries with updates?
 *    Answer: Use 2D Binary Indexed Tree where each row has its own BIT structure.
 *    Related: https://leetcode.com/problems/range-sum-query-2d-mutable/
 * 2. What if we need to support range updates (update all elements in a range)?
 *    Answer: Use difference array technique with BIT or lazy propagation in segment tree.
 * 3. How to handle very large arrays with sparse updates?
 *    Answer: Use coordinate compression to map indices to smaller range.
 * 4. What's the space-time tradeoff between BIT and Segment Tree?
 *    Answer: BIT uses less space (n vs 4n) but segment tree supports more operations like range min/max.
 */
public class RangeSumQueryMutable {

  private final int[] originalNums;
  private final BinaryIndexedTree fenwickTree;

  /**
   * Initializes the NumArray object with the integer array nums.
   *
   * Algorithm: Binary Indexed Tree Construction
   * Step 1: Store original array for calculating deltas during updates
   * Step 2: Initialize BIT with size n+1 (1-indexed for easier bit manipulation)
   * Step 3: Add each element to BIT using add operation
   *
   * Time Complexity: O(n log n) where n is length of nums
   * Space Complexity: O(n) for BIT array and original array storage
   *
   * @param nums the input integer array
   */
  public RangeSumQueryMutable(int[] nums) {
    if (nums == null) {
      originalNums = new int[0];
      fenwickTree = new BinaryIndexedTree(0);
      return;
    }

    this.originalNums = nums.clone();
    this.fenwickTree = new BinaryIndexedTree(nums.length);

    // Build the fenwick tree by adding each element
    for (int i = 0; i < nums.length; i++) {
      fenwickTree.add(i + 1, nums[i]); // Convert to 1-indexed
    }
  }

  /**
   * Updates the value of nums[index] to val.
   *
   * Algorithm: Delta Update in BIT
   * Step 1: Calculate delta = newValue - oldValue
   * Step 2: Add delta to BIT at position (index + 1) to maintain 1-indexing
   * Step 3: Update original array with new value
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(1)
   *
   * @param index the index to update
   * @param val the new value
   */
  public void update(int index, int val) {
    if (index < 0 || index >= originalNums.length) {
      return; // Handle invalid index
    }

    int delta = val - originalNums[index];
    fenwickTree.add(index + 1, delta); // Convert to 1-indexed
    originalNums[index] = val;
  }

  /**
   * Returns the sum of elements from left to right inclusive.
   *
   * Algorithm: Prefix Sum Difference
   * Step 1: Get prefix sum up to right index
   * Step 2: Get prefix sum up to (left - 1) index
   * Step 3: Return difference to get range sum
   *
   * Formula: sumRange(left, right) = prefixSum(right) - prefixSum(left - 1)
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(1)
   *
   * @param left the left boundary index
   * @param right the right boundary index
   * @return sum of elements in the range [left, right]
   */
  public int sumRange(int left, int right) {
    if (left < 0 || right >= originalNums.length || left > right) {
      return 0; // Handle invalid range
    }

    return fenwickTree.getPrefix(right + 1) - fenwickTree.getPrefix(left);
  }

  /**
   * Binary Indexed Tree (Fenwick Tree) implementation for efficient range sum queries.
   * Uses 1-based indexing for easier bit manipulation operations.
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(n)
   */
  private static class BinaryIndexedTree {
    private final int[] tree;

    public BinaryIndexedTree(int size) {
      this.tree = new int[size + 1]; // 1-indexed array
    }

    /**
     * Adds delta to the element at position index.
     * Updates all relevant nodes in the BIT structure.
     */
    public void add(int index, int delta) {
      while (index < tree.length) {
        tree[index] += delta;
        index += getLowBit(index); // Move to next responsible index
      }
    }

    /**
     * Returns prefix sum from index 1 to index (inclusive).
     * Traverses from index towards root accumulating sums.
     */
    public int getPrefix(int index) {
      int sum = 0;
      while (index > 0) {
        sum += tree[index];
        index -= getLowBit(index); // Move to parent index
      }
      return sum;
    }

    // Returns the lowest set bit of index (index & -index)
    private int getLowBit(int index) {
      return index & (-index);
    }
  }

  // Alternative approach using Segment Tree (more memory but supports more operations)

  /**
   * Alternative implementation using Segment Tree.
   * Provides O(log n) updates and queries with support for additional operations.
   *
   * Time Complexity: O(log n) for updates and queries
   * Space Complexity: O(n) for segment tree array
   */
  public static class SegmentTreeApproach {
    private final int[] nums;
    private final int[] segmentTree;
    private final int arraySize;

    public SegmentTreeApproach(int[] nums) {
      this.nums = nums.clone();
      this.arraySize = nums.length;
      this.segmentTree = new int[4 * arraySize]; // Segment tree needs 4n space
      buildTree(0, 0, arraySize - 1);
    }

    // Build segment tree recursively
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

    // Update element and propagate changes up the tree
    public void update(int index, int val) {
      updateTree(0, 0, arraySize - 1, index, val);
      nums[index] = val;
    }

    /**
     * Updates the value of nums[updateIndex] to updateValue.
     * @param treeIndex current index in segment tree
     * @param start start of current segment
     * @param end end of current segment
     * @param updateIndex updateIndex to update
     * @param updateValue new value to set
     */
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

    // Query range sum using segment tree
    public int sumRange(int left, int right) {
      return queryTree(0, 0, arraySize - 1, left, right);
    }

    private int queryTree(int treeIndex, int start, int end, int leftRange, int rightRange) {
      if (rightRange < start || leftRange > end) {
        // No overlap, return 0
        return 0;
      }

      if (leftRange <= start && end <= rightRange) {
        // Complete overlap of current segment with query range, return the current segment sum
        return segmentTree[treeIndex]; // Complete overlap
      }

      // Partial overlap - query both children
      int mid = start + (end - start) / 2;
      int leftChild = 2 * treeIndex + 1;
      int rightChild = 2 * treeIndex + 2;

      return queryTree(leftChild, start, mid, leftRange, rightRange)
          + queryTree(rightChild, mid + 1, end, leftRange, rightRange);
    }
  }
}

/**
 * Usage Example:
 * NumArray numArray = new NumArray(nums);
 * int result = numArray.sumRange(left, right);
 * numArray.update(index, val);
 */
