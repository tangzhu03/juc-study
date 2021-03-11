package com.study.cyclicBarrier;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujian
 * @since 2021/2/28 23:59
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        Thread[] threads = new Thread[12];
        CyclicBarrier barrier = new CyclicBarrier(6, () -> System.out.println("满人，发车。。。"));


        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    sleepMillis(500);
                    System.out.println(Thread.currentThread().getName() + "就绪。。");
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                sleepMillis(500);
                System.out.println(Thread.currentThread().getName() + "执行。。");
            }, "p" + (i + 1));
        }


        Arrays.asList(threads).forEach(thread -> {
            thread.start();
        });
    }

    public static void sleepMillis(int val) {
        try {
            TimeUnit.MILLISECONDS.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
