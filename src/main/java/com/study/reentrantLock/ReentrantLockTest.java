package com.study.reentrantLock;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhoujian
 * @since 2021/2/22 23:31
 */
public class ReentrantLockTest {
    private static long x = 0L;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(false); // false: 非公平锁， true：公平锁
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                lock.lock();
                for (int i1 = 0; i1 < 10000; i1++) {
                    x++;
                }
                lock.unlock();
            }, String.valueOf(i + 1));
        }
        Arrays.asList(threads).forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(x);
    }

}
