package array;

/**
 * Range Sum Query Mutable
 *
 * Problem: Design data structure for array that supports point updates and range sum queries.
 * Updates change single element, queries return sum of range.
 *
 * Example: nums = [1,3,5] -> sumRange(0,2) = 9, update(1,2) -> sumRange(0,2) = 8
 *
 * LeetCode: https://leetcode.com/problems/range-sum-query-mutable
 *
 * Follow-up Questions:
 * - How to handle range updates too? (Use segment tree with lazy propagation)
 * - What if we need other range operations (min, max)? (Generalize segment tree)
 * - Can we optimize for mostly reads or mostly writes? (Choose appropriate data structure)
 */
public class RangeSumQueryMutable {

    private int[] tree;
    private int n;

    /**
     * Constructs the data structure using Binary Indexed Tree (Fenwick Tree).
     *
     * Algorithm:
     * 1. Binary Indexed Tree allows O(log n) updates and O(log n) range sum queries
     * 2. Tree[i] stores partial sum responsible for range ending at index i
     * 3. Uses bit manipulation to navigate tree structure efficiently
     *
     * Time Complexity: O(n log n) for construction
     * Space Complexity: O(n) for tree array
     *
     * @param nums initial array values
     */
    public RangeSumQueryMutable(int[] nums) {
        n = nums.length;
        tree = new int[n + 1]; // 1-indexed tree

        // Build tree by adding each element
        for (int i = 0; i < n; i++) {
            updateBIT(i + 1, nums[i]);
        }
    }

    /**
     * Updates element at index to new value.
     *
     * Algorithm:
     * 1. Calculate difference between new and old values
     * 2. Propagate difference through all nodes responsible for this index
     * 3. Use bit manipulation to find next responsible node: i += (i & -i)
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     *
     * @param index position to update (0-based)
     * @param val new value
     */
    public void update(int index, int val) {
        // Get current value to calculate difference
        int currentVal = sumRange(index, index);
        int diff = val - currentVal;

        // Update BIT with difference
        updateBIT(index + 1, diff);
    }

    // Helper method to update BIT
    private void updateBIT(int index, int delta) {
        while (index <= n) {
            tree[index] += delta;
            index += index & (-index); // Move to next responsible node
        }
    }

    /**
     * Returns sum of elements from left to right inclusive.
     *
     * Algorithm:
     * 1. Range sum = prefixSum(right) - prefixSum(left-1)
     * 2. Prefix sum calculated by traversing tree upward
     * 3. Use bit manipulation to find parent node: i -= (i & -i)
     *
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     *
     * @param left start index (inclusive)
     * @param right end index (inclusive)
     * @return sum of elements in range [left, right]
     */
    public int sumRange(int left, int right) {
        return getPrefixSum(right + 1) - getPrefixSum(left);
    }

    // Helper method to get prefix sum
    private int getPrefixSum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= index & (-index); // Move to parent node
        }
        return sum;
    }

    /**
     * Alternative implementation using Segment Tree
     */
    public static class RangeSumQuerySegmentTree {
        private int[] tree;
        private int n;

        public RangeSumQuerySegmentTree(int[] nums) {
            n = nums.length;
            tree = new int[4 * n]; // Segment tree needs 4*n space
            buildTree(nums, 0, 0, n - 1);
        }

        private void buildTree(int[] nums, int node, int start, int end) {
            if (start == end) {
                tree[node] = nums[start];
            } else {
                int mid = (start + end) / 2;
                buildTree(nums, 2 * node + 1, start, mid);
                buildTree(nums, 2 * node + 2, mid + 1, end);
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }

        public void update(int index, int val) {
            updateTree(0, 0, n - 1, index, val);
        }

        private void updateTree(int node, int start, int end, int index, int val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (index <= mid) {
                    updateTree(2 * node + 1, start, mid, index, val);
                } else {
                    updateTree(2 * node + 2, mid + 1, end, index, val);
                }
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }

        public int sumRange(int left, int right) {
            return queryTree(0, 0, n - 1, left, right);
        }

        private int queryTree(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                return 0; // Out of range
            }
            if (left <= start && end <= right) {
                return tree[node]; // Complete overlap
            }

            // Partial overlap
            int mid = (start + end) / 2;
            int leftSum = queryTree(2 * node + 1, start, mid, left, right);
            int rightSum = queryTree(2 * node + 2, mid + 1, end, left, right);
            return leftSum + rightSum;
        }
    }

    /**
     * Simple array-based implementation (less efficient but easy to understand)
     * Time Complexity: O(1) update, O(n) range sum
     */
    public static class RangeSumQueryArray {
        private int[] nums;

        public RangeSumQueryArray(int[] nums) {
            this.nums = nums.clone();
        }

        public void update(int index, int val) {
            nums[index] = val;
        }

        public int sumRange(int left, int right) {
            int sum = 0;
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
            return sum;
        }
    }
}
