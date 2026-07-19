package linkedlist;

import java.util.Arrays;

/**
 * Problem: Delete Node in a Linked List
 *
 * Delete a non-tail node when only that node reference is available. Since the
 * previous node is unknown, copy the next node into the current node and bypass
 * the next node.
 *
 * Leetcode: https://leetcode.com/problems/delete-node-in-a-linked-list/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Value copy | Pointer bypass
 *
 * Example:
 *   Input:  head = [4,5,1,9], node = 5
 *   Output: [4,1,9]
 *   Why:    node 5 becomes value 1, then skips the original node 1.
 *
 * Follow-ups:
 *   1. What if node is the tail?
 *      This API cannot delete it because no previous pointer is available.
 *   2. What if the head is available?
 *      Track prev/current and bypass the target normally.
 *   3. What if this is a doubly linked list?
 *      Update both next and prev around the deleted node.
 *
 * Related: Remove Linked List Elements (203), Remove Nth Node From End of List (19).
 */
public class DeleteNodeInALinkedList {

    public static void main(String[] args) {
        DeleteNodeInALinkedList solver = new DeleteNodeInALinkedList();
        int[] input = {4, 5, 1, 9};
        ListNode head = new ListNode(4); head.next = new ListNode(5); head.next.next = new ListNode(1); head.next.next.next = new ListNode(9);
        solver.deleteNode(head.next);
        int[] output = {head.val, head.next.val, head.next.next.val};
        int[] expected = {4, 1, 9};
        System.out.printf("head=%s delete=5 -> %s  expected=%s%n", Arrays.toString(input), Arrays.toString(output), Arrays.toString(expected));
        int[] edgeInput = {1, 2};
        ListNode edgeHead = new ListNode(1); edgeHead.next = new ListNode(2);
        solver.deleteNode(edgeHead);
        int[] edgeOutput = {edgeHead.val};
        int[] edgeExpected = {2};
        System.out.printf("head=%s delete=1 -> %s  expected=%s%n", Arrays.toString(edgeInput), Arrays.toString(edgeOutput), Arrays.toString(edgeExpected));
    }





    // Definition for singly-linked list.
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

                        /**
     * Intuition: without the previous node, the only removable physical node is
     * node.next. Copy its value into node first, then unlink node.next so the
     * visible list no longer contains the requested value.
     *
     * Algorithm:
     *   1. Copy node.next.val into node.val.
     *   2. Point node.next to node.next.next.
     *
     * Time:  O(1) - two assignments are performed.
     * Space: O(1) - no extra structure is used.
     *
     * @param node non-tail node to delete
     */
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

    /**
     * Robust version with null checks.
     */
    public void deleteNodeRobust(ListNode node) {
        if (node == null || node.next == null) {
            throw new IllegalArgumentException("Cannot delete this node");
        }
        node.val = node.next.val;
        node.next = node.next.next;
    }
}