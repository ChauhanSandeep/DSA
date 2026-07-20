package linkedlist;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Linked List Cycle II
 *
 * Given a linked list, return the node where a cycle begins. If there is no
 * cycle, return null. The primary Floyd solution leaves the list structure
 * unchanged.
 *
 * Leetcode: https://leetcode.com/problems/linked-list-cycle-ii/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Linked list | Floyd cycle detection | Two pointers
 *
 * Example:
 *   Input:  head = [3,2,0,-4], pos = 1
 *   Output: node with value 2
 *   Why:    the tail points back to the node at index 1.
 *
 * Follow-ups:
 *   1. How do you find cycle length?
 *      Walk once around the cycle after detecting a meeting point.
 *   2. How do you remove the cycle?
 *      Find the node before the entry and set its next to null.
 *   3. What if the structure is a graph?
 *      Use graph visited-state cycle detection instead.
 *
 * Related: Linked List Cycle (141), Find the Duplicate Number (287).
 */
public class LinkedListCycleII {

    public static void main(String[] args) {
        LinkedListCycleII solver = new LinkedListCycleII();
        ListNode head = new ListNode(3); head.next = new ListNode(2); head.next.next = new ListNode(0); head.next.next.next = new ListNode(-4); head.next.next.next.next = head.next;
        ListNode output = solver.detectCycle(head);
        System.out.printf("head=%s pos=%d -> %d  expected=%d%n", Arrays.toString(new int[] {3, 2, 0, -4}), 1, output == null ? -1 : output.val, 2);
        ListNode acyclic = new ListNode(1); acyclic.next = new ListNode(2);
        ListNode noCycle = solver.detectCycle(acyclic);
        System.out.printf("head=%s pos=%d -> %d  expected=%d%n", Arrays.toString(new int[] {1, 2}), -1, noCycle == null ? -1 : noCycle.val, -1);

        runBitManipulationDemo(solver, new int[] {1, 2}, -1);
        runBitManipulationDemo(solver, new int[] {3, 2, 0, -4}, 0);
        runBitManipulationDemo(solver, new int[] {3, 2, 0, -4}, 1);
        runBitManipulationDemo(solver, new int[] {7}, 0);
    }

    // Definition for singly-linked list
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    private static void runBitManipulationDemo(LinkedListCycleII solver, int[] values, int cycleEntryIndex) {
        ListNode[] nodes = buildList(values, cycleEntryIndex);
        ListNode output = solver.detectCycleBitManipulation(nodes.length == 0 ? null : nodes[0]);

        System.out.printf("detectCycleBitManipulation input=%s pos=%d -> %d  expected=%d%n",
            Arrays.toString(values), cycleEntryIndex, indexOf(nodes, output), cycleEntryIndex);
    }

    private static ListNode[] buildList(int[] values, int cycleEntryIndex) {
        ListNode[] nodes = new ListNode[values.length];
        for (int i = 0; i < values.length; i++) {
            nodes[i] = new ListNode(values[i]);
            if (i > 0) {
                nodes[i - 1].next = nodes[i];
            }
        }

        if (values.length > 0 && cycleEntryIndex >= 0) {
            nodes[values.length - 1].next = nodes[cycleEntryIndex];
        }

        return nodes;
    }

    private static int indexOf(ListNode[] nodes, ListNode target) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == target) {
                return i;
            }
        }

        return -1;
    }

        /**
     * Intuition: fast gains one node per step on slow, so the two pointers meet
     * if a cycle exists. Resetting one pointer to head makes both pointers the
     * same distance from the entry, so moving together finds the start.
     *
     * Algorithm:
     *   1. Return null for an empty or single-node list.
     *   2. Move slow by one and fast by two until they meet or fast reaches null.
     *   3. Return null if fast proves the list is acyclic.
     *   4. Move start from head and slow from the meeting point until they meet.
     *
     * Time:  O(n) - the two phases traverse a linear number of nodes.
     * Space: O(1) - only pointer variables are used.
     *
     * @param head head of the linked list
     * @return cycle entry node, or null if no cycle exists
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

    /** Recursively returns the first node seen twice. */
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
     * Intuition: the old value-marking idea was not safe because node values are
     * part of the input, not scratch space. Changing them can corrupt the list, and
     * a marker value can collide with a real value. Keep the public method name for
     * callers, but use Floyd's pointer-only cycle detection so the returned node is
     * the true cycle entry and the list is left unchanged.
     *
     * Algorithm:
     *   1. Run the standard Floyd cycle-entry routine.
     *   2. Return the entry node it finds, or null for an acyclic list.
     *
     * Time:  O(n) - Floyd's two phases walk only a linear number of nodes.
     * Space: O(1) - only pointer variables are used.
     *
     * @param head head of the linked list
     * @return cycle entry node, or null if no cycle exists
     */
    public ListNode detectCycleBitManipulation(ListNode head) {
        return detectCycle(head);
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