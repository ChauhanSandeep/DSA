package linkedlist;

import java.util.Arrays;

/**
 * Problem: Reverse Nodes in k-Group
 *
 * Reverse nodes in consecutive groups of size k. A trailing group with fewer
 * than k nodes must remain in its original order.
 *
 * Leetcode: https://leetcode.com/problems/reverse-nodes-in-k-group/ (Hard)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Group reversal | Dummy node
 *
 * Example:
 *   Input:  head = [1,2,3,4,5], k = 2
 *   Output: [2,1,4,3,5]
 *   Why:    the full pairs [1,2] and [3,4] reverse, while [5] remains.
 *
 * Follow-ups:
 *   1. Reverse groups from the right?
 *      Compute length, skip the left offset, then reverse full groups.
 *   2. Vary k by segment?
 *      Read group sizes from an array and verify each group before reversal.
 *   3. Can recursion solve it?
 *      Reverse the first k nodes, then recurse on the rest.
 *
 * Related: Reverse Linked List (206), Swap Nodes in Pairs (24).
 */
public class ReverseListInKGroup {

  public static void main(String[] args) {
    ReverseListInKGroup solver = new ReverseListInKGroup();
    int[][] inputs = { {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5} };
    int[] groupSizes = {2, 3};
    int[][] expected = { {2, 1, 4, 3, 5}, {3, 2, 1, 4, 5} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = solver.reverseInGroupsOfK(head, groupSizes[i]);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s k=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), groupSizes[i], Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }


  /**
   * Intuition: reuse the same SRK head-insertion trick from Reverse Linked
   * List II — repeatedly lift the node right after currentGroupStart to just
   * after previousGroupEnd. Running that move groupSize - 1 times reverses
   * an entire group without a separate reverse helper. Advance previousGroupEnd
   * to currentGroupStart (now the group tail) and repeat.
   *
   * Algorithm:
   *   1. Return originalHead when the list is empty or groupSize <= 1.
   *   2. Set previousGroupEnd to dummyHead.
   *   3. While a full group of groupSize exists starting at previousGroupEnd.next:
   *      a. Set currentGroupStart = previousGroupEnd.next (becomes the tail).
   *      b. Repeat groupSize - 1 times: lift currentGroupStart.next to the group front.
   *      c. Advance previousGroupEnd to currentGroupStart.
   *
   * Time:  O(n) - each node is moved exactly once across all groups.
   * Space: O(1) - only a dummy and a few pointers are used.
   *
   * @param originalHead head of the linked list
   * @param groupSize number of nodes per reversed group
   * @return head after reversing every complete group
   */
  public ListNode reverseInGroupsOfK(ListNode originalHead, int groupSize) {
    if (originalHead == null || groupSize <= 1) return originalHead;

    ListNode dummyHead = new ListNode(-1);
    dummyHead.next = originalHead;
    ListNode prev = dummyHead;

    while (hasKNodes(prev.next, groupSize)) {
      // --- Step: SRK head-insertion, repeated groupSize-1 times --------
      ListNode curr = prev.next; // will become the tail

      for (int i = 0; i < groupSize - 1; i++) {
        ListNode next = curr.next;
        curr.next = next.next;
        next.next = prev.next;
        prev.next = next;
      }
      prev = curr; // currentGroupStart is now the group tail
    }

    return dummyHead.next;
  }

  /** Returns true if at least k nodes exist starting from startNode. */
  private boolean hasKNodes(ListNode startNode, int k) {
    int count = 0;
    while (startNode != null && count < k) {
      count++;
      startNode = startNode.next;
    }
    return count == k;
  }
}
