package linkedlist;

import java.util.Arrays;

/**
 * Problem: Odd Even Linked List
 *
 * Reorder a linked list so nodes at odd positions come first, followed by nodes
 * at even positions. Positions are 1-based, and relative order inside each group
 * must be preserved.
 *
 * Leetcode: https://leetcode.com/problems/odd-even-linked-list/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | In-place partition | Odd/even pointers
 *
 * Example:
 *   Input:  head = [1,2,3,4,5]
 *   Output: [1,3,5,2,4]
 *   Why:    positions 1,3,5 come before positions 2,4.
 *
 * Follow-ups:
 *   1. Group by value parity instead?
 *      Build odd-value and even-value chains based on val % 2.
 *   2. Split into k position classes?
 *      Maintain k heads and tails, then concatenate them.
 *   3. Can it stay O(1) space?
 *      Yes, relink existing odd and even chains in place.
 *
 * Related: Partition List (86), Split Linked List in Parts (725).
 */
public class OddEvenLinkedList {

  public static void main(String[] args) {
    int[][] inputs = { {1, 2, 3, 4, 5}, {1} };
    int[][] expected = { {1, 3, 5, 2, 4}, {1} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = oddEvenList(head);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }


    /**
   * Intuition: odd nodes are already in the desired order if each odd pointer
   * skips over the next even node. The even chain is built the same way, and its
   * saved head is attached after the final odd node.
   *
   * Algorithm:
   *   1. Return head for an empty or single-node list.
   *   2. Start odd at head, even at head.getNext(), and save evenHead.
   *   3. Rewire odd to the next odd and even to the next even while a pair remains.
   *   4. Attach evenHead after odd and return head.
   *
   * Time:  O(n) - every node is rewired at most once.
   * Space: O(1) - only a few pointers are stored.
   *
   * @param head head of the linked list
   * @return reordered list with odd-indexed nodes first
   */
  public static ListNode oddEvenList(ListNode head) {
      if (head == null || head.getNext() == null) {
          return head;
      }

    ListNode odd = head;                     // Points to current odd node
    ListNode even = head.getNext();          // Points to current even node
    ListNode evenHead = even;                // To reconnect at the end

    while (even != null && even.getNext() != null) {
      odd.setNext(even.getNext());
      odd = odd.getNext();

      even.setNext(odd.getNext());
      even = even.getNext();
    }

    odd.setNext(evenHead); // Attach even list after odd list
    return head;
  }

  /**
   * Alternative approach using explicit odd and even list construction.
   * This approach is more intuitive but uses slightly more space for dummy nodes.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   */
  public ListNode oddEvenListAlternative(ListNode head) {
    if (head == null) return null;

    // Create dummy heads for odd and even lists
    ListNode oddDummy = new ListNode(0);
    ListNode evenDummy = new ListNode(0);
    ListNode oddTail = oddDummy;
    ListNode evenTail = evenDummy;

    boolean isOddIndex = true;

    while (head != null) {
      if (isOddIndex) {
        oddTail.next = head;
        oddTail = oddTail.next;
      } else {
        evenTail.next = head;
        evenTail = evenTail.next;
      }

      head = head.next;
      isOddIndex = !isOddIndex;
    }

    // Terminate even list and connect odd list to even list
    evenTail.next = null;
    oddTail.next = evenDummy.next;

    return oddDummy.next;
  }
}
