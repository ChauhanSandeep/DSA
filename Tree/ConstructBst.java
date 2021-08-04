package Tree;

/**
 * Given preorder and inorder traversal of a binary tree. Create the tree.
 */
public class ConstructBst {

    public static void main(String[] args) {
        int[] preorder = {3,9,20,15,7};
        int[] inorder = {9,3,15,20,7};
        TreeNode result = new ConstructBst().buildTree(preorder, inorder);
        System.out.println(result);
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) return null;
        TreeNode result = buildTree(preorder, inorder, 0, preorder.length-1, 0);
        return result;
    }

    public TreeNode buildTree(int[] preorder, int[] inorder, int inLeft, int inRight, int preStart) {
        if(inLeft > inRight || preStart >= preorder.length) return null;
        if(inLeft == inRight) return new TreeNode(inorder[inLeft]);

        TreeNode currentNode = new TreeNode(preorder[preStart]);
        int pivot = findIndex(inorder, preorder[preStart]);

        TreeNode leftTree = buildTree(preorder, inorder, inLeft, pivot-1, preStart+1);
        TreeNode rightTree = buildTree(preorder, inorder, pivot+1, inRight, preStart + pivot - inLeft + 1);

        currentNode.left = leftTree;
        currentNode.right = rightTree;
        return currentNode;
    }

    public int findIndex(int[] arr, int target) {
        for(int i=0; i<arr.length; i++) {
            if(arr[i] == target) return i;
        }
        return -1;
    }


}
