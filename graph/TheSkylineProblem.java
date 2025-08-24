package graph;

import java.util.*;

/**
 * 218. The Skyline Problem
 *
 * Problem: Given array of buildings [left, right, height], return the skyline's
 * key points. A key point is the left point where the skyline's height changes.
 *
 * Example:
 * Input: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * Output: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 *
 * LeetCode: https://leetcode.com/problems/the-skyline-problem
 *
 * Follow-up questions:
 * Q: How to handle buildings with same coordinates but different heights?
 * A: Process start events before end events, and higher buildings first.
 *
 * Q: Can we optimize for very dense building distributions?
 * A: Use coordinate compression and segment trees for range updates.
 *
 * Q: How to extend to 3D skyline problems?
 * A: Use sweep line in multiple dimensions with more complex data structures.
 */
public class TheSkylineProblem {

    /**
     * Sweep line algorithm using priority queue.
     *
     * Algorithm: Event-driven sweep line
     * - Create events for building starts and ends
     * - Sort events by x-coordinate with tie-breaking rules
     * - Use max heap to track active building heights
     * - When max height changes, record key point
     *
     * Time Complexity: O(n log n) where n is number of buildings
     * Space Complexity: O(n) for events and priority queue
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        // Create events for building starts and ends
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];

            events.add(new Event(left, height, EventType.START));
            events.add(new Event(right, height, EventType.END));
        }

        // Sort events
        Collections.sort(events);

        // Max heap for active building heights
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        maxHeap.offer(0); // Ground level

        int prevMaxHeight = 0;

        for (Event event : events) {
            if (event.type == EventType.START) {
                maxHeap.offer(event.height);
            } else {
                maxHeap.remove(event.height);
            }

            int currentMaxHeight = maxHeap.peek();
            if (currentMaxHeight != prevMaxHeight) {
                result.add(Arrays.asList(event.x, currentMaxHeight));
                prevMaxHeight = currentMaxHeight;
            }
        }

        return result;
    }

    // Event class for sweep line
    private static class Event implements Comparable<Event> {
        int x;
        int height;
        EventType type;

        Event(int x, int height, EventType type) {
            this.x = x;
            this.height = height;
            this.type = type;
        }

        @Override
        public int compareTo(Event other) {
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }

            // Tie-breaking rules for same x-coordinate
            if (this.type == EventType.START && other.type == EventType.START) {
                return Integer.compare(other.height, this.height); // Higher buildings first
            } else if (this.type == EventType.END && other.type == EventType.END) {
                return Integer.compare(this.height, other.height); // Lower buildings first
            } else if (this.type == EventType.START && other.type == EventType.END) {
                return -1; // Start events before end events
            } else {
                return 1; // End events after start events
            }
        }
    }

    private enum EventType {
        START, END
    }

    /**
     * Divide and conquer approach.
     * Recursively solve left and right halves, then merge skylines.
     */
    public List<List<Integer>> getSkylineDivideConquer(int[][] buildings) {
        if (buildings.length == 0) {
            return new ArrayList<>();
        }

        return divideConquer(buildings, 0, buildings.length - 1);
    }

    // Recursive divide and conquer
    private List<List<Integer>> divideConquer(int[][] buildings, int left, int right) {
        if (left == right) {
            // Single building
            int[] building = buildings[left];
            List<List<Integer>> result = new ArrayList<>();
            result.add(Arrays.asList(building[0], building[2])); // Start
            result.add(Arrays.asList(building[1], 0));           // End
            return result;
        }

        int mid = left + (right - left) / 2;
        List<List<Integer>> leftSkyline = divideConquer(buildings, left, mid);
        List<List<Integer>> rightSkyline = divideConquer(buildings, mid + 1, right);

        return mergeSkylines(leftSkyline, rightSkyline);
    }

    // Merge two skylines
    private List<List<Integer>> mergeSkylines(List<List<Integer>> left, List<List<Integer>> right) {
        List<List<Integer>> result = new ArrayList<>();
        int i = 0, j = 0;
        int leftHeight = 0, rightHeight = 0;

        while (i < left.size() && j < right.size()) {
            List<Integer> leftPoint = left.get(i);
            List<Integer> rightPoint = right.get(j);

            int x, maxHeight;

            if (leftPoint.get(0) < rightPoint.get(0)) {
                x = leftPoint.get(0);
                leftHeight = leftPoint.get(1);
                i++;
            } else if (leftPoint.get(0) > rightPoint.get(0)) {
                x = rightPoint.get(0);
                rightHeight = rightPoint.get(1);
                j++;
            } else {
                x = leftPoint.get(0);
                leftHeight = leftPoint.get(1);
                rightHeight = rightPoint.get(1);
                i++;
                j++;
            }

            maxHeight = Math.max(leftHeight, rightHeight);

            if (result.isEmpty() || result.get(result.size() - 1).get(1) != maxHeight) {
                result.add(Arrays.asList(x, maxHeight));
            }
        }

        // Add remaining points
        while (i < left.size()) {
            result.add(left.get(i++));
        }

        while (j < right.size()) {
            result.add(right.get(j++));
        }

        return result;
    }

    /**
     * TreeMap-based approach for cleaner height management.
     * Uses TreeMap to efficiently track height frequencies.
     */
    public List<List<Integer>> getSkylineTreeMap(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        for (int[] building : buildings) {
            events.add(new Event(building[0], building[2], EventType.START));
            events.add(new Event(building[1], building[2], EventType.END));
        }

        Collections.sort(events);

        // TreeMap to track height frequencies (descending order)
        TreeMap<Integer, Integer> heights = new TreeMap<>((a, b) -> Integer.compare(b, a));
        heights.put(0, 1); // Ground level

        int prevMaxHeight = 0;

        for (Event event : events) {
            if (event.type == EventType.START) {
                heights.put(event.height, heights.getOrDefault(event.height, 0) + 1);
            } else {
                int count = heights.get(event.height);
                if (count == 1) {
                    heights.remove(event.height);
                } else {
                    heights.put(event.height, count - 1);
                }
            }

            int currentMaxHeight = heights.firstKey();
            if (currentMaxHeight != prevMaxHeight) {
                result.add(Arrays.asList(event.x, currentMaxHeight));
                prevMaxHeight = currentMaxHeight;
            }
        }

        return result;
    }

    /**
     * Segment tree approach for coordinate compression.
     * Efficient for very dense coordinate spaces.
     */
    public List<List<Integer>> getSkylineSegmentTree(int[][] buildings) {
        // Coordinate compression
        Set<Integer> coords = new TreeSet<>();
        for (int[] building : buildings) {
            coords.add(building[0]);
            coords.add(building[1]);
        }

        List<Integer> sortedCoords = new ArrayList<>(coords);
        Map<Integer, Integer> coordToIndex = new HashMap<>();
        for (int i = 0; i < sortedCoords.size(); i++) {
            coordToIndex.put(sortedCoords.get(i), i);
        }

        // Segment tree for range maximum queries
        int n = sortedCoords.size();
        SegmentTree segTree = new SegmentTree(n);

        // Process buildings
        for (int[] building : buildings) {
            int left = coordToIndex.get(building[0]);
            int right = coordToIndex.get(building[1]) - 1; // Exclusive right
            if (right >= left) {
                segTree.update(left, right, building[2]);
            }
        }

        // Extract skyline points
        List<List<Integer>> result = new ArrayList<>();
        int prevHeight = 0;

        for (int i = 0; i < n - 1; i++) {
            int height = segTree.query(i);
            if (height != prevHeight) {
                result.add(Arrays.asList(sortedCoords.get(i), height));
                prevHeight = height;
            }
        }

        // Add final point to ground
        if (!result.isEmpty()) {
            result.add(Arrays.asList(sortedCoords.get(n - 1), 0));
        }

        return result;
    }

    // Segment tree with lazy propagation for range updates
    private static class SegmentTree {
        int[] tree;
        int[] lazy;
        int size;

        SegmentTree(int n) {
            size = n;
            tree = new int[4 * n];
            lazy = new int[4 * n];
        }

        void update(int left, int right, int val) {
            update(1, 0, size - 1, left, right, val);
        }

        private void update(int node, int start, int end, int left, int right, int val) {
            if (lazy[node] != 0) {
                tree[node] = Math.max(tree[node], lazy[node]);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], lazy[node]);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], lazy[node]);
                }
                lazy[node] = 0;
            }

            if (start > right || end < left) return;

            if (start >= left && end <= right) {
                tree[node] = Math.max(tree[node], val);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], val);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], val);
                }
                return;
            }

            int mid = (start + end) / 2;
            update(2 * node, start, mid, left, right, val);
            update(2 * node + 1, mid + 1, end, left, right, val);
            tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
        }

        int query(int pos) {
            return query(1, 0, size - 1, pos);
        }

        private int query(int node, int start, int end, int pos) {
            if (lazy[node] != 0) {
                tree[node] = Math.max(tree[node], lazy[node]);
                if (start != end) {
                    lazy[2 * node] = Math.max(lazy[2 * node], lazy[node]);
                    lazy[2 * node + 1] = Math.max(lazy[2 * node + 1], lazy[node]);
                }
                lazy[node] = 0;
            }

            if (start == end) {
                return tree[node];
            }

            int mid = (start + end) / 2;
            if (pos <= mid) {
                return query(2 * node, start, mid, pos);
            } else {
                return query(2 * node + 1, mid + 1, end, pos);
            }
        }
    }

    /**
     * Multiset approach using frequency counting.
     * Alternative implementation using manual multiset.
     */
    public List<List<Integer>> getSkylineMultiset(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<int[]> events = new ArrayList<>();

        for (int[] building : buildings) {
            events.add(new int[]{building[0], -building[2]}); // Start (negative for sorting)
            events.add(new int[]{building[1], building[2]});  // End
        }

        // Sort events
        events.sort((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]); // Start events (negative) come first
        });

        List<Integer> heights = new ArrayList<>();
        heights.add(0); // Ground level

        for (int[] event : events) {
            int x = event[0];
            int h = event[1];

            if (h < 0) {
                // Start event
                heights.add(-h);
                Collections.sort(heights, Collections.reverseOrder());
            } else {
                // End event
                heights.remove(Integer.valueOf(h));
            }

            int maxHeight = heights.get(0);

            if (result.isEmpty() || result.get(result.size() - 1).get(1) != maxHeight) {
                result.add(Arrays.asList(x, maxHeight));
            }
        }

        return result;
    }

    /**
     * Line sweep with coordinate compression for memory efficiency.
     * Optimized for sparse coordinate distributions.
     */
    public List<List<Integer>> getSkylineCompressed(int[][] buildings) {
        // Extract and compress coordinates
        Set<Integer> coordSet = new HashSet<>();
        for (int[] building : buildings) {
            coordSet.add(building[0]);
            coordSet.add(building[1]);
        }

        List<Integer> coords = new ArrayList<>(coordSet);
        Collections.sort(coords);

        Map<Integer, Integer> coordMap = new HashMap<>();
        for (int i = 0; i < coords.size(); i++) {
            coordMap.put(coords.get(i), i);
        }

        // Create height array
        int[] heights = new int[coords.size()];

        // Process each building
        for (int[] building : buildings) {
            int leftIdx = coordMap.get(building[0]);
            int rightIdx = coordMap.get(building[1]);

            for (int i = leftIdx; i < rightIdx; i++) {
                heights[i] = Math.max(heights[i], building[2]);
            }
        }

        // Extract skyline key points
        List<List<Integer>> result = new ArrayList<>();
        int prevHeight = 0;

        for (int i = 0; i < coords.size(); i++) {
            int currentHeight = i < heights.length ? heights[i] : 0;

            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(coords.get(i), currentHeight));
                prevHeight = currentHeight;
            }
        }

        // Ensure ending at ground level
        if (!result.isEmpty() && result.get(result.size() - 1).get(1) != 0) {
            int lastX = coords.get(coords.size() - 1);
            result.add(Arrays.asList(lastX, 0));
        }

        return result;
    }

    /**
     * Returns additional skyline statistics.
     * Extension providing area under skyline and other metrics.
     */
    public SkylineStats getSkylineStats(int[][] buildings) {
        List<List<Integer>> skyline = getSkyline(buildings);

        long totalArea = 0;
        int maxHeight = 0;
        int keyPoints = skyline.size();

        for (int i = 0; i < skyline.size() - 1; i++) {
            List<Integer> current = skyline.get(i);
            List<Integer> next = skyline.get(i + 1);

            int width = next.get(0) - current.get(0);
            int height = current.get(1);

            totalArea += (long) width * height;
            maxHeight = Math.max(maxHeight, height);
        }

        return new SkylineStats(skyline, totalArea, maxHeight, keyPoints);
    }

    // Statistics result class
    public static class SkylineStats {
        public final List<List<Integer>> skyline;
        public final long totalArea;
        public final int maxHeight;
        public final int keyPoints;

        public SkylineStats(List<List<Integer>> skyline, long totalArea, int maxHeight, int keyPoints) {
            this.skyline = skyline;
            this.totalArea = totalArea;
            this.maxHeight = maxHeight;
            this.keyPoints = keyPoints;
        }

        @Override
        public String toString() {
            return String.format("Area: %d, Max Height: %d, Key Points: %d", totalArea, maxHeight, keyPoints);
        }
    }
}