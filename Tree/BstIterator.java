package Tree;

import java.util.Stack;

/**
 * https://www.interviewbit.com/problems/bst-iterator/
 */
public class BstIterator {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(7);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(9);
        root.right = new TreeNode(13);
        root.right.right = new TreeNode(15);

        BstIterator iterator = new BstIterator(root);
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    Stack<TreeNode> stack;
    /**
     * @param root root of the tree to be iterated on
     */
    public BstIterator(TreeNode root) {
        stack = new Stack<>();
        TreeNode curr = root;
        while(curr != null) {
            stack.push(curr);
            curr = curr.left;
        }
    }

    /**
     * @return whether we have a next smallest number
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * @return the next smallest number
     */
    public int next() {
        TreeNode curr = stack.pop();
        int res = curr.val;
        curr = curr.right;
        while(curr != null) {
            stack.push(curr);
            curr = curr.left;
        }
        return res;

    }
}
