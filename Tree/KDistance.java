package Tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Find all the nodes at distance K from target Node in binary tree
 */
public class KDistance {

    List<Integer> ans;
    TreeNode target;
    int k;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(0);
        root.right = new TreeNode(1);
        root.right.right = new TreeNode(2);
        root.right.right.right = new TreeNode(3);
        new KDistance().distanceK(root, root.right, 2);
    }



    public List<Integer> distanceK(TreeNode root, TreeNode target, int K) {
        this.ans = new LinkedList<>();
        this.target = target;
        this.k = K;
        dfs(root);
        System.out.println(ans);
        return ans;
    }

    public int dfs(TreeNode node) {
        if(node == null) return -1;

        if(node == target) {
            addSubtree(node, 0);
            return 1;
        }
        int left = dfs(node.left);
        int right = dfs(node.right);

        if(left != -1) {
            if(left == k) ans.add(node.val);

            addSubtree(node.right, left+1);
            return left+1;
        }
        if(right != -1) {
            if(right == k) ans.add(node.val);

            addSubtree(node.left, right+1);
            return right+1;
        }

        return -1;
    }

    public void addSubtree(TreeNode node, int distance) {
        if(node == null || distance > k) {
            return;
        }
        if(distance == k) {
            System.out.println("Adding " + node.val);
            ans.add(node.val);
            return;
        }


        addSubtree(node.left, distance + 1);
        addSubtree(node.right, distance + 1);
    }
}
