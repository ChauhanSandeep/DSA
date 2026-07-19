package linkedlist;


import java.util.Arrays;

/**
 * Problem: Intersection of Two Linked Lists
 *
 * Return the first node shared by two singly linked lists by reference, not by
 * value. If the lists do not share a node, return null without modifying either
 * list.
 *
 * Leetcode: https://leetcode.com/problems/intersection-of-two-linked-lists/ (Easy)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Length alignment | Two pointers
 *
 * Example:
 *   Input:  A = [1,2,8,9], B = [4,5,8,9]
 *   Output: 8
 *   Why:    both lists point to the same node with value 8.
 *
 * Follow-ups:
 *   1. Can this avoid computing lengths?
 *      Use two pointers that switch heads when they reach null.
 *   2. What if a list has a cycle?
 *      Detect cycle starts first, then handle cyclic intersection cases.
 *   3. Can you find all shared nodes?
 *      Once the first shared node is found, every following node is shared.
 *
 * Related: Linked List Cycle II (142).
 */
public class IntersectionList {

  public static void main(String[] args) {
    ListNode shared = new ListNode(8); shared.next = new ListNode(9);
    ListNode head1 = new ListNode(1); head1.next = new ListNode(2); head1.next.next = shared;
    ListNode head2 = new ListNode(4); head2.next = new ListNode(5); head2.next.next = shared;
    ListNode output = getIntersectionByTwoPointer(head1, head2);
    System.out.printf("listA=%s listB=%s -> %d  expected=%d%n", Arrays.toString(new int[] {1, 2, 8, 9}), Arrays.toString(new int[] {4, 5, 8, 9}), output == null ? -1 : output.val, 8);
    ListNode noA = new ListNode(1); noA.next = new ListNode(2);
    ListNode noB = new ListNode(3);
    ListNode noOutput = getIntersectionByTwoPointer(noA, noB);
    System.out.printf("listA=%s listB=%s -> %d  expected=%d%n", Arrays.toString(new int[] {1, 2}), Arrays.toString(new int[] {3}), noOutput == null ? -1 : noOutput.val, -1);
  }

    /**
   * Intuition: if both pointers have the same remaining distance to the tail,
   * the first equal reference they encounter is the intersection. Advancing the
   * longer list by the length difference creates that alignment.
   *
   * Algorithm:
   *   1. Return null if either list is empty.
   *   2. Measure len1 and len2.
   *   3. Advance the longer head by the length difference.
   *   4. Move both heads together until they match or reach the end.
   *
   * Time:  O(n + m) - each list is traversed a constant number of times.
   * Space: O(1) - only pointers and lengths are stored.
   *
   * @param head1 head of the first linked list
   * @param head2 head of the second linked list
   * @return first shared node by reference, or null
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