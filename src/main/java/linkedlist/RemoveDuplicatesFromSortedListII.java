package linkedlist;

import java.util.Arrays;

/**
 * Problem: Remove Duplicates from Sorted List II
 *
 * Given a sorted linked list, remove every value that appears more than once,
 * leaving only values that were unique in the original list. The result remains
 * sorted because only nodes are deleted.
 *
 * Leetcode: https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Dummy node | Duplicate-run skipping
 *
 * Example:
 *   Input:  head = [1,2,3,3,4,4,5]
 *   Output: [1,2,5]
 *   Why:    3 and 4 appear in duplicate runs, so all of their nodes are removed.
 *
 * Follow-ups:
 *   1. Keep one copy instead?
 *      Use the simpler duplicate skip from Leetcode 83.
 *   2. What if the list is unsorted?
 *      Count frequencies, then relink nodes with frequency one.
 *   3. Remove values appearing exactly k times?
 *      Count frequencies and filter on count != k.
 *
 * Related: Remove Duplicates from Sorted List (83), Remove Linked List Elements (203).
 */
public class RemoveDuplicatesFromSortedListII {

  public static void main(String[] args) {
    RemoveDuplicatesFromSortedListII solver = new RemoveDuplicatesFromSortedListII();
    int[][] inputs = { {1, 2, 3, 3, 4, 4, 5}, {1, 1, 1, 2, 3} };
    int[][] expected = { {1, 2, 5}, {2, 3} };
    for (int i = 0; i < inputs.length; i++) {
      ListNode head = null, tail = null;
      for (int value : inputs[i]) {
        ListNode node = new ListNode(value);
        if (head == null) { head = node; tail = node; } else { tail.next = node; tail = node; }
      }
      ListNode outputHead = solver.deleteDuplicates(head);
      int[] output = new int[expected[i].length];
      for (int j = 0; j < output.length && outputHead != null; j++, outputHead = outputHead.next) output[j] = outputHead.val;
      System.out.printf("head=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
    }
  }

    /**
   * Intuition: duplicates in a sorted list form one contiguous run. prev points
   * to the last confirmed unique node, and dummy protects the real head when the
   * first run must be deleted.
   *
   * Algorithm:
   *   1. Return head for an empty or single-node list.
   *   2. Start dummy before head, prev at dummy, and curr at head.
   *   3. If curr starts a duplicate run, advance curr past the run and set prev.next.
   *   4. Otherwise advance prev and curr by one node.
   *
   * Time:  O(n) - each node is visited as part of one scan or skipped run.
   * Space: O(1) - only dummy, prev, and curr are stored.
   *
   * @param head head of the sorted linked list
   * @return head after removing all duplicated values
   */
  public ListNode deleteDuplicates(ListNode head) {
    // Handle empty or single-node lists
    if (head == null || head.next == null) {
      return head;
    }

    // Create dummy head to simplify edge case handling
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    ListNode prev = dummy;  // Points to last confirmed unique node
    ListNode curr = head;   // Scans through the list

    while (curr != null) {
      // Check if current node has duplicates by looking ahead
      if (curr.next != null && curr.val == curr.next.val) {
        // Found duplicates - skip all nodes with this value
        int duplicateValue = curr.val;

        // Skip all consecutive nodes with duplicate value
        while (curr != null && curr.val == duplicateValue) {
          curr = curr.next;
        }

        // Connect prev to the node after all duplicates
        prev.next = curr;
      } else {
        // No duplicates found - this is a unique value, keep it
        prev = curr;
        curr = curr.next;
      }
    }

    return dummy.next; // Return actual head (skip dummy)
  }

  /**
   * Approach using frequency counting for comparison (uses extra space).
   * This method shows how the problem could be solved with additional space.
   *
   * Algorithm: Two-pass frequency counting
   * 1. First pass: count frequency of each value using HashMap
   * 2. Second pass: keep only nodes with frequency = 1
   *
   * Time Complexity: O(n) - two passes through the list
   * Space Complexity: O(k) where k is number of unique values
   *
   * Note: Uses extra space but can be useful for unsorted lists.
   */
  public ListNode deleteDuplicatesWithFrequency(ListNode head) {
    if (head == null) {
      return null;
    }

    // First pass: count frequencies
    java.util.Map<Integer, Integer> frequencyMap = new java.util.HashMap<>();
    ListNode curr = head;
    while (curr != null) {
      frequencyMap.put(curr.val, frequencyMap.getOrDefault(curr.val, 0) + 1);
      curr = curr.next;
    }

    // Second pass: keep only nodes with frequency 1
    ListNode dummy = new ListNode(0);
    ListNode prev = dummy;
    curr = head;

    while (curr != null) {
      if (frequencyMap.get(curr.val) == 1) {
        prev.next = curr;
        prev = curr;
      }
      curr = curr.next;
    }

    prev.next = null; // Terminate the list
    return dummy.next;
  }

    // Definition for singly-linked list
    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
