package com.evanlu.template;

public class StringDisplay extends AbstractDisplay{
    private String string;
    private int width;

    public StringDisplay(String string) {
        this.string = string;
        width = string.getBytes().length;
    }

    @Override
    public void open() {
        prinntLine();
    }

    private void prinntLine() {
        System.out.print("+");
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    @Override
    public void close() {
        prinntLine();
    }

    @Override
    public void print() {
        System.out.println("|" + string +"|");
    }
}
