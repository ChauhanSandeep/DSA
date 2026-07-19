package heaps;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Arrays;

// Private inner class for ListNode

/**
 * Problem: Merge K Sorted Linked Lists
 *
 * Given an array of sorted linked-list heads, merge all nodes into one sorted
 * linked list. The heap solution repeatedly chooses the smallest current head
 * among the k lists and advances only that list.
 *
 * Leetcode: https://leetcode.com/problems/merge-k-sorted-lists/ (Hard)
 * Rating:   acceptance 60.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | K-way merge | Divide and conquer alternative
 *
 * Example:
 *   Input:  lists = [[1,4,5],[1,3,4],[2,6]]
 *   Output: [1,1,2,3,4,4,5,6]
 *   Why:    repeatedly taking the smallest available list head preserves sorted order.
 *
 * Follow-ups:
 *   1. Can you avoid the heap?
 *      Merge lists by divide and conquer for the same O(n log k) time.
 *   2. What if lists are streamed from disk?
 *      Keep only one current node or record per stream in the heap.
 *   3. How do you merge descending lists?
 *      Reverse the comparator or normalize each list direction first.
 *   4. What if k is huge but most lists are empty?
 *      Only offer non-null heads, so heap size is the number of active lists.
 *
 * Related: Merge Two Sorted Lists (21), Merge Sorted Array (88).
 */

public class MergeKLists {

    /**
   * Intuition: each list is already sorted, so the next node in the merged output
   * must be the smallest among the current heads. A min heap keeps those active
   * heads ordered and only needs the successor of the node just removed.
   *
   * Algorithm:
   *   1. Return null for a null or empty array of lists.
   *   2. Offer every non-null list head into a min heap by node value.
   *   3. Poll the smallest node, append it to the output tail, and advance the tail.
   *   4. If that node has a next node, offer it into the heap.
   *
   * Time:  O(n log k) - each of n nodes is offered and polled from a heap of k lists.
   * Space: O(k) - the heap stores at most one active node per list.
   *
   * @param lists array of sorted linked-list heads
   * @return head of the merged sorted linked list
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
    ListNode[] firstInput = new ListNode[3];
    firstInput[0] = new ListNode(1, new ListNode(4, new ListNode(5)));
    firstInput[1] = new ListNode(1, new ListNode(3, new ListNode(4)));
    firstInput[2] = new ListNode(2, new ListNode(6));
    ListNode firstGot = mergeKSortedLists(firstInput);
    StringBuilder firstOut = new StringBuilder("[");
    for (ListNode node = firstGot; node != null; node = node.next) {
      if (firstOut.length() > 1) firstOut.append(", ");
      firstOut.append(node.val);
    }
    firstOut.append("]");
    System.out.printf("lists=%s -> %s  expected=%s%n",
        Arrays.toString(new String[]{"[1, 4, 5]", "[1, 3, 4]", "[2, 6]"}),
        firstOut, "[1, 1, 2, 3, 4, 4, 5, 6]");

    ListNode[] emptyInput = new ListNode[0];
    ListNode emptyGot = mergeKSortedLists(emptyInput);
    System.out.printf("lists=%s -> %s  expected=%s%n",
        Arrays.toString(new String[]{}), emptyGot, "null");
  }

  /** Basic singly linked-list node used by the demos and merge methods. */
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