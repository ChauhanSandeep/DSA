package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

public class RotateList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));
        list.printList(head);
        ListNode newHead = rotateList(head, 3);
        System.out.println("After");
        list.printList(newHead);
    }

    private static ListNode rotateList(ListNode head, int index) {
        ListNode tail = head;

        if(tail == null || tail.getNext() == null) return tail;
        int len = 1;
        while(tail.getNext() != null) {
            tail = tail.getNext();
            len++;
        }
        index = index % len;

        ListNode newHead = head;
        ListNode pointer = head;
        for(int i=0; i<index && newHead != null; i++) {
            newHead = newHead.getNext();
        }

        tail.setNext(pointer);
        while(pointer.getNext() != newHead) {
            pointer = pointer.getNext();
        }
        pointer.setNext(null);
        return newHead;
    }
}
