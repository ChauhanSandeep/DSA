package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Given the head of a singly linked list, group all the nodes with odd indices together followed by the nodes with even indices, and return the reordered list.
 *
 * Algorithm:
 * - Maintain separate pointers for odd and even indexed nodes.
 * - Traverse the list and adjust pointers to group odd indices together followed by even indices.
 * - Merge the two lists at the end.
 *
 * Time Complexity: O(n), where n is the number of nodes in the list.
 * Space Complexity: O(1), since no extra space is used.
 *
 * https://leetcode.com/problems/odd-even-linked-list/
 */
public class OddEvenLinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        ListNode reorderedList = new OddEvenLinkedList().oddEvenList(head);
        list.printList(reorderedList);
    }

    /**
     * Reorders the linked list by grouping odd-indexed nodes first, followed by even-indexed nodes.
     * @param head The head of the linked list.
     * @return The reordered linked list.
     */
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode odd = head;
        ListNode even = head.next;
        ListNode evenHead = even;

        // Rearrange nodes in odd-even order
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }

        // Attach even list after odd list
        odd.next = evenHead;
        return head;
    }
}
