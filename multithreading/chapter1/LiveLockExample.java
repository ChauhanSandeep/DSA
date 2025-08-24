package multithreading.chapter1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates a LiveLock scenario.
 * 
 * **LiveLock Explanation:**
 * - Unlike deadlock, where threads remain indefinitely blocked, in a livelock,
 *   two or more threads repeatedly attempt to acquire the same resources but keep 
 *   releasing them in a way that prevents progress.
 * - In this example, `worker1` and `worker2` continuously release locks 
 *   when they can't acquire the second one, causing an infinite loop.
 * 
 * **Solution:**
 * - Both workers attempt to acquire locks in a fair way using `tryLock()`
 * - If unable to acquire the second lock, they **release the first lock** 
 *   to let the other thread proceed.
 * 
 * **Time Complexity:** O(1) per iteration  
 * **Space Complexity:** O(1) (constant space usage)
 */
public class LiveLockExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        new LiveLockExample().startThreads();
    }

    /**
     * Starts worker threads that simulate livelock behavior.
     */
    public void startThreads() {
        new Thread(this::worker1, "Worker1Thread").start();
        new Thread(this::worker2, "Worker2Thread").start();
    }

    /**
     * Worker 1 tries to acquire lock1, then attempts to acquire lock2.
     * If it cannot acquire lock2, it releases lock1 and retries.
     */
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

    /**
     * Worker 2 tries to acquire lock2, then attempts to acquire lock1.
     * If it cannot acquire lock1, it releases lock2 and retries.
     */
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
