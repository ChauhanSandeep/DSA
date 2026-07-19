package Multithreading.concurrentcollections;

import java.util.concurrent.*;

/**
 * Demonstrates one-shot coordination with CountDownLatch.
 *
 * Ten worker tasks are submitted to a small fixed thread pool, while the main
 * thread waits only for the first five countdowns. CountDownLatch is not reset
 * after it reaches zero, so it is useful when one or more threads need to wait
 * for a fixed number of events before continuing.
 */
public class LatchExample {

    private static final int THREAD_POOL_SIZE = 2;
    private static final int TOTAL_TASKS = 10;
    private static final int LATCH_COUNT = 5;

    public static void main(String[] args) {
        new LatchExample().executeTasks();
    }

    private void executeTasks() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(LATCH_COUNT);  // Wait for only 5 threads

        // Submitting 10 worker tasks to the executor
        for (int i = 1; i <= TOTAL_TASKS; i++) {
            executorService.execute(new Worker(i, latch));
        }

        try {
            latch.await();  // Main thread waits until 5 workers complete
            System.out.println("Desired number of threads executed successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted while waiting for latch.");
        }

        // Shutdown executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /** Worker that simulates work and decrements the shared latch. */
    static class Worker implements Runnable {
        private final int workerId;
        private final CountDownLatch latch;

        public Worker(int workerId, CountDownLatch latch) {
            this.workerId = workerId;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Thread " + workerId + " has started.");
            try {
                TimeUnit.SECONDS.sleep(1); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread " + workerId + " was interrupted.");
            }
            latch.countDown();
            System.out.println("Thread " + workerId + " completed. Latch count: " + latch.getCount());
        }
    }
}
