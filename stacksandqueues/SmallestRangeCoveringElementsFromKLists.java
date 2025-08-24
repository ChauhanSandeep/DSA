package stacksandqueues;

import java.util.*;

/**
 * Problem: Smallest Range Covering Elements from K Lists
 *
 * You have k lists of sorted integers in non-decreasing order. Find the smallest range that includes
 * at least one number from each of the k lists.
 *
 * The range [a,b] is smaller than range [c,d] if b - a < d - c or a < c if b - a == d - c.
 *
 * Example:
 * Input: nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
 * Output: [20,24]
 * Explanation:
 * List 1: [4, 10, 15, 24,26], 24 is in range [20,24].
 * List 2: [0, 9, 12, 20], 20 is in range [20,24].
 * List 3: [5, 18, 22, 30], 22 is in range [20,24].
 *
 * LeetCode: https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists
 *
 * Time Complexity: O(n * log m) where n is the total number of elements and m is the number of lists
 * Space Complexity: O(m) for the min heap
 */
public class SmallestRangeCoveringElementsFromKLists {
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
}
