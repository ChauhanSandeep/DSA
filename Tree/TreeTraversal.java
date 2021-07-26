package Tree;

public class TreeTraversal {

  public static void main(String[] args) {
    /*
            5
           /    \
         4       6
        / \       \
       3  14       7
                  / \
                 9   8
        */
    Node root = new Node(5);
    root.left = new Node(4);
    root.left.right = new Node(14);
    root.left.left = new Node(3);
    root.right = new Node(6);
    root.right.right = new Node(7);
    root.right.right.left = new Node(9);
    root.right.right.right = new Node(8);
    postorder(root);
    inorder(root);
    preorder(root);

  }


  static void postorder(Node node) {
    if (node == null) return;

    postorder(node.left);
    postorder(node.right);
    System.out.print(node.data + "->");
  }

  static void inorder(Node node) {
    if (node == null) return;

    inorder(node.left);
    System.out.print(node.data + "->");
    inorder(node.right);
  }

  static void preorder(Node node) {
    if (node == null) return;

    System.out.print(node.data + "->");
    preorder(node.left);
    preorder(node.right);
  }

}
