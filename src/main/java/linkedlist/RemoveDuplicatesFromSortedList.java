package linkedlist;

import java.util.Arrays;

/**
 * Problem: Remove Duplicates from Sorted List
 *
 * Given a sorted linked list, delete repeated nodes so each value appears once.
 * Since the list is sorted, every duplicate of a value appears directly next to
 * another copy of that value.
 *
 * Leetcode: https://leetcode.com/problems/remove-duplicates-from-sorted-list/ (Easy)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Sorted scan | In-place deletion
 *
 * Example:
 *   Input:  head = [1,1,2]
 *   Output: [1,2]
 *   Why:    the second 1 is adjacent to the first and can be skipped.
 *
 * Follow-ups:
 *   1. Remove all duplicated values instead of keeping one?
 *      Use a dummy node and skip each full duplicate run.
 *   2. What if the list is unsorted?
 *      Track seen values with a set, or sort before deduping.
 *   3. Can this be recursive?
 *      Deduplicate head.next, then skip matching next nodes.
 *
 * Related: Remove Duplicates from Sorted List II (82).
 */
public class RemoveDuplicatesFromSortedList {

    public static void main(String[] args) {
        RemoveDuplicatesFromSortedList solver = new RemoveDuplicatesFromSortedList();
        int[][] inputs = { {1, 1, 2}, {1, 1, 1} };
        int[][] expected = { {1, 2}, {1} };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = null, tail = null;
            for (int value : inputs[i]) {
                ListNode node = new ListNode(value);
                if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
            }
            ListNode outputHead = solver.deleteDuplicates(head);
            int[] output = new int[expected[i].length];
            for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
            System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
        }
    }

    // Definition for singly-linked list
    private static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

        /**
     * Intuition: in a sorted list, any duplicate of current.val must be current.next.
     * Keep current fixed while deleting duplicates, and move current only when the
     * next value is different.
     *
     * Algorithm:
     *   1. Return head for an empty or single-node list.
     *   2. Start current at head.
     *   3. If current and current.next match, skip current.next.
     *   4. Otherwise advance current to the next unique value.
     *
     * Time:  O(n) - each node is inspected once.
     * Space: O(1) - duplicates are removed by pointer rewiring.
     *
     * @param head head of the sorted linked list
     * @return head with one copy per value
     */
    public ListNode deleteDuplicates(ListNode head) {
        // Handle edge cases
        if (head == null || head.next == null) {
            return head;
        }

        ListNode current = head;

        // Traverse and remove duplicates
        while (current != null && current.next != null) {
            if (current.val == current.next.val) {
                // Skip the duplicate node
                current.next = current.next.next;
            } else {
                // Move to next unique element
                current = current.next;
            }
        }

        return head;
    }
}