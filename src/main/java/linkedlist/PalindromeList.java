package linkedlist;

import java.util.Arrays;

/**
 * Problem: Palindrome Linked List
 *
 * Determine whether a singly linked list reads the same forward and backward.
 * The primary method finds the midpoint, reverses the second half, and compares
 * the two forward scans.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-linked-list/ (Easy)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Slow and fast pointers | Reverse second half
 *
 * Example:
 *   Input:  head = [1,2,2,1]
 *   Output: true
 *   Why:    the first half [1,2] matches the reversed second half [1,2].
 *
 * Follow-ups:
 *   1. Can you restore the list afterward?
 *      Reverse the second half again and reconnect it.
 *   2. Can recursion solve it?
 *      Yes, but it uses O(n) stack space.
 *   3. What if values are objects?
 *      Compare with equals while keeping the same pointer strategy.
 *
 * Related: Reverse Linked List (206), Middle of the Linked List (876).
 */
public class PalindromeList {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 2, 1}, {1, 2} };
        boolean[] expected = { true, false };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = null, tail = null;
            for (int value : inputs[i]) {
                ListNode node = new ListNode(value);
                if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
            }
            boolean output = isPalindrome(head);
            System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), output, expected[i]);
        }
    }


        /**
     * Intuition: a palindrome has matching halves when the second half is read
     * backward. Reversing from the middle lets both comparisons move forward
     * through linked-list pointers.
     *
     * Algorithm:
     *   1. Return true for an empty or single-node list.
     *   2. Find the middle with slow and fast pointers.
     *   3. Reverse the list starting at the middle node.
     *   4. Compare the original head against the reversed second half.
     *
     * Time:  O(n) - finding, reversing, and comparing are linear.
     * Space: O(1) - reversal is done in place.
     *
     * @param head head of the linked list
     * @return true if the list values form a palindrome
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true; // Edge case: Empty or single-node list

        // Step 1: Find the middle of the linked list
        ListNode middleNode = findMiddle(head);

        // Step 2: Reverse the second half of the list
        ListNode reversedSecondHalf = reverseList(middleNode);

        // Step 3: Compare both halves
        return isEqual(head, reversedSecondHalf);
    }

    /**
     * Uses the slow and fast pointer technique to find the middle of the list.
     * @param head Head of the list.
     * @return The starting node of the second half of the list.
     */
    /** Returns the middle node, or the second middle node for even lengths. */
    private static ListNode findMiddle(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow; // Middle of the list (or second half start in even-length lists)
    }

    /**
     * Reverses a linked list.
     * @param head Head of the list to reverse.
     * @return New head of the reversed list.
     */
    /** Reverses the list starting at head and returns the new head. */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head, next;

        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }

    /**
     * Compares two linked lists for equality.
     * @param first First list's head.
     * @param second Second list's head.
     * @return True if both lists are identical, otherwise False.
     */
    /** Compares first against second until the second list ends. */
    private static boolean isEqual(ListNode first, ListNode second) {
        while (second != null) { // Only check second half (first half is always longer or equal)
            if (first.val != second.val) return false;
            first = first.next;
            second = second.next;
        }
        return true;
    }
}
