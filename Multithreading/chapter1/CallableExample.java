package Multithreading.chapter1;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableExample {

    public static void main(String[] args) {

        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(1000);
            return "current time :: " + LocalDateTime.now();
        };

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Callable<String>> tasksList = Arrays.asList(callableTask, callableTask, callableTask);

//        1. execute tasks list using invokeAll() method
        try {
            List<Future<String>> results = executorService.invokeAll(tasksList);
            for (Future<String> result : results) {
                System.out.println(result.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

//        2. execute individual tasks using submit() method
        Future<String> result = executorService.submit(callableTask);
        while (!result.isDone()) {
            try{
                System.out.println("Method returned value : " + result.get());
                break;
            }catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
}

