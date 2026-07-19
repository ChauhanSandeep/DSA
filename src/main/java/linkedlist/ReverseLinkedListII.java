package linkedlist;

import java.util.Arrays;

/**
 * Problem: Reverse Linked List II
 *
 * Reverse the nodes between positions left and right in a singly linked list.
 * Positions are 1-based and inclusive, and nodes outside the window keep their
 * original relative order.
 *
 * Leetcode: https://leetcode.com/problems/reverse-linked-list-ii/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Dummy node | In-place sublist reversal
 *
 * Example:
 *   Input:  head = [1,2,3,4,5], left = 2, right = 4
 *   Output: [1,4,3,2,5]
 *   Why:    only [2,3,4] is reversed, while 1 and 5 stay in place.
 *
 * Follow-ups:
 *   1. Can this be one pass?
 *      Yes, locate prevNode once and use head insertion inside the window.
 *   2. Reverse by value range instead?
 *      Scan for boundaries by value, then apply the same rewiring.
 *   3. Reverse multiple ranges?
 *      Process ranges left to right so earlier changes do not invalidate later ones.
 *
 * Related: Reverse Linked List (206), Reverse Nodes in k-Group (25).
 */
public class ReverseLinkedListII {

  public static void main(String[] args) {
    ReverseLinkedListII solver = new ReverseLinkedListII();
    int[][] inputs = { {1, 2, 3, 4, 5}, {3, 5} };
    int[] lefts = {2, 1};
    int[] rights = {4, 2};
    int[][] expected = { {1, 4, 3, 2, 5}, {5, 3} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = solver.reverseBetween(head, lefts[i], rights[i]);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s left=%d right=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), lefts[i], rights[i], Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }

    /**
   * Intuition: keep the node before left fixed, then repeatedly lift the node
   * after currNode to the front of the sublist. That head-insertion motion
   * reverses only the requested window.
   *
   * Algorithm:
   *   1. Return head when the list is empty or left equals right.
   *   2. Create dummyHead and move prevNode to the node before left.
   *   3. Set currNode to the first node in the window.
   *   4. Repeat right - left times, moving currNode.next after prevNode.
   *
   * Time:  O(n) - the scan and rewiring touch nodes up to right once.
   * Space: O(1) - only a dummy and a few pointers are used.
   *
   * @param head head of the linked list
   * @param left first 1-based position to reverse
   * @param right last 1-based position to reverse
   * @return head after reversing the requested sublist
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