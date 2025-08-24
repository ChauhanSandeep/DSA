package multithreading.chapter1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates a potential deadlock scenario and its prevention.
 * 
 * **Problem:**
 * - `worker1` locks `lock1` first, then tries to acquire `lock2`.
 * - `worker2` locks `lock2` first, then tries to acquire `lock1`.
 * - This can cause a deadlock if both threads wait indefinitely for the other to release its lock.
 * 
 * **Solution:**
 * - Used `tryLock()` with a timeout to prevent deadlocks.
 * - If a thread cannot acquire both locks, it releases the acquired lock and retries.
 * 
 * **Time Complexity:** O(1) per iteration  
 * **Space Complexity:** O(1) (constant space usage)
 */
public class DeadlockPreventionExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new DeadlockPreventionExample().startThreads();
    }

    /**
     * Starts two threads that attempt to acquire locks in a conflicting order.
     */
    public void startThreads() {
        new Thread(this::worker1, "Worker1").start();
        new Thread(this::worker2, "Worker2").start();
    }

    /**
     * Worker1 tries to acquire lock1 first, then lock2.
     * Uses tryLock() with timeout to prevent deadlock.
     */
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

    /**
     * Worker2 tries to acquire lock2 first, then lock1.
     * Uses tryLock() with timeout to prevent deadlock.
     */
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
