# Tree Pattern

## Core Idea
DP on trees: solve for children first (post-order), then combine results at current node.

## The Pattern (First Principles)

```
Result at node = combine(
    result from left subtree,
    result from right subtree,
    node value
)
```

Use **post-order traversal** (left, right, root).

## Universal Template

```java
public int treeDP(TreeNode root) {
    if (root == null) return baseValue;
    
    // Solve for children first
    int left = treeDP(root.left);
    int right = treeDP(root.right);
    
    // Combine at current node
    int result = combine(left, right, root.val);
    
    return result;
}
```

## Common Patterns

### Single Value (Max Path, Diameter)
```java
int maxPath = Integer.MIN_VALUE;

int dfs(TreeNode node) {
    if (node == null) return 0;
    
    int left = dfs(node.left);
    int right = dfs(node.right);
    
    // Update global (path through node)
    maxPath = max(maxPath, left + right + node.val);
    
    // Return to parent (path continuing upward)
    return node.val + max(left, right);
}
```

### Multiple States (House Robber III)
```java
int[] dfs(TreeNode node) { // returns [rob, notRob]
    if (node == null) return new int[]{0, 0};
    
    int[] left = dfs(node.left);
    int[] right = dfs(node.right);
    
    int rob = node.val + left[1] + right[1];
    int notRob = max(left[0], left[1]) + max(right[0], right[1]);
    
    return new int[]{rob, notRob};
}
```

## Key Insight
Return value ≠ global answer. Return what **parent needs**, update global for **final answer**.

**Time**: O(n) - visit each node once  
**Space**: O(h) - recursion depth
