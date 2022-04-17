package design.myconnect4;

import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Board board = new Board(6, 7);

        int movesCount = 1;
        Player player1 = new Player("P1", 'B');
        Player player2 = new Player("P2", 'R');
        Player currPlayer = player2;
        boolean foundWinner = false;

        while(!foundWinner && movesCount <= 42) {
            currPlayer = currPlayer == player1 ? player2 : player1;

            board.display();
            System.out.print("Player " + currPlayer.color + ", choose a column: ");
            int move = in.nextInt();

            board.makeMove(move, currPlayer);
            foundWinner = board.isWinner(currPlayer);
            movesCount++;
        }

        board.display();
        if (foundWinner) {
            System.out.println(currPlayer.name + " " + currPlayer.color + " won");
        } else {
            System.out.println("Tie game");
        }

    }
}
