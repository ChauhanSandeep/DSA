package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

public class ReverseList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));
        list.printList(head);
        head = reverseList(head);
        System.out.println("After complete reversal");
        list.printList(head);
        head = reverseLinkedList(head, 4);
        System.out.println("After batch wise reversal");
        list.printList(head);
    }

    /**
     * Iterative solution to reverse linked list
     */
    public static ListNode reverseList(ListNode head) {

        ListNode before = null;
        ListNode curr = head;
        ListNode next = head;

        while(curr != null) {
            next = curr.getNext();
            curr.setNext(before);
            before = curr;
            curr = next;
        }
        return before;
    }

    // TOOD: create recursive reverse linked list program.
    public static void reverseLinkedListRec(LinkedList list) {

    }

    /**
     * Reverse k nodes at a time
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseLinkedList(ListNode head, int k) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = null;

        int count = 0;
        while(curr != null && count < k) {
            next = curr.getNext();
            curr.setNext(prev);
            prev = curr;
            curr = next;
            count++;
        }

        if(next != null) {
            head.setNext(reverseLinkedList(next, k));
        }
        return prev;
    }
}
