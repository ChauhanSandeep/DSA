package Tree;

import com.sun.source.tree.BinaryTree;

import java.util.LinkedList;
import java.util.Queue;

public class ConnectNode {
    public static void main(String[] args) {

        /* Constructed binary tree is
             10
            /  \
          8     2
         /
        3
        */
        Node1 root = new Node1(10);
        root.left = new Node1(8);
        root.right = new Node1(2);
        root.left.left = new Node1(3);
        connect(root);
    }

    /**
     * Connect nodes at the same level in binary tree.
     * @param root
     */
    public static void connect(Node1 root) {
        Queue<Node1> queue1 = new LinkedList<>();
        Queue<Node1> queue2 = new LinkedList<>();
        queue1.offer(root);

        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            while (!queue1.isEmpty()) {
                Node1 node = queue1.poll();
                node.nextRight = queue1.peek();
                if (node.left != null) {
                    queue2.offer(node.left);
                }
                if (node.right != null) {
                    queue2.offer(node.right);
                }
            }
            while (!queue2.isEmpty()) {
                Node1 node = queue2.poll();
                node.nextRight = queue2.peek();
                if (node.left != null) {
                    queue1.offer(node.left);
                }
                if (node.right != null) {
                    queue1.offer(node.right);
                }
            }
        }
    }

    static class Node1{
        int data;
        Node1 left;
        Node1 right;
        Node1 nextRight;
        Node1(int data){
            this.data = data;
            left=null;
            right=null;
            nextRight = null;
        }
    }
}

