package multithreading.chapter1;

import java.util.concurrent.Semaphore;

/**
 * Demonstrates the usage of Semaphores for controlling thread access to resources.
 * 
 * **Concept of Semaphores:**
 * - A semaphore controls access to a shared resource by maintaining a set number of permits.
 * - `Semaphore(1, true)` creates a **mutex** (mutual exclusion), allowing only one thread at a time.
 * - `Semaphore(2, true)` allows **two** threads to access a resource concurrently.
 * 
 * **Key Features:**
 * - Demonstrates semaphore usage with one and two permits.
 * - Ensures fairness (`true` in constructor) to grant permits in the order of requests.
 * - Simulates sender and receiver tasks acquiring and releasing semaphore permits.
 * 
 * **Time Complexity:** O(N) per thread (since each prints N times)  
 * **Space Complexity:** O(1) (constant extra space usage)
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        Semaphore singlePermitSemaphore = new Semaphore(1, true); // Mutex (only 1 thread allowed)
        
        Thread sender1 = new Thread(new SenderTask(singlePermitSemaphore));
        Thread receiver1 = new Thread(new ReceiverTask(singlePermitSemaphore));

        sender1.start();
        receiver1.start();

        try {
            sender1.join();
            receiver1.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        System.out.println("------- Allowing two threads concurrently -------");

        Semaphore multiPermitSemaphore = new Semaphore(2, true); // Allows 2 threads at a time

        Thread sender2 = new Thread(new SenderTask(multiPermitSemaphore));
        Thread receiver2 = new Thread(new ReceiverTask(multiPermitSemaphore));
        Thread receiver3 = new Thread(new ReceiverTask(multiPermitSemaphore));

        sender2.start();
        receiver2.start();
        receiver3.start();
    }
}

/**
 * Task representing a sender that prints messages while holding a semaphore permit.
 */
class SenderTask implements Runnable {
    private final Semaphore semaphore;

    public SenderTask(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " (Sender) -> Message " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            semaphore.release(); // Always release the permit
        }
    }
}

/**
 * Task representing a receiver that prints messages while holding a semaphore permit.
 */
class ReceiverTask implements Runnable {
    private final Semaphore semaphore;

    public ReceiverTask(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " (Receiver) -> Message " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            semaphore.release(); // Always release the permit
        }
    }
}
