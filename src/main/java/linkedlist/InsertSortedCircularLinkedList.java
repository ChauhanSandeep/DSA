package linkedlist;

import java.util.Arrays;

/**
 * Problem: Insert into a Sorted Circular Linked List
 *
 * Insert a value into an ascending circular linked list while preserving sorted
 * circular order. The given head can point anywhere, so insertion may happen in
 * the middle, at the wrap boundary, or anywhere after a full loop.
 *
 * Leetcode: https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Circular traversal | Boundary detection
 *
 * Example:
 *   Input:  head = [3,4,1], insertVal = 2
 *   Output: [3,4,1,2]
 *   Why:    2 belongs after 1 and before 3 in the circular sorted order.
 *
 * Follow-ups:
 *   1. How do duplicates affect insertion?
 *      A full loop means any equal-value gap is valid.
 *   2. What if the list is descending?
 *      Reverse the middle and boundary comparisons.
 *   3. Can you return the minimum node?
 *      Track the wrap point and return current.next after insertion.
 */
public class InsertSortedCircularLinkedList {

    public static void main(String[] args) {
        InsertSortedCircularLinkedList solver = new InsertSortedCircularLinkedList();
        int[] input = {3, 4, 1};
        ListNode head = new ListNode(3); head.next = new ListNode(4); head.next.next = new ListNode(1); head.next.next.next = head;
        ListNode outputHead = solver.insert(head, 2);
        int[] output = new int[4];
        ListNode current = outputHead;
        for (int i = 0; i < output.length; i++, current = current.next) output[i] = current.val;
        int[] expected = {3, 4, 1, 2};
        System.out.printf("head=%s insertVal=2 -> %s  expected=%s%n", Arrays.toString(input), Arrays.toString(output), Arrays.toString(expected));
        ListNode single = solver.insert(null, 1);
        int[] singleOutput = {single.val};
        int[] singleExpected = {1};
        System.out.printf("head=%s insertVal=1 -> %s  expected=%s%n", "[]", Arrays.toString(singleOutput), Arrays.toString(singleExpected));
    }


        /**
     * Intuition: a value fits either between two increasing neighbors or at the
     * single maximum-to-minimum wrap. If a full loop finds neither, all gaps are
     * equivalent and the new value can be inserted at the current position.
     *
     * Algorithm:
     *   1. If head is null, create one node that points to itself.
     *   2. Walk current around the cycle looking for an in-order gap.
     *   3. Also accept the wrap boundary when insertVal is outside the range.
     *   4. Stop after a full loop if needed, then splice in newNode.
     *
     * Time:  O(n) - at most one full cycle is scanned.
     * Space: O(1) - only current and newNode are stored.
     *
     * @param head any node in the sorted circular linked list
     * @param insertVal value to insert
     * @return original head, or the new node for an empty list
     */
    public ListNode insert(ListNode head, int insertVal) {
        // Case 1: If the list is empty, create a new single-node circular list.
        if (head == null) {
            ListNode newNode = new ListNode(insertVal);
            newNode.next = newNode; // Circular reference
            return newNode;
        }

        ListNode current = head;
        ListNode newNode = new ListNode(insertVal);

        while (true) {
            // Case 2: Insert in the middle of sorted values (between two valid nodes)
            if (insertVal >= current.val && insertVal <= current.next.val) {
                break;
            }

            // Case 3: Insert at the boundary (minimum or maximum value in the circular list)
            // This happens when we wrap around the circular list and find a decreasing point.
            if (current.val > current.next.val && (insertVal >= current.val || insertVal <= current.next.val)) {
                break;
            }

            // Move to the next node in the circular list
            current = current.next;

            // Case 4: If we complete a full loop, insert the new value anywhere
            // Situations where this could happen:
            // - The list has only one node (current == current.next)
            // - The new value is not between any two existing values and we have traversed the entire list without finding a suitable position.
            if (current == head) {
                break;
            }
        }

        // Insert the new node into the list
        newNode.next = current.next;
        current.next = newNode;

        return head;
    }
}
