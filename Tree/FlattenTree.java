package Tree;

/**
 * Convert a binary tree to linked list
 */
public class FlattenTree {

    public static void main(String[] args) {
        /* Construct the following tree
                  1
                /   \
               /     \
              2       3
             / \     /
            4   5   6
                   /
                  7
         Result:
                   1
                    \
                     2
                      \
                       4
                        \
                         5
                          \
                           3
                            \
                             6
                              \
                               7
        */
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        new FlattenTree().flatten(root);
    }


    public void flatten(Node node) {
        if (node == null || (node.left == null && node.right == null)) return;

        /*
        1. flatten the left side of binary tree
        2. attach flattened left subtree to the right side
        3. attach existing right subtree to the end of the right subtree
        4. recursively flatten on the right side
         */
        if (node.left != null) {
            flatten(node.left);

            Node currRightNode = node.right;
            node.right = node.left;
            node.left = null;

            Node newRightNode = node.right;
            while (newRightNode.right != null) {
                newRightNode = newRightNode.right;
            }

            newRightNode.right = currRightNode;
        }
        flatten(node.right);
    }


}
