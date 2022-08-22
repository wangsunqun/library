package com.wsq.library.quasar;

import co.paralleluniverse.fibers.Fiber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class QuasarApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(QuasarApplication.class, args);

        CountDownLatch countDownLatch = new CountDownLatch(10000);
        long start = System.currentTimeMillis();

        // 线程
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10000; i++) {
            executor.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        System.out.println("thread use:" + (System.currentTimeMillis() - start) + " ms");

        CountDownLatch countDownLatch2 = new CountDownLatch(10000);
        long start2 = System.currentTimeMillis();

        // 协程
        for (int i = 0; i < 10000; i++) {
            new Fiber<>(() -> {
                Fiber.sleep(1000);
                countDownLatch2.countDown();
            }).start();
        }

        countDownLatch2.await();
        System.out.println("fiber use:" + (System.currentTimeMillis() - start2) + " ms");
    }

}
