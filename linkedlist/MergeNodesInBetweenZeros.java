package linkedlist;

/**
 * Problem: Merge Nodes in Between Zeros
 *
 * You are given the head of a linked list, which contains a series of integers separated by 0's.
 * The beginning and end of the linked list will have Node.val == 0.
 * For every two consecutive 0's, merge all the nodes lying in between them into a single node
 * whose value is the sum of all the merged nodes. The modified list should not contain any 0's.
 * Return the head of the modified linked list.
 *
 * Example:
 * Input: head = [0,3,1,0,4,5,2,0]
 * Output: [4,11]
 * Explanation:
 * - The sum of the nodes marked in green: 3 + 1 = 4.
 * - The sum of the nodes marked in red: 4 + 5 + 2 = 11.
 *
 * LeetCode: https://leetcode.com/problems/merge-nodes-in-between-zeros
 *
 * Follow-up Questions:
 * 1. What if the list doesn't start or end with 0?
 *    Answer: Add validation or modify logic to handle edge cases.
 *
 * 2. Can we solve this in-place without creating new nodes?
 *    Answer: Yes, reuse existing nodes and modify their values.
 *
 * 3. What if we need to preserve the original structure?
 *    Answer: Create new nodes instead of modifying existing ones.
 *
 * @author Sandeep
 */
public class MergeNodesInBetweenZeros {

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * Merges nodes between zeros by creating new nodes for sums.
     *
     * Algorithm:
     * 1. Skip the initial zero node
     * 2. For each segment between zeros, calculate the sum
     * 3. Create a new node with the sum value
     * 4. Connect the new nodes to form the result list
     *
     * Time Complexity: O(n) where n is the number of nodes
     * Space Complexity: O(1) excluding the result list
     *
     * @param head Head of the linked list starting and ending with 0
     * @return Head of the modified linked list without zeros
     */
    public ListNode mergeNodes(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode dummyHead = new ListNode(0);
        ListNode resultTail = dummyHead;
        ListNode current = head.next; // Skip the initial zero

        while (current != null) {
            int sum = 0;

            // Sum all nodes until we hit a zero
            while (current != null && current.val != 0) {
                sum += current.val;
                current = current.next;
            }

            // Create new node with the sum
            if (sum > 0) {
                resultTail.next = new ListNode(sum);
                resultTail = resultTail.next;
            }

            // Move past the zero
            if (current != null) {
                current = current.next;
            }
        }

        return dummyHead.next;
    }

    /**
     * In-place solution that modifies existing nodes to save space.
     * Reuses the first non-zero node in each segment to store the sum.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param head Head of the linked list
     * @return Head of the modified linked list
     */
    public ListNode mergeNodesInPlace(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode current = head.next; // Skip initial zero
        ListNode resultHead = current; // This will be our result head

        while (current != null) {
            int sum = 0;
            ListNode sumNode = current; // Node that will store the sum

            // Calculate sum for current segment
            while (current != null && current.val != 0) {
                sum += current.val;
                current = current.next;
            }

            // Store sum in the first node of the segment
            sumNode.val = sum;

            // Skip the zero and find next segment
            if (current != null) {
                current = current.next;
                sumNode.next = current;
            } else {
                sumNode.next = null;
            }
        }

        return resultHead;
    }

    /**
     * Recursive approach for merging nodes between zeros.
     * Uses recursion to process each segment.
     */
    public ListNode mergeNodesRecursive(ListNode head) {
        return mergeHelper(head.next); // Skip initial zero
    }

    // Helper method for recursive approach
    private ListNode mergeHelper(ListNode node) {
        if (node == null) return null;

        int sum = 0;
        ListNode current = node;

        // Calculate sum until zero
        while (current != null && current.val != 0) {
            sum += current.val;
            current = current.next;
        }

        // Create result node with sum
        ListNode resultNode = new ListNode(sum);

        // Recursively process remaining segments
        if (current != null && current.next != null) {
            resultNode.next = mergeHelper(current.next);
        }

        return resultNode;
    }
}