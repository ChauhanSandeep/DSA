# 🎯 DSA Interview Q&A Guide for FAANG

A comprehensive guide covering essential data structures, algorithms, patterns, and problem-solving approaches for FAANG-level interviews. This document consolidates key concepts from the repository to help you quickly revise and recall critical ideas during technical interviews.

---

## 📋 Table of Contents
1. [Arrays & Two Pointers](#arrays--two-pointers)
2. [Sliding Window](#sliding-window)
3. [Binary Search](#binary-search)
4. [Hashing & HashMaps](#hashing--hashmaps)
5. [Strings](#strings)
6. [Linked Lists](#linked-lists)
7. [Stacks & Queues](#stacks--queues)
8. [Trees & Binary Trees](#trees--binary-trees)
9. [Graphs](#graphs)
10. [Dynamic Programming](#dynamic-programming)
11. [Backtracking](#backtracking)
12. [Heaps & Priority Queues](#heaps--priority-queues)
13. [Greedy Algorithms](#greedy-algorithms)
14. [Bit Manipulation](#bit-manipulation)
15. [Design Patterns](#design-patterns)

---

## Arrays & Two Pointers

### Question: When should I use the two-pointer technique, and what are the common patterns?

**Answer:**

The **two-pointer technique** is optimal when you need to:
- Find pairs/triplets that satisfy a condition (e.g., sum = target)
- Process arrays from both ends simultaneously
- Remove duplicates in-place
- Partition arrays based on conditions

**Common Patterns:**

1. **Opposite Direction (Converging):**
   - Start: `left = 0`, `right = n-1`
   - Move pointers toward each other
   - Use when: Array is sorted or you need to examine pairs

```java
// Two Sum II - Sorted Array
int left = 0, right = nums.length - 1;
while (left < right) {
    int sum = nums[left] + nums[right];
    if (sum == target) return new int[]{left, right};
    else if (sum < target) left++;
    else right--;
}
```

2. **Same Direction (Fast & Slow):**
   - Both start at 0, move at different speeds
   - Use when: Removing duplicates, cycle detection

```java
// Remove Duplicates from Sorted Array
int slow = 0;
for (int fast = 1; fast < nums.length; fast++) {
    if (nums[fast] != nums[slow]) {
        slow++;
        nums[slow] = nums[fast];
    }
}
```

**When to Use:**
- Array is sorted → Consider two pointers
- Need O(1) space for in-place operations
- Looking for pairs/triplets
- Partitioning problems (Dutch National Flag)

**Common Pitfalls:**
- Not handling edge cases (empty array, single element)
- Forgetting to update pointers correctly
- Not considering duplicates when required

**Time Complexity:** O(n) - single pass
**Space Complexity:** O(1) - in-place

**Practice Problems:**
- [Two Sum II - Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)
- [3Sum](https://leetcode.com/problems/3sum/)
- [Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)
- [Container With Most Water](https://leetcode.com/problems/container-with-most-water/)
- [Sort Colors (Dutch National Flag)](https://leetcode.com/problems/sort-colors/)
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)

---

### Question: How do you approach container/trap water problems?

**Answer:**

**Core Insight:** At each position, the water level is determined by the minimum of the maximum heights on both sides.

**Two-Pointer Approach:**
```java
int left = 0, right = n - 1;
int leftMax = 0, rightMax = 0, water = 0;

while (left < right) {
    if (height[left] < height[right]) {
        if (height[left] >= leftMax) {
            leftMax = height[left];
        } else {
            water += leftMax - height[left];
        }
        left++;
    } else {
        if (height[right] >= rightMax) {
            rightMax = height[right];
        } else {
            water += rightMax - height[right];
        }
        right--;
    }
}
```

**Why This Works:**
- Process from both ends
- Move pointer with smaller height (limits water capacity)
- Track max height seen so far from each direction

**Variations:**
- **Container With Most Water:** Area = min(height[i], height[j]) × (j - i)
- **Trapping Rain Water:** Sum water trapped at each position

**Time:** O(n), **Space:** O(1)

**Practice Problems:**
- [Container With Most Water](https://leetcode.com/problems/container-with-most-water/)
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)

---

## Sliding Window

### Question: How do I recognize when to use sliding window, and what are the key patterns?

**Answer:**

**Recognition Signals:**
- Problems involving **contiguous subarrays/substrings**
- Keywords: "maximum/minimum", "longest/shortest", "subarray/substring"
- Need to optimize over all possible windows

**Two Main Patterns:**

**1. Fixed Size Window:**
```java
// Maximum sum of subarray of size k
int windowSum = 0;
for (int i = 0; i < k; i++) {
    windowSum += arr[i];
}
int maxSum = windowSum;

for (int i = k; i < n; i++) {
    windowSum = windowSum - arr[i - k] + arr[i];
    maxSum = Math.max(maxSum, windowSum);
}
```

**2. Variable Size Window (Expand/Shrink):**
```java
// Longest substring with at most k distinct characters
Map<Character, Integer> map = new HashMap<>();
int left = 0, maxLen = 0;

for (int right = 0; right < s.length(); right++) {
    map.put(s.charAt(right), map.getOrDefault(s.charAt(right), 0) + 1);
    
    // Shrink window when constraint violated
    while (map.size() > k) {
        char leftChar = s.charAt(left);
        map.put(leftChar, map.get(leftChar) - 1);
        if (map.get(leftChar) == 0) map.remove(leftChar);
        left++;
    }
    
    maxLen = Math.max(maxLen, right - left + 1);
}
```

**Template for Variable Window:**
1. Initialize `left = 0`, expand `right`
2. Add element at `right` to window state
3. While window invalid: shrink from `left`
4. Update result after window is valid

**When to Use:**
- Finding optimal subarray/substring
- All elements must be contiguous
- Can maintain window state efficiently (hash map, counter)

**Common Mistakes:**
- Not shrinking window when constraint violated
- Incorrect window state updates
- Off-by-one errors in length calculation

**Time Complexity:** O(n) - each element visited at most twice
**Space Complexity:** O(k) for hash map (k = distinct elements)

**Practice Problems:**
- [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
- [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
- [Longest Substring with At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/)
- [Maximum Sum Subarray of Size K](https://leetcode.com/problems/maximum-average-subarray-i/)
- [Fruit Into Baskets](https://leetcode.com/problems/fruit-into-baskets/)
- [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/)

---

### Question: What's the pattern for solving "Sliding Window Maximum" type problems?

**Answer:**

**Problem:** Find maximum in each window of size k as it slides.

**Key Insight:** Use a **Monotonic Deque** to maintain potential maximums.

**Approach:**
- Deque stores **indices** in decreasing order of values
- Front of deque = current window maximum
- Remove indices outside current window from front
- Remove smaller elements from back before adding new element

```java
Deque<Integer> deque = new LinkedList<>();
int[] result = new int[n - k + 1];

for (int i = 0; i < n; i++) {
    // Remove indices outside current window
    while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
        deque.pollFirst();
    }
    
    // Remove smaller elements from back
    while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
        deque.pollLast();
    }
    
    deque.offerLast(i);
    
    // Add to result when window is full
    if (i >= k - 1) {
        result[i - k + 1] = nums[deque.peekFirst()];
    }
}
```

**Why Deque?**
- Need to remove from both ends efficiently
- Front removal: expired indices
- Back removal: maintain decreasing order

**Time:** O(n), **Space:** O(k)

**Practice Problems:**
- [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
- [Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/)

---

## Binary Search

### Question: What's the mental model for binary search, and when do I use different variations?

**Answer:**

**Core Template:**
```java
int left = 0, right = n - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;  // Avoid overflow
    if (condition(mid)) {
        // Found or move right half
        left = mid + 1;
    } else {
        // Move left half
        right = mid - 1;
    }
}
```

**Key Decision: `left <= right` vs `left < right`?**
- `left <= right`: When searching for exact value or checking mid itself
- `left < right`: When finding first/last occurrence or boundary

**Three Main Patterns:**

**1. Classical Search (Find Exact):**
- Return mid when found
- Time: O(log n)

**2. Find Boundary (First/Last Occurrence):**
```java
// Find first occurrence
while (left < right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] >= target) {
        right = mid;  // Don't exclude mid
    } else {
        left = mid + 1;
    }
}
return left;  // left == right
```

**3. Binary Search on Answer Space:**
- Not searching in array, but searching for optimal value
- Examples: Minimum capacity to ship packages, Koko eating bananas

```java
// Template for "minimize maximum" or "maximize minimum"
int left = minPossible, right = maxPossible;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (feasible(mid)) {
        right = mid;  // Try smaller value
    } else {
        left = mid + 1;  // Need larger value
    }
}
return left;
```

**When to Use:**
- Array is sorted or monotonic
- Finding threshold/boundary
- Optimization problems with monotonic property
- "Search space" can be binary searched

**Common Pitfalls:**
- Integer overflow: Use `left + (right - left) / 2`
- Infinite loops: Ensure left/right always update
- Off-by-one errors: Be clear about inclusive/exclusive bounds

**Practice Problems:**
- [Binary Search](https://leetcode.com/problems/binary-search/)
- [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)
- [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/)
- [Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)
- [Minimum Number of Days to Make m Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/)
- [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/)

---

### Question: How do you handle binary search in rotated sorted arrays?

**Answer:**

**Key Insight:** At least one half is always sorted. Determine which half is sorted, then decide which half to search.

```java
int left = 0, right = n - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;
    
    if (nums[mid] == target) return mid;
    
    // Determine which half is sorted
    if (nums[left] <= nums[mid]) {  // Left half sorted
        if (nums[left] <= target && target < nums[mid]) {
            right = mid - 1;  // Target in left half
        } else {
            left = mid + 1;   // Target in right half
        }
    } else {  // Right half sorted
        if (nums[mid] < target && target <= nums[right]) {
            left = mid + 1;   // Target in right half
        } else {
            right = mid - 1;  // Target in left half
        }
    }
}
return -1;
```

**Why This Works:**
- Rotation creates two sorted subarrays
- Compare nums[left] with nums[mid] to identify sorted half
- If target in sorted half → search there, else search other half

**Variations:**
- **With Duplicates:** May need to skip duplicates (worst case O(n))
- **Find Minimum:** Modify to find rotation point

**Time:** O(log n), **Space:** O(1)

**Practice Problems:**
- [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/)
- [Search in Rotated Sorted Array II](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)
- [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/)
- [Find Minimum in Rotated Sorted Array II](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)

---

## Hashing & HashMaps

### Question: What are the key patterns for using hash maps in interviews?

**Answer:**

**Common Use Cases:**

**1. Frequency Counting:**
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
    // Or: freq.merge(c, 1, Integer::sum);
}
```

**2. Index Mapping (Two Sum Pattern):**
```java
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) {
        return new int[]{map.get(complement), i};
    }
    map.put(nums[i], i);
}
```

**3. Grouping/Categorization:**
```java
Map<String, List<String>> groups = new HashMap<>();
for (String s : strs) {
    String key = getKey(s);  // e.g., sorted string for anagrams
    groups.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
}
```

**4. Seen/Visited Tracking:**
```java
Set<String> seen = new HashSet<>();
if (seen.contains(item)) {
    // Already processed
}
seen.add(item);
```

**When to Use:**
- Need O(1) lookup/insert
- Counting occurrences
- Detecting duplicates
- Mapping relationships
- Caching computed values

**Trade-offs:**
- **Time:** O(1) average for operations, O(n) worst case with collisions
- **Space:** O(n) additional space
- **vs Array:** Use array when keys are bounded integers (0 to n)

**Common Patterns:**
- **Complement pattern:** Store seen values, check for complement
- **Sliding window with map:** Track character frequencies
- **Graph problems:** Store adjacency lists

**Java 8 Helpers:**
```java
map.putIfAbsent(key, defaultValue);
map.computeIfAbsent(key, k -> new ArrayList<>());
map.merge(key, 1, Integer::sum);
map.getOrDefault(key, 0);
```

**Practice Problems:**
- [Two Sum](https://leetcode.com/problems/two-sum/)
- [Group Anagrams](https://leetcode.com/problems/group-anagrams/)
- [Valid Anagram](https://leetcode.com/problems/valid-anagram/)
- [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)
- [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/)
- [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)

---

## Strings

### Question: What are the essential string patterns for coding interviews?

**Answer:**

**1. Sliding Window for Substrings:**
- Longest substring without repeating characters
- Minimum window substring
- Template: Expand right, shrink left when invalid

**2. Two Pointers for Palindromes:**
```java
// Expand around center
for (int i = 0; i < n; i++) {
    int len1 = expandAroundCenter(s, i, i);      // Odd length
    int len2 = expandAroundCenter(s, i, i + 1);  // Even length
    int maxLen = Math.max(len1, len2);
}

private int expandAroundCenter(String s, int left, int right) {
    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
        left--;
        right++;
    }
    return right - left - 1;
}
```

**3. String Building:**
```java
// Use StringBuilder for concatenation in loops
StringBuilder sb = new StringBuilder();
for (char c : chars) {
    sb.append(c);
}
String result = sb.toString();
```

**4. Character Frequency:**
```java
// For small character sets (a-z)
int[] freq = new int[26];
for (char c : s.toCharArray()) {
    freq[c - 'a']++;
}
```

**5. String Matching (KMP, Rabin-Karp):**
- KMP for pattern matching in O(m + n)
- Rabin-Karp for multiple pattern search using rolling hash

**Common Problems:**
- **Anagrams:** Sort or frequency map
- **Palindrome:** Two pointers or DP
- **Subsequence:** DP or greedy
- **Pattern Matching:** KMP, sliding window

**Key Insights:**
- Strings are immutable in Java → use StringBuilder
- Array of 26 integers faster than HashMap for lowercase letters
- Consider both odd and even length palindromes

**Practice Problems:**
- [Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/)
- [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)
- [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/)
- [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)
- [Edit Distance](https://leetcode.com/problems/edit-distance/)
- [Implement strStr()](https://leetcode.com/problems/implement-strstr/)

---

## Linked Lists

### Question: What are the fundamental linked list patterns and tricks?

**Answer:**

**1. Two Pointers (Fast & Slow):**
```java
// Find middle node
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
// slow is now at middle
```

**Use Cases:**
- Find middle
- Detect cycle
- Find kth from end (fast ahead by k steps)

**2. Dummy Node:**
```java
ListNode dummy = new ListNode(0);
dummy.next = head;
// Simplifies edge cases (empty list, removing head)
return dummy.next;
```

**3. Reverse Linked List:**
```java
ListNode prev = null, curr = head;
while (curr != null) {
    ListNode next = curr.next;
    curr.next = prev;
    prev = curr;
    curr = next;
}
return prev;  // New head
```

**4. Merge Two Sorted Lists:**
```java
ListNode dummy = new ListNode(0);
ListNode curr = dummy;
while (l1 != null && l2 != null) {
    if (l1.val < l2.val) {
        curr.next = l1;
        l1 = l1.next;
    } else {
        curr.next = l2;
        l2 = l2.next;
    }
    curr = curr.next;
}
curr.next = (l1 != null) ? l1 : l2;
return dummy.next;
```

**5. Cycle Detection (Floyd's Algorithm):**
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true;  // Cycle detected
}
return false;
```

**Common Mistakes:**
- Not handling null pointers
- Losing reference to nodes during manipulation
- Not using dummy node when it simplifies logic

**Time Complexity:** Most operations O(n)
**Space Complexity:** O(1) for in-place operations

**Practice Problems:**
- [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)
- [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/)
- [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/)
- [Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)
- [Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)
- [Reorder List](https://leetcode.com/problems/reorder-list/)
- [Palindrome Linked List](https://leetcode.com/problems/palindrome-linked-list/)

---

## Stacks & Queues

### Question: When should I use a stack, and what are the classic patterns?

**Answer:**

**Stack Use Cases:**

**1. Matching Parentheses/Brackets:**
```java
Stack<Character> stack = new Stack<>();
for (char c : s.toCharArray()) {
    if (c == '(' || c == '[' || c == '{') {
        stack.push(c);
    } else {
        if (stack.isEmpty()) return false;
        char top = stack.pop();
        if (!matches(top, c)) return false;
    }
}
return stack.isEmpty();
```

**2. Monotonic Stack (Next Greater Element):**
```java
// Find next greater element for each element
Stack<Integer> stack = new Stack<>();
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
        result[stack.pop()] = nums[i];
    }
    stack.push(i);
}
```

**Why Monotonic Stack?**
- Maintains elements in increasing/decreasing order
- O(n) solution for "next greater/smaller" problems
- Each element pushed/popped once

**3. Evaluate Expression:**
- Infix to postfix conversion
- Calculate postfix expressions
- Use two stacks: operators and operands

**4. Backtracking State:**
- DFS traversal
- Undo operations
- Function call stack simulation

**Queue Patterns:**

**1. BFS Traversal:**
```java
Queue<Node> queue = new LinkedList<>();
queue.offer(root);

while (!queue.isEmpty()) {
    int size = queue.size();
    for (int i = 0; i < size; i++) {
        Node node = queue.poll();
        // Process node
        for (Node neighbor : node.neighbors) {
            queue.offer(neighbor);
        }
    }
}
```

**2. Sliding Window Maximum (Deque):**
- Use `ArrayDeque` for O(1) operations at both ends
- Better than Stack/Queue alone

**When to Use:**
- **Stack:** LIFO needed, matching pairs, monotonic properties
- **Queue:** FIFO needed, BFS, level-order traversal
- **Deque:** Need both LIFO and FIFO operations

**Practice Problems:**
- [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)
- [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/)
- [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)
- [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)
- [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)
- [Min Stack](https://leetcode.com/problems/min-stack/)
- [Implement Queue using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/)

---

## Trees & Binary Trees

### Question: What are the essential tree traversal patterns and when to use each?

**Answer:**

**Traversal Types:**

**1. DFS - Recursive:**
```java
// Inorder (Left, Root, Right) - BST gives sorted order
void inorder(TreeNode root) {
    if (root == null) return;
    inorder(root.left);
    process(root);
    inorder(root.right);
}

// Preorder (Root, Left, Right) - Copy tree structure
void preorder(TreeNode root) {
    if (root == null) return;
    process(root);
    preorder(root.left);
    preorder(root.right);
}

// Postorder (Left, Right, Root) - Delete tree, evaluate expression
void postorder(TreeNode root) {
    if (root == null) return;
    postorder(root.left);
    postorder(root.right);
    process(root);
}
```

**2. DFS - Iterative (Using Stack):**
```java
Stack<TreeNode> stack = new Stack<>();
stack.push(root);

while (!stack.isEmpty()) {
    TreeNode node = stack.pop();
    process(node);
    
    if (node.right != null) stack.push(node.right);
    if (node.left != null) stack.push(node.left);
}
```

**3. BFS - Level Order:**
```java
Queue<TreeNode> queue = new LinkedList<>();
queue.offer(root);

while (!queue.isEmpty()) {
    int levelSize = queue.size();
    List<Integer> level = new ArrayList<>();
    
    for (int i = 0; i < levelSize; i++) {
        TreeNode node = queue.poll();
        level.add(node.val);
        
        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
    result.add(level);
}
```

**When to Use Each:**
- **Inorder:** BST operations (find kth smallest, validate BST)
- **Preorder:** Copy tree, serialize tree
- **Postorder:** Delete tree, calculate height/size
- **Level Order:** Level-by-level processing, shortest path

**Common Patterns:**

**1. Path Sum Problems:**
```java
boolean hasPathSum(TreeNode root, int sum) {
    if (root == null) return false;
    if (root.left == null && root.right == null) {
        return sum == root.val;
    }
    return hasPathSum(root.left, sum - root.val) ||
           hasPathSum(root.right, sum - root.val);
}
```

**2. Tree Height/Depth:**
```java
int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

**3. Lowest Common Ancestor:**
```java
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    
    if (left != null && right != null) return root;
    return (left != null) ? left : right;
}
```

**Time Complexity:** O(n) for all traversals
**Space Complexity:** 
- Recursive: O(h) stack space (h = height)
- Iterative: O(w) for BFS (w = max width)

**Practice Problems:**
- [Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/)
- [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)
- [Binary Tree Zigzag Level Order Traversal](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/)
- [Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/)
- [Path Sum](https://leetcode.com/problems/path-sum/)
- [Path Sum II](https://leetcode.com/problems/path-sum-ii/)

---

### Question: How do you validate a Binary Search Tree?

**Answer:**

**Key Property:** For every node, all left subtree values < node.val < all right subtree values.

**Common Mistake:** Only checking immediate children (fails for [5,1,4,null,null,3,6])

**Correct Approach - Range Checking:**
```java
boolean isValidBST(TreeNode root) {
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
}

boolean validate(TreeNode node, long min, long max) {
    if (node == null) return true;
    
    if (node.val <= min || node.val >= max) return false;
    
    return validate(node.left, min, node.val) &&
           validate(node.right, node.val, max);
}
```

**Alternative - Inorder Traversal:**
```java
Integer prev = null;
boolean isValidBST(TreeNode root) {
    if (root == null) return true;
    
    if (!isValidBST(root.left)) return false;
    
    if (prev != null && root.val <= prev) return false;
    prev = root.val;
    
    return isValidBST(root.right);
}
```

**Why Inorder Works:** BST's inorder traversal is strictly increasing.

**Time:** O(n), **Space:** O(h)

**Practice Problems:**
- [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/)
- [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)
- [Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)
- [Lowest Common Ancestor of a Binary Search Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)

---

## Graphs

### Question: What are the fundamental graph traversal algorithms and when to use each?

**Answer:**

**1. BFS (Breadth-First Search):**
```java
void bfs(int start, List<List<Integer>> graph) {
    Queue<Integer> queue = new LinkedList<>();
    boolean[] visited = new boolean[n];
    
    queue.offer(start);
    visited[start] = true;
    
    while (!queue.isEmpty()) {
        int node = queue.poll();
        process(node);
        
        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }
    }
}
```

**Use BFS When:**
- Finding shortest path in unweighted graph
- Level-by-level exploration needed
- Checking if graph is bipartite

**2. DFS (Depth-First Search):**
```java
void dfs(int node, List<List<Integer>> graph, boolean[] visited) {
    visited[node] = true;
    process(node);
    
    for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
            dfs(neighbor, graph, visited);
        }
    }
}
```

**Use DFS When:**
- Detecting cycles
- Topological sorting
- Finding connected components
- Path finding (not necessarily shortest)

**3. Union-Find (Disjoint Set):**
```java
class UnionFind {
    int[] parent, rank;
    
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression
        }
        return parent[x];
    }
    
    boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false;
        
        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
}
```

**Use Union-Find When:**
- Detecting cycles in undirected graph
- Finding connected components
- Minimum spanning tree (Kruskal's)
- Dynamic connectivity

**Complexity:**
- **BFS/DFS:** Time O(V + E), Space O(V)
- **Union-Find:** Nearly O(1) per operation with path compression

**Practice Problems:**
- [Number of Islands](https://leetcode.com/problems/number-of-islands/)
- [Clone Graph](https://leetcode.com/problems/clone-graph/)
- [Course Schedule](https://leetcode.com/problems/course-schedule/)
- [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)
- [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/)
- [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/)
- [Number of Connected Components in an Undirected Graph](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/)

---

### Question: Explain Dijkstra's algorithm and when to use it vs other shortest path algorithms.

**Answer:**

**Dijkstra's Algorithm (Single-Source Shortest Path):**

**Use When:**
- Finding shortest path from one source to all nodes
- Graph has **non-negative weights**
- Need actual shortest paths, not just if path exists

**Implementation:**
```java
int[] dijkstra(List<int[]>[] graph, int src, int n) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
    pq.offer(new int[]{src, 0});  // {node, distance}
    
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int node = curr[0], d = curr[1];
        
        if (d > dist[node]) continue;  // Already found better path
        
        for (int[] edge : graph[node]) {
            int neighbor = edge[0], weight = edge[1];
            int newDist = dist[node] + weight;
            
            if (newDist < dist[neighbor]) {
                dist[neighbor] = newDist;
                pq.offer(new int[]{neighbor, newDist});
            }
        }
    }
    return dist;
}
```

**Comparison with Other Algorithms:**

| Algorithm | Use Case | Graph Type | Time Complexity |
|-----------|----------|------------|----------------|
| **Dijkstra** | Single-source, non-negative weights | Weighted | O((V + E) log V) |
| **Bellman-Ford** | Single-source, can have negative weights | Weighted | O(V × E) |
| **Floyd-Warshall** | All-pairs shortest path | Weighted | O(V³) |
| **BFS** | Single-source, unweighted | Unweighted | O(V + E) |

**Why Greedy Works for Dijkstra:**
- With non-negative weights, once a node is processed (min distance found), it's final
- Cannot find shorter path later

**Key Insight:** Use PriorityQueue to always process closest unvisited node next.

**Time:** O((V + E) log V) with binary heap
**Space:** O(V)

**Practice Problems:**
- [Network Delay Time](https://leetcode.com/problems/network-delay-time/)
- [Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
- [Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/)
- [Swim in Rising Water](https://leetcode.com/problems/swim-in-rising-water/)

---

### Question: How do you detect and handle cycles in graphs?

**Answer:**

**Undirected Graph - Union-Find:**
```java
boolean hasCycle(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);
    for (int[] edge : edges) {
        if (!uf.union(edge[0], edge[1])) {
            return true;  // Cycle detected
        }
    }
    return false;
}
```

**Directed Graph - DFS with States:**
```java
boolean hasCycle(List<List<Integer>> graph) {
    int n = graph.size();
    int[] state = new int[n];  // 0: unvisited, 1: visiting, 2: visited
    
    for (int i = 0; i < n; i++) {
        if (state[i] == 0 && dfs(i, graph, state)) {
            return true;
        }
    }
    return false;
}

boolean dfs(int node, List<List<Integer>> graph, int[] state) {
    state[node] = 1;  // Mark as visiting
    
    for (int neighbor : graph.get(node)) {
        if (state[neighbor] == 1) return true;  // Back edge = cycle
        if (state[neighbor] == 0 && dfs(neighbor, graph, state)) {
            return true;
        }
    }
    
    state[node] = 2;  // Mark as visited
    return false;
}
```

**Key Difference:**
- **Undirected:** Cycle exists if edge connects two nodes in same component
- **Directed:** Cycle exists if back edge found (edge to ancestor in DFS tree)

**Topological Sort Connection:**
- Directed graph has cycle ↔ No topological ordering exists

**Practice Problems:**
- [Redundant Connection](https://leetcode.com/problems/redundant-connection/)
- [Redundant Connection II](https://leetcode.com/problems/redundant-connection-ii/)
- [Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/)

---

## Dynamic Programming

### Question: How do I recognize a DP problem and what's the systematic approach to solve it?

**Answer:**

**Recognition Signals:**
1. **Optimal Substructure:** Solution can be built from optimal solutions to subproblems
2. **Overlapping Subproblems:** Same subproblems solved multiple times
3. **Keywords:** "maximum/minimum", "count ways", "longest/shortest", "can/possible"

**Systematic 5-Step Approach:**

**1. Define State:**
   - `dp[i]` = answer for subproblem ending at i
   - `dp[i][j]` = answer for subproblem from i to j

**2. Write Recurrence Relation:**
   - How does `dp[i]` relate to previous states?

**3. Handle Base Cases:**
   - What are the smallest subproblems?

**4. Determine Iteration Order:**
   - Ensure dependencies are computed first

**5. Optimize Space:**
   - Can we use O(1) or O(n) instead of O(n²)?

**Example: Longest Increasing Subsequence**

```java
// Step 1: dp[i] = length of LIS ending at i
int[] dp = new int[n];
Arrays.fill(dp, 1);  // Step 3: Base case

// Step 4: Iterate left to right
for (int i = 1; i < n; i++) {
    for (int j = 0; j < i; j++) {
        // Step 2: Recurrence
        if (nums[j] < nums[i]) {
            dp[i] = Math.max(dp[i], dp[j] + 1);
        }
    }
}

// Step 5: Return maximum
return Arrays.stream(dp).max().getAsInt();
```

**Common DP Patterns:**

**1. Linear DP (1D):**
- House Robber, Climbing Stairs
- `dp[i]` depends on `dp[i-1]`, `dp[i-2]`, etc.

**2. String DP:**
- Longest Common Subsequence
- `dp[i][j]` for strings s[0..i] and t[0..j]

**3. Knapsack DP:**
- `dp[i][w]` = max value with first i items and weight w

**4. Interval DP:**
- Burst Balloons, Matrix Chain Multiplication
- `dp[i][j]` = answer for subarray [i, j]

**5. State Machine DP:**
- Stock Buy/Sell problems
- Track different states (holding, not holding, cooldown)

**Practice Problems:**
- [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
- [House Robber](https://leetcode.com/problems/house-robber/)
- [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)
- [Coin Change](https://leetcode.com/problems/coin-change/)
- [Word Break](https://leetcode.com/problems/word-break/)
- [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)
- [Edit Distance](https://leetcode.com/problems/edit-distance/)

---

### Question: What's the pattern for solving knapsack problems and their variations?

**Answer:**

**0/1 Knapsack (Each item used once):**

```java
int knapsack(int[] weights, int[] values, int W) {
    int n = weights.length;
    int[][] dp = new int[n + 1][W + 1];
    
    for (int i = 1; i <= n; i++) {
        for (int w = 1; w <= W; w++) {
            if (weights[i - 1] <= w) {
                dp[i][w] = Math.max(
                    dp[i - 1][w],  // Don't take item
                    dp[i - 1][w - weights[i - 1]] + values[i - 1]  // Take item
                );
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }
    return dp[n][W];
}
```

**Space Optimized (1D array):**
```java
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++) {
    // Iterate backwards to avoid using updated values
    for (int w = W; w >= weights[i]; w--) {
        dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
    }
}
return dp[W];
```

**Variations:**

**1. Unbounded Knapsack (Unlimited items):**
```java
// Iterate forwards (can reuse items)
for (int i = 0; i < n; i++) {
    for (int w = weights[i]; w <= W; w++) {
        dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
    }
}
```

**2. Subset Sum (Can we make sum S?):**
```java
boolean[] dp = new boolean[S + 1];
dp[0] = true;
for (int num : nums) {
    for (int s = S; s >= num; s--) {
        dp[s] = dp[s] || dp[s - num];
    }
}
return dp[S];
```

**3. Coin Change (Minimum coins for amount):**
```java
int[] dp = new int[amount + 1];
Arrays.fill(dp, amount + 1);
dp[0] = 0;

for (int coin : coins) {
    for (int a = coin; a <= amount; a++) {
        dp[a] = Math.min(dp[a], dp[a - coin] + 1);
    }
}
return dp[amount] > amount ? -1 : dp[amount];
```

**Key Differences:**
- **0/1:** Iterate backwards for space optimization
- **Unbounded:** Iterate forwards (can reuse)
- **Subset Sum:** Boolean DP (feasibility)
- **Coin Change:** Minimize count

**Time:** O(n × W), **Space:** O(W) with optimization

**Practice Problems:**
- [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)
- [Target Sum](https://leetcode.com/problems/target-sum/)
- [Coin Change](https://leetcode.com/problems/coin-change/)
- [Coin Change 2](https://leetcode.com/problems/coin-change-2/)
- [Ones and Zeroes](https://leetcode.com/problems/ones-and-zeroes/)

---

### Question: How do you approach interval DP problems?

**Answer:**

**Pattern:** Solve for all subarrays, starting from smaller intervals to larger.

**Template:**
```java
// dp[i][j] = answer for subarray [i, j]
int[][] dp = new int[n][n];

// Iterate by interval length
for (int len = 1; len <= n; len++) {
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        
        // Try all split points k in [i, j]
        for (int k = i; k < j; k++) {
            dp[i][j] = Math.max(dp[i][j], 
                dp[i][k] + dp[k + 1][j] + cost(i, j, k));
        }
    }
}
return dp[0][n - 1];
```

**Example: Matrix Chain Multiplication**
```java
int matrixChainMultiplication(int[] dims) {
    int n = dims.length - 1;  // Number of matrices
    int[][] dp = new int[n][n];
    
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i < n - len + 1; i++) {
            int j = i + len - 1;
            dp[i][j] = Integer.MAX_VALUE;
            
            for (int k = i; k < j; k++) {
                int cost = dp[i][k] + dp[k + 1][j] + 
                          dims[i] * dims[k + 1] * dims[j + 1];
                dp[i][j] = Math.min(dp[i][j], cost);
            }
        }
    }
    return dp[0][n - 1];
}
```

**Key Insight:** 
- Process shorter intervals first
- For each interval, try all possible split points
- Combine solutions of smaller intervals

**Common Problems:**
- Burst Balloons
- Matrix Chain Multiplication
- Palindrome Partitioning
- Minimum Cost Tree from Leaf Values

**Time:** O(n³), **Space:** O(n²)

**Practice Problems:**
- [Burst Balloons](https://leetcode.com/problems/burst-balloons/)
- [Minimum Cost Tree From Leaf Values](https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/)
- [Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)
- [Stone Game VII](https://leetcode.com/problems/stone-game-vii/)

---

## Backtracking

### Question: What's the systematic approach to backtracking problems?

**Answer:**

**Template:**
```java
void backtrack(state, choices, result) {
    if (isComplete(state)) {
        result.add(new ArrayList<>(state));
        return;
    }
    
    for (choice : choices) {
        if (isValid(choice, state)) {
            // Make choice
            state.add(choice);
            
            // Recurse
            backtrack(state, nextChoices, result);
            
            // Undo choice (backtrack)
            state.remove(state.size() - 1);
        }
    }
}
```

**Three Key Components:**
1. **Choice:** What decisions can we make?
2. **Constraints:** When is a choice valid?
3. **Goal:** When have we found a solution?

**Example: Permutations**
```java
void permute(int[] nums, List<Integer> current, boolean[] used, 
             List<List<Integer>> result) {
    if (current.size() == nums.length) {
        result.add(new ArrayList<>(current));
        return;
    }
    
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        
        // Choose
        current.add(nums[i]);
        used[i] = true;
        
        // Explore
        permute(nums, current, used, result);
        
        // Unchoose
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

**Example: Subsets**
```java
void subsets(int[] nums, int start, List<Integer> current, 
             List<List<Integer>> result) {
    result.add(new ArrayList<>(current));
    
    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);
        subsets(nums, i + 1, current, result);
        current.remove(current.size() - 1);
    }
}
```

**Optimization - Pruning:**
```java
// Skip invalid branches early
if (!isPromising(state)) return;

// Handle duplicates
if (i > start && nums[i] == nums[i - 1]) continue;
```

**Common Patterns:**
- **Permutations:** Use all elements, different orders
- **Combinations:** Choose k elements, order doesn't matter
- **Subsets:** Choose any number of elements
- **N-Queens:** Place items with constraints

**Time:** Often O(2ⁿ) or O(n!)
**Space:** O(n) for recursion depth

**Practice Problems:**
- [Permutations](https://leetcode.com/problems/permutations/)
- [Subsets](https://leetcode.com/problems/subsets/)
- [Combination Sum](https://leetcode.com/problems/combination-sum/)
- [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/)
- [N-Queens](https://leetcode.com/problems/n-queens/)
- [Word Search](https://leetcode.com/problems/word-search/)
- [Generate Parentheses](https://leetcode.com/problems/generate-parentheses/)

---

## Heaps & Priority Queues

### Question: When should I use a heap, and what are the key patterns?

**Answer:**

**Use Heap When:**
- Need to repeatedly find min/max element
- Processing elements in priority order
- Maintaining a sliding window of k elements
- Merging sorted lists/streams

**Java PriorityQueue:**
```java
// Min heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
// Or: Collections.reverseOrder()

// Custom comparator
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
```

**Common Patterns:**

**1. Top K Elements:**
```java
// Use min heap of size k
PriorityQueue<Integer> heap = new PriorityQueue<>();
for (int num : nums) {
    heap.offer(num);
    if (heap.size() > k) {
        heap.poll();  // Remove smallest
    }
}
// Heap contains k largest elements
```

**2. Merge K Sorted Lists:**
```java
PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
for (ListNode list : lists) {
    if (list != null) heap.offer(list);
}

ListNode dummy = new ListNode(0), curr = dummy;
while (!heap.isEmpty()) {
    ListNode node = heap.poll();
    curr.next = node;
    curr = curr.next;
    if (node.next != null) heap.offer(node.next);
}
```

**3. Median Finder (Two Heaps):**
```java
class MedianFinder {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    
    void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
```

**Complexity:**
- **Insert:** O(log n)
- **Delete:** O(log n)
- **Peek:** O(1)
- **Build Heap:** O(n)

**Practice Problems:**
- [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/)
- [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)
- [Merge K Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)
- [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/)
- [Task Scheduler](https://leetcode.com/problems/task-scheduler/)
- [Ugly Number II](https://leetcode.com/problems/ugly-number-ii/)

---

## Greedy Algorithms

### Question: How do I recognize when a greedy approach works, and what are the pitfalls?

**Answer:**

**Recognition:**
1. **Optimal Substructure:** Global optimum contains local optimums
2. **Greedy Choice Property:** Local optimal choice leads to global optimum
3. Problem involves **scheduling, intervals, or selection**

**Key Insight:** Greedy works when making locally optimal choice doesn't prevent future optimal choices.

**Classic Examples:**

**1. Activity Selection (Interval Scheduling):**
```java
// Sort by end time, select non-overlapping intervals
Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
int count = 0, lastEnd = Integer.MIN_VALUE;

for (int[] interval : intervals) {
    if (interval[0] >= lastEnd) {
        count++;
        lastEnd = interval[1];
    }
}
```

**Why Greedy Works:** Choosing earliest ending time leaves most room for future activities.

**2. Jump Game:**
```java
int maxReach = 0;
for (int i = 0; i < nums.length; i++) {
    if (i > maxReach) return false;
    maxReach = Math.max(maxReach, i + nums[i]);
}
return true;
```

**3. Huffman Coding (Minimum cost):**
- Use min heap to build optimal prefix tree
- Always combine two smallest frequencies

**When Greedy Fails:**
- **Coin Change:** Greedy doesn't work for arbitrary denominations
  - [1, 3, 4], target = 6 → Greedy: [4, 1, 1], Optimal: [3, 3]
- **Knapsack:** Need DP for 0/1 knapsack (greedy works for fractional)

**Verification:**
1. Sort/arrange elements appropriately
2. Prove locally optimal → globally optimal
3. Verify no future constraints violated

**Time:** Usually O(n log n) due to sorting

**Practice Problems:**
- [Jump Game](https://leetcode.com/problems/jump-game/)
- [Jump Game II](https://leetcode.com/problems/jump-game-ii/)
- [Gas Station](https://leetcode.com/problems/gas-station/)
- [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)
- [Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/)
- [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/)

---

## Bit Manipulation

### Question: What are the essential bit manipulation tricks for interviews?

**Answer:**

**Common Operations:**

```java
// Check if kth bit is set
boolean isSet = (num & (1 << k)) != 0;

// Set kth bit
num |= (1 << k);

// Clear kth bit
num &= ~(1 << k);

// Toggle kth bit
num ^= (1 << k);

// Clear lowest set bit
num &= (num - 1);

// Get lowest set bit
int lowestBit = num & (-num);

// Check if power of 2
boolean isPowerOf2 = (num > 0) && ((num & (num - 1)) == 0);

// Count set bits (Brian Kernighan's)
int countBits(int num) {
    int count = 0;
    while (num != 0) {
        num &= (num - 1);
        count++;
    }
    return count;
}
```

**Key Patterns:**

**1. XOR Properties:**
- `a ^ a = 0`
- `a ^ 0 = a`
- `a ^ b ^ a = b` (useful for finding unique element)

```java
// Find single number in array where all others appear twice
int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
        result ^= num;
    }
    return result;
}
```

**2. Subset Generation:**
```java
// Generate all subsets using bits
for (int mask = 0; mask < (1 << n); mask++) {
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
            subset.add(nums[i]);
        }
    }
    // Process subset
}
```

**3. Swap without temp:**
```java
a ^= b;
b ^= a;
a ^= b;
```

**Common Problems:**
- **Single Number:** Use XOR
- **Missing Number:** XOR all indices and values
- **Power of Two:** Check `n & (n-1) == 0`
- **Hamming Distance:** Count differing bits

**Time:** O(1) for most operations, O(log n) for counting bits

**Practice Problems:**
- [Single Number](https://leetcode.com/problems/single-number/)
- [Single Number II](https://leetcode.com/problems/single-number-ii/)
- [Missing Number](https://leetcode.com/problems/missing-number/)
- [Power of Two](https://leetcode.com/problems/power-of-two/)
- [Counting Bits](https://leetcode.com/problems/counting-bits/)
- [Hamming Distance](https://leetcode.com/problems/hamming-distance/)
- [Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/)

---

## Design Patterns

### Question: What are the key design patterns relevant for system design interviews?

**Answer:**

**1. Singleton Pattern:**
```java
public class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
**Use:** Database connections, configuration managers

**2. Factory Pattern:**
```java
interface Shape {
    void draw();
}

class ShapeFactory {
    Shape getShape(String type) {
        if (type.equals("CIRCLE")) return new Circle();
        if (type.equals("SQUARE")) return new Square();
        return null;
    }
}
```
**Use:** Creating objects without specifying exact class

**3. Observer Pattern:**
```java
interface Observer {
    void update(String message);
}

class Subject {
    List<Observer> observers = new ArrayList<>();
    
    void attach(Observer o) { observers.add(o); }
    void notifyObservers(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}
```
**Use:** Event systems, publish-subscribe

**4. Builder Pattern:**
```java
class User {
    private String name;
    private int age;
    private String email;
    
    public static class Builder {
        private String name;
        private int age;
        private String email;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
    
    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }
}

// Usage: new User.Builder().name("John").age(30).build();
```
**Use:** Complex object construction with many optional parameters

**5. LRU Cache:**
```java
class LRUCache {
    class Node {
        int key, value;
        Node prev, next;
    }
    
    private Map<Integer, Node> cache;
    private Node head, tail;
    private int capacity;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) return -1;
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            cache.put(key, newNode);
            addToHead(newNode);
            
            if (cache.size() > capacity) {
                Node removed = removeTail();
                cache.remove(removed.key);
            }
        }
    }
    
    // Helper methods: moveToHead, addToHead, removeTail, removeNode
}
```

**Practice Problems:**
- [LRU Cache](https://leetcode.com/problems/lru-cache/)
- [LFU Cache](https://leetcode.com/problems/lfu-cache/)
- [Design HashMap](https://leetcode.com/problems/design-hashmap/)
- [Min Stack](https://leetcode.com/problems/min-stack/)
- [Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/)

---

## 🎓 Problem-Solving Framework

### Question: What's a systematic approach to tackle any coding problem in an interview?

**Answer:**

**1. Clarify Requirements (2 minutes):**
- Input/output format
- Edge cases (empty, null, negatives)
- Constraints (size, time limit)
- Examples to verify understanding

**2. Think Out Loud (3-5 minutes):**
- Identify problem type (DP, graph, sliding window, etc.)
- Discuss brute force approach first
- Think about similar problems you've solved
- Consider different data structures

**3. Choose Approach:**
- Start with brute force if complex
- Optimize step by step
- Discuss time/space trade-offs

**4. Code (10-15 minutes):**
- Write clean, readable code
- Use meaningful variable names
- Handle edge cases
- Add comments for complex logic

**5. Test (3-5 minutes):**
- Walk through with example
- Check edge cases
- Look for off-by-one errors

**6. Optimize:**
- Analyze complexity
- Discuss potential improvements
- Consider space optimization

**Red Flags to Avoid:**
- Jumping to code immediately
- Not discussing approach first
- Ignoring edge cases
- Not testing your solution
- Being completely silent

**Time Management:**
- 5 min: Understanding + Approach
- 15 min: Coding
- 5 min: Testing + Discussion

---

## 🔑 Complexity Analysis Cheat Sheet

| Data Structure | Access | Search | Insert | Delete | Space |
|----------------|--------|--------|--------|--------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | O(n) |
| Linked List | O(n) | O(n) | O(1) | O(1) | O(n) |
| Stack | O(n) | O(n) | O(1) | O(1) | O(n) |
| Queue | O(n) | O(n) | O(1) | O(1) | O(n) |
| Hash Table | N/A | O(1)* | O(1)* | O(1)* | O(n) |
| Binary Tree | O(n) | O(n) | O(n) | O(n) | O(n) |
| BST | O(log n)* | O(log n)* | O(log n)* | O(log n)* | O(n) |
| Heap | N/A | O(n) | O(log n) | O(log n) | O(n) |

*Average case

| Algorithm | Best | Average | Worst | Space |
|-----------|------|---------|-------|-------|
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) |
| DFS/BFS | O(V+E) | O(V+E) | O(V+E) | O(V) |

---

## 💡 Interview Tips

**Communication:**
- Think out loud constantly
- Explain your reasoning
- Ask clarifying questions
- Discuss trade-offs

**Problem-Solving:**
- Start with brute force
- Optimize incrementally
- Use examples to verify logic
- Consider edge cases early

**Coding:**
- Write clean, modular code
- Use descriptive names
- Handle errors gracefully
- Test as you code

**When Stuck:**
- Revisit examples
- Try smaller inputs
- Think about similar problems
- Ask for hints (shows good judgment)

---

**Made with ☕ for FAANG Interview Success!**
