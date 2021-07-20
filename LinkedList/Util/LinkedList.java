package LinkedList.Util;

public class LinkedList {

    Node head;
    Node tail;
    public LinkedList(Node head) {
        this.head = head;
        this.tail = head;
    }
    public LinkedList(){}

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public void add(Node node) {
        tail.next = node;
        tail = tail.next;
    }

    public void printList(Node head) {
        Node temp = head;
        while(temp != null) {
            System.out.println(temp.getData());
            temp = temp.getNext();
        }
    }
}
