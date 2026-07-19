package linkedlist;

import java.util.Arrays;

/**
 * Problem: Rotate List
 *
 * Rotate a singly linked list to the right by k places. Nodes shifted off the
 * tail wrap to the front, and all other relative ordering is preserved.
 *
 * Leetcode: https://leetcode.com/problems/rotate-list/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Circular list | Length modulo
 *
 * Example:
 *   Input:  head = [1,2,3,4,5], k = 2
 *   Output: [4,5,1,2,3]
 *   Why:    the last two nodes wrap to the front in order.
 *
 * Follow-ups:
 *   1. Rotate left by k?
 *      Rotate right by length - (k % length).
 *   2. What if k is huge?
 *      Reduce it with k % length before rewiring.
 *   3. What if the input is circular?
 *      Find the intended break point and avoid adding another cycle.
 *
 * Related: Rotate Array (189), Split Linked List in Parts (725).
 */
public class RotateList {

  public static void main(String[] args) {
    int[][] inputs = { {1, 2, 3, 4, 5}, {0, 1, 2} };
    int[] rotations = {2, 4};
    int[][] expected = { {4, 5, 1, 2, 3}, {2, 0, 1} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = rotateRight(head, rotations[i]);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s k=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), rotations[i], Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }

    /**
   * Intuition: right rotation is simple after temporarily making the list a
   * circle. Once tail points to head, choose the new tail length - k steps from
   * the old head and break the circle there.
   *
   * Algorithm:
   *   1. Return head for an empty list, single node, or k = 0.
   *   2. Walk to tail while counting length.
   *   3. Reduce k by length and return head if the effective rotation is zero.
   *   4. Link tail to head, find newTail, set newHead, and break the circle.
   *
   * Time:  O(n) - one pass counts nodes and one partial pass finds the break.
   * Space: O(1) - only pointers and counters are stored.
   *
   * @param head head of the linked list
   * @param k number of right rotations
   * @return new head after rotation
   */
  public static ListNode rotateRight(ListNode head, int k) {
    if (head == null || head.getNext() == null || k == 0) {
      return head;
    }

    // Step 1: Compute length and find tail
    ListNode tail = head;
    int length = 1;

    while (tail.getNext() != null) {
      tail = tail.getNext();
      length++;
    }

    // Step 2: Normalize k
    k = k % length;
    if (k == 0) {
      return head;
    }

    // Step 3: Make the list circular
    tail.setNext(head);

    // Step 4: Find new tail (length - k steps from start)
    int stepsToNewTail = length - k;
    ListNode newTail = head;
    for (int i = 1; i < stepsToNewTail; i++) {
      newTail = newTail.getNext();
    }

    // Step 5: Set new head and break circle
    ListNode newHead = newTail.getNext();
    newTail.setNext(null);

    return newHead;
  }
}
