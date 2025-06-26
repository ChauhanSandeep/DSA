package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Problem: Check if a singly linked list is a palindrome.
 *
 * Approach:
 * 1. Find the middle of the linked list using the slow-fast pointer approach.
 * 2. Reverse the second half of the list.
 * 3. Compare the first half with the reversed second half.
 * 4. Restore the list to its original state (useful in real-world scenarios).
 *
 * Time Complexity: O(N) - Traversing the list multiple times.
 * Space Complexity: O(1) - No extra space used apart from pointers.
 *
 * Edge Cases Considered:
 * - Empty list (returns true).
 * - Single-node list (returns true).
 * - Even and odd-length lists.
 *
 * Link: https://leetcode.com/problems/palindrome-linked-list/
 */
public class OddEvenLinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(2));
        list.add(new ListNode(1));

        System.out.println("Is linked list palindrome? " + isPalindrome(head));
    }

    /**
     * Checks if a given linked list is a palindrome.
     * @param head Head node of the linked list.
     * @return true if the list is a palindrome, otherwise false.
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.getNext() == null) {
            return true; // Empty or single-node list is always a palindrome.
        }

        // Step 1: Find the middle of the list
        ListNode middleNode = findMiddle(head);
        ListNode secondHalfStart = reverseList(middleNode.getNext());

        // Step 2: Compare the two halves
        boolean isPalindrome = compareLists(head, secondHalfStart);

        // Step 3: Restore the list to its original form
        middleNode.setNext(reverseList(secondHalfStart));

        return isPalindrome;
    }

    /**
     * Finds the middle node of the linked list.
     * @param head Head node of the list.
     * @return Middle node of the list.
     */
    private static ListNode findMiddle(ListNode head) {
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        while (fastPointer != null && fastPointer.getNext() != null) {
            slowPointer = slowPointer.getNext();
            fastPointer = fastPointer.getNext().getNext();
        }
        return slowPointer;
    }

    /**
     * Reverses a linked list.
     * @param head Head of the list to be reversed.
     * @return New head after reversal.
     */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;

        while (current != null) {
            ListNode nextNode = current.getNext();
            current.setNext(prev);
            prev = current;
            current = nextNode;
        }
        return prev;
    }

    /**
     * Compares two linked lists for equality.
     * @param first First list.
     * @param second Second list (reversed).
     * @return true if both lists are identical, otherwise false.
     */
    private static boolean compareLists(ListNode first, ListNode second) {
        while (second != null) { // Only need to check the second half
            if (first.getVal() != second.getVal()) {
                return false;
            }
            first = first.getNext();
            second = second.getNext();
        }
        return true;
    }
}
