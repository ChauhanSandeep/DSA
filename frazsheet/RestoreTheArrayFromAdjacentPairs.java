package frazsheet;

import java.util.*;

/**
 * Problem: Restore the Array from Adjacent Pairs
 * 
 * There is an integer array nums that consists of n unique elements, but you have forgotten it.
 * However, you do remember every pair of adjacent elements in nums.
 * 
 * You are given a 2D integer array adjacentPairs of size n - 1 where each adjacentPairs[i] = [ui, vi]
 * indicates that the elements ui and vi are adjacent in nums.
 * 
 * It is guaranteed that every adjacent pair of elements nums[i] and nums[i+1] will exist in adjacentPairs,
 * either as [nums[i], nums[i+1]] or [nums[i+1], nums[i]]. The pairs can appear in any order.
 * 
 * Example:
 * Input: adjacentPairs = [[2,1],[3,4],[3,2]]
 * Output: [1,2,3,4]
 * Explanation: The array can be reconstructed as [1,2,3,4] or [4,3,2,1]
 * 
 * LeetCode: https://leetcode.com/problems/restore-the-array-from-adjacent-pairs
 * 
 * Time Complexity: O(n) where n is the number of elements in the original array
 * Space Complexity: O(n) for storing the graph and result
 */
public class RestoreTheArrayFromAdjacentPairs {
    public int[] restoreArray(int[][] adjacentPairs) {
        // Build adjacency list
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        // Count occurrences to find the ends of the array (which appear once)
        Map<Integer, Integer> count = new HashMap<>();
        
        // Build the graph and count occurrences
        for (int[] pair : adjacentPairs) {
            int u = pair[0], v = pair[1];
            
            // Build adjacency list
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            
            // Count occurrences
            count.put(u, count.getOrDefault(u, 0) + 1);
            count.put(v, count.getOrDefault(v, 0) + 1);
        }
        
        // Find the start node (a node with only one connection)
        int start = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 1) {
                start = entry.getKey();
                break;
            }
        }
        
        // Reconstruct the array
        int n = adjacentPairs.length + 1;
        int[] result = new int[n];
        result[0] = start;
        
        // Use a set to keep track of visited nodes
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        
        // Reconstruct the array
        for (int i = 1; i < n; i++) {
            int current = result[i - 1];
            for (int neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    result[i] = neighbor;
                    visited.add(neighbor);
                    break;
                }
            }
        }
        
        return result;
    }
}
