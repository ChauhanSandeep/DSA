package LinkedList;

/**
 * 237. Delete Node in a Linked List
 * 
 * Problem: Write a function to delete a node (except the tail) in a singly linked list,
 * given only access to that node.
 * 
 * Example:
 * Input: head = [4,5,1,9], node = reference to node with value 5
 * After calling function, list becomes [4,1,9]
 * 
 * LeetCode: https://leetcode.com/problems/delete-node-in-a-linked-list
 * 
 * Follow-up questions:
 * Q: What if node is tail?
 * A: Cannot delete tail with this method; need reference to previous node.
 * 
 * Q: How to delete multiple consecutive nodes?
 * A: Copy next.next value and update pointers accordingly.
 * 
 * Q: Can we handle cycles?
 * A: This method ignores cycles; more context needed.
 */
public class DeleteNodeInALinkedList {
    
    // Definition for singly-linked list.
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    
    /**
     * Deletes given node by copying next node's value and bypassing it.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
    
    /**
     * Robust version with null checks.
     */
    public void deleteNodeRobust(ListNode node) {
        if (node == null || node.next == null) {
            throw new IllegalArgumentException("Cannot delete this node");
        }
        node.val = node.next.val;
        node.next = node.next.next;
    }
}