package segmenttree;


public class RangeSumQuery2DMutable {
    public static void main(String[] args) {
        int[][] matrix = {{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1},};
        NumMatrix numMatrix = new NumMatrix(matrix);

        System.out.println(numMatrix.sumRegion(2, 1, 4, 3)); // Output: 8
        numMatrix.update(3, 2, 2);
        System.out.println(numMatrix.sumRegion(2, 1, 4, 3)); // Output: 10
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
         * Constructs the NumMatrix object and builds the 2D segment tree from the input matrix.
         *
         * @param matrix The initial 2D integer matrix.
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
            buildTreeX(0, 0, numRows - 1, matrix);
        }

        /**
         * Recursively builds the outer segment tree (over rows).
         * For each node in the row-tree, it builds an inner segment tree (over columns).
         *
         * @param treeIndexX The index of the current node in the outer (row) tree.
         * @param startX     The starting row index for the range of this node.
         * @param endX       The ending row index for the range of this node.
         * @param matrix     The original input matrix.
         */
        private void buildTreeX(int treeIndexX, int startX, int endX, int[][] matrix) {
            // Base case for the outer tree: we are at a single row.
            // Build a 1D segment tree for the columns of this row.
            if (startX == endX) {
                buildTreeY(treeIndexX, 0, 0, numCols - 1, startX, matrix);
                return;
            }

            // Recursive step for the outer tree:
            int midX = startX + (endX - startX) / 2;
            int leftChildX = 2 * treeIndexX + 1;
            int rightChildX = 2 * treeIndexX + 2;

            buildTreeX(leftChildX, startX, midX, matrix);
            buildTreeX(rightChildX, midX + 1, endX, matrix);

            // Combine the results from the children's column-trees.
            // The value for each node in the current column-tree is the sum of the values
            // from the corresponding nodes in the children's column-trees.
            for (int treeIndexY = 0; treeIndexY < 4 * numCols; treeIndexY++) {
                segmentTree2D[treeIndexX][treeIndexY] =
                    segmentTree2D[leftChildX][treeIndexY] + segmentTree2D[rightChildX][treeIndexY];
            }
        }

        /**
         * Recursively builds the inner segment tree (over columns) for a specific row-node.
         *
         * @param treeIndexX The index of the outer (row) tree node we are building for.
         * @param treeIndexY The index of the current node in the inner (column) tree.
         * @param startY     The starting column index for the range of this node.
         * @param endY       The ending column index for the range of this node.
         * @param rowIndex   The row of the matrix we are processing.
         * @param matrix     The original input matrix.
         */
        private void buildTreeY(int treeIndexX, int treeIndexY, int startY, int endY, int rowIndex, int[][] matrix) {
            // Base case for the inner tree: we are at a single column (a single cell).
            if (startY == endY) {
                segmentTree2D[treeIndexX][treeIndexY] = matrix[rowIndex][startY];
                return;
            }

            // Recursive step for the inner tree:
            int midY = startY + (endY - startY) / 2;
            int leftChildY = 2 * treeIndexY + 1;
            int rightChildY = 2 * treeIndexY + 2;

            buildTreeY(treeIndexX, leftChildY, startY, midY, rowIndex, matrix);
            buildTreeY(treeIndexX, rightChildY, midY + 1, endY, rowIndex, matrix);

            segmentTree2D[treeIndexX][treeIndexY] =
                segmentTree2D[treeIndexX][leftChildY] + segmentTree2D[treeIndexX][rightChildY];
        }

        /**
         * Updates the value at a specific cell in the matrix and the segment tree.
         *
         * @param row The row index to update.
         * @param col The column index to update.
         * @param val The new value.
         */
        public void update(int row, int col, int val) {
            updateTreeX(0, 0, numRows - 1, row, col, val);
        }

        private void updateTreeX(int treeIndexX, int startX, int endX, int updateRow, int updateCol, int updateVal) {
            // We are on the path to the leaf row.
            if (startX == endX) {
                // We've found the leaf row, now update its corresponding column tree.
                updateTreeY(treeIndexX, 0, 0, numCols - 1, updateCol, updateVal);
            } else {
                int midX = startX + (endX - startX) / 2;
                int leftChildX = 2 * treeIndexX + 1;
                int rightChildX = 2 * treeIndexX + 2;

                if (updateRow <= midX) {
                    updateTreeX(leftChildX, startX, midX, updateRow, updateCol, updateVal);
                } else {
                    updateTreeX(rightChildX, midX + 1, endX, updateRow, updateCol, updateVal);
                }

                // After the child has been updated, combine the results for this node's column tree.
                for (int treeIndexY = 0; treeIndexY < 4 * numCols; treeIndexY++) {
                    segmentTree2D[treeIndexX][treeIndexY] =
                        segmentTree2D[leftChildX][treeIndexY] + segmentTree2D[rightChildX][treeIndexY];
                }
            }
        }

        private void updateTreeY(int treeIndexX, int treeIndexY, int startY, int endY, int updateCol, int updateVal) {
            // Base case: We have reached the leaf node (column) to update.
            if (startY == endY) {
                segmentTree2D[treeIndexX][treeIndexY] = updateVal;
                return;
            }

            int midY = startY + (endY - startY) / 2;
            int leftChildY = 2 * treeIndexY + 1;
            int rightChildY = 2 * treeIndexY + 2;

            if (updateCol <= midY) {
                updateTreeY(treeIndexX, leftChildY, startY, midY, updateCol, updateVal);
            } else {
                updateTreeY(treeIndexX, rightChildY, midY + 1, endY, updateCol, updateVal);
            }

            segmentTree2D[treeIndexX][treeIndexY] =
                segmentTree2D[treeIndexX][leftChildY] + segmentTree2D[treeIndexX][rightChildY];
        }

        /**
         * Calculates the sum of the elements in a rectangular region of the matrix.
         *
         * @param row1 The starting row index of the region.
         * @param col1 The starting column index of the region.
         * @param row2 The ending row index of the region.
         * @param col2 The ending column index of the region.
         * @return The sum of the elements in the specified region.
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return queryTreeX(0, 0, numRows - 1, row1, row2, col1, col2);
        }

        private int queryTreeX(int treeIndexX, int startX, int endX, int r1, int r2, int c1, int c2) {
            // No overlap with the row range
            if (r2 < startX || r1 > endX) {
                return 0;
            }
            // Complete overlap with the row range
            if (r1 <= startX && endX <= r2) {
                // The row range is fully contained. Query the column tree of this node.
                return queryTreeY(treeIndexX, 0, 0, numCols - 1, c1, c2);
            }

            // Partial overlap with the row range
            int midX = startX + (endX - startX) / 2;
            int leftChildX = 2 * treeIndexX + 1;
            int rightChildX = 2 * treeIndexX + 2;

            int leftSum = queryTreeX(leftChildX, startX, midX, r1, r2, c1, c2);
            int rightSum = queryTreeX(rightChildX, midX + 1, endX, r1, r2, c1, c2);

            return leftSum + rightSum;
        }

        private int queryTreeY(int treeIndexX, int treeIndexY, int startY, int endY, int c1, int c2) {
            // No overlap with the column range
            if (c2 < startY || c1 > endY) {
                return 0;
            }
            // Complete overlap with the column range
            if (c1 <= startY && endY <= c2) {
                return segmentTree2D[treeIndexX][treeIndexY];
            }

            // Partial overlap with the column range
            int midY = startY + (endY - startY) / 2;
            int leftChildY = 2 * treeIndexY + 1;
            int rightChildY = 2 * treeIndexY + 2;

            int leftSum = queryTreeY(treeIndexX, leftChildY, startY, midY, c1, c2);
            int rightSum = queryTreeY(treeIndexX, rightChildY, midY + 1, endY, c1, c2);

            return leftSum + rightSum;
        }
    }
}
