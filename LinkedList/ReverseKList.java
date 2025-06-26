package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * ✅ Problem: Reverse Nodes in k-Group
 * 🔗 LeetCode: https://leetcode.com/problems/reverse-nodes-in-k-group/
 * 📚 Problem Statement:
 * Given the head of a linked list, reverse the nodes of the list `k` at a time and return the modified list.
 * If the number of nodes is not a multiple of `k`, then the remaining nodes at the end should remain unchanged.
 * For example, given the list 1 -> 2 -> 3 -> 4 -> 5 and k = 2,
 *
 * 🔍 **Approach:**
 * 1️⃣ Count the total number of nodes.
 * 2️⃣ Reverse every group of `k` nodes while keeping track of links.
 * 3️⃣ Link the reversed segments correctly and preserve the remaining nodes.
 *
 * 📊 **Time Complexity:** O(N) - Each node is processed twice.
 * 🛠 **Space Complexity:** O(1) - Constant extra space is used.
 */
public class ReverseKList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));

        int k = 2;
        ListNode result = new ReverseKList().reverseKGroup(head, k);
        System.out.println("Reversed in groups of " + k + ": " + result);
    }

    /**
     *
     * Algorithm:
     * 1. Traverse the list and reverse the first 'k' nodes.
     * 2. Recursively call the function for the next part of the list.
     *
     * Time Complexity: O(N) - Each node is visited once.
     * Space Complexity: O(N) - Due to recursive calls.
     *
     * @param head Head of the linked list.
     * @param k Group size for reversal.
     * @return New head after group-wise reversal.
     */
    public static ListNode reverseInGroupsRecursive(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head; // No change needed
        }

        ListNode prev = null, current = head, nextNode;
        int count = 0;

        // Reverse first 'k' nodes
        while (current != null && count < k) {
            nextNode = current.getNext();
            current.setNext(prev);
            prev = current;
            current = nextNode;
            count++;
        }

        // Recursively reverse the next part and attach it
        if (current != null) {
            head.setNext(reverseInGroupsRecursive(current, k));
        }

        return prev; // New head of the reversed section
    }

    /**
     * Reverses nodes of a linked list in groups of `k`.
     * @param head Head of the linked list.
     * @param k Group size for reversal.
     * @return New head of the modified list.
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 1) return head; // Edge case: No need to reverse

        int totalNodes = countNodes(head);
        ListNode dummy = new ListNode(0); // Dummy node for easy list manipulation
        dummy.next = head;

        ListNode prevTail = dummy; // Last node of the previous reversed group
        ListNode curr = head;

        while (totalNodes >= k) {
            ListNode groupHead = curr; // Store head of the current group
            ListNode prev = null;
            ListNode next = null;

            // Reverse k nodes
            for (int i = 0; i < k; i++) {
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }

            // Link the reversed group
            prevTail.next = prev;
            groupHead.next = curr;
            prevTail = groupHead; // Update prevTail for the next iteration

            totalNodes -= k;
        }

        return dummy.next; // Return the new head (skip dummy node)
    }

    /**
     * Counts the number of nodes in a linked list.
     * @param head Head of the list.
     * @return Total node count.
     */
    private int countNodes(ListNode head) {
        int count = 0;
        while (head != null) {
            count++;
            head = head.next;
        }
        return count;
    }
}
