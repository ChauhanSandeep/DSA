package DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Pascal's Triangle (LeetCode #118)
 * 
 * Problem Statement:
 * Given an integer numRows, return the first numRows of Pascal's triangle.
 * In Pascal's triangle, each number is the sum of the two numbers directly above it.
 * 
 * Example 1:
 * Input: numRows = 5
 * Output: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
 * 
 * Example 2:
 * Input: numRows = 1
 * Output: [[1]]
 * 
 * Approaches:
 * 1. Iterative Approach (Optimal): O(n²) time, O(1) space (excluding output)
 *    - Build each row based on the previous row
 *    - Each element (except first and last) is the sum of the two elements above it
 * 
 * 2. Mathematical Approach (Using Combinations): O(n²) time, O(1) space (excluding output)
 *    - Calculate each element using the combination formula: C(row, col) = row! / (col! * (row-col)!)
 *    - More efficient for getting a specific element but not better for generating the whole triangle
 * 
 * 3. Dynamic Programming with Memoization: O(n²) time, O(n²) space
 *    - Store computed rows to avoid redundant calculations
 *    - Useful if we need to generate the triangle multiple times with different numRows
 * 
 * Time Complexity: O(n²) where n is numRows
 * Space Complexity: O(1) for optimal solution (excluding output)
 * 
 * Follow-up Questions:
 * 1. How would you optimize if you only needed to return the kth row of Pascal's triangle?
 *    Answer: We can optimize to use O(k) space by only storing the previous row.
 * 
 * 2. What if we need to handle very large values (n > 30) where numbers might overflow?
 *    Answer: We would need to use BigInteger to handle large numbers and prevent overflow.
 * 
 * 3. How would you modify the solution to return the triangle as a 1D array in zigzag order?
 *    Answer: We can traverse the triangle row by row, alternating between left-to-right and right-to-left.
 * 
 * LeetCode: https://leetcode.com/problems/pascals-triangle/
 */
public class PascalsTriangle {
    
    /**
     * Iterative Solution
     * 
     * Approach:
     * 1. Initialize the result list with the first row [1]
     * 2. For each subsequent row up to numRows:
     *    a. Create a new row starting and ending with 1
     *    b. Calculate middle elements as the sum of the two elements above them
     * 3. Add each row to the result list
     * 
     * Time Complexity: O(n²) where n is numRows
     * Space Complexity: O(1) excluding output, O(n²) including output
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        
        if (numRows <= 0) {
            return triangle;
        }
        
        // First row is always [1]
        List<Integer> firstRow = new ArrayList<>();
        firstRow.add(1);
        triangle.add(firstRow);
        
        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> prevRow = triangle.get(rowNum - 1);
            List<Integer> currentRow = new ArrayList<>();
            
            // First element is always 1
            currentRow.add(1);
            
            // Calculate middle elements
            for (int j = 1; j < rowNum; j++) {
                currentRow.add(prevRow.get(j - 1) + prevRow.get(j));
            }
            
            // Last element is always 1
            currentRow.add(1);
            
            triangle.add(currentRow);
        }
        
        return triangle;
    }
    
    /**
     * Mathematical Approach (Using Combinations)
     * 
     * Approach:
     * 1. Each element at position (i, j) can be calculated using combinations: C(i, j)
     * 2. C(i, j) = C(i, j-1) * (i - j + 1) / j
     * 3. This approach is more efficient for getting a specific element but not better for the whole triangle
     * 
     * Time Complexity: O(n²)
     * Space Complexity: O(1) excluding output
     */
    public List<List<Integer>> generateMath(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            int value = 1;  // C(i, 0) = 1
            
            for (int j = 0; j <= i; j++) {
                row.add(value);
                // Calculate C(i, j+1) using C(i, j)
                // C(i, j+1) = C(i, j) * (i - j) / (j + 1)
                value = value * (i - j) / (j + 1);
            }
            
            triangle.add(row);
        }
        
        return triangle;
    }
    
    /**
     * Get a specific row of Pascal's Triangle (0-indexed)
     * 
     * @param rowIndex The 0-based row index
     * @return The row at the given index
     */
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>();
        
        // Use the combination formula to generate each element
        for (int j = 0; j <= rowIndex; j++) {
            row.add(combination(rowIndex, j));
        }
        
        return row;
    }
    
    /**
     * Helper method to calculate combination C(n, k)
     */
    private int combination(int n, int k) {
        // To avoid integer overflow, use long for intermediate calculations
        long result = 1;
        
        // Since C(n, k) = C(n, n-k), we can optimize by using the smaller k
        if (k > n - k) {
            k = n - k;
        }
        
        // Calculate value of [n * (n-1) * ... * (n-k+1)] / [k * (k-1) * ... * 1]
        for (int i = 0; i < k; i++) {
            result = result * (n - i);
            result = result / (i + 1);
        }
        
        return (int) result;
    }
    
    public static void main(String[] args) {
        PascalsTriangle solution = new PascalsTriangle();
        
        // Test cases
        System.out.println("Test 1 (5 rows):");
        printTriangle(solution.generate(5));
        
        System.out.println("\nTest 2 (1 row):");
        printTriangle(solution.generate(1));
        
        System.out.println("\nTest 3 (0 rows):");
        printTriangle(solution.generate(0));
        
        // Test mathematical approach
        System.out.println("\nMathematical Approach (5 rows):");
        printTriangle(solution.generateMath(5));
        
        // Test getting a specific row
        System.out.println("\nRow at index 3 (0-based):");
        System.out.println(solution.getRow(3));  // Expected: [1, 3, 3, 1]
    }
    
    // Helper method to print the triangle
    private static void printTriangle(List<List<Integer>> triangle) {
        for (List<Integer> row : triangle) {
            System.out.println(row);
        }
    }
}
