package LinkedList;

/**
 * Given the head of a linked list, remove the nth node from the end of the list and return its head.
 * 
 * Example 1:
 * Input: head = [1,2,3,4,5], n = 2
 * Output: [1,2,3,5]
 * 
 * Example 2:
 * Input: head = [1], n = 1
 * Output: []
 * 
 * Example 3:
 * Input: head = [1,2], n = 1
 * Output: [1]
 * 
 * LeetCode: https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 * 
 * Follow-up Questions:
 * 1. How would you solve this problem with only one pass through the list?
 *    - We can use the two-pointer technique with a fast and slow pointer.
 * 2. What if we need to remove the nth node from the beginning instead of the end?
 *    - We can directly traverse to the (n-1)th node and remove the next node.
 * 3. How would you handle very large lists that don't fit in memory?
 *    - We would need to process the list in chunks or use external sorting.
 * 
 * Related Problems:
 * - Remove Duplicates from Sorted List (https://leetcode.com/problems/remove-duplicates-from-sorted-list/)
 * - Remove Linked List Elements (https://leetcode.com/problems/remove-linked-list-elements/)
 * - Delete Node in a Linked List (https://leetcode.com/problems/delete-node-in-a-linked-list/)
 */
public class RemoveNthNodeFromEndOfList {
    
    /**
     * Definition for singly-linked list.
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Removes the nth node from the end of the list in one pass.
     * 
     * @param head The head of the linked list
     * @param n The position from the end to remove (1-based)
     * @return The head of the modified list
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // Create a dummy node to handle edge cases (like removing the first node)
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode fast = dummy;
        ListNode slow = dummy;
        
        // Move fast pointer n+1 steps ahead
        for (int i = 0; i <= n; i++) {
            if (fast == null) {
                // Handle the case where n is larger than the list length
                return head;
            }
            fast = fast.next;
        }
        
        // Move both pointers until fast reaches the end
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // Remove the nth node from the end
        slow.next = slow.next.next;
        
        return dummy.next;
    }
    
    /**
     * Removes the nth node from the beginning of the list.
     * 
     * @param head The head of the linked list
     * @param n The position from the beginning to remove (1-based)
     * @return The head of the modified list
     */
    public ListNode removeNthFromStart(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        
        // Handle the case when removing the first node
        if (n == 1) {
            return head.next;
        }
        
        ListNode current = head;
        int count = 1;
        
        // Traverse to the (n-1)th node
        while (current != null && count < n - 1) {
            current = current.next;
            count++;
        }
        
        // If we found the (n-1)th node and it's not the last node
        if (current != null && current.next != null) {
            current.next = current.next.next;
        }
        
        return head;
    }
    
    /**
     * Solution for "Remove Duplicates from Sorted List" (LeetCode #83).
     * Given a sorted linked list, delete all duplicates such that each element appears only once.
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return null;
        }
        
        ListNode current = head;
        
        while (current != null && current.next != null) {
            if (current.val == current.next.val) {
                // Skip the next node if it's a duplicate
                current.next = current.next.next;
            } else {
                // Move to the next node
                current = current.next;
            }
        }
        
        return head;
    }
    
    /**
     * Solution for "Remove Linked List Elements" (LeetCode #203).
     * Remove all elements from a linked list of integers that have value val.
     */
    public ListNode removeElements(ListNode head, int val) {
        // Create a dummy node to handle the case when the first node needs to be removed
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        ListNode prev = dummy;
        ListNode current = head;
        
        while (current != null) {
            if (current.val == val) {
                // Remove the current node
                prev.next = current.next;
            } else {
                // Move the prev pointer only if we didn't remove a node
                prev = current;
            }
            current = current.next;
        }
        
        return dummy.next;
    }
    
    /**
     * Solution for "Delete Node in a Linked List" (LeetCode #237).
     * Write a function to delete a node in a singly-linked list.
     * You will not be given access to the head of the list, 
     * instead you will be given access to the node to be deleted directly.
     */
    public void deleteNode(ListNode node) {
        if (node == null || node.next == null) {
            // Cannot delete the last node or null with this method
            return;
        }
        
        // Copy the value from the next node
        node.val = node.next.val;
        // Skip the next node
        node.next = node.next.next;
    }
    
    /**
     * Helper method to create a linked list from an array of integers.
     */
    public static ListNode createLinkedList(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        for (int val : values) {
            current.next = new ListNode(val);
            current = current.next;
        }
        
        return dummy.next;
    }
    
    /**
     * Helper method to convert a linked list to a string for display purposes.
     */
    public static String linkedListToString(ListNode head) {
        if (head == null) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        ListNode current = head;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(",");
            }
            current = current.next;
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Helper method to get the length of a linked list.
     */
    public static int getLength(ListNode head) {
        int length = 0;
        ListNode current = head;
        
        while (current != null) {
            length++;
            current = current.next;
        }
        
        return length;
    }
}
