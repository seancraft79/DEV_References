package com.example.testapp.CompletableFutureSample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * TASK 를 순차적으로 실행시키고 마지막에 "all tasks completed" 를 표시
 */
public class CompletableFutureSample {
    Runnable task = () -> {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("TASK completed in " + Thread.currentThread().getName());
    };

    public void completableFuture() {
        CompletableFuture
                .runAsync(task)
                .thenCompose(aVoid -> CompletableFuture.runAsync(task))
                .thenAcceptAsync(aVoid -> System.out.println("all tasks completed!!"))
                .exceptionally(throwable -> {
                    System.out.println("exception occurred!!");
                    return null;
                });
        System.out.println("Non Blocking!!");
    }
}
