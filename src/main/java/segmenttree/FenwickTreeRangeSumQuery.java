package segmenttree;

/**
 * Range Sum Query - Mutable using Fenwick Tree (Binary Indexed Tree)
 *
 * Problem Statement:
 * Given an integer array nums, handle multiple queries of the following types:
 * 1. Update the value of an element in nums
 * 2. Calculate the sum of elements between indices left and right inclusive
 *
 * FENWICK TREE APPROACH:
 * - More space efficient than Segment Tree (O(n) vs O(4n))
 * - Simpler implementation with elegant bit manipulation
 * - Excellent for prefix sum queries and point updates
 *
 * CORE IDEA: Instead of storing individual elements, we store "cumulative contributions"
 * where each index is responsible for a specific range of elements.
 *
 * RANGE DERIVATION:
 * For any index i, the range it covers is [i - (i & -i) + 1, i]
 * where (i & -i) isolates the lowest set bit, telling us the range size.
 *
 * Examples:
 * - Index 4 (100 binary): lowest set bit = 4, range = [4-4+1, 4] = [1,4] (4 elements)
 * - Index 6 (110 binary): lowest set bit = 2, range = [6-2+1, 6] = [5,6] (2 elements)
 * - Index 8 (1000 binary): lowest set bit = 8, range = [8-8+1, 8] = [1,8] (8 elements)
 *
 * Time Complexity: O(log n) for both update and query
 * Space Complexity: O(n)
 *
 * LeetCode: https://leetcode.com/problems/range-sum-query-mutable/
 */
public class FenwickTreeRangeSumQuery {

  private final int[] nums;  // Keep track of original array values
  private final FenwickTree fenwickTree;

  /**
   * Constructs the Fenwick Tree from the input array.
   *
   * INITIALIZATION STRATEGY:
   * 1. Keep a copy of original values to calculate deltas during updates
   * 2. Build Fenwick tree by adding each element at its corresponding position
   *
   * @param nums the input array to initialize with
   */
  public FenwickTreeRangeSumQuery(int[] nums) {
    if (nums == null) {
      this.nums = new int[0];
      this.fenwickTree = new FenwickTree(0);
      return;
    }

    this.nums = nums.clone(); // Keep copy for delta calculations
    this.fenwickTree = new FenwickTree(nums.length);

    // Build the Fenwick tree by adding each element
    for (int i = 0; i < nums.length; i++) {
      fenwickTree.add(i + 1, nums[i]); // Convert to 1-based indexing
    }
  }

  /**
   * Updates the value at the specified index to a new value.
   *
   * Instead of rebuilding the tree, we calculate the difference between
   * new and old values and add this difference to the tree.
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(1)
   *
   * @param index 0-based index to update
   * @param newValue the new value to set at this index
   */
  public void update(int index, int newValue) {
    if (index < 0 || index >= nums.length) {
      return; // Ignore invalid indices
    }

    int oldValue = nums[index];
    int difference = newValue - oldValue;

    fenwickTree.add(index + 1, difference); // Apply difference to tree (convert to 1-based)
    nums[index] = newValue; // Update our stored copy
  }

  /**
   * Returns the sum of elements in the range [left, right] (both inclusive).
   *
   * RANGE SUM STRATEGY:
   * Use the prefix sum property of Fenwick tree:
   * sum[left, right] = prefixSum[right] - prefixSum[left-1]
   *
   * Time Complexity: O(log n)
   * Space Complexity: O(1)
   *
   * @param left 0-based left boundary (inclusive)
   * @param right 0-based right boundary (inclusive)
   * @return sum of elements in the specified range
   */
  public int sumRange(int left, int right) {
    if (left < 0 || right >= nums.length || left > right) {
      return 0; // Return 0 for invalid ranges
    }

    int prefixSumUpToRight = fenwickTree.prefixSum(right + 1); // Convert to 1-based, get sum[1..right+1]
    int prefixSumBeforeLeft = fenwickTree.prefixSum(left);     // Convert to 1-based, get sum[1..left]

    return prefixSumUpToRight - prefixSumBeforeLeft;   // Result: sum[left+1..right+1] in 1-based = sum[left..right] in 0-based
  }

  /**
   * Binary Indexed Tree (Fenwick Tree) - Core implementation for efficient range sum queries.
   *
   * KEY CONCEPTS:
   * - Uses 1-based indexing (index 0 is unused) to simplify bit operations
   * - Each index i covers a range determined by its lowest set bit
   * - Tree structure allows O(log n) updates and queries
   */
  private static class FenwickTree {
    private final int[] tree; // Each position stores sum for its responsible range

    /**
     * Creates a Fenwick Tree with given capacity.
     * @param size The number of elements the tree should handle
     */
    public FenwickTree(int size) {
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
     * - Position 3 (011 binary): responsible for [3,3], add 5. Next: 3 + (3 & -3) = 3 + 1 = 4
     * - Position 4 (100 binary): responsible for [1,4], add 5. Next: 4 + (4 & -4) = 4 + 4 = 8
     * - Position 8 (1000 binary): responsible for [1,8], add 5. Next: 8 + (8 & -8) = 8 + 8 = 16
     *
     * WHY THIS WORKS: Each position's lowest set bit determines how many elements it covers.
     * By adding this bit value, we jump to the next ancestor in the tree hierarchy.
     *
     * @param position 1-based index where to apply the update
     * @param valueToAdd the value to add at this position
     */
    public void add(int position, int valueToAdd) {
      while (position < tree.length) {
        tree[position] += valueToAdd;
        position += position & (-position); // Jump to next ancestor using lowest set bit
      }
    }

    /**
     * Get prefix sum from index 1 to position (inclusive).
     * This traverses DOWN the tree collecting partial sums.
     *
     * INTUITION: To get sum[1...position], we collect sums from tree nodes
     * whose ranges end exactly at or before the target position.
     *
     * Example: prefixSum(7) queries positions 7→6→4→0
     * - Position 7 (111 binary): get sum for [7,7], then 7 - (7 & -7) = 7 - 1 = 6
     * - Position 6 (110 binary): get sum for [5,6], then 6 - (6 & -6) = 6 - 2 = 4
     * - Position 4 (100 binary): get sum for [1,4], then 4 - (4 & -4) = 4 - 4 = 0
     * - Position 0: stop (reached root)
     *
     * Total = sum[1,4] + sum[5,6] + sum[7,7] = sum[1,7]
     *
     * WHY THIS WORKS: By subtracting the lowest set bit, we move to the parent node
     * in the tree structure, collecting all disjoint ranges that sum to our target.
     *
     * @param position 1-based index up to which we want the prefix sum
     * @return sum of elements from index 1 to position (inclusive)
     */
    public int prefixSum(int position) {
      int totalSum = 0;
      while (position > 0) {
        totalSum += tree[position];
        position -= position & (-position); // Move to parent using lowest set bit
      }
      return totalSum;
    }
  }
}
