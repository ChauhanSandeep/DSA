package multithreading.chapter1;

/**
 * Demonstrates different ways to create and execute threads in Java.
 * 
 * - Using a class that implements Runnable (`WorkerRunnable`).
 * - Using a class that extends Thread (`WorkerThread`).
 * - Using an anonymous inner class (`AnonymousRunnable`).
 * - Using a lambda expression (`LambdaRunnable`).
 * 
 * Each thread sleeps for 1 second between iterations and prints a message.
 * 
 * Time Complexity: O(1) per iteration (constant time operations inside loop).
 * Space Complexity: O(1) (constant space usage per thread).
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new WorkerRunnable());
        Thread thread2 = new WorkerThread();

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                executeThreadTask("AnonymousRunnable");
            }
        });

        Thread thread4 = new Thread(() -> executeThreadTask("LambdaRunnable"));

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /**
     * A utility method to execute a simple thread task.
     * @param threadName The name of the thread (for logging purposes).
     */
    private static void executeThreadTask(String threadName) {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(threadName + " - Iteration: " + i);
            }
        } catch (InterruptedException e) {
            System.err.println(threadName + " interrupted: " + e.getMessage());
        }
    }
}

/**
 * A thread worker implementing Runnable interface.
 */
class WorkerRunnable implements Runnable {
    @Override
    public void run() {
        executeThreadTask("WorkerRunnable");
    }

    private void executeThreadTask(String threadName) {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(threadName + " - Iteration: " + i);
            }
        } catch (InterruptedException e) {
            System.err.println(threadName + " interrupted: " + e.getMessage());
        }
    }
}

/**
 * A thread worker extending the Thread class.
 */
class WorkerThread extends Thread {
    @Override
    public void run() {
        executeThreadTask("WorkerThread");
    }

    private void executeThreadTask(String threadName) {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(threadName + " - Iteration: " + i);
            }
        } catch (InterruptedException e) {
            System.err.println(threadName + " interrupted: " + e.getMessage());
        }
    }
}
