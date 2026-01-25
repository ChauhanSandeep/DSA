package arrays.twopointers;

import java.util.*;

/**
 * 1499. Max Value of Equation
 *
 * Problem: Given an array points where points[i] = [xi, yi], and integer k,
 * find the maximum value of yi + yj + |xi - xj| where |xi - xj| <= k.
 * Since xi < xj, this becomes yi + yj + xj - xi.
 *
 * Example:
 * Input: points = [[1,3],[2,0],[5,10],[6,-10]], k = 1
 * Output: 4
 * Explanation: First two points: 3 + 0 + |2 - 1| = 4
 *
 * LeetCode: https://leetcode.com/problems/max-value-of-equation
 *
 * Follow-up questions:
 * Q: What if k is very large relative to coordinate range?
 * A: All pairs become valid, use simpler O(n²) approach or optimize differently.
 *
 * Q: How to handle negative coordinates or very large ranges?
 * A: Algorithm works the same, just be careful with integer overflow.
 *
 * Q: Can we optimize for sparse point distributions?
 * A: Use coordinate compression or spatial data structures like KD-trees.
 * LeetCode Contest Rating: 2456
 */
public class MaxValueOfEquation {

    /**
     * Priority Queue (Max Heap) approach using sliding window.
     *
     * Algorithm: Sliding window with heap optimization
     * - Rewrite equation as: (yi - xi) + (yj + xj) for i < j
     * - Use max heap to track maximum (yi - xi) values
     * - For each point j, find maximum valid (yi - xi) where xi >= xj - k
     * - Remove invalid points from heap and calculate max value
     *
     * Time Complexity: O(n log n) where n is number of points
     * Space Complexity: O(n) for the priority queue
     */
    public int findMaxValueOfEquation(int[][] points, int k) {
        // Max heap storing [yi - xi, xi] ordered by yi - xi descending
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        int maxValue = Integer.MIN_VALUE;

        for (int[] point : points) {
            int x = point[0], y = point[1];

            // Remove points that are too far (xi < xj - k)
            while (!maxHeap.isEmpty() && x - maxHeap.peek()[1] > k) {
                maxHeap.poll();
            }

            // Calculate max value with current point as j
            if (!maxHeap.isEmpty()) {
                int maxYiMinusXi = maxHeap.peek()[0];
                maxValue = Math.max(maxValue, maxYiMinusXi + y + x);
            }

            // Add current point for future calculations
            maxHeap.offer(new int[]{y - x, x});
        }

        return maxValue;
    }

    /**
     * Deque-based sliding window maximum approach.
     * More efficient than heap for this specific problem structure.
     */
    public int findMaxValueOfEquationDeque(int[][] points, int k) {
        // Deque storing indices, maintaining decreasing order of (yi - xi)
        Deque<Integer> deque = new ArrayDeque<>();
        int maxValue = Integer.MIN_VALUE;

        for (int j = 0; j < points.length; j++) {
            int xj = points[j][0], yj = points[j][1];

            // Remove points outside valid range
            while (!deque.isEmpty() && xj - points[deque.peekFirst()][0] > k) {
                deque.pollFirst();
            }

            // Calculate max value using best point i
            if (!deque.isEmpty()) {
                int bestI = deque.peekFirst();
                int xi = points[bestI][0], yi = points[bestI][1];
                maxValue = Math.max(maxValue, yi + yj + xj - xi);
            }

            // Add current point, maintaining decreasing order
            while (!deque.isEmpty()) {
                int lastIdx = deque.peekLast();
                int lastValue = points[lastIdx][1] - points[lastIdx][0];
                int currentValue = yj - xj;

                if (currentValue <= lastValue) {
                    break;
                }
                deque.pollLast();
            }

            deque.offerLast(j);
        }

        return maxValue;
    }

    /**
     * Two-pointer approach with sorting by transformed values.
     * Alternative perspective using different data organization.
     */
    public int findMaxValueOfEquationTwoPointer(int[][] points, int k) {
        // Create array of [yi - xi, xi, yi] for easier processing
        int n = points.length;
        PointData[] data = new PointData[n];

        for (int i = 0; i < n; i++) {
            int x = points[i][0], y = points[i][1];
            data[i] = new PointData(y - x, x, y);
        }

        int maxValue = Integer.MIN_VALUE;

        // For each point j, find the best point i
        for (int j = 0; j < n; j++) {
            int xj = data[j].x, yj = data[j].y;

            // Find maximum yi - xi among valid points
            int maxYiMinusXi = Integer.MIN_VALUE;

            for (int i = 0; i < j; i++) {
                if (xj - data[i].x <= k) {
                    maxYiMinusXi = Math.max(maxYiMinusXi, data[i].yMinusX);
                }
            }

            if (maxYiMinusXi != Integer.MIN_VALUE) {
                maxValue = Math.max(maxValue, maxYiMinusXi + yj + xj);
            }
        }

        return maxValue;
    }

    // Helper class for point data
    private static class PointData {
        int yMinusX, x, y;

        PointData(int yMinusX, int x, int y) {
            this.yMinusX = yMinusX;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Brute force approach checking all valid pairs.
     * Simple but inefficient - for verification and small inputs.
     */
    public int findMaxValueOfEquationBruteForce(int[][] points, int k) {
        int maxValue = Integer.MIN_VALUE;
        int n = points.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int xi = points[i][0], yi = points[i][1];
                int xj = points[j][0], yj = points[j][1];

                if (xj - xi <= k) {
                    int value = yi + yj + xj - xi;
                    maxValue = Math.max(maxValue, value);
                }
            }
        }

        return maxValue;
    }

    /**
     * Segment tree approach for range maximum queries.
     * Overkill for this problem but demonstrates advanced technique.
     */
    public int findMaxValueOfEquationSegmentTree(int[][] points, int k) {
        int n = points.length;
        if (n < 2) return Integer.MIN_VALUE;

        // Compress coordinates
        Set<Integer> uniqueX = new HashSet<>();
        for (int[] point : points) {
            uniqueX.add(point[0]);
        }

        List<Integer> sortedX = new ArrayList<>(uniqueX);
        Collections.sort(sortedX);

        Map<Integer, Integer> xToIndex = new HashMap<>();
        for (int i = 0; i < sortedX.size(); i++) {
            xToIndex.put(sortedX.get(i), i);
        }

        // Build segment tree for range maximum queries
        SegmentTree segTree = new SegmentTree(sortedX.size());
        int maxValue = Integer.MIN_VALUE;

        for (int[] point : points) {
            int x = point[0], y = point[1];
            int xIndex = xToIndex.get(x);

            // Find range of valid x coordinates [x-k, x-1]
            int leftBound = x - k;
            int rightBound = x - 1;

            // Binary search for range bounds in compressed coordinates
            int leftIndex = binarySearchLeft(sortedX, leftBound);
            int rightIndex = binarySearchRight(sortedX, rightBound);

            if (leftIndex <= rightIndex) {
                int maxYMinusX = segTree.query(leftIndex, rightIndex);
                if (maxYMinusX != Integer.MIN_VALUE) {
                    maxValue = Math.max(maxValue, maxYMinusX + y + x);
                }
            }

            // Update segment tree with current point
            segTree.update(xIndex, y - x);
        }

        return maxValue;
    }

    // Binary search helpers
    private int binarySearchLeft(List<Integer> arr, int target) {
        int left = 0, right = arr.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid) >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private int binarySearchRight(List<Integer> arr, int target) {
        int left = 0, right = arr.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid) <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }

    // Simple segment tree for range maximum queries
    private static class SegmentTree {
        int[] tree;
        int n;

        SegmentTree(int size) {
            n = size;
            tree = new int[4 * size];
            Arrays.fill(tree, Integer.MIN_VALUE);
        }

        void update(int pos, int value) {
            update(1, 0, n - 1, pos, value);
        }

        private void update(int node, int start, int end, int pos, int value) {
            if (start == end) {
                tree[node] = Math.max(tree[node], value);
            } else {
                int mid = (start + end) / 2;
                if (pos <= mid) {
                    update(2 * node, start, mid, pos, value);
                } else {
                    update(2 * node + 1, mid + 1, end, pos, value);
                }
                tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
            }
        }

        int query(int left, int right) {
            if (left > right) return Integer.MIN_VALUE;
            return query(1, 0, n - 1, left, right);
        }

        private int query(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                return Integer.MIN_VALUE;
            }
            if (left <= start && end <= right) {
                return tree[node];
            }
            int mid = (start + end) / 2;
            return Math.max(query(2 * node, start, mid, left, right),
                           query(2 * node + 1, mid + 1, end, left, right));
        }
    }

    /**
     * Optimized approach using monotonic deque with custom comparator.
     * Most efficient solution for this specific problem.
     */
    public int findMaxValueOfEquationOptimized(int[][] points, int k) {
        // Monotonic deque storing [yi - xi, xi] in decreasing order of yi - xi
        Deque<int[]> deque = new ArrayDeque<>();
        int maxValue = Integer.MIN_VALUE;

        for (int[] point : points) {
            int x = point[0], y = point[1];

            // Remove points outside valid range
            while (!deque.isEmpty() && x - deque.peekFirst()[1] > k) {
                deque.pollFirst();
            }

            // Calculate maximum value with current point
            if (!deque.isEmpty()) {
                maxValue = Math.max(maxValue, deque.peekFirst()[0] + y + x);
            }

            // Add current point maintaining monotonic property
            int currentValue = y - x;
            while (!deque.isEmpty() && deque.peekLast()[0] <= currentValue) {
                deque.pollLast();
            }

            deque.offerLast(new int[]{currentValue, x});
        }

        return maxValue;
    }

    /**
     * TreeMap approach for handling dynamic range queries.
     * Good for scenarios with frequent insertions and range queries.
     */
    public int findMaxValueOfEquationTreeMap(int[][] points, int k) {
        // TreeMap: x-coordinate -> max(yi - xi) for that x
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int maxValue = Integer.MIN_VALUE;

        for (int[] point : points) {
            int x = point[0], y = point[1];

            // Find maximum yi - xi in valid range [x-k, x-1]
            Map<Integer, Integer> validRange = map.subMap(x - k, true, x - 1, true);

            int maxYMinusX = Integer.MIN_VALUE;
            for (int value : validRange.values()) {
                maxYMinusX = Math.max(maxYMinusX, value);
            }

            if (maxYMinusX != Integer.MIN_VALUE) {
                maxValue = Math.max(maxValue, maxYMinusX + y + x);
            }

            // Update map with current point
            map.put(x, Math.max(map.getOrDefault(x, Integer.MIN_VALUE), y - x));
        }

        return maxValue;
    }
}