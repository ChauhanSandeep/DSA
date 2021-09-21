package Array;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
 *
 * Integers in each row are sorted from left to right.
 * The first integer of each row is greater than the last integer of the previous row.
 */
public class Search2DMatrix {

    public static void main(String[] args) {
        int[][] matrix = {
                {1,3,5,7},
                {10,11,16,20},
                {23,30,34,60}
        };
        boolean result = new Search2DMatrix().searchMatrix(matrix, 11);
        System.out.println(result);
        result = new Search2DMatrix().searchMatrix(matrix, 13);
        System.out.println(result);
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int upper = 0;
        int lower = matrix.length -1;
        int m = matrix.length - 1;
        int n = matrix[0].length - 1;

        // FIND CORRECT ROW
        while(upper < lower) {
            int mid = (upper+lower)/2;
            if(target <= matrix[mid][n] && target >= matrix[mid][0]) {
                upper = mid;
                break;
            }else if (target <= matrix[upper][n] && target >= matrix[upper][0]) {
                break;
            }else if (target <= matrix[lower][n] && target >= matrix[lower][0]) {
                upper = lower;
                break;
            }else if(target >= matrix[mid][0] && target <= matrix[lower][n]) {
                upper = mid+1;
            }else {
                lower = mid-1;
            }
        }

        // BINARY SEARCH IN CORRECT ROW
        int left = 0;
        int right = n;

        while(left <= right) {
            int mid = (left + right)/2;
            int curr = matrix[upper][mid];
            if(curr == target) return true;
            else if (curr < target) left = mid+1;
            else right = mid-1;
        }
        return false;
    }
}
