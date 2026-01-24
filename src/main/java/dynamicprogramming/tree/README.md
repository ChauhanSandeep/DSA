# Tree Pattern

## Pattern Overview

The **Tree** pattern applies dynamic programming to tree structures. Unlike linear DP, tree DP involves bottom-up computation from leaves to root or top-down with memoization, considering subtree solutions.

## Core Characteristics

- **Tree Structure**: Problems involve binary trees or general trees
- **Recursive Nature**: Solutions depend on subtree solutions
- **DFS Traversal**: Use post-order (bottom-up) or with memoization
- **State per Node**: Each node has associated DP state(s)

## When to Recognize Tree DP Pattern

Look for these indicators:
1. Problem involves a **tree structure**
2. Need to compute values **recursively** from children to parent
3. **Optimization** over subtrees (max, min, count)
4. Decision at each node affects subtree solutions

Example phrases: "binary tree", "subtree", "path in tree", "house robber on tree", "tree diameter"

## Generic Steps to Solve Tree DP Problems

### Step 1: Identify What to Track at Each Node
**Question**: What information do we need from each node?
- Maximum/minimum value in subtree
- Count of valid paths
- States (selected/not selected, used/not used)

### Step 2: Define DP State
```
For each node:
- dp[node][state] = optimal value for subtree rooted at node in given state

Examples:
- dp[node][SELECTED] = max value when node is selected
- dp[node][NOT_SELECTED] = max value when node is not selected
- dp[node] = maximum path sum in subtree rooted at node
```

### Step 3: Establish Base Cases
```java
// Leaf nodes or null nodes
if (node == null) {
    return base_value;  // 0, -infinity, etc.
}

// For leaf nodes
if (node.left == null && node.right == null) {
    return compute_leaf_value(node);
}
```

### Step 4: Derive Recurrence Relation

**General Pattern** (Post-Order DFS):
```java
int dfs(TreeNode node) {
    // Base case
    if (node == null) return baseValue;
    
    // Recursively solve for children (post-order)
    int leftResult = dfs(node.left);
    int rightResult = dfs(node.right);
    
    // Compute result for current node using children's results
    int result = combineResults(node.val, leftResult, rightResult);
    
    return result;
}
```

## Common Problem Templates

### Template 1: House Robber III (State Machine on Tree)

**Problem**: Max money robbing houses in binary tree, can't rob adjacent nodes.

```java
class Solution {
    public int rob(TreeNode root) {
        int[] result = robHelper(root);
        return Math.max(result[0], result[1]);
    }
    
    // Returns [robRoot, notRobRoot]
    private int[] robHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // If we rob current node, can't rob children
        int robRoot = node.val + left[1] + right[1];
        
        // If we don't rob current, take max from children
        int notRobRoot = Math.max(left[0], left[1]) + 
                        Math.max(right[0], right[1]);
        
        return new int[]{robRoot, notRobRoot};
    }
}
```

**Key Insight**: Each node has two states: robbed or not robbed. Return both from each subtree.

### Template 2: Binary Tree Maximum Path Sum

**Problem**: Find path with maximum sum (can start/end at any node).

```java
class Solution {
    private int maxSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }
    
    // Returns max gain if path goes through node and continues upward
    private int maxGain(TreeNode node) {
        if (node == null) return 0;
        
        // Max gain from left and right subtrees (ignore negative)
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        // Path through current node (left + node + right)
        int pathSum = node.val + leftGain + rightGain;
        maxSum = Math.max(maxSum, pathSum);
        
        // Return max gain if path continues upward through node
        return node.val + Math.max(leftGain, rightGain);
    }
}
```

**Key Insight**: At each node, consider:
1. Path through node (left + node + right) - update global max
2. Path continuing upward (node + max(left, right)) - return to parent

### Template 3: Diameter/Longest Path in Tree

**Problem**: Longest path between any two nodes.

```java
class Solution {
    private int diameter = 0;
    
    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return diameter;
    }
    
    private int height(TreeNode node) {
        if (node == null) return 0;
        
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        
        // Path through current node
        diameter = Math.max(diameter, leftHeight + rightHeight);
        
        // Return height of subtree
        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

### Template 4: Count Nodes with Specific Property

**Problem**: Count nodes where subtree satisfies a property.

```java
class Solution {
    private int count = 0;
    
    public int countNodes(TreeNode root) {
        dfs(root);
        return count;
    }
    
    // Returns [minVal, maxVal] in subtree
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
        }
        
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // Check if current subtree satisfies property
        if (satisfiesProperty(node, left, right)) {
            count++;
        }
        
        // Return info for parent
        int minVal = Math.min(node.val, Math.min(left[0], right[0]));
        int maxVal = Math.max(node.val, Math.max(left[1], right[1]));
        
        return new int[]{minVal, maxVal};
    }
}
```

### Template 5: DP with Multiple States per Node

**Problem**: Track multiple states per node (e.g., with camera, without camera, covered).

```java
class Solution {
    private static final int NEED_CAMERA = 0;
    private static final int HAS_CAMERA = 1;
    private static final int COVERED = 2;
    
    private int cameras = 0;
    
    public int minCameraCover(TreeNode root) {
        int state = dfs(root);
        return cameras + (state == NEED_CAMERA ? 1 : 0);
    }
    
    private int dfs(TreeNode node) {
        if (node == null) return COVERED;
        
        int left = dfs(node.left);
        int right = dfs(node.right);
        
        // If any child needs camera, place camera here
        if (left == NEED_CAMERA || right == NEED_CAMERA) {
            cameras++;
            return HAS_CAMERA;
        }
        
        // If any child has camera, current is covered
        if (left == HAS_CAMERA || right == HAS_CAMERA) {
            return COVERED;
        }
        
        // Both children covered but not by camera at children
        // Current node needs camera from parent
        return NEED_CAMERA;
    }
}
```

## Common Patterns and Variations

### 1. **Single Value Return**
Simple cases where we only need one value:
```java
int dfs(TreeNode node) {
    if (node == null) return 0;
    int left = dfs(node.left);
    int right = dfs(node.right);
    return compute(node.val, left, right);
}
```

### 2. **Array/Tuple Return**
When we need multiple values from subtree:
```java
int[] dfs(TreeNode node) {
    // Return multiple values: [state1, state2, ...]
    int[] left = dfs(node.left);
    int[] right = dfs(node.right);
    return new int[]{compute1(...), compute2(...)};
}
```

### 3. **Global Variable Update**
For tracking global optimum:
```java
private int globalMax = Integer.MIN_VALUE;

int dfs(TreeNode node) {
    // Compute local value
    int localValue = ...;
    globalMax = Math.max(globalMax, localValue);
    return valueForParent;
}
```

### 4. **Memoization**
For overlapping subproblems (rare in trees):
```java
Map<TreeNode, Integer> memo = new HashMap<>();

int dfs(TreeNode node) {
    if (memo.containsKey(node)) return memo.get(node);
    // Compute
    int result = ...;
    memo.put(node, result);
    return result;
}
```

## Tree Traversal Approaches

### Post-Order (Bottom-Up)
Most tree DP uses post-order:
```java
void postOrder(TreeNode node) {
    if (node == null) return;
    postOrder(node.left);   // Process left
    postOrder(node.right);  // Process right
    process(node);          // Process current
}
```

### Pre-Order with State Passing
Sometimes pass state down:
```java
void preOrder(TreeNode node, int parentState) {
    if (node == null) return;
    int currentState = compute(node, parentState);
    preOrder(node.left, currentState);
    preOrder(node.right, currentState);
}
```

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(n) where n = number of nodes (visit each node once)
- **Space**: O(h) where h = height of tree (recursion stack)
  - Best case: O(log n) for balanced tree
  - Worst case: O(n) for skewed tree

## Common Mistakes to Avoid

1. **Wrong base case**: null nodes need correct return value
2. **Global vs local**: Distinguish between global max and value returned to parent
3. **Path vs subtree**: Clarify if answer is within subtree or can span across root
4. **Forgetting null checks**: Always check if node is null
5. **Not handling edge cases**: Single node, empty tree

## Problem-Solving Checklist

- [ ] What information do we need from each subtree?
- [ ] Single value or multiple states per node?
- [ ] Define what the DFS function returns
- [ ] Establish base case (null node, leaf node)
- [ ] Use post-order traversal (children before parent)
- [ ] Track global optimum if path can span across root
- [ ] Return value for parent (for bottom-up computation)
- [ ] Handle edge cases (null root, single node)
- [ ] Consider space complexity (recursion depth)

## Related Patterns

- **State Machine**: Tree DP often involves states per node
- **Graph DP**: Generalization to arbitrary graphs (DAGs)
- **DFS/Backtracking**: Tree DP uses DFS traversal

## Practice Problems in this Package

1. **House Robber III** - Rob houses in binary tree with adjacency constraint
