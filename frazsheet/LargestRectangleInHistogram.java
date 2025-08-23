package frazsheet;

import java.util.*;

/**
 * 84. Largest Rectangle in Histogram
 * 
 * Problem: Given array of integers heights representing histogram bar heights
 * where width of each bar is 1, return the area of the largest rectangle.
 * 
 * Example:
 * Input: heights = [2,1,5,6,2,3]
 * Output: 10
 * Explanation: Rectangle with height 5 and width 2 has area = 10.
 * 
 * LeetCode: https://leetcode.com/problems/largest-rectangle-in-histogram
 * 
 * Follow-up questions:
 * Q: How to handle very large arrays efficiently?
 * A: Use divide-and-conquer or optimize stack operations with careful indexing.
 * 
 * Q: Can we find all rectangles above a certain area threshold?
 * A: Modify stack algorithm to collect all valid rectangles during traversal.
 * 
 * Q: How to extend to 2D maximum rectangle problem?
 * A: Use this as subroutine, treating each row as histogram base.
 */
public class LargestRectangleInHistogram {
    
    /**
     * Monotonic stack approach - optimal solution.
     * 
     * Algorithm: Stack-based scanning
     * - Use stack to maintain indices of increasing heights
     * - When current height < stack top height, calculate rectangle area
     * - For each popped height, width = current index - previous index - 1
     * - Process remaining stack elements after main loop
     * 
     * Time Complexity: O(n) - each element pushed/popped once
     * Space Complexity: O(n) for stack storage
     */
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int index = 0;
        
        while (index < heights.length) {
            // If current height is greater or equal, push index
            if (stack.isEmpty() || heights[index] >= heights[stack.peek()]) {
                stack.push(index++);
            } else {
                // Calculate area with heights[top] as smallest bar
                int top = stack.pop();
                int area = heights[top] * (stack.isEmpty() ? index : index - stack.peek() - 1);
                maxArea = Math.max(maxArea, area);
            }
        }
        
        // Calculate area for remaining bars in stack
        while (!stack.isEmpty()) {
            int top = stack.pop();
            int area = heights[top] * (stack.isEmpty() ? index : index - stack.peek() - 1);
            maxArea = Math.max(maxArea, area);
        }
        
        return maxArea;
    }
    
    /**
     * Cleaner monotonic stack implementation.
     * Adds sentinel values to avoid edge case handling.
     */
    public int largestRectangleAreaClean(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        
        // Add sentinel values to simplify logic
        for (int i = 0; i <= heights.length; i++) {
            int h = (i == heights.length) ? 0 : heights[i];
            
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            
            stack.push(i);
        }
        
        return maxArea;
    }
    
    /**
     * Divide and conquer approach.
     * Recursively find maximum in left, right, and crossing rectangle.
     */
    public int largestRectangleAreaDivideConquer(int[] heights) {
        return divideConquer(heights, 0, heights.length - 1);
    }
    
    private int divideConquer(int[] heights, int left, int right) {
        if (left > right) return 0;
        if (left == right) return heights[left];
        
        // Find minimum height and its index
        int minIndex = left;
        for (int i = left; i <= right; i++) {
            if (heights[i] < heights[minIndex]) {
                minIndex = i;
            }
        }
        
        // Calculate area with minimum height spanning entire range
        int minArea = heights[minIndex] * (right - left + 1);
        
        // Recursively solve left and right subproblems
        int leftArea = divideConquer(heights, left, minIndex - 1);
        int rightArea = divideConquer(heights, minIndex + 1, right);
        
        return Math.max(minArea, Math.max(leftArea, rightArea));
    }
    
    /**
     * Optimized divide and conquer using segment tree.
     * Faster minimum range queries reduce time complexity.
     */
    public int largestRectangleAreaSegmentTree(int[] heights) {
        if (heights.length == 0) return 0;
        
        SegmentTree st = new SegmentTree(heights);
        return divideConquerOptimized(heights, 0, heights.length - 1, st);
    }
    
    private int divideConquerOptimized(int[] heights, int left, int right, SegmentTree st) {
        if (left > right) return 0;
        if (left == right) return heights[left];
        
        int minIndex = st.query(left, right);
        int minArea = heights[minIndex] * (right - left + 1);
        
        int leftArea = divideConquerOptimized(heights, left, minIndex - 1, st);
        int rightArea = divideConquerOptimized(heights, minIndex + 1, right, st);
        
        return Math.max(minArea, Math.max(leftArea, rightArea));
    }
    
    // Segment tree for range minimum queries
    private static class SegmentTree {
        private int[] tree;
        private int[] heights;
        private int n;
        
        SegmentTree(int[] heights) {
            this.heights = heights;
            this.n = heights.length;
            this.tree = new int[4 * n];
            build(1, 0, n - 1);
        }
        
        private void build(int node, int start, int end) {
            if (start == end) {
                tree[node] = start;
            } else {
                int mid = (start + end) / 2;
                build(2 * node, start, mid);
                build(2 * node + 1, mid + 1, end);
                
                int leftMin = tree[2 * node];
                int rightMin = tree[2 * node + 1];
                tree[node] = (heights[leftMin] <= heights[rightMin]) ? leftMin : rightMin;
            }
        }
        
        int query(int left, int right) {
            return query(1, 0, n - 1, left, right);
        }
        
        private int query(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                return -1;
            }
            
            if (left <= start && end <= right) {
                return tree[node];
            }
            
            int mid = (start + end) / 2;
            int leftMin = query(2 * node, start, mid, left, right);
            int rightMin = query(2 * node + 1, mid + 1, end, left, right);
            
            if (leftMin == -1) return rightMin;
            if (rightMin == -1) return leftMin;
            
            return (heights[leftMin] <= heights[rightMin]) ? leftMin : rightMin;
        }
    }
    
    /**
     * Brute force approach for verification.
     * Checks all possible rectangles explicitly.
     */
    public int largestRectangleAreaBruteForce(int[] heights) {
        int maxArea = 0;
        
        for (int i = 0; i < heights.length; i++) {
            int minHeight = heights[i];
            
            for (int j = i; j < heights.length; j++) {
                minHeight = Math.min(minHeight, heights[j]);
                int area = minHeight * (j - i + 1);
                maxArea = Math.max(maxArea, area);
            }
        }
        
        return maxArea;
    }
    
    /**
     * Two-pass approach calculating left and right boundaries.
     * Precomputes for each bar how far it can extend left and right.
     */
    public int largestRectangleAreaTwoPass(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        
        int[] leftBound = new int[n];  // Left boundary for each bar
        int[] rightBound = new int[n]; // Right boundary for each bar
        
        // Calculate left boundaries
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            leftBound[i] = stack.isEmpty() ? 0 : stack.peek() + 1;
            stack.push(i);
        }
        
        // Calculate right boundaries
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            rightBound[i] = stack.isEmpty() ? n - 1 : stack.peek() - 1;
            stack.push(i);
        }
        
        // Calculate maximum area
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int area = heights[i] * (rightBound[i] - leftBound[i] + 1);
            maxArea = Math.max(maxArea, area);
        }
        
        return maxArea;
    }
    
    /**
     * Dynamic programming approach.
     * Uses DP to track maximum rectangle ending at each position.
     */
    public int largestRectangleAreaDP(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        
        // dp[i] = maximum area of rectangle with right edge at position i
        int[] dp = new int[n];
        int maxArea = heights[0];
        dp[0] = heights[0];
        
        for (int i = 1; i < n; i++) {
            // Find how far left we can extend with current height
            int j = i;
            while (j > 0 && heights[j - 1] >= heights[i]) {
                j--;
            }
            
            int area = heights[i] * (i - j + 1);
            dp[i] = area;
            maxArea = Math.max(maxArea, area);
        }
        
        return maxArea;
    }
    
    /**
     * Returns all rectangles above a given area threshold.
     * Extension that finds multiple large rectangles.
     */
    public List<Rectangle> findLargeRectangles(int[] heights, int minArea) {
        List<Rectangle> rectangles = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i <= heights.length; i++) {
            int h = (i == heights.length) ? 0 : heights[i];
            
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                int area = height * width;
                
                if (area >= minArea) {
                    int left = stack.isEmpty() ? 0 : stack.peek() + 1;
                    int right = i - 1;
                    rectangles.add(new Rectangle(left, right, height, area));
                }
            }
            
            stack.push(i);
        }
        
        return rectangles;
    }
    
    // Rectangle class for result storage
    public static class Rectangle {
        public final int left, right, height, area;
        
        public Rectangle(int left, int right, int height, int area) {
            this.left = left;
            this.right = right;
            this.height = height;
            this.area = area;
        }
        
        @Override
        public String toString() {
            return String.format("Rectangle[left=%d, right=%d, height=%d, area=%d]", 
                               left, right, height, area);
        }
    }
    
    /**
     * Parallel processing approach for very large arrays.
     * Divides array into chunks and processes concurrently.
     */
    public int largestRectangleAreaParallel(int[] heights) {
        if (heights.length < 10000) {
            return largestRectangleArea(heights); // Use sequential for small arrays
        }
        
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = heights.length / numThreads;
        
        List<Integer> results = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();
        
        for (int t = 0; t < numThreads; t++) {
            int start = t * chunkSize;
            int end = (t == numThreads - 1) ? heights.length : (t + 1) * chunkSize;
            
            Thread thread = new Thread(() -> {
                int[] chunk = Arrays.copyOfRange(heights, start, end);
                int maxArea = largestRectangleArea(chunk);
                results.add(maxArea);
            });
            
            threads.add(thread);
            thread.start();
        }
        
        // Wait for completion and find maximum
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        
        return results.stream().mapToInt(Integer::intValue).max().orElse(0);
    }
    
    /**
     * Memory-optimized approach using iterative stack simulation.
     * Reduces memory allocation for very large inputs.
     */
    public int largestRectangleAreaMemoryOptimized(int[] heights) {
        int[] stack = new int[heights.length]; // Pre-allocated stack
        int stackTop = -1;
        int maxArea = 0;
        
        for (int i = 0; i <= heights.length; i++) {
            int h = (i == heights.length) ? 0 : heights[i];
            
            while (stackTop >= 0 && h < heights[stack[stackTop]]) {
                int height = heights[stack[stackTop--]];
                int width = (stackTop < 0) ? i : i - stack[stackTop] - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            
            if (i < heights.length) {
                stack[++stackTop] = i;
            }
        }
        
        return maxArea;
    }
    
    /**
     * Histogram analysis providing detailed statistics.
     * Extension offering comprehensive histogram analysis.
     */
    public HistogramAnalysis analyzeHistogram(int[] heights) {
        int maxArea = largestRectangleArea(heights);
        List<Rectangle> largeRectangles = findLargeRectangles(heights, maxArea / 2);
        
        int totalArea = Arrays.stream(heights).sum();
        double averageHeight = Arrays.stream(heights).average().orElse(0.0);
        int maxHeight = Arrays.stream(heights).max().orElse(0);
        int minHeight = Arrays.stream(heights).min().orElse(0);
        
        return new HistogramAnalysis(
            maxArea, largeRectangles.size(), totalArea, 
            averageHeight, maxHeight, minHeight, largeRectangles
        );
    }
    
    // Analysis result class
    public static class HistogramAnalysis {
        public final int maxRectangleArea;
        public final int largeRectanglesCount;
        public final int totalArea;
        public final double averageHeight;
        public final int maxHeight;
        public final int minHeight;
        public final List<Rectangle> largeRectangles;
        
        public HistogramAnalysis(int maxRectangleArea, int largeRectanglesCount, 
                               int totalArea, double averageHeight, int maxHeight, 
                               int minHeight, List<Rectangle> largeRectangles) {
            this.maxRectangleArea = maxRectangleArea;
            this.largeRectanglesCount = largeRectanglesCount;
            this.totalArea = totalArea;
            this.averageHeight = averageHeight;
            this.maxHeight = maxHeight;
            this.minHeight = minHeight;
            this.largeRectangles = largeRectangles;
        }
        
        public double getEfficiency() {
            return totalArea == 0 ? 0 : (double) maxRectangleArea / totalArea;
        }
        
        @Override
        public String toString() {
            return String.format("Max Rectangle: %d, Total Area: %d, Efficiency: %.2f%%, " +
                               "Height Range: [%d, %d], Avg: %.1f",
                               maxRectangleArea, totalArea, getEfficiency() * 100,
                               minHeight, maxHeight, averageHeight);
        }
    }
}