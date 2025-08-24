package multithreading.concurrentcollections;

import java.util.concurrent.*;

/**
 * Demonstrates the use of CountDownLatch to synchronize multiple threads.
 *
 * CountDownLatch allows a certain number of threads to reach a common synchronization point before proceeding.
 * Once the latch count reaches zero, the main thread continues execution.
 *
 * - A thread pool of size 2 is created.
 * - 10 worker threads are executed, but the main thread waits only for 5 of them to finish.
 * - The remaining threads continue executing in the background.
 *
 * Key Features:
 * - CountDownLatch ensures only 5 worker threads must complete before proceeding.
 * - Other worker threads may still be running, but they do not block the main thread.
 *
 * Time Complexity: O(n) where n = total number of worker threads.
 * Space Complexity: O(1) (constant space apart from thread storage).
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

    /**
     * Worker class that simulates a task and decrements the latch count.
     */
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
