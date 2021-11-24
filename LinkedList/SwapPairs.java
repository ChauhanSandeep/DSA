package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Swap node of linked list in pairs
 * 1->2->3->4
 * result:
 * 2->1->4->3
 */
public class SwapPairs {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        ListNode resultHead = new SwapPairs().swapPairs(head);
        System.out.println(resultHead);
    }

    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode prev = dummy;

        while(head != null && head.next != null) {
            //Nodes to be swapped
            ListNode first = head;
            ListNode second = head.next;
            // swap
            prev.next = second;
            first.next = second.next;
            second.next = first;
            // Reinitializing the head and prev for next swap
            prev = first;
            head = first.next;
        }
        return dummy.next;
    }
}
