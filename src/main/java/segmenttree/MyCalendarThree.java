package segmenttree;

import java.util.*;

/**
 * My Calendar III
 *
 * Problem Statement:
 * A k-booking happens when k events have some non-empty intersection (i.e., there is some time
 * that is common to all k events). You are given some events [start, end), after each given
 * event, return an integer k representing the maximum k-booking between all the previous events.
 *
 * Implement the MyCalendarThree class:
 * - MyCalendarThree() Initializes the object.
 * - int book(int start, int end) Returns the largest integer k such that there exists a k-booking.
 *
 * Example:
 * Input: ["MyCalendarThree", "book", "book", "book", "book", "book", "book"]
 *        [[], [10,20], [50,60], [10,40], [5,15], [5,10], [25,55]]
 * Output: [null, 1, 1, 2, 3, 3, 3]
 * Explanation:
 * book(10,20) -> 1 (first event, no overlap)
 * book(50,60) -> 1 (second event, no overlap with first)
 * book(10,40) -> 2 (overlaps with [10,20), max overlap is 2)
 * book(5,15) -> 3 (overlaps with previous events, max overlap is 3)
 *
 * LeetCode Link: https://leetcode.com/problems/my-calendar-iii
 *
 * Follow-up Questions:
 * 1. How would you optimize for very large coordinate ranges (0 to 10^9)?
 *    Answer: Use coordinate compression to map coordinates to smaller consecutive ranges.
 * 2. What if we need to support event deletion as well?
 *    Answer: Use segment tree with range updates and maintain counts that can be decremented.
 * 3. How to handle real-time queries with millions of events?
 *    Answer: Use persistent segment trees or fractional cascading for time-travel queries.
 *    Related: https://leetcode.com/problems/my-calendar-ii/
 * 4. What's the space-time tradeoff between TreeMap and Segment Tree approaches?
 *    Answer: TreeMap uses O(n) space with O(n log n) per query, Segment Tree uses O(range) space with O(log range) per query.
 */
public class MyCalendarThree {

    private final TreeMap<Integer, Integer> timeline;

    /**
     * Initializes the MyCalendarThree object using TreeMap approach.
     *
     * Algorithm: Difference Array with TreeMap
     * Step 1: Initialize TreeMap to maintain sorted timeline of events
     * Step 2: Use difference array concept where each start increments count and each end decrements
     * Step 3: TreeMap automatically maintains sorted order for efficient range processing
     *
     * Time Complexity: O(1) for initialization
     * Space Complexity: O(1) initially, grows to O(n) where n is number of unique time points
     */
    public MyCalendarThree() {
        timeline = new TreeMap<>();
    }

    /**
     * Books a new event and returns the maximum k-booking after this event.
     *
     * Algorithm: Line Sweep with Difference Array
     * Step 1: Add start time to timeline with +1 (event begins)
     * Step 2: Add end time to timeline with -1 (event ends)
     * Step 3: Perform line sweep through sorted timeline computing prefix sums
     * Step 4: Track maximum prefix sum encountered (maximum overlapping events)
     * Step 5: Return the maximum k-booking value
     *
     * Time Complexity: O(n log n) where n is number of unique time points
     * - O(log n) for TreeMap insertions
     * - O(n) for iterating through all time points to find maximum
     * Space Complexity: O(n) for storing unique time points in TreeMap
     *
     * @param start the start time of the event (inclusive)
     * @param end the end time of the event (exclusive)
     * @return the maximum k-booking after adding this event
     */
    public int book(int start, int end) {
        // Step 1: Event starts at 'start' time - increment count
        timeline.merge(start, 1, Integer::sum);

        // Step 2: Event ends at 'end' time - decrement count
        timeline.merge(end, -1, Integer::sum);

        // Step 3: Line sweep through timeline computing prefix sums
        int currentOverlap = 0;
        int maxOverlap = 0;

        // Step 4: Process events in chronological order
        for (int delta : timeline.values()) {
            currentOverlap += delta;
            // Step 5: Track maximum number of simultaneous events
            maxOverlap = Math.max(maxOverlap, currentOverlap);
        }

        return maxOverlap;
    }

    /**
     * Alternative implementation using Segment Tree with Lazy Propagation.
     * Provides better performance for large coordinate ranges through coordinate compression.
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

/**
 * Usage Example:
 * MyCalendarThree myCalendarThree = new MyCalendarThree();
 * int result1 = myCalendarThree.book(10, 20); // returns 1
 * int result2 = myCalendarThree.book(50, 60); // returns 1
 * int result3 = myCalendarThree.book(10, 40); // returns 2
 * int result4 = myCalendarThree.book(5, 15);  // returns 3
 *
 * Alternative usage:
 * MyCalendarThree.MyCalendarThreeSegmentTree segTree = new MyCalendarThree.MyCalendarThreeSegmentTree();
 * int result = segTree.book(10, 20);
 */
