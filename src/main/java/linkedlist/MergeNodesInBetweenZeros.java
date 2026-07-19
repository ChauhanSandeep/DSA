package linkedlist;

import java.util.Arrays;

/**
 * Problem: Merge Nodes in Between Zeros
 *
 * A linked list starts and ends with zero, and values between consecutive zeros
 * form one segment. Replace each segment with a single node whose value is that
 * segment sum, and return the list without zeros.
 *
 * Leetcode: https://leetcode.com/problems/merge-nodes-in-between-zeros/ (Medium)
 * Rating:   1333
 * Pattern:  Linked list | Segment sum | Dummy node
 *
 * Example:
 *   Input:  head = [0,3,1,0,4,5,2,0]
 *   Output: [4,11]
 *   Why:    the segment sums are 3 + 1 = 4 and 4 + 5 + 2 = 11.
 *
 * Follow-ups:
 *   1. Can this be done in-place?
 *      Store each segment sum in the first node of that segment.
 *   2. What if values can be negative?
 *      Track whether a segment existed instead of checking sum > 0.
 *   3. What if boundaries may be missing?
 *      Validate input or treat list ends as implicit zeros.
 */
public class MergeNodesInBetweenZeros {

    public static void main(String[] args) {
        MergeNodesInBetweenZeros solver = new MergeNodesInBetweenZeros();
        int[][] inputs = { {0, 3, 1, 0, 4, 5, 2, 0}, {0, 1, 0} };
        int[][] expected = { {4, 11}, {1} };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = null, tail = null;
            for (int value : inputs[i]) {
                ListNode node = new ListNode(value);
                if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
            }
            ListNode outputHead = solver.mergeNodes(head);
            int[] output = new int[expected[i].length];
            for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
            System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
        }
    }

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

        /**
     * Intuition: zeros are separators. Start after the leading zero, accumulate
     * values until the next zero, append one node for that completed sum, then
     * move past the separator to begin the next segment.
     *
     * Algorithm:
     *   1. Return null for an empty list or only the leading zero.
     *   2. Use resultHead/resultTail as a dummy-built output list.
     *   3. Accumulate sum while current is non-null and current.val is not zero.
     *   4. Append sum when positive, skip the zero, and continue.
     *
     * Time:  O(n) - each input node is visited once.
     * Space: O(1) - excluding output nodes, only pointers and sum are stored.
     *
     * @param head list that starts and ends with zero
     * @return head of the summed list without zeros
     */
    public ListNode mergeNodes(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode resultHead = new ListNode(0);
        ListNode resultTail = resultHead;

        ListNode current = head.next; // Skip the initial zero

        while (current != null) {
            int sum = 0;

            // Sum all nodes until we hit a zero
            while (current != null && current.val != 0) {
                sum += current.val;
                current = current.next;
            }

            // Create new node with the sum
            if (sum > 0) {
                resultTail.next = new ListNode(sum);
                resultTail = resultTail.next;
            }

            // Move past the zero
            if (current != null) {
                current = current.next;
            }
        }

        return resultHead.next;
    }

    /**
     * In-place solution that modifies existing nodes to save space.
     * Reuses the first non-zero node in each segment to store the sum.
     *
     * Algorithm:
     * 1. Skip the initial zero node
     * 2. For each segment between zeros, calculate the sum
     * 3. Store sum in the first node of the segment
     * 4. Skip the zero and find next segment
     * 5. Return the modified linked list
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param head Head of the linked list
     * @return Head of the modified linked list
     */
    public ListNode mergeNodesInPlace(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode current = head.next; // Skip initial zero
        ListNode resultHead = current; // This will be our result head

        while (current != null) {
            int sum = 0;
            ListNode sumNode = current; // Node that will store the sum

            // Calculate sum for current segment
            while (current != null && current.val != 0) {
                sum += current.val;
                current = current.next;
            }

            // Store sum in the first node of the segment
            sumNode.val = sum;

            // Skip the zero and find next segment
            if (current != null) {
                current = current.next;
                sumNode.next = current;
            } else {
                sumNode.next = null;
            }
        }

        return resultHead;
    }
}