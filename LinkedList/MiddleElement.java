package LinkedList;

import java.util.Optional;


/**
 * Problem: Find the Middle Element of a Singly Linked List
 * Leetcode Link: https://leetcode.com/problems/middle-of-the-linked-list/
 *
 * Given the head of a singly linked list, return the middle node. If there are two middle nodes,
 * return the second middle node.
 *
 * Example:
 * Input: 1 -> 2 -> 3 -> 4 -> 5
 * Output: 3
 *
 * Input: 1 -> 2 -> 3 -> 4 -> 5 -> 6
 * Output: 4
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. Can you find the kth node from the end in one pass?
 *    - Yes, using the same two-pointer (fast/slow) approach with a gap of k.
 *    - Link: https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 * 2. Can you solve it if the list is a circular linked list?
 *    - Requires cycle detection and length calculation before finding mid.
 * 3. How would you modify this for a doubly linked list?
 *    - Same logic applies, though reverse traversal becomes easier.
 */
public class MiddleElement {

  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(4));
    list.add(new ListNode(5));
    list.add(new ListNode(6));

    Optional<Integer> middleValue = findMiddleElement(head);
    System.out.println("Middle Element: " + middleValue.orElse(-1));
  }

  /**
   * Finds the middle element in a singly linked list.
   *
   * Steps:
   * - Use two pointers: slow and fast.
   * - Move slow by one step and fast by two steps.
   * - When fast reaches the end, slow will be at the middle.
   *
   * Algorithm:
   * - Two-pointer (slow-fast) technique.
   *
   * Time Complexity: O(N) — Single pass through the list.
   * Space Complexity: O(1) — No extra data structures used.
   *
   * @param head The head node of the linked list.
   * @return An Optional containing the value of the middle node.
   */
  private static Optional<Integer> findMiddleElement(ListNode head) {
      if (head == null) {
          return Optional.empty();
      }

    ListNode slow = head;
    ListNode fast = head;

    // Advance fast by two steps and slow by one step until fast reaches end
    while (fast != null && fast.getNext() != null) {
      slow = slow.getNext();
      fast = fast.getNext().getNext();
    }

    return Optional.of(slow.getVal());
  }
}

