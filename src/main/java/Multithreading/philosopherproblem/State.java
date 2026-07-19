package Multithreading.philosopherproblem;

/**
 * Labels whether a printed chopstick action refers to the left or right side.
 *
 * The enum is used only for readable Dining Philosophers logging; the actual
 * mutual exclusion is handled by Chopstick's ReentrantLock.
 */
public enum State {
    LEFT,  // Represents the left chopstick
    RIGHT; // Represents the right chopstick

    /** Returns the display label used in philosopher log messages. */
    @Override
    public String toString() {
        return this == LEFT ? "Left" : "Right";
    }
}
