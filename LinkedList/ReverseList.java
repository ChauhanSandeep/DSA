package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.Node;

public class ReverseList {
    public static void main(String[] args) {
        Node head = new Node(1);
        LinkedList list = new LinkedList(head);
        list.add(new Node(2));
        list.add(new Node(3));
        list.add(new Node(4));
        list.add(new Node(5));
        list.add(new Node(6));
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
    public static Node reverseList(Node head) {

        Node before = null;
        Node curr = head;
        Node next = head;

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
    public static Node reverseLinkedList(Node head, int k) {
        Node prev = null;
        Node curr = head;
        Node next = null;

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
