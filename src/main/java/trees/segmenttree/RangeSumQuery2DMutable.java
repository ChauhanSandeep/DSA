package trees.segmenttree;

import java.util.Arrays;
/**
 * Problem: Range Sum Query 2D - Mutable
 *
 * Maintain a matrix under point updates and rectangle-sum queries. The original
 * solution uses a 2D segment tree: an outer tree over rows, and for each row node
 * an inner segment tree over columns.
 *
 * Leetcode: https://leetcode.com/problems/range-sum-query-2d-mutable/ (Hard)
 * Rating:   not available (premium problem)
 * Pattern:  Segment tree | 2D segment tree | Point update and rectangle query
 *
 * Example:
 *   Input:  sumRegion(2,1,4,3), update(3,2,2), sumRegion(2,1,4,3)
 *   Output: [8,10]
 *   Why:    changing matrix[3][2] from 0 to 2 adds two to the queried rectangle.
 *
 * Follow-ups:
 *   1. How would you reduce constants?
 *      Use a 2D Fenwick Tree for sums with simpler O(mn) storage.
 *   2. How would you support rectangle updates?
 *      Add lazy propagation in both dimensions or use multiple BITs.
 *   3. How would you handle sparse matrices?
 *      Use coordinate compression and maps for only touched cells.
 *   4. How would you support min or max queries?
 *      Keep the segment tree shape but change the combine operation.
 *
 * Related: Range Sum Query - Mutable (307).
 */
public class RangeSumQuery2DMutable {
        public static void main(String[] args) {
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        NumMatrix numMatrix = new NumMatrix(matrix);
        int before = numMatrix.sumRegion(2, 1, 4, 3);
        numMatrix.update(3, 2, 2);
        int after = numMatrix.sumRegion(2, 1, 4, 3);
        System.out.printf("matrix=%s -> [%d, %d]  expected=[8, 10]%n",
            Arrays.deepToString(matrix), before, after);

        NumMatrix single = new NumMatrix(new int[][] {{5}});
        System.out.printf("matrix=%s -> %d  expected=5%n",
            Arrays.deepToString(new int[][] {{5}}), single.sumRegion(0, 0, 0, 0));
    }


    /**
     * Solves the Range Sum Query 2D - Mutable problem using a 2D Segment Tree.
     * This class allows for efficient updates and sum region queries on a 2D matrix.
     * It's conceptually a Segment Tree of Segment Trees.
     */
    static class NumMatrix {
        private final int[][] segmentTree2D; // The 2D array representing the segment tree
        private final int numRows;           // The number of rows in the original matrix
        private final int numCols;           // The number of columns in the original matrix

                /**
         * Intuition: each row segment stores a full column segment tree. Leaf row nodes
         * are built from the matrix row; internal row nodes combine the corresponding
         * column-tree entries from their children.
         *
         * Algorithm:
         *   1. Handle null or empty matrices with empty tree storage.
         *   2. Store row and column counts.
         *   3. Allocate a 4*numRows by 4*numCols segment tree.
         *   4. Build the outer row tree, building or combining column trees at each node.
         *
         * Time:  O(mn) - every represented matrix cell contributes to segment tree sums.
         * Space: O(mn) - the 2D segment tree array stores row and column tree nodes.
         *
         * @param matrix initial matrix values
         */
        public NumMatrix(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                this.numRows = 0;
                this.numCols = 0;
                this.segmentTree2D = new int[0][0];
                return;
            }

            this.numRows = matrix.length;
            this.numCols = matrix[0].length;
            // 4*N is a safe upper bound for segment tree size. For 2D, it's (4*rows) x (4*cols).
            this.segmentTree2D = new int[4 * numRows][4 * numCols];
            buildTree(0, 0, numRows - 1, matrix);
        }

        /**
         * Recursively builds the outer segment tree (over rows).
         * For each node in the row-tree, it builds an inner segment tree (over columns).
         *
         * @param treeIndexRow The index of the current node in the outer (row) tree.
         * @param startRow     The starting row index for the range of this node.
         * @param endRow       The ending row index for the range of this node.
         * @param matrix       The original input matrix.
         */
        private void buildTree(int treeIndexRow, int startRow, int endRow, int[][] matrix) {
            // Base case for the outer tree: we are at a single row.
            // Build a 1D segment tree for the columns of this row.
            if (startRow == endRow) {
                buildTreeCols(treeIndexRow, 0, 0, numCols - 1, startRow, matrix);
                return;
            }

            // Recursive step for the outer tree:
            int midRow = startRow + (endRow - startRow) / 2;
            int leftChildRowIndex = 2 * treeIndexRow + 1;
            int rightChildRowIndex = 2 * treeIndexRow + 2;

            buildTree(leftChildRowIndex, startRow, midRow, matrix);
            buildTree(rightChildRowIndex, midRow + 1, endRow, matrix);

            // Combine the results from the children's column-trees.
            // The value for each node in the current column-tree is the sum of the values
            // from the corresponding nodes in the children's column-trees.
            for (int treeIndexCol = 0; treeIndexCol < 4 * numCols; treeIndexCol++) {
                segmentTree2D[treeIndexRow][treeIndexCol] =
                    segmentTree2D[leftChildRowIndex][treeIndexCol] + segmentTree2D[rightChildRowIndex][treeIndexCol];
            }
        }

        /**
         * Recursively builds the inner segment tree (over columns) for a specific row-node.
         *
         * @param treeIndexRow The index of the outer (row) tree node we are building for.
         * @param treeIndexCol The index of the current node in the inner (column) tree.
         * @param startCol     The starting column index for the range of this node.
         * @param endCol       The ending column index for the range of this node.
         * @param rowIndex     The row of the matrix we are processing.
         * @param matrix       The original input matrix.
         */
        private void buildTreeCols(int treeIndexRow, int treeIndexCol, int startCol, int endCol, int rowIndex, int[][] matrix) {
            // Base case for the inner tree: we are at a single column (a single cell).
            if (startCol == endCol) {
                segmentTree2D[treeIndexRow][treeIndexCol] = matrix[rowIndex][startCol];
                return;
            }

            // Recursive step for the inner tree:
            int midCol = startCol + (endCol - startCol) / 2;
            int leftChildColIndex = 2 * treeIndexCol + 1;
            int rightChildColIndex = 2 * treeIndexCol + 2;

            buildTreeCols(treeIndexRow, leftChildColIndex, startCol, midCol, rowIndex, matrix);
            buildTreeCols(treeIndexRow, rightChildColIndex, midCol + 1, endCol, rowIndex, matrix);

            segmentTree2D[treeIndexRow][treeIndexCol] =
                segmentTree2D[treeIndexRow][leftChildColIndex] + segmentTree2D[treeIndexRow][rightChildColIndex];
        }

                /**
         * Intuition: changing one cell affects exactly one leaf row and every ancestor row
         * on the path to the root. At each affected row node, the matching column segment
         * tree position must be updated or recombined.
         *
         * Algorithm:
         *   1. Recurse through the row tree toward row.
         *   2. At the leaf row, update the column tree at col.
         *   3. While returning, recompute each affected row node by combining children.
         *
         * Time:  O(log m * log n) - one row path and one column path per affected row node.
         * Space: O(log m + log n) - recursion stack across row and column trees.
         *
         * @param row row index to update
         * @param col column index to update
         * @param val replacement value
         */
        public void update(int row, int col, int val) {
            updateTreeRows(0, 0, numRows - 1, row, col, val);
        }

        /**
         * Recursively updates the segment tree for a specific row-node.
         *
         * @param treeIndexRow The index of the outer (row) tree node we are updating for.
         * @param startRow     The starting row index for the range of this node.
         * @param endRow       The ending row index for the range of this node.
         * @param updateRow    The row of the matrix we are updating.
         * @param updateCol    The column of the matrix we are updating.
         * @param updateVal    The new value for the cell.
         */
        private void updateTreeRows(int treeIndexRow, int startRow, int endRow, int updateRow, int updateCol, int updateVal) {
            // We are on the path to the leaf row.
            if (startRow == endRow) {
                // We've found the leaf row, now update its corresponding column tree.
                updateTreeCols(treeIndexRow, 0, 0, numCols - 1, updateCol, updateVal);
            } else {
                int midRow = startRow + (endRow - startRow) / 2;
                int leftChildRowIndex = 2 * treeIndexRow + 1;
                int rightChildRowIndex = 2 * treeIndexRow + 2;

                if (updateRow <= midRow) {
                    updateTreeRows(leftChildRowIndex, startRow, midRow, updateRow, updateCol, updateVal);
                } else {
                    updateTreeRows(rightChildRowIndex, midRow + 1, endRow, updateRow, updateCol, updateVal);
                }

                // After the child has been updated, combine the results for this node's column tree.
                for (int treeIndexCol = 0; treeIndexCol < 4 * numCols; treeIndexCol++) {
                    segmentTree2D[treeIndexRow][treeIndexCol] =
                        segmentTree2D[leftChildRowIndex][treeIndexCol] + segmentTree2D[rightChildRowIndex][treeIndexCol];
                }
            }
        }

        /**
         * Recursively updates the inner segment tree (over columns) for a specific row-node.
         *
         * @param treeIndexRow The index of the outer (row) tree node we are updating for.
         * @param treeIndexCol The index of the current node in the inner (column) tree.
         * @param startCol     The starting column index for the range of this node.
         * @param endCol       The ending column index for the range of this node.
         * @param updateCol    The column of the matrix we are updating.
         * @param updateVal    The new value for the cell.
         */
        private void updateTreeCols(int treeIndexRow, int treeIndexCol, int startCol, int endCol, int updateCol, int updateVal) {
            // Base case: We have reached the leaf node (column) to update.
            if (startCol == endCol) {
                segmentTree2D[treeIndexRow][treeIndexCol] = updateVal;
                return;
            }

            int midCol = startCol + (endCol - startCol) / 2;
            int leftChildColIndex = 2 * treeIndexCol + 1;
            int rightChildColIndex = 2 * treeIndexCol + 2;

            if (updateCol <= midCol) {
                updateTreeCols(treeIndexRow, leftChildColIndex, startCol, midCol, updateCol, updateVal);
            } else {
                updateTreeCols(treeIndexRow, rightChildColIndex, midCol + 1, endCol, updateCol, updateVal);
            }

            segmentTree2D[treeIndexRow][treeIndexCol] =
                segmentTree2D[treeIndexRow][leftChildColIndex] + segmentTree2D[treeIndexRow][rightChildColIndex];
        }

                /**
         * Intuition: a rectangle query decomposes into O(log m) row segments, and each
         * fully covered row segment answers its column range from its inner column tree.
         *
         * Algorithm:
         *   1. Query the outer row tree for rows row1..row2.
         *   2. Return 0 for row segments with no overlap.
         *   3. For complete row overlap, query that node's column tree for col1..col2.
         *   4. For partial row overlap, sum left and right row results.
         *
         * Time:  O(log m * log n) - row decomposition times column range queries.
         * Space: O(log m + log n) - recursion stack during the query.
         *
         * @param row1 top row, inclusive
         * @param col1 left column, inclusive
         * @param row2 bottom row, inclusive
         * @param col2 right column, inclusive
         * @return sum of the rectangle
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return queryTreeRows(0, 0, numRows - 1, row1, row2, col1, col2);
        }

        /**
         * Queries the outer row segment tree and delegates full row coverage to column queries.
         */
        private int queryTreeRows(int treeIndexRow, int startRow, int endRow, int leftRowRange, int rightRowRange, int leftColRange, int rightColRange) {
            // No overlap with the row range
            if (rightRowRange < startRow || leftRowRange > endRow) {
                return 0;
            }
            // Complete overlap with the row range
            if (leftRowRange <= startRow && endRow <= rightRowRange) {
                // The row range is fully contained. Query the column tree of this node.
                return queryTreeCols(treeIndexRow, 0, 0, numCols - 1, leftColRange, rightColRange);
            }

            // Partial overlap with the row range
            int midRow = startRow + (endRow - startRow) / 2;
            int leftChildRowIndex = 2 * treeIndexRow + 1;
            int rightChildRowIndex = 2 * treeIndexRow + 2;

            int leftSum = queryTreeRows(leftChildRowIndex, startRow, midRow, leftRowRange, rightRowRange, leftColRange, rightColRange);
            int rightSum = queryTreeRows(rightChildRowIndex, midRow + 1, endRow, leftRowRange, rightRowRange, leftColRange, rightColRange);

            return leftSum + rightSum;
        }

        /**
         * Queries one inner column segment tree for the requested column interval.
         */
        private int queryTreeCols(int treeIndexRow, int treeIndexCol, int startCol, int endCol, int leftColRange, int rightColRange) {
            // No overlap with the column range
            if (rightColRange < startCol || leftColRange > endCol) {
                return 0;
            }
            // Complete overlap with the column range
            if (leftColRange <= startCol && endCol <= rightColRange) {
                return segmentTree2D[treeIndexRow][treeIndexCol];
            }

            // Partial overlap with the column range
            int midCol = startCol + (endCol - startCol) / 2;
            int leftChildColIndex = 2 * treeIndexCol + 1;
            int rightChildColIndex = 2 * treeIndexCol + 2;

            int leftSum = queryTreeCols(treeIndexRow, leftChildColIndex, startCol, midCol, leftColRange, rightColRange);
            int rightSum = queryTreeCols(treeIndexRow, rightChildColIndex, midCol + 1, endCol, leftColRange, rightColRange);

            return leftSum + rightSum;
        }
    }
}
