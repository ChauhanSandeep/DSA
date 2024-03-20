package Tree;

public class CheckBst {

    public static void main(String[] args) {
        Node root = new Node(4);
        root.left = new Node(2);
        root.right = new Node(5);
        root.left.left = new Node(1);
        root.left.right = new Node(3);

        System.out.println("is BST? " + isBST(root));
    }

    public static boolean isBST(Node root) {
        return isBstRec(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    public static boolean isBstRec(Node node, int max, int min) {
        if (node == null) return true;

        return node.data < max &&
                node.data > min &&
                isBstRec(node.left, node.data, min) &&
                isBstRec(node.right, max, node.data);
    }

}
