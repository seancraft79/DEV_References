package com.example.testapp.CompletableFutureSample;

import java.util.concurrent.CompletableFuture;

/**
 * 두가지 프로세스를 parallel 하게 동시에 진행하고 결과 값을 조합한 처리를 할때
 */
public class ThenCombine {
    public void thenCombineTest() {
        Price price = new Price();
        CompletableFuture<Double> price1 = price.getPriceAsync(1);
        CompletableFuture<Double> price2 = price.getPriceAsync(2);
        price2.thenCombineAsync(price1, (a, b) -> a + b)
                .thenAcceptAsync(System.out::print);

        System.out.println("Non Blocking!!");
    }

    static class Price {
        public double getPrice(double oldprice) throws Exception {
            return calculatePrice(oldprice);
        }

        public double calculatePrice(double oldprice) throws Exception {
            System.out.println("Input :" + oldprice + " in " + Thread.currentThread().getName());
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
