# DSA Problem-Solving Patterns

This document summarizes common Data Structures and Algorithms (DSA) patterns used throughout this repository. Each pattern includes a description, usage criteria, example problems, code snippets from actual solutions, and complexity analysis.

---

## 1. Sliding Window

**When to use:** When you need to find a subarray or substring that satisfies a condition (like sum, distinct elements, or max/min length). Useful for contiguous sequence problems where you need to track a window of elements.

**Example problems:**
- Sliding Window Maximum
- Longest Substring Without Repeating Characters
- Maximum Sum Subarray of Size K
- Minimum Window Substring

**Code snippet:**
```java
int left = 0;
for (int right = 0; right < nums.length; right++) {
    // Expand window by adding right element
    while (!indexDeque.isEmpty() && indexDeque.peekFirst() < right - (k - 1)) {
        indexDeque.pollFirst();  // Remove elements outside window
    }
    // Process current window
    if (right >= k - 1) {
        result[right - (k - 1)] = nums[indexDeque.peekFirst()];
    }
}
```

**Complexity:** Time - O(n), Space - O(k) where k is the window size

---

## 2. Two Pointers

**When to use:** When you need to search pairs in a sorted/unsorted array, or find elements satisfying certain conditions. Often used with sorted arrays to optimize from O(n²) to O(n).

**Example problems:**
- Container With Most Water
- Two Sum (sorted array)
- Remove Duplicates from Sorted Array
- Trapping Rain Water

**Code snippet:**
```java
int left = 0;
int right = heights.length - 1;
int maxWater = 0;

while (left < right) {
    int minHeight = Math.min(heights[left], heights[right]);
    int width = right - left;
    int currentArea = minHeight * width;
    maxWater = Math.max(maxWater, currentArea);
    
    // Move pointer with smaller height
    if (heights[left] < heights[right]) {
        left++;
    } else {
        right--;
    }
}
```

**Complexity:** Time - O(n), Space - O(1)

---

## 3. Binary Search

**When to use:** When searching in a sorted array or when you can define a monotonic search space where you can eliminate half the candidates in each iteration.

**Example problems:**
- Search in Rotated Sorted Array
- Find Peak Element
- Find Minimum in Rotated Sorted Array
- Single Element in a Sorted Array

**Code snippet:**
```java
int left = 0, right = nums.length - 1;

while (left < right) {
    int mid = left + (right - left) / 2;
    
    if (nums[mid] > nums[right]) {
        left = mid + 1;  // Minimum is in right half
    } else {
        right = mid;  // Minimum could be mid or in left half
    }
}
return nums[left];
```

**Complexity:** Time - O(log n), Space - O(1)

---

## 4. Binary Search on Answer

**When to use:** When you need to find an optimal value (minimum or maximum) that satisfies certain conditions. The search space is the range of possible answers, not array indices.

**Example problems:**
- Capacity To Ship Packages Within D Days
- Split Array Largest Sum
- Koko Eating Bananas
- Minimum Limit of Balls in a Bag

**Code snippet:**
```java
int left = maxWeight, right = totalWeight;

// Binary search on capacity
while (left < right) {
    int mid = left + (right - left) / 2;
    
    if (canShipWithCapacity(weights, D, mid)) {
        right = mid;  // Try smaller capacity
    } else {
        left = mid + 1;  // Need larger capacity
    }
}

private boolean canShipWithCapacity(int[] weights, int D, int capacity) {
    int daysNeeded = 1;
    int currentLoad = 0;
    for (int weight : weights) {
        if (currentLoad + weight > capacity) {
            daysNeeded++;
            currentLoad = weight;
            if (daysNeeded > D) return false;
        } else {
            currentLoad += weight;
        }
    }
    return true;
}
```

**Complexity:** Time - O(n log(sum)), Space - O(1)

---

## 5. Backtracking

**When to use:** When you need to explore all possible solutions by making choices, then undoing them if they don't lead to a valid solution. Common in permutations, combinations, and constraint satisfaction problems.

**Example problems:**
- N-Queens
- Permutations
- Combinations
- Subset Generation
- Word Search

**Code snippet:**
```java
private void backtrack(int row, int n, char[][] board, 
                      Set<Integer> columns, Set<Integer> diagonals, 
                      Set<Integer> antiDiagonals, List<List<String>> solutions) {
    // Base case: All queens placed
    if (row == n) {
        solutions.add(convertBoardToList(board));
        return;
    }
    
    for (int col = 0; col < n; col++) {
        int diagonal = row - col;
        int antiDiagonal = row + col;
        
        if (columns.contains(col) || diagonals.contains(diagonal) 
            || antiDiagonals.contains(antiDiagonal)) continue;
        
        // Make choice
        board[row][col] = 'Q';
        columns.add(col);
        diagonals.add(diagonal);
        antiDiagonals.add(antiDiagonal);
        
        // Recurse
        backtrack(row + 1, n, board, columns, diagonals, antiDiagonals, solutions);
        
        // Undo choice (backtrack)
        board[row][col] = '.';
        columns.remove(col);
        diagonals.remove(diagonal);
        antiDiagonals.remove(antiDiagonal);
    }
}
```

**Complexity:** Time - O(N!) for N-Queens, varies by problem; Space - O(N) for recursion stack

---

## 6. Dynamic Programming - Tabulation

**When to use:** When a problem has overlapping subproblems and optimal substructure. Build solutions bottom-up using a table to store intermediate results.

**Example problems:**
- Climbing Stairs
- Coin Change
- Longest Common Subsequence
- 0/1 Knapsack
- House Robber

**Code snippet:**
```java
// Climbing Stairs example with space optimization
public int climbStairs(int totalSteps) {
    if (totalSteps <= 1) return 1;
    
    int waysToPreviousStep = 2;  // ways to reach step 2
    int waysToTwoStepsBack = 1;  // ways to reach step 1
    
    for (int currentStep = 3; currentStep <= totalSteps; currentStep++) {
        int currentWays = waysToPreviousStep + waysToTwoStepsBack;
        waysToTwoStepsBack = waysToPreviousStep;
        waysToPreviousStep = currentWays;
    }
    
    return waysToPreviousStep;
}
```

**Complexity:** Time - O(n), Space - O(1) with space optimization, O(n) with DP array

---

## 7. Dynamic Programming - Memoization

**When to use:** Top-down approach to DP where you recursively solve subproblems and cache results to avoid recomputation.

**Example problems:**
- Fibonacci Numbers
- Decode Ways
- Burst Balloons
- Regular Expression Matching

**Code snippet:**
```java
public int climbStairsMemoization(int totalSteps) {
    int[] memo = new int[totalSteps + 1];
    return climbStairsHelper(totalSteps, memo);
}

private int climbStairsHelper(int remainingSteps, int[] memo) {
    // Base cases
    if (remainingSteps <= 1) return 1;
    
    // Return cached result if available
    if (memo[remainingSteps] > 0) {
        return memo[remainingSteps];
    }
    
    // Compute and cache
    memo[remainingSteps] = climbStairsHelper(remainingSteps - 1, memo) +
                          climbStairsHelper(remainingSteps - 2, memo);
    
    return memo[remainingSteps];
}
```

**Complexity:** Time - O(n), Space - O(n) for memoization table + recursion stack

---

## 8. Greedy Algorithm

**When to use:** When making locally optimal choices leads to a globally optimal solution. Often involves sorting and making the best choice at each step.

**Example problems:**
- Candy Distribution
- Jump Game
- Meeting Rooms
- Activity Selection

**Code snippet:**
```java
// Candy distribution - Two-pass greedy approach
public int candy(int[] ratings) {
    int n = ratings.length;
    if (n <= 1) return n;
    
    int[] candies = new int[n];
    Arrays.fill(candies, 1);
    
    // Left to right pass
    for (int i = 1; i < n; i++) {
        if (ratings[i] > ratings[i - 1]) {
            candies[i] = candies[i - 1] + 1;
        }
    }
    
    // Right to left pass
    for (int i = n - 2; i >= 0; i--) {
        if (ratings[i] > ratings[i + 1]) {
            candies[i] = Math.max(candies[i], candies[i + 1] + 1);
        }
    }
    
    return Arrays.stream(candies).sum();
}
```

**Complexity:** Time - O(n), Space - O(n)

---

## 9. Heap / Priority Queue

**When to use:** When you need to repeatedly find minimum/maximum elements, or maintain a sorted order dynamically as elements are added/removed.

**Example problems:**
- Kth Largest Element
- Merge K Sorted Lists
- Top K Frequent Elements
- Meeting Rooms II

**Code snippet:**
```java
// Kth Largest using Min-Heap
public int findKthLargestUsingMinHeap(int[] inputArray, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    
    for (int num : inputArray) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll();  // Remove smallest element
        }
    }
    
    return minHeap.peek();  // Root is kth largest
}
```

**Complexity:** Time - O(n log k), Space - O(k)

---

## 10. Graph Traversal - BFS

**When to use:** When you need to explore a graph level by level, find shortest paths in unweighted graphs, or process nodes by distance from source.

**Example problems:**
- Binary Tree Level Order Traversal
- Shortest Path in Binary Matrix
- Word Ladder
- Rotting Oranges

**Code snippet:**
```java
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    
    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> currentLevel = new ArrayList<>();
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);
            
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(currentLevel);
    }
    
    return result;
}
```

**Complexity:** Time - O(V + E), Space - O(w) where w is maximum width

---

## 11. Graph Traversal - DFS

**When to use:** When you need to explore all paths, detect cycles, or traverse to maximum depth. Uses recursion or explicit stack.

**Example problems:**
- Number of Islands
- Path Sum
- Clone Graph
- Course Schedule

**Code snippet:**
```java
// Recursive DFS
public void dfs(int node, boolean[] visited, List<List<Integer>> graph) {
    visited[node] = true;
    
    for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
            dfs(neighbor, visited, graph);
        }
    }
}

// Iterative DFS using stack
public void dfsIterative(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Stack<Integer> stack = new Stack<>();
    stack.push(start);
    
    while (!stack.isEmpty()) {
        int node = stack.pop();
        if (!visited[node]) {
            visited[node] = true;
            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                }
            }
        }
    }
}
```

**Complexity:** Time - O(V + E), Space - O(V) for recursion stack or explicit stack

---

## 12. Union-Find (Disjoint Set)

**When to use:** When you need to group elements into disjoint sets and efficiently check connectivity or merge sets. Common in graph connectivity problems.

**Example problems:**
- Number of Connected Components
- Redundant Connection
- Accounts Merge
- Minimum Spanning Tree (Kruskal's)

**Code snippet:**
```java
public class DisjointSet {
    private int[] parent;
    private int[] rank;
    
    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    // Find with path compression
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    // Union by rank
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) return;
        
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
}
```

**Complexity:** Time - O(α(n)) amortized per operation (nearly constant), Space - O(n)

---

## 13. Monotonic Stack

**When to use:** When you need to find the next greater/smaller element or maintain elements in monotonic order for range queries.

**Example problems:**
- Daily Temperatures
- Next Greater Element
- Largest Rectangle in Histogram
- Trapping Rain Water

**Code snippet:**
```java
public int[] dailyTemperatures(int[] temperatures) {
    int n = temperatures.length;
    int[] result = new int[n];
    Stack<Integer> stack = new Stack<>();  // Store indices
    
    for (int i = 0; i < n; i++) {
        // Pop while current temperature is warmer
        while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
            int prevIndex = stack.pop();
            result[prevIndex] = i - prevIndex;
        }
        stack.push(i);
    }
    
    return result;
}
```

**Complexity:** Time - O(n), Space - O(n)

---

## 14. Fast & Slow Pointers (Floyd's Cycle Detection)

**When to use:** When you need to detect cycles in linked lists or find the middle element. Also known as the "tortoise and hare" algorithm.

**Example problems:**
- Linked List Cycle
- Linked List Cycle II
- Find Middle of Linked List
- Happy Number

**Code snippet:**
```java
public ListNode detectCycle(ListNode head) {
    if (head == null || head.next == null) return null;
    
    // Phase 1: Detect cycle
    ListNode slow = head;
    ListNode fast = head;
    
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) break;
    }
    
    if (fast == null || fast.next == null) return null;
    
    // Phase 2: Find cycle start
    slow = head;
    while (slow != fast) {
        slow = slow.next;
        fast = fast.next;
    }
    
    return slow;
}
```

**Complexity:** Time - O(n), Space - O(1)

---

## 15. Bit Manipulation

**When to use:** When you need to perform operations at the bit level for optimization or when dealing with subsets, masks, or XOR properties.

**Example problems:**
- Single Number
- Power of Two
- Counting Bits
- Maximum XOR of Two Numbers

**Code snippet:**
```java
public int findMaximumXor(int[] arr) {
    int maxXor = 0;
    int mask = 0;
    
    // Process from most significant bit to least
    for (int i = 31; i >= 0; i--) {
        mask = mask | (1 << i);
        HashSet<Integer> prefixes = new HashSet<>();
        
        // Store all prefixes at current bit level
        for (int num : arr) {
            prefixes.add(num & mask);
        }
        
        // Try to set current bit in result
        int newMax = maxXor | (1 << i);
        for (int prefix : prefixes) {
            // Check if complement exists
            if (prefixes.contains(prefix ^ newMax)) {
                maxXor = newMax;
                break;
            }
        }
    }
    
    return maxXor;
}
```

**Complexity:** Time - O(n × log M) where M is max value, Space - O(n)

---

## 16. Segment Tree

**When to use:** When you need to perform range queries and updates efficiently on an array, such as range sum, range minimum/maximum.

**Example problems:**
- Range Sum Query - Mutable
- My Calendar III
- The Skyline Problem

**Code snippet:**
```java
class SegmentTree {
    private int[] tree;
    private int n;
    
    public SegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }
    
    private void build(int[] nums, int node, int start, int end) {
        if (start == end) {
            tree[node] = nums[start];
        } else {
            int mid = start + (end - start) / 2;
            build(nums, 2 * node + 1, start, mid);
            build(nums, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    public int query(int left, int right) {
        return queryUtil(0, 0, n - 1, left, right);
    }
    
    private int queryUtil(int node, int start, int end, int l, int r) {
        if (r < start || l > end) return 0;
        if (l <= start && end <= r) return tree[node];
        
        int mid = start + (end - start) / 2;
        return queryUtil(2 * node + 1, start, mid, l, r) +
               queryUtil(2 * node + 2, mid + 1, end, l, r);
    }
}
```

**Complexity:** Build - O(n), Query - O(log n), Update - O(log n); Space - O(n)

---

## 17. Dijkstra's Algorithm

**When to use:** When you need to find the shortest path in a weighted graph with non-negative edge weights.

**Example problems:**
- Network Delay Time
- Path With Minimum Effort
- Cheapest Flights Within K Stops

**Code snippet:**
```java
public int networkDelayTime(int[][] times, int n, int k) {
    // Build adjacency list
    Map<Integer, List<int[]>> graph = new HashMap<>();
    for (int[] time : times) {
        graph.computeIfAbsent(time[0], x -> new ArrayList<>())
             .add(new int[]{time[1], time[2]});
    }
    
    // Priority queue: [distance, node]
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, k});
    
    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;
    
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], node = curr[1];
        
        if (d > dist[node]) continue;
        
        if (graph.containsKey(node)) {
            for (int[] next : graph.get(node)) {
                int neighbor = next[0], weight = next[1];
                int newDist = d + weight;
                
                if (newDist < dist[neighbor]) {
                    dist[neighbor] = newDist;
                    pq.offer(new int[]{newDist, neighbor});
                }
            }
        }
    }
    
    int maxDist = 0;
    for (int i = 1; i <= n; i++) {
        if (dist[i] == Integer.MAX_VALUE) return -1;
        maxDist = Math.max(maxDist, dist[i]);
    }
    
    return maxDist;
}
```

**Complexity:** Time - O((V + E) log V) with binary heap, Space - O(V + E)

---

## 18. Topological Sort (Kahn's Algorithm)

**When to use:** When you need to order nodes in a directed acyclic graph (DAG) such that for every edge u → v, u comes before v.

**Example problems:**
- Course Schedule
- Course Schedule II
- Alien Dictionary
- Task Scheduling

**Code snippet:**
```java
public int[] findOrder(int numCourses, int[][] prerequisites) {
    // Build graph and calculate in-degrees
    List<List<Integer>> graph = new ArrayList<>();
    int[] inDegree = new int[numCourses];
    
    for (int i = 0; i < numCourses; i++) {
        graph.add(new ArrayList<>());
    }
    
    for (int[] prereq : prerequisites) {
        graph.get(prereq[1]).add(prereq[0]);
        inDegree[prereq[0]]++;
    }
    
    // Start with nodes having 0 in-degree
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < numCourses; i++) {
        if (inDegree[i] == 0) {
            queue.offer(i);
        }
    }
    
    int[] result = new int[numCourses];
    int index = 0;
    
    while (!queue.isEmpty()) {
        int course = queue.poll();
        result[index++] = course;
        
        for (int next : graph.get(course)) {
            inDegree[next]--;
            if (inDegree[next] == 0) {
                queue.offer(next);
            }
        }
    }
    
    return index == numCourses ? result : new int[0];
}
```

**Complexity:** Time - O(V + E), Space - O(V + E)

---

## Pattern Selection Guide

### Problem Type → Pattern Mapping

| Problem Type | Recommended Pattern |
|-------------|-------------------|
| Contiguous subarray/substring | Sliding Window |
| Sorted array search | Binary Search, Two Pointers |
| Optimization with constraints | Binary Search on Answer, DP, Greedy |
| All combinations/permutations | Backtracking, Recursion |
| Overlapping subproblems | Dynamic Programming |
| Graph connectivity | Union-Find, DFS, BFS |
| Shortest path (weighted) | Dijkstra, Bellman-Ford |
| Shortest path (unweighted) | BFS |
| Next greater/smaller | Monotonic Stack |
| K largest/smallest | Heap |
| Cycle detection | Fast & Slow Pointers, DFS |
| Range queries | Segment Tree, Prefix Sum |
| Bit-level operations | Bit Manipulation |
| Task scheduling | Topological Sort, Greedy |

---

## Additional Tips

1. **Identify the pattern early**: Look for keywords like "subarray", "sorted", "all combinations", "shortest path", etc.
2. **Consider constraints**: Time/space constraints often hint at the required pattern.
3. **Start with brute force**: Understanding the naive solution helps identify optimizations.
4. **Look for properties**: Monotonicity, optimal substructure, greedy choice property.
5. **Practice pattern recognition**: The more problems you solve, the faster you'll recognize patterns.

---

**Repository Structure Note:** This DSA repository organizes code by topic (arrays, trees, graphs, etc.) and by algorithmic technique (backtracking, dynamic programming, etc.). Each Java file includes detailed comments explaining the approach, complexity, and follow-up questions to deepen understanding.
