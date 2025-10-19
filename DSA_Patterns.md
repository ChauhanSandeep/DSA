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

## 19. Micro-Patterns

These are **tactical, reusable coding patterns** that appear across multiple DSA problems. Unlike broad algorithm categories, micro-patterns are **small tricks and optimizations** that can be applied in various contexts. They represent **FAANG-level insights** that elevate solutions from good to exceptional.

---

### Micro-Pattern: Index-as-Hash (Negative Marking)

**Problem Context:** Finding duplicates, missing numbers, or marking presence in arrays where `1 ≤ nums[i] ≤ n` and array modification is allowed.

**Key Idea:** Use array indices as hash keys by negating values at `nums[i] - 1` to mark that number `i` has been seen, achieving O(1) space without extra data structures.

**LeetCode Problems:**
- [442. Find All Duplicates in an Array](https://leetcode.com/problems/find-all-duplicates-in-an-array/)
- [448. Find All Numbers Disappeared in an Array](https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/)
- [41. First Missing Positive](https://leetcode.com/problems/first-missing-positive/)
- [287. Find the Duplicate Number](https://leetcode.com/problems/find-the-duplicate-number/) (with modification)

**Code Snippet:**
```java
// Mark presence by negating value at index
for (int i = 0; i < nums.length; i++) {
    int index = Math.abs(nums[i]) - 1;
    if (nums[index] < 0) {
        // Already visited - duplicate found
        result.add(Math.abs(nums[i]));
    } else {
        nums[index] = -nums[index];  // Mark as visited
    }
}
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** Works only when values are in range [1, n]. Requires array modification. Related to cyclic sort pattern. Use `Math.abs()` when reading marked values.

---

### Micro-Pattern: Prefix Sum with HashMap

**Problem Context:** Finding subarrays with specific sum properties (zero sum, sum equals k, subarray divisibility).

**Key Idea:** Store cumulative sums in a HashMap; if the same prefix sum appears twice, the subarray between those indices has sum zero (or use `prefixSum - target` for sum k problems).

**LeetCode Problems:**
- [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)
- [523. Continuous Subarray Sum](https://leetcode.com/problems/continuous-subarray-sum/)
- [525. Contiguous Array](https://leetcode.com/problems/contiguous-array/)
- [974. Subarray Sums Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/)

**Code Snippet:**
```java
Map<Integer, Integer> prefixSumMap = new HashMap<>();
prefixSumMap.put(0, -1);  // Handle subarrays starting at index 0
int runningSum = 0;

for (int i = 0; i < nums.length; i++) {
    runningSum += nums[i];
    if (prefixSumMap.containsKey(runningSum - target)) {
        int startIdx = prefixSumMap.get(runningSum - target);
        // Found subarray from startIdx+1 to i
    }
    prefixSumMap.putIfAbsent(runningSum, i);
}
```

**Complexity:** Time - O(n), Space - O(n)

**Notes:** Initialize with `(0, -1)` to handle edge cases. For "first occurrence" problems, use `putIfAbsent()`. For count problems, use `getOrDefault()` with frequency tracking.

---

### Micro-Pattern: XOR for Bit-Level Prefix Optimization

**Problem Context:** Finding maximum XOR pairs, detecting single numbers, or bit manipulation optimization problems.

**Key Idea:** Build XOR results bit-by-bit from MSB to LSB, using HashSet to check if complementary prefixes exist that can set each bit greedily.

**LeetCode Problems:**
- [421. Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/)
- [1707. Maximum XOR With an Element From Array](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/)
- [1938. Maximum Genetic Difference Query](https://leetcode.com/problems/maximum-genetic-difference-query/)

**Code Snippet:**
```java
int maxXor = 0, mask = 0;
for (int i = 31; i >= 0; i--) {
    mask |= (1 << i);
    Set<Integer> prefixes = new HashSet<>();
    for (int num : arr) {
        prefixes.add(num & mask);
    }
    int candidate = maxXor | (1 << i);
    for (int prefix : prefixes) {
        if (prefixes.contains(candidate ^ prefix)) {
            maxXor = candidate;
            break;
        }
    }
}
```

**Complexity:** Time - O(n × 32) = O(n), Space - O(n)

**Notes:** Greedy bit-setting from MSB ensures maximum value. XOR property: `a ^ b = c ⟹ a ^ c = b`. Works for finding max XOR of any two numbers efficiently.

---

### Micro-Pattern: Monotonic Stack for Next Greater/Smaller

**Problem Context:** Finding next greater/smaller element, stock span, daily temperatures, histogram problems.

**Key Idea:** Maintain a monotonic stack (increasing/decreasing) and pop elements when a larger/smaller element is found, resolving their "next greater/smaller" in one pass.

**LeetCode Problems:**
- [496. Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/)
- [503. Next Greater Element II](https://leetcode.com/problems/next-greater-element-ii/)
- [739. Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)
- [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)
- [901. Online Stock Span](https://leetcode.com/problems/online-stock-span/)

**Code Snippet:**
```java
Stack<Integer> stack = new Stack<>();  // Store indices
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
        int prevIdx = stack.pop();
        result[prevIdx] = nums[i];  // Found next greater
    }
    stack.push(i);
}
```

**Complexity:** Time - O(n), Space - O(n)

**Notes:** Each element pushed/popped exactly once. Store indices (not values) for distance calculations. Use decreasing stack for "next greater", increasing for "next smaller". Right-to-left traversal finds "previous" elements instead.

---

### Micro-Pattern: Floyd's Cycle Detection as Array Index Following

**Problem Context:** Finding duplicates in arrays where values point to indices, or detecting cycles in functional graphs.

**Key Idea:** Treat array as linked list where `next = nums[current]`, then apply two-pointer cycle detection to find the cycle entrance (the duplicate).

**LeetCode Problems:**
- [287. Find the Duplicate Number](https://leetcode.com/problems/find-the-duplicate-number/)
- [142. Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/)
- [202. Happy Number](https://leetcode.com/problems/happy-number/)

**Code Snippet:**
```java
// Phase 1: Detect cycle
int slow = nums[0], fast = nums[0];
do {
    slow = nums[slow];
    fast = nums[nums[fast]];
} while (slow != fast);

// Phase 2: Find cycle entrance
fast = nums[0];
while (slow != fast) {
    slow = nums[slow];
    fast = nums[fast];
}
return slow;  // The duplicate number
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** Works when array represents a functional graph with exactly one cycle. Phase 1 finds meeting point inside cycle; phase 2 finds entrance. Read-only, constant space solution for duplicate detection.

---

### Micro-Pattern: Linked List Interweaving for O(1) Space Cloning

**Problem Context:** Deep copying linked lists with random pointers or complex references without extra space.

**Key Idea:** Interweave copied nodes with original nodes (original→copy→original→copy), set pointers using relationships, then separate lists.

**LeetCode Problems:**
- [138. Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/)
- [133. Clone Graph](https://leetcode.com/problems/clone-graph/) (uses similar HashMap concept)

**Code Snippet:**
```java
// Step 1: Interweave - create copies next to originals
Node curr = head;
while (curr != null) {
    Node copy = new Node(curr.val);
    copy.next = curr.next;
    curr.next = copy;
    curr = copy.next;
}

// Step 2: Set random pointers for copies
curr = head;
while (curr != null) {
    if (curr.random != null) {
        curr.next.random = curr.random.next;
    }
    curr = curr.next.next;
}
```

**Complexity:** Time - O(n), Space - O(1) excluding result

**Notes:** Alternative to HashMap approach. Three-pass algorithm. Preserves original list if needed. Key insight: `original.random.next` gives copied random node.

---

### Micro-Pattern: Kadane's Running Maximum with Reset

**Problem Context:** Maximum subarray sum, maximum product subarray, or any contiguous sequence optimization.

**Key Idea:** Track running sum/product; if it becomes unfavorable (negative for sum, < 1 for product), reset to current element as new potential starting point.

**LeetCode Problems:**
- [53. Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)
- [152. Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)
- [918. Maximum Sum Circular Subarray](https://leetcode.com/problems/maximum-sum-circular-subarray/)
- [1749. Maximum Absolute Sum of Any Subarray](https://leetcode.com/problems/maximum-absolute-sum-of-any-subarray/)

**Code Snippet:**
```java
int maxSum = Integer.MIN_VALUE;
int currentSum = 0;

for (int num : nums) {
    currentSum += num;
    maxSum = Math.max(maxSum, currentSum);
    
    if (currentSum < 0) {
        currentSum = 0;  // Reset - start fresh
    }
}
return maxSum;
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** Works because negative prefix can never help future sums. For maximum product, track both max and min (negative × negative = positive). To return actual subarray, track start/end indices during updates.

---

### Micro-Pattern: Two-Variable Space Optimization in DP

**Problem Context:** Dynamic programming problems where current state depends only on last 1-2 states (Fibonacci, House Robber, Climbing Stairs).

**Key Idea:** Instead of maintaining full DP array, keep only the last two values needed, reducing space from O(n) to O(1).

**LeetCode Problems:**
- [198. House Robber](https://leetcode.com/problems/house-robber/)
- [213. House Robber II](https://leetcode.com/problems/house-robber-ii/)
- [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
- [509. Fibonacci Number](https://leetcode.com/problems/fibonacci-number/)
- [1137. N-th Tribonacci Number](https://leetcode.com/problems/n-th-tribonacci-number/)

**Code Snippet:**
```java
int prev2 = nums[0];           // dp[i-2]
int prev1 = Math.max(nums[0], nums[1]);  // dp[i-1]

for (int i = 2; i < nums.length; i++) {
    int current = Math.max(prev1, prev2 + nums[i]);
    prev2 = prev1;
    prev1 = current;
}
return prev1;
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** Common in linear DP with recurrence `dp[i] = f(dp[i-1], dp[i-2])`. Rolling variable pattern. If you need backtracking to find solution path, must keep full array. Works for problems with no inter-dependency beyond immediate neighbors.

---

### Micro-Pattern: Greedy Bit Manipulation with Carry Simulation

**Problem Context:** Adding numbers without `+` operator, bit counting, or implementing arithmetic operations.

**Key Idea:** XOR gives sum without carry, AND shifted left gives carry; iterate until no carry remains.

**LeetCode Problems:**
- [371. Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/)
- [67. Add Binary](https://leetcode.com/problems/add-binary/)
- [989. Add to Array-Form of Integer](https://leetcode.com/problems/add-to-array-form-of-integer/)

**Code Snippet:**
```java
while (b != 0) {
    int carry = a & b;      // Calculate carry bits
    a = a ^ b;              // Sum without carry
    b = carry << 1;         // Shift carry left
}
return a;
```

**Complexity:** Time - O(1) for fixed-size integers, Space - O(1)

**Notes:** XOR handles addition bit-by-bit without carry. AND identifies where both bits are 1 (carry needed). Shift carry left for next position. Works for both positive and negative numbers in two's complement.

---

### Micro-Pattern: Sliding Window Maximum with Deque

**Problem Context:** Finding maximum/minimum in all windows of size k, maintaining order in dynamic range queries.

**Key Idea:** Use monotonic deque to maintain potential maximums in decreasing order; remove from front when outside window, from back when smaller than current.

**LeetCode Problems:**
- [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
- [1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit](https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/)
- [862. Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/)

**Code Snippet:**
```java
Deque<Integer> deque = new ArrayDeque<>();  // Stores indices
int[] result = new int[nums.length - k + 1];

for (int i = 0; i < nums.length; i++) {
    // Remove indices outside window
    while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
        deque.pollFirst();
    }
    // Remove smaller elements (won't be max)
    while (!deque.isEmpty() && nums[i] > nums[deque.peekLast()]) {
        deque.pollLast();
    }
    deque.offerLast(i);
    if (i >= k - 1) {
        result[i - k + 1] = nums[deque.peekFirst()];
    }
}
```

**Complexity:** Time - O(n), Space - O(k)

**Notes:** Each element added/removed at most once. Front of deque always has window maximum. Store indices (not values) to check if still in window. Deque maintains decreasing order of values.

---

### Micro-Pattern: Binary Search on Answer Space

**Problem Context:** Optimization problems asking for minimum capacity, maximum minimum, or splitting constraints (Koko Bananas, Ship Packages, Split Array).

**Key Idea:** Binary search not on array indices but on the range of possible answers; use a feasibility function to check if an answer works.

**LeetCode Problems:**
- [1011. Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)
- [410. Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/)
- [875. Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/)
- [1552. Magnetic Force Between Two Balls](https://leetcode.com/problems/magnetic-force-between-two-balls/)
- [1482. Minimum Number of Days to Make m Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)

**Code Snippet:**
```java
int left = maxElement, right = totalSum;

while (left < right) {
    int mid = left + (right - left) / 2;
    
    if (isFeasible(nums, mid, constraints)) {
        right = mid;  // Try smaller answer
    } else {
        left = mid + 1;  // Need larger answer
    }
}

private boolean isFeasible(int[] nums, int capacity, int k) {
    // Check if 'capacity' satisfies problem constraints
}
```

**Complexity:** Time - O(n log(range)), Space - O(1)

**Notes:** Answer space must be monotonic (if X works, all values > X work or vice versa). Feasibility check is problem-specific but typically O(n). Common when problem asks for "minimum of maximum" or "maximum of minimum".

---

### Micro-Pattern: Fast HashMap Initialization with computeIfAbsent

**Problem Context:** Building graphs, grouping anagrams, or any multi-value mapping where we lazily initialize collections.

**Key Idea:** Use `computeIfAbsent` to avoid null checks and make code cleaner when values are collections or complex objects.

**LeetCode Problems:**
- [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/)
- [582. Kill Process](https://leetcode.com/problems/kill-process/)
- [692. Top K Frequent Words](https://leetcode.com/problems/top-k-frequent-words/)
- [1152. Analyze User Website Visit Pattern](https://leetcode.com/problems/analyze-user-website-visit-pattern/)

**Code Snippet:**
```java
Map<Integer, List<Integer>> graph = new HashMap<>();

// Old verbose way
if (!graph.containsKey(node)) {
    graph.put(node, new ArrayList<>());
}
graph.get(node).add(neighbor);

// Clean Java 8 way
graph.computeIfAbsent(node, k -> new ArrayList<>()).add(neighbor);
```

**Complexity:** Time - O(1) amortized, Space - O(1) per entry

**Notes:** `computeIfAbsent` only creates new value if key absent. Lambda `k -> new ArrayList<>()` creates collection on demand. Works with any collection type. More concise and less error-prone than manual checks.

---

### Micro-Pattern: In-Place Array Reversal with Two Pointers

**Problem Context:** Reversing arrays, strings, or portions of sequences without extra space.

**Key Idea:** Use two pointers (start and end) moving toward center, swapping elements until they meet.

**LeetCode Problems:**
- [189. Rotate Array](https://leetcode.com/problems/rotate-array/)
- [344. Reverse String](https://leetcode.com/problems/reverse-string/)
- [151. Reverse Words in a String](https://leetcode.com/problems/reverse-words-in-a-string/)
- [186. Reverse Words in a String II](https://leetcode.com/problems/reverse-words-in-a-string-ii/)

**Code Snippet:**
```java
private void reverse(int[] arr, int start, int end) {
    while (start < end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}
// Usage: reverse(arr, 0, k-1); reverse(arr, k, n-1); reverse(arr, 0, n-1);
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** Foundation for rotate array in O(1) space (reverse three times). Works with any indexable sequence. For strings, convert to char array first since strings are immutable. Can be combined with other patterns for complex transformations.

---

### Micro-Pattern: Math Trick - Finding Single Element with XOR

**Problem Context:** Finding unique element when all others appear twice, or bit manipulation for cancellation.

**Key Idea:** XOR has properties `a ^ a = 0` and `a ^ 0 = a`, so XORing all elements cancels duplicates leaving only the unique one.

**LeetCode Problems:**
- [136. Single Number](https://leetcode.com/problems/single-number/)
- [137. Single Number II](https://leetcode.com/problems/single-number-ii/)
- [260. Single Number III](https://leetcode.com/problems/single-number-iii/)
- [540. Single Element in a Sorted Array](https://leetcode.com/problems/single-element-in-a-sorted-array/)

**Code Snippet:**
```java
int unique = 0;
for (int num : nums) {
    unique ^= num;
}
return unique;  // All duplicates cancel out
```

**Complexity:** Time - O(n), Space - O(1)

**Notes:** XOR is commutative and associative, so order doesn't matter. Extension: for two unique numbers, use XOR + bit partitioning. Works when all others appear even number of times. Can't directly extend to "appears 3 times" (need different bit counting).

---

### Micro-Pattern: Sentinel Nodes for Edge Case Handling

**Problem Context:** Linked list operations (merge, insert, delete) where head might change.

**Key Idea:** Create a dummy node pointing to head; all operations treat it uniformly, eliminating special cases for head operations.

**LeetCode Problems:**
- [21. Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)
- [83. Remove Duplicates from Sorted List](https://leetcode.com/problems/remove-duplicates-from-sorted-list/)
- [82. Remove Duplicates from Sorted List II](https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/)
- [203. Remove Linked List Elements](https://leetcode.com/problems/remove-linked-list-elements/)
- [86. Partition List](https://leetcode.com/problems/partition-list/)

**Code Snippet:**
```java
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode curr = dummy;

while (curr.next != null) {
    if (shouldDelete(curr.next)) {
        curr.next = curr.next.next;  // Delete
    } else {
        curr = curr.next;
    }
}
return dummy.next;  // New head (might differ from old)
```

**Complexity:** Time - depends on operation, Space - O(1)

**Notes:** Eliminates null checks and head-change logic. After operations, return `dummy.next` as new head. Common in merge sorted lists, remove duplicates, partition list. Also called "dummy head" or "sentinel" pattern.

---

**Repository Structure Note:** This DSA repository organizes code by topic (arrays, trees, graphs, etc.) and by algorithmic technique (backtracking, dynamic programming, etc.). Each Java file includes detailed comments explaining the approach, complexity, and follow-up questions to deepen understanding.
