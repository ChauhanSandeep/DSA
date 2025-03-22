package tree;

/**
 * Given a Binary Search Tree (BST) and two node values, this program finds the Lowest Common Ancestor (LCA) of the two nodes.
 *
 * Intuition:
 * - In a BST, the left child is smaller than the parent node and the right child is greater.
 * - The LCA of two nodes is the deepest node that is an ancestor of both nodes.
 * - If both node values are smaller than the current node, the LCA lies in the left subtree. 
 * - If both node values are larger than the current node, the LCA lies in the right subtree. 
 * - If one node is on one side and the other is on the opposite side, the current node is their LCA.
 *
 * Time Complexity: O(H) where H is the height of the tree. In the worst case, this could be O(N), where N is the number of nodes.
 * Space Complexity: O(1), since we're using only a constant amount of extra space.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
 */

public class LowestCommonAncestor {
    
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
        /*
            Example Tree:
                    5
                   /   \
                 4      6
                /        \
               3          7
                         \
                          8
        */
        
        // Construct the example tree
        Node root = new Node(5);
        root.left = new Node(4);
        root.left.left = new Node(3);
        root.right = new Node(6);
        root.right.right = new Node(7);
        root.right.right.right = new Node(8);

        // Test case 1: Find LCA of nodes 7 and 8
        System.out.printf("Lowest common ancestor of 7 and 8 is: %d\n", findLCA(root, 7, 8).data);
        
        // Test case 2: Find LCA of nodes 3 and 8
        System.out.printf("Lowest common ancestor of 3 and 8 is: %d\n", findLCA(root, 3, 8).data);
    }

    /**
     * Finds the Lowest Common Ancestor (LCA) of two nodes in a Binary Search Tree (BST).
     *
     * @param root The root node of the BST.
     * @param n1   The value of the first node.
     * @param n2   The value of the second node.
     * @return The LCA node of the two nodes.
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
