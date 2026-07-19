package Multithreading.philosopherproblem;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents one lock-protected chopstick in the Dining Philosophers problem.
 *
 * Each chopstick owns a ReentrantLock. pickUp uses timed tryLock so a philosopher
 * can give up instead of blocking forever, while putDown releases the same lock
 * after eating or after a failed second chopstick attempt.
 */
public class Chopstick {

    private final Lock chopstickLock;  // Ensures mutual exclusion
    private final int chopstickId;     // Unique identifier for each chopstick

    /** Creates a chopstick with a display id and its own ReentrantLock. */
    public Chopstick(int id) {
        this.chopstickId = id;
        this.chopstickLock = new ReentrantLock();
    }

    /**
     * Attempts to acquire this chopstick with timed tryLock.
     *
     * @param philosopher philosopher attempting to pick up the chopstick
     * @param state left-or-right label printed for the philosopher
     * @return true when the lock is acquired before the timeout
     * @throws InterruptedException if interrupted while waiting for the lock
     */
    public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
        if (chopstickLock.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println("Philosopher " + philosopher.getPhilosopherId() + " picked up " + state + " chopstick " + chopstickId);
            return true;
        }
        return false; // Could not acquire the chopstick within the timeout
    }

    /**
     * Releases this chopstick's lock after a philosopher is done with it.
     *
     * @param philosopher philosopher releasing the chopstick
     * @param state left-or-right label printed for the philosopher
     */
    public void putDown(Philosopher philosopher, State state) {
        chopstickLock.unlock();
        System.out.println("Philosopher " + philosopher.getPhilosopherId() + " put down " + state + " chopstick " + chopstickId);
    }
}
