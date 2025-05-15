package Multithreading.philosopherproblem;

/**
 * Represents the state of a chopstick in the Dining Philosophers problem.
 * A philosopher can pick up either the LEFT or RIGHT chopstick.
 * 
 * Problem: Dining Philosophers Problem (Concurrency Control)
 * 
 * @author [Your Name]
 */
public enum State {
    LEFT,  // Represents the left chopstick
    RIGHT; // Represents the right chopstick

    /**
     * Provides a user-friendly representation of the state.
     * 
     * @return A formatted string of the chopstick position.
     */
    @Override
    public String toString() {
        return this == LEFT ? "Left" : "Right";
    }
}
