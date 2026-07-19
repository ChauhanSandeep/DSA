package dailybytes.linkedlist;

/**
 * Problem: Linked List Cycle
 *
 * Given a singly linked list, determine whether following next pointers can loop
 * forever. A cycle exists when some node's next pointer leads back to an earlier
 * node instead of eventually reaching null.
 *
 * Leetcode: https://leetcode.com/problems/linked-list-cycle/ (Easy)
 * Rating:   acceptance 54.7% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Linked list | Floyd cycle detection | Slow and fast pointers
 *
 * Example:
 *   Input:  1 -> 2 -> 3 -> 4 -> 5, with 5.next pointing to 3
 *   Output: true
 *   Why:    a traversal revisits node 3 after node 5, so the list never ends.
 *
 * Follow-ups:
 *   1. Return the node where the cycle begins?
 *      After slow and fast meet, move one pointer to head and advance both one step.
 *   2. Find the cycle length?
 *      Keep one pointer fixed at the meeting node and walk the other around once.
 *   3. Detect a cycle without modifying nodes in a huge list?
 *      Floyd's algorithm already uses O(1) extra memory and no mutation.
 *   4. Detect cycles in a graph instead of a list?
 *      Use DFS coloring or union-find depending on graph direction.
 *
 * Related: Linked List Cycle II (142), Happy Number (202).
 */
public class CyclicLinkedList {

    public static void main(String[] args) {
        MyNode shared = new MyNode(3);
        MyLinkedList cyclic = new MyLinkedList(new MyNode(1, new MyNode(2, shared)));
        shared.setNext(new MyNode(4, new MyNode(5, shared)));

        MyLinkedList acyclic = new MyLinkedList();
        acyclic.push(new MyNode(1));
        acyclic.push(new MyNode(2));
        acyclic.push(new MyNode(3));

        MyLinkedList[] inputs = { cyclic, acyclic };
        String[] labels = { "1->2->3->4->5->3", acyclic.toString() };
        boolean[] expected = { true, false };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = findCycle(inputs[i]);
            System.out.printf("list=%s -> %b  expected=%b%n", labels[i], output, expected[i]);
        }
    }

    /**
     * Intuition: if a list has a loop, a faster pointer moving two steps at a
     * time must eventually lap a slower pointer moving one step at a time. If no
     * loop exists, the fast pointer reaches null first.
     *
     * Algorithm:
     *   1. Start slow and fast at list.getHead().
     *   2. Move slow one node and fast two nodes while fast can advance.
     *   3. Return true if both pointers reference the same node.
     *   4. Return false if fast reaches the end.
     *
     * Time:  O(n) - fast either reaches the end or meets slow after linear work.
     * Space: O(1) - only two pointers are stored.
     *
     * @param list singly linked list to inspect
     * @return true when the list contains a cycle
     */
    public static boolean findCycle(MyLinkedList list) {
        MyNode slow = list.getHead();
        MyNode fast = list.getHead();

        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();        // Move slow pointer one step
            fast = fast.getNext().getNext(); // Move fast pointer two steps

            if (slow == fast) return true; // If they meet, there is a cycle
        }

        return false; // No cycle
    }
}