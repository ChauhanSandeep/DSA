package multithreading.philosopherproblem;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a philosopher in the Dining Philosophers problem.
 * Each philosopher alternates between thinking and eating, while ensuring no deadlocks occur.
 *
 * Problem: Dining Philosophers Problem (Concurrency Control)
 * Solution: Uses a tryLock() mechanism with a timeout to prevent deadlock.
 *
 * @author [Your Name]
 */
public class Philosopher implements Runnable {

    private final int philosopherId;
    private final Chopstick lowerChopstick;
    private final Chopstick higherChopstick;
    private final Random random;
    private final AtomicBoolean hasFinishedEating;  // Ensures thread-safe state management
    private int eatingCounter = 0;  // Tracks the number of times the philosopher has eaten

    public int getPhilosopherId() {
        return philosopherId;
    }

    public Chopstick getLowerChopstick() {
        return lowerChopstick;
    }

    public Chopstick getHigherChopstick() {
        return higherChopstick;
    }

    public Random getRandom() {
        return random;
    }

    public AtomicBoolean getHasFinishedEating() {
        return hasFinishedEating;
    }

    public int getEatingCounter() {
        return eatingCounter;
    }

    /**
     * Initializes a philosopher with assigned chopsticks.
     * The chopsticks are assigned in increasing order to reduce deadlock probability.
     *
     * @param id The philosopher's unique ID.
     * @param leftChopstick The left chopstick.
     * @param rightChopstick The right chopstick.
     */
    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.philosopherId = id;
        this.random = new Random();
        this.hasFinishedEating = new AtomicBoolean(false);

        // To avoid deadlocks, always pick up the lower ID chopstick first
        if (leftChopstick.hashCode() < rightChopstick.hashCode()) {
            this.lowerChopstick = leftChopstick;
            this.higherChopstick = rightChopstick;
        } else {
            this.lowerChopstick = rightChopstick;
            this.higherChopstick = leftChopstick;
        }
    }

    @Override
    public void run() {
        try {
            while (!hasFinishedEating.get()) {
                think();
                if (lowerChopstick.pickUp(this, State.LEFT)) {
                    if (higherChopstick.pickUp(this, State.RIGHT)) {
                        eat();
                        higherChopstick.putDown(this, State.RIGHT);
                    }
                    lowerChopstick.putDown(this, State.LEFT);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Properly handle thread interruption
        }
    }

    /**
     * Simulates the philosopher thinking.
     * The philosopher waits for a random amount of time before attempting to eat.
     */
    private void think() throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is thinking...");
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    /**
     * Simulates the philosopher eating.
     * The philosopher waits for a random amount of time while eating.
     */
    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is eating...");
        eatingCounter++;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    /**
     * Sets the philosopher's state to full, signaling them to stop eating.
     */
    public void setFull() {
        hasFinishedEating.set(true);
    }
}
