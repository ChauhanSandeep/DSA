package trees.segmenttree;

import java.util.Arrays;
/**
 * Problem: Range Sum Query - Mutable
 *
 * Maintain an integer array under point updates and range-sum queries. A Fenwick
 * Tree stores prefix-sum contributions so each update and query touches only the
 * indexes reached by the lowest-set-bit jumps.
 *
 * Leetcode: https://leetcode.com/problems/range-sum-query-mutable/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Segment tree topic | Fenwick Tree | Prefix sums with point updates
 *
 * Example:
 *   Input:  nums = [1,3,5], sumRange(0,2), update(1,2), sumRange(0,2)
 *   Output: [9,8]
 *   Why:    the total changes from 1+3+5 to 1+2+5 after the point update.
 *
 * Follow-ups:
 *   1. How would you support range updates too?
 *      Use two Fenwick Trees or a lazy segment tree.
 *   2. How would you support range minimum?
 *      Use a segment tree because Fenwick sums rely on invertible prefix operations.
 *   3. How would this extend to 2D?
 *      Maintain a Fenwick Tree of Fenwick Trees over rows and columns.
 *   4. How would you handle sparse huge indexes?
 *      Compress coordinates or use hash maps for Fenwick storage.
 *
 * Related: Range Sum Query 2D - Mutable (308).
 */
public class FenwickTreeRangeSumQuery {

  public static void main(String[] args) {
    int[] nums = {1, 3, 5};
    FenwickTreeRangeSumQuery rangeSum = new FenwickTreeRangeSumQuery(nums);
    int before = rangeSum.sumRange(0, 2);
    rangeSum.update(1, 2);
    int after = rangeSum.sumRange(0, 2);

    FenwickTreeRangeSumQuery empty = new FenwickTreeRangeSumQuery(null);
    System.out.printf("nums=%s -> [%d, %d]  expected=[9, 8]%n",
        Arrays.toString(nums), before, after);
    System.out.printf("nums=null sumRange(0,0) -> %d  expected=0%n", empty.sumRange(0, 0));
  }


  private final int[] nums;  // Keep track of original array values
  private final FenwickTree fenwickTree;

    /**
   * Intuition: keep a private copy of nums so an update can be converted into a
   * delta. Adding that delta at index + 1 updates every Fenwick bucket whose covered
   * range includes the changed element.
   *
   * Algorithm:
   *   1. For null input, create empty backing arrays.
   *   2. Clone nums for future delta calculations.
   *   3. Create a FenwickTree with nums.length capacity.
   *   4. Add each value at its 1-based Fenwick position.
   *
   * Time:  O(n log n) - each initial value is added through Fenwick ancestors.
   * Space: O(n) - original copy plus Fenwick tree array.
   *
   * @param nums initial array values
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
   * Intuition: a point update changes all prefix sums that include that index by the
   * same difference. Fenwick add applies exactly that difference to the affected
   * buckets.
   *
   * Algorithm:
   *   1. Ignore indexes outside nums.
   *   2. Compute newValue - oldValue.
   *   3. Add the difference at index + 1 in the Fenwick tree.
   *   4. Store newValue in nums.
   *
   * Time:  O(log n) - lowest-set-bit jumps visit Fenwick ancestors.
   * Space: O(1) - only a few scalar values are used.
   *
   * @param index 0-based index to update
   * @param newValue replacement value
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
   * Intuition: any inclusive range sum is the difference of two prefix sums. The
   * Fenwick tree can compute prefix sums quickly, so sum[left..right] becomes
   * prefix(right + 1) minus prefix(left).
   *
   * Algorithm:
   *   1. Return 0 for invalid ranges.
   *   2. Query prefix sum through right + 1.
   *   3. Query prefix sum before left.
   *   4. Return their difference.
   *
   * Time:  O(log n) - two Fenwick prefix queries.
   * Space: O(1) - no extra data structure is allocated.
   *
   * @param left 0-based inclusive left boundary
   * @param right 0-based inclusive right boundary
   * @return sum of nums[left..right]
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
