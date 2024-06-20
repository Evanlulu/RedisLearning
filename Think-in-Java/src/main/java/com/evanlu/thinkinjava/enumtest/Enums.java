package com.evanlu.thinkinjava.enumtest;

import java.util.Random;

public class Enums {
    private static Random rand = new Random(47);
    public static<T extends Enum<T>> T random(Class<T> ec){
        return  random(ec.getEnumConstants());
    }
    public static <T extends Enum<T>> T random(T[] values){
        return values[rand.nextInt(values.length)];
    }
    enum Activity{
        SITTING, LYING, STANDING, HOPPING, RUNNING, DOGGING, JUMPING, FALLING, FLYING
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.print(Enums.random(Activity.class) + " ");

        }
    }
 }
