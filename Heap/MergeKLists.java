package Heap;

import LinkedList.Util.ListNode;

import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/merge-k-sorted-lists/
 *
 * Given k sorted linked lists, merge them into one sorted linked list.
 *
 * Algorithm:
 * - Use a min-heap (PriorityQueue) to store the head nodes of all lists.
 * - Extract the minimum node from the heap and add it to the result list.
 * - If the extracted node has a next node, push it into the heap.
 *
 * Time Complexity: O(N log K), where N is the total number of nodes and K is the number of lists.
 * Space Complexity: O(K) for storing K elements in the heap.
 */
public class MergeKLists {

    public static void main(String args[]) {
        int k = 3;

        ListNode arr[] = new ListNode[k];

        arr[0] = new ListNode(1);
        arr[0].next = new ListNode(3);
        arr[0].next.next = new ListNode(5);
        arr[0].next.next.next = new ListNode(7);

        arr[1] = new ListNode(2);
        arr[1].next = new ListNode(4);
        arr[1].next.next = new ListNode(6);
        arr[1].next.next.next = new ListNode(8);

        arr[2] = new ListNode(0);
        arr[2].next = new ListNode(9);
        arr[2].next.next = new ListNode(10);
        arr[2].next.next.next = new ListNode(11);

        ListNode head = mergeKSortedLists(arr);
        printList(head); // Expected output: 0 1 2 3 4 5 6 7 8 9 10 11
    }

    /**
     * Merges k sorted linked lists into one sorted linked list.
     *
     * @param lists Array of sorted linked lists.
     * @return The merged sorted linked list.
     */
    public static ListNode mergeKSortedLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((o1, o2) -> o1.val - o2.val);

        // Add the head node of each list to the minHeap
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.add(list);
            }
        }

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            tail.next = node;
            tail = node;

            if (node.next != null) {
                minHeap.add(node.next);
            }
        }
        return dummy.next;
    }

    /**
     * Prints a linked list.
     *
     * @param head The head of the linked list.
     */
    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }
}
