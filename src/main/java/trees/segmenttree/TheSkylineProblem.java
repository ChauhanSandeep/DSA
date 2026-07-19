package trees.segmenttree;

import java.util.*;


/**
 * Problem: The Skyline Problem
 *
 * Given buildings as [left, right, height], return the key points where the outer
 * skyline height changes. Overlapping buildings require tracking the tallest
 * active building at each x-coordinate.
 *
 * Leetcode: https://leetcode.com/problems/the-skyline-problem/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Segment tree topic | Sweep line | Max heap of active heights
 *
 * Example:
 *   Input:  buildings = [[2,9,10],[3,7,15],[5,12,12]]
 *   Output: [[2,10],[3,15],[7,12],[12,0]]
 *   Why:    the visible height changes at starts or ends of buildings only.
 *
 * Follow-ups:
 *   1. How would you handle many buildings with the same x-coordinate?
 *      Process all events at that x before deciding whether height changed.
 *   2. How would you avoid heap remove cost?
 *      Use a TreeMap height multiset for O(log n) deletes.
 *   3. How would you support online building insertions?
 *      Use a segment tree over compressed coordinates with range max updates.
 *   4. How would this extend to 3D city blocks?
 *      Sweep one dimension and maintain a 2D structure over the other footprint.
 *
 * Related: Meeting Rooms II (253), My Calendar III (732).
 */
public class TheSkylineProblem {

    public static void main(String[] args) {
        TheSkylineProblem solver = new TheSkylineProblem();
        int[][] buildings = {
            {2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}
        };
        int[][] single = { {1, 2, 1} };

        System.out.printf("buildings=%s -> %s  expected=[[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]%n",
            Arrays.deepToString(buildings), solver.getSkyline(buildings));
        System.out.printf("buildings=%s -> %s  expected=[[1, 1], [2, 0]]%n",
            Arrays.deepToString(single), solver.getSkyline(single));
    }


        /**
     * Intuition: skyline changes can only happen when a building starts or ends. Sweep
     * events from left to right while a max heap stores active heights; when the heap
     * top changes, the visible skyline height changed.
     *
     * Algorithm:
     *   1. Convert buildings into start and end events.
     *   2. Sort events by x, with starts before ends at the same x.
     *   3. Process all events sharing the same x and update the max heap.
     *   4. Add a key point whenever the current max height differs from currHeight.
     *
     * Time:  O(n log n) - sorting events and heap updates dominate.
     * Space: O(n) - events and active heights are stored.
     *
     * @param buildings array of [left, right, height] buildings
     * @return skyline key points as [x, height] pairs
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        if (buildings == null || buildings.length == 0) {
            return result;
        }

        List<Event> events = createEvents(buildings);
        Collections.sort(events, (e1, e2) -> {
            if (e1.x != e2.x) {
                return Integer.compare(e1.x, e2.x);
            }
            // For same x, process start events before end events
            if (e1.isStart && e2.isStart) {
                return Integer.compare(e2.height, e1.height); // Higher height first
            }
            if (!e1.isStart && !e2.isStart) {
                return Integer.compare(e1.height, e2.height); // Lower height first
            }
            return e1.isStart ? -1 : 1; // Start event before end event
        });

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
     * Creates start and end events for every building.
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
            // If at same x-coordinate, process starts before ends
            if (this.isStart != other.isStart) {
                return this.isStart ? -1 : 1;
            }
            // For starts, taller buildings first; for ends, shorter buildings first
            return this.isStart ?
                Integer.compare(other.height, this.height) :
                Integer.compare(this.height, other.height);
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
