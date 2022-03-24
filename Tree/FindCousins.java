package Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * https://www.interviewbit.com/problems/cousins-in-binary-tree/
 */
public class FindCousins {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        ArrayList<Integer> result = new FindCousins().solve(root, 5);
        System.out.println(result);
    }

    Map<Integer, ArrayList<Integer>> map;
    int resultLevel;
    public ArrayList<Integer> solve(TreeNode root, int target) {
        if(root == null || root.val == target) return new ArrayList<>();

        map = new HashMap<>();
        this.resultLevel = -1;
        traverse(root, target, 0);
        return map.get(resultLevel);
    }

    private boolean traverse(TreeNode node, int target, int level) {
        if(node == null) return false;
        map.putIfAbsent(level, new ArrayList<>());

        if(node.val == target) {
            resultLevel = level;
            return true;
        }
        map.get(level).add(node.val);
        boolean foundLeft = traverse(node.left, target, level+1);
        if(foundLeft) return false; // don't traverse right sibling

        boolean foundRight = traverse(node.right, target, level+1);
        if(foundRight && node.left != null) {
            // remove left sibling from result
            map.get(level+1).remove(Integer.valueOf(node.left.val));
        }
        return false;
    }
}
