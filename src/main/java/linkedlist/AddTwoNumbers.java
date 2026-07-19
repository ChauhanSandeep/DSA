package linkedlist;

import java.util.Arrays;

/**
 * Problem: Add Two Numbers
 *
 * Add two non-negative integers stored as linked lists in reverse digit order.
 * Each node stores one decimal digit, and the result must use the same linked
 * list representation.
 *
 * Leetcode: https://leetcode.com/problems/add-two-numbers/ (Medium)
 * Rating:   acceptance 48.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Linked list | Dummy node | Carry propagation
 *
 * Example:
 *   Input:  l1 = [2,4,3], l2 = [5,6,4]
 *   Output: [7,0,8]
 *   Why:    342 + 465 = 807, stored in reverse order as 7 -> 0 -> 8.
 *
 * Follow-ups:
 *   1. Digits are stored in forward order?
 *      Use stacks or recurse by aligned length, as in Add Two Numbers II.
 *   2. Need bases other than 10?
 *      Replace modulo and division by that base; carry logic is unchanged.
 *   3. Need to add many lists?
 *      Sum one digit column across all active nodes with one carry.
 *
 * Related: Add Two Numbers II (445), Plus One Linked List (369).
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        AddTwoNumbers solver = new AddTwoNumbers();
        int[][] leftInputs = { {2, 4, 3}, {9, 9, 9, 9} };
        int[][] rightInputs = { {5, 6, 4}, {9, 9, 9} };
        int[][] expected = { {7, 0, 8}, {8, 9, 9, 0, 1} };
        for (int i = 0; i < leftInputs.length; i++) {
            ListNode node1 = null, tail1 = null;
            for (int digit : leftInputs[i]) {
                ListNode node = new ListNode(digit);
                if (node1 == null) { node1 = node; tail1 = node; } else { tail1.next = node; tail1 = node; }
            }
            ListNode node2 = null, tail2 = null;
            for (int digit : rightInputs[i]) {
                ListNode node = new ListNode(digit);
                if (node2 == null) { node2 = node; tail2 = node; } else { tail2.next = node; tail2 = node; }
            }
            ListNode outputHead = solver.addTwoNumbers(node1, node2);
            int[] output = new int[expected[i].length];
            for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
            System.out.printf("l1=%s l2=%s -> %s  expected=%s%n", Arrays.toString(leftInputs[i]), Arrays.toString(rightInputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
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
     * Intuition: this is ordinary column addition, and the lists already expose
     * digits from least significant to most significant. A dummyHead gives a
     * stable place to append every output digit while carry moves to the next column.
     *
     * Algorithm:
     *   1. Return the other list if one input is null.
     *   2. Keep current at dummyHead and carry at 0.
     *   3. Add available node digits plus carry, append sum % 10, and update carry.
     *   4. Advance any non-null input pointer until both lists and carry are done.
     *
     * Time:  O(max(m, n)) - each node from the longer list is read once.
     * Space: O(max(m, n)) - the returned linked list stores the sum digits.
     *
     * @param node1 first number in reverse digit order
     * @param node2 second number in reverse digit order
     * @return sum in reverse digit order
     */
    public ListNode addTwoNumbers(ListNode node1, ListNode node2) {
        // Handle edge cases
        if (node1 == null) return node2;
        if (node2 == null) return node1;

        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;
        int carry = 0;

        // Process both lists and handle carry
        while (node1 != null || node2 != null || carry > 0) {
            int digit1 = (node1 != null) ? node1.val : 0;
            int digit2 = (node2 != null) ? node2.val : 0;

            int sum = digit1 + digit2 + carry;
            carry = sum / 10;

            current.next = new ListNode(sum % 10);
            current = current.next;

            // Move to next nodes if available
            if (node1 != null) node1 = node1.next;
            if (node2 != null) node2 = node2.next;
        }

        return dummyHead.next;
    }
}