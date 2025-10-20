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
   * Binary Indexed Tree (Fenwick Tree) implementation for efficient range sum queries.
   * Uses 1-based indexing for easier bit manipulation operations.
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(n)
   */
  private static class BinaryIndexedTree {
    private final int[] tree; // contains the sums of ranges

    /**
     * Constructs a Binary Indexed Tree (Fenwick Tree) with the given size.
     * @param size The size of the array to be managed by the BIT.
     */
    public BinaryIndexedTree(int size) {
      this.tree = new int[size + 1]; // 1-indexed array
    }

    /**
     * Adds delta to the element at position index.
     * Updates all relevant nodes in the BIT structure.
     *
     * Steps:
     * 1. Start from index and add delta to tree[index].
     * 2. Move to the next responsible index by adding the lowest set bit. The movement patterns is
     *    1 -> 2 -> 4 -> 8 -> 16 -> ...
     * 3. Repeat until index exceeds tree size.
     *
     * @param index the position to update (1-based index)
     * @param delta the value to add
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
     *
     * Steps:
     * 1. Initialize sum to 0.
     * 2. While index > 0, add tree[index] to sum.
     * 3. Move to parent index by subtracting the lowest set bit.
     * 4. Return the accumulated sum.
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
      int val = nums[i];
      fenwickTree.add(i + 1, val); // Convert to 1-indexed
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
   * Alternative implementation using Segment Tree.
   * Provides O(log n) updates and queries with support for additional operations.
   *
   * Algorithm:
   * 1. Build the segment tree recursively, storing the sum for each segment.
   * 2. For update, propagate the change from the leaf node up to the root, updating affected segment sums.
   * 3. For sumRange, recursively query the relevant segments and combine their sums.
   *
   * Steps:
   * - Build: Recursively split the array and store sums in the segment tree array.
   * - Update: Update the value at the leaf and update all ancestors.
   * - Query: If the current segment is fully within the query range, return its sum; otherwise, query children.
   *
   * Time Complexity: O(log n) for updates and queries
   * Space Complexity: O(n) for segment tree array
   */
  public static class SegmentTreeApproach {
    private final int[] nums;         // Original array (for reference)
    private final int[] segmentTree;  // Segment tree array
    private final int arraySize;      // Size of the original array

    /**
     * Constructs the segment tree from the input array.
     * @param nums input array
     */
    public SegmentTreeApproach(int[] nums) {
      this.nums = nums.clone();
      this.arraySize = nums.length;
      this.segmentTree = new int[4 * arraySize]; // 4n is enough for segment tree storage
      buildTree(0, 0, arraySize - 1);
    }

    /**
     * Recursively builds the segment tree.
     * @param treeIndex current index in segment tree array
     * @param start start index of the segment in original array
     * @param end end index of the segment in original array
     */
    private void buildTree(int treeIndex, int start, int end) {
      if (start == end) {
        segmentTree[treeIndex] = nums[start];
        return;
      }

      int mid = start + (end - start) / 2;
      int leftChildIndex = 2 * treeIndex + 1;
      int rightChildIndex = 2 * treeIndex + 2;

      buildTree(leftChildIndex, start, mid);
      buildTree(rightChildIndex, mid + 1, end);

      segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }

    /**
     * Updates the value at the given index and propagates the change up the tree.
     * @param index index to update
     * @param val new value
     */
    public void update(int index, int val) {
      updateTree(0, 0, arraySize - 1, index, val);
      nums[index] = val;
    }

    /**
     * Helper for update: recursively updates the segment tree.
     *
     * @param treeIndex current index in segment tree
     * @param start start of current segment
     * @param end end of current segment
     * @param updateIdx index to update
     * @param updateVal new value
     */
    private void updateTree(int treeIndex, int start, int end, int updateIdx, int updateVal) {
      if (start == end) {
        // We have reached the leaf node to update, here start == end == updateIdx
        segmentTree[treeIndex] = updateVal;
        return;
      }

      int mid = start + (end - start) / 2;

      int leftChildIndex = 2 * treeIndex + 1;
      int rightChildIndex = 2 * treeIndex + 2;

      if (updateIdx <= mid) {
        updateTree(leftChildIndex, start, mid, updateIdx, updateVal);
      } else {
        updateTree(rightChildIndex, mid + 1, end, updateIdx, updateVal);
      }

      // Update current node sum after updating children
      segmentTree[treeIndex] = segmentTree[leftChildIndex] + segmentTree[rightChildIndex];
    }

    /**
     * Returns the sum of elements in the range [left, right].
     *
     * @param left left boundary (inclusive)
     * @param right right boundary (inclusive)
     * @return sum of elements in range
     */
    public int sumRange(int left, int right) {
      return queryTree(0, 0, arraySize - 1, left, right);
    }

    /**
     * Helper for sumRange: recursively queries the segment tree.
     *
     * @param treeIdx current index in segment tree
     * @param start start of current segment
     * @param end end of current segment
     * @param leftRange left boundary of query
     * @param rightRange right boundary of query
     * @return sum in the given range
     */
    private int queryTree(int treeIdx, int start, int end, int leftRange, int rightRange) {
      // No overlap
      if (rightRange < start || leftRange > end) {
        // No overlap, return 0
        return 0;
      }

      // Complete overlap
      if (leftRange <= start && end <= rightRange) {
        return segmentTree[treeIdx];
      }

      // Partial overlap: query both children
      int mid = start + (end - start) / 2;
      int leftChildIndex = 2 * treeIdx + 1;
      int rightChildIndex = 2 * treeIdx + 2;

      return queryTree(leftChildIndex, start, mid, leftRange, rightRange)
          + queryTree(rightChildIndex, mid + 1, end, leftRange, rightRange);
    }
  }
}
