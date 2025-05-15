package multithreading.chapter1;

import java.util.concurrent.*;

/**
 * Demonstrates different types of thread pools available in Java's Executor Framework.
 *
 * <p> Thread Pool Types Covered:
 * 1. Single Thread Executor - Executes tasks sequentially using a single worker thread.
 * 2. Fixed Thread Pool - Uses a fixed number of threads to execute tasks concurrently.
 * 3. Cached Thread Pool - Creates new threads as needed and reuses idle threads.
 * 4. Scheduled Thread Pool - Schedules tasks for execution at fixed intervals or with a delay.
 *
 * <p> Ensures proper shutdown of executors to prevent resource leaks.
 */
public class ThreadPoolExample {

    public static void main(String[] args) {
        executeSingleThreadPool();
//        executeFixedThreadPool();
//        executeCachedThreadPool();
//        executeScheduledThreadPool();
    }

    /**
     * Executes tasks using a Single Thread Executor.
     * Ensures that tasks execute sequentially in a single-threaded environment.
     */
    public static void executeSingleThreadPool() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 5; i++) {
            singleThreadExecutor.submit(new WorkerTask(i));
        }

        // Shutdown the executor service
        shutdownExecutor(singleThreadExecutor);
    }

    /**
     * Executes tasks using a Fixed Thread Pool with 5 worker threads.
     * Ideal for executing a predictable number of tasks in parallel.
     */
    public static void executeFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            fixedThreadPool.submit(new WorkerTask(i));
        }

        shutdownExecutor(fixedThreadPool);
    }

    /**
     * Executes tasks using a Cached Thread Pool.
     * Suitable for handling many short-lived asynchronous tasks.
     */
    public static void executeCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            cachedThreadPool.submit(new WorkerTask(i));
        }

        shutdownExecutor(cachedThreadPool);
    }

    /**
     * Executes tasks using a Scheduled Thread Pool.
     * Schedules periodic execution of a task with a fixed delay.
     */
    public static void executeScheduledThreadPool() {
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

        scheduledExecutor.scheduleAtFixedRate(
                () -> System.out.println("Scheduled task executed by " + Thread.currentThread().getName()),
                1000, 5000, TimeUnit.MILLISECONDS
        );

        shutdownExecutor(scheduledExecutor);
    }

    /**
     * Properly shuts down an executor service to prevent resource leaks.
     *
     * @param executorService the executor to shut down
     */
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

/**
 * Represents a worker task that prints task execution details.
 */
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
