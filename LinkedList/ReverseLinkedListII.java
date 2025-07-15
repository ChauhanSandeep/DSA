package LinkedList;

/**
 * Problem Statement:
 * Given the head of a singly linked list and two integers left and right, reverse the nodes of the list
 * from position `left` to `right`, and return the modified list. Positions are 1-based.
 *
 * Example:
 * Input: head = [1,2,3,4,5], left = 2, right = 4
 * Output: [1,4,3,2,5]
 * Explanation: The sublist [2,3,4] is reversed in-place to [4,3,2].
 *
 * Leetcode URL:
 * https://leetcode.com/problems/reverse-linked-list-ii
 *
 * Follow-up Questions:
 * 1. Can you perform the reversal in a single pass and in-place?
 *    → Yes, this solution does exactly that by carefully stitching sublist pointers.
 *
 * 2. How would this change if you needed to reverse nodes based on value range instead of position?
 *    → You'd need to scan for matching nodes by value and use dummy heads to build sublists accordingly.
 */

public class ReverseLinkedListII {

    /**
     * Reverses a sublist of the linked list from position `leftIndex` to `rightIndex`.
     *
     * Algorithm:
     * - Use a dummy node before head to handle edge cases cleanly.
     * - Traverse to the node just before `leftIndex` and keep reference to it (`prev`).
     * - Reverse the sublist of length (rightIndex - leftIndex + 1) using iterative head insertion method.
     * - Stitch the reversed sublist back into the main list.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param head  The head of the linked list
     * @param leftIndex  Start position (0-based)
     * @param rightIndex End position (0-based)
     * @return Head of the modified list
     */
    public ListNode reverseBetween(ListNode head, int leftIndex, int rightIndex) {
      if (head == null || leftIndex == rightIndex) {
        return head;
      }

      ListNode dummy = new ListNode(0);
      dummy.next = head;

      ListNode prev = dummy;

      // Step 1: Move `prev` to node just before `leftIndex`
      for (int i = 0; i < leftIndex; i++) {
        prev = prev.next;
      }

      // Step 2: Reverse sublist using head-insertion method
      ListNode curr = prev.next;      // Start of the sublist to reverse
      ListNode then = curr.next;      // Node to be moved

      for (int i = 0; i < rightIndex - leftIndex; i++) {
        curr.next = then.next;      // Detach `then` from list
        then.next = prev.next;      // Point `then` to the front of the reversed sublist
        prev.next = then;           // Insert `then` right after `prev`
        then = curr.next;           // Move to next node to reverse
      }

      return dummy.next;
    }

    // Helper class definition for ListNode
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }
}