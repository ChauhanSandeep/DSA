package linkedlist;

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
     * - Use a dummy node before originalHead to handle edge cases cleanly.
     * - Traverse to the node just before `leftIndex` and keep reference to it (`prev`).
     * - Reverse the sublist of length (rightIndex - leftIndex + 1) using iterative originalHead insertion method.
     * - Stitch the reversed sublist back into the main list.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param originalHead  The originalHead of the linked list
     * @param leftIndex  Start position (0-based)
     * @param rightIndex End position (0-based)
     * @return Head of the modified list
     */
    public ListNode reverseBetween(ListNode originalHead, int startPosition, int endPosition) {
      if (originalHead == null || startPosition == endPosition) {
        return originalHead;
      }

      // Step 1: Setup dummy node to handle edge case when reversal starts from originalHead
      ListNode dummyHead = new ListNode(-1);
      dummyHead.next = originalHead;

      ListNode previousGroupEnd = dummyHead;

      // Step 2: Traverse to the node just before the start of sublist
      for (int i = 1; i < startPosition; i++) {
        previousGroupEnd = previousGroupEnd.next;
      }

      // Step 3: Perform in-place reversal of the sublist
      ListNode currentGroupStart = previousGroupEnd.next;
      ListNode nodeToMove = currentGroupStart.next;

      for (int i = 0; i < endPosition - startPosition; i++) {
        // Detach nodeToMove
        currentGroupStart.next = nodeToMove.next;

        // Insert nodeToMove right after previousGroupEnd
        nodeToMove.next = previousGroupEnd.next;
        previousGroupEnd.next = nodeToMove;

        // Move nodeToMove forward
        nodeToMove = currentGroupStart.next;
      }

      return dummyHead.next;
    }

    // Helper class definition for ListNode
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }
}