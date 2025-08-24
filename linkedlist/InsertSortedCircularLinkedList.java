package linkedlist;

/**
 * Given a circular linked list that is sorted in ascending order, insert a value `insertVal`
 * into the list while maintaining its sorted order.
 *
 * The circular linked list does not have a defined head, but at least one reference node is given.
 * If the list is empty, create a new single-node circular list and return it.
 * Insertion should be done in such a way that the list remains sorted, even if the list contains duplicate values.
 *
 * For example, if the list is 3 -> 4 -> 1 (circular),
 * inserting 2 should result in 3 -> 4 -> 1 -> 2 (circular).
 *
 * Problem Link: https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/
 *
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
     * Steps:
     * 1. If the list is empty, create a new node that points to itself.
     * 2. Traverse the list to find the correct position for insertion:
     *   - If the new value is between two existing values, insert it there.
     *   - If the new value is greater than the maximum or less than the minimum, insert it at the boundary.
     *   - If we complete a full loop without finding a suitable position, insert the new value anywhere.
     *
     * Time Complexity: O(N), where N is the number of nodes in the list.
     * Space Complexity: O(1), as we use only a few extra pointers.
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
            if (insertVal >= current.val && insertVal <= current.next.val) {
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
            // Situations where this could happen:
            // - The list has only one node (current == current.next)
            // - The new value is not between any two existing values and we have traversed the entire list without finding a suitable position.
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
