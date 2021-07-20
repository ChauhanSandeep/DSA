package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.TreeMap;

public class BottomView {
    public static void main(String[] args) {
        IndexedNode root = new IndexedNode(20);
        root.left = new IndexedNode(8);
        root.right = new IndexedNode(22);
        root.left.left = new IndexedNode(5);
        root.left.right = new IndexedNode(3);
        root.right.left = new IndexedNode(4);
        root.right.right = new IndexedNode(25);
        root.left.right.left = new IndexedNode(10);
        root.left.right.right = new IndexedNode(14);
        List<Integer> bottomView = getBottomView(root);
        System.out.println(bottomView);
    }

    private static List<Integer> getBottomView(IndexedNode root) {

        Map<Integer, IndexedNode> map = new TreeMap<>();
        List<Integer> result = new ArrayList<>();
        if(root == null) return result;

        root.hd = 0;
        Queue<IndexedNode> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty()) {
            IndexedNode node = queue.poll();
            map.put(node.hd, node);

            if(node.left != null) {
                IndexedNode leftNode = node.left;
                leftNode.hd = node.hd-1;
                queue.offer(leftNode);
            }
            if(node.right != null) {
                IndexedNode rightNode = node.right;
                rightNode.hd = node.hd+1;
                queue.offer(rightNode);
            }

        }

        for(Map.Entry<Integer, IndexedNode> entry: map.entrySet()) {
            result.add(entry.getValue().data);
        }
        return result;

    }
}

class IndexedNode {
    int data;
    public IndexedNode left;
    public IndexedNode right;
    public int hd;

    public IndexedNode(int item) {
        this.data = item;
        this.left = null;
        this.right = null;
    }
}
