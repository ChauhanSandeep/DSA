package Heap;

import LinkedList.Util.ListNode;
import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/merge-k-sorted-lists/
 *
 * Given k sorted linked lists, merge them into one sorted linked list.
 *
 * Approach 1: Min-Heap (Priority Queue)
 * - Use a Min-Heap (PriorityQueue) to store the head nodes of all lists.
 * - Extract the smallest node from the heap and append it to the result list.
 * - If the extracted node has a next node, push it into the heap.
 *
 * Time Complexity: O(N log K) → N = total nodes, K = number of lists
 * Space Complexity: O(K) → Storing K elements in the heap
 *
 * Approach 2 (Optimized): Divide and Conquer (Merging Two Lists at a Time)
 * - Recursively merge lists in pairs using `mergeTwoLists()`.
 * - Reduces the problem size logarithmically.
 *
 * Time Complexity: O(N log K) → Same as the heap approach but reduces extra space.
 * Space Complexity: O(1) (iterative) / O(log K) (recursive calls)
 */
public class MergeKLists {

    public static void main(String[] args) {
        ListNode[] lists = new ListNode[3];

        lists[0] = new ListNode(1, new ListNode(3, new ListNode(5, new ListNode(7))));
        lists[1] = new ListNode(2, new ListNode(4, new ListNode(6, new ListNode(8))));
        lists[2] = new ListNode(0, new ListNode(9, new ListNode(10, new ListNode(11))));

        System.out.println("Merged List (Min-Heap Approach):");
        printList(mergeKSortedLists(lists));

        System.out.println("\nMerged List (Divide & Conquer Approach):");
        printList(mergeKSortedListsOptimized(lists));
    }

    /**
     * Merges k sorted linked lists using a Min-Heap (PriorityQueue).
     * @param lists Array of sorted linked lists.
     * @return The merged sorted linked list.
     */
    public static ListNode mergeKSortedLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

        // Add the head node of each list to the heap
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }

        ListNode dummy = new ListNode(-1); // Dummy node to track result list
        ListNode tail = dummy;

        // Process heap until empty
        while (!minHeap.isEmpty()) {
            ListNode smallestNode = minHeap.poll();
            tail.next = smallestNode;
            tail = smallestNode;

            if (smallestNode.next != null) {
                minHeap.offer(smallestNode.next);
            }
        }
        return dummy.next;
    }

    /**
     * Optimized Approach: Merges k sorted linked lists using a Divide & Conquer strategy.
     * @param lists Array of sorted linked lists.
     * @return The merged sorted linked list.
     */
    public static ListNode mergeKSortedListsOptimized(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeLists(lists, 0, lists.length - 1);
    }

    /**
     * Helper function to recursively merge lists using divide and conquer.
     */
    private static ListNode mergeLists(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left]; // Base case: single list
        if (left > right) return null; // Edge case: invalid indices

        int mid = left + (right - left) / 2;
        ListNode leftMerged = mergeLists(lists, left, mid);
        ListNode rightMerged = mergeLists(lists, mid + 1, right);
        return mergeTwoLists(leftMerged, rightMerged);
    }

    /**
     * Merges two sorted linked lists into one.
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    /**
     * Prints a linked list.
     */
    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " -> ");
            head = head.next;
        }
        System.out.println("null");
    }
}
