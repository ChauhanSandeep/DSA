package linkedlist;


import java.util.Arrays;

/**
 * Problem: Reverse Linked List
 *
 * Reverse a singly linked list and return the new head. This file keeps the
 * original iterative head-insertion implementation and a recursive alternative
 * for comparison.
 *
 * Leetcode: https://leetcode.com/problems/reverse-linked-list/ (Easy)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Pointer rewiring | Head insertion
 *
 * Example:
 *   Input:  head = [1,2,3]
 *   Output: [3,2,1]
 *   Why:    next pointers are redirected so the original tail becomes the head.
 *
 * Follow-ups:
 *   1. Reverse only a sublist?
 *      Use the same head-insertion idea between fixed boundaries.
 *   2. Reverse in groups of k?
 *      Verify k nodes, reverse that group, then reconnect and repeat.
 *   3. What is the recursion risk?
 *      The recursive version can overflow the stack on very long lists.
 *
 * Related: Reverse Linked List II (92), Reverse Nodes in k-Group (25).
 */
public class ReverseLinkedList {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 3}, {7} };
        int[][] expected = { {3, 2, 1}, {7} };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = null, tail = null;
            for (int value : inputs[i]) {
                ListNode node = new ListNode(value);
                if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
            }
            ListNode outputHead = reverseList(head);
            int[] output = new int[expected[i].length];
            for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
            System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
        }
    }


        /**
     * Intuition: keep dummy before the list and repeatedly lift curr.next to the
     * front, right after dummy. The reversed prefix grows at the front while curr
     * remains the tail of that prefix.
     *
     * Algorithm:
     *   1. Return head for an empty or single-node list.
     *   2. Create dummy, prev, curr, and next exactly as the original code does.
     *   3. While next exists, remove next after curr and insert it after prev.
     *   4. Refresh next from curr.next and return dummy.next.
     *
     * Time:  O(n) - each node after head is moved once.
     * Space: O(1) - only dummy and three pointers are stored.
     *
     * @param head head of the linked list
     * @return new head of the reversed list
     */
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;
        ListNode next = curr.next;

        // Move next to the front of the growing reversed prefix.
        while (next != null) {
            curr.next = next.next;
            next.next = prev.next;
            prev.next = next;
            next = curr.next;
            System.out.println("Complete states: prev = " + prev.val + ", curr = " + curr.val + ", next = " + (next != null ? next.val : "null"));
        }

        return dummy.next;
    }

    /**
     * Reverses the entire linked list recursively.
     *
     * @param head Head of the linked list.
     * @return New head of the reversed linked list.
     */
    public static ListNode reverseListRec(ListNode head) {
        if (head == null || head.getNext() == null) {
            return head; // Base case: If list is empty or has only one node, return head.
        }

        ListNode reversedHead = reverseListRec(head.getNext()); // Reverse remaining list
        head.getNext().setNext(head); // Reverse the current node's next link
        head.setNext(null); // Set current node's next to null (to avoid cycles)

        return reversedHead;
    }
}
