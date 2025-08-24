package dailybytes.linkedlist;

/**
 * This class represents a custom singly linked list implementation with methods to add nodes,
 * print the list, and manage the head node.
 */
public class MyLinkedList {
    private MyNode head;

    public MyLinkedList() { }

    public MyLinkedList(MyNode head) {
        this.head = head;
    }

    /**
     * Adds a node to the end of the linked list.
     * @param node The node to be added.
     */
    public void push(MyNode node) {
        if (head == null) {
            head = node;
            return;
        }
        MyNode temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(node);
    }

    /**
     * Prints the linked list in a human-readable format.
     */
    public void printList() {
        MyNode temp = head;
        while (temp != null) {
            System.out.print(temp.getData() + " -> ");
            temp = temp.getNext();
        }
        System.out.println("NULL");
    }

    public MyNode getHead() {
        return head;
    }

    public void setHead(MyNode head) {
        this.head = head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MyLinkedList{");
        MyNode temp = head;
        while (temp != null) {
            sb.append(temp.getData()).append(" -> ");
            temp = temp.getNext();
        }
        sb.append("NULL}");
        return sb.toString();
    }
}
