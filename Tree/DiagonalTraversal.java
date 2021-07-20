package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiagonalTraversal {
    public static void main(String[] args) {
        Node root = new Node(8);
        root.left = new Node(3);
        root.right = new Node(10);
        root.left.left = new Node(1);
        root.left.right = new Node(6);
        root.right.right = new Node(14);
        root.right.right.left = new Node(13);
        root.left.right.left = new Node(4);
        root.left.right.right = new Node(7);

        List<List<Integer>> result = diagonalTraversal(root);
        System.out.println(result);
    }

    /**
     * Traverse binary tree in diagonal manner
     * https://www.geeksforgeeks.org/diagonal-traversal-of-binary-tree/
     */
    public static List<List<Integer>> diagonalTraversal(Node root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();

        diagonalTraversalRec(root, map, 0);
        List<List<Integer>> result = new ArrayList<>();
        for(Map.Entry<Integer, List<Integer>> entry: map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public static void diagonalTraversalRec(Node node, Map<Integer, List<Integer>> map, int row) {
        if(node == null) {
            return;
        }
        diagonalTraversalRec(node.left, map, row+1);
        if(map.get(row) == null) {
            List<Integer> list = new ArrayList<>();
            list.add(node.data);
            map.put(row, list);
        }else{
            List<Integer> list = map.get(row);
            list.add(node.data);
            map.put(row, list);
        }
        diagonalTraversalRec(node.right, map, row);
    }
}
