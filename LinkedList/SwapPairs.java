package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Problem: Swap nodes in pairs in a given linked list.
 * 
 * Given a singly linked list, swap every two adjacent nodes and return the modified list.
 * 
 * Example:
 * Input:  1 -> 2 -> 3 -> 4
 * Output: 2 -> 1 -> 4 -> 3
 * 
 * Approach:
 * - Use a dummy node to simplify edge cases.
 * - Traverse the list in pairs and swap nodes by adjusting pointers.
 * - Maintain a `prev` pointer to keep track of the previous node for proper linking.
 * - Iterate until we reach the end of the list.
 * 
 * Time Complexity: O(N) - We traverse the list once.
 * Space Complexity: O(1) - No extra space is used apart from a few pointers.
 * 
 * LeetCode Link: https://leetcode.com/problems/swap-nodes-in-pairs/
 */
public class SwapPairs {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));

        SwapPairs swapper = new SwapPairs();
        ListNode resultHead = swapper.swapNodesInPairs(head);

        System.out.println(resultHead);
    }

    /**
     * Swaps adjacent nodes in pairs.
     * @param head The head of the linked list.
     * @return The new head after swapping nodes in pairs.
     */
    public ListNode swapNodesInPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head; // No swap needed for empty list or single node
        }

        ListNode dummy = new ListNode(-1); // Dummy node to handle edge cases
        dummy.next = head;
        ListNode previous = dummy;

        while (head != null && head.next != null) {
            ListNode firstNode = head;
            ListNode secondNode = head.next;

            // Swap adjacent nodes
            previous.next = secondNode;
            firstNode.next = secondNode.next;
            secondNode.next = firstNode;

            // Move pointers for next iteration
            previous = firstNode;
            head = firstNode.next;
        }
        return dummy.next;
    }
}
