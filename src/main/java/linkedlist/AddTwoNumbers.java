package linkedlist;

/**
 * Problem: Add Two Numbers
 *
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order, and each of their nodes contains a single digit.
 * Add the two numbers and return the sum as a linked list.
 *
 * Example:
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807
 *
 * LeetCode: https://leetcode.com/problems/add-two-numbers
 *
 * Follow-up Questions:
 * 1. What if the digits are stored in normal order (not reversed)?
 *    Answer: Use a stack to reverse or recursively calculate with carry propagation.
 *    Related: https://leetcode.com/problems/add-two-numbers-ii/
 *
 * 2. How would you handle very large numbers that exceed integer limits?
 *    Answer: Continue using the linked list approach as it naturally handles arbitrary precision.
 *
 * 3. What if one list is significantly longer than the other?
 *    Answer: Current solution handles this by checking for null and treating missing digits as 0.
 *
 * @author Sandeep
 */
public class AddTwoNumbers {

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * Adds two numbers represented as linked lists in reverse order.
     *
     * Algorithm:
     * 1. Initialize a dummy head to simplify result list construction
     * 2. Use a carry variable to handle overflow from each digit addition
     * 3. Traverse both lists simultaneously, adding corresponding digits plus carry
     * 4. Create new nodes for the result with the sum modulo 10
     * 5. Update carry to sum divided by 10 for the next iteration
     * 6. Continue until both lists are processed and no carry remains
     *
     * Time Complexity: O(max(m, n)) where m and n are lengths of the two lists
     * Space Complexity: O(max(m, n)) for the result list
     *
     * @param node1 First number as linked list in reverse order
     * @param node2 Second number as linked list in reverse order
     * @return Sum as linked list in reverse order
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