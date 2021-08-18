package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

public class MiddleElement {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        System.out.println(middleElement(head));

    }

    /**
     * Find middle element of a linked list
     * @param head
     * @return
     */
    private static int middleElement(ListNode head) {
        if (head == null) return -1;

        ListNode slow = head;
        ListNode fast = head;

        while (fast.getNext() != null && fast.getNext().getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        return slow.getVal();

    }
}
