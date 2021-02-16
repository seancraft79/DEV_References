package com.example.testapp.CompletableFutureSample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture 를 반환하는 Method를 Chain으로 실행하고 싶을때...
 * 즉 이전에 Async 프로세스로 응답 받은 값을 다음 Async 프로세스의 인자로 사용하는 경우에 아래와 같이 thenCompose, thenComposeAsync 를 사용할 수 있다
 */
public class ThenCompose {
    // 비동기처리 Return 값을 다음 처리의 Parameter 로 사용할때 사용한다
    public void thenComposeTest() {
        Price price = new Price();
        price.getPriceAsync(1)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(r -> price.getPriceAsync(r));

        System.out.println("Non Blocking!!");
    }

    static class Price {
        public double getPrice(double oldprice) throws Exception {
            return calculatePrice(oldprice);
        }

        public double calculatePrice(double oldprice) throws Exception {
            System.out.println("Input :" + oldprice);
            Thread.sleep(1000l);
            System.out.println("Output :" + (oldprice + 1l));
            return oldprice + 1l;
        }

        public CompletableFuture<Double> getPriceAsync(double oldPrice) {
            CompletableFuture<Double> completableFuture = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    double price = calculatePrice(oldPrice);
                    completableFuture.complete(price);
                } catch (Exception ex) {
                    completableFuture.completeExceptionally(ex);
                }
            }).start();

            return completableFuture;
        }
    }
}
