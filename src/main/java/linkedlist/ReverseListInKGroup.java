package linkedlist;

/**
 * Problem: Reverse Nodes in k-Group
 * LeetCode: https://leetcode.com/problems/reverse-nodes-in-k-group/
 * Problem Statement:
 * Given the head of a linked list, reverse the nodes of the list `k` at a time and return the modified list.
 * If the number of nodes is not a multiple of `k`, then the remaining nodes at the end should remain unchanged.
 * For given the list   1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9 -> 10 -> 11 -> 12 -> 13 and k = 2,
 * the output should be 3 -> 2 -> 1 -> 6 -> 5 -> 4 -> 9 -> 8 -> 7 -> 12 -> 11 -> 10 -> 13
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ReverseListInKGroup {

  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(4));
    list.add(new ListNode(5));

    int k = 2;
    ListNode result = new ReverseListInKGroup().reverseInGroupsOfK(head, k);
    System.out.println("Reversed in groups of " + k + ": " + result);
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
   * Reverses nodes in groups of k.
   *
   * @param originalHead The head of the original linked list.
   * @param groupSize    The size of the group to reverse.
   * @return The head of the modified list after reversing in k-groups.
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
