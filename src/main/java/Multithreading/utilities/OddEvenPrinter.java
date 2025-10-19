package Multithreading.utilities;

/**
 * Problem: Print Odd and Even numbers using two threads in sequence.
 * 
 * Intuition:
 * - One thread prints odd numbers, and the other prints even numbers.
 * - A shared lock is used for synchronization.
 * - Threads use `wait()` and `notifyAll()` to alternate execution.
 *
 * Approach:
 * - Use a shared lock object.
 * - Each thread waits until it's its turn to print.
 * - The sequence is maintained by a shared flag.
 *
 * Time Complexity: O(N) — Each number is printed once.
 * Space Complexity: O(1) — Constant extra space used.
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

/**
 * Runnable class for printing numbers sequentially.
 */
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
