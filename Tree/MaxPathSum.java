package Tree;

/**
 * Find max path sum from one node of tree to other node.
 * The nodes need not be starting/ending at left of root.
 */
public class MaxPathSum {

    int result = Integer.MIN_VALUE;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(-100);
        root.left = new TreeNode(-200);
        root.left.left = new TreeNode(-300);
        root.left.right = new TreeNode(-400);
        int ans = new MaxPathSum().maxPathSum(root);
        System.out.println(ans);
    }

    public int maxPathSum(TreeNode root) {
        if(root == null) return 0;
        maxPathSumRec(root);
        return result;
    }

    public Integer maxPathSumRec(TreeNode node) {
        if(node == null) return null;

        Integer left = maxPathSumRec(node.left);
        Integer right = maxPathSumRec(node.right);
        int curr = node.val;

        if(left == null && right == null) {
            this.result = Math.max(result, curr);
            return curr;
        } else if(left == null) {
            this.result = Math.max(result, Math.max(curr, Math.max(right + curr, right)));
            return curr + right;
        }else if (right == null) {
            this.result = Math.max(result, Math.max(curr, Math.max(left + curr, left)));
            return curr + left;
        }else {
            this.result = Math.max(result, Math.max(
                    curr, Math.max(
                            left + right + curr, Math.max(
                                    left + curr, Math.max(
                                            right + curr, Math.max(left, right))))));
            return Math.max(left+ curr, right + curr);
        }
    }
}
