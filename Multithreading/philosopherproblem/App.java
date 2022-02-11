package Multithreading.philosopherproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws InterruptedException{
        ExecutorService executorService = null;
        Philosopher[] philosophers = null;
        Chopstick[] chopsticks = null;

        try {
            executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);
            philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
            chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

            for(int i=0;i<Constants.NUMBER_OF_CHOPSTICKS;i++){
                chopsticks[i] = new Chopstick(i);
            }

            for(int i=0;i<Constants.NUMBER_OF_PHILOSOPHERS;i++){
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i+1) % Constants.NUMBER_OF_PHILOSOPHERS]);
                executorService.execute(philosophers[i]);
            }

            Thread.sleep(Constants.SIMULATION_RUNNING_TIME);

            for(Philosopher philosopher : philosophers){
                philosopher.setFull(true);
            }

        }finally {
            executorService.shutdown();
            while(!executorService.isTerminated()) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            for(Philosopher philosopher : philosophers) {
                System.out.println(philosopher.getId() + " eats " + philosopher.getEatingCounter() + " times");
            }
        }
    }
}
