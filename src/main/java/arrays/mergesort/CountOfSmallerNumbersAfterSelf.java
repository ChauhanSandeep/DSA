package arrays.mergesort;

import java.util.*;

/**
 * 315. Count of Smaller Numbers After Self
 *
 * Problem: Given an integer array nums, return an integer array counts where
 * counts[i] is the number of smaller elements to the right of nums[i].
 *
 * Example:
 * Input: nums = [5,2,6,1]
 * Output: [2,1,1,0]
 * Explanation: For 5: numbers 2,1 are smaller. For 2: number 1 is smaller.
 * For 6: number 1 is smaller. For 1: no smaller numbers.
 *
 * LeetCode: https://leetcode.com/problems/count-of-smaller-numbers-after-self
 *
 * Follow-up questions:
 * Q: Can we optimize for arrays with many duplicates?
 * A: Use coordinate compression and handle duplicates efficiently in data structures.
 *
 * Q: What if we need to support dynamic updates?
 * A: Use persistent data structures or segment trees with lazy propagation.
 *
 * Q: How to handle very large numbers or floating point values?
 * A: Use discretization/coordinate compression to map to smaller integer range.
 */
public class CountOfSmallerNumbersAfterSelf {

    /**
     * Merge Sort approach with index tracking.
     *
     * Algorithm: Modified merge sort
     * - During merge process, count inversions for each element
     * - Maintain original indices to update correct positions in result
     * - When merging, count elements from right array that are smaller
     *
     * Time Complexity: O(n log n)
     * Space Complexity: O(n) for auxiliary arrays
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int[][] indexedNums = new int[n][2]; // [value, original_index]

        for (int i = 0; i < n; i++) {
            indexedNums[i] = new int[]{nums[i], i};
        }

        mergeSort(indexedNums, result, 0, n - 1);

        List<Integer> answer = new ArrayList<>();
        for (int count : result) {
            answer.add(count);
        }

        return answer;
    }

    // Modified merge sort that counts smaller elements
    private void mergeSort(int[][] nums, int[] result, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        mergeSort(nums, result, left, mid);
        mergeSort(nums, result, mid + 1, right);
        merge(nums, result, left, mid, right);
    }

    // Merge and count smaller elements
    private void merge(int[][] nums, int[] result, int left, int mid, int right) {
        int[][] temp = new int[right - left + 1][2];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (nums[j][0] < nums[i][0]) {
                temp[k++] = nums[j++];
            } else {
                // Count elements from right subarray that are smaller
                result[nums[i][0]] += j - mid - 1;
                temp[k++] = nums[i++];
            }
        }

        while (i <= mid) {
            result[nums[i][1]] += j - mid - 1;
            temp[k++] = nums[i++];
        }

        while (j <= right) {
            temp[k++] = nums[j++];
        }

        for (i = left; i <= right; i++) {
            nums[i] = temp[i - left];
        }
    }

    /**
     * Binary Indexed Tree (Fenwick Tree) approach.
     * Efficient for coordinate-compressed values.
     */
    public List<Integer> countSmallerBIT(int[] nums) {
        // Coordinate compression
        Set<Integer> uniqueValues = new HashSet<>();
        for (int num : nums) {
            uniqueValues.add(num);
        }

        List<Integer> sortedValues = new ArrayList<>(uniqueValues);
        Collections.sort(sortedValues);

        Map<Integer, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex.put(sortedValues.get(i), i + 1); // BIT uses 1-based indexing
        }

        BIT bit = new BIT(sortedValues.size());
        List<Integer> result = new ArrayList<>();

        // Process from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            int compressedValue = valueToIndex.get(nums[i]);
            int count = bit.query(compressedValue - 1); // Count smaller values
            result.add(count);
            bit.update(compressedValue, 1); // Add current value
        }

        Collections.reverse(result);
        return result;
    }

    // Binary Indexed Tree implementation
    private static class BIT {
        private int[] tree;
        private int size;

        BIT(int size) {
            this.size = size;
            this.tree = new int[size + 1];
        }

        void update(int index, int delta) {
            while (index <= size) {
                tree[index] += delta;
                index += index & (-index);
            }
        }

        int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & (-index);
            }
            return sum;
        }
    }

    /**
     * Segment Tree approach with coordinate compression.
     * More flexible for range updates and queries.
     */
    public List<Integer> countSmallerSegmentTree(int[] nums) {
        // Coordinate compression
        Set<Integer> uniqueValues = new TreeSet<>();
        for (int num : nums) {
            uniqueValues.add(num);
        }

        List<Integer> sortedValues = new ArrayList<>(uniqueValues);
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex.put(sortedValues.get(i), i);
        }

        SegmentTree segTree = new SegmentTree(sortedValues.size());
        List<Integer> result = new ArrayList<>();

        // Process from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            int compressedValue = valueToIndex.get(nums[i]);
            int count = compressedValue > 0 ? segTree.query(0, compressedValue - 1) : 0;
            result.add(count);
            segTree.update(compressedValue, 1);
        }

        Collections.reverse(result);
        return result;
    }

    // Segment Tree implementation
    private static class SegmentTree {
        private int[] tree;
        private int size;

        SegmentTree(int size) {
            this.size = size;
            this.tree = new int[4 * size];
        }

        void update(int pos, int val) {
            update(1, 0, size - 1, pos, val);
        }

        private void update(int node, int start, int end, int pos, int val) {
            if (start == end) {
                tree[node] += val;
            } else {
                int mid = (start + end) / 2;
                if (pos <= mid) {
                    update(2 * node, start, mid, pos, val);
                } else {
                    update(2 * node + 1, mid + 1, end, pos, val);
                }
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }

        int query(int left, int right) {
            if (left > right) return 0;
            return query(1, 0, size - 1, left, right);
        }

        private int query(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                return 0;
            }
            if (left <= start && end <= right) {
                return tree[node];
            }
            int mid = (start + end) / 2;
            return query(2 * node, start, mid, left, right) +
                   query(2 * node + 1, mid + 1, end, left, right);
        }
    }

    /**
     * Multiset approach using TreeMap for simplicity.
     * Good for understanding but less efficient than specialized structures.
     */
    public List<Integer> countSmallerTreeMap(int[] nums) {
        List<Integer> result = new ArrayList<>();
        TreeMap<Integer, Integer> map = new TreeMap<>();

        // Process from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            // Count elements smaller than nums[i]
            int count = 0;
            for (Map.Entry<Integer, Integer> entry : map.headMap(nums[i]).entrySet()) {
                count += entry.getValue();
            }

            result.add(count);
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }

        Collections.reverse(result);
        return result;
    }

    /**
     * Brute force approach for verification.
     * Simple but O(n²) time complexity.
     */
    public List<Integer> countSmallerBruteForce(int[] nums) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int count = 0;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[i]) {
                    count++;
                }
            }
            result.add(count);
        }

        return result;
    }

    /**
     * Balanced BST approach using custom implementation.
     * Each node stores subtree size for efficient counting.
     */
    public List<Integer> countSmallerBST(int[] nums) {
        List<Integer> result = new ArrayList<>();
        BST bst = new BST();

        // Process from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            int count = bst.insert(nums[i]);
            result.add(count);
        }

        Collections.reverse(result);
        return result;
    }

    // Custom BST with subtree size tracking
    private static class BST {
        private BSTNode root;

        int insert(int val) {
            InsertResult result = insertHelper(root, val);
            root = result.node;
            return result.smallerCount;
        }

        private InsertResult insertHelper(BSTNode node, int val) {
            if (node == null) {
                return new InsertResult(new BSTNode(val), 0);
            }

            if (val <= node.val) {
                InsertResult left = insertHelper(node.left, val);
                node.left = left.node;
                node.leftSize++;
                return new InsertResult(node, left.smallerCount);
            } else {
                InsertResult right = insertHelper(node.right, val);
                node.right = right.node;
                return new InsertResult(node, node.leftSize + node.duplicates + right.smallerCount);
            }
        }

        private static class BSTNode {
            int val;
            int duplicates = 1;
            int leftSize = 0;
            BSTNode left, right;

            BSTNode(int val) {
                this.val = val;
            }
        }

        private static class InsertResult {
            BSTNode node;
            int smallerCount;

            InsertResult(BSTNode node, int smallerCount) {
                this.node = node;
                this.smallerCount = smallerCount;
            }
        }
    }

    /**
     * Divide and conquer approach using modified merge sort.
     * Alternative implementation with cleaner structure.
     */
    public List<Integer> countSmallerDivideConquer(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int[] indices = new int[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        mergeSort(nums, indices, result, 0, n - 1);

        List<Integer> answer = new ArrayList<>();
        for (int count : result) {
            answer.add(count);
        }

        return answer;
    }

    // Merge sort with inversion counting
    private void mergeSort(int[] nums, int[] indices, int[] result, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;
        mergeSort(nums, indices, result, left, mid);
        mergeSort(nums, indices, result, mid + 1, right);
        merge(nums, indices, result, left, mid, right);
    }

    // Merge with counting
    private void merge(int[] nums, int[] indices, int[] result, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (nums[indices[j]] < nums[indices[i]]) {
                temp[k++] = indices[j++];
            } else {
                result[indices[i]] += j - mid - 1;
                temp[k++] = indices[i++];
            }
        }

        while (i <= mid) {
            result[indices[i]] += j - mid - 1;
            temp[k++] = indices[i++];
        }

        while (j <= right) {
            temp[k++] = indices[j++];
        }

        for (i = left; i <= right; i++) {
            indices[i] = temp[i - left];
        }
    }

    /**
     * Optimized approach for arrays with limited range.
     * Uses counting sort principles when range is small.
     */
    public List<Integer> countSmallerLimitedRange(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();

        int min = Arrays.stream(nums).min().getAsInt();
        int max = Arrays.stream(nums).max().getAsInt();
        int range = max - min + 1;

        // If range is reasonable, use direct indexing
        if (range <= 100000) {
            int[] count = new int[range];
            List<Integer> result = new ArrayList<>();

            // Process from right to left
            for (int i = nums.length - 1; i >= 0; i--) {
                int normalizedVal = nums[i] - min;

                // Count smaller elements
                int smallerCount = 0;
                for (int j = 0; j < normalizedVal; j++) {
                    smallerCount += count[j];
                }

                result.add(smallerCount);
                count[normalizedVal]++;
            }

            Collections.reverse(result);
            return result;
        }

        // Fall back to BIT approach for large ranges
        return countSmallerBIT(nums);
    }

    /**
     * Parallel processing approach for very large arrays.
     * Divides array into segments and processes concurrently.
     */
    public List<Integer> countSmallerParallel(int[] nums) {
        if (nums.length < 10000) {
            return countSmaller(nums); // Use sequential for small arrays
        }

        // For very large arrays, parallel processing becomes complex due to dependencies
        // This is a simplified version focusing on the merge sort approach
        int n = nums.length;
        int[] result = new int[n];
        int[][] indexedNums = new int[n][2];

        for (int i = 0; i < n; i++) {
            indexedNums[i] = new int[]{nums[i], i};
        }

        // Use parallel merge sort (simplified implementation)
        parallelMergeSort(indexedNums, result, 0, n - 1);

        List<Integer> answer = new ArrayList<>();
        for (int count : result) {
            answer.add(count);
        }

        return answer;
    }

    // Parallel merge sort (simplified)
    private void parallelMergeSort(int[][] nums, int[] result, int left, int right) {
        if (left >= right) return;

        int mid = left + (right - left) / 2;

        // For large subarrays, use parallel processing
        if (right - left > 1000) {
            Thread leftThread = new Thread(() -> parallelMergeSort(nums, result, left, mid));
            Thread rightThread = new Thread(() -> parallelMergeSort(nums, result, mid + 1, right));

            leftThread.start();
            rightThread.start();

            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            parallelMergeSort(nums, result, left, mid);
            parallelMergeSort(nums, result, mid + 1, right);
        }

        merge(nums, result, left, mid, right);
    }
}