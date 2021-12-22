package Tree;

/**
 * Given a binary tree denoted by root node `root` and a leaf node `leaf` from this tree.
 *  Connected nodes (left child, right child and parent) get burned in 1 second.
 * Find minimum time required to burn the complete binary tree.
 */
public class BurnTree {

    public static void main(String[] args) {
    /*
            5
           /  \
         4      6
        / \      \
       3  14       7
                  / \
                 9   8
        */
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.right = new TreeNode(14);
        root.left.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);
        System.out.println("time required to burn tree is " + new BurnTree().solve(root, 14));

    }
    private int totalTime;
    public int solve(TreeNode root, int leaf) {
        this.totalTime = 0;
        traverse(root, leaf);
        return totalTime;
    }

    private int traverse(TreeNode node, int leaf) {
        if(node == null) return -1;
        if(node.val == leaf) return 0;

        int left = traverse(node.left, leaf);
        if(left != -1) {
            totalTime = Math.max(totalTime, left+1);
            burnChild(node.right, left+1);
            return left+1;
        }
        int right = traverse(node.right, leaf);
        if(right != -1) {
            totalTime = Math.max(totalTime, right+1);
            burnChild(node.left, right+1);
            return right+1;
        }
        return -1;
    }

    public void burnChild(TreeNode node, int time) {
        if(node == null) return;
        totalTime = Math.max(totalTime, 1+time);
        burnChild(node.left, time+1);
        burnChild(node.right, time+1);
    }


}
