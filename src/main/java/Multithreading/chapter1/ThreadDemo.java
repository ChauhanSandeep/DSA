package Multithreading.chapter1;

/**
 * Demonstrates four basic Java thread creation styles.
 *
 * The main method starts a Runnable implementation, a Thread subclass, an
 * anonymous Runnable, and a lambda Runnable. The example does not share mutable
 * state; it focuses on thread construction and scheduling rather than locking.
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

    /** Runs the repeated print-and-sleep task used by the inline thread demos. */
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

/** Runnable-based worker used by ThreadDemo. */
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

/** Thread subclass worker used by ThreadDemo. */
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
