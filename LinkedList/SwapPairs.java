package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;


/**
 * Problem: Swap nodes in pairs in a given linked list.
 *
 * Given a singly linked list, swap every two adjacent nodes and return the modified list.
 *
 * Example:
 * Input:  1 -> 2 -> 3 -> 4
 * Output: 2 -> 1 -> 4 -> 3
 *
 * Approach:
 * - Use a dummy node to simplify edge cases.
 * - Traverse the list in pairs and swap nodes by adjusting pointers.
 * - Maintain a `prev` pointer to keep track of the previous node for proper linking.
 * - Iterate until we reach the end of the list.
 *
 * Time Complexity: O(N) - We traverse the list once.
 * Space Complexity: O(1) - No extra space is used apart from a few pointers.
 *
 * LeetCode Link: https://leetcode.com/problems/swap-nodes-in-pairs/
 */
public class SwapPairs {

  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(4));

    SwapPairs swapper = new SwapPairs();
    ListNode resultHead = swapper.swapNodesInPairs(head);

    System.out.println(resultHead);
  }

  /**
   * Intuition:
   * Think of swapping in *chunks of size 2*.
   * Every iteration we focus on two nodes at a time:
   *     [first] -> [second] -> [next]
   * becomes
   *     [second] -> [first] -> [next]
   *
   * Approach:
   *  1. Use a **dummy node** to simplify swapping at the head of the list.
   *  2. Initialize two pointers:
   *     - `previous`: Points to the end of the processed part.
   *     - `head`: Points to the current pair’s first node.
   *  3. Loop while both `head` and `head.next` exist:
   *     - Identify the two nodes to swap.
   *     - Adjust pointers to swap them.
   *     - Reconnect the previous group to the new start of the current group.
   *     - Move `previous` and `head` forward by two nodes.
   *
   * Time Complexity: O(N) – We process every node once.
   * Space Complexity: O(1) – In-place manipulation with constant space.
   */
  public ListNode swapNodesInPairs(ListNode head) {
      if (head == null || head.next == null) {
          return head;
      }

    // Dummy node points to the new head (after first swap)
    ListNode dummy = new ListNode(-1);
    dummy.next = head;

    ListNode previous = dummy;  // End of the last processed group

    // Loop till at least two nodes remain to be swapped
    while (head != null && head.next != null) {

      // Step 1: Identify nodes to be swapped
      ListNode firstNode = head;
      ListNode secondNode = head.next;
      ListNode nextGroupHead = secondNode.next;

      // Step 2: Perform the swap (actual pointer manipulation)
      previous.next = secondNode;         // Connect previous group to second
      secondNode.next = firstNode;        // second -> first
      firstNode.next = nextGroupHead;     // first -> next group's head

      // Step 3: Move pointers ahead for next swap
      previous = firstNode;       // previous should point to the end of the newly swapped pair
      head = nextGroupHead;       // head moves to the start of the next pair

      /*
       * 1 -> 2 -> 3 -> 4
       * dummy -> 2 -> 1 -> 3 -> 4
       * previous = 1, head = 3
       *
       * dummy -> 2 -> 1 -> 4 -> 3
       * previous = 3, head = null (loop ends)
       */
    }

    return dummy.next; // The new head after first swap
  }
}
