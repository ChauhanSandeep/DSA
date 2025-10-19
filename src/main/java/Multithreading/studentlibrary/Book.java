package Multithreading.studentlibrary;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a book in a library that can be read by students in a concurrent environment.
 * Ensures thread safety using a ReentrantLock to prevent multiple students from reading the same book at the same time.
 *
 * Problem: Concurrency control in resource-sharing environments.
 * Solution: Uses explicit locking via ReentrantLock to ensure mutual exclusion.
 *
 * @author [Your Name]
 */
public class Book {

    private final int bookId;
    private final Lock bookLock;

    /**
     * Initializes a book with a unique ID.
     *
     * @param id The unique identifier for the book.
     */
    public Book(int id) {
        this.bookId = id;
        this.bookLock = new ReentrantLock();
    }

    /**
     * Allows a student to read the book while ensuring proper synchronization.
     *
     * @param student The student attempting to read the book.
     * @throws InterruptedException If the thread is interrupted while reading.
     */
    public void read(Student student) throws InterruptedException {
        bookLock.lock();
        try {
            System.out.println("Student " + student.getId() + " started reading " + this);
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("Student " + student.getId() + " finished reading " + this);
        } finally {
            bookLock.unlock(); // Ensures the lock is always released
        }
    }

    @Override
    public String toString() {
        return "Book#" + this.bookId;
    }
}
