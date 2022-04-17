package design.myconnect4;

public class Board {

    char[][] grid;

    public Board(int rows, int cols) {
        grid = new char[rows][cols];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    public void makeMove(int col, Player player) {
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = player.color;
                break;
            }
        }
    }

    public boolean isWinner(Player player) {
        //check for 4 across
        char color = player.color;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == color &&
                        grid[row][col + 1] == color &&
                        grid[row][col + 2] == color &&
                        grid[row][col + 3] == color) {
                    return true;
                }
            }
        }
        //check for 4 up and down
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col] == color &&
                        grid[row + 2][col] == color &&
                        grid[row + 3][col] == color) {
                    return true;
                }
            }
        }
        //check left diagonal
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 3; col < grid[0].length; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col - 1] == color &&
                        grid[row + 2][col - 2] == color &&
                        grid[row + 3][col - 3] == color) {
                    return true;
                }
            }
        }
        //check right diagonal
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col + 1] == color &&
                        grid[row + 2][col + 2] == color &&
                        grid[row + 3][col + 3] == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        System.out.println(" 0 1 2 3 4 5 6");
        System.out.println("---------------");
        for (int row = 0; row < grid.length; row++) {
            System.out.print("|");
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(grid[row][col]);
                System.out.print("|");
            }
            System.out.println();
            System.out.println("---------------");
        }
        System.out.println(" 0 1 2 3 4 5 6");
        System.out.println();
    }
}
