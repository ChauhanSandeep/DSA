package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.
 */
public class ReverseKList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        ListNode resultNode = new ReverseKList().reverseKGroup(head, 2);
        System.out.println(resultNode);
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if(head == null){
            return null;
        }
        int count = 0;
        ListNode temp = head;
        while(temp != null){
            count++;
            temp = temp.next;
        }

        ListNode result = new ListNode(0);

        // For sublist reversal
        ListNode curr = head;
        ListNode prev = null;
        ListNode next = null;

        // for linking reversed sublists
        ListNode prevTail = result;
        ListNode newTail = null;

        while(curr != null && count >= k){
            newTail = curr;          // this will be tail of current sublist in the end
            for(int i = 0; i < k; i++){
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
                count--;
            }
            prevTail.next = prev;   // link next of last sublist to starting of current sublist
            prevTail = newTail;     // update tail of previous Node for next iteration
        }
        prevTail.next = curr;
        return result.next;
    }
}