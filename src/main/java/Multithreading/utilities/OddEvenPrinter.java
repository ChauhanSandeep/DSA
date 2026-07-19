package Multithreading.utilities;

/**
 * Demonstrates ordered odd/even printing with wait and notifyAll.
 *
 * Two NumberPrinter threads share one monitor object and a static turn flag.
 * Each thread enters a synchronized block, waits while it is not that thread's
 * turn, prints its number, toggles the flag, and notifies the other thread.
 */
public class OddEvenPrinter {

    public static void main(String[] args) {
        Object lock = new Object();

        Thread oddThread = new Thread(new NumberPrinter(lock, false, 1, 2), "Odd");
        Thread evenThread = new Thread(new NumberPrinter(lock, true, 2, 2), "Even");

        oddThread.start();
        evenThread.start();
    }
}

/** Runnable that prints either odd or even numbers using the shared monitor. */
class NumberPrinter implements Runnable {
    private static boolean isEvenTurn = false; // Shared flag to indicate turn
    private final Object lock;
    private final boolean isEvenThread;
    private int number;
    private final int step;

    public NumberPrinter(Object lock, boolean isEvenThread, int startNumber, int step) {
        this.lock = lock;
        this.isEvenThread = isEvenThread;
        this.number = startNumber;
        this.step = step;
    }

    @Override
    public void run() {
        while (number <= 20) { // Limiting for demonstration, can be adjusted
            synchronized (lock) {
                while (isEvenThread != isEvenTurn) {
                    try {
                        lock.wait(); // Wait until it's this thread's turn
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread interrupted: " + e.getMessage());
                    }
                }

                System.out.println(Thread.currentThread().getName() + ": " + number);
                number += step;
                isEvenTurn = !isEvenTurn; // Toggle flag for the next thread

                lock.notifyAll(); // Notify waiting threads
            }

            try {
                Thread.sleep(500); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
