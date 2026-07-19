package design.myconnect4;

/**
 * Problem: Connect Four Player
 *
 * Store the name and display color for one Connect Four participant. The game and
 * board classes read these fields when announcing turns and placing pieces.
 *
 * Pattern:  Design | Value object | Game metadata
 *
 * Example:
 *   Input:  new Player("P1", 'B')
 *   Output: name = "P1", color = 'B'
 *   Why:    the constructor records the identity and board symbol exactly as provided.
 *
 * Follow-ups:
 *   1. How would you make players immutable?
 *      Mark fields final and expose read-only accessors.
 *   2. How would you support AI players?
 *      Add a strategy object that can choose a column from the board state.
 *   3. How would you track player statistics?
 *      Store wins, losses, and move counts outside the board logic.
 */
public class Player {
    String name;  // Player's name or identifier
    char color;   // Character representing the player's disc color on the board

    /**
     * Creates a new player with specified name and disc color.
     *
     * @param name Player's name or identifier
     * @param color Character representing player's disc color (e.g., 'R', 'Y')
     */
    public Player(String name, char color) {
        this.name = name;
        this.color = color;
    }

    public static void main(String[] args) {
        Player player = new Player("P1", 'B');
        System.out.printf("player=P1/B -> %s/%s  expected=P1/B%n", player.name, player.color);

        Player other = new Player("P2", 'R');
        System.out.printf("player=P2/R -> %s/%s  expected=P2/R%n", other.name, other.color);
    }
}
