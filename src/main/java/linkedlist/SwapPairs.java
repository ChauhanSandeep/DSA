package linkedlist;

/**
 * Swap Nodes in Pairs - LeetCode Problem 24
 *
 * Problem Statement:
 * Given a linked list, swap every two adjacent nodes and return its head.
 * You must solve the problem without modifying the values in the list's nodes
 * (i.e., only nodes themselves may be changed).
 *
 * Example:
 * Input: head = [1,2,3,4]
 * Output: [2,1,4,3]
 * Explanation: Swap (1,2) to get (2,1), swap (3,4) to get (4,3).
 * The iterative approach uses dummy node to simplify edge cases and three pointers
 * to manage the swapping: prev (before pair), curr (first node), next (second node).
 *
 * LeetCode Link: https://leetcode.com/problems/swap-nodes-in-pairs/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you swap every k adjacent nodes instead of pairs?
 *    Answer: Extend the algorithm with k-node groups, use similar pointer manipulation.
 *    Related: LeetCode 25 - https://leetcode.com/problems/reverse-nodes-in-k-group/
 *
 * 2. What if we need to swap nodes at specific positions (not adjacent)?
 *    Answer: Use two-pass approach to locate nodes, then swap with careful pointer updates.
 *    Related: LeetCode 1721 - https://leetcode.com/problems/swapping-nodes-in-a-linked-list/
 *
 * 3. How to handle this for doubly linked list?
 *    Answer: Additional prev pointer maintenance for both directions during swaps.
 *
 * 4. What if we need to reverse pairs instead of swapping?
 *    Answer: Same algorithm works since swapping two nodes effectively reverses the pair.
 *    Related: LeetCode 206 - https://leetcode.com/problems/reverse-linked-list/
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
   * Think of swapping in chunks of size 2.
   * Every iteration we focus on two nodes at a time:
   *     [first] -> [second] -> [next]
   * becomes
   *     [second] -> [first] -> [next]
   *
   * Approach:
   *  1. Use a dummy node to simplify swapping at the head of the list.
   *  2. Initialize two pointers:
   *     - previous: Points to the end of the processed part.
   *     - head: Points to the current pair’s first node.
   *  3. Loop while both head and head.next exist:
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

    ListNode previousPairEndNode = dummy;  // End of the last processed group

    // Loop till at least two nodes remain to be swapped
    while (head != null && head.next != null) {

      // Step 1: Identify nodes to be swapped
      ListNode firstNode = head;
      ListNode secondNode = head.next;
      ListNode nextPairStartNode = secondNode.next;

      // Step 2: Perform the swap (actual pointer manipulation)
      previousPairEndNode.next = secondNode;         // Connect previousPairEndNode group to second
      secondNode.next = firstNode;        // second -> first
      firstNode.next = nextPairStartNode;     // first -> next group's head

      // Step 3: Move pointers ahead for next swap
      previousPairEndNode = firstNode;       // previousPairEndNode should point to the end of the newly swapped pair
      head = nextPairStartNode;       // head moves to the start of the next pair

      /*
       * 1 -> 2 -> 3 -> 4
       * dummy -> 2 -> 1 -> 3 -> 4
       * previousPairEndNode = 1, head = 3
       *
       * dummy -> 2 -> 1 -> 4 -> 3
       * previousPairEndNode = 3, head = null (loop ends)
       */
    }

    return dummy.next; // The new head after first swap
  }
}
