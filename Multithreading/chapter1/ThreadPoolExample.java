package Multithreading.chapter1;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {

    public static void main(String[] args) {
        runUsingSingleThreadPool();
//        runUsingFixedThreadPool();
//        runUsingCachedThreadPool();
//        runUsingScheduledThreadPool();
    }

    public static void runUsingSingleThreadPool() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for(int i=0; i<5; i++) {
            singleThreadExecutor.submit(new Task(i));
        }
        // SHUTDOWN EXECUTOR (otherwise process will never finish)

        // prevent executor to execute further tasks
        singleThreadExecutor.shutdown();
        // wait for 1 second and terminate actual running tasks
        try{
            if(singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                singleThreadExecutor.shutdownNow();
            }
        }catch (InterruptedException e) {
            singleThreadExecutor.shutdownNow();
        }

    }

    public static void runUsingFixedThreadPool() {
        ExecutorService poolThreadExecutor = Executors.newFixedThreadPool(5);
        for(int i=0; i<5; i++) {
            poolThreadExecutor.submit(new Task(i));
        }
        // we have to shut down the executor
        poolThreadExecutor.shutdown();

        try{
            if(poolThreadExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                poolThreadExecutor.shutdownNow();
            }
        }catch (InterruptedException e) {
            poolThreadExecutor.shutdownNow();
            e.printStackTrace();
        }
    }

    public static void runUsingCachedThreadPool() {
        ExecutorService cachedThreadPoolExecutor = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++) {
            cachedThreadPoolExecutor.submit(new Task(i));
        }

        cachedThreadPoolExecutor.shutdown();
        try{
            if(cachedThreadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                cachedThreadPoolExecutor.shutdownNow();
            }
        }catch (InterruptedException e) {
            cachedThreadPoolExecutor.shutdownNow();
            e.printStackTrace();
        }
    }

    public static void runUsingScheduledThreadPool() {
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
        scheduledExecutor.scheduleAtFixedRate(new Task(1), 1000, 5000, TimeUnit.MILLISECONDS);

        scheduledExecutor.shutdown();

        try{
            if(scheduledExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        }catch (InterruptedException e) {
            scheduledExecutor.shutdownNow();
            e.printStackTrace();
        }
    }
}

class Task implements Runnable {
    private int id;

    public Task(int id) {
        this.id = id;
    }

    public void run() {
        System.out.println("Task with id " + id + " is in progress. Thread id " + Thread.currentThread().getName());
        long duration = (long) (Math.random() * 2);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
