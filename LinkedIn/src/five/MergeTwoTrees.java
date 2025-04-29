package five;
//Tree has keys and values, values represent sum of this and child node values
//NOTE: This is not a BTree
//When merging - absent branches should be created, and values for existing branches are summed
//Keys are unique among child nodes of a single parent
//Child nodes could be stored in any order

//  Source:            Target:                      Result:
//  ROOT:22            ROOT:20                      ROOT:42
//  /  |  \              / | \                    /   /      \  \
//W:2 K:16 C:4        W:5 K:2 R:13             W:7  K:18     C:4 R:13
//   / | \             |   |   \              /   /  | \  \       |
//V:3 E:6  F:7        M:5 M:2  P:13         M:5 V:3 E:6 F:7 M:2  P:13


// public class MergeTwoTrees {
//   public MergeTwoTrees() {
//     //implement if needed
//   }

//   public void merge(TreeNode s, TreeNode t) {
//     //implement this
//   }
// }

// interface TreeNode {
//   //Implement this
// }



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MergeTwoTrees {

    // Merges tree `source` into `target`
    public void merge(TreeNode target, TreeNode source) {
      if (target == null || source == null || source.children.isEmpty()) {
        return;
      }

        // Stack to track source tree traversal (DFS-style)
        Deque<NodeState> sourceStack = new ArrayDeque<>();

        // Stack to track target tree traversal in sync
        Deque<TreeNode> targetStack = new ArrayDeque<>();

        // Initialize both stacks with the roots
        sourceStack.push(new NodeState(0, source));
        targetStack.push(target);

        while (!sourceStack.isEmpty()) {
            NodeState sourceState = sourceStack.peek();
            TreeNode currentTargetNode = targetStack.peek();

            // If source node has more children to process
            if (sourceState.currentIndex < sourceState.node.children.size()) {
                TreeNode sourceChild = sourceState.node.children.get(sourceState.currentIndex);
                TreeNode targetChild = null;

                // Find matching child in target tree (by key)
                for (TreeNode child : currentTargetNode.children) {
                    if (child.key.equals(sourceChild.key)) {
                        targetChild = child;
                        // Merge value
                        targetChild.value += sourceChild.value;
                        break;
                    }
                }

                // If no matching key found, create and attach a new child
                if (targetChild == null) {
                    targetChild = new TreeNode(sourceChild.value, sourceChild.key);
                    currentTargetNode.children.add(targetChild);
                }

                // Push the child nodes to continue DFS traversal
                sourceStack.push(new NodeState(0, sourceChild));
                targetStack.push(targetChild);

                // Increment index of current node to process next child in future iteration
                sourceState.currentIndex++;

            } else {
                // All children processed; backtrack
                sourceStack.pop();
                targetStack.pop();
            }
        }

        // Merge root node values at the end
        target.value += source.value;
    }

    // Represents a node and current child index for DFS traversal
    private static class NodeState {
        int currentIndex;
        TreeNode node;

        NodeState(int index, TreeNode node) {
            this.currentIndex = index;
            this.node = node;
        }
    }

    // TreeNode class with key, value, and child list
    private static class TreeNode {
        String key;
        int value;
        List<TreeNode> children;

        TreeNode(int value, String key) {
            this.value = value;
            this.key = key;
            this.children = new ArrayList<>();
        }
    }
}
