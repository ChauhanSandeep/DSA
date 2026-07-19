package trees.segmenttree;

import java.util.*;

/**
 * Problem: My Calendar III
 *
 * Book half-open events [start, end) and after each booking return the maximum
 * number of simultaneous events seen so far. The primary solution records start
 * and end deltas in sorted order and line-sweeps the prefix sums.
 *
 * Leetcode: https://leetcode.com/problems/my-calendar-iii/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Segment tree topic | Line sweep | TreeMap difference timeline
 *
 * Example:
 *   Input:  book [10,20], [50,60], [10,40], [5,15]
 *   Output: [1,1,2,3]
 *   Why:    after [5,15), the time range [10,15) is covered by three events.
 *
 * Follow-ups:
 *   1. How would you improve per-booking time for many events?
 *      Use a dynamic segment tree with lazy propagation over the coordinate range.
 *   2. How would you support cancellations?
 *      Apply the inverse deltas and update the segment tree or timeline counts.
 *   3. How would you handle arbitrary large timestamps sparsely?
 *      Use coordinate compression or dynamically allocated segment tree nodes.
 *   4. How would you return the time interval with max overlap?
 *      Track the best segment while sweeping or store argmax data in the segment tree.
 *
 * Related: My Calendar I (729), My Calendar II (731).
 */
public class MyCalendarThree {

    public static void main(String[] args) {
        MyCalendarThree calendar = new MyCalendarThree();
        int[][] bookings = { {10, 20}, {50, 60}, {10, 40}, {5, 15}, {5, 10}, {25, 55} };
        int[] expected = {1, 1, 2, 3, 3, 3};
        int[] output = new int[bookings.length];

        for (int i = 0; i < bookings.length; i++) {
            output[i] = calendar.book(bookings[i][0], bookings[i][1]);
        }
        System.out.printf("bookings=%s -> %s  expected=%s%n",
            Arrays.deepToString(bookings), Arrays.toString(output), Arrays.toString(expected));

        MyCalendarThree edge = new MyCalendarThree();
        System.out.printf("bookings=%s -> %d  expected=1%n",
            Arrays.deepToString(new int[][] {{1, 2}}), edge.book(1, 2));
    }


    private final TreeMap<Integer, Integer> timeline; // <Time, NumOfBookings>

        /**
     * Intuition: a booking only changes overlap counts at its start and end. A sorted
     * timeline of deltas lets every book call recompute the running active count in
     * chronological order.
     *
     * Algorithm:
     *   1. Initialize an empty TreeMap from time to delta count.
     *   2. Future bookings will add +1 at start and -1 at end.
     *   3. The sorted map order supports line sweeping.
     *
     * Time:  O(1) - only the map object is created.
     * Space: O(1) - no bookings have been stored yet.
     */
    public MyCalendarThree() {
        timeline = new TreeMap<>();
    }

        /**
     * Intuition: the maximum overlap is the maximum prefix sum of start/end deltas.
     * Adding +1 at startTime and -1 at endTime preserves half-open interval behavior
     * because an event ending at t is removed before later time ranges.
     *
     * Algorithm:
     *   1. Increment the delta at startTime.
     *   2. Decrement the delta at endTime.
     *   3. Sweep timeline values in sorted order while accumulating activeEvents.
     *   4. Return the largest activeEvents value seen.
     *
     * Time:  O(n) - each call sweeps all unique time points after O(log n) updates.
     * Space: O(n) - timeline stores unique start and end times.
     *
     * @param startTime inclusive event start
     * @param endTime exclusive event end
     * @return maximum number of overlapping booked events so far
     */
    public int book(int startTime, int endTime) {
        // Update timeline with event start (+1) and end (-1)
        timeline.put(startTime, timeline.getOrDefault(startTime, 0) + 1);
        timeline.put(endTime, timeline.getOrDefault(endTime, 0) - 1);

        // Sweep through timeline to find maximum overlap
        int activeEvents = 0;
        int maxBooking = 0;

        for (int delta : timeline.values()) {
            activeEvents += delta;
            maxBooking = Math.max(maxBooking, activeEvents);
        }

        return maxBooking;
    }

    /**
     * Alternative implementation using Segment Tree with Lazy Propagation.
     * Provides better performance for large coordinate ranges through coordinate compression.
     * However this might not be required in interview for implementation
     */
    public static class MyCalendarThreeSegmentTree {

        private final SegmentTreeLazyRange segmentTree;

        /**
         * Initializes the MyCalendarThree object using Segment Tree approach.
         *
         * Algorithm: Segment Tree with Lazy Propagation and Coordinate Compression
         * Step 1: Initialize segment tree covering the full coordinate range [0, 10^9]
         * Step 2: Use lazy propagation for efficient range updates
         * Step 3: Maintain maximum value across all ranges for k-booking calculation
         *
         * Time Complexity: O(1) for initialization
         * Space Complexity: O(log(max_coordinate)) for segment tree structure
         */
        public MyCalendarThreeSegmentTree() {
            segmentTree = new SegmentTreeLazyRange(0, 1000000000);
        }

        /**
         * Books a new event and returns the maximum k-booking after this event.
         *
         * Algorithm: Range Update with Maximum Query
         * Step 1: Perform range update [start, end-1] incrementing count by 1
         * Step 2: Use lazy propagation to efficiently update overlapping ranges
         * Step 3: Query entire range to find maximum overlapping count
         * Step 4: Return the maximum k-booking value from segment tree root
         *
         * Time Complexity: O(log n) where n is the coordinate range
         * Space Complexity: O(log n) for segment tree nodes created dynamically
         *
         * @param start the start time of the event (inclusive)
         * @param end the end time of the event (exclusive)
         * @return the maximum k-booking after adding this event
         */
        public int book(int start, int end) {
            // Step 1: Update range [start, end-1] with +1 (end is exclusive)
            segmentTree.updateRange(start, end - 1, 1);

            // Step 2: Query maximum value across entire range
            return segmentTree.getMaxValue();
        }
    }

    /**
     * Segment Tree with Lazy Propagation supporting range updates and maximum queries.
     * Uses dynamic node creation to handle large coordinate ranges efficiently.
     */
    private static class SegmentTreeLazyRange {
        private int start, end;
        private int value, lazyValue;
        private SegmentTreeLazyRange left, right;
        private int maxValue;

        /**
         * Creates segment tree node covering range [start, end].
         *
         * Algorithm: Dynamic Segment Tree Construction
         * Step 1: Initialize node with given range boundaries
         * Step 2: Set initial values to 0 (no events initially)
         * Step 3: Create child nodes lazily when needed for memory efficiency
         *
         * Time Complexity: O(1) per node creation
         * Space Complexity: O(1) per node
         */
        public SegmentTreeLazyRange(int start, int end) {
            this.start = start;
            this.end = end;
            this.value = 0;
            this.lazyValue = 0;
            this.maxValue = 0;
            this.left = null;
            this.right = null;
        }

        /**
         * Updates range [updateStart, updateEnd] by adding delta value.
         *
         * Algorithm: Lazy Propagation Range Update
         * Step 1: Push down any pending lazy updates to children
         * Step 2: Check for complete overlap, partial overlap, or no overlap
         * Step 3: For complete overlap, update current node and mark as lazy
         * Step 4: For partial overlap, create children if needed and recurse
         * Step 5: Update maximum value by combining children's maximum values
         *
         * Time Complexity: O(log n) where n is the range size
         * Space Complexity: O(log n) for created nodes in worst case
         */
        public void updateRange(int updateStart, int updateEnd, int delta) {
            // Step 1: Apply pending lazy updates
            pushLazy();

            if (updateEnd < start || updateStart > end) {
                return; // No overlap
            }

            // Step 3: Complete overlap - update current node
            if (updateStart <= start && end <= updateEnd) {
                lazyValue += delta;
                pushLazy();
                return;
            }

            // Step 4: Partial overlap - create children and recurse
            createChildren();
            left.updateRange(updateStart, updateEnd, delta);
            right.updateRange(updateStart, updateEnd, delta);

            // Step 5: Update maximum from children
            maxValue = Math.max(left.maxValue, right.maxValue);
        }

        /**
         * Returns the maximum value across the entire range.
         *
         * Algorithm: Maximum Value Query
         * Step 1: Apply any pending lazy updates
         * Step 2: Return the maximum value maintained at root
         *
         * Time Complexity: O(1) since we maintain max at root
         * Space Complexity: O(1)
         */
        public int getMaxValue() {
            pushLazy();
            return maxValue;
        }

        // Applies pending lazy updates and propagates to children
        private void pushLazy() {
            if (lazyValue != 0) {
                value += lazyValue;
                maxValue = Math.max(maxValue, value);

                if (left != null) {
                    left.lazyValue += lazyValue;
                    right.lazyValue += lazyValue;
                }

                lazyValue = 0;
            }
        }

        // Creates left and right children if they don't exist
        private void createChildren() {
            if (left == null) {
                int mid = start + (end - start) / 2;
                left = new SegmentTreeLazyRange(start, mid);
                right = new SegmentTreeLazyRange(mid + 1, end);
            }
        }
    }

    /**
     * Alternative implementation using coordinate compression for better space efficiency.
     * Suitable when dealing with very large coordinate ranges but limited number of events.
     */
    public static class MyCalendarThreeCompressed {

        private final List<int[]> events;

        /**
         * Initializes using coordinate compression approach.
         *
         * Algorithm: Event Storage with Coordinate Compression
         * Step 1: Store all events in a list for later processing
         * Step 2: Use coordinate compression during query to map to smaller range
         * Step 3: Apply line sweep on compressed coordinates
         *
         * Time Complexity: O(1) for initialization
         * Space Complexity: O(1) initially
         */
        public MyCalendarThreeCompressed() {
            events = new ArrayList<>();
        }

        /**
         * Books a new event and returns the maximum k-booking.
         *
         * Algorithm: Coordinate Compression with Line Sweep
         * Step 1: Add new event to events list
         * Step 2: Extract all unique coordinates (starts and ends)
         * Step 3: Sort coordinates and create compression mapping
         * Step 4: Apply difference array technique on compressed coordinates
         * Step 5: Perform line sweep to find maximum overlapping count
         *
         * Time Complexity: O(n^2) where n is number of events
         * - O(n) to extract coordinates
         * - O(n log n) to sort coordinates
         * - O(n^2) to process all events against all coordinates
         * Space Complexity: O(n) for storing events and coordinates
         *
         * @param start the start time of the event (inclusive)
         * @param end the end time of the event (exclusive)
         * @return the maximum k-booking after adding this event
         */
        public int book(int start, int end) {
            // Step 1: Add new event to collection
            events.add(new int[]{start, end});

            // Step 2: Extract and sort all unique coordinates
            Set<Integer> coordinateSet = new TreeSet<>();
            for (int[] event : events) {
                coordinateSet.add(event[0]); // start time
                coordinateSet.add(event[1]); // end time
            }

            List<Integer> coordinates = new ArrayList<>(coordinateSet);

            // Step 3: Create difference array for compressed coordinates
            int[] diff = new int[coordinates.size()];

            // Step 4: Apply events to difference array
            for (int[] event : events) {
                int startIdx = Collections.binarySearch(coordinates, event[0]);
                int endIdx = Collections.binarySearch(coordinates, event[1]);

                diff[startIdx]++;     // Event starts
                if (endIdx < diff.length) {
                    diff[endIdx]--;   // Event ends
                }
            }

            // Step 5: Line sweep to find maximum overlap
            int currentOverlap = 0;
            int maxOverlap = 0;

            for (int delta : diff) {
                currentOverlap += delta;
                maxOverlap = Math.max(maxOverlap, currentOverlap);
            }

            return maxOverlap;
        }
    }
}

