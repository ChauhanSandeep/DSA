package LinkedList;

import java.util.*;

/**
 * 25. Reverse Nodes in k-Group
 * 
 * Problem: Given a linked list, reverse the nodes in groups of k and return the modified list.
 * If the number of nodes is not a multiple of k, the remaining nodes should stay as is.
 * 
 * Example:
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 * 
 * Input: head = [1,2,3,4,5], k = 3
 * Output: [3,2,1,4,5]
 * 
 * LeetCode: https://leetcode.com/problems/reverse-nodes-in-k-group
 * 
 * Follow-up questions:
 * Q: What if we need to reverse from right to left instead?
 * A: Use stack or two-pass approach to identify groups from the end.
 * 
 * Q: How to handle very long linked lists efficiently?
 * A: Use iterative approach with constant space to avoid stack overflow.
 * 
 * Q: Can we support different group sizes for different segments?
 * A: Extend to take array of group sizes and process accordingly.
 */
public class ReverseNodesInKGroup {
    
    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * Recursive approach - elegant but uses O(n/k) stack space.
     * 
     * Algorithm: Divide and conquer recursion
     * - Check if we have at least k nodes remaining
     * - Reverse current k nodes
     * - Recursively process remaining list
     * - Connect reversed group with recursively processed tail
     * 
     * Time Complexity: O(n) where n is number of nodes
     * Space Complexity: O(n/k) for recursion stack
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        // Check if we have at least k nodes
        ListNode current = head;
        for (int i = 0; i < k; i++) {
            if (current == null) return head; // Not enough nodes
            current = current.next;
        }
        
        // Reverse first k nodes
        ListNode prev = null;
        current = head;
        for (int i = 0; i < k; i++) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        // Recursively process remaining list
        head.next = reverseKGroup(current, k);
        
        return prev; // New head of reversed group
    }
    
    /**
     * Iterative approach - constant space complexity.
     * More efficient for very long lists.
     */
    public ListNode reverseKGroupIterative(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        // Count total nodes
        int length = getLength(head);
        int numGroups = length / k;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;
        
        for (int group = 0; group < numGroups; group++) {
            ListNode groupNext = groupPrev;
            
            // Find end of current group
            for (int i = 0; i < k; i++) {
                groupNext = groupNext.next;
            }
            
            ListNode nextGroupStart = groupNext.next;
            
            // Reverse current group
            ListNode[] reversed = reverseGroup(groupPrev.next, groupNext);
            
            // Connect with previous group
            groupPrev.next = reversed[0]; // New head
            reversed[1].next = nextGroupStart; // Connect tail
            
            // Move to next group
            groupPrev = reversed[1];
        }
        
        return dummy.next;
    }
    
    // Helper: get length of linked list
    private int getLength(ListNode head) {
        int length = 0;
        while (head != null) {
            length++;
            head = head.next;
        }
        return length;
    }
    
    // Helper: reverse group and return [newHead, newTail]
    private ListNode[] reverseGroup(ListNode start, ListNode end) {
        ListNode prev = end.next;
        ListNode current = start;
        
        while (current != end) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        current.next = prev;
        return new ListNode[]{end, start}; // [newHead, newTail]
    }
    
    /**
     * Stack-based approach for better readability.
     * Uses stack to simplify group reversal logic.
     */
    public ListNode reverseKGroupStack(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (true) {
            // Check if we have k nodes
            ListNode current = prev;
            for (int i = 0; i < k; i++) {
                current = current.next;
                if (current == null) {
                    return dummy.next; // Not enough nodes
                }
            }
            
            // Push k nodes to stack
            Stack<ListNode> stack = new Stack<>();
            current = prev.next;
            for (int i = 0; i < k; i++) {
                stack.push(current);
                current = current.next;
            }
            
            // Pop and reconnect
            while (!stack.isEmpty()) {
                prev.next = stack.pop();
                prev = prev.next;
            }
            
            prev.next = current;
        }
    }
    
    /**
     * Two-pointer approach with clear separation of concerns.
     * Separates node identification from reversal logic.
     */
    public ListNode reverseKGroupTwoPointer(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupEnd = dummy;
        
        while (hasKNodes(prevGroupEnd.next, k)) {
            ListNode groupStart = prevGroupEnd.next;
            ListNode groupEnd = getKthNode(groupStart, k);
            ListNode nextGroupStart = groupEnd.next;
            
            // Reverse current group
            ListNode reversedHead = reverseList(groupStart, groupEnd);
            
            // Reconnect
            prevGroupEnd.next = reversedHead;
            groupStart.next = nextGroupStart;
            prevGroupEnd = groupStart;
        }
        
        return dummy.next;
    }
    
    // Check if there are at least k nodes starting from head
    private boolean hasKNodes(ListNode head, int k) {
        while (k > 0 && head != null) {
            head = head.next;
            k--;
        }
        return k == 0;
    }
    
    // Get the kth node (1-indexed)
    private ListNode getKthNode(ListNode head, int k) {
        while (k > 1 && head != null) {
            head = head.next;
            k--;
        }
        return head;
    }
    
    // Reverse list from start to end (inclusive)
    private ListNode reverseList(ListNode start, ListNode end) {
        ListNode prev = end.next;
        ListNode current = start;
        
        while (current != end) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        current.next = prev;
        return end; // New head
    }
    
    /**
     * Optimized single-pass approach.
     * Combines length calculation with processing.
     */
    public ListNode reverseKGroupOptimized(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupEnd = dummy;
        ListNode current = head;
        
        while (current != null) {
            // Find group boundaries
            ListNode groupStart = current;
            int count = 0;
            
            // Move current to find group end
            while (current != null && count < k) {
                current = current.next;
                count++;
            }
            
            if (count == k) {
                // We have a full group, reverse it
                ListNode nextGroupStart = current;
                ListNode reversedHead = reverseKNodes(groupStart, k);
                
                prevGroupEnd.next = reversedHead;
                groupStart.next = nextGroupStart;
                prevGroupEnd = groupStart;
            }
        }
        
        return dummy.next;
    }
    
    // Reverse exactly k nodes starting from head
    private ListNode reverseKNodes(ListNode head, int k) {
        ListNode prev = null;
        ListNode current = head;
        
        for (int i = 0; i < k; i++) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev; // New head
    }
    
    /**
     * Extension: Reverse k-group from right to left.
     * Reverses groups starting from the end of the list.
     */
    public ListNode reverseKGroupFromRight(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        int length = getLength(head);
        int fullGroups = length / k;
        int offset = length % k;
        
        // Skip offset nodes
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        for (int i = 0; i < offset; i++) {
            prev = prev.next;
        }
        
        // Reverse full groups
        for (int group = 0; group < fullGroups; group++) {
            ListNode groupStart = prev.next;
            
            // Find group end
            ListNode groupEnd = groupStart;
            for (int i = 1; i < k; i++) {
                groupEnd = groupEnd.next;
            }
            
            ListNode nextGroupStart = groupEnd.next;
            
            // Reverse group
            ListNode reversedHead = reverseList(groupStart, groupEnd);
            prev.next = reversedHead;
            groupStart.next = nextGroupStart;
            prev = groupStart;
        }
        
        return dummy.next;
    }
    
    /**
     * Variable group sizes approach.
     * Takes array of group sizes and processes accordingly.
     */
    public ListNode reverseVariableGroups(ListNode head, int[] groupSizes) {
        if (head == null || groupSizes.length == 0) return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        for (int k : groupSizes) {
            if (k <= 1) continue;
            
            // Check if we have enough nodes
            if (!hasKNodes(prev.next, k)) {
                break; // Not enough nodes for this group
            }
            
            ListNode groupStart = prev.next;
            ListNode groupEnd = getKthNode(groupStart, k);
            ListNode nextGroupStart = groupEnd.next;
            
            // Reverse current group
            ListNode reversedHead = reverseList(groupStart, groupEnd);
            
            // Reconnect
            prev.next = reversedHead;
            groupStart.next = nextGroupStart;
            prev = groupStart;
        }
        
        return dummy.next;
    }
    
    /**
     * Parallel processing approach for very long lists.
     * Divides list into segments and processes concurrently.
     */
    public ListNode reverseKGroupParallel(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        int length = getLength(head);
        if (length < 10000) {
            return reverseKGroup(head, k); // Use sequential for small lists
        }
        
        // Split list into segments
        int segmentSize = Math.max(1000, length / Runtime.getRuntime().availableProcessors());
        List<ListNode> segments = splitList(head, segmentSize);
        
        // Process segments in parallel
        List<ListNode> processedSegments = segments.parallelStream()
            .map(segment -> reverseKGroup(segment, k))
            .collect(java.util.stream.Collectors.toList());
        
        // Reconnect segments
        return reconnectSegments(processedSegments);
    }
    
    // Split list into segments of given size
    private List<ListNode> splitList(ListNode head, int segmentSize) {
        List<ListNode> segments = new ArrayList<>();
        ListNode current = head;
        
        while (current != null) {
            ListNode segmentStart = current;
            ListNode prev = null;
            
            // Move to end of current segment
            for (int i = 0; i < segmentSize && current != null; i++) {
                prev = current;
                current = current.next;
            }
            
            // Disconnect segment
            if (prev != null) {
                prev.next = null;
            }
            
            segments.add(segmentStart);
        }
        
        return segments;
    }
    
    // Reconnect processed segments
    private ListNode reconnectSegments(List<ListNode> segments) {
        if (segments.isEmpty()) return null;
        
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        for (ListNode segment : segments) {
            current.next = segment;
            
            // Move to end of current segment
            while (current.next != null) {
                current = current.next;
            }
        }
        
        return dummy.next;
    }
    
    /**
     * Memory-efficient approach with minimal auxiliary space.
     * Uses only a few variables regardless of list size.
     */
    public ListNode reverseKGroupMemoryEfficient(ListNode head, int k) {
        if (head == null || k == 1) return head;
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupEnd = dummy;
        
        while (true) {
            // Check and find kth node
            ListNode kthNode = prevGroupEnd;
            for (int i = 0; i < k; i++) {
                kthNode = kthNode.next;
                if (kthNode == null) {
                    return dummy.next; // Not enough nodes
                }
            }
            
            ListNode nextGroupStart = kthNode.next;
            
            // Reverse group in place
            ListNode prev = nextGroupStart;
            ListNode current = prevGroupEnd.next;
            
            while (current != nextGroupStart) {
                ListNode next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }
            
            // Update connections
            ListNode originalGroupStart = prevGroupEnd.next;
            prevGroupEnd.next = kthNode;
            prevGroupEnd = originalGroupStart;
        }
    }
    
    /**
     * Utility methods for testing and validation.
     * Helper functions for linked list manipulation and verification.
     */
    public static class ListUtils {
        
        // Create linked list from array
        public static ListNode createList(int[] values) {
            if (values.length == 0) return null;
            
            ListNode head = new ListNode(values[0]);
            ListNode current = head;
            
            for (int i = 1; i < values.length; i++) {
                current.next = new ListNode(values[i]);
                current = current.next;
            }
            
            return head;
        }
        
        // Convert linked list to array
        public static int[] toArray(ListNode head) {
            List<Integer> values = new ArrayList<>();
            
            while (head != null) {
                values.add(head.val);
                head = head.next;
            }
            
            return values.stream().mapToInt(Integer::intValue).toArray();
        }
        
        // Print linked list
        public static void printList(ListNode head) {
            StringBuilder sb = new StringBuilder();
            
            while (head != null) {
                sb.append(head.val);
                if (head.next != null) {
                    sb.append(" -> ");
                }
                head = head.next;
            }
            
            System.out.println(sb.toString());
        }
        
        // Check if two lists are equal
        public static boolean areEqual(ListNode list1, ListNode list2) {
            while (list1 != null && list2 != null) {
                if (list1.val != list2.val) {
                    return false;
                }
                list1 = list1.next;
                list2 = list2.next;
            }
            
            return list1 == null && list2 == null;
        }
        
        // Verify k-group reversal correctness
        public static boolean verifyKGroupReversal(ListNode original, ListNode result, int k) {
            int[] originalArray = toArray(original);
            int[] resultArray = toArray(result);
            
            if (originalArray.length != resultArray.length) {
                return false;
            }
            
            int n = originalArray.length;
            int numFullGroups = n / k;
            
            // Check full groups are reversed
            for (int group = 0; group < numFullGroups; group++) {
                int start = group * k;
                
                for (int i = 0; i < k; i++) {
                    if (originalArray[start + i] != resultArray[start + k - 1 - i]) {
                        return false;
                    }
                }
            }
            
            // Check remaining elements are unchanged
            int remainingStart = numFullGroups * k;
            for (int i = remainingStart; i < n; i++) {
                if (originalArray[i] != resultArray[i]) {
                    return false;
                }
            }
            
            return true;
        }
        
        // Create a copy of linked list
        public static ListNode copyList(ListNode head) {
            if (head == null) return null;
            
            ListNode dummy = new ListNode(0);
            ListNode current = dummy;
            
            while (head != null) {
                current.next = new ListNode(head.val);
                current = current.next;
                head = head.next;
            }
            
            return dummy.next;
        }
    }
}