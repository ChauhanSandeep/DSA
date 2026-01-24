package trees.segmenttree;

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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RangeSumQueryMutable {

  /**
   * Segment Tree based implementation using Segment Tree.
   * Provides O(log n) updates and queries with support for additional operations.
   *
   * WHEN TO USE SEGMENT TREE:
   * - Need range minimum/maximum queries (not just sums)
   * - Need range updates (update all elements in a range)
   * - Need more complex aggregate operations
   * - Memory usage is not a primary concern
   *
   * WHEN TO USE FENWICK TREE (FenwickTreeRangeSumQuery):
   * - Only need range sum queries
   * - Want better space efficiency (n vs 4n)
   * - Prefer simpler implementation
   * - Working with very large arrays
   *
   * Algorithm:
   * 1. Build the segment tree recursively, storing the sum for each segment.
   * 2. For update, propagate the change from the leaf node up to the root, updating affected segment sums.
   * 3. For sumRange, recursively query the relevant segments and combine their sums.
   *
   * Time Complexity: O(log n) for updates and queries
   * Space Complexity: O(4n) for segment tree array
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
     * - If leaf node, store the array value.
     * - Find midpoint, build left and right children, then set current node sum.
     * - Handles segments defined by [start, end].
     * 
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
