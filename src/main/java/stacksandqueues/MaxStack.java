package stacksandqueues;

import java.util.*;

/**
 * Problem: Max Stack
 *
 * Design a max stack data structure that supports the stack operations and supports finding the
 * stack's maximum element.
 *
 * Implement the MaxStack class:
 * - MaxStack() Initializes the stack object.
 * - void push(int x) Pushes element x onto the stack.
 * - int pop() Removes the element on top of the stack and returns it.
 * - int top() Gets the element on the top of the stack without removing it.
 * - int peekMax() Retrieves the maximum element in the stack without removing it.
 * - int popMax() Retrieves and removes the maximum element in the stack.
 *
 * Example:
 * Input: ["MaxStack", "push", "push", "push", "top", "popMax", "top", "peekMax", "pop", "top"]
 *        [[], [5], [1], [5], [], [], [], [], [], []]
 * Output: [null, null, null, null, 5, 5, 1, 5, 1, 5]
 *
 * LeetCode: https://leetcode.com/problems/max-stack
 *
 * Follow-up Questions:
 * 1. How would you optimize for frequent popMax operations?
 *    Answer: Use TreeMap with doubly linked list for O(log n) popMax instead of O(n).
 *
 * 2. What if we need to support getMin operation as well?
 *    Answer: Maintain separate min stack or extend current approach to track both max and min.
 *
 * 3. How would you handle concurrent access to the stack?
 *    Answer: Add synchronization using locks or use concurrent data structures.
 *    Related: https://leetcode.com/problems/min-stack/
 */
public class MaxStack {

    /**
     * Simple implementation using single stack with pairs.
     * Each element stores (value, maxSoFar).
     *
     * Algorithm:
     * 1. Use a single stack to store pairs of (value, currentMax).
     * 2. On push, determine the new max and store it with the value.
     * 3. On pop, simply pop from the stack.
     * 4. On top, return the value from the top pair.
     * 5. On peekMax, return the max from the top pair.
     * 6. On popMax, use a temporary stack to hold elements until max is found,
     *   then restore elements back to the main stack.
     *
     * Time Complexities:
     * - push, pop, top, peekMax: O(1)
     * - popMax: O(n) in worst case
     */
    static class MaxStackSimple implements MaxStackInterface {
        private Stack<int[]> stack; // [value, max_so_far]

        public MaxStackSimple() {
            stack = new Stack<>();
        }

        public void push(int x) {
            int currentMax = stack.isEmpty() ? x : Math.max(x, stack.peek()[1]);
            stack.push(new int[]{x, currentMax});
        }

        public int pop() {
            return stack.pop()[0];
        }

        public int peek() {
            return stack.peek()[0];
        }

        public int peekMax() {
            return stack.peek()[1];
        }

        public int popMax() {
            Stack<int[]> temp = new Stack<>();
            int maxVal = peekMax();

            // Find and remove the max element
            while (stack.peek()[0] != maxVal) {
                temp.push(stack.pop());
            }

            stack.pop(); // Remove the max element

            // Restore the temporary elements
            while (!temp.isEmpty()) {
                int val = temp.pop()[0];
                push(val); // Use push to maintain max values correctly
            }

            return maxVal;
        }
    }

    /**
     * Optimized implementation using TreeMap and custom Node structure.
     *
     * Algorithm:
     * 1. Use a doubly linked list to maintain stack order.
     * 2. Use a TreeMap to map values to lists of nodes for O(log n) max retrieval.
     * 3. Each push adds a new node to the linked list and updates the TreeMap.
     * 4. Each pop removes the tail node from the linked list and updates the TreeMap.
     * 5. peekMax retrieves the last key in the TreeMap.
     * 6. popMax retrieves and removes the last node from the list in the TreeMap, and removes it from the linked list.
     *
     * Time Complexities:
     * - push, pop, peek, peekMax: O(log n)
     * - popMax: O(log n)
     *
     * Space Complexity: O(n) for storing nodes and mappings.
     *
     * This implementation is more complex but optimizes popMax to O(log n).
     * It uses a combination of a doubly linked list for stack operations
     * and a TreeMap for efficient max value tracking.
     *
     *
     * Provides O(log n) for all operations including popMax.
     */
    static class MaxStackOptimized implements MaxStackInterface {
        private Node head;
        private Node tail;
        private int nodeId;
        private TreeMap<Integer, List<Node>> valueToNodes;
        private TreeMap<Integer, Node> idToNode;

        class Node {
            int val;
            int id;
            Node prev;
            Node next;

            Node(int val, int id) {
                this.val = val;
                this.id = id;
            }
        }

        public MaxStackOptimized() {
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
            nodeId = 0;
            valueToNodes = new TreeMap<>();
            idToNode = new TreeMap<>();
        }

        public void push(int val) {
            Node node = new Node(val, ++nodeId);

            // Add to doubly linked list
            addToTail(node);

            // Add to maps
            valueToNodes.computeIfAbsent(val, k -> new ArrayList<>()).add(node);
            idToNode.put(nodeId, node);
        }

        public int pop() {
            Node node = tail.prev;
            removeNode(node);

            // Clean up maps
            List<Node> nodes = valueToNodes.get(node.val);
            nodes.remove(node);
            if (nodes.isEmpty()) {
                valueToNodes.remove(node.val);
            }
            idToNode.remove(node.id);

            return node.val;
        }

        public int peek() {
            return tail.prev.val;
        }

        public int peekMax() {
            return valueToNodes.lastKey();
        }

        public int popMax() {
            int maxVal = peekMax();
            List<Node> nodes = valueToNodes.get(maxVal);
            Node nodeToRemove = nodes.get(nodes.size() - 1);

            removeNode(nodeToRemove);

            nodes.remove(nodes.size() - 1);
            if (nodes.isEmpty()) {
                valueToNodes.remove(maxVal);
            }
            idToNode.remove(nodeToRemove.id);

            return maxVal;
        }

        // Helper method to add node to tail of doubly linked list
        private void addToTail(Node node) {
            Node prevNode = tail.prev;
            prevNode.next = node;
            node.prev = prevNode;
            node.next = tail;
            tail.prev = node;
        }

        // Helper method to remove node from doubly linked list
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    // Common interface for all implementations
    interface MaxStackInterface {
        void push(int x);
        int pop();
        int peek();
        int peekMax();
        int popMax();
    }
}