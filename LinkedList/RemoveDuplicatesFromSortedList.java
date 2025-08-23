package LinkedList;

/**
 * Problem: Remove Duplicates from Sorted List
 * 
 * Given the head of a sorted linked list, delete all duplicates such that each element appears only once.
 * Return the linked list sorted as well.
 * 
 * Example:
 * Input: head = [1,1,2]
 * Output: [1,2]
 * Explanation: Remove one of the duplicate 1s.
 * 
 * LeetCode: https://leetcode.com/problems/remove-duplicates-from-sorted-list
 * 
 * Follow-up Questions:
 * 1. What if we need to remove ALL occurrences of duplicates (not keep one)?
 *    Answer: Use a more complex logic to track duplicates and remove entirely.
 *    Related: https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/
 * 
 * 2. How would you handle an unsorted linked list?
 *    Answer: Use HashSet to track seen values or sort first.
 * 
 * 3. Can we solve this recursively?
 *    Answer: Yes, recursively process the list and skip duplicates.
 * 
 * @author Sandeep
 */
public class RemoveDuplicatesFromSortedList {
    
    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Removes duplicates from sorted linked list using iterative approach.
     * 
     * Algorithm:
     * 1. Traverse the list with current pointer
     * 2. For each node, check if next node has same value
     * 3. If duplicate found, skip the duplicate node by updating next pointer
     * 4. Continue until all duplicates are removed
     * 
     * Time Complexity: O(n) where n is the number of nodes
     * Space Complexity: O(1) as we only use constant extra space
     * 
     * @param head Head of the sorted linked list
     * @return Head of the list with duplicates removed
     */
    public ListNode deleteDuplicates(ListNode head) {
        // Handle edge cases
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode current = head;
        
        // Traverse and remove duplicates
        while (current != null && current.next != null) {
            if (current.val == current.next.val) {
                // Skip the duplicate node
                current.next = current.next.next;
            } else {
                // Move to next unique element
                current = current.next;
            }
        }
        
        return head;
    }
    
    /**
     * Recursive approach to remove duplicates from sorted list.
     * 
     * Algorithm:
     * 1. Base case: if current node is null or last node, return as is
     * 2. Recursively process the rest of the list
     * 3. If current node value equals next node value, skip current and return next
     * 4. Otherwise, connect current to the processed rest and return current
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n) due to recursion stack
     * 
     * @param head Head of the sorted linked list
     * @return Head of the list with duplicates removed
     */
    public ListNode deleteDuplicatesRecursive(ListNode head) {
        // Base case
        if (head == null || head.next == null) {
            return head;
        }
        
        // Recursively process the rest
        head.next = deleteDuplicatesRecursive(head.next);
        
        // If current node is duplicate, skip it
        if (head.val == head.next.val) {
            return head.next;
        }
        
        return head;
    }
}