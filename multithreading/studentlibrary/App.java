package multithreading.studentlibrary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simulates a student-library scenario where multiple students try to read books concurrently.
 * Uses a fixed thread pool to manage student threads efficiently.
 *
 * Problem: Concurrency control in resource-sharing environments.
 * Solution: Uses a thread pool to prevent excessive thread creation and manage students effectively.
 *
 * @author [Your Name]
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Library simulation starting...");

        Book[] books = new Book[Constants.NUMBER_OF_BOOKS];
        Student[] students = new Student[Constants.NUMBER_OF_STUDENTS];
        ExecutorService studentThreadPool = Executors.newFixedThreadPool(Constants.NUMBER_OF_STUDENTS);

        try {
            // Initialize books
            for (int i = 0; i < Constants.NUMBER_OF_BOOKS; i++) {
                books[i] = new Book(i);
            }

            // Initialize students and submit them as tasks
            for (int i = 0; i < Constants.NUMBER_OF_STUDENTS; i++) {
                students[i] = new Student(i, books);
                studentThreadPool.submit(students[i]);
            }

        } catch (Exception e) {
            System.err.println("An error occurred during the simulation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            studentThreadPool.shutdown();
        }

        System.out.println("Library simulation ended.");
    }
}
