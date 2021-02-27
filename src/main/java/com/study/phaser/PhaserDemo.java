package com.study.phaser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujian
 * @since 2021/2/23 22:13
 */
public class PhaserDemo {
    public static void main(String[] args) {
        MarriagePhaser phaser = new MarriagePhaser();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new People(phaser), "p" + (i + 1));
        }
        List<Thread> threadList = new ArrayList<>(Arrays.asList(threads));
        threadList.add(new Thread(new People(phaser), "新郎"));
        threadList.add(new Thread(new People(phaser), "新娘"));
        threadList.forEach(thread -> {
            thread.start();
            phaser.register();
        });
    }


    static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            if (registeredParties == 0) {
                System.out.println("婚礼结束了。。。。。" + registeredParties);
                System.out.println();
                return super.onAdvance(phase, registeredParties);
            }
            switch (phase) {
                case 0:
                    System.out.println("大家都到了。。。。" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("大家吃完饭了。。。。。" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("大家都走了。。。。。" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("新郎新娘洞房了。。。。。" + registeredParties);
                    System.out.println();
                    return false;
                default:
                    return true;
            }
        }
    }

    static class People implements Runnable {
        private final Phaser phaser;

        public People(Phaser phaser) {
            this.phaser = phaser;
        }

        static void sleepMills(int val) {
            try {
                TimeUnit.MILLISECONDS.sleep(val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String getThreadName() {
            return Thread.currentThread().getName();
        }

        void arrive() {
            sleepMills(500);
            System.out.println(getThreadName() + "=====到了");
            phaser.arriveAndAwaitAdvance();
        }

        void eat() {
            sleepMills(501);
            System.out.println(getThreadName() + "=====吃完了");
            phaser.arriveAndAwaitAdvance();
        }

        void leave() {
            sleepMills(500);
            System.out.println(getThreadName() + "=====离开了");
            phaser.arriveAndAwaitAdvance();
        }

        void hug() {
            String threadName = getThreadName();
            if ("新郎".equals(threadName) || "新娘".equals(threadName)) {
                System.out.println(getThreadName() + "=====洞房了");
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
            if ("新郎".equals(getThreadName()) || "新娘".equals(getThreadName())) {
                phaser.arriveAndDeregister();
            }
        }
    }
}
