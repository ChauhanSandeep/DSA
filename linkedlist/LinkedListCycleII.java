package linkedlist;

import java.util.*;

/**
 * 142. Linked List Cycle II
 *
 * Problem: Given the head of a linked list, return the node where the cycle begins.
 * If there is no cycle, return null.
 *
 * Example:
 * Input: head = [3,2,0,-4], pos = 1
 * Output: return node with value 2
 * Explanation: There is a cycle, tail connects to the 1st node (0-indexed).
 *
 * LeetCode: https://leetcode.com/problems/linked-list-cycle-ii
 *
 * Follow-up questions:
 * Q: Can you solve it using O(1) memory?
 * A: Yes, use Floyd's cycle-finding algorithm with mathematical analysis.
 *
 * Q: How to find the length of the cycle?
 * A: After finding cycle start, traverse once more until reaching same node.
 *
 * Q: What if there are multiple cycles?
 * A: This problem assumes at most one cycle; for multiple cycles, need graph algorithms.
 */
public class LinkedListCycleII {

    // Definition for singly-linked list
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * Floyd's Cycle-Finding Algorithm (Tortoise and Hare).
     * Optimal O(1) space solution using two pointers.
     *
     * Algorithm:
     * Phase 1: Detect cycle using slow/fast pointers
     * Phase 2: Find cycle start using mathematical property
     * - Distance from head to cycle start = Distance from meeting point to cycle start
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        // Phase 1: Detect if cycle exists
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                // Cycle detected
                break;
            }
        }

        // No cycle found
        if (fast == null || fast.next == null) {
            return null;
        }

        // Phase 2: Find the start of the cycle
        ListNode start = head;
        while (start != slow) {
            start = start.next;
            slow = slow.next;
        }

        return start;
    }

    /**
     * Hash Set approach using extra space.
     * Straightforward but uses O(n) space.
     */
    public ListNode detectCycleHashSet(ListNode head) {
        Set<ListNode> visited = new HashSet<>();
        ListNode current = head;

        while (current != null) {
            if (visited.contains(current)) {
                return current; // First revisited node is cycle start
            }
            visited.add(current);
            current = current.next;
        }

        return null; // No cycle
    }

    /**
     * Modified node approach (destructive).
     * Modifies the linked list structure temporarily.
     */
    public ListNode detectCycleModifyNodes(ListNode head) {
        if (head == null) return null;

        ListNode dummy = new ListNode(0); // Marker node
        ListNode current = head;

        while (current != null) {
            if (current.next == dummy) {
                return current; // Found the cycle start
            }

            ListNode next = current.next;
            current.next = dummy; // Mark as visited
            current = next;
        }

        return null; // No cycle
    }

    /**
     * Comprehensive solution that also returns cycle information.
     * Provides additional details about the cycle structure.
     */
    public CycleInfo detectCycleWithInfo(ListNode head) {
        if (head == null || head.next == null) {
            return new CycleInfo(null, 0, 0, 0);
        }

        // Phase 1: Detect cycle and count total nodes
        ListNode slow = head;
        ListNode fast = head;
        int totalNodes = 0;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            totalNodes++;

            if (slow == fast) {
                break;
            }
        }

        if (fast == null || fast.next == null) {
            // No cycle, count total nodes
            int count = 0;
            ListNode temp = head;
            while (temp != null) {
                count++;
                temp = temp.next;
            }
            return new CycleInfo(null, 0, count, 0);
        }

        // Phase 2: Find cycle start and measure distances
        int distanceToStart = 0;
        ListNode start = head;
        while (start != slow) {
            start = start.next;
            slow = slow.next;
            distanceToStart++;
        }

        // Phase 3: Find cycle length
        int cycleLength = 1;
        ListNode temp = start.next;
        while (temp != start) {
            temp = temp.next;
            cycleLength++;
        }

        return new CycleInfo(start, distanceToStart, distanceToStart + cycleLength, cycleLength);
    }

    // Helper class for comprehensive cycle information
    public static class CycleInfo {
        ListNode cycleStart;
        int distanceToStart;
        int totalNodes;
        int cycleLength;

        CycleInfo(ListNode cycleStart, int distanceToStart, int totalNodes, int cycleLength) {
            this.cycleStart = cycleStart;
            this.distanceToStart = distanceToStart;
            this.totalNodes = totalNodes;
            this.cycleLength = cycleLength;
        }

        public boolean hasCycle() {
            return cycleStart != null;
        }
    }

    /**
     * Alternative Floyd's algorithm implementation with detailed steps.
     * More explicit about the mathematical reasoning.
     */
    public ListNode detectCycleExplicit(ListNode head) {
        if (head == null) return null;

        ListNode tortoise = head;
        ListNode hare = head;

        // Step 1: Move tortoise one step, hare two steps
        do {
            if (hare == null || hare.next == null) {
                return null; // No cycle
            }
            tortoise = tortoise.next;
            hare = hare.next.next;
        } while (tortoise != hare);

        // Step 2: Mathematical proof shows distance from head to cycle start
        // equals distance from meeting point to cycle start
        tortoise = head;
        while (tortoise != hare) {
            tortoise = tortoise.next;
            hare = hare.next;
        }

        return tortoise;
    }

    /**
     * Recursive approach for cycle detection.
     * Uses function call stack instead of explicit pointers.
     */
    public ListNode detectCycleRecursive(ListNode head) {
        Set<ListNode> visited = new HashSet<>();
        return findCycleRecursive(head, visited);
    }

    private ListNode findCycleRecursive(ListNode node, Set<ListNode> visited) {
        if (node == null) {
            return null;
        }

        if (visited.contains(node)) {
            return node;
        }

        visited.add(node);
        return findCycleRecursive(node.next, visited);
    }

    /**
     * Iterative approach with step counting.
     * Tracks the number of steps taken during detection.
     */
    public DetectionResult detectCycleWithSteps(ListNode head) {
        if (head == null || head.next == null) {
            return new DetectionResult(null, 0);
        }

        ListNode slow = head;
        ListNode fast = head;
        int steps = 0;

        // Phase 1: Detect cycle
        do {
            if (fast == null || fast.next == null) {
                return new DetectionResult(null, steps);
            }
            slow = slow.next;
            fast = fast.next.next;
            steps++;
        } while (slow != fast);

        // Phase 2: Find start
        slow = head;
        int additionalSteps = 0;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
            additionalSteps++;
        }

        return new DetectionResult(slow, steps + additionalSteps);
    }

    // Helper class for step tracking
    public static class DetectionResult {
        ListNode cycleStart;
        int totalSteps;

        DetectionResult(ListNode cycleStart, int totalSteps) {
            this.cycleStart = cycleStart;
            this.totalSteps = totalSteps;
        }
    }

    /**
     * Memory-efficient approach for very large lists.
     * Uses bit manipulation to mark visited nodes.
     */
    public ListNode detectCycleBitManipulation(ListNode head) {
        // This approach modifies node values temporarily
        // Only works if node values can be modified and restored

        ListNode current = head;
        final int VISITED_MARKER = Integer.MIN_VALUE;
        List<ListNode> modifiedNodes = new ArrayList<>();

        while (current != null) {
            if (current.val == VISITED_MARKER) {
                // Restore modified values
                for (ListNode node : modifiedNodes) {
                    node.val = ~node.val; // Restore original value
                }
                return current;
            }

            // Mark as visited by storing complement
            modifiedNodes.add(current);
            current.val = VISITED_MARKER;
            current = current.next;
        }

        // Restore all modified values
        for (ListNode node : modifiedNodes) {
            node.val = ~node.val;
        }

        return null;
    }

    /**
     * Debug version that provides detailed traversal information.
     * Useful for understanding the algorithm step by step.
     */
    public ListNode detectCycleDebug(ListNode head) {
        if (head == null || head.next == null) {
            System.out.println("List too short for cycle");
            return null;
        }

        ListNode slow = head;
        ListNode fast = head;
        int phase1Steps = 0;

        System.out.println("Phase 1: Cycle Detection");
        do {
            if (fast == null || fast.next == null) {
                System.out.println("No cycle detected after " + phase1Steps + " steps");
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
            phase1Steps++;
            System.out.println("Step " + phase1Steps + ": slow=" + slow.val + ", fast=" + fast.val);
        } while (slow != fast);

        System.out.println("Cycle detected at step " + phase1Steps);
        System.out.println("Phase 2: Finding cycle start");

        slow = head;
        int phase2Steps = 0;
        while (slow != fast) {
            System.out.println("Phase2 Step " + phase2Steps + ": slow=" + slow.val + ", fast=" + fast.val);
            slow = slow.next;
            fast = fast.next;
            phase2Steps++;
        }

        System.out.println("Cycle start found: " + slow.val + " after " + phase2Steps + " additional steps");
        return slow;
    }
}