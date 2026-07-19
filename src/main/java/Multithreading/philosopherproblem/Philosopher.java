package Multithreading.philosopherproblem;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents one philosopher task in the Dining Philosophers simulation.
 *
 * A philosopher loops until an AtomicBoolean stop flag is set. Each loop thinks,
 * tries to acquire two Chopstick locks with timed tryLock calls, eats if both
 * locks are held, and releases any acquired locks before retrying.
 */
public class Philosopher implements Runnable {

    private final int philosopherId;
    private final Chopstick lowerChopstick;
    private final Chopstick higherChopstick;
    private final Random random;
    private final AtomicBoolean hasFinishedEating;  // Ensures thread-safe state management
    private int eatingCounter = 0;  // Tracks the number of times the philosopher has eaten

    /** Returns this philosopher's display id. */
    public int getPhilosopherId() {
        return philosopherId;
    }

    /** Returns the chopstick this philosopher tries before the other one. */
    public Chopstick getLowerChopstick() {
        return lowerChopstick;
    }

    /** Returns the chopstick this philosopher tries after the first one. */
    public Chopstick getHigherChopstick() {
        return higherChopstick;
    }

    /** Returns the random source used to vary thinking and eating delays. */
    public Random getRandom() {
        return random;
    }

    /** Returns the AtomicBoolean stop flag used by the simulation. */
    public AtomicBoolean getHasFinishedEating() {
        return hasFinishedEating;
    }

    /** Returns how many times this philosopher has eaten. */
    public int getEatingCounter() {
        return eatingCounter;
    }

    /**
     * Initializes a philosopher with two shared chopsticks.
     *
     * @param id philosopher display id
     * @param leftChopstick one neighboring chopstick
     * @param rightChopstick the other neighboring chopstick
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

    /** Simulates thinking before the philosopher tries to acquire chopsticks. */
    private void think() throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is thinking...");
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    /** Simulates eating while both chopstick locks are held. */
    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + philosopherId + " is eating...");
        eatingCounter++;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    /** Signals this philosopher to stop after the current loop iteration. */
    public void setFull() {
        hasFinishedEating.set(true);
    }
}
