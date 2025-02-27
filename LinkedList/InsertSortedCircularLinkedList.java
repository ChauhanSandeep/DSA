package LinkedList;

import LinkedList.Util.ListNode;

/**
 * Given a Circular Linked List node, which is sorted in ascending order, write a function to insert a value insertVal into the list such that it remains a sorted circular list.
 *
 * https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/
 *
 * Time Complexity: O(N), where N is the number of nodes in the circular linked list.
 * Space Complexity: O(1), since we only use a few pointers.
 */
public class InsertSortedCircularLinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        head.next = new ListNode(4);
        head.next.next = new ListNode(1);
        head.next.next.next = head;

        InsertSortedCircularLinkedList list = new InsertSortedCircularLinkedList();
        head = list.insert(head, 2);
    }

    /**
     * Inserts a new node into a sorted circular linked list while maintaining order.
     *
     * @param head The head of the circular linked list
     * @param insertVal The value to insert
     * @return The head of the updated circular linked list
     */
    public ListNode insert(ListNode head, int insertVal) {
        if (head == null) {
            ListNode node = new ListNode(insertVal);
            node.next = node;
            return node;
        }

        ListNode current = head;
        ListNode newNode = new ListNode(insertVal);

        while (true) {
            // Case 1: Insert in the middle of sorted values
            if (current.val <= insertVal && insertVal <= current.next.val) {
                break;
            }

            // Case 2: Insert at the boundary (smallest or largest)
            if (current.val > current.next.val && (insertVal >= current.val || insertVal <= current.next.val)) {
                break;
            }

            // Move to the next node
            current = current.next;

            // Case 3: If we complete a full loop, insert anywhere
            if (current == head) break;
        }

        // Insert the new node
        newNode.next = current.next;
        current.next = newNode;

        return head;
    }
}
