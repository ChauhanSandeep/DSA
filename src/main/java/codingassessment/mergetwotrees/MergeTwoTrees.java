package codingassessment.mergetwotrees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Problem: Merge Two N-ary Trees
 *
 * Given two N-ary trees (not binary trees), merge the source tree into the target tree.
 * Each node has a key (unique among siblings) and a value. When merging:
 * - If a key exists in both trees, sum the values
 * - If a key exists only in source tree, create new branch in target tree
 * - Keys are unique among child nodes of a single parent
 * - Child nodes can be stored in any order
 *
 * 📝 Example:
 *   Source:            Target:                      Result:
 *   ROOT:22            ROOT:20                      ROOT:42
 *   /  |  \              / | \                    /   /      \  \
 * W:2 K:16 C:4        W:5 K:2 R:13             W:7  K:18     C:4 R:13
 *    / | \             |   |   \              /   /  | \  \       |
 * V:3 E:6  F:7        M:5 M:2  P:13         M:5 V:3 E:6 F:7 M:2  P:13
 *
 * 🎯 Constraints:
 * - Tree nodes have unique keys among siblings
 * - Values are integers that can be summed
 * - Trees can have arbitrary number of children per node
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: How would you handle trees with millions of nodes efficiently?
 *    A: Use iterative DFS with explicit stack (as implemented) to avoid stack overflow.
 *       Consider using memory-mapped files or streaming for very large trees.
 *
 * 2. Q: What if we want to merge multiple trees (not just two)?
 *    A: Extend solution to merge trees pairwise: merge(merge(t1, t2), t3), or use HashMap
 *       at each level to aggregate values from all trees by key before creating merged nodes.
 *
 * 3. Q: How would you implement merge for binary trees instead of N-ary trees?
 *    A: Simpler approach - recursively merge left and right subtrees. At each node, sum values
 *       if both exist, or take non-null node if one is null.
 *
 * 4. Q: What if keys are not unique among siblings?
 *    A: Need to define merge strategy: sum all nodes with same key, or maintain separate nodes.
 *       May need to use position-based merging instead of key-based.
 *
 * 5. Q: How would you return a new merged tree without modifying target?
 *    A: Create deep copy of target first, then merge source into the copy. Or build new tree
 *       by traversing both trees simultaneously and creating new nodes.
 *
 * Related Problems:
 * - Merge Two Binary Trees (LeetCode #617)
 * - Clone N-ary Tree
 */
public class MergeTwoTrees {

    /**
     * Merges source tree into target tree using iterative DFS.
     *
     * Algorithm:
     * 1. Use two parallel stacks to traverse source and target trees in sync
     * 2. For each source node child, find matching key in target node children
     * 3. If key exists in target, sum values and push both children to stacks
     * 4. If key missing in target, create new subtree from source node
     * 5. After processing all children, merge root values
     *
     * Key insight: Using NodeState to track current child index enables iterative DFS
     * that mirrors recursive approach without stack overflow risk. Parallel traversal
     * maintains correspondence between source and target nodes.
     *
     * Time Complexity: O(n * m) where n is number of nodes in source tree and m is
     * average number of children per node (due to linear search for matching keys)
     *
     * Space Complexity: O(h) where h is height of source tree (for DFS stacks)
     *
     * @param target The target tree to merge into (modified in-place)
     * @param source The source tree to merge from (not modified)
     */
    public void merge(TreeNode target, TreeNode source) {
      if (target == null || source == null || source.children.isEmpty()) {
        return;
      }

        // Parallel stacks for synchronized DFS traversal
        Deque<NodeState> sourceStack = new ArrayDeque<>();
        Deque<TreeNode> targetStack = new ArrayDeque<>();

        // Start traversal from roots
        sourceStack.push(new NodeState(0, source));
        targetStack.push(target);

        while (!sourceStack.isEmpty()) {
            NodeState sourceState = sourceStack.peek();
            TreeNode currentTargetNode = targetStack.peek();

            // Process next child if available
            if (sourceState.currentIndex < sourceState.node.children.size()) {
                TreeNode sourceChild = sourceState.node.children.get(sourceState.currentIndex);
                TreeNode targetChild = null;

                // Search for matching key in target's children
                for (TreeNode child : currentTargetNode.children) {
                    if (child.key.equals(sourceChild.key)) {
                        targetChild = child;
                        targetChild.value += sourceChild.value;
                        break;
                    }
                }

                // Create new branch if key not found in target
                if (targetChild == null) {
                    targetChild = new TreeNode(sourceChild.value, sourceChild.key);
                    currentTargetNode.children.add(targetChild);
                }

                // Continue DFS into child nodes
                sourceStack.push(new NodeState(0, sourceChild));
                targetStack.push(targetChild);

                sourceState.currentIndex++;

            } else {
                // Backtrack after processing all children
                sourceStack.pop();
                targetStack.pop();
            }
        }

        // Merge root values
        target.value += source.value;
    }

    /**
     * Helper class to track DFS traversal state.
     * Maintains current position in children list to enable iterative traversal.
     */
    private static class NodeState {
        int currentIndex;
        TreeNode node;

        NodeState(int index, TreeNode node) {
            this.currentIndex = index;
            this.node = node;
        }
    }

    /**
     * N-ary tree node with key-value pair and children list.
     * Keys are unique among siblings to enable key-based matching during merge.
     */
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
