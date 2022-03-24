package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiagonalTraversal {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(8);
        root.left = new TreeNode(3);
        root.right = new TreeNode(10);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(6);
        root.right.right = new TreeNode(14);
        root.right.right.left = new TreeNode(13);
        root.left.right.left = new TreeNode(4);
        root.left.right.right = new TreeNode(7);
        ArrayList<Integer> solve = new DiagonalTraversal().solve(root);
        System.out.println(solve);

    }

    /**
     * Traverse binary tree in diagonal manner
     * https://www.interviewbit.com/problems/diagonal-traversal/
     */
    public ArrayList<Integer> solve(TreeNode root) {
        if(root == null) return null;
        List<List<Integer>> indexList = new ArrayList<>();

        traverse(root, 0, indexList);
        ArrayList<Integer> result = new ArrayList<>();
        for(List<Integer> list: indexList) {
            result.addAll(list);
        }
        return result;
    }

    private void traverse(TreeNode node, int index, List<List<Integer>> indexList) {
        if(node == null) return;
        traverse(node.left, index+1, indexList);
        while(indexList.size() <= index) {
            indexList.add(new ArrayList<>());
        }
        indexList.get(index).add(node.val);
        traverse(node.right, index, indexList);
    }
}
