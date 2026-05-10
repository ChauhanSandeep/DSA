package linkedlist;

/**
 * Problem Statement:
 * Given the head of a singly linked list and two integers left and right,
 * reverse the nodes of the list
 * from position `left` to `right`, and return the modified list. Positions are
 * 1-based.
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
 * → Yes, this solution does exactly that by carefully stitching sublist
 * pointers.
 *
 * 2. How would this change if you needed to reverse nodes based on value range
 * instead of position?
 * → You'd need to scan for matching nodes by value and use dummy heads to build
 * sublists accordingly.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */

public class ReverseLinkedListII {

  /**
   * Reverses a sublist of the linked list from position `left` to `right`
   * (1-based, inclusive).
   *
   * Algorithm:
   * - Use a dummy node before head to handle edge cases cleanly.
   * - Traverse to the node just before `left` and keep reference to it (`prev`).
   * - Reverse the sublist of length (right - left + 1) using iterative
   * head-insertion method.
   * - Stitch the reversed sublist back into the main list.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @param head  Head of the linked list
   * @param left  Start position (1-based, inclusive)
   * @param right End position (1-based, inclusive)
   * @return Head of the modified list
   */
  public ListNode reverseBetween(ListNode head, int left, int right) {
    if (head == null || left == right) {
      return head;
    }

    // Step 1: Setup dummy node to handle edge case when reversal starts from head
    ListNode dummyHead = new ListNode(-1);
    dummyHead.next = head;

    ListNode prevNode = dummyHead;

    // Step 2: Traverse to the node just before the start of sublist
    for (int i = 1; i < left; i++) {
      prevNode = prevNode.next;
    }

    // Step 3: Perform in-place reversal of the sublist
    ListNode currNode = prevNode.next;
    ListNode nextNode = currNode.next;

    for (int i = 0; i < right - left; i++) {
      nextNode = currNode.next;

      // SRK method to reverse a sublist
      currNode.next = nextNode.next;
      nextNode.next = prevNode.next;
      prevNode.next = nextNode;
    }

    return dummyHead.next;
  }

  // Helper class definition for ListNode
  public static class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
      this.val = val;
    }
  }
}