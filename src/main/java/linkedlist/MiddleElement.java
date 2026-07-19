package linkedlist;


import java.util.Arrays;

/**
 * Problem: Middle of the Linked List
 *
 * Return the middle node of a singly linked list. If there are two middle nodes,
 * return the second one, matching the standard slow/fast pointer behavior.
 *
 * Leetcode: https://leetcode.com/problems/middle-of-the-linked-list/ (Easy)
 * Rating:   1232
 * Pattern:  Linked list | Slow and fast pointers
 *
 * Example:
 *   Input:  head = [1,2,3,4,5,6]
 *   Output: 4
 *   Why:    even-length lists have two middles, and the second is required.
 *
 * Follow-ups:
 *   1. Find kth from the end in one pass?
 *      Start fast k nodes ahead, then move both pointers together.
 *   2. What if the list is circular?
 *      Detect the cycle and measure usable length first.
 *   3. Return the first middle instead?
 *      Stop before fast can advance two full steps.
 *
 * Related: Remove Nth Node From End of List (19), Linked List Cycle (141).
 */
public class MiddleElement {

  public static void main(String[] args) {
    int[][] inputs = { {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5, 6} };
    int[] expected = {3, 4};
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode middle = findMiddleElement(head);
      System.out.printf("head=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), middle == null ? -1 : middle.getVal(), expected[i]);
    }
  }


    /**
   * Intuition: fast moves twice as quickly as slow. When fast reaches the end,
   * slow has moved half as many steps and therefore sits on the middle node.
   *
   * Algorithm:
   *   1. Return null for an empty list.
   *   2. Start slow and fast at head.
   *   3. Move slow one step and fast two steps while fast can move two.
   *   4. Return slow.
   *
   * Time:  O(n) - fast scans the list once.
   * Space: O(1) - only slow and fast are stored.
   *
   * @param head head of the linked list
   * @return middle node, or null for an empty list
   */
  private static ListNode findMiddleElement(ListNode head) {
      if (head == null) {
          return null;
      }

    ListNode slow = head;
    ListNode fast = head;

    // Advance fast by two steps and slow by one step until fast reaches end
    while (fast != null && fast.getNext() != null) {
      slow = slow.getNext();
      fast = fast.getNext().getNext();
    }

    return slow;
  }
}

