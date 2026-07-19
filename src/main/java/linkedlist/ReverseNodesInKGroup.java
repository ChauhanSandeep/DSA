package linkedlist;

import java.util.*;

/**
 * Problem: Reverse Nodes in k-Group
 *
 * Reverse every complete group of k nodes in a singly linked list, leaving the
 * final incomplete group unchanged. This file keeps several original variants
 * for comparison.
 *
 * Leetcode: https://leetcode.com/problems/reverse-nodes-in-k-group/ (Hard)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | k-group reversal | Two pointers
 *
 * Example:
 *   Input:  head = [1,2,3,4,5], k = 3
 *   Output: [3,2,1,4,5]
 *   Why:    only the first three nodes form a complete group.
 *
 * Follow-ups:
 *   1. Avoid recursion stack growth?
 *      Use the iterative two-pointer or stack variant.
 *   2. Reverse groups from the right?
 *      Compute length, skip length % k nodes, then reverse full groups.
 *   3. Can group sizes vary?
 *      Accept an array of sizes and verify each group before reversing.
 *
 * Related: Swap Nodes in Pairs (24), Reverse Linked List II (92).
 */
public class ReverseNodesInKGroup {

    public static void main(String[] args) {
        ReverseNodesInKGroup solver = new ReverseNodesInKGroup();
        int[][] inputs = { {1, 2, 3, 4, 5}, {1, 2, 3, 4, 5} };
        int[] ks = {2, 3};
        int[][] expected = { {2, 1, 4, 3, 5}, {3, 2, 1, 4, 5} };
        for (int i = 0; i < inputs.length; i++) {
            ListNode head = ListUtils.createList(inputs[i]);
            ListNode outputHead = solver.reverseKGroupTwoPointer(head, ks[i]);
            int[] output = ListUtils.toArray(outputHead);
            System.out.printf("head=%s k=%d -> %s  expected=%s%n", Arrays.toString(inputs[i]), ks[i], Arrays.toString(output), Arrays.toString(expected[i]));
        }
    }

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

        /**
     * Intuition: separate the work into finding a complete k-node group,
     * reversing exactly that closed interval, and reconnecting it to the prefix
     * and suffix that stay outside the group.
     *
     * Algorithm:
     *   1. Return head when the list is empty or k is 1.
     *   2. Use dummy and prevGroupEndNode before the current group.
     *   3. While k nodes remain, identify group start, group end, and next group start.
     *   4. Reverse the group, reconnect it, and move prevGroupEndNode forward.
     *
     * Time:  O(n) - every node is counted and rewired a constant number of times.
     * Space: O(1) - only pointer variables are used.
     *
     * @param head head of the linked list
     * @param k group size to reverse
     * @return head after reversing each complete k-sized group
     */
    public ListNode reverseKGroupTwoPointer(ListNode head, int k) {
        if (head == null || k == 1) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupEndNode = dummy;

        while (hasKNodes(prevGroupEndNode.next, k)) {
            ListNode currGroupStartNode = prevGroupEndNode.next;
            ListNode currGroupEndNode = getKthNode(currGroupStartNode, k);
            ListNode nextGroupStartNode = currGroupEndNode.next;

            // Reverse current group
            ListNode reversedHead = reverseList(currGroupStartNode, currGroupEndNode);

            // Reconnect
            prevGroupEndNode.next = reversedHead;
            currGroupStartNode.next = nextGroupStartNode;
            prevGroupEndNode = currGroupStartNode;
        }

        return dummy.next;
    }

    /** Returns true when at least k nodes remain from head. */
    private boolean hasKNodes(ListNode head, int k) {
        while (k > 0 && head != null) {
            head = head.next;
            k--;
        }
        return k == 0;
    }

    /** Returns the kth node from head using 1-based counting. */
    private ListNode getKthNode(ListNode head, int k) {
        while (k > 1 && head != null) {
            head = head.next;
            k--;
        }
        return head;
    }

    /** Reverses the inclusive range start..end and returns end as the new head. */
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

    /** Counts the number of nodes in the list. */
    private int getLength(ListNode head) {
        int length = 0;
        while (head != null) {
            length++;
            head = head.next;
        }
        return length;
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

    /** Reverses exactly k nodes starting at head and returns the new head. */
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

    /** Splits the list into disconnected segments of at most segmentSize nodes. */
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

    /** Connects processed segments back into one list. */
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