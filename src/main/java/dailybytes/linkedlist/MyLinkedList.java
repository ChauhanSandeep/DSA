package dailybytes.linkedlist;

import java.util.Arrays;

/**
 * Problem: Custom Singly Linked List Helper
 *
 * This utility class stores a head pointer and supports appending nodes,
 * printing the list, and updating the head. Other Daily Bytes linked-list
 * exercises use it as their shared list representation.
 *
 * Pattern:  Linked list | Node references | Tail append traversal
 *
 * Example:
 *   Input:  push nodes [1,2,3]
 *   Output: MyLinkedList{1 -> 2 -> 3 -> NULL}
 *   Why:    each push walks to the current tail and links the new node after it.
 *
 * Follow-ups:
 *   1. Make push O(1)?
 *      Store and update a tail pointer in addition to head.
 *   2. Support deletion by value?
 *      Track previous and current pointers while traversing.
 *   3. Make the list generic?
 *      Parameterize MyNode and MyLinkedList with a type variable.
 *   4. Detect accidental cycles before printing?
 *      Use a visited set or Floyd's cycle detection before traversal.
 */
public class MyLinkedList {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 3}, {} };
        String[] expected = { "MyLinkedList{1 -> 2 -> 3 -> NULL}", "MyLinkedList{NULL}" };

        for (int i = 0; i < inputs.length; i++) {
            MyLinkedList list = new MyLinkedList();
            for (int value : inputs[i]) {
                list.push(new MyNode(value));
            }
            System.out.printf("values=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), list, expected[i]);
        }
    }
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
