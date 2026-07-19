package Multithreading.studentlibrary;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a shared library book protected by a ReentrantLock.
 *
 * Students may choose the same Book concurrently, but read locks the book before
 * printing and sleeping. The finally block releases the lock so another waiting
 * student can read the book even if the current thread is interrupted.
 */
public class Book {

    private final int bookId;
    private final Lock bookLock;

    /** Creates a book with a display id and its own lock. */
    public Book(int id) {
        this.bookId = id;
        this.bookLock = new ReentrantLock();
    }

    /**
     * Lets one student read this book while holding the book lock.
     *
     * @param student student attempting to read the book
     * @throws InterruptedException if interrupted during the simulated read
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
