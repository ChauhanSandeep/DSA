package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.Node;

public class RotateList {
    public static void main(String[] args) {
        Node head = new Node(1);
        LinkedList list = new LinkedList(head);
        list.add(new Node(2));
        list.add(new Node(3));
        list.add(new Node(4));
        list.add(new Node(5));
        list.add(new Node(6));
        list.printList(head);
        Node newHead = rotateList(head, 3);
        System.out.println("After");
        list.printList(newHead);
    }

    private static Node rotateList(Node head, int index) {
        Node tail = head;

        if(tail == null || tail.getNext() == null) return tail;
        int len = 1;
        while(tail.getNext() != null) {
            tail = tail.getNext();
            len++;
        }
        index = index % len;

        Node newHead = head;
        Node pointer = head;
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
