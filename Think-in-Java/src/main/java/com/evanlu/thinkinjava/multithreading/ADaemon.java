package com.evanlu.thinkinjava.multithreading;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class ADaemon implements Runnable{
    @Override
    public void run() {
        try{
            System.out.println("Starting ADaemon");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("This should always run? ");
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new ADaemon());
//        t.setDaemon(true); 有註解調才會 打印 starting
        t.start();
    }
}
