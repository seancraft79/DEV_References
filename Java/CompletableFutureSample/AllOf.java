package com.example.testapp.CompletableFutureSample;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 동시에 n개의 요청을 호출하고 모든 호출이 완성되면 진행하기
 */
public class AllOf {

    private String buildMessage() {
        try {
            Thread.sleep(3 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Completed in " + Thread.currentThread().getName();
    }


    public void allOfTest() {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(this::buildMessage);

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);


        CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture[3]))
                .thenApplyAsync(result -> completableFutures.stream().map(future -> future.join()).collect(Collectors.toList()))
                .thenAcceptAsync(messages -> messages.forEach(message -> System.out.println(message)));

        System.out.println("Non Blocking!!");
    }
}
