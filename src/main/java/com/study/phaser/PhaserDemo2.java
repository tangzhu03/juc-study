package com.study.phaser;

import java.util.concurrent.Phaser;

/**
 * @author zhoujian
 * @since 2021/2/24 23:34
 */
public class PhaserDemo2 {
    private static final Phaser phaser = new Phaser(2);

    public static void main(String[] args) {
        phaser.arriveAndAwaitAdvance();
        new Thread(phaser::arriveAndAwaitAdvance).start();
        System.out.println("end");

    }
}
