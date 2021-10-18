package Tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Find all the nodes at distance K from target Node in binary tree
 */
public class KDistance {

    List<Integer> ans;
    TreeNode target;
    int K;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        new KDistance().distanceK(root, root.left, 1);
    }



    public List<Integer> distanceK(TreeNode root, TreeNode target, int K) {
        ans = new LinkedList<>();
        this.target = target;
        this.K = K;
        dfs(root);
        System.out.println(ans);
        return ans;
    }

    // Return vertex distance from node to target if exists, else -1
    // Vertex distance: the number of vertices on the path from node to target
    public int dfs(TreeNode node) {
        if (node == null) return -1;
        if (node == target) {
            addSubtree(node, 0);
            return 1;
        }
        int left = dfs(node.left);
        int right = dfs(node.right);
        if (left != -1) {
            if (left == K) ans.add(node.val);
            addSubtree(node.right, left + 1);
            return left + 1;
        }
        if (right != -1) {
            if (right == K) ans.add(node.val);
            addSubtree(node.left, right + 1);
            return right + 1;
        }
        return -1;
    }

    // Add all nodes 'K - dist' from the node to answer.
    public void addSubtree(TreeNode node, int dist) {
        if (node == null) return;
        if (dist == K) {
            ans.add(node.val);
        } else {
            addSubtree(node.left, dist + 1);
            addSubtree(node.right, dist + 1);
        }
    }
}
