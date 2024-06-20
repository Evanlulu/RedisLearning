package com.evanlu.iterator;

import java.util.Iterator;

public class BookShelf implements Aggregate{
    private Book[] books;
    private int last = 0;

    public BookShelf(int max) {
        this.books = new Book[max];
    }

    public Book getBookAt(int index){
        return books[index];
    }
    public void appendBook(Book book){
        this.books[last] = book;
        last ++ ;
    }
    public int getLength(){
        return last;
    }

    @Override
    public Iterator iterator() {
        return new BookShelfInterator(this);
    }
}
