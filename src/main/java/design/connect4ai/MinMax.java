package design.connect4ai;

import java.util.LinkedList;
import java.util.Random;

/**
 * Problem: Connect Four Minimax Player
 *
 * Choose a Connect Four move by exploring future board states to a fixed depth.
 * The computer maximizes the board utility for O, while the simulated user minimizes
 * that utility for X.
 *
 * Pattern:  Game AI | Minimax | Depth-limited search
 *
 * Example:
 *   Input:  empty board with maxDepth = 0
 *   Output: utility value 0
 *   Why:    no moves are expanded at depth 0, so the neutral empty board is evaluated directly.
 *
 * Follow-ups:
 *   1. How would you speed up search?
 *      Add alpha-beta pruning to skip branches that cannot change the result.
 *   2. How would you avoid recomputing states?
 *      Store board hashes in a transposition table.
 *   3. How would you reduce randomness in equal scores?
 *      Prefer center columns or the shortest winning line as a deterministic tie-break.
 */
public class MinMax {
    //Variable that holds the maximum depth the MinMax algorithm will reach (level of the three)
    int maxDepth;
    //Variable that holds which letter the computer controls
    int computerLetter;
    ////////////////////

    //Constructor
    public MinMax(int thePlayerLetter) {
        maxDepth = 5; //This is important to get a better decision (more depth, more accurate decision, more time)
        computerLetter = thePlayerLetter;
    }//end Constructor

    //Initiates the MinMax algorithm
    public GamePlay getNextMove(State board) {
        //We want to take the lowest of its recursive generated values in order to choose the greatest
        return max(board.boardWithExpansion(board), 0);
    }//end getNextMove

    //The max and min methods are called interchangingly, one after another until a max depth is reached
    //The difference between Tic Tac Toe program is that here we can specify a depth (number of levels in the three)
    public GamePlay min(State board, int depth) { //MIN plays 'X' (user)
        Random r = new Random();
        // If MIN is called on a state that is terminal or after a maximum depth is reached, then a heuristic is calculated on the state and the move returned.
        if((board.checkGameOver()) || (depth == maxDepth)){
            GamePlay baseMove = new GamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        }else{
            //The children-moves of the state are calculated (expansion)
            LinkedList<State> children = new LinkedList<State>(board.getChildren(State.X));
            GamePlay minMove = new GamePlay();
            minMove = minMove.moveToCompare(Integer.MAX_VALUE);
            for (int i =0; i < children.size();i++) {
                State child = children.get(i);
                //And for each child max is called, on a lower depth
                GamePlay move = max(child, depth + 1);
                //The child-move with the lowest value is selected and returned by max
                if(move.getValue() <= minMove.getValue()) {
                    if ((move.getValue() == minMove.getValue())) {
                        //If the heuristic has the same value then we randomly choose one of the two moves
                        if (r.nextInt(2) == 0) { //If 0, we rewrite the maxMove. Else, the maxMove is the same
                            minMove.setRow(child.lastMove.row);
                            minMove.setCol(child.lastMove.col);
                            minMove.setValue(move.getValue());
                        }
                    }
                    else {
                        minMove.setRow(child.lastMove.row);
                        minMove.setCol(child.lastMove.col);
                        minMove.setValue(move.getValue());
                    }
                }
            }
            return minMove;
        }
    }//end min

    //The max and min methods are called interchangingly, one after another until a max depth or game over is reached
    public GamePlay max(State board, int depth) { //MAX plays 'O'
        Random r = new Random();
        //If MAX is called on a state that is terminal or after a maximum depth is reached, then a heuristic is calculated on the state and the move returned.
        if((board.checkGameOver()) || (depth == maxDepth)) {
            GamePlay baseMove = new GamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        }else{
            LinkedList<State> children = new LinkedList<State>(board.getChildren(computerLetter));
            GamePlay maxMove = new GamePlay();
            maxMove = maxMove.moveToCompare(Integer.MIN_VALUE);
            for (int i =0; i < children.size();i++) {
                State child = children.get(i);
                GamePlay move = min(child, depth + 1);
                //Here is the difference with Min method: The greatest value is selected
                if(move.getValue() >= maxMove.getValue()) {
                    if ((move.getValue() == maxMove.getValue())) {
                        if (r.nextInt(2) == 0) {
                            maxMove.setRow(child.lastMove.row);
                            maxMove.setCol(child.lastMove.col);
                            maxMove.setValue(move.getValue());
                        }
                    }
                else {
                    maxMove.setRow(child.lastMove.row);
                    maxMove.setCol(child.lastMove.col);
                    maxMove.setValue(move.getValue());
                    }
                }
            }
            return maxMove;
        }
    }

    public static void main(String[] args) {
        State emptyBoard = new State();
        MinMax computer = new MinMax(State.O);
        computer.maxDepth = 0;
        GamePlay nextMove = computer.getNextMove(emptyBoard);
        System.out.printf("maxDepth=0,getNextMove(empty) -> %d  expected=0%n", nextMove.getValue());

        GamePlay minMove = computer.min(emptyBoard, 0);
        System.out.printf("maxDepth=0,min(empty) -> %d  expected=0%n", minMove.getValue());
    }
}