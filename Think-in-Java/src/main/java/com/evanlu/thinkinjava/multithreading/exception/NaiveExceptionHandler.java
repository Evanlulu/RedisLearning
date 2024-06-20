package com.evanlu.thinkinjava.multithreading.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NaiveExceptionHandler {
    public static void main(String[] args) {
        try {
            ExecutorService exec  = Executors.newCachedThreadPool();
            exec.execute(new ExceptionThread());
        }catch (RuntimeException e){
            System.out.println("Exception has been handled");
        }
    }
}
