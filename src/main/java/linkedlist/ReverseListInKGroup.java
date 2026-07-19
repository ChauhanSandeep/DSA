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
   * Problem: Reverse nodes in k-group from a singly linked list.
   *
   * Input: 1 -> 2 -> 3 -> 4 -> 5, k = 3
   * Output: 3 -> 2 -> 1 -> 4 -> 5
   *
   * Approach:
   * - Use a dummy node to simplify pointer management.
   * - Maintain pointers to the end of the last processed group and the start of the current group.
   * - Traverse the list in k-sized groups.
   * - For each group:
   *   - Verify that k nodes exist.
   *   - Reverse the k nodes in-place.
   *   - Reconnect the reversed group with the previous and next parts of the list.
   *   - Set the pointers for the next iteration.
   *
   * Time: O(N), each node is visited once.
   * Space: O(1), in-place reversal with pointers.
   */
    /**
   * Intuition: treat each k-sized block as a closed mini-list with a start and
   * end. Reverse that block, connect the previous group to its new head, and
   * connect its old head to the next group.
   *
   * Algorithm:
   *   1. Return originalHead when the list is empty or groupSize <= 1.
   *   2. Use dummyHead, previousGroupEnd, and currentGroupStart to track boundaries.
   *   3. While a complete group exists, find currentGroupEnd and nextGroupStart.
   *   4. Reverse the group, reconnect it, and advance to the next group.
   *
   * Time:  O(n) - each node is checked and reversed a constant number of times.
   * Space: O(1) - reversal uses pointer variables only.
   *
   * @param originalHead head of the linked list
   * @param groupSize number of nodes per reversed group
   * @return head after reversing every complete group
   */
  public ListNode reverseInGroupsOfK(ListNode originalHead, int groupSize) {
    if (originalHead == null || groupSize <= 1) {
      return originalHead; // Nothing to reverse
    }

    ListNode dummyHead = new ListNode(-1); // Dummy node to simplify edge cases
    dummyHead.next = originalHead;

    ListNode previousGroupEnd = dummyHead; // Points to end of last processed group
    ListNode currentGroupStart = originalHead; // Start of the group to be reversed

    while (hasKNodes(currentGroupStart, groupSize)) {
      // Step 1: Identify the end of the current k-sized group
      ListNode currentGroupEnd = currentGroupStart;
      for (int i = 1; i < groupSize; i++) {
        currentGroupEnd = currentGroupEnd.next;
      }

      ListNode nextGroupStart = currentGroupEnd.next; // This will be start of the next group

      // Step 2: Reverse current group
      reverseGroup(currentGroupStart, currentGroupEnd);

      // Step 3: Connect the reversed group with previous and next parts
      previousGroupEnd.next = currentGroupEnd; // currentGroupEnd is new head after reversal
      currentGroupStart.next = nextGroupStart; // currentGroupStart is now end of group

      // Step 4: Move pointers for next iteration
      previousGroupEnd = currentGroupStart;
      currentGroupStart = nextGroupStart;
    }

    return dummyHead.next;
  }

  /**
   * Reverses the linked list from start to end (inclusive).
   */
  private void reverseGroup(ListNode groupStart, ListNode groupEnd) {
    ListNode previousNode = null;
    ListNode currentNode = groupStart;
    ListNode stopNode = groupEnd.next;

    while (currentNode != stopNode) {
      ListNode nextNode = currentNode.next;
      currentNode.next = previousNode;

      previousNode = currentNode;
      currentNode = nextNode;
    }
  }

  /**
   * Checks if there are at least k nodes left from the current node.
   */
  private boolean hasKNodes(ListNode startNode, int k) {
    int count = 0;
    while (startNode != null && count < k) {
      count++;
      startNode = startNode.next;
    }
    return count == k;
  }
}
