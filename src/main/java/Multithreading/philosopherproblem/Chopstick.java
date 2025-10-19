package Multithreading.philosopherproblem;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a chopstick used by philosophers in the Dining Philosophers problem.
 * Each chopstick can be picked up or put down using locking mechanisms to avoid race conditions.
 *
 * Problem: Dining Philosophers Problem (Concurrency Control)
 * Algorithm: Used a tryLock() mechanism to prevent deadlock by setting a timeout.
 *
 * @author [Your Name]
 */
public class Chopstick {

    private final Lock chopstickLock;  // Ensures mutual exclusion
    private final int chopstickId;     // Unique identifier for each chopstick

    /**
     * Constructor to initialize the chopstick with an ID.
     * @param id Unique ID of the chopstick.
     */
    public Chopstick(int id) {
        this.chopstickId = id;
        this.chopstickLock = new ReentrantLock();
    }

    /**
     * Attempts to pick up the chopstick.
     * Uses tryLock() with a timeout to avoid deadlock.
     *
     * @param philosopher The philosopher attempting to pick up the chopstick.
     * @param state LEFT or RIGHT chopstick
     * @return true if the philosopher successfully picks up the chopstick, false otherwise.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
        if (chopstickLock.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println("Philosopher " + philosopher.getPhilosopherId() + " picked up " + state + " chopstick " + chopstickId);
            return true;
        }
        return false; // Could not acquire the chopstick within the timeout
    }

    /**
     * Releases the chopstick after use.
     * Unlocks the chopstick to allow other philosophers to use it.
     *
     * @param philosopher The philosopher putting down the chopstick.
     * @param state LEFT or RIGHT chopstick
     */
    public void putDown(Philosopher philosopher, State state) {
        chopstickLock.unlock();
        System.out.println("Philosopher " + philosopher.getPhilosopherId() + " put down " + state + " chopstick " + chopstickId);
    }
}
