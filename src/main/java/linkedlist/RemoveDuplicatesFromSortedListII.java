package linkedlist;

/**
 * 82. Remove Duplicates from Sorted List II
 *
 * Problem Statement:
 * Given the head of a sorted linked list, delete all nodes that have duplicate
 * numbers, leaving only distinct numbers from the original list. Return the
 * linked list sorted as well.
 *
 * Example:
 * Input: head = [1,2,3,3,4,4,5]
 * Output: [1,2,5]
 * Explanation: Remove all nodes with values 3 and 4 (including all occurrences).
 *
 * Input: head = [1,1,1,2,3]
 * Output: [2,3]
 * Explanation: Remove all nodes with value 1 (all three occurrences).
 *
 * Input: head = [1,2,2]
 * Output: [1]
 * Explanation: Remove all nodes with value 2 (both occurrences).
 *
 * LeetCode Link: https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/
 *
 * Follow-up Questions:
 * 1. What if we want to keep exactly one occurrence of duplicates instead of removing all?
 *    Answer: This becomes LeetCode 83 - just skip extra duplicates but keep the first one.
 *    Related: https://leetcode.com/problems/remove-duplicates-from-sorted-list/
 * 2. How would you handle an unsorted linked list?
 *    Answer: Use HashMap to track frequencies, then make a second pass to filter.
 * 3. What if we want to remove elements that appear exactly k times?
 *    Answer: Use frequency counting with HashMap, then filter based on count != k.
 * 4. How would you handle very large lists efficiently?
 *    Answer: Stream processing or external sorting with chunked processing.
 *
 * Related Problems:
 * - 83. Remove Duplicates from Sorted List: https://leetcode.com/problems/remove-duplicates-from-sorted-list/
 * - 203. Remove Linked List Elements: https://leetcode.com/problems/remove-linked-list-elements/
 * - 237. Delete Node in a Linked List: https://leetcode.com/problems/delete-node-in-a-linked-list/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */

public class RemoveDuplicatesFromSortedListII {

  /**
   * Removes all nodes that have duplicate values, keeping only unique values.
   *
   * Algorithm: Dummy head with two-pointer technique
   * 1. Use dummy head to handle edge cases where head nodes need removal
   * 2. Use 'prev' pointer to track last confirmed unique node
   * 3. Use 'curr' pointer to scan and detect duplicate sequences
   * 4. When duplicates found, skip entire sequence; otherwise keep the unique node
   *
   * Key insight: Unlike problem 83, we need to remove ALL occurrences of duplicates,
   * not just keep one. This requires looking ahead to detect if current value
   * appears multiple times before deciding whether to keep or remove it.
   *
   * Time Complexity: O(n) where n is number of nodes - single pass through list
   * Space Complexity: O(1) - only using constant extra space for pointers
   *
   * @param head Head of the sorted linked list
   * @return Head of the list after removing all duplicate nodes
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
