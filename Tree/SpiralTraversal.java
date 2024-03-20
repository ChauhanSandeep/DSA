package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SpiralTraversal {

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(7);
        root.left.right = new Node(6);
        root.right.left = new Node(5);
        root.right.right = new Node(4);
        System.out.println("Spiral Order traversal of Binary Tree is ");
        System.out.println(spiralTraversal(root));
    }

    /**
     * print binary tree in spiral order traversal
     * @param root
     * @return
     */
    public static List<Integer> spiralTraversal(Node root) {
        List<Integer> result = new ArrayList<> ();

        if(root == null) return null;

        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        stack1.push(root);
        while(!stack1.isEmpty() || !stack2.isEmpty()) {
            while(!stack1.isEmpty()) {
                Node node = stack1.pop();
                result.add(node.data);
                if(node.right != null) {
                    stack2.push(node.right);
                }
                if(node.left != null) {
                    stack2.push(node.left);
                }
            }

            while(!stack2.isEmpty()) {
                Node node = stack2.pop();
                result.add(node.data);
                if(node.left != null) {
                    stack1.push(node.left);
                }
                if(node.right != null) {
                    stack1.push(node.right);
                }
            }
        }
        return result;

    }
}
