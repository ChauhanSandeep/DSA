package dailybytes.linkedlist;

import java.util.Arrays;

/**
 * Problem: Remove Linked List Elements
 *
 * Given a linked list and a target value, remove every node whose data equals
 * that value. The list head may also need to move if one or more leading nodes
 * are removed.
 *
 * Leetcode: https://leetcode.com/problems/remove-linked-list-elements/ (Easy)
 * Rating:   acceptance 54.9% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Linked list | Pointer rewiring | Head cleanup
 *
 * Example:
 *   Input:  list = 3 -> 3 -> 2 -> 3 -> NULL, val = 3
 *   Output: 2 -> NULL
 *   Why:    every node containing 3 is bypassed, leaving only the node with 2.
 *
 * Follow-ups:
 *   1. Return the new head instead of mutating a wrapper list?
 *      Use a dummy node and return dummy.next after rewiring.
 *   2. Remove nodes matching any value in a set?
 *      Check membership in a HashSet while traversing.
 *   3. Remove nodes from a doubly linked list?
 *      Update both next and previous pointers for each removed node.
 *   4. Remove nodes recursively?
 *      Recurse to the tail, then decide whether to keep each node while unwinding.
 *
 * Related: Delete Node in a Linked List (237), Remove Nth Node From End of List (19).
 */
public class RemoveNodes {

    public static void main(String[] args) {
        int[][] inputs = { {3, 3, 2, 3}, {1, 2, 3} };
        int[] valuesToRemove = { 3, 4 };
        String[] expected = { "MyLinkedList{2 -> NULL}", "MyLinkedList{1 -> 2 -> 3 -> NULL}" };

        for (int i = 0; i < inputs.length; i++) {
            MyLinkedList list = new MyLinkedList();
            for (int value : inputs[i]) {
                list.push(new MyNode(value));
            }
            removeNodes(list, valuesToRemove[i]);
            System.out.printf("list=%s val=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), valuesToRemove[i], list, expected[i]);
        }
    }

    /**
     * Intuition: removing a node means making the previous kept node skip it.
     * Leading matches are special because there is no previous node yet, so move
     * head past all target values first, then rewire matching next nodes during a
     * single traversal.
     *
     * Algorithm:
     *   1. Move head past every leading node whose data equals val.
     *   2. Traverse from the first kept head using temp.
     *   3. If temp.next contains val, bypass it by linking to temp.next.next.
     *   4. Otherwise advance temp, then store the new head back on list.
     *
     * Time:  O(n) - each node is visited at most once.
     * Space: O(1) - only pointer variables are used.
     *
     * @param list linked list to mutate
     * @param val value to remove from the linked list
     */
    public static void removeNodes(MyLinkedList list, int val) {
        MyNode head = list.getHead();

        // Remove head nodes that match the specified value
        while (head != null && head.getData() == val) {
            head = head.getNext();
        }

        MyNode temp = head;

        // Traverse the list and remove matching nodes
        while (temp != null && temp.getNext() != null) {
            if (temp.getNext().getData() == val) {
                temp.setNext(temp.getNext().getNext());
            } else {
                temp = temp.getNext();
            }
        }

        list.setHead(head);
    }
}
