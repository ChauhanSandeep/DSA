package Multithreading.chapter1;

import java.util.concurrent.*;

/**
 * Demonstrates ExecutorService variants from Java's executor framework.
 *
 * The class keeps separate methods for a single-thread executor, fixed thread
 * pool, cached thread pool, and scheduled executor. Each demo submits WorkerTask
 * instances and uses shutdown/awaitTermination so worker threads do not leak.
 */
public class ThreadPoolExample {

    public static void main(String[] args) {
        System.out.println("Single thread executor:");
        executeSingleThreadPool();
        System.out.println("Fixed thread pool:");
        executeFixedThreadPool();
        System.out.println("Cached thread pool:");
        executeCachedThreadPool();
    }

    /** Executes tasks sequentially on one executor worker thread. */
    public static void executeSingleThreadPool() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            singleThreadExecutor.submit(new WorkerTask(i));
        }

        // Shutdown the executor service
        shutdownExecutor(singleThreadExecutor);
    }

    /** Executes tasks concurrently on a fixed-size pool. */
    public static void executeFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            fixedThreadPool.submit(new WorkerTask(i));
        }

        shutdownExecutor(fixedThreadPool);
    }

    /** Executes tasks on a cached pool that can create and reuse workers. */
    public static void executeCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            cachedThreadPool.submit(new WorkerTask(i));
        }

        shutdownExecutor(cachedThreadPool);
    }

    /** Schedules a periodic task on a ScheduledExecutorService. */
    public static void executeScheduledThreadPool() {
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

        scheduledExecutor.scheduleAtFixedRate(
                () -> System.out.println("Scheduled task executed by " + Thread.currentThread().getName()),
                1000, 5000, TimeUnit.MILLISECONDS
        );

        shutdownExecutor(scheduledExecutor);
    }

    /** Shuts down an executor and interrupts remaining tasks if they overrun. */
    private static void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            // Wait for ongoing tasks to complete
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Force shutdown if tasks don't terminate in time
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }
}

/** Worker task that prints which executor thread is running it. */
class WorkerTask implements Runnable {
    private final int taskId;

    public WorkerTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Executing Task " + taskId + " on Thread " + Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep((long) (Math.random() * 2)); // Simulate task duration
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }
}
