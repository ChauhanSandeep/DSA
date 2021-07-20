package Tree;

public class LowestCommonAncestor {
    public static void main(String[] args) {
        /*
            5
           /    \
         4       6
        /         \
       3           7
                    \
                     8
        */
        Node root = new Node(5);
        root.left = new Node(4);
        root.left.left = new Node(3);
        root.right = new Node(6);

        root.right.right = new Node(7);
        root.right.right.right = new Node(8);
        System.out.printf("Lowest common ancestor is %s\n", findLCA(root, 7, 8).data);
        System.out.printf("Lowest common ancestor is %s\n", findLCA(root, 3, 8).data);
    }

    /**
     * Given two nodes of a BST, find the lowest common ancestor
     * @param root
     * @param n1
     * @param n2
     * @return
     */
    static Node findLCA(Node root, int n1, int n2) {
        Node temp = root;
        while (temp != null) {
            if ((temp.data <= n2 && temp.data >= n1) || (temp.data >= n2 && temp.data <= n1)) {
                return temp;
            }

            if (temp.data < n1 && temp.data < n2) temp = temp.right;
            else temp = temp.left;
        }
        return temp;
    }
}
