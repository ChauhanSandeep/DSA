package Heap;

import LinkedList.Util.Node;

import java.util.PriorityQueue;

public class MergeKLists {

    public static void main(String args[]) {
        int k = 3;

        Node arr[] = new Node[k];

        arr[0] = new Node(1);
        arr[0].next = new Node(3);
        arr[0].next.next = new Node(5);
        arr[0].next.next.next = new Node(7);

        arr[1] = new Node(2);
        arr[1].next = new Node(4);
        arr[1].next.next = new Node(6);
        arr[1].next.next.next = new Node(8);

        arr[2] = new Node(0);
        arr[2].next = new Node(9);
        arr[2].next.next = new Node(10);
        arr[2].next.next.next = new Node(11);

        Node head = mergeKSortedLists(arr, k);
        printList(head); // 0 1 2 3 4 5 6 7 8 9 10 11
    }


    /**
     * Given k sorted linked lists, merge the linked lists to create a common linked list
     * @param arr
     * @param k
     * @return
     */
    public static Node mergeKSortedLists(Node arr[], int k) {

        PriorityQueue<Node> minHeap = new PriorityQueue<>((o1, o2) -> o1.data - o2.data);

        // add first element from each list to minHeap
        for (int i = 0; i < k; i++) {
            if (arr[i] != null) {
                minHeap.add(arr[i]);
            }
        }

        Node head = null, temp = null;
        while (!minHeap.isEmpty()) {
            Node node = minHeap.peek();
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

    public static void printList(Node head) {
        while (head != null) {
            System.out.print(head.data + " ");
            head = head.next;
        }
    }

}