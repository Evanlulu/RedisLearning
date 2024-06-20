package com.evanlu.template;

public class Mian {
    public static void main(String[] args) {
        AbstractDisplay test1 = new CharDisplay('t');
        AbstractDisplay test2 = new StringDisplay("test1");
        AbstractDisplay test3 = new StringDisplay("qqqqqqqqqqqqqqqqq");
        test1.display();
        test2.display();
        test3.display();

    }
}
