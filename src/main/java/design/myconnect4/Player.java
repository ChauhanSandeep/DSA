package design.myconnect4;

/**
 * Player Class for Connect 4 Game
 *
 * Represents a player in the Connect 4 game with a name and disc color.
 *
 * Properties:
 * - name: Player's identifier (e.g., "Player 1", "Alice")
 * - color: Character representing player's disc color on the board (e.g., 'R' for red, 'Y' for yellow)
 *
 * Design Notes:
 * - Simple value object (data holder)
 * - Color should be a visible character for display purposes
 * - In a complete implementation, could add methods for AI move calculation,
 *   player statistics, or move history
 *
 * Typical Usage:
 * - Player player1 = new Player("Alice", 'R');
 * - Player player2 = new Player("Bob", 'Y');
 *
 * @author Sandeep Chauhan
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
}
