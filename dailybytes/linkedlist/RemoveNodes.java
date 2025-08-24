package dailybytes.linkedlist;

/**
 * This class contains a method to remove all nodes containing a specified value from a linked list.
 *
 * Algorithm:
 * - Traverse the linked list and remove nodes that match the specified value.
 * - Time Complexity: O(n)
 * - Space Complexity: O(1)
 *
 * LeetCode Problem Link: https://leetcode.com/problems/remove-linked-list-elements/
 */
public class RemoveNodes {

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.push(new MyNode(3));
        list.push(new MyNode(3));
        list.push(new MyNode(2));
        list.push(new MyNode(3));

        System.out.print("Original list: ");
        list.printList();

        removeNodes(list, 3);

        System.out.print("Modified list: ");
        list.printList();
    }

    /**
     * Removes all nodes containing the specified value from the linked list.
     * @param list The linked list from which nodes need to be removed.
     * @param val The value to be removed from the linked list.
     */
    public static void removeNodes(MyLinkedList list, int val) {
        MyNode head = list.getHead();

        // Remove head nodes that match the specified value
        while (head != null && head.getData() == val) {
            head = head.getNext();
        }

        MyNode temp = head;

        // Traverse the list and remove matching nodes
        while (temp != null && temp.getNext() != null) {
            if (temp.getNext().getData() == val) {
                temp.setNext(temp.getNext().getNext());
            } else {
                temp = temp.getNext();
            }
        }

        list.setHead(head);
    }
}
