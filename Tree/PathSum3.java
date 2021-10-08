package Tree;

import java.util.HashMap;
import java.util.Map;

/**
 *https://leetcode.com/problems/path-sum-iii/
 *
 * Given the root of a binary tree and an integer targetSum, return the number of paths where the sum of the values along the path equals targetSum.
 * The path does not need to start or end at the root or a leaf, but it must go downwards (i.e., traveling only from parent nodes to child nodes).
 */
public class PathSum3 {

    public int result;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.left.left = new TreeNode(3);
        root.left.left.right = new TreeNode(-2);
        root.left.right = new TreeNode(2);
        root.left.right.right = new TreeNode(1);
        root.right = new TreeNode(-3);
        root.right.right = new TreeNode(11);

        int result = new PathSum3().pathSum(root, 8);
        System.out.println(result);
    }

    public int pathSum(TreeNode root, int sum) {
        Map<Integer, Integer> map = new HashMap<>();
        pathSum(root, 0, sum, map);
        return result;
    }

    public void pathSum(TreeNode root, int currSum, int k, Map<Integer, Integer> map) {
        if (root == null) return;
        currSum += root.val;

        if (root.val == k || currSum == k) {
            System.out.println("contians value at " + root.val);
            result++;
        }
        if (map.containsKey(currSum - k)) {
            System.out.println("contians currSum at " + root.val);
            result += map.get(currSum - k);
        }

        map.put(currSum, map.getOrDefault(currSum, 0) + 1);

        pathSum(root.left, currSum, k, map);
        pathSum(root.right, currSum, k, map);
        if (map.get(currSum) - 1 > 0) {
            map.put(currSum, map.get(currSum) - 1);
        } else {
            map.remove(currSum);
        }

    }
}
