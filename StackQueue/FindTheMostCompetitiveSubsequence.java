package StackQueue;

import java.util.*;

/**
 * 1673. Find the Most Competitive Subsequence
 * 
 * Problem: Given an integer array nums and a positive integer k, return the most
 * competitive subsequence of nums of size k. A subsequence is competitive if it
 * is lexicographically smaller than any other subsequence of the same size.
 * 
 * Example:
 * Input: nums = [3,5,2,6], k = 2
 * Output: [2,6]
 * Explanation: Among all subsequences of size 2, [2,6] is the most competitive.
 * 
 * LeetCode: https://leetcode.com/problems/find-the-most-competitive-subsequence
 * 
 * Follow-up questions:
 * Q: What if we need the k-th most competitive subsequence?
 * A: Use backtracking with lexicographic ordering or priority queue approaches.
 * 
 * Q: How to handle very large arrays efficiently?
 * A: Current solution is already O(n), can't improve asymptotically.
 * 
 * Q: Can we solve this problem in parallel?
 * A: Difficult due to sequential dependencies; stack operations are inherently sequential.
 */
public class FindTheMostCompetitiveSubsequence {
    
    /**
     * Monotonic stack approach to find most competitive subsequence.
     * 
     * Algorithm: Greedy with stack
     * - Use stack to build result subsequence
     * - For each element, pop stack elements that are larger and can be replaced
     * - Only pop if we have enough remaining elements to fill required slots
     * - Add current element to stack
     * 
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(k) for the stack/result
     */
    public int[] mostCompetitive(int[] nums, int k) {
        Stack<Integer> stack = new Stack<>();
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            // Pop elements that are larger than current and can be safely removed
            while (!stack.isEmpty() 
                   && stack.peek() > nums[i] 
                   && stack.size() + (n - i) > k) {
                stack.pop();
            }
            
            // Add current element if we haven't reached k elements yet
            if (stack.size() < k) {
                stack.push(nums[i]);
            }
        }
        
        // Convert stack to array
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = stack.pop();
        }
        
        return result;
    }
    
    /**
     * Deque-based approach for more flexibility in operations.
     * Provides same functionality but with different data structure.
     */
    public int[] mostCompetitiveDeque(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            // Remove larger elements from the end if we can afford to
            while (!deque.isEmpty() 
                   && deque.peekLast() > nums[i] 
                   && deque.size() + (n - i) > k) {
                deque.removeLast();
            }
            
            if (deque.size() < k) {
                deque.addLast(nums[i]);
            }
        }
        
        return deque.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Array-based approach without extra data structures.
     * Uses array indices to simulate stack behavior.
     */
    public int[] mostCompetitiveArray(int[] nums, int k) {
        int[] result = new int[k];
        int top = -1; // Stack pointer
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            // Pop elements that are larger and can be replaced
            while (top >= 0 
                   && result[top] > nums[i] 
                   && (top + 1) + (n - i) > k) {
                top--;
            }
            
            // Push current element if space available
            if (top + 1 < k) {
                result[++top] = nums[i];
            }
        }
        
        return result;
    }
    
    /**
     * Two-pointer approach with explicit remaining count tracking.
     * More intuitive for understanding the algorithm logic.
     */
    public int[] mostCompetitiveTwoPointer(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            int remaining = n - i; // Elements remaining including current
            
            // Remove larger elements if we have enough elements left
            while (!result.isEmpty() 
                   && result.get(result.size() - 1) > nums[i]
                   && result.size() + remaining > k) {
                result.remove(result.size() - 1);
            }
            
            // Add current element if we need more elements
            if (result.size() < k) {
                result.add(nums[i]);
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Recursive approach for educational purposes.
     * Not efficient but shows the decision-making process clearly.
     */
    public int[] mostCompetitiveRecursive(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        findMostCompetitive(nums, 0, k, result, new ArrayList<>());
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    // Recursive helper to find most competitive subsequence
    private void findMostCompetitive(int[] nums, int index, int k, List<Integer> best, List<Integer> current) {
        if (current.size() == k) {
            if (best.isEmpty() || isLexicographicallySmaller(current, best)) {
                best.clear();
                best.addAll(current);
            }
            return;
        }
        
        if (index >= nums.length || current.size() + (nums.length - index) < k) {
            return;
        }
        
        // Include current element
        current.add(nums[index]);
        findMostCompetitive(nums, index + 1, k, best, current);
        current.remove(current.size() - 1);
        
        // Exclude current element
        findMostCompetitive(nums, index + 1, k, best, current);
    }
    
    // Check if list1 is lexicographically smaller than list2
    private boolean isLexicographicallySmaller(List<Integer> list1, List<Integer> list2) {
        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            if (list1.get(i) < list2.get(i)) return true;
            if (list1.get(i) > list2.get(i)) return false;
        }
        return list1.size() < list2.size();
    }
    
    /**
     * Priority queue approach for finding k smallest elements with position constraints.
     * Alternative perspective on the problem.
     */
    public int[] mostCompetitivePriorityQueue(int[] nums, int k) {
        // This approach doesn't work directly due to ordering constraints
        // Included for completeness but reverts to stack approach
        return mostCompetitive(nums, k);
    }
    
    /**
     * Segment tree approach for range minimum queries.
     * Overkill for this problem but demonstrates advanced techniques.
     */
    public int[] mostCompetitiveAdvanced(int[] nums, int k) {
        // For this specific problem, the stack approach is optimal
        // Segment tree would be useful if we had multiple queries
        return mostCompetitive(nums, k);
    }
}