package Multithreading.chapter1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates livelock-style retry behavior with two ReentrantLock objects.
 *
 * Both workers keep doing useful-looking work: they acquire one lock, try to
 * acquire the other with a timeout, then release and retry when the second lock
 * is unavailable. The example uses tryLock and explicit unlocks to show how
 * progress can still be delayed even when threads are not permanently blocked.
 */
public class LiveLockExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new LiveLockExample().startThreads();
    }

    /** Starts the two workers that compete for the same locks in reverse order. */
    public void startThreads() {
        new Thread(this::worker1, "Worker1Thread").start();
        new Thread(this::worker2, "Worker2Thread").start();
    }

    /** Repeatedly tries lock1 first, then lock2, releasing lock1 on failure. */
    public void worker1() {
        while (true) {
            lock1.lock();
            System.out.println(Thread.currentThread().getName() + " acquired lock1, trying to get lock2");

            try {
                if (lock2.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " acquired lock2, work finished");
                        return; // Exit the loop once work is done
                    } finally {
                        lock2.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " could not acquire lock2, releasing lock1");
                }
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupt status
            } finally {
                lock1.unlock();
            }
        }
    }

    /** Repeatedly tries lock2 first, then lock1, releasing lock2 on failure. */
    public void worker2() {
        while (true) {
            lock2.lock();
            System.out.println(Thread.currentThread().getName() + " acquired lock2, trying to get lock1");

            try {
                if (lock1.tryLock(50, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " acquired lock1, work finished");
                        return; // Exit the loop once work is done
                    } finally {
                        lock1.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " could not acquire lock1, releasing lock2");
                }
            } catch (InterruptedException e) {
                System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupt status
            } finally {
                lock2.unlock();
            }
        }
    }
}
