package com.study;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author zhoujian
 * @since 2021/2/22 22:47
 */
public class JucTest {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(50, () -> {
            // 每20个线程一组，每组执行一次
            System.out.println("满人，发车");
        });

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    //System.out.println(Thread.currentThread().getName() + "到了");
                    barrier.await();
                    // 每20个线程一组，每组执行20次
                    System.out.println(Thread.currentThread().getName() + "============");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, i + 1 + "").start();
        }
    }
}
