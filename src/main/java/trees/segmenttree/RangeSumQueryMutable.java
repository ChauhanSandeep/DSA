package trees.segmenttree;

import java.util.Arrays;
/**
 * Problem: Range Sum Query - Mutable
 *
 * Maintain an array under point updates and inclusive range-sum queries. The
 * primary implementation stores segment sums in an array-backed segment tree.
 *
 * Leetcode: https://leetcode.com/problems/range-sum-query-mutable/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Segment tree | Divide and conquer ranges | Point update and range query
 *
 * Example:
 *   Input:  nums = [1,3,5], sumRange(0,2), update(1,2), sumRange(0,2)
 *   Output: [9,8]
 *   Why:    the updated array is [1,2,5], whose total is 8.
 *
 * Follow-ups:
 *   1. How would you support range updates?
 *      Add lazy propagation to defer updates for fully covered segments.
 *   2. How would you reduce memory for pure sums?
 *      Use a Fenwick Tree, which stores O(n) prefix contributions.
 *   3. How would you support range min and range max too?
 *      Store multiple aggregates per segment tree node.
 *   4. How would you handle sparse indexes?
 *      Use a dynamic segment tree with nodes created on demand.
 *
 * Related: Range Sum Query 2D - Mutable (308).
 */
public class RangeSumQueryMutable {

  public static void main(String[] args) {
    int[] nums = {1, 3, 5};
    SegmentTreeApproach rangeSum = new SegmentTreeApproach(nums);
    int before = rangeSum.sumRange(0, 2);
    rangeSum.update(1, 2);
    int after = rangeSum.sumRange(0, 2);
    System.out.printf("nums=%s -> [%d, %d]  expected=[9, 8]%n",
        Arrays.toString(nums), before, after);

    int[] singleInput = {5};
    SegmentTreeApproach single = new SegmentTreeApproach(singleInput);
    System.out.printf("nums=%s -> %d  expected=5%n",
        Arrays.toString(singleInput), single.sumRange(0, 0));
  }


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
     * Intuition: each segment tree node stores the sum for one array interval. Leaves
     * store single values; internal nodes combine left and right child sums.
     *
     * Algorithm:
     *   1. Clone nums and remember arraySize.
     *   2. Allocate a 4*n segment tree array.
     *   3. Recursively build from the full range.
     *   4. Store leaf values and combine child sums on return.
     *
     * Time:  O(n) - each segment tree node is built once.
     * Space: O(n) - segment tree array plus cloned nums.
     *
     * @param nums initial array values
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
     * Intuition: a point update changes one leaf and every segment that contains that
     * leaf. Recomputing sums on the path back to the root restores all affected range
     * answers.
     *
     * Algorithm:
     *   1. Recurse from the root range toward index.
     *   2. Replace the leaf value with val.
     *   3. Recompute each ancestor as left child sum plus right child sum.
     *   4. Store val in nums.
     *
     * Time:  O(log n) - one root-to-leaf path is updated.
     * Space: O(log n) - recursion depth is the tree height.
     *
     * @param index index to update
     * @param val replacement value
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
     * Intuition: a query range can be decomposed into segment tree intervals. Segments
     * outside the range contribute zero, segments fully inside contribute their stored
     * sum, and partial segments split into children.
     *
     * Algorithm:
     *   1. Query from the root segment.
     *   2. Return 0 for no overlap.
     *   3. Return the stored node sum for complete overlap.
     *   4. Split partial overlap and add left and right query results.
     *
     * Time:  O(log n) - only boundary paths and covered segments are visited.
     * Space: O(log n) - recursion stack follows tree height.
     *
     * @param left inclusive query start
     * @param right inclusive query end
     * @return sum of nums[left..right]
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
