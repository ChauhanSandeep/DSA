package DailyBytes.LinkedListPackage;
import java.util.*;

public class CyclicLinkedList {
    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        MyNode node1 = new MyNode(1);
        MyNode node2 = new MyNode(2);
        MyNode node3 = new MyNode(3);
        MyNode node4 = new MyNode(4);

        list.push(node4);
        list.push(node3);
        list.push(node2);
        list.push(node1);

        // Creating a cycle: 4 → 2 (node4 points back to node2)
        node4.setNext(node2);

        System.out.println("Does the linked list contain a cycle? " + findCycle(list));
    }

    /**
     * Corrected cycle detection using Floyd's Cycle Detection Algorithm
     */
    public static boolean findCycle(MyLinkedList list) {
        MyNode slow = list.getHead();
        MyNode fast = list.getHead();

        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();        // Move slow pointer one step
            fast = fast.getNext().getNext(); // Move fast pointer two steps

            if (slow == fast) return true; // If they meet, there is a cycle
        }

        return false; // No cycle
    }
}