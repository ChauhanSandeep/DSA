package LinkedList.Util;

/**
 * A simple singly linked list implementation.
 * Supports adding nodes, retrieving head and tail, and printing the list.
 */
public class LinkedList {

    private ListNode head;
    private ListNode tail;

    /**
     * Constructor to initialize the linked list with a given head node.
     * @param head The initial head node of the list.
     */
    public LinkedList(ListNode head) {
        this.head = head;
        this.tail = head;
    }

    /**
     * Default constructor to initialize an empty linked list.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Gets the head node of the linked list.
     * @return The head node.
     */
    public ListNode getHead() {
        return head;
    }

    /**
     * Sets the head node of the linked list.
     * @param head The new head node.
     */
    public void setHead(ListNode head) {
        this.head = head;
        if (tail == null) { // Update tail if the list was empty
            tail = head;
        }
    }

    /**
     * Gets the tail node of the linked list.
     * @return The tail node.
     */
    public ListNode getTail() {
        return tail;
    }

    /**
     * Sets the tail node of the linked list.
     * @param tail The new tail node.
     */
    public void setTail(ListNode tail) {
        this.tail = tail;
    }

    /**
     * Adds a node to the end of the linked list.
     * Handles the case when the list is initially empty.
     * @param node The node to be added.
     */
    public void add(ListNode node) {
        if (node == null) {
            return; // Ignore null nodes
        }

        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
    }

    /**
     * Prints the linked list starting from the head node.
     * Uses StringBuilder for better performance.
     */
    public void printList() {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        while (current != null) {
            sb.append(current.getVal()).append(" -> ");
            current = current.getNext();
        }
        sb.append("NULL");
        System.out.println(sb.toString());
    }
}
