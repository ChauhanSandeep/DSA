package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;
import java.util.Optional;

/**
 * Problem: Find the middle element of a singly linked list.
 * 
 * Approach:
 * - Uses the slow and fast pointer technique.
 * - Slow pointer moves one step at a time.
 * - Fast pointer moves two steps at a time.
 * - When fast pointer reaches the end, slow pointer is at the middle.
 *
 * Time Complexity: O(N) - We traverse the list once.
 * Space Complexity: O(1) - No extra space is used.
 *
 * Edge Cases Considered:
 * - Empty list (returns empty Optional).
 * - List with one node (returns that node).
 * - List with an even number of nodes (returns the second middle node).
 *
 * Link: https://leetcode.com/problems/middle-of-the-linked-list/
 */
public class MiddleElement {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        Optional<Integer> middleElement = findMiddleElement(head);
        System.out.println("Middle Element: " + middleElement.orElse(-1));
    }

    /**
     * Finds the middle element of a linked list using the slow-fast pointer approach.
     * @param head Head node of the linked list.
     * @return Optional containing the value of the middle element, or empty if list is null.
     */
    private static Optional<Integer> findMiddleElement(ListNode head) {
        if (head == null) return Optional.empty(); // Handle empty list

        ListNode slowPointer = head;
        ListNode fastPointer = head;

        while (fastPointer != null && fastPointer.getNext() != null) {
            slowPointer = slowPointer.getNext();
            fastPointer = fastPointer.getNext().getNext();
        }
        
        return Optional.of(slowPointer.getVal());
    }
}
