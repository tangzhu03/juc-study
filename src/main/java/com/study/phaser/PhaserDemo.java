package com.study.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujian
 * @since 2021/2/23 22:13
 */
public class PhaserDemo {
    private final Random r = new Random();
    private final MarriagePhaser marriagePhaser = new MarriagePhaser();


    void holdWedding() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Person("p" + i)).start();
        }
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }
    public static void main(String[] args) {
        PhaserDemo phaserDemo = new PhaserDemo();
        phaserDemo.marriagePhaser.bulkRegister(12);
        phaserDemo.holdWedding();

    }

    public void sleepMills(int mills) {
        try {
            TimeUnit.MILLISECONDS.sleep(r.nextInt(mills));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐了。。" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("所有人开始吃饭。。" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("所有人离开了。。" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("新郎新娘洞房了。。" + registeredParties);
                    System.out.println();
                    return true;
                default:
                    return true;
            }
        }
    }

    class Person implements Runnable {
        private final String name;

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            sleepMills(1000);
            System.out.println(name + "到了。。。");
            marriagePhaser.arriveAndAwaitAdvance();
        }

        public void eat() {
            sleepMills(1000);
            System.out.println(name + "开始吃饭。。。");
            marriagePhaser.arriveAndAwaitAdvance();
        }

        public void leave() {
            sleepMills(1000);
            System.out.println(name + "离开了。。。");
            marriagePhaser.arriveAndAwaitAdvance();
        }

        public void hug() {
            sleepMills(1000);
            if (name.equals("新郎") || name.equals("新娘")) {
                System.out.println(name + "洞房了。。。。");
                marriagePhaser.arriveAndAwaitAdvance();
            } else {
                marriagePhaser.arriveAndDeregister();
            }

        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
        }
    }
}
