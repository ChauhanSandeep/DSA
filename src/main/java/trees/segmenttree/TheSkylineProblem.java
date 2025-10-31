package trees.segmenttree;

import java.util.*;


/**
 * The Skyline Problem
 *
 * Problem Statement:
 * Given the locations and heights of all buildings in a city, compute the skyline formed by these buildings.
 * Each building is represented as [left, right, height] where left and right are x-coordinates of building edges.
 * Return key points where the skyline height changes as [[x1,y1],[x2,y2],...] sorted by x-coordinate.
 *
 * Example:
 * Input: buildings = [
 *      [2,  9,  10],
 *      [3,  7,  15],
 *      [5,  12, 12],
 *      [15, 20, 10],
 *      [19, 24, 8]
 * ]
 * Output: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 * Explanation: At x=2, building starts with height 10. At x=3, taller building (height 15) starts.
 * At x=7, the tallest building ends, height drops to 12. Pattern continues until all buildings end.
 *
 * LeetCode Link: https://leetcode.com/problems/the-skyline-problem
 *
 * Follow-up Questions:
 * 1. How would you handle buildings with the same coordinates but different heights?
 *    Answer: Process all building starts before ends at same x-coordinate, and sort by height appropriately.
 * 2. What if we need to find skyline for 3D buildings (with depth)?
 *    Answer: Extend to 3D sweep plane algorithm, maintaining max height for each (x,z) coordinate.
 * 3. How to optimize for very sparse buildings across large coordinate range?
 *    Answer: Use coordinate compression to map x-coordinates to smaller consecutive range.
 * 4. What's the space-time tradeoff between different approaches?
 *    Answer: Segment tree uses more space (4n) but provides flexibility for range operations and updates.
 */
public class TheSkylineProblem {

    /**
     * Returns the skyline formed by the given buildings using sweep line with priority queue.
     *
     * Algorithm: Sweep Line with Priority Queue
     * Step 1: Create events for building starts and ends, sort by x-coordinate
     * Step 2: Process events left to right, maintaining active building heights in max heap
     * Step 3: At each x-coordinate, update heap and check if max height changed
     * Step 4: Add key point to result only when skyline height changes
     * Step 5: Handle edge cases like overlapping buildings and same x-coordinates
     *
     * Time Complexity: O(n log n) where n is number of buildings
     * Space Complexity: O(n) for events list and heap storage
     *
     * @param buildings array of buildings where each building is [left, right, height]
     * @return list of key points representing skyline as [x, height] pairs
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        if (buildings == null || buildings.length == 0) {
            return result;
        }

        List<Event> events = createEvents(buildings);
        Collections.sort(events);

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.offer(0);

        int currHeight = 0;

        for (int i = 0; i < events.size(); ) {
            int currentX = events.get(i).x;

            // While there are events at the same x-coordinate, process them.
            // Start events add heights, end events remove heights.
            while (i < events.size() && events.get(i).x == currentX) {
                Event event = events.get(i);
                if (event.isStart) {
                    maxHeap.offer(event.height);
                } else {
                    maxHeap.remove(event.height);
                }
                i++;
            }

            // At this point, we have processed all events at currentX.
            // The current max height is the top of the max heap.
            int newHeight = maxHeap.peek();
            if (newHeight != currHeight) {
                result.add(Arrays.asList(currentX, newHeight));
                currHeight = newHeight;
            }
        }

        return result;
    }

    /**
     * Segment Tree approach with coordinate compression for handling large coordinate ranges.
     *
     * Algorithm: Segment Tree with Lazy Propagation and Coordinate Compression
     * Step 1: Extract all unique x-coordinates from buildings and compress them to 0..k-1 range
     * Step 2: Build segment tree supporting range maximum update and point/range maximum queries
     * Step 3: Process buildings in descending height order to handle overlapping correctly
     * Step 4: For each building, perform range update on compressed coordinate range
     * Step 5: Query segment tree at all coordinate boundaries to detect height changes
     * Step 6: Map compressed coordinates back to original values for result
     *
     * Time Complexity: O(n log n + k log k) where n is buildings, k is unique coordinates
     * - O(n log n) for sorting buildings by height
     * - O(k log k) for segment tree operations across k coordinates
     * Space Complexity: O(k) for coordinate compression and segment tree storage
     *
     * @param buildings array of buildings where each building is [left, right, height]
     * @return list of key points representing skyline as [x, height] pairs
     */
    public List<List<Integer>> getSkylineSegmentTree(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        if (buildings == null || buildings.length == 0) {
            return result;
        }

        // Step 1: Coordinate compression - collect all unique x-coordinates
        Set<Integer> coordinateSet = new TreeSet<>();
        for (int[] building : buildings) {
            coordinateSet.add(building[0]); // left boundary
            coordinateSet.add(building[1]); // right boundary
        }

        List<Integer> coordinates = new ArrayList<>(coordinateSet);
        Map<Integer, Integer> coordinateIndex = new HashMap<>();
        for (int i = 0; i < coordinates.size(); i++) {
            coordinateIndex.put(coordinates.get(i), i);
        }

        // Step 2: Build segment tree for range updates and queries
        SegmentTreeLazy segTree = new SegmentTreeLazy(coordinates.size());

        // Step 3: Sort buildings by height in descending order for proper overlap handling
        Arrays.sort(buildings, (a, b) -> Integer.compare(b[2], a[2]));

        // Step 4: Process each building with range update on segment tree
        for (int[] building : buildings) {
            int leftIdx = coordinateIndex.get(building[0]);
            int rightIdx = coordinateIndex.get(building[1]);
            int height = building[2];

            // Update range [leftIdx, rightIdx) with maximum height
            if (leftIdx < rightIdx) {
                segTree.updateRange(leftIdx, rightIdx - 1, height);
            }
        }

        // Step 5: Query segment tree to find skyline key points
        int prevHeight = 0;
        for (int i = 0; i < coordinates.size(); i++) {
            int currentHeight = segTree.queryPoint(i);

            // Step 6: Add key point when height changes
            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(coordinates.get(i), currentHeight));
                prevHeight = currentHeight;
            }
        }

        return result;
    }

    // Creates events for building starts and ends with proper sorting priority
    private List<Event> createEvents(int[][] buildings) {
        List<Event> events = new ArrayList<>();

        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];

            events.add(new Event(left, height, true));
            events.add(new Event(right, height, false));
        }

        return events;
    }

    /**
     * Event class representing building start or end at specific x-coordinate.
     */
    private static class Event implements Comparable<Event> {
        final int x;
        final int height;
        final boolean isStart;

        Event(int x, int height, boolean isStart) {
            this.x = x;
            this.height = height;
            this.isStart = isStart;
        }

        @Override
        public int compareTo(Event other) {
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }

            if (this.isStart && other.isStart) {
                // if both are start events, higher height first
                return Integer.compare(other.height, this.height);
            } else if (!this.isStart && !other.isStart) {
                // if both are end events, lower height first
                return Integer.compare(this.height, other.height);
            } else {
                // if one is start and other is end, start event first
                return this.isStart ? -1 : 1;
            }
        }
    }

    /**
     * Segment Tree with Lazy Propagation supporting range maximum updates.
     * Maintains the maximum value for each range efficiently.
     */
    private static class SegmentTreeLazy {
        private final int[] tree;
        private final int[] lazy;
        private final int size;

        public SegmentTreeLazy(int size) {
            this.size = size;
            this.tree = new int[4 * size];
            this.lazy = new int[4 * size];
        }

        /**
         * Updates range [left, right] with maximum of current value and newValue.
         *
         * Algorithm: Lazy Propagation Range Update
         * Step 1: Handle lazy propagation from parent nodes
         * Step 2: Check for complete overlap, partial overlap, or no overlap
         * Step 3: For complete overlap, update current node and mark children as lazy
         * Step 4: For partial overlap, recursively update children and merge results
         * Step 5: Update current node value from children after recursive calls
         *
         * Time Complexity: O(log n) per update
         */
        public void updateRange(int left, int right, int value) {
            updateRange(0, 0, size - 1, left, right, value);
        }

        private void updateRange(int node, int start, int end, int left, int right, int value) {
            // Step 1: Apply lazy propagation
            pushLazy(node, start, end);

            if (start > right || end < left) {
                return; // No overlap
            }

            // Step 3: Complete overlap - update with maximum value
            if (left <= start && end <= right) {
                lazy[node] = Math.max(lazy[node], value);
                pushLazy(node, start, end);
                return;
            }

            // Step 4: Partial overlap - recursively update children
            int mid = start + (end - start) / 2;
            updateRange(2 * node + 1, start, mid, left, right, value);
            updateRange(2 * node + 2, mid + 1, end, left, right, value);

            // Step 5: Update current node from children
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }

        /**
         * Queries the maximum value at a specific point.
         *
         * Algorithm: Point Query with Lazy Propagation
         * Step 1: Apply pending lazy updates along path to target point
         * Step 2: Traverse down tree following path to target index
         * Step 3: Return value at leaf node representing the point
         *
         * Time Complexity: O(log n) per query
         */
        public int queryPoint(int index) {
            return queryPoint(0, 0, size - 1, index);
        }

        private int queryPoint(int node, int start, int end, int index) {
            // Step 1: Apply lazy propagation
            pushLazy(node, start, end);

            if (start == end) {
                // Step 3: Reached target point
                return tree[node];
            }

            // Step 2: Navigate to appropriate child
            int mid = start + (end - start) / 2;
            if (index <= mid) {
                return queryPoint(2 * node + 1, start, mid, index);
            } else {
                return queryPoint(2 * node + 2, mid + 1, end, index);
            }
        }

        // Apply lazy updates and propagate to children
        private void pushLazy(int node, int start, int end) {
            if (lazy[node] != 0) {
                tree[node] = Math.max(tree[node], lazy[node]);

                if (start != end) {
                    // Propagate to children
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], lazy[node]);
                    lazy[2 * node + 2] = Math.max(lazy[2 * node + 2], lazy[node]);
                }

                lazy[node] = 0;
            }
        }
    }
}

/**
 * Usage Example:
 * TheSkylineProblem solution = new TheSkylineProblem();
 * List<List<Integer>> skyline = solution.getSkylineSegmentTree(buildings);
 */
