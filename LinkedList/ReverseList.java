package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;


/**
 * This class provides methods to reverse a linked list.
 * Includes:
 * - Iterative reversal of the entire list.
 * - Recursive reversal (TODO).
 * - Reversing in groups of 'k' nodes.
 *
 * LeetCode Problem: https://leetcode.com/problems/reverse-linked-list/
 */
public class ReverseList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        System.out.println("Original List:");
        list.printList(head);

        head = reverseList(head);
        System.out.println("After complete reversal:");
        list.printList(head);

        head = reverseInGroups(head, 4);
        System.out.println("After batch-wise reversal:");
        list.printList(head);
    }

    /**
     * Reverses the entire linked list iteratively.
     *
     * @param head Head of the linked list.
     * @return New head of the reversed linked list.
     *
     * Time Complexity: O(n) - Iterates through the list once.
     * Space Complexity: O(1) - Uses constant extra space.
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;

        while (current != null) {
            ListNode nextNode = current.getNext(); // Store next node
            current.setNext(prev); // Reverse link
            prev = current; // Move prev forward
            current = nextNode; // Move current forward
        }
        return prev; // New head of reversed list
    }

    /**
     * TODO: Implement recursive reversal of the linked list.
     *
     * @param list Linked list object to be reversed.
     */
    public static void reverseLinkedListRec(LinkedList list) {
        // Complete this method
    }

    /**
     * Reverses the linked list in groups of 'k' nodes.
     *
     * @param head Head of the linked list.
     * @param k Group size for reversal.
     * @return New head after group-wise reversal.
     *
     * Time Complexity: O(n) - Each node is visited once.
     * Space Complexity: O(n/k) - Due to recursive calls.
     */
    public static ListNode reverseInGroups(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head; // No change needed
        }

        ListNode prev = null;
        ListNode current = head;
        int count = 0;

        // Reverse first 'k' nodes
        while (current != null && count < k) {
            ListNode nextNode = current.getNext();
            current.setNext(prev);
            prev = current;
            current = nextNode;
            count++;
        }

        // Recursively reverse the next part and attach it
        if (current != null) {
            head.setNext(reverseInGroups(current, k));
        }

        return prev; // New head of the reversed section
    }
}
