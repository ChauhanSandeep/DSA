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
        new InsertSortedCircularLinkedList().insert(head, 2);
    }

    public ListNode insert(ListNode head, int insertVal) {
        if(head == null) {
            ListNode node = new ListNode(insertVal);
            node.next = node;
            return node;
        }
        ListNode temp = head;
        boolean found = false;
        while(!found) {
            if((temp.val < temp.next.val && insertVal >= temp.val && insertVal <= temp.next.val) || // still climbing
                    (temp.val > temp.next.val && ((insertVal >= temp.val) || (insertVal <= temp.next.val))) || // tipped off
                    (temp.next == head)) {
                // System.out.println(temp.val);
                ListNode node = new ListNode(insertVal);
                node.next = temp.next;
                temp.next = node;
                found = true;
            }
            temp = temp.next;
        }
        return head;
    }
}
