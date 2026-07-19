package trees.binarysearchtree;

/**
 * Problem: Lowest Common Ancestor of a Binary Search Tree
 *
 * Given a BST and two node values, find the lowest node whose subtree contains
 * both values. The BST ordering lets the search move left, move right, or stop at
 * the split point.
 *
 * Leetcode: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Ordered path search
 *
 * Example:
 *   Input:  root = [5,4,6,3,null,null,7,null,null,null,8], n1 = 3, n2 = 8
 *   Output: 5
 *   Why:    3 lies to the left of 5 and 8 lies to the right, so 5 is the split point.
 *
 * Follow-ups:
 *   1. What if the values may not exist?
 *      Search for both values first or return presence flags with the candidate.
 *   2. How would this work with duplicate values?
 *      Define a duplicate-side policy and use it consistently in comparisons.
 *   3. How would you answer many queries?
 *      Store parent/depth data or preprocess ancestors for faster repeated lookup.
 *   4. What changes for a plain binary tree?
 *      You must search both subtrees because ordering no longer prunes a path.
 *
 * Related: Lowest Common Ancestor of a Binary Tree (236).
 */

public class LowestCommonAncestorOfBinarySearchTree {

    // Node class to represent each node in the binary search tree
    static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

        public static void main(String[] args) {
        Node root = new Node(5);
        root.left = new Node(4);
        root.left.left = new Node(3);
        root.right = new Node(6);
        root.right.right = new Node(7);
        root.right.right.right = new Node(8);

        System.out.printf("n1=%d n2=%d -> %d  expected=7%n", 7, 8, findLCA(root, 7, 8).data);
        System.out.printf("n1=%d n2=%d -> %d  expected=5%n", 3, 8, findLCA(root, 3, 8).data);
    }


        /**
     * Intuition: in a BST, a node separates smaller values on the left from larger
     * values on the right. If both targets are below one side, the LCA must also be on
     * that side. The first node that does not send both targets the same way is the
     * lowest split point.
     *
     * Algorithm:
     *   1. Swap n1 and n2 if needed so n1 <= n2.
     *   2. Start from root as currentNode.
     *   3. Move left while currentNode.data is greater than n2.
     *   4. Move right while currentNode.data is less than n1; otherwise return currentNode.
     *
     * Time:  O(h) - one path from root to the split point is traversed.
     * Space: O(1) - only currentNode and a swap temp are stored.
     *
     * @param root root of the BST
     * @param n1 first target value
     * @param n2 second target value
     * @return LCA node, or null if traversal falls off the tree
     */
    static Node findLCA(Node root, int n1, int n2) {
        // Ensure that n1 is always smaller than n2 for clarity in comparison.
        if (n1 > n2) {
            int temp = n1;
            n1 = n2;
            n2 = temp;
        }

        Node currentNode = root;

        // Traverse the tree to find the LCA.
        while (currentNode != null) {
            // If both nodes n1 and n2 lie on the left side of the current node, move left.
            if (currentNode.data > n2) {
                currentNode = currentNode.left;
            }
            // If both nodes n1 and n2 lie on the right side of the current node, move right.
            else if (currentNode.data < n1) {
                currentNode = currentNode.right;
            }
            // If one node is on the left side and the other is on the right, current node is the LCA.
            else {
                return currentNode;
            }
        }

        // Return null if no LCA is found (this should not happen in a valid BST).
        return null;
    }
}
