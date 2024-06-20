package com.evanlu.thinkinjava.multithreading;

public class BasicThreads {
    public static void main(String[] args) {
        Thread r = new Thread(new LiftOff());
        r.start();
        System.out.println("Waiting for LiftOff!");
    }
}
