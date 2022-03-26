package Tree;

import java.util.ArrayList;

/**
 * Find all the nodes at distance K from target Node in binary tree
 */
public class KDistance {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(0);
        root.right = new TreeNode(1);
        root.right.right = new TreeNode(2);
        root.right.right.right = new TreeNode(3);
        new KDistance().distanceK(root, 1, 2);
    }

    ArrayList<Integer> result;

    public ArrayList<Integer> distanceK(TreeNode root, int target, int distance) {
        this.result = new ArrayList<>();
        findNodes(root, target, distance);
        return result;
    }

    private int findNodes(TreeNode node, int target, int distance) {
        if (node == null) return -1;
        if (node.val == target) {
            markChild(node.left, distance, 1);
            markChild(node.right, distance, 1);
            return 1;
        }
        int left = findNodes(node.left, target, distance);
        if (left > 0 && left <= distance) {
            if (left == distance) {
                result.add(node.val);
                return left + 1;
            }
            markChild(node.right, distance, left + 1);
            return left + 1;
        }


        int right = findNodes(node.right, target, distance);
        if (right > 0 && right <= distance) {
            if (right == distance) {
                result.add(node.val);
                return right + 1;
            }
            markChild(node.left, distance, right + 1);
            return right + 1;
        }
        return -1;
    }

    public void markChild(TreeNode node, int distance, int curr) {
        if (node == null || curr > distance) return;
        if (curr == distance) result.add(node.val);

        markChild(node.left, distance, curr + 1);
        markChild(node.right, distance, curr + 1);
    }
}
