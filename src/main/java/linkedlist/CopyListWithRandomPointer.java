package linkedlist;

import java.util.Arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Copy List with Random Pointer
 *
 * Deep-copy a linked list where each node has next and random pointers. Every
 * copied pointer must target a copied node, never a node from the original list.
 *
 * Leetcode: https://leetcode.com/problems/copy-list-with-random-pointer/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Hash map | Deep copy
 *
 * Example:
 *   Input:  head = [[7,null],[13,0]]
 *   Output: [[7,null],[13,0]]
 *   Why:    the copied 13 node's random pointer targets the copied 7 node.
 *
 * Follow-ups:
 *   1. Can this use O(1) extra space?
 *      Interleave copies with originals, wire random pointers, then detach.
 *   2. What if random pointers form cycles?
 *      The node map still prevents duplicate copies and infinite traversal.
 *   3. How does this extend to graphs?
 *      Use the same original-to-copy map during DFS or BFS.
 *
 * Related: Clone Graph (133).
 */
public class CopyListWithRandomPointer {

    public static void main(String[] args) {
        CopyListWithRandomPointer solver = new CopyListWithRandomPointer();
        Node head = new Node(7);
        head.next = new Node(13);
        head.next.random = head;
        Node copied = solver.copyRandomList(head);
        int[][] output = { {copied.val, copied.random == null ? -1 : copied.random.val}, {copied.next.val, copied.next.random.val} };
        int[][] expected = { {7, -1}, {13, 7} };
        System.out.printf("head=%s -> %s  expected=%s%n", "[[7,null],[13,0]]", Arrays.deepToString(output), Arrays.deepToString(expected));
        Node single = new Node(1);
        single.random = single;
        Node singleCopy = solver.copyRandomList(single);
        int[][] singleOutput = { {singleCopy.val, singleCopy.random == singleCopy ? 0 : -1} };
        int[][] singleExpected = { {1, 0} };
        System.out.printf("head=%s -> %s  expected=%s%n", "[[1,0]]", Arrays.deepToString(singleOutput), Arrays.deepToString(singleExpected));
    }





    // Definition for a Node
    static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

                        /**
     * Intuition: random pointers can point anywhere, including backward, so build
     * all copied nodes before wiring pointers. The map then translates every
     * original next/random reference into its copied counterpart.
     *
     * Algorithm:
     *   1. Return null for an empty list.
     *   2. First pass: put each original node and a new copied node in nodeMap.
     *   3. Second pass: assign copied next and random pointers from nodeMap.
     *   4. Return nodeMap.get(head).
     *
     * Time:  O(n) - two passes over the original list.
     * Space: O(n) - one map entry per original node.
     *
     * @param head head of the original list
     * @return head of the deep-copied list
     */
    public Node copyRandomList(Node head) {
        if (head == null) return null;

        Map<Node, Node> nodeMap = new HashMap<>(); // <Original Node, Copied Node>

        // First pass: Create all nodes and store mapping
        Node current = head;
        while (current != null) {
            nodeMap.put(current, new Node(current.val));
            current = current.next;
        }

        // Second pass: Set next and random pointers
        current = head;
        while (current != null) {
            Node copiedNode = nodeMap.get(current);
            copiedNode.next = nodeMap.get(current.next);
            copiedNode.random = nodeMap.get(current.random);
            current = current.next;
        }

        return nodeMap.get(head);
    }

    /**
     * Space-optimized approach using interweaving technique.
     * Interweaving technique:
     * 1. Create copied nodes and place them next to original nodes
     * 2. Set random pointers for copied nodes
     * 3. Separate the two lists
     *
     * Algorithm:
     * 1. Create copied nodes and interweave with original nodes
     * 2. Set random pointers for copied nodes
     * 3. Separate the two lists
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1) excluding the result list
     *
     * @param head Head of the original linked list
     * @return Head of the deep copied linked list
     */
    public Node copyRandomListOptimized(Node head) {
        if (head == null) return null;

        // Pass 1: Create copied nodes and place them next to original nodes
        Node current = head;
        while (current != null) {
            Node copiedNode = new Node(current.val);
            copiedNode.next = current.next;
            current.next = copiedNode;
            current = copiedNode.next;
        }

        // Pass 2: Set random pointers for copied nodes
        current = head;
        while (current != null) {
            if (current.random != null) {
                current.next.random = current.random.next;
            }
            current = current.next.next;
        }

        // Pass 3: Separate the two lists
        Node copiedHead = head.next;
        Node originalCurrent = head;
        Node copiedCurrent = copiedHead;

        while (originalCurrent != null) {
            originalCurrent.next = copiedCurrent.next;
            originalCurrent = originalCurrent.next;

            if (originalCurrent != null) {
                copiedCurrent.next = originalCurrent.next;
                copiedCurrent = copiedCurrent.next;
            }
        }

        return copiedHead;
    }
}