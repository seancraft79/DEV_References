package com.example.testapp.CompletableFutureSample;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 동시에 n개의 요청을 호출하고 하나라도 호출이 완성되면 진행하기
 */
public class AnyOf {
    private String buildMessage(int index) {
        try {
            Thread.sleep(3 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Completed!! [" + index + "] in " + Thread.currentThread().getName();
    }

    public void anyOfTest() {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> buildMessage(1));
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> buildMessage(2));
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> buildMessage(3));

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture
                .anyOf(completableFutures.toArray(new CompletableFuture[3]))
                .thenAcceptAsync(result -> System.out.println(result));

        System.out.println("Non Blocking!!");
    }
}
