package stacksandqueues;

import java.util.*;

/**
 * Problem: Smallest Range Covering Elements from K Lists
 *
 * Given k sorted lists, find the smallest inclusive range [left,right] that
 * contains at least one number from every list. If two ranges have the same
 * width, choose the one with the smaller left endpoint.
 *
 * Leetcode: https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/ (Hard)
 * Rating:   acceptance 70.3% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Min heap | K-way merge | Sliding range over lists
 *
 * Example:
 *   Input:  nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
 *   Output: [20,24]
 *   Why:    20 from list 2, 24 from list 1, and 22 from list 3 all fit inside [20,24].
 *
 * Follow-ups:
 *   1. Lists are unsorted?
 *      Sort each list first, then run the same k-way merge.
 *   2. Need all minimum-width ranges?
 *      Continue scanning after updates and collect every range with the best width.
 *   3. Lists are streamed from disk?
 *      Keep one cursor per stream and a heap of current heads; load only the next value needed.
 *   4. Need at least m of k lists covered instead of all k?
 *      Flatten tagged values and use a sliding window over the sorted merged stream.
 *
 * Related: Merge k Sorted Lists (23), Minimum Window Substring (76).
 */
public class SmallestRangeCoveringElementsFromKLists {
        /**
     * Intuition: one chosen value from each list forms a range from the current
     * minimum to the current maximum. To possibly shrink that range, advance the
     * list that owns the minimum; moving any other list cannot lower the left
     * edge. The heap finds that current minimum.
     *
     * Algorithm:
     *   1. Push the first value from each list and track the current max.
     *   2. Pop the current minimum and update the best range.
     *   3. Push the next value from the popped element's list, updating max.
     *   4. Stop when a list is exhausted.
     *
     * Time:  O(n log k) - each value can enter and leave the heap once.
     * Space: O(k) - the heap stores one active value per list.
     *
     * @param nums k sorted integer lists
     * @return smallest inclusive range covering every list
     */
public int[] smallestRange(List<List<Integer>> nums) {
        // Min heap to track the current smallest element from each list
        PriorityQueue<Element> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);

        int max = Integer.MIN_VALUE;
        int range = Integer.MAX_VALUE;

        // Initialize the heap with the first element of each list
        for (int i = 0; i < nums.size(); i++) {
            int val = nums.get(i).get(0);
            minHeap.offer(new Element(i, 0, val));
            max = Math.max(max, val);
        }

        int start = -1, end = -1;

        // Process until we've exhausted any of the lists
        while (minHeap.size() == nums.size()) {
            Element curr = minHeap.poll();
            int currRange = max - curr.val;

            // Update the smallest range
            if (currRange < range) {
                range = currRange;
                start = curr.val;
                end = max;
            }

            // Move to the next element in the current list
            if (curr.idx + 1 < nums.get(curr.row).size()) {
                int nextVal = nums.get(curr.row).get(curr.idx + 1);
                minHeap.offer(new Element(curr.row, curr.idx + 1, nextVal));
                max = Math.max(max, nextVal);
            }
        }

        return new int[]{start, end};
    }

    // Helper class to store the value and its position
    private static class Element {
        int row;    // which list
        int idx;    // index in the list
        int val;    // value

        Element(int row, int idx, int val) {
            this.row = row;
            this.idx = idx;
            this.val = val;
        }
    }

    public static void main(String[] args) {
        SmallestRangeCoveringElementsFromKLists solver = new SmallestRangeCoveringElementsFromKLists();
        List<List<List<Integer>>> inputs = Arrays.asList(Arrays.asList(Arrays.asList(1)), Arrays.asList(Arrays.asList(4, 10, 15, 24, 26), Arrays.asList(0, 9, 12, 20), Arrays.asList(5, 18, 22, 30)), Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
        String[] expected = {"[1, 1]", "[20, 24]", "[1, 1]"};
        for (int i = 0; i < inputs.size(); i++) {
            int[] got = solver.smallestRange(inputs.get(i));
            System.out.printf("nums=%s -> %s  expected=%s%n", inputs.get(i), Arrays.toString(got), expected[i]);
        }
    }
}
