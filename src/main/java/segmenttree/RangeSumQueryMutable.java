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

  private final int[] nums;  // Keep track of original array values
  private final BinaryIndexedTree bit;

  /**
   * Binary Indexed Tree (Fenwick Tree) - A tree that efficiently handles prefix sum queries.
   *
   * CORE IDEA: Instead of storing individual elements, we store "cumulative contributions"
   * where each index is responsible for a specific range of elements.
   *
   * How it works:
   * - Uses 1-based indexing (index 0 is unused)
   * - Each index i covers a range determined by its lowest set bit
   * - Index 4 (100 in binary) covers 4 elements: [1,2,3,4]
   * - Index 6 (110 in binary) covers 2 elements: [5,6]
   * - Index 8 (1000 in binary) covers 8 elements: [1,2,3,4,5,6,7,8]
   *
   * Time: O(log n) for both update and query
   * Space: O(n)
   */
  private static class BinaryIndexedTree {
    private final int[] tree; // Each position stores sum for its responsible range

    public BinaryIndexedTree(int size) {
      this.tree = new int[size + 1]; // +1 because we use 1-based indexing
    }

    /**
     * Add a value to position i (1-based index).
     * This propagates the change UP the tree to all ancestors.
     *
     * INTUITION: When we update position i, we need to update all tree nodes
     * that include position i in their range.
     *
     * Example: add(3, 5) updates positions 3→4→8→16...
     * - Position 3: responsible for [3,3], add 5
     * - Position 4: responsible for [1,4], add 5
     * - Position 8: responsible for [1,8], add 5
     */
    public void add(int i, int delta) {
      while (i < tree.length) {
        tree[i] += delta;
        i += i & (-i); // Move to next ancestor using lowest set bit
      }
    }

    /**
     * Get prefix sum from 1 to i (inclusive).
     * This traverses DOWN the tree collecting partial sums.
     *
     * INTUITION: To get sum[1...i], we collect sums from tree nodes
     * whose ranges end exactly at or before position i.
     *
     * Example: prefixSum(7) queries positions 7→6→4→0
     * - Position 7: get sum for [7,7]
     * - Position 6: get sum for [5,6]
     * - Position 4: get sum for [1,4]
     * Total = sum[1,4] + sum[5,6] + sum[7,7] = sum[1,7]
     */
    public int prefixSum(int i) {
      int sum = 0;
      while (i > 0) {
        sum += tree[i];
        i -= i & (-i); // Move to parent using lowest set bit
      }
      return sum;
    }
  }

  public RangeSumQueryMutable(int[] nums) {
    if (nums == null) {
      this.nums = new int[0];
      this.bit = new BinaryIndexedTree(0);
      return;
    }

    this.nums = nums.clone();
    this.bit = new BinaryIndexedTree(nums.length);

    // Initialize the tree by adding each element
    for (int i = 0; i < nums.length; i++) {
      bit.add(i + 1, nums[i]); // Convert to 1-based indexing
    }
  }

  /**
   * Update array[index] to newValue.
   *
   * STRATEGY: Instead of rebuilding the tree, we calculate the difference
   * between old and new values and add this difference to the tree.
   */
  public void update(int index, int newValue) {
    if (index < 0 || index >= nums.length) {
      return;
    }

    int oldValue = nums[index];
    int difference = newValue - oldValue;

    bit.add(index + 1, difference); // Update tree with difference
    nums[index] = newValue; // Update our copy
  }

  /**
   * Calculate sum of elements from left to right (inclusive).
   *
   * STRATEGY: Use prefix sum property
   * sum[left, right] = prefixSum[right] - prefixSum[left-1]
   *
   * Example: sum[2,5] = prefixSum[5] - prefixSum[1]
   *         = (nums[0]+nums[1]+nums[2]+nums[3]+nums[4]) - (nums[0])
   *         = nums[1]+nums[2]+nums[3]+nums[4]
   */
  public int sumRange(int left, int right) {
    if (left < 0 || right >= nums.length || left > right) {
      return 0;
    }

    int rightSum = bit.prefixSum(right + 1); // Sum from 1 to right+1 (1-based)
    int leftSum = bit.prefixSum(left);       // Sum from 1 to left (1-based)

    return rightSum - leftSum;
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
