package linkedlist;


/**
 * ✅ Problem: Reverse Linked List
 * 🔗 LeetCode: https://leetcode.com/problems/reverse-linked-list/
 * 📚 Problem Statement:
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 *
 * 🔍 **Approach:**
 * - Iterative reversal of the entire list.
 * - Recursive reversal of the entire list.
 * - Reversing in groups of 'k' nodes.
 *
 * 📊 **Time Complexity:**
 * - `reverseList()`: O(N) - Iterates through the list once.
 * - `reverseListRec()`: O(N) - Visits each node once.
 * - `reverseInGroups()`: O(N) - Each node is visited once.
 *
 * 🛠 **Space Complexity:**
 * - `reverseList()`: O(1) - Uses constant extra space.
 * - `reverseListRec()`: O(N) - Due to recursive function calls.
 * - `reverseInGroups()`: O(N) - Due to recursive calls.
 */
public class ReverseLinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(4));
        list.add(new ListNode(5));
        list.add(new ListNode(6));

        System.out.println("Original List:");
        list.printList();

        head = reverseList(head);
        System.out.println("After complete reversal:");
        list.printList();

        head = reverseListRec(head);
        System.out.println("After recursive reversal:");
        list.printList();
    }

    /**
     * Reverses the entire linked list iteratively.
     *
     * @param head Head of the linked list.
     * @return New head of the reversed linked list.
     */
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;
        ListNode then = curr.next;

        /**
         * Complete states: prev = 0, curr = 1, then = 2
         * 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> null
         *
         * Complete states: prev = 0, curr = 1, then = 3
         * 0 -> 2 -> 1 -> 3 -> 4 -> 5 -> 6 -> null
         *
         * Complete states: prev = 0, curr = 1, then = 4
         * 0 -> 3 -> 2 -> 1 -> 4 -> 5 -> 6 -> null
         *
         * Complete states: prev = 0, curr = 1, then = 5
         * 0 -> 4 -> 3 -> 2 -> 1 -> 5 -> 6 -> null
         *
         * Complete states: prev = 0, curr = 1, then = 6
         * 0 -> 5 -> 4 -> 3 -> 2 -> 1 -> 6 -> null
         *
         * Basically this is a head-insertion method where we take the `then` node and insert it right after `prev`.
         * We repeat this until `then` becomes null, which means we have processed all nodes.
         */
        while (then != null) {
            curr.next = then.next;
            then.next = prev.next;
            prev.next = then;
            then = curr.next;
            System.out.println("Complete states: prev = " + prev.val + ", curr = " + curr.val + ", then = " + (then != null ? then.val : "null"));
        }

        return dummy.next;
    }

    /**
     * Reverses the entire linked list recursively.
     *
     * @param head Head of the linked list.
     * @return New head of the reversed linked list.
     */
    public static ListNode reverseListRec(ListNode head) {
        if (head == null || head.getNext() == null) {
            return head; // Base case: If list is empty or has only one node, return head.
        }

        ListNode reversedHead = reverseListRec(head.getNext()); // Reverse remaining list
        head.getNext().setNext(head); // Reverse the current node's next link
        head.setNext(null); // Set current node's next to null (to avoid cycles)

        return reversedHead;
    }
}
