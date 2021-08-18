package Heap;

import LinkedList.Util.ListNode;

import java.util.PriorityQueue;

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

        ListNode head = mergeKSortedLists(arr, k);
        printList(head); // 0 1 2 3 4 5 6 7 8 9 10 11
    }


    /**
     * Given k sorted linked lists, merge the linked lists to create a common linked list
     * @param arr
     * @param k
     * @return
     */
    public static ListNode mergeKSortedLists(ListNode arr[], int k) {

        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((o1, o2) -> o1.val - o2.val);

        // add first element from each list to minHeap
        for (int i = 0; i < k; i++) {
            if (arr[i] != null) {
                minHeap.add(arr[i]);
            }
        }

        ListNode head = null, temp = null;
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.peek();
            minHeap.remove();

            if (node.next != null)  minHeap.add(node.next);

            if (head == null) { // first element taken from minHeap
                head = node;
            } else {
                temp.next = node;
            }
            temp = node;
        }
        return head;
    }

    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
    }

}