package dailybytes.linkedlist;

import java.util.Arrays;

/**
 * Problem: Singly Linked List Node Helper
 *
 * This utility class stores one integer value and a reference to the next node.
 * It is the shared node representation used by the Daily Bytes linked-list
 * exercises in this package.
 *
 * Pattern:  Linked list | Node references | Data container
 *
 * Example:
 *   Input:  new MyNode(1, new MyNode(2))
 *   Output: MyNode{data=1, next=2}
 *   Why:    the node stores data 1 and its next reference points to the node
 *           containing 2.
 *
 * Follow-ups:
 *   1. Make the node generic?
 *      Replace int data with a type parameter T.
 *   2. Support doubly linked lists?
 *      Add a previous pointer and keep both links consistent.
 *   3. Avoid recursive toString on long lists?
 *      This toString prints only the next node's data, so it is already O(1).
 *   4. Make nodes immutable?
 *      Remove setters and set fields only through constructors.
 */
public class MyNode {

    public static void main(String[] args) {
        int[][] inputs = { {1, 2}, {7} };
        String[] expected = { "MyNode{data=1, next=2}", "MyNode{data=7, next=NULL}" };

        for (int i = 0; i < inputs.length; i++) {
            MyNode node = inputs[i].length == 1
                ? new MyNode(inputs[i][0])
                : new MyNode(inputs[i][0], new MyNode(inputs[i][1]));
            System.out.printf("values=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), node, expected[i]);
        }
    }

    private int data;
    private MyNode next;

    public MyNode() { }

    public MyNode(int data) {
        this.data = data;
        this.next = null;
    }

    public MyNode(int data, MyNode next) {
        this.data = data;
        this.next = next;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "data=" + data +
                ", next=" + (next != null ? next.getData() : "NULL") +
                '}';
    }
}
