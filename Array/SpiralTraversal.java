package Array;

public class SpiralTraversal {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3, 4},
                {4, 5, 6, 5},
                {7, 8, 9, 1},
                {1, 9, 0, 2},
                {1, 9, 0, 2}
        };

        printSpiral(matrix);
    }

    private static void printSpiral(int[][] matrix) {
        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;
        int dir = 0;

        while(left <= right && top <= bottom) {
            switch(dir) {
                case 0:
                    //left to right
                    for(int i=left; i <= right; i++) {
                        System.out.println(matrix[top][i]);
                    }
                    top++;
                    break;
                case 1:
                    // top to bottom
                    for(int i=top; i<=bottom; i++) {
                        System.out.println(matrix[i][right]);
                    }
                    right--;
                    break;
                case 2:
                    // right to left
                    for(int i=right; i>=left; i--) {
                        System.out.println(matrix[bottom][i]);
                    }
                    bottom--;
                    break;
                case 3:
                    // bottom to top
                    for(int i=bottom; i>=top; i--) {
                        System.out.println(matrix[i][left]);
                    }
                    left ++;
                    break;
            }
            dir = (dir + 1)%4;
        }



    }
}
