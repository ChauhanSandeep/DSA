package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.Node;

public class MiddleElement {
    public static void main(String[] args) {
        Node head = new Node(1);
        LinkedList list = new LinkedList(head);
        list.add(new Node(2));
        list.add(new Node(3));
        list.add(new Node(4));
        list.add(new Node(5));
        list.add(new Node(6));

        System.out.println(middleElement(head));

    }

    /**
     * Find middle element of a linked list
     * @param head
     * @return
     */
    private static int middleElement(Node head) {
        if (head == null) return -1;

        Node slow = head;
        Node fast = head;

        while (fast.getNext() != null && fast.getNext().getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        return slow.getData();

    }
}
