package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Given the head of a singly linked list, group all the nodes with odd indices together followed by the nodes with even indices, and return the reordered list.
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
        ListNode listNode = new OddEvenLinkedList().oddEvenList(head);
        System.out.println(listNode.val);
    }

    public ListNode oddEvenList(ListNode head) {
        if(head == null || head.next == null || head.next.next == null) return head;

        ListNode temp1 = head;
        ListNode temp2 = head.next;
        ListNode secondHead = head.next;

        while(temp2 != null && temp2.next != null) {
            temp1.next = temp2.next;
            temp1 = temp1.next;
            temp2.next = temp1.next;
            temp2 = temp2.next;
        }
        temp1.next = secondHead;
        return head;
    }
}
