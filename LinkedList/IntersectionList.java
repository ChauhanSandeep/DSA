package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.Node;

public class IntersectionList {
    public static void main(String[] args) {
        Node head1 = new Node(1);
        Node head2 = new Node(10);
        Node four = new Node(4);

        LinkedList list = new LinkedList(head1);
        list.add(new Node(2));
        list.add(new Node(3));
        list.add(four);
        list.add(new Node(5));
        list.add(new Node(6));

        LinkedList list2 = new LinkedList(head2);
        head2.setNext(new Node(11));
        head2.getNext().setNext(new Node(12));
        head2.getNext().getNext().setNext(four);
        System.out.println("first");
        list.printList(head1);
        System.out.println("Second");
        list2.printList(head2);
        Node intersectionNode = findIntersection(head1, head2);
        System.out.println("Intersection of node is " + intersectionNode.getData());
    }

    public static Node findIntersection(Node head1, Node head2) {
        int len1 = findLen(head1);
        int len2 = findLen(head2);

        if(len2 > len1) {
            return findIntersection(head2, head1, len2-len1);
        }else{
            return findIntersection(head1, head2, len1-len2);
        }
    }

    public static Node findIntersection(Node head1, Node head2, int commonLen) {
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

    public static int findLen(Node node) {
        Node temp = node;
        int len = 0;
        while(temp != null) {
            len++;
            temp = temp.getNext();
        }
        return len;
    }
}
