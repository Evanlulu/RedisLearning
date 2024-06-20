package com.evanlu.thinkinjava.enumtest;

public class EnumClass {
    enum Shrubbery{ GROUND, CRAWLING, HANGING }

    public static void main(String[] args) {
        for (Shrubbery s : Shrubbery.values()) {
            System.out.println(s + " ordinary: " + s.ordinal());
            System.out.println(s.getDeclaringClass());
            System.out.println(s.name());
            System.out.println("=====================");
        }
        for (String s : "HANGING CRAWLING GROUND".split(" ")) {
            Shrubbery shrubbery = Enum.valueOf(Shrubbery.class, s);
            System.out.println(shrubbery);
        }

    }
}
