package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class VerticalTraversal {

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        root.right.left.right = new Node(8);
        root.right.right.right = new Node(9);
        System.out.println("Vertical Order traversal is");
        System.out.println(verticalOrder(root));
    }

    /**
     * print tree vertically top to bottom, left to right
     * @param root
     * @return
     */
    static ArrayList <Integer> verticalOrder(Node root)
    {
        if(root == null) return null;

        Queue<IndexNode> queue = new LinkedList<>();
        IndexNode indexRoot = new IndexNode(root);
        queue.offer(indexRoot);

        Map<Integer, List<Integer>> map = new TreeMap<>();
        while(!queue.isEmpty()) {
            IndexNode indexNode = queue.poll();
            if(map.containsKey(indexNode.hd)) {
                map.get(indexNode.hd).add(indexNode.node.data);
            }else{
                List<Integer> list = new ArrayList<>();
                list.add(indexNode.node.data);
                map.put(indexNode.hd, list);
            }

            if(indexNode.node.left != null) {
                IndexNode leftNode = new IndexNode(indexNode.node.left, indexNode.hd - 1);
                queue.offer(leftNode);
            }
            if(indexNode.node.right != null) {
                IndexNode rightNode = new IndexNode(indexNode.node.right, indexNode.hd + 1);
                queue.offer(rightNode);
            }
        }
        ArrayList<Integer> result = new ArrayList<>();

        for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            for(Integer i: entry.getValue()) {
                result.add(i);
            }
        }
        return result;
    }

    // Few test cases are failing with this method
    public static void printVerticalOrder(Node root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        int hd = 0;

        printVerticalOrder(root, hd, map);

        for(Map.Entry<Integer, List<Integer>> entry: map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void printVerticalOrder(Node node, int hd, Map<Integer, List<Integer>> map) {
        if(map.containsKey(hd)) {
            map.get(hd).add(node.data);
        }else{
            List<Integer> list = new ArrayList<>();
            list.add(node.data);
            map.put(hd, list);
        }

        if(node.left != null) {
            printVerticalOrder(node.left, hd-1, map);
        }
        if(node.right != null) {
            printVerticalOrder(node.right, hd + 1, map);
        }
    }
}

class IndexNode {
    Node node;
    int hd;

    public IndexNode(Node node) {
        this.node = node;
        this.hd = 0;
    }
    public IndexNode(Node node, int hd) {
        this.node = node;
        this.hd = hd;
    }
}
