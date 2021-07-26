package Tree;


/**
 * A binary tree is called a height balanced binary tree if it satisfies following conditions -
 * 1. If at any given node, absolute difference of height of left sub-tree and height of right sub-tree is not greater than 1.
 * 2. For any given node, left sub-tree and right sub-tree that node are balanced binary trees themselves.
 */

class CheckBalanced {

    public static void main(String[] args) {
        /* Construct the following tree
                  1
                /   \
               /     \
              2       3
             / \     /
            4   5   6
                   /
                  7
        */

        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
//        root.right.left.left = new Node(7);

        if (isBalanced(root) != -1) System.out.print("Binary tree is balanced");
        else System.out.print("Binary tree is not balanced");
    }

    public static int isBalanced(Node node) {
        if(node == null) return 0;

        int leftHeight = isBalanced(node.left);
        int rightHeight = isBalanced(node.right);

        if(leftHeight == -1 || rightHeight == -1) return -1; // one of the subtree is not balanced

        if(Math.abs(leftHeight- rightHeight) > 1) return -1; // diff of subtree height is more than one

        return Math.max(leftHeight, rightHeight) + 1; // return maxHeight

    }

}