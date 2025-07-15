package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Problem: Reverse Nodes in k-Group
 * LeetCode: https://leetcode.com/problems/reverse-nodes-in-k-group/
 * Problem Statement:
 * Given the head of a linked list, reverse the nodes of the list `k` at a time and return the modified list.
 * If the number of nodes is not a multiple of `k`, then the remaining nodes at the end should remain unchanged.
 * For example, given the list 1 -> 2 -> 3 -> 4 -> 5 and k = 2,
 * the output should be 2 -> 1 -> 4 -> 3 -> 5.
 *
 * **Approach:**
 * Count the total number of nodes.
 * Reverse every group of `k` nodes while keeping track of links.
 * Link the reversed segments correctly and preserve the remaining nodes.
 *
 * **Time Complexity:** O(N) - Each node is processed twice.
 * **Space Complexity:** O(1) - Constant extra space is used.
 */
public class ReverseListInKGroup {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));

        int k = 2;
        ListNode result = new ReverseListInKGroup().reverseKGroup(head, k);
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
        if (head == null || k <= 1) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode groupPrev = dummy;

        while (true) {
            // Step 1: Get the kth node ahead (to check if a full group exists)
            ListNode kth = getKthNode(groupPrev, k);
            if (kth == null) break;

            // Step 2: Reverse the group using head-insertion (with 'then' pointer)
            ListNode curr = groupPrev.next;
            ListNode then = curr.next;

            for (int i = 1; i < k; i++) {
                curr.next = then.next;
                then.next = groupPrev.next;
                groupPrev.next = then;
                then = curr.next;
            }

            // Step 3: Move to the next group
            groupPrev = curr; // `curr` is now at the end of the reversed group
        }

        return dummy.next;
    }

    private ListNode getKthNode(ListNode start, int k) {
        while (k-- > 0 && start != null) {
            start = start.next;
        }
        return start;
    }
}
