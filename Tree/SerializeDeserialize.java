package Tree;

import java.util.LinkedList;
import java.util.Queue;

public class SerializeDeserialize {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        String serializedString = new SerializeDeserialize().serialize(root);
        System.out.println(serializedString);
        TreeNode deserializedNode = new SerializeDeserialize().deserialize(serializedString);
        System.out.println(deserializedNode);
    }

    public String serialize(TreeNode root) {
        if(root == null) return "";
        StringBuilder builder = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if(curr == null) {
                builder.append("null, ");
            }else{
                builder.append(curr.val + ", ");
                queue.offer(curr.left);
                queue.offer(curr.right);
            }
        }
        return builder.toString();
    }

    public TreeNode deserialize(String data) {
        if(data == null || data.trim().length() == 0) return null;

        String[] nodes = data.split(", ");
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        queue.offer(root);
        int index = 1;

        while(!queue.isEmpty()) {
            TreeNode currNode = queue.poll();
            String leftVal = nodes[index++];
            String rightVal = nodes[index++];
            if(!leftVal.equals("null")) {
                TreeNode leftNode = new TreeNode(Integer.parseInt(leftVal));
                currNode.left = leftNode;
                queue.offer(leftNode);
            }
            if(!rightVal.equals("null")) {
                TreeNode rightNode = new TreeNode(Integer.parseInt(rightVal));
                currNode.right = rightNode;
                queue.offer(rightNode);
            }
        }
        return root;
    }
}
