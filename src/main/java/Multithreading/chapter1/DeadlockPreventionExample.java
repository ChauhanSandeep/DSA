package Multithreading.chapter1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates avoiding a classic two-lock deadlock with timed ReentrantLock
 * acquisition.
 *
 * The two workers request lock1 and lock2 in opposite orders, which would be
 * dangerous with unconditional lock calls. Each worker uses tryLock with a
 * timeout so a failed second acquisition releases the first lock instead of
 * waiting forever.
 */
public class DeadlockPreventionExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new DeadlockPreventionExample().startThreads();
    }

    /** Starts the two workers that acquire the same locks in opposite orders. */
    public void startThreads() {
        new Thread(this::worker1, "Worker1").start();
        new Thread(this::worker2, "Worker2").start();
    }

    /** Tries lock1 first, then lock2, using timed tryLock calls. */
    private void worker1() {
        try {
            if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + " acquired lock1");

                Thread.sleep(300); // Simulate work

                if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " acquired lock2");
                    } finally {
                        lock2.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " could not acquire lock2, releasing lock1");
                }

                lock1.unlock();
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire lock1");
            }
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
        }
    }

    /** Tries lock2 first, then lock1, using timed tryLock calls. */
    private void worker2() {
        try {
            if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + " acquired lock2");

                Thread.sleep(300); // Simulate work

                if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " acquired lock1");
                    } finally {
                        lock1.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " could not acquire lock1, releasing lock2");
                }

                lock2.unlock();
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire lock2");
            }
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
        }
    }
}
