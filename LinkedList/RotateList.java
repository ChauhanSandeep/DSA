package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * ✅ Problem: Rotate a Linked List by K Places
 * 🔗 LeetCode: https://leetcode.com/problems/rotate-list/
 *
 * 🔍 **Approach:**
 * 1. Find the length of the linked list.
 * 2. Compute the effective rotation index using `k % length` to avoid redundant rotations.
 * 3. Traverse to the new tail (length - k - 1).
 * 4. Set the new head as `newTail.next` and break the connection.
 * 5. Attach the old tail to the old head to form a circular list, then break at the new tail.
 *
 * 📊 **Time Complexity:** O(N) - Single traversal to find length, another to rotate.  
 * 🛠 **Space Complexity:** O(1) - Only a few extra pointers used.
 */
public class RotateList {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        System.out.println("Original List:");
        list.printList(head);

        ListNode newHead = rotateList(head, 3);
        System.out.println("After Rotation:");
        list.printList(newHead);
    }

    /**
     * Rotates a linked list to the right by `k` positions.
     *
     * @param head Head of the linked list.
     * @param k Number of positions to rotate.
     * @return New head of the rotated linked list.
     */
    public static ListNode rotateList(ListNode head, int k) {
        if (head == null || head.getNext() == null || k == 0) {
            return head; // No rotation needed
        }

        // Step 1: Compute length and find tail
        ListNode tail = head;
        int length = 1;
        while (tail.getNext() != null) {
            tail = tail.getNext();
            length++;
        }

        // Step 2: Compute the effective rotation index
        k = k % length;
        if (k == 0) return head; // No change if k is a multiple of length

        // Step 3: Find new tail (length - k - 1) and new head (length - k)
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.getNext();
        }
        ListNode newHead = newTail.getNext();

        // Step 4: Perform rotation
        tail.setNext(head);  // Make it circular
        newTail.setNext(null); // Break circularity at new tail

        return newHead;
    }
}
