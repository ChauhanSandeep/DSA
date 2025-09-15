package heaps;

import java.util.Comparator;
import java.util.PriorityQueue;

// Private inner class for ListNode

/**
 * Problem: Merge K Sorted Linked Lists
 *
 * Leetcode Link: https://leetcode.com/problems/merge-k-sorted-lists/
 *
 * Problem Statement:
 * Given an array of `k` sorted linked lists, merge all the lists into one sorted linked list and return its head.
 *
 * Example:
 * Input:
 *   [
 *     1 → 3 → 5 → 7,
 *     2 → 4 → 6 → 8,
 *     0 → 9 → 10 → 11
 *   ]
 * Output:
 *   0 → 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9 → 10 → 11
 *
 * 🔄 Follow-up Questions:
 * 1. Can you do it using Divide and Conquer instead of a heap?
 *    🔗 https://leetcode.com/problems/merge-k-sorted-lists/discuss/10527/A-java-solution-based-on-divide-and-conquer
 * 2. Can you merge them in-place without extra memory?
 *    - Not always, as linked list nodes are immutable in Leetcode.
 * 3. What changes if the lists are streaming?
 *    - Use a real-time stream merge with a size-limited min-heap.
 * 4. How would you merge k sorted arrays instead of lists?
 *    - Similar heap approach, but use indices for each array to track progress.
 * 5. What if k is very large and lists are huge, causing memory issues?
 *    - Use external sorting or merge in a tournament fashion to reduce memory, but heap is efficient for n total nodes.
 *      Relevant problem: https://leetcode.com/problems/merge-sorted-array/
 * 6. How to merge without extra space?
 *    - Not possible efficiently without modifying lists, but divide and conquer can be done recursively with O(log k) space.
 */
public class MergeKLists {

  /**
   * ✅ Approach 1: Merge K Sorted Lists using Min-Heap
   *
   * 🧠 Steps:
   * 1. Insert the head of each non-null list into the min-heap.
   * 2. Extract the node with the smallest value and append it to the result.
   * 3. If that node has a `next`, push it into the heap.
   * 4. Repeat until heap is empty.
   *
   * ⏱ Time Complexity: O(N log K), where N = total number of nodes, K = number of lists.
   * 🧠 Space Complexity: O(K), to store K nodes in the heap at a time.
   */
  public static ListNode mergeKSortedLists(ListNode[] lists) {
      if (lists == null || lists.length == 0) {
          return null;
      }

    PriorityQueue<ListNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.val));

    // Add head of each list to the heap
    for (ListNode node : lists) {
      if (node != null) {
        minHeap.offer(node);
      }
    }

    ListNode dummyHead = new ListNode(-1);
    ListNode tail = dummyHead;

    while (!minHeap.isEmpty()) {
      ListNode smallest = minHeap.poll();
      tail.next = smallest;
      tail = smallest;

      if (smallest.next != null) {
        minHeap.offer(smallest.next);
      }
    }

    return dummyHead.next;
  }

  /**
   * ✅ Approach 2: Optimized Divide and Conquer
   *
   * Thinking process:
   * - Instead of merging all lists pairwise, we can divide the list of lists into two halves,
   *   merge each half recursively, and then merge the two sorted halves.
   * - This reduces the number of merges and avoids excessive heap operations.
   *
   * ⏱ Time Complexity: O(N log K), where N = total number of nodes, K = number of lists.
   * 🧠 Space Complexity:
   *    - O(1) for iterative mergeTwoLists
   *    - O(log K) due to recursive stack space
   */
  public static ListNode mergeKSortedListsOptimized(ListNode[] lists) {
      if (lists == null || lists.length == 0) {
          return null;
      }
    return mergeListsRecursive(lists, 0, lists.length - 1);
  }

  /**
   * Helper method for recursive merge using Divide and Conquer.
   */
  private static ListNode mergeListsRecursive(ListNode[] lists, int left, int right) {
      if (left == right) {
          return lists[left];
      }
      if (left > right) {
          return null;
      }

    int mid = left + (right - left) / 2;
    ListNode leftMerged = mergeListsRecursive(lists, left, mid);
    ListNode rightMerged = mergeListsRecursive(lists, mid + 1, right);

    return mergeTwoSortedLists(leftMerged, rightMerged);
  }

  /**
   * Merges two sorted linked lists into one sorted list.
   * Uses iterative approach to minimize stack space.
   */
  private static ListNode mergeTwoSortedLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(-1);
    ListNode current = dummy;

    while (l1 != null && l2 != null) {
      if (l1.val < l2.val) {
        current.next = l1;
        l1 = l1.next;
      } else {
        current.next = l2;
        l2 = l2.next;
      }
      current = current.next;
    }

    // Append the remaining list
    current.next = (l1 != null) ? l1 : l2;

    return dummy.next;
  }

  /**
   * Utility method to print a linked list to console.
   */
  public static void printList(ListNode head) {
    while (head != null) {
      System.out.print(head.val + " → ");
      head = head.next;
    }
    System.out.println("null");
  }

  public static void main(String[] args) {
    ListNode[] inputLists = new ListNode[3];

    inputLists[0] = new ListNode(1, new ListNode(3, new ListNode(5, new ListNode(7))));
    inputLists[1] = new ListNode(2, new ListNode(4, new ListNode(6, new ListNode(8))));
    inputLists[2] = new ListNode(0, new ListNode(9, new ListNode(10, new ListNode(11))));

    System.out.println("🔹 Merged List using Min-Heap:");
    printList(mergeKSortedLists(inputLists));

    System.out.println("\n🔹 Merged List using Divide & Conquer:");
    printList(mergeKSortedListsOptimized(inputLists));
  }

  // Private inner class for ListNode
  private static class ListNode {
    int val;
    ListNode next;

    ListNode() {}

    ListNode(int val) {
      this.val = val;
    }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }
  }
}