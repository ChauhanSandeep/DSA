package multithreading.philosopherproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Dining Philosophers Problem Implementation using Multithreading.
 *
 * Problem Statement:
 * - Five philosophers sit around a circular table.
 * - Each philosopher alternates between thinking and eating.
 * - To eat, a philosopher needs to pick up two chopsticks (left and right).
 * - The challenge is to prevent deadlock and starvation while ensuring fairness.
 *
 * Solution Approach:
 * - Each philosopher runs in a separate thread.
 * - We use an array of chopsticks where each philosopher shares chopsticks with neighbors.
 * - To prevent deadlock, we ensure a global ordering of chopstick acquisition.
 *
 * Time Complexity:
 * - O(N) where N is the number of philosophers.
 *
 * Space Complexity:
 * - O(N) due to the creation of N threads.
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = null;
        Philosopher[] philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
        Chopstick[] chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

        try {
            executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);

            // Initialize chopsticks
            for (int i = 0; i < Constants.NUMBER_OF_CHOPSTICKS; i++) {
                chopsticks[i] = new Chopstick(i);
            }

            // Initialize philosophers and assign them chopsticks
            for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
                Chopstick leftChopstick = chopsticks[i];
                Chopstick rightChopstick = chopsticks[(i + 1) % Constants.NUMBER_OF_PHILOSOPHERS];

                philosophers[i] = new Philosopher(i, leftChopstick, rightChopstick);
                executorService.execute(philosophers[i]); // Start philosopher thread
            }

            // Let the simulation run for a fixed duration
            TimeUnit.MILLISECONDS.sleep(Constants.SIMULATION_RUNNING_TIME);

            // Signal philosophers to stop
            for (Philosopher philosopher : philosophers) {
                philosopher.setFull();
            }

        } finally {
            // Gracefully shutdown the executor service
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                TimeUnit.MILLISECONDS.sleep(1000); // Wait until all threads finish execution
            }

            // Print the number of times each philosopher has eaten
            for (Philosopher philosopher : philosophers) {
                System.out.println("Philosopher " + philosopher.getPhilosopherId() + " ate " + philosopher.getEatingCounter() + " times.");
            }
        }
    }
}
