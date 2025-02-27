package LinkedList;

import LinkedList.Util.ListNode;

/**
 * Given a Circular Linked List node, which is sorted in ascending order, write a function to insert a value insertVal into the list such that it remains a sorted circular list.
 *
 * https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/
 */
public class InsertSortedCircularLinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        head.next = new ListNode(4);
        head.next.next = new ListNode(1);
        head.next.next.next = head;
        
        InsertSortedCircularLinkedList list = new InsertSortedCircularLinkedList();
        list.insert(head, 2);
    }

    public ListNode insert(ListNode head, int insertVal) {
        if (head == null) {
            ListNode node = new ListNode(insertVal);
            node.next = node;
            return node;
        }
        
        ListNode temp = head;
        while (true) {
            if ((temp.val <= insertVal && insertVal <= temp.next.val) || // Insert within sorted range
                (temp.val > temp.next.val && (insertVal >= temp.val || insertVal <= temp.next.val)) || // Insert at boundary
                (temp.next == head)) { // Insert if full loop completed
                
                ListNode node = new ListNode(insertVal);
                node.next = temp.next;
                temp.next = node;
                break;
            }
            
            temp = temp.next;
        }
        return head;
    }
}
