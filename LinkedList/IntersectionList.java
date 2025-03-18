package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Finds the intersection node of two singly linked lists, if they intersect.
 * 
 * Two linked lists intersect if they share a common node (by reference, not value).
 * This method identifies the intersection by:
 *  1. Determining the lengths of both lists.
 *  2. Aligning the starting position of the longer list.
 *  3. Traversing both lists simultaneously to find the common node.
 *
 * Time Complexity: O(N + M), where N and M are the lengths of the two lists.
 * Space Complexity: O(1), as we only use a few pointers.
 */
public class IntersectionList {
    public static void main(String[] args) {
        // Creating first linked list: 1 -> 2 -> 3 -> 4 -> 5 -> 6
        ListNode head1 = new ListNode(1);
        ListNode intersectionNode = new ListNode(4);
        
        LinkedList list1 = new LinkedList(head1);
        list1.add(new ListNode(2));
        list1.add(new ListNode(3));
        list1.add(intersectionNode);
        list1.add(new ListNode(5));
        list1.add(new ListNode(6));

        // Creating second linked list: 10 -> 11 -> 12 -> 4 -> 5 -> 6
        ListNode head2 = new ListNode(10);
        LinkedList list2 = new LinkedList(head2);
        head2.setNext(new ListNode(11));
        head2.getNext().setNext(new ListNode(12));
        head2.getNext().getNext().setNext(intersectionNode); // Intersection occurs at node 4

        // Printing both lists
        System.out.println("First Linked List:");
        list1.printList(head1);
        System.out.println("Second Linked List:");
        list2.printList(head2);

        // Finding intersection
        ListNode result = getIntersectionNode(head1, head2);
        if (result != null) {
            System.out.println("Intersection found at node with value: " + result.getVal());
        } else {
            System.out.println("No intersection found.");
        }
    }

    /**
     * Finds the intersection node of two singly linked lists.
     *
     * @param head1 The head of the first linked list.
     * @param head2 The head of the second linked list.
     * @return The intersection node, or null if no intersection exists.
     */
    public static ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) return null;

        int len1 = getLength(head1);
        int len2 = getLength(head2);

        // Align the longer list to the same starting position as the shorter one
        if (len1 > len2) {
            head1 = advanceBy(head1, len1 - len2);
        } else {
            head2 = advanceBy(head2, len2 - len1);
        }

        // Traverse both lists together to find the intersection
        while (head1 != null && head2 != null) {
            if (head1 == head2) return head1;
            head1 = head1.getNext();
            head2 = head2.getNext();
        }

        return null; // No intersection found
    }

    /**
     * Returns the length of a linked list.
     *
     * @param node The head of the linked list.
     * @return The length of the linked list.
     */
    public static int getLength(ListNode node) {
        int length = 0;
        while (node != null) {
            length++;
            node = node.getNext();
        }
        return length;
    }

    /**
     * Moves a linked list pointer forward by a given number of steps.
     *
     * @param node The starting node.
     * @param steps The number of steps to advance.
     * @return The new starting node after advancing.
     */
    private static ListNode advanceBy(ListNode node, int steps) {
        while (steps > 0 && node != null) {
            node = node.getNext();
            steps--;
        }
        return node;
    }
}
