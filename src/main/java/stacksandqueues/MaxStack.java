package stacksandqueues;

import java.util.Stack;

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
 *
 * @author Sandeep
 */
public class MaxStack {

    /**
     * Implementation using two stacks approach.
     * Main stack stores values, aux stack stores max values.
     *
     * Time Complexities:
     * - push, pop, top, peekMax: O(1)
     * - popMax: O(n) in worst case
     *
     * Space Complexity: O(n)
     */
    static class MaxStackTwoStacks implements MaxStackInterface {
        private Stack<Integer> stack;
        private Stack<Integer> maxStack;

        public MaxStackTwoStacks() {
            stack = new Stack<>();
            maxStack = new Stack<>();
        }

        public void push(int x) {
            stack.push(x);

            // Push current max to max stack
            if (maxStack.isEmpty()) {
                maxStack.push(x);
            } else {
                maxStack.push(Math.max(x, maxStack.peek()));
            }
        }

        public int pop() {
            maxStack.pop();
            return stack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int peekMax() {
            return maxStack.peek();
        }

        public int popMax() {
            int max = peekMax();
            Stack<Integer> temp = new Stack<>();

            // Pop elements until we find the max element
            while (top() != max) {
                temp.push(pop());
            }

            // Remove the max element
            pop();

            // Push back the temporary elements
            while (!temp.isEmpty()) {
                push(temp.pop());
            }

            return max;
        }
    }

    /**
     * Optimized implementation using TreeMap and custom Node structure.
     * Provides O(log n) for all operations including popMax.
     */
    static class MaxStackOptimized implements MaxStackInterface {
        private Node head;
        private Node tail;
        private int nodeId;
        private java.util.TreeMap<Integer, java.util.List<Node>> valueToNodes;
        private java.util.TreeMap<Integer, Node> idToNode;

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
            valueToNodes = new java.util.TreeMap<>();
            idToNode = new java.util.TreeMap<>();
        }

        public void push(int x) {
            Node node = new Node(x, ++nodeId);

            // Add to doubly linked list
            addToTail(node);

            // Add to maps
            valueToNodes.computeIfAbsent(x, k -> new java.util.ArrayList<>()).add(node);
            idToNode.put(nodeId, node);
        }

        public int pop() {
            Node node = tail.prev;
            removeNode(node);

            // Clean up maps
            java.util.List<Node> nodes = valueToNodes.get(node.val);
            nodes.remove(node);
            if (nodes.isEmpty()) {
                valueToNodes.remove(node.val);
            }
            idToNode.remove(node.id);

            return node.val;
        }

        public int top() {
            return tail.prev.val;
        }

        public int peekMax() {
            return valueToNodes.lastKey();
        }

        public int popMax() {
            int maxVal = peekMax();
            java.util.List<Node> nodes = valueToNodes.get(maxVal);
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

    /**
     * Simple implementation using single stack with pairs.
     * Each element stores (value, max_so_far).
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

        public int top() {
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
     * Design pattern example: Factory method for different implementations.
     */
    public enum StackType {
        TWO_STACKS, OPTIMIZED, SIMPLE
    }

    public static MaxStackInterface createMaxStack(StackType type) {
        switch (type) {
            case TWO_STACKS:
                return new MaxStackTwoStacks();
            case OPTIMIZED:
                return new MaxStackOptimized();
            case SIMPLE:
                return new MaxStackSimple();
            default:
                throw new IllegalArgumentException("Unknown stack type: " + type);
        }
    }

    // Common interface for all implementations
    interface MaxStackInterface {
        void push(int x);
        int pop();
        int top();
        int peekMax();
        int popMax();
    }

    // Make all implementations implement the common interface
    static {
        // This ensures all implementations can be used interchangeably
    }
}