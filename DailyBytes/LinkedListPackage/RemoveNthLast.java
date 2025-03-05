package DailyBytes.LinkedListPackage;

/**
 * This class contains a method to remove the nth node from the end of a linked list.
 * 
 * Algorithm:
 * - Calculate the length of the linked list.
 * - Traverse to the nth node from the end and remove it.
 * - Time Complexity: O(n)
 * - Space Complexity: O(1)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 */
public class RemoveNthLast {

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.push(new MyNode(1));
        list.push(new MyNode(2));
        list.push(new MyNode(3));
        list.push(new MyNode(4));
        list.push(new MyNode(5));

        System.out.print("Original list: ");
        list.printList();

        removeFromLast(list, 2);

        System.out.print("Modified list: ");
        list.printList();
    }

    /**
     * Removes the nth node from the end of the linked list.
     * @param list The linked list from which the node needs to be removed.
     * @param n The index from the end of the list of the node to be removed.
     */
    public static void removeFromLast(MyLinkedList list, int n) {
        if (n <= 0) {
            return;
        }

        MyNode head = list.getHead();
        if (head == null) {
            return;
        }

        int length = 0;
        MyNode temp = head;

        // Calculate the length of the linked list
        while (temp != null) {
            temp = temp.getNext();
            length++;
        }

        // If n is greater than the length of the list, do nothing
        if (n > length) {
            return;
        }

        temp = head;

        // Remove the head node if it is the nth node from the end
        if (n == length) {
            list.setHead(head.getNext());
            return;
        }

        // Traverse to the nth node from the end
        for (int i = 1; i < length - n; i++) {
            temp = temp.getNext();
        }

        // Remove the nth node from the end
        temp.setNext(temp.getNext().getNext());
    }
}
