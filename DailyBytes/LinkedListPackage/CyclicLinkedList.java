package DailyBytes.LinkedListPackage;

/**
 * This class detects if a linked list contains a cycle.
 * 
 * Algorithm:
 * - Use Floyd's Cycle-Finding Algorithm (Tortoise and Hare) to check for the presence of a cycle.
 * - Time Complexity: O(n)
 * - Space Complexity: O(1)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/linked-list-cycle/
 */
public class CyclicLinkedList {

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.push(new MyNode(1));
        list.push(new MyNode(2));
        list.push(new MyNode(3));
        list.push(new MyNode(1)); // Creates a cycle for testing

        boolean isCycle = hasCycle(list);
        System.out.println("Does linked list contain a cycle? " + isCycle);
    }

    /**
     * Given a linked list containing unique numbers, return whether or not it has a cycle.
     * A cycle is a circular arrangement where one node points back to a previous node.
     * 
     * @param list The input linked list.
     * @return True if the linked list contains a cycle, false otherwise.
     */
    public static boolean hasCycle(MyLinkedList list) {
        MyNode slow = list.getHead();
        MyNode fast = list.getHead();

        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}

class MyLinkedList {
    private MyNode head;

    public MyNode getHead() {
        return head;
    }

    public void push(MyNode node) {
        node.setNext(head);
        head = node;
    }
}

class MyNode {
    private int data;
    private MyNode next;

    public MyNode(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }
}
