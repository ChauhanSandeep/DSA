package Multithreading.chapter1;

import java.util.concurrent.Semaphore;

/**
 * Demonstrates fair Semaphore permits as a concurrency throttle.
 *
 * The first demo uses one fair permit as a mutex so only one task prints at a
 * time. The second demo uses two fair permits so two tasks may enter the
 * critical section concurrently while the third waits for a release.
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

/** Sender task that prints messages while holding one semaphore permit. */
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

/** Receiver task that prints messages while holding one semaphore permit. */
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
