package greedy;

import java.util.*;

/**
 * 57. Insert Interval
 *
 * Problem: Given a set of non-overlapping intervals sorted by start time,
 * insert a new interval and merge if necessary.
 *
 * Example:
 * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
 * Output: [[1,5],[6,9]]
 *
 * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 * Output: [[1,2],[3,10],[12,16]]
 *
 * LeetCode: https://leetcode.com/problems/insert-interval
 *
 * Follow-up questions:
 * Q: How to handle very large arrays of intervals efficiently?
 * A: Use segment trees or interval trees for logarithmic operations.
 *
 * Q: What if intervals can have different types or priorities?
 * A: Extend with metadata and custom merge rules.
 *
 * Q: How to support batch insertions of multiple intervals?
 * A: Use sweep line algorithms or bulk insertion optimizations.
 */
public class InsertInterval {

    /**
     * Linear scan approach - optimal for sorted intervals.
     *
     * Algorithm: Three-phase processing
     * - Phase 1: Add all intervals that end before newInterval starts
     * - Phase 2: Merge all overlapping intervals with newInterval
     * - Phase 3: Add all intervals that start after merged interval ends
     *
     * Time Complexity: O(n) where n is number of intervals
     * Space Complexity: O(n) for result list
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        int length = intervals.length;

        // Phase 1: Add intervals that end before newInterval starts
        while (i < length && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // Phase 2: Merge overlapping intervals - until end of new interval is greater than start of next interval
        while(i<length && newInterval[1] >= intervals[i][0]) {
            // start of merged interval is min of starts of overlapping intervals
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            // end of merged interval is max of ends of overlapping intervals
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);

        // Phase 3: Add remaining intervals
        while (i < length) {
            result.add(intervals[i]);
            i++;
        }

        return result.toArray(new int[result.size()][]);
    }

    /**
     * Binary search optimization for insertion point.
     * More efficient when most intervals don't overlap with new interval.
     */
    public int[][] insertBinarySearch(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();

        // Find insertion point using binary search
        int left = findInsertPosition(intervals, newInterval[0]);
        int right = findMergeEnd(intervals, newInterval[1]);

        // Add intervals before merge region
        for (int i = 0; i < left; i++) {
            result.add(intervals[i]);
        }

        // Create merged interval
        int mergedStart = newInterval[0];
        int mergedEnd = newInterval[1];

        if (left < intervals.length && intervals[left][0] <= newInterval[1]) {
            mergedStart = Math.min(mergedStart, intervals[left][0]);
        }

        if (right >= 0 && intervals[right][1] >= newInterval[0]) {
            mergedEnd = Math.max(mergedEnd, intervals[right][1]);
        }

        result.add(new int[]{mergedStart, mergedEnd});

        // Add intervals after merge region
        for (int i = right + 1; i < intervals.length; i++) {
            result.add(intervals[i]);
        }

        return result.toArray(new int[result.size()][]);
    }

    // Find position where interval should be inserted
    private int findInsertPosition(int[][] intervals, int start) {
        int left = 0, right = intervals.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (intervals[mid][1] < start) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    // Find end of merge region
    private int findMergeEnd(int[][] intervals, int end) {
        int left = 0, right = intervals.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (intervals[mid][0] <= end) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    /**
     * In-place modification approach.
     * Minimizes memory allocation for better performance.
     */
    public int[][] insertInPlace(int[][] intervals, int[] newInterval) {
        if (intervals.length == 0) {
            return new int[][]{newInterval};
        }

        List<int[]> merged = new ArrayList<>();
        boolean inserted = false;

        for (int[] interval : intervals) {
            if (interval[1] < newInterval[0]) {
                // Non-overlapping, before new interval
                merged.add(interval);
            } else if (interval[0] > newInterval[1]) {
                // Non-overlapping, after new interval
                if (!inserted) {
                    merged.add(newInterval);
                    inserted = true;
                }
                merged.add(interval);
            } else {
                // Overlapping, merge with new interval
                newInterval[0] = Math.min(newInterval[0], interval[0]);
                newInterval[1] = Math.max(newInterval[1], interval[1]);
            }
        }

        if (!inserted) {
            merged.add(newInterval);
        }

        return merged.toArray(new int[merged.size()][]);
    }

    /**
     * Interval tree approach for frequent insertions.
     * Efficient for multiple insertion operations.
     */
    public static class IntervalTree {
        private IntervalNode root;

        public void insert(int[] interval) {
            root = insertHelper(root, interval);
        }

        private IntervalNode insertHelper(IntervalNode node, int[] interval) {
            if (node == null) {
                return new IntervalNode(interval);
            }

            // Insert based on start time
            if (interval[0] < node.interval[0]) {
                node.left = insertHelper(node.left, interval);
            } else {
                node.right = insertHelper(node.right, interval);
            }

            // Update max end time
            node.maxEnd = Math.max(node.maxEnd, interval[1]);
            if (node.left != null) {
                node.maxEnd = Math.max(node.maxEnd, node.left.maxEnd);
            }
            if (node.right != null) {
                node.maxEnd = Math.max(node.maxEnd, node.right.maxEnd);
            }

            return node;
        }

        public List<int[]> getAllIntervals() {
            List<int[]> result = new ArrayList<>();
            inorderTraversal(root, result);
            return result;
        }

        private void inorderTraversal(IntervalNode node, List<int[]> result) {
            if (node != null) {
                inorderTraversal(node.left, result);
                result.add(node.interval);
                inorderTraversal(node.right, result);
            }
        }

        // Find all intervals that overlap with given interval
        public List<int[]> findOverlapping(int[] interval) {
            List<int[]> result = new ArrayList<>();
            findOverlappingHelper(root, interval, result);
            return result;
        }

        private void findOverlappingHelper(IntervalNode node, int[] interval, List<int[]> result) {
            if (node == null) return;

            // Check if current interval overlaps
            if (intervalsOverlap(node.interval, interval)) {
                result.add(node.interval);
            }

            // Search left subtree if it might contain overlapping intervals
            if (node.left != null && node.left.maxEnd >= interval[0]) {
                findOverlappingHelper(node.left, interval, result);
            }

            // Search right subtree if it might contain overlapping intervals
            if (node.right != null && node.interval[0] <= interval[1]) {
                findOverlappingHelper(node.right, interval, result);
            }
        }

        private boolean intervalsOverlap(int[] a, int[] b) {
            return Math.max(a[0], b[0]) <= Math.min(a[1], b[1]);
        }

        private static class IntervalNode {
            int[] interval;
            int maxEnd;
            IntervalNode left, right;

            IntervalNode(int[] interval) {
                this.interval = interval;
                this.maxEnd = interval[1];
            }
        }
    }

    /**
     * Segment tree approach for range-based interval operations.
     * Efficient for coordinate compression and bulk operations.
     */
    public int[][] insertSegmentTree(int[][] intervals, int[] newInterval) {
        // Coordinate compression
        Set<Integer> coords = new TreeSet<>();
        for (int[] interval : intervals) {
            coords.add(interval[0]);
            coords.add(interval[1]);
        }
        coords.add(newInterval[0]);
        coords.add(newInterval[1]);

        List<Integer> sortedCoords = new ArrayList<>(coords);
        Map<Integer, Integer> coordMap = new HashMap<>();
        for (int i = 0; i < sortedCoords.size(); i++) {
            coordMap.put(sortedCoords.get(i), i);
        }

        // Build segment tree
        SegmentTree segTree = new SegmentTree(sortedCoords.size());

        // Mark existing intervals
        for (int[] interval : intervals) {
            int start = coordMap.get(interval[0]);
            int end = coordMap.get(interval[1]);
            segTree.update(start, end);
        }

        // Insert new interval
        int newStart = coordMap.get(newInterval[0]);
        int newEnd = coordMap.get(newInterval[1]);
        segTree.update(newStart, newEnd);

        // Extract merged intervals
        List<int[]> result = segTree.extractIntervals(sortedCoords);
        return result.toArray(new int[result.size()][]);
    }

    // Simplified segment tree for interval marking
    private static class SegmentTree {
        boolean[] marked;
        int size;

        SegmentTree(int size) {
            this.size = size;
            this.marked = new boolean[size];
        }

        void update(int start, int end) {
            for (int i = start; i <= end && i < size; i++) {
                marked[i] = true;
            }
        }

        List<int[]> extractIntervals(List<Integer> coords) {
            List<int[]> intervals = new ArrayList<>();
            int start = -1;

            for (int i = 0; i < marked.length; i++) {
                if (marked[i] && start == -1) {
                    start = i;
                } else if (!marked[i] && start != -1) {
                    intervals.add(new int[]{coords.get(start), coords.get(i - 1)});
                    start = -1;
                }
            }

            if (start != -1) {
                intervals.add(new int[]{coords.get(start), coords.get(marked.length - 1)});
            }

            return intervals;
        }
    }

    /**
     * Batch insertion approach for multiple intervals.
     * Optimized for inserting multiple intervals at once.
     */
    public int[][] insertBatch(int[][] intervals, int[][] newIntervals) {
        List<int[]> allIntervals = new ArrayList<>();

        // Add existing intervals
        for (int[] interval : intervals) {
            allIntervals.add(interval);
        }

        // Add new intervals
        for (int[] interval : newIntervals) {
            allIntervals.add(interval);
        }

        // Sort by start time
        allIntervals.sort((a, b) -> Integer.compare(a[0], b[0]));

        // Merge overlapping intervals
        return mergeIntervals(allIntervals);
    }

    // Helper method to merge overlapping intervals
    private int[][] mergeIntervals(List<int[]> intervals) {
        if (intervals.isEmpty()) return new int[0][];

        List<int[]> merged = new ArrayList<>();
        int[] current = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            int[] next = intervals.get(i);

            if (current[1] >= next[0]) {
                // Merge intervals
                current[1] = Math.max(current[1], next[1]);
            } else {
                // Non-overlapping
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);
        return merged.toArray(new int[merged.size()][]);
    }

    /**
     * Priority queue approach for streaming interval insertion.
     * Handles intervals arriving in arbitrary order.
     */
    public static class StreamingIntervalInserter {
        private PriorityQueue<int[]> minHeap; // By start time
        private PriorityQueue<int[]> maxHeap; // By end time
        private List<int[]> merged;

        public StreamingIntervalInserter() {
            this.minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
            this.maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[1], a[1]));
            this.merged = new ArrayList<>();
        }

        public void insert(int[] interval) {
            minHeap.offer(interval);

            // Process intervals that can be merged
            processPendingIntervals();
        }

        private void processPendingIntervals() {
            List<int[]> toProcess = new ArrayList<>();

            while (!minHeap.isEmpty()) {
                toProcess.add(minHeap.poll());
            }

            if (toProcess.isEmpty()) return;

            // Sort by start time
            toProcess.sort((a, b) -> Integer.compare(a[0], b[0]));

            // Merge with existing intervals
            merged.addAll(toProcess);
            merged.sort((a, b) -> Integer.compare(a[0], b[0]));

            // Merge overlapping intervals
            List<int[]> newMerged = new ArrayList<>();
            int[] current = merged.get(0);

            for (int i = 1; i < merged.size(); i++) {
                int[] next = merged.get(i);

                if (current[1] >= next[0]) {
                    current[1] = Math.max(current[1], next[1]);
                } else {
                    newMerged.add(current);
                    current = next;
                }
            }

            newMerged.add(current);
            merged = newMerged;
        }

        public List<int[]> getMergedIntervals() {
            return new ArrayList<>(merged);
        }
    }

    /**
     * Generic interval operations utility class.
     * Provides various interval manipulation methods.
     */
    public static class IntervalUtils {

        // Check if two intervals overlap
        public static boolean overlap(int[] a, int[] b) {
            return Math.max(a[0], b[0]) <= Math.min(a[1], b[1]);
        }

        // Merge two overlapping intervals
        public static int[] merge(int[] a, int[] b) {
            if (!overlap(a, b)) {
                throw new IllegalArgumentException("Intervals do not overlap");
            }

            return new int[]{Math.min(a[0], b[0]), Math.max(a[1], b[1])};
        }

        // Find intersection of two intervals
        public static int[] intersect(int[] a, int[] b) {
            int start = Math.max(a[0], b[0]);
            int end = Math.min(a[1], b[1]);

            return (start <= end) ? new int[]{start, end} : null;
        }

        // Calculate total covered length
        public static int totalLength(int[][] intervals) {
            if (intervals.length == 0) return 0;

            int[][] merged = mergeOverlapping(intervals);
            int total = 0;

            for (int[] interval : merged) {
                total += interval[1] - interval[0] + 1;
            }

            return total;
        }

        // Merge all overlapping intervals
        public static int[][] mergeOverlapping(int[][] intervals) {
            if (intervals.length <= 1) return intervals;

            Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

            List<int[]> merged = new ArrayList<>();
            int[] current = intervals[0];

            for (int i = 1; i < intervals.length; i++) {
                if (current[1] >= intervals[i][0]) {
                    current[1] = Math.max(current[1], intervals[i][1]);
                } else {
                    merged.add(current);
                    current = intervals[i];
                }
            }

            merged.add(current);
            return merged.toArray(new int[merged.size()][]);
        }

        // Find gaps between intervals
        public static int[][] findGaps(int[][] intervals, int rangeStart, int rangeEnd) {
            int[][] merged = mergeOverlapping(intervals);
            List<int[]> gaps = new ArrayList<>();

            // Gap before first interval
            if (merged.length > 0 && rangeStart < merged[0][0]) {
                gaps.add(new int[]{rangeStart, merged[0][0] - 1});
            }

            // Gaps between intervals
            for (int i = 0; i < merged.length - 1; i++) {
                if (merged[i][1] + 1 < merged[i + 1][0]) {
                    gaps.add(new int[]{merged[i][1] + 1, merged[i + 1][0] - 1});
                }
            }

            // Gap after last interval
            if (merged.length > 0 && merged[merged.length - 1][1] < rangeEnd) {
                gaps.add(new int[]{merged[merged.length - 1][1] + 1, rangeEnd});
            }

            return gaps.toArray(new int[gaps.size()][]);
        }
    }
}