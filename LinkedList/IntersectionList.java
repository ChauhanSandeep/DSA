package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;


/**
 * Problem: Find the intersection node of two singly linked lists.
 *
 * Two linked lists intersect if they share a common node (by reference, not by value).
 * The intersection point is defined as the first common node from the end where both lists meet.
 *
 * Example:
 * List A: 1 -> 2 -> 3 \
 *                     -> 8 -> 9
 * List B:       4 -> 5 /
 * Output: Node with value 8
 *
 * LeetCode Link:
 * https://leetcode.com/problems/intersection-of-two-linked-lists/
 *
 * Follow-Up Questions:
 * - Can you solve it with O(1) space and O(n) time without modifying the list? (Yes, use two-pointer approach)
 * - What if the list is extremely long? (Optimize length alignment or use the two-pointer no-length method)
 * - Can you find intersection in case of looped/circular linked lists? (Use Floyd’s cycle detection + intersection logic)
 */
public class IntersectionList {

  /**
   * Finds the intersection node using length alignment method.
   *
   * Steps:
   * - Calculate lengths of both lists.
   * - Move the head of the longer list ahead by difference in lengths.
   * - Move both pointers together until they meet or reach end.
   *
   * Time Complexity: O(N + M)
   * Space Complexity: O(1)
   */
  public static ListNode getIntersectionByLengthAlignment(ListNode head1, ListNode head2) {
      if (head1 == null || head2 == null) {
          return null;
      }

    int len1 = getLength(head1);
    int len2 = getLength(head2);

    // Advance the longer list to equalize the length difference
    if (len1 > len2) {
      head1 = advancePointer(head1, len1 - len2);
    } else {
      head2 = advancePointer(head2, len2 - len1);
    }

    // Move both pointers in sync until they meet
    while (head1 != null && head2 != null) {
        if (head1 == head2) {
            return head1;
        }
      head1 = head1.getNext();
      head2 = head2.getNext();
    }

    return null; // No intersection
  }

  /**
   * Optimized approach: Two-pointer traversal without calculating lengths.
   * This is better because it avoids the need to traverse the lists twice.
   *
   * Steps:
   * - Initialize two pointers at both list heads.
   * - When either pointer reaches end, redirect it to the other list’s head.
   * - They will meet at the intersection or at null after one full traversal.
   *
   * This works because:
   * Suppose you have two lists: List A and list B
   * a = length of unique part of List A
   * b = length of unique part of List B
   * c = length of common tail
   * When ptr1 reaches the end of List A, it switches to List B, and vice versa.
   * After the switch:
   *   - ptr1 will traverse a + c nodes in total.
   *   - ptr2 will traverse b + c nodes in total.
   * If they meet, they will be at the intersection node after both have traversed the same total length.
   *
   * Time Complexity: O(N + M)
   * Space Complexity: O(1)
   */
  public static ListNode getIntersectionByTwoPointer(ListNode head1, ListNode head2) {
      if (head1 == null || head2 == null) {
          return null;
      }

    ListNode ptr1 = head1;
    ListNode ptr2 = head2;

    // Traverse both lists, switching heads when reaching the end
    while (ptr1 != ptr2) {
      ptr1 = (ptr1 == null) ? head2 : ptr1.getNext();
      ptr2 = (ptr2 == null) ? head1 : ptr2.getNext();
    }

    return ptr1; // Either intersection node or null
  }

  /**
   * Returns the length of the linked list.
   */
  private static int getLength(ListNode node) {
    int count = 0;
    while (node != null) {
      count++;
      node = node.getNext();
    }
    return count;
  }

  /**
   * Advances the given node pointer by `steps` positions.
   */
  private static ListNode advancePointer(ListNode node, int steps) {
    while (steps-- > 0 && node != null) {
      node = node.getNext();
    }
    return node;
  }
}