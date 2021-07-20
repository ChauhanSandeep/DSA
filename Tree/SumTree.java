package Tree;

public class SumTree {

    public static void main(String[] args) {
/*
                26
                /  \
              10     3
            /    \     \
          4      6      3
*/
        Node root = new Node(26);
        root.left = new Node(10);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(6);
        root.right.right = new Node(3);
        System.out.println("Is sum tree? " + isSumTree(root));
    }

    /**
     * Check if binary tree is sum tree.
     * A SumTree is a Binary Tree where the value of a node is equal to the sum of the nodes present in its left subtree and right subtree.
     */
    public static boolean isSumTree(Node root) {
        if (root == null) return true;
        try {
            int data = isSumTreeRec(root);
            return 2 * root.data == data;
        } catch (Exception e) {
            return false;
        }
    }

    public static int isSumTreeRec(Node node) {
        if (node.left == null && node.right == null) {
            return node.data;
        }

        if (node.left == null) {
            if (node.data == isSumTreeRec(node.right)) {
                return 2 * node.data;
            }
            throw new RuntimeException("Not sum tree");
        }

        if (node.right == null) {
            if (node.data == isSumTreeRec(node.left)) {
                return 2 * node.data;
            }
            throw new RuntimeException("Not sum tree");
        }

        if (node.data == isSumTreeRec(node.left) + isSumTreeRec(node.right)) {
            return 2 * node.data;
        }
        throw new RuntimeException("Not sum tree");
    }

}
