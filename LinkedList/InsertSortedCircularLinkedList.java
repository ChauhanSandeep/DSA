package LinkedList;

import LinkedList.Util.ListNode;

/**
 * Given a circular linked list that is sorted in ascending order, insert a value `insertVal`
 * into the list while maintaining its sorted order.
 * 
 * The circular linked list does not have a defined head, but at least one reference node is given.
 * If the list is empty, create a new single-node circular list and return it.
 *
 * Problem Link: https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/
 * 
 * Time Complexity: O(N), where N is the number of nodes in the list.
 * Space Complexity: O(1), as we use only a few extra pointers.
 */
public class InsertSortedCircularLinkedList {

    public static void main(String[] args) {
        // Creating a sorted circular linked list: 3 -> 4 -> 1 -> (back to 3)
        ListNode head = new ListNode(3);
        head.next = new ListNode(4);
        head.next.next = new ListNode(1);
        head.next.next.next = head; // Circular link

        InsertSortedCircularLinkedList list = new InsertSortedCircularLinkedList();
        head = list.insert(head, 2); // Inserting 2 into the circular list
    }

    /**
     * Inserts a new node into a sorted circular linked list while maintaining order.
     *
     * @param head The head of the circular linked list
     * @param insertVal The value to insert
     * @return The head of the updated circular linked list
     */
    public ListNode insert(ListNode head, int insertVal) {
        // Case 1: If the list is empty, create a new single-node circular list.
        if (head == null) {
            ListNode newNode = new ListNode(insertVal);
            newNode.next = newNode; // Circular reference
            return newNode;
        }

        ListNode current = head;
        ListNode newNode = new ListNode(insertVal);

        while (true) {
            // Case 2: Insert in the middle of sorted values (between two valid nodes)
            if (current.val <= insertVal && insertVal <= current.next.val) {
                break;
            }

            // Case 3: Insert at the boundary (minimum or maximum value in the circular list)
            // This happens when we wrap around the circular list and find a decreasing point.
            if (current.val > current.next.val && (insertVal >= current.val || insertVal <= current.next.val)) {
                break;
            }

            // Move to the next node in the circular list
            current = current.next;

            // Case 4: If we complete a full loop, insert the new value anywhere
            if (current == head) {
                break;
            }
        }

        // Insert the new node into the list
        newNode.next = current.next;
        current.next = newNode;

        return head;
    }
}
