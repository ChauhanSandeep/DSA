package linkedlist;

/**
 * Problem: Rotate a Linked List by K Places
 * Leetcode Link: https://leetcode.com/problems/rotate-list/
 *
 * Given the head of a singly linked list and an integer k, rotate the list to the right by k places.
 *
 * Example:
 * Input: 1 -> 2 -> 3 -> 4 -> 5 -> 6, k = 3
 * Output: 4 -> 5 -> 6 -> 1 -> 2 -> 3
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. Can you do this in constant space?
 *    - Yes, by modifying the pointers in-place.
 * 2. How would you rotate to the left by k?
 *    - Rotate right by (length - k) positions.
 * 3. What if the list is circular?
 *    - Track cycle, break at the correct location to rotate effectively.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RotateList {
  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(4));
    list.add(new ListNode(5));
    list.add(new ListNode(6));

    ListNode newHead = rotateRight(head, 3);
    System.out.println(newHead);
  }

  /**
   * Rotates the linked list to the right by k places.
   *
   * Steps:
   * 1. Handle edge cases (null list, k = 0, single node).
   * 2. Find the length of the list and connect tail to head to make it circular.
   * 3. Compute effective k by taking k % length.
   * 4. Find the new tail node (length - k steps from the start).
   * 5. Set the new head and break the circle by setting newTail.next = null.
   *
   * Algorithm:
   * - Two-pass approach: one to calculate length, one to find new tail.
   *
   * Time Complexity: O(N)
   * Space Complexity: O(1)
   *
   * @param head The head node of the linked list.
   * @param k    Number of positions to rotate.
   * @return New head of the rotated list.
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
