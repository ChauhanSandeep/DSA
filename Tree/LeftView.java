package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LeftView {
    public static void main(String[] args) {
        Node root = new Node(10);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(7);
        root.left.right = new Node(8);
        root.right.right = new Node(15);
        root.right.left = new Node(12);
        root.right.right.left = new Node(14);

        List<Integer> leftNodes = printLeftView(root);
        System.out.println(leftNodes);
    }

    public static List<Integer> printLeftView(Node root) {
        List<Integer> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        if(root == null) return result;

        queue.offer(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i=0; i<size; i++) {
                Node node = queue.poll();
                if(i ==0) result.add(node.data);
                if(node.left != null) queue.offer(node.left);
                if(node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }
}
