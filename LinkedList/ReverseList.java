package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * ✅ Problem: Reverse Linked List
 * 🔗 LeetCode: https://leetcode.com/problems/reverse-linked-list/
 * 📚 Problem Statement:
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 *
 * 🔍 **Approach:**
 * - Iterative reversal of the entire list.
 * - Recursive reversal of the entire list.
 * - Reversing in groups of 'k' nodes.
 *
 * 📊 **Time Complexity:**
 * - `reverseList()`: O(N) - Iterates through the list once.
 * - `reverseListRec()`: O(N) - Visits each node once.
 * - `reverseInGroups()`: O(N) - Each node is visited once.
 *
 * 🛠 **Space Complexity:**
 * - `reverseList()`: O(1) - Uses constant extra space.
 * - `reverseListRec()`: O(N) - Due to recursive function calls.
 * - `reverseInGroups()`: O(N) - Due to recursive calls.
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
        list.printList();

        head = reverseList(head);
        System.out.println("After complete reversal:");
        list.printList();

        head = reverseListRec(head);
        System.out.println("After recursive reversal:");
        list.printList();

        head = reverseInGroups(head, 4);
        System.out.println("After batch-wise reversal:");
        list.printList();
    }

    /**
     * Reverses the entire linked list iteratively.
     *
     * @param head Head of the linked list.
     * @return New head of the reversed linked list.
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null, current = head, nextNode;

        while (current != null) {
            nextNode = current.getNext(); // Store next node
            current.setNext(prev); // Reverse link
            prev = current; // Move prev forward
            current = nextNode; // Move current forward
        }
        return prev; // New head of reversed list
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
