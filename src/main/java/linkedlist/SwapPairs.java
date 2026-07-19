package linkedlist;

import java.util.Arrays;

/**
 * Problem: Swap Nodes in Pairs
 *
 * Swap every two adjacent nodes in a linked list and return the new head. Node
 * values must not be changed; only next pointers may be rewired.
 *
 * Leetcode: https://leetcode.com/problems/swap-nodes-in-pairs/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Pair rewiring | Dummy node
 *
 * Example:
 *   Input:  head = [1,2,3,4]
 *   Output: [2,1,4,3]
 *   Why:    1 swaps with 2, and 3 swaps with 4.
 *
 * Follow-ups:
 *   1. Swap every k nodes?
 *      Generalize to k-group reversal after verifying a full group.
 *   2. Swap non-adjacent positions?
 *      Track each node and its previous node, then rewire both neighborhoods.
 *   3. Doubly linked list version?
 *      Update both next and prev links for every swapped node.
 *
 * Related: Reverse Nodes in k-Group (25), Swapping Nodes in a Linked List (1721).
 */
public class SwapPairs {

  public static void main(String[] args) {
    SwapPairs solver = new SwapPairs();
    int[][] inputs = { {1, 2, 3, 4}, {1, 2, 3} };
    int[][] expected = { {2, 1, 4, 3}, {2, 1, 3} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = solver.swapNodesInPairs(head);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }


    /**
   * Intuition: each iteration owns firstNode and secondNode. previousPairEndNode
   * connects the processed prefix to secondNode, secondNode points back to
   * firstNode, and firstNode points to the next pair.
   *
   * Algorithm:
   *   1. Return head for an empty or single-node list.
   *   2. Create dummy before head and keep previousPairEndNode at the processed tail.
   *   3. While a full pair remains, name firstNode, secondNode, and nextPairStartNode.
   *   4. Rewire the three next pointers, then advance to the next pair.
   *
   * Time:  O(n) - every node participates in at most one swap.
   * Space: O(1) - only a dummy and pair pointers are stored.
   *
   * @param head head of the linked list
   * @return head after swapping adjacent pairs
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

      // The old first node is now the processed tail for this pair.
    }

    return dummy.next; // The new head after first swap
  }
}
