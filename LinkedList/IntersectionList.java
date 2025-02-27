package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

public class IntersectionList {
    public static void main(String[] args) {
        ListNode head1 = new ListNode(1);
        ListNode head2 = new ListNode(10);
        ListNode four = new ListNode(4);

        LinkedList list = new LinkedList(head1);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(four);
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        LinkedList list2 = new LinkedList(head2);
        head2.setNext(new ListNode(11));
        head2.getNext().setNext(new ListNode(12));
        head2.getNext().getNext().setNext(four);
        System.out.println("first");
        list.printList(head1);
        System.out.println("Second");
        list2.printList(head2);
        ListNode intersectionNode = findIntersection(head1, head2);
        System.out.println("Intersection of node is " + intersectionNode.getVal());
    }

    public static ListNode findIntersection(ListNode head1, ListNode head2) {
        int len1 = findLen(head1);
        int len2 = findLen(head2);

        if(len2 > len1) {
            return findIntersection(head2, head1, len2-len1);
        }else{
            return findIntersection(head1, head2, len1-len2);
        }
    }

    public static ListNode findIntersection(ListNode head1, ListNode head2, int commonLen) {
        int len = 0;
        while(len < commonLen) {
            head1 = head1.getNext();
            len++;
        }

        while(head1 != null || head2 != null) {
            if(head1 == head2) return head1;

            head1 = head1.getNext();
            head2 = head2.getNext();
        }
        throw new RuntimeException("Not found");

    }

    public static int findLen(ListNode node) {
        ListNode temp = node;
        int len = 0;
        while(temp != null) {
            len++;
            temp = temp.getNext();
        }
        return len;
    }
}
