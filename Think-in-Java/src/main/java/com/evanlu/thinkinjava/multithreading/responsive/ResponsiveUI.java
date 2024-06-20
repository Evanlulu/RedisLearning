package com.evanlu.thinkinjava.multithreading.responsive;

public class ResponsiveUI extends Thread{
    public static volatile double d = 1;

    public ResponsiveUI() {
        setDaemon(true);
        start();
    }
    public void run(){
        while (true)
            d = d + (Math.PI + Math.E) / d;

    }

    public static void main(String[] args) throws Exception {
        new ResponsiveUI();
        System.in.read();
        System.out.println(d);
    }
}
