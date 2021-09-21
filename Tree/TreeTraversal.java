package Tree;

import java.util.Stack;

public class TreeTraversal {

    public static void main(String[] args) {
    /*
            5
           /    \
         4       6
        / \       \
       3  14       7
                  / \
                 9   8
        */
        Node root = new Node(5);
        root.left = new Node(4);
        root.left.right = new Node(14);
        root.left.left = new Node(3);
        root.right = new Node(6);
        root.right.right = new Node(7);
        root.right.right.left = new Node(9);
        root.right.right.right = new Node(8);
        postorder(root);
        inorderIterative(root);
        preorder(root);

    }


    static void postorder(Node node) {
        if (node == null) return;

        postorder(node.left);
        postorder(node.right);
        System.out.print(node.data + "->");
    }

    static void inorder(Node node) {
        if (node == null) return;

        inorder(node.left);
        System.out.print(node.data + "->");
        inorder(node.right);
    }

    static void preorder(Node node) {
        if (node == null) return;

        System.out.print(node.data + "->");
        preorder(node.left);
        preorder(node.right);
    }

    public static void inorderIterative(Node root) {
        Stack<Node> stack = new Stack();
        Node curr = root;

        while (!stack.empty() || curr != null) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.pop();
                System.out.print(curr.data + "->");
                curr = curr.right;
            }
        }
    }

    public static void preorderIterative(Node root) {
        if (root == null) return;

        Stack<Node> stack = new Stack();
        stack.push(root);

        while (!stack.empty()) {
            Node curr = stack.pop();
            System.out.print(curr.data + "->");

//            the right child must be pushed first so that the left child is processed first (LIFO order)
            if (curr.right != null) stack.push(curr.right);
            if (curr.left != null) stack.push(curr.left);
        }
    }

    public static void postorderIterative(Node root) {
        // create an empty stack and push the root node
        Stack<Node> stack = new Stack();
        stack.push(root);

        // create another stack to store postorder traversal
        Stack<Integer> out = new Stack();

        // loop till stack is empty
        while (!stack.empty()) {
            // pop a node from the stack and push the data into the output stack
            Node curr = stack.pop();
            out.push(curr.data);

            // push the left and right child of the popped node into the stack
            if (curr.left != null) {
                stack.push(curr.left);
            }

            if (curr.right != null) {
                stack.push(curr.right);
            }
        }

        // print postorder traversal
        while (!out.empty()) {
            System.out.print(out.pop() + " ");
        }
    }

}
