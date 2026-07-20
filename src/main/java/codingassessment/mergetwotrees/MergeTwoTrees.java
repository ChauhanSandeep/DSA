package codingassessment.mergetwotrees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Problem: Merge Two N-ary Trees
 *
 * Given a target N-ary tree and a source N-ary tree, merge the source into the
 * target in place. Nodes are matched by key among siblings. Matching nodes have
 * their values summed, while source-only children are copied into the matching
 * target location.
 *
 * Pattern:  Tree | Iterative DFS | Key-based sibling matching
 *
 * Example:
 *   Input:  target = A:10[B:1,C:2], source = A:5[B:4,D:7]
 *   Output: A:15[B:5,C:2,D:7]
 *   Why:    A and B match by key and have their values summed, C remains from the
 *           target, and D is copied from the source.
 *
 * Follow-ups:
 *   1. Avoid the linear scan through target children for every source child?
 *      Build a key-to-child map for each target sibling list before processing that parent.
 *   2. Return a new merged tree without mutating either input?
 *      Deep-copy the target first, then merge the source into that copy.
 *   3. Merge many source trees at once?
 *      Aggregate children by key at each level, summing values before recursing or pushing work.
 *   4. Handle duplicate keys among siblings?
 *      Define a conflict rule first, such as grouping duplicates by key or switching to position-based merge.
 *
 * Related: Merge Two Binary Trees (617), Clone N-ary Tree.
 */
public class MergeTwoTrees {

    /**
     * Intuition: a recursive merge would be natural: merge the two roots, then
     * recursively merge children that share a key. This code uses explicit stacks
     * to simulate that DFS. The two stack tops represent the same logical position:
     * a source node and the target node receiving its contribution. For each source
     * child, the code scans target children for the same key, adds values on a
     * match, or creates a missing child before descending.
     *
     * Algorithm:
     *   1. Return early if target or source is null.
     *   2. Push source and target roots onto parallel stacks.
     *   3. For each source child, linearly scan target children for a matching key.
     *   4. Sum a matching target child or create a new target child, then push the pair.
     *   5. Pop when a source node has no more children, then add the source root value.
     *
     * Time:  O(s*c) - each source node is processed once and may scan c target siblings.
     * Space: O(h) - the explicit DFS stacks grow with the source tree height.
     *
     * @param target target tree that is updated in place
     * @param source source tree whose values and missing branches are merged into target
     */
    public void merge(TreeNode target, TreeNode source) {
        if (target == null || source == null) {
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
     * Tracks which child index is next for one source node during iterative DFS.
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
     * N-ary tree node with key, value, and insertion-ordered children.
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

    public static void main(String[] args) {
        MergeTwoTrees solver = new MergeTwoTrees();

        TreeNode target = new TreeNode(10, "A");
        target.children.add(new TreeNode(1, "B"));
        target.children.add(new TreeNode(2, "C"));
        TreeNode source = new TreeNode(5, "A");
        source.children.add(new TreeNode(4, "B"));
        source.children.add(new TreeNode(7, "D"));
        solver.merge(target, source);
        String firstOutput = target.key + ":" + target.value + "["
            + target.children.get(0).key + ":" + target.children.get(0).value + ","
            + target.children.get(1).key + ":" + target.children.get(1).value + ","
            + target.children.get(2).key + ":" + target.children.get(2).value + "]";

        TreeNode nullSourceTarget = new TreeNode(2, "R");
        solver.merge(nullSourceTarget, null);
        String secondOutput = nullSourceTarget.key + ":" + nullSourceTarget.value;

        TreeNode leafTarget = new TreeNode(2, "R");
        TreeNode leafSource = new TreeNode(3, "R");
        solver.merge(leafTarget, leafSource);
        String thirdOutput = leafTarget.key + ":" + leafTarget.value;

        String[] inputs = {
            "target=A:10[B:1,C:2], source=A:5[B:4,D:7]",
            "target=R:2, source=null",
            "target=R:2, source=R:3"
        };
        String[] outputs = {firstOutput, secondOutput, thirdOutput};
        String[] expected = {"A:15[B:5,C:2,D:7]", "R:2", "R:5"};

        for (int i = 0; i < inputs.length; i++) {
            System.out.printf("%s -> %s  expected=%s%n", inputs[i], outputs[i], expected[i]);
        }
    }
}
